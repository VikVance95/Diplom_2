import com.github.javafaker.Faker;
import constants.Endpoints;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;


import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.core.IsEqual.equalTo;


public class CreateOrderTest extends Endpoints {

    UserSteps userSteps;
    User user;

    Faker faker = new Faker();

    String randomEmail = faker.internet().emailAddress().toLowerCase();
    String randomPassword = faker.internet().password();
    String randomName = faker.name().fullName();

    public static final String ING_1 = "61c0c5a71d1f82001bdaaa6d";
    public static final String ING_2 = "61c0c5a71d1f82001bdaaa6f";

    @Before
    @Step("Создание пользователя")
    public void setUp() {
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
    @DisplayName("Создание заказа авторизованного пользователя")
    @Description("проверка создания заказа с корректными ингредиентами авторизированным пользователем")
    public void createOrderCorrectIngridsAutorizedUser() {
        OrderSteps orderSteps = new OrderSteps();
        user = new User(user.getEmail(), user.getPassword());
        userSteps.loginUser(user);
        orderSteps.accessToken = userSteps.accessToken;
        List<String> ingredients = java.util.List.of(ING_1,ING_2);
        Order order = new Order(ingredients);
        Response response1 = orderSteps.createOrder(order);
        response1.then()
                .log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа не авторизованного пользователя")
    @Description("проверка создания заказа с корректными ингредиентами неавторизированным пользователем")
    public void createOrderCorrectIngridsUnautorizedUser() {
        OrderSteps orderSteps = new OrderSteps();
        orderSteps.accessToken = userSteps.accessToken;
        List<String> ingredients = java.util.List.of(ING_1,ING_2);
        Order order = new Order(ingredients);
        Response response1 = orderSteps.createOrder(order);
        response1.then()
                .log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя без ингрединетов")
    @Description("проверка создания заказа без ингредиентов авторизированным пользователем")
    public void createOrderWithoutIngridsAutorizedUser() {
        OrderSteps orderSteps = new OrderSteps();
        user = new User(user.getEmail(), user.getPassword());
        userSteps.loginUser(user);
        orderSteps.accessToken = userSteps.accessToken;
        List<String> ingredients = java.util.List.of();
        Order order = new Order(ingredients);
        Response response1 = orderSteps.createOrder(order);
        response1.then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя с некорректными ингредиентами")
    @Description("проверка создания заказа с некорректными ингредиентами авторизированным пользователем")
    public void createOrderIncorrectIngridsAutorizedUser() {
        OrderSteps orderSteps = new OrderSteps();
        user = new User(user.getEmail(), user.getPassword());
        userSteps.loginUser(user);
        orderSteps.accessToken = userSteps.accessToken;
        List<String> ingredients = java.util.List.of(ING_1 + "1",ING_2);
        Order order = new Order(ingredients);
        Response response1 = orderSteps.createOrder(order);
        response1.then()
                .log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}