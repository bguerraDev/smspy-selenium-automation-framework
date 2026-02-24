package api;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.awaitility.Awaitility.await;

import io.qameta.allure.Allure;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import utilities.ConfigReader;

public class BaseClient {

    public static RequestSpecification baseSpec;
    protected static RequestSpecification authSpec;

    static {
        // Read config
        String baseUrl = ConfigReader.getProperty("api.base.url");

        // Base Spec (Public)
        baseSpec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                // We use our custom masking filter instead of the default AllureRestAssured
                .addFilter(new AllureMaskingFilter())
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * returns a RequestSpecification with Bearer token header
     */
    public static RequestSpecification getAuthSpec(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(baseSpec)
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }

    public static Response executeWithColdStartRetry(Supplier<Response> requestSupplier) {
        return await()
                .atMost(120, TimeUnit.SECONDS)
                .pollInterval(4, TimeUnit.SECONDS)
                .ignoreExceptions() // optional: ignore connection refused etc.
                .until(requestSupplier::get,
                        response -> response.getStatusCode() < 500 && response.getStatusCode() != 0);
    }

    /**
     * Custom Filter to log requests to Allure while masking sensitive data
     * (passwords).
     */
    public static class AllureMaskingFilter implements Filter {
        @Override
        public Response filter(
                FilterableRequestSpecification requestSpec,
                FilterableResponseSpecification responseSpec,
                FilterContext ctx) {

            // 1. Capture request details
            String method = requestSpec.getMethod();
            String uri = requestSpec.getURI();
            Object body = requestSpec.getBody();
            String requestBodyStr = (body != null) ? body.toString() : "";

            // 2. MASK PASSWORD using regex
            // Matches: "password":"...", "password" : "...", etc.
            String maskedBody = requestBodyStr.replaceAll("(\"password\"\\s*:\\s*\")[^\"]+(\")", "$1***$2");

            // 3. Attach MASKED Request to Allure
            Allure.addAttachment("API Request", "application/json",
                    "Method: " + method + "\nURL: " + uri + "\n\nBody:\n" + maskedBody);

            // 4. Proceed with ORIGINAL request (so backend gets real password)
            Response response = ctx.next(requestSpec, responseSpec);

            // 5. Attach Response to Allure
            Allure.addAttachment("API Response", "application/json",
                    "Status: " + response.getStatusCode() + "\n\nBody:\n" + response.asPrettyString());

            return response;
        }
    }
}
