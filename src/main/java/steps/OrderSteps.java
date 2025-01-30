package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;
import static constants.Endpoints.*;

public class OrderSteps {
    public String accessToken;

    @Step("Создание заказа")
    public Response createOrder(Order order){
        return
                given()
                        .header("Authorization", accessToken)
                        .header("Content-type", "application/json")
                        .body(order)
                        .when()
                        .post(CREATE_ORDER_URI);
    }

    @Step("Получение списка заказов авторизованного пользователя")
    public  Response getOrdersList(){
        return
                given()
                        .header("Authorization", accessToken)
                        .get(CREATE_ORDER_URI);
    }

    @Step("Получение списка заказов не авторизованного пользователя")
    public  Response getOrdersListUnauthorized(){
        return
                given().get(CREATE_ORDER_URI);
    }
}