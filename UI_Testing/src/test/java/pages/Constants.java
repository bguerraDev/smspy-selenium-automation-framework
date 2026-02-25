package pages;

import org.openqa.selenium.By;
import utilities.ConfigReader;

public class Constants {

    /**
     * URLs from smspyfrontend
     */
    public static String getUrlBase() {
        return ConfigReader.getBaseUrlLogin();
    }

    public static String getUrlMessagesInbox() {
        return ConfigReader.getBaseUrlMessages();
    }

    public static String getUrlMessagesSend() {
        return ConfigReader.getBaseUrlMessagesSend();
    }

    public static String getUrlProfile() {
        return ConfigReader.getBaseUrlProfile();
    }

    /**
     * Selectors
     */
    public static final By INPUT_USERNAME = By.cssSelector("input[type='text']");
    public static final By INPUT_PASSWORD = By.cssSelector("input[type='password']");
    public static final By BUTTON_LOGIN = By.cssSelector("button[type='submit']");
    public static final By H2_TITLE_MESSAGE_LIST = By.xpath("//h2[contains(text(), 'Mensajes')]");
    public static final By BUTTON_SEND_MESSAGE = By.xpath("//button[contains(text(), 'Enviar Mensaje')]");
    public static final By BUTTON_MY_PROFILE = By.xpath("//button[contains(text(), 'Mi Perfil')]");
    public static final By RECEIVER_SELECT = By.tagName("select");
    public static final By RECEIVER_SELECT_LIST_USERS = By.xpath("//select/option[not(@disabled)]");
    public static final By INPUT_MESSAGE = By.tagName("textarea");
    public static final By LABEL_FILE_UPLOAD = By.xpath("//label[@for='file-upload']");
    public static final By INPUT_IMAGE = By.id("file-upload");
    public static final By INPUT_IMAGE_SPAN = By.xpath("//label[@for='file-upload']/following-sibling::span");
    public static final By BUTTON_FORM_SEND = By.xpath("//button[contains(text(), 'Enviar')]");
    public static final By SPAN_MESSAGE_SENT = By.xpath("//span[contains(text(), 'Mensaje enviado')]");
    public static final By TOAST_SUCCESS = By.xpath("//div[contains(@class, 'bg-green-600') and contains(text(), 'Mensaje enviado')]");
    public static final By TOAST_CLOSE_BUTTON = By.xpath("//button[text()='✕' and contains(@class, 'ml-4')]");
    public static final By BUTTON_VOLVER = By.xpath("//button[contains(text(), 'Volver')]");
    public static final By LIST_MESSAGES_EMPTY_RECEIVED = By.xpath("//div[contains(p, 'No hay mensajes recibidos')]");
    public static final By LIST_MESSAGES_EMPTY_SENT = By.xpath("//div[contains(p, 'No hay mensajes enviados')]");
    public static final By LIST_MESSAGES_RECEIVED = By.xpath("//h3[contains(text(), 'Mensajes recibidos')]/following-sibling::div");
    public static final By LIST_MESSAGES_SENT = By.xpath("//h3[contains(text(), 'Mensajes enviados')]/following-sibling::div");
    public static final By FIRST_MESSAGE_SENT_HEADER_USER = By.xpath("//h3[contains(text(), 'Mensajes enviados')]/following-sibling::div[1]//h5");
    public static final By FIRST_MESSAGE_SENT_HEADER_CONTENT = By.xpath("//h3[contains(text(), 'Mensajes enviados')]/following-sibling::div[1]//p");
    public static final By FIRST_MESSAGE_SENT_HEADER_IMAGE_S3 = By.xpath("//h3[contains(text(), 'Mensajes enviados')]/following-sibling::div[1]//img[contains(@src, 'amazonaws')]");
    public static final By TITLE_MI_PERFIL = By.xpath("//h2[contains(text(), 'Mi perfil')]");
    public static final By PROFILE_USERNAME_INPUT = By.xpath("//input[@type='text']");
    public static final By UPLOAD_IMAGE_AVATAR = By.id("avatar-upload");
    public static final By UPLOAD_IMAGE_AVATAR_SPAN = By.xpath("//input[@id='file-upload']/following-sibling::span");
    public static final By BUTTON_UPDATE_PROFILE_AVATAR = By.xpath("//button[contains(text(), 'Actualizar')]");
    public static final By SPAN_AVATAR_UPDATED = By.xpath("//span[contains(text(), 'Perfil actualizado')]");
    public static final By IMAGE_URL_AVATAR_S3 = By.xpath("//img[contains(@src, 'amazonaws')]");
    public static final By BUTTON_GO_TO_MY_MESSAGES = By.xpath("//button[contains(text(), 'Ir a mis mensajes')]");
    public static final By BUTTON_LOGOUT = By.xpath("//button[contains(text(), 'Cerrar sesión')]");
}