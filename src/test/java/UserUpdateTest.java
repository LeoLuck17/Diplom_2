import Utils.GeneratorRandomData;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.ChangeableDataUser;
import model.User;
import model.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserUpdateTest {
    GeneratorRandomData generatorRandomData = new GeneratorRandomData();
    UserClient userClient = new UserClient();
    User user = generatorRandomData.randomUser();
    UserCredentials userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
    String accessToken;
    String newEmail = generatorRandomData.getEmail();
    String newName = generatorRandomData.getName();

    @DisplayName("Создание пользователя")
    @Before
    public void setUp() {
        ValidatableResponse responseCreated = userClient.createdUser(user);
        accessToken = (responseCreated.extract().path("accessToken"));
        accessToken = accessToken.substring(7);
    }

    @Test
    public void userCanBeUpdateAllDataWithAuthorization() {
        userClient.loginUser(userCredentials);
        ChangeableDataUser changeableDataUser = new ChangeableDataUser(newEmail, newName);
        ValidatableResponse responseUpdate = userClient.updateUserWithAuthorization(changeableDataUser, accessToken);
        boolean success = responseUpdate.extract().path("success");
        assertTrue(success);
    }

    @Test
    public void userCanBeUpdateEmailWithAuthorization() {
        userClient.loginUser(userCredentials);
        ChangeableDataUser changeableDataUser = new ChangeableDataUser(null, newName);
        ValidatableResponse responseUpdate = userClient.updateUserWithAuthorization(changeableDataUser, accessToken);
        boolean success = responseUpdate.extract().path("success");
        assertTrue(success);
    }

    @Test
    public void userCanBeUpdateNameWithAuthorization() {
        userClient.loginUser(userCredentials);
        ChangeableDataUser changeableDataUser = new ChangeableDataUser(newEmail, null);
        ValidatableResponse responseUpdate = userClient.updateUserWithAuthorization(changeableDataUser, accessToken);
        boolean success = responseUpdate.extract().path("success");
        assertTrue(success);
    }

    @Test
    public void userCanNotUpdateAllDataWithoutAuthorization() {
        ChangeableDataUser changeableDataUser = new ChangeableDataUser(newEmail, newName);
        ValidatableResponse responseUpdate = userClient.updateUserWithoutAuthorization(changeableDataUser);
        assertEquals(SC_UNAUTHORIZED, responseUpdate.extract().statusCode());
        assertEquals("You should be authorised", responseUpdate.extract().path("message"));
    }

    @Test
    public void userCanNotUpdateEmailWithoutAuthorization() {
        ChangeableDataUser changeableDataUser = new ChangeableDataUser(newEmail, null);
        ValidatableResponse responseUpdate = userClient.updateUserWithoutAuthorization(changeableDataUser);
        assertEquals(SC_UNAUTHORIZED, responseUpdate.extract().statusCode());
        assertEquals("You should be authorised", responseUpdate.extract().path("message"));
    }

    @Test
    public void userCanNotUpdateNameWithoutAuthorization() {
        ChangeableDataUser changeableDataUser = new ChangeableDataUser(null, newName);
        ValidatableResponse responseUpdate = userClient.updateUserWithoutAuthorization(changeableDataUser);
        assertEquals(SC_UNAUTHORIZED, responseUpdate.extract().statusCode());
        assertEquals("You should be authorised", responseUpdate.extract().path("message"));
    }

    @DisplayName("Удаление пользователя")
    @After
    public void clearUp() {
        userClient.deleteUser(accessToken);
    }
}