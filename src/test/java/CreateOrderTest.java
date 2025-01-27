import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {
    UserSteps testUserSteps = new UserSteps();
    User testUser = new User();
    OrderSteps testOrderSteps = new OrderSteps();

    @Before
    public void setUp() {
        testUser.getData();
        testOrderSteps.setIngredientsList();
    }

    @After
    public void tearDown() {
        testUserSteps.delete();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Создание заказа с авторизацией, возвращает 200 ОК")
    public void creatingOrderWithAuthorizationTest() {
        testUserSteps.userUniqueRegistration();
        testUserSteps.setAccessToken();
        testOrderSteps.createOrderAuth(testUserSteps.getAccessToken());
        testOrderSteps.getOrderResponse()
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .and()
                .body("name", notNullValue());

    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Создание заказа без авторизации, возвращает 401 Unauthorized")
    public void creatingOrderUnauthTest() {
        testUserSteps.userUniqueRegistration();
        testOrderSteps.createOrderUnauth();
        testOrderSteps.getOrderResponse()
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Создание заказа без ингредиентов, возвращает 400 Bad Request")
    public void orderCreationWithoutIngredientTest() {
        testUserSteps.userUniqueRegistration();
        testUserSteps.setAccessToken();
        testOrderSteps.createOrderNoIngredient(testUserSteps.getAccessToken());
        testOrderSteps.getOrderResponse()
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Создание заказа с неверным хешем ингредиентов, возвращает 500 Internal Server Error")
    public void orderCreationWithInvalidIngredientHashTest() {
        testUserSteps.userUniqueRegistration();
        testUserSteps.setAccessToken();
        testOrderSteps.createOrderWithInvalidIngredientHash(testUserSteps.getAccessToken());
        testOrderSteps.getOrderResponse()
                .then()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Получение заказов конкретного авторизованного пользователя, возвращает 200 ОК")
    public void getOrdersByAuthorizedUserTest() {
        testUserSteps.userUniqueRegistration();
        testUserSteps.setAccessToken();
        testOrderSteps.createOrderAuth(testUserSteps.getAccessToken());
        testOrderSteps.getOrderAuth(testUserSteps.getAccessToken());
        testOrderSteps.getOrderResponse()
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Получение заказов конкретного неавторизованного пользователя, возвращает 401 Unauthorized")
    public void getOrdersByUnauthorizedUserTest() {
        testOrderSteps.getOrderUnauth();
        testOrderSteps.getOrderResponse()
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
