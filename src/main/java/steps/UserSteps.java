package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;

import static io.restassured.RestAssured.given;
import static constants.Endpoints.*;

public class UserSteps {
    public Response handleCreate;
    public Response handleLogin;
    public String accessToken;


    @Step("Создание пользователя")
    public Response createUser(User user) {
        handleCreate =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user)
                        .when()
                        .post(CREATE_USER_URI);
        accessToken = handleCreate.then()
                .extract().path("accessToken");
        return handleCreate;
    }

    @Step("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete(USER_URI)
                    .then()
                    .log().all();
        }
    }

    @Step("Авторизация пользователя")
    public Response loginUser(User user){
        handleLogin =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user)
                        .when()
                        .post(LOGIN_USER_URI);
        accessToken = handleLogin.then()
                .extract().path("accessToken");
        return handleLogin;
    }

    @Step("Изменение почты авторизованного пользователя")
    public Response changeEmailUser(User user1){
        return
                given()
                        .header("Authorization", accessToken)
                        .header("Content-type", "application/json")
                        .body(user1)
                        .when()
                        .patch(USER_URI);
    }

    @Step("Изменение почты не авторизованного пользователя")
    public Response changeEmailUnauthorizedUser(User user1){
        return
                given()
                        .header("Content-type", "application/json")
                        .body(user1)
                        .when()
                        .patch(USER_URI);
    }


}