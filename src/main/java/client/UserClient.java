package client;


import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.ChangeableDataUser;
import model.User;
import model.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    private static final String PATH_CREATED = "/api/auth/register";
    private static final String PATH_LOGIN = "/api/auth/login";
    private static final String PATH_USER_UPDATE_DELETE = "/api/auth/user";

    @Step("Создание пользователя")
    public ValidatableResponse createdUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(PATH_CREATED)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(UserCredentials userCredentials) {
        return given()
                .spec(getSpec())
                .body(userCredentials)
                .when()
                .post(PATH_LOGIN)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getSpec())
                .body(accessToken)
                .when()
                .delete(PATH_USER_UPDATE_DELETE)
                .then();

    }

    @Step("Обновление пользователя с авторизацией")
    public ValidatableResponse updateUserWithAuthorization(ChangeableDataUser changeableDataUser, String accessToken) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
                .body(changeableDataUser)
                .when()
                .patch(PATH_USER_UPDATE_DELETE)
                .then();
    }

    @Step("Обновление пользователя без авторизации")
    public ValidatableResponse updateUserWithoutAuthorization(ChangeableDataUser changeableDataUser) {
        return given()
                .spec(getSpec())
                .body(changeableDataUser)
                .when()
                .patch(PATH_USER_UPDATE_DELETE)
                .then();
    }
}
