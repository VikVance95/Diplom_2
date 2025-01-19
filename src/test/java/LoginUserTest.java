import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static constants.Data.*;

public class LoginUserTest {

    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        ValidatableResponse responseCreate = userSteps.createUser(RANDOM_EMAIL, RANDOM_PASS, RANDOM_NAME);
        accessToken = userSteps.getAccessToken(responseCreate);
    }

    @Test
    @DisplayName("Успешный логин пользователя")
    public void loginUserSuccess() {
        ValidatableResponse responseLogin = userSteps.login(RANDOM_EMAIL, RANDOM_PASS);
        userSteps.checkAnswerSuccess(responseLogin);
    }

    @Test
    @DisplayName("Логин пользователя с неверным email")
    public void loginUserWithWrongEmailUnauthorized() {
        ValidatableResponse responseLogin = userSteps.login("wrongEmail@yandex.ru", RANDOM_PASS);
        userSteps.checkAnswerWithWrongData(responseLogin);
    }

    @Test
    @DisplayName("Логин пользователя с неверным паролем")
    public void loginUserWithWrongPassUnauthorized() {
        ValidatableResponse responseLogin = userSteps.login(RANDOM_EMAIL, "123456");
        userSteps.checkAnswerWithWrongData(responseLogin);
    }

    @After
    public void close() {
        userSteps.deletingUsersAfterTests(accessToken);
    }
}
