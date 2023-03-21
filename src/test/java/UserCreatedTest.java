import Utils.GeneratorRandomData;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.*;

public class UserCreatedTest {
    GeneratorRandomData generatorRandomData = new GeneratorRandomData();
    public String email = generatorRandomData.getEmail();
    public String password = generatorRandomData.getPassword();
    public String name = generatorRandomData.getName();
    UserClient userClient = new UserClient();
    User user;

    @DisplayName("Создание пользователя с валидными данными")
    @Test
    public void userCanBeCreated() {
        user = new User(email, password, name);
        ValidatableResponse responseCreated = userClient.createdUser(user);
        assertTrue(responseCreated.extract().path("success"));
        String accessToken = responseCreated.extract().path("accessToken");
        accessToken = accessToken.substring(7);
        userClient.deleteUser(accessToken);
    }

    @DisplayName("Создание пользователя с существующими учетными данными")
    @Test
    public void cannotCreatedDoubleUser() {
        user = new User(email, password, name);
        ValidatableResponse responseCreated = userClient.createdUser(user);
        ValidatableResponse responseCreatedDouble = userClient.createdUser(user);
        assertTrue(responseCreated.extract().path("success"));
        assertFalse(responseCreatedDouble.extract().path("success"));
        assertEquals(SC_FORBIDDEN, responseCreatedDouble.extract().statusCode());
        assertEquals("User already exists", responseCreatedDouble.extract().path("message"));
    }

    @DisplayName("Создание пользователя без email")
    @Test
    public void createdUserWithoutEmail() {
        user = new User(null, password, name);
        ValidatableResponse responseCreated = userClient.createdUser(user);
        assertFalse(responseCreated.extract().path("success"));
        assertEquals(SC_FORBIDDEN, responseCreated.extract().statusCode());
        assertEquals("Email, password and name are required fields", responseCreated.extract().path("message"));
    }

    @DisplayName("Создание пользователя без password")
    @Test
    public void createdUserWithoutPassword() {
        user = new User(email, null, name);
        ValidatableResponse responseCreated = userClient.createdUser(user);
        assertFalse(responseCreated.extract().path("success"));
        assertEquals(SC_FORBIDDEN, responseCreated.extract().statusCode());
        assertEquals("Email, password and name are required fields", responseCreated.extract().path("message"));
    }

    @DisplayName("Создание пользователя без name")
    @Test
    public void createdUserWithoutName() {
        user = new User(email, password, null);
        ValidatableResponse responseCreated = userClient.createdUser(user);
        assertFalse(responseCreated.extract().path("success"));
        assertEquals(SC_FORBIDDEN, responseCreated.extract().statusCode());
        assertEquals("Email, password and name are required fields", responseCreated.extract().path("message"));
    }
}