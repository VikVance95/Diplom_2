package constants;

import io.restassured.RestAssured;

public class Endpoints {
    protected static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    public static final String CREATE_ORDER_URI = "/api/orders";
    public static final String CREATE_USER_URI = "/api/auth/register";
    public static final String USER_URI = "/api/auth/user";
    public static final String LOGIN_USER_URI = "/api/auth/login";


    public Endpoints(){
        RestAssured.baseURI = BASE_URI;
    }
}