package com.bguerradev.qa.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import com.bguerradev.qa.utilities.ConfigReader;

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
                .log(LogDetail.ALL)
                .build();
    }

}