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

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class GetOrderUserTest {
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
        ValidatableResponse responseCreatedUser = userClient.createdUser(user);
        accessToken = responseCreatedUser.extract().path("accessToken");
        accessToken = accessToken.substring(7);
        order = generatorOrder.getOrderWithIngredient();
        orderClient.createdOrderWithAuthorization(order, accessToken);
    }

    @DisplayName("Получение списка заказов конкретного пользователя с авторизацией")
    @Test
    public void getOrderUserWithAuthorization() {
        ValidatableResponse getOrderResponse = orderClient.getOrderUser(accessToken);
        assertTrue(getOrderResponse.extract().path("success"));
        assertEquals(SC_OK, getOrderResponse.extract().statusCode());
    }

    @DisplayName("Получение списка заказов конкретного пользователя без авторизации")
    @Test
    public void getOrderUserWithoutAuthorization() {
        ValidatableResponse getOrderResponse = orderClient.getOrderUser(" ");
        assertFalse(getOrderResponse.extract().path("success"));
        assertEquals(SC_UNAUTHORIZED, getOrderResponse.extract().statusCode());
        assertEquals("You should be authorised", getOrderResponse.extract().path("message"));
    }

    @DisplayName("Удаление пользователя")
    @After
    public void clearUp() {
        userClient.deleteUser(accessToken);
    }
}
