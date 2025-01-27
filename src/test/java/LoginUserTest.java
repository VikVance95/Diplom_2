import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {

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
    @DisplayName("Авторизация с данными существующего пользователя")
    @Description("Авторизация с данными существующего пользователя, возвращает 200 OK")
    public void loginUnderExistingUserTest() {
        testUserSteps.userUniqueRegistration();
        testUserSteps.userLogIn();
        testUserSteps.getResponse()
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином и паролем")
    @Description("Авторизация пользователя с неверным логином и паролем, возвращает 401 Unauthorized")
    public void loginWithIncorrectLoginAndPasswordTest() {
        testUserSteps.userLogIn();
        testUserSteps.getResponse()
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }
}
