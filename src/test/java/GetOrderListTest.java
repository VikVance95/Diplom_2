import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import constants.Endpoints;
import steps.OrderSteps;
import steps.UserSteps;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.core.IsEqual.equalTo;

public class GetOrderListTest extends Endpoints {

    UserSteps userSteps;
    User user;

    Faker faker = new Faker();

    String randomEmail = faker.internet().emailAddress().toLowerCase();
    String randomPassword = faker.internet().password();
    String randomName = faker.name().fullName();

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
    @DisplayName("Получение списка заказов авторизованного пользователя")
    @Description("проверка получения списка заказов авторизированным пользователем")
    public void getOrdersListAuthorizedUserTest(){
        OrderSteps orderSteps = new OrderSteps();
        user = new User(user.getEmail(), user.getPassword());
        userSteps.loginUser(user);
        orderSteps.accessToken = userSteps.accessToken;
        Response response = orderSteps.getOrdersList();
        response.then()
                .log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение списка заказов не авторизованного пользователя")
    @Description("проверка того, что невозможно получить список заказов без авторизации")
    public void getOrdersListUnauthorizedUserTest(){
        OrderSteps orderSteps = new OrderSteps();
        orderSteps.accessToken = userSteps.accessToken;
        Response response = orderSteps.getOrdersListUnauthorized();
        response.then()
                .log().all()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}