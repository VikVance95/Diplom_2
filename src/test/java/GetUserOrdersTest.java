import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.List;

import static constants.Data.*;

public class GetUserOrdersTest {
    private UserSteps userSteps;
    private Order order;
    private OrderSteps orderSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        userSteps.createUser(RANDOM_EMAIL, RANDOM_PASS, RANDOM_NAME);
        ValidatableResponse responseLogin = userSteps.login(RANDOM_EMAIL, RANDOM_PASS);
        accessToken = userSteps.getAccessToken(responseLogin);
        order = new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa72"));
        orderSteps.createOrderWithToken(accessToken, order);
    }

    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя")
    public void getListOfOrdersAuthSuccess() {
        ValidatableResponse responseGetList = orderSteps.listOfOrdersWithToken(accessToken);
        userSteps.checkAnswerSuccess(responseGetList);
    }

    @Test
    @DisplayName("Получение списка заказов не авторизованного пользователя")
    public void getListOfOrdersNonAuthUnauthorized() {
        ValidatableResponse responseGetList = orderSteps.listOfOrdersWithoutToken();
        orderSteps.checkAnswerGetListNonAuth(responseGetList);
    }

    @After
    public void close() {
        userSteps.deletingUsersAfterTests(accessToken);
    }
}
