import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import constants.Endpoints;
import steps.UserSteps;
import model.User;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.core.IsEqual.equalTo;

public class CreateUserTest extends Endpoints {

    UserSteps userSteps;
    User user;

    Faker faker = new Faker();

    String randomEmail = faker.internet().emailAddress().toLowerCase();
    String randomPassword = faker.internet().password();
    String randomName = faker.name().fullName();


    @After
    @Step("Удаление пользователя")
    public void cleanUp(){
        userSteps.deleteUser();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("проверка создания уникального пользователя")
    public void createUniqueUserTest(){
        userSteps = new UserSteps();
        user = new User(randomEmail, randomPassword, randomName);
        Response response = userSteps.createUser(user);
        response.then().statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание идентичного пользователя")
    @Description("проверка невозможности создания идентичного пользователя")
    public void cantCreateSameUserTest(){
        userSteps = new UserSteps();
        user = new User(randomEmail, randomPassword, randomName);
        Response response1 = userSteps.createUser(user);
        Response response2 = userSteps.createUser(user);
        response2.then().statusCode(SC_FORBIDDEN)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без почты")
    @Description("проверка того, что невозможно создать пользователя без указания почты")
    public void cantCreateNoEmailUserTest(){
        userSteps = new UserSteps();
        user = new User(null, randomPassword, randomName);
        Response response = userSteps.createUser(user);
        response.then().statusCode(SC_FORBIDDEN)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("проверка того, что невозможно создать пользователя без указания пароля")
    public void cantCreateNoPasswordUserTest(){
        userSteps = new UserSteps();
        user = new User(randomEmail, null, randomName);
        Response response = userSteps.createUser(user);
        response.then().statusCode(SC_FORBIDDEN)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("проверка того, что невозможно создать пользователя без указания имени")
    public void cantCreateNoNameUserTest(){
        userSteps = new UserSteps();
        user = new User(randomEmail, randomPassword, null);
        Response response = userSteps.createUser(user);
        response.then().statusCode(SC_FORBIDDEN)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}