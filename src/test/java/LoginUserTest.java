import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import constants.Endpoints;
import steps.UserSteps;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class LoginUserTest extends Endpoints {

    UserSteps userSteps;
    User user;

    Faker faker = new Faker();

    String randomEmail = faker.internet().emailAddress().toLowerCase();
    String randomPassword = faker.internet().password();
    String randomName = faker.name().fullName();

    @Before
    @Step("Создание пользователя")
    public void setUp(){
        userSteps = new UserSteps();
        user = new User(randomEmail, randomPassword, randomName);
        Response response = userSteps.createUser(user);
    }

    @After
    @Step("Удаление пользователя")
    public void cleanUp(){
        userSteps.deleteUser();
    }

    @Test
    @DisplayName("Успешная авторизация")
    @Description("проверка успешной авторизации пользователя")
    public void loginCorrectDataTest(){
        user = new User(user.getEmail(), user.getPassword());
        Response response = userSteps.loginUser(user);
        response.then().statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неверной почтой")
    @Description("проверка того, что нельзя авторизоваться с неверной почтой")
    public void cantLoginIncorrectEmailTest(){
        user = new User("1" +user.getEmail(), user.getPassword());
        Response response = userSteps.loginUser(user);
        response.then().statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("проверка того, что нельзя авторизоваться с неверным паролем")
    public void cantLoginIncorrectPasswordTest(){
        user = new User(user.getEmail(), "1" + user.getPassword());
        Response response = userSteps.loginUser(user);
        response.then().statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}