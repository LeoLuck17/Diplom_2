package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private static final String PATH_ORDER = "/api/orders";

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createdOrderWithAuthorization(Order order, String accessToken) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(PATH_ORDER)
                .then();
    }

    @Step("Создание заказа без авторизацией")
    public ValidatableResponse createdOrderWithoutAuthorization(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(PATH_ORDER)
                .then();
    }

    @Step("Получить заказы конкретного пользователя")
    public ValidatableResponse getOrderUser(String accessToken) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .when()
                .get(PATH_ORDER)
                .then();
    }
}
