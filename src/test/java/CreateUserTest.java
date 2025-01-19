import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static constants.Data.*;

public class CreateUserTest {

    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    public void createUniqueUserSuccess() {
        ValidatableResponse responseCreate = userSteps.createUser(RANDOM_EMAIL, RANDOM_PASS, RANDOM_NAME);
        userSteps.checkAnswerSuccess(responseCreate);
        accessToken = userSteps.getAccessToken(responseCreate);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createDuplicationUserForbidden() {
        ValidatableResponse responseCreate = userSteps.createUser(RANDOM_EMAIL, RANDOM_PASS, RANDOM_NAME);
        accessToken = userSteps.getAccessToken(responseCreate);
        ValidatableResponse responseIdentical = userSteps.createUser(RANDOM_EMAIL, RANDOM_PASS, RANDOM_NAME);
        userSteps.checkAnswerAlreadyExist(responseIdentical);
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailForbidden() {
        ValidatableResponse responseCreate = userSteps.createUser("", RANDOM_PASS, RANDOM_NAME);
        userSteps.checkAnswerForbidden(responseCreate);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordForbidden() {
        ValidatableResponse responseCreate = userSteps.createUser(RANDOM_EMAIL, "", RANDOM_NAME);
        userSteps.checkAnswerForbidden(responseCreate);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameForbidden() {
        ValidatableResponse responseCreate = userSteps.createUser(RANDOM_EMAIL, RANDOM_PASS, "");
        userSteps.checkAnswerForbidden(responseCreate);
    }

    @After
    public void close() {
        userSteps.deletingUsersAfterTests(accessToken);
    }
}
