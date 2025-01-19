import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static constants.Data.*;

public class UpdateUserDataTest {

    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Обновление данных пользователя с авторизацией")
    public void changingDataWithAuthPossible() {
        ValidatableResponse responseCreate = userSteps.createUser(RANDOM_EMAIL, RANDOM_PASS, RANDOM_NAME);
        userSteps.checkAnswerSuccess(responseCreate);
        ValidatableResponse responseLogin = userSteps.login(RANDOM_EMAIL, RANDOM_PASS);
        userSteps.checkAnswerSuccess(responseLogin);
        accessToken = userSteps.getAccessToken(responseLogin);
        ValidatableResponse responseChangeWithToken = userSteps.authorizationWithToken(accessToken, "x" + RANDOM_EMAIL, "x" + RANDOM_PASS, "x" + RANDOM_NAME);
        userSteps.checkAnswerSuccess(responseChangeWithToken);
    }

    @Test
    @DisplayName("Обновление данных пользователя без авторизации")
    public void changingDataWithoutAuthNotPossible() {
        ValidatableResponse responseCreate = userSteps.createUser(RANDOM_EMAIL, RANDOM_PASS, RANDOM_NAME);
        userSteps.checkAnswerSuccess(responseCreate);
        ValidatableResponse responseLogin = userSteps.login(RANDOM_EMAIL, RANDOM_PASS);
        userSteps.checkAnswerSuccess(responseLogin);
        accessToken = userSteps.getAccessToken(responseLogin);
        ValidatableResponse responseChangeWithoutToken = userSteps.authorizationWithoutToken("x" + RANDOM_EMAIL, "x" + RANDOM_PASS, "x" + RANDOM_NAME);
        userSteps.checkAnswerWithoutToken(responseChangeWithoutToken);
    }

    @After
    public void close() {
        userSteps.deletingUsersAfterTests(accessToken);
    }

}
