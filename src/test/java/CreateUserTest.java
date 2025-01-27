import constants.BaseSpec;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {

    UserSteps testUserSteps = new UserSteps();
    User testUser = new User();

    @Before
    public void setUp() {
        testUser.getData();
    }

    @After
    public void tearDown() {
        testUserSteps.delete();
    }

    @Test
    @DisplayName("Регистрация нового уникального пользователя")
    @Description("Регистрация нового уникального пользователя, возвращает 200 OK")
    public void userCreationIsPossible() {
        testUserSteps.userUniqueRegistration();
        testUserSteps.getResponse()
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Регистрация не уникального пользователя")
    @Description("Регистрация двух пользователей с одинаковыми данными, возвращает 403 Forbidden")
    public void registerDoppelgangerUserTest() {
        testUserSteps.userUniqueRegistration();
        testUserSteps.userUniqueRegistration();
        testUserSteps.getResponse()
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("")
    @Description("Регистрация пользователя без email невозможна, возвращает 403 Forbidden")
    public void userCreationWithoutEmailNotPossibleTest() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("password", testUser.getPassword());
        dataMap.put("name", testUser.getName());
        testUserSteps.setResponse(given()
                .spec(BaseSpec.getBaseSpec())
                .body(dataMap)
                .when()
                .post("auth/register"));
        testUserSteps.getResponse()
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("")
    @Description("Регистрация пользователя без пароля невозможна, возвращает 403 Forbidden")
    public void userCreationWithoutPasswordNotPossibleTest() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("password", testUser.getEmail());
        dataMap.put("name", testUser.getName());
        testUserSteps.setResponse(given()
                .spec(BaseSpec.getBaseSpec())
                .body(dataMap)
                .when()
                .post("auth/register"));
        testUserSteps.getResponse()
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация пользователя без имени")
    @Description("Регистрация пользователя без имени невозможна, возвращает 403 Forbidden")
    public void userCreationWithoutNameNotPossibleTest() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("password", testUser.getEmail());
        dataMap.put("name", testUser.getPassword());
        testUserSteps.setResponse(given()
                .spec(BaseSpec.getBaseSpec())
                .body(dataMap)
                .when()
                .post("auth/register"));
        testUserSteps.getResponse()
                .then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
