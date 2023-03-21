import Utils.GeneratorOrder;
import Utils.GeneratorRandomData;
import client.OrderClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateOrderTest {
    GeneratorOrder generatorOrder = new GeneratorOrder();
    GeneratorRandomData generatorRandomData = new GeneratorRandomData();
    OrderClient orderClient = new OrderClient();
    Order order;
    User user = generatorRandomData.randomUser();
    UserClient userClient = new UserClient();
    String accessToken;

    @DisplayName("Создание пользователя")
    @Before
    public void setUp() {
        ValidatableResponse responseCreated = userClient.createdUser(user);
        accessToken = responseCreated.extract().path("accessToken");
        accessToken = accessToken.substring(7);
    }

    @DisplayName("Создание заказ с ингредиентами но без авторизации")
    @Test
    public void orderCannotBeCreatedWithoutAuthorizationAndWithIngredients() {
        order = generatorOrder.getOrderWithIngredient();
        ValidatableResponse responseCreated = orderClient.createdOrderWithoutAuthorization(order);
        assertFalse(responseCreated.extract().path("success"));
        assertEquals(SC_UNAUTHORIZED, responseCreated.extract().statusCode());
    }

    @DisplayName("Создание заказ без ингредиентов без авторизации")
    @Test
    public void orderCannotBeCreatedWithoutAuthorizationAndWithoutIngredients() {
        order = generatorOrder.getOrderWithoutIngredients();
        ValidatableResponse responseCreated = orderClient.createdOrderWithoutAuthorization(order);
        assertFalse(responseCreated.extract().path("success"));
        assertEquals(SC_BAD_REQUEST, responseCreated.extract().statusCode());
    }

    @DisplayName("Создание заказ с ингредиентами с авторизацией")
    @Test
    public void orderCanBeCreatedWithAuthorizationAndWithIngredients() {
        order = generatorOrder.getOrderWithIngredient();
        ValidatableResponse responseCreated = orderClient.createdOrderWithAuthorization(order, accessToken);
        assertTrue(responseCreated.extract().path("success"));
        assertEquals(SC_OK, responseCreated.extract().statusCode());
    }

    @DisplayName("Создание заказ без ингредиентов с авторизацией")
    @Test
    public void orderCannotBeCreatedWithAuthorizationAndWithoutIngredients() {
        order = generatorOrder.getOrderWithoutIngredients();
        ValidatableResponse responseCreated = orderClient.createdOrderWithAuthorization(order, accessToken);
        assertFalse(responseCreated.extract().path("success"));
        assertEquals(SC_BAD_REQUEST, responseCreated.extract().statusCode());
    }

    @DisplayName("Создание заказ с некорректным хешем ингредиента с авторизацией")
    @Test
    public void orderCannotBeCreatedWithAuthorizationAndIncorrectIngredients() {
        order = generatorOrder.getOrderWithIncorrectIngredients();
        ValidatableResponse responseCreated = orderClient.createdOrderWithAuthorization(order, accessToken);
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseCreated.extract().statusCode());
    }

    @DisplayName("Удаление пользователя")
    @After
    public void clearUp() {
        userClient.deleteUser(accessToken);
    }
}
