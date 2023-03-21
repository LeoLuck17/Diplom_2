import Utils.GeneratorRandomData;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class UserLoginTest {
    public String accessToken;
    GeneratorRandomData generatorRandomData = new GeneratorRandomData();
    UserClient userClient = new UserClient();
    User user = generatorRandomData.randomUser();
    public String email = user.getEmail();
    public String password = user.getPassword();
    UserCredentials userCredentials;

    @DisplayName("Создание пользователя")
    @Before
    public void setUp() {
        ValidatableResponse responseCreated = userClient.createdUser(user);
        accessToken = responseCreated.extract().path("accessToken");
        accessToken = accessToken.substring(7);
    }

    @DisplayName("Авторизация пользователя с валидными данными")
    @Test
    public void userCanBeLogin() {
        userCredentials = new UserCredentials(email, password);
        ValidatableResponse responseLogin = userClient.loginUser(userCredentials);
        assertTrue(responseLogin.extract().path("success"));
        assertEquals(SC_OK, responseLogin.extract().statusCode());
    }

    @DisplayName("Авторизация пользователя без email")
    @Test
    public void userLoginWithoutEmail() {
        userCredentials = new UserCredentials(null, password);
        ValidatableResponse responseLogin = userClient.loginUser(userCredentials);
        assertFalse(responseLogin.extract().path("success"));
        assertEquals(SC_UNAUTHORIZED, responseLogin.extract().statusCode());
        assertEquals("email or password are incorrect", responseLogin.extract().path("message"));
    }

    @DisplayName("Авторизация пользователя без password")
    @Test
    public void userLoginWithoutPassword() {
        userCredentials = new UserCredentials(email, null);
        ValidatableResponse responseLogin = userClient.loginUser(userCredentials);
        assertFalse(responseLogin.extract().path("success"));
        assertEquals(SC_UNAUTHORIZED, responseLogin.extract().statusCode());
        assertEquals("email or password are incorrect", responseLogin.extract().path("message"));
    }

    @DisplayName("Удаление пользователя")
    @After
    public void clearUp() {
        userClient.deleteUser(accessToken);
    }
}