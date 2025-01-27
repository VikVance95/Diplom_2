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

public class UpdateUserDataTest {

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
    @DisplayName("Изменение данных авторизованного пользователя")
    @Description("Изменение данных авторизованного пользователя, возвращает 200 ОК")
    public void changingAuthorizedUserDataTest() {
        testUserSteps.userUniqueRegistration();
        testUserSteps.setAccessToken();
        testUser.getData();
        testUserSteps.userDataRefresh(testUserSteps.getAccessToken());
        testUserSteps.getUserData();
        testUserSteps.getResponse()
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение данных неавторизованного пользователя")
    @Description("Изменение данных неавторизованного пользователя, возвращает 401 Unauthorized")
    public void changingUnauthorizedUserDataTest() {
        testUserSteps.userDataRefreshUnauthorised();
        testUserSteps.getResponse()
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }

}
