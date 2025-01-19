package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import model.Order;

import static constants.Endpoints.ORDERS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class OrderSteps extends Client {
    @Step("Создание заказа без токена")
    public ValidatableResponse createOrderWithoutToken(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Создание заказа с токеном")
    public ValidatableResponse createOrderWithToken(String accessToken, Order order) {
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body(order)
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Получение списка заказов без токена")
    public ValidatableResponse listOfOrdersWithoutToken() {
        return given()
                .spec(getSpec())
                .body("")
                .when()
                .get(ORDERS)
                .then();
    }

    @Step("Получение списка заказов с токеном")
    public ValidatableResponse listOfOrdersWithToken(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body("")
                .when()
                .get(ORDERS)
                .then();
    }

    @Step("Проверка ответа при создании заказа без ингредиентов")
    public void checkAnswerWithoutIngredients(ValidatableResponse validatableResponse) {
        validatableResponse
                .body("success", is(false))
                .statusCode(400);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("Ingredient ids must be provided", actualMessage);
    }

    @Step("Проверка ответа при создании заказа с неверным хэшем")
    public void checkAnswerWithWrongHash(ValidatableResponse validatableResponse) {
        validatableResponse
                .statusCode(500);
    }

    @Step("Проверка ответа при получении списка заказов от неавторизованного пользователя")
    public void checkAnswerGetListNonAuth(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(401);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("You should be authorised", actualMessage);
    }
}
