package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserCredentials;
import org.junit.Assert;

import static constants.Endpoints.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class UserSteps extends Client {

    @Step("Создание уникального пользователя")
    public ValidatableResponse createUser(String email, String password, String name) {
        User user = new User(email, password, name);
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(AUTH_REGISTER)
                .then();

    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .when()
                .delete(AUTH_USER);
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(String email, String password) {
        UserCredentials credentials = new UserCredentials(email, password);
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(AUTH_LOGIN)
                .then();
    }

    @Step("Авторизация с токеном")
    public ValidatableResponse authorizationWithToken(String accessToken, String email, String password, String name) {
        User user = new User(email, password, name);
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body(user)
                .when()
                .patch(AUTH_USER)
                .then();
    }

    @Step("Авторизация без токена")
    public ValidatableResponse authorizationWithoutToken(String email, String password, String name) {
        User user = new User(email, password, name);
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(AUTH_USER)
                .then();
    }


    @Step("Получение токена")
    public String getAccessToken(ValidatableResponse validatableResponse) {
        return validatableResponse.extract().path("accessToken");
    }

    @Step("Проверка тела ответа - (success: true) и статуса при создании, изменении данных пользователя или получении списка заказов - 200")
    public void checkAnswerSuccess(ValidatableResponse validatableResponse) {
        validatableResponse
                .body("success", is(true))
                .statusCode(200);
    }

    @Step("Проверка тела ответа при создании уже существующего пользователя")
    public void checkAnswerAlreadyExist(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(403);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("User already exists", actualMessage);
    }

    @Step("Валидация ответа при регистрации без поля email, password или name")
    public void checkAnswerForbidden(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(403);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("Email, password and name are required fields", actualMessage);
    }

    @Step("Проверка ответа при авторизации с неверными данными")
    public void checkAnswerWithWrongData(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(401);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("email or password are incorrect", actualMessage);
    }

    @Step("Валидация ответа при изменении данных пользователя без токена")
    public void checkAnswerWithoutToken(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(401);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("You should be authorised", actualMessage);
    }

    @Step("Удаление пользователей после тестов")
    public void deletingUsersAfterTests(String accessToken) {
        if (accessToken != null) {
            deleteUser(accessToken);
        } else {
            given().spec(getSpec())
                    .when()
                    .delete(AUTH_USER);
        }
    }

}
