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

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.core.IsEqual.equalTo;

public class UpdateUserDataTest extends Endpoints {

    UserSteps userSteps;
    User user;
    User user1;
    User user2;

    Faker faker = new Faker();

    String randomEmail = faker.internet().emailAddress().toLowerCase();
    String randomPassword = faker.internet().password();
    String randomName = faker.name().fullName();

    String randomEmail2 = "1" + randomEmail;
    String randomPassword2 = "1" + randomPassword;
    String randomName2 = "1" + randomName;

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
    @DisplayName("Изменение почты авторизованного пользователя")
    @Description("проверка обновления почты пользователя при авторизации")
    public void changeEmailAuthorizedUserTest(){
        userSteps.loginUser(user);
        user1 = new User("22" + user.getEmail(), user.getPassword(), user.getName());
        Response response = userSteps.changeEmailUser(user1);
        response.then()
                .log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo("22" + user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Изменение имени авторизованного пользователя")
    @Description("проверка обновления имени пользователя при авторизации")
    public void changeNameAuthorizedUserTest(){
        userSteps.loginUser(user);
        user1 = new User(user.getEmail(), user.getPassword(), "22" + user.getName());
        Response response = userSteps.changeEmailUser(user1);
        response.then()
                .log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo("22" + user.getName()));
    }

    @Test
    @DisplayName("Изменение почты на уже использующуюся")
    @Description("проверка получения ошибки при передаче почты, которая уже используется")
    public void errorChangeSameEmailAuthorizedUserTest(){
        UserSteps userSteps2 = new UserSteps();
        user2 = new User(randomEmail2, randomPassword2, randomName2);
        userSteps2.createUser(user2);
        userSteps.loginUser(user);
        user1 = new User(user2.getEmail(), user.getPassword(), user.getName());
        Response response = userSteps.changeEmailUser(user1);
        response.then()
                .log().all()
                .statusCode(SC_FORBIDDEN)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
        userSteps2.deleteUser();
    }

    @Test
    @DisplayName("Изменение почты не авторизованного пользователя")
    @Description("проверка того, что нельзя изменить почту пользователя без авторизации")
    public void cantChangeEmailUnauthorizedUserTest(){
        user1 = new User("22" + user.getEmail(), user.getPassword(), user.getName());
        Response response = userSteps.changeEmailUnauthorizedUser(user1);
        response.then()
                .log().all()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение имени не авторизованного пользователя")
    @Description("проверка того, что нельзя изменить имя пользователя без авторизации")
    public void cantChangeNameUnauthorizedUserTest(){
        user1 = new User(user.getEmail(), user.getPassword(), "22" + user.getName());
        Response response = userSteps.changeEmailUnauthorizedUser(user1);
        response.then()
                .log().all()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}