import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

public class UserCreateTest {
    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserCenerator.getDefault();
        userClient = new UserClient();
    }

    @After
    public void cleanUp() {
        if (accessToken != null)
            userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Создание пользователя. Проверка статус кода ответа")
    public void userCanBeCreatedAndCheckStatusCode() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString().substring(6).trim();

        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }

    @Test
    @DisplayName("Создание пользователя. Проверка тела ответа")
    public void userCanBeCreatedAndCheckResponse() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString().substring(6).trim();

        boolean body = response.extract().path("success");
        assertEquals(true, body);
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей. Проверка статус кода ответа")
    public void twoIdenticalUsersCannotBeCreatedAndCheckStatusCode() {
        accessToken = userClient.create(user).extract().path("accessToken").toString().substring(6).trim();
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей. Проверка тела ответа")
    public void twoIdenticalUsersCannotBeCreatedAndCheckResponse() {
        accessToken = userClient.create(user).extract().path("accessToken").toString().substring(6).trim();
        ValidatableResponse response = userClient.create(user);

        String body = response.extract().path("message");
        assertEquals("User already exists", body);
    }

    @Test
    @DisplayName("Создание пользователя без email. Проверка статус кода ответа")
    public void userWithoutEmailCannotBeCreatedAndCheckStatusCode() {
        user = UserCenerator.getWithoutEmail();
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
    }

    @Test
    @DisplayName("Создание пользователя без email. Проверка тела ответа")
    public void userWithoutEmailCannotBeCreatedAndCheckResponse() {
        user = UserCenerator.getWithoutEmail();
        ValidatableResponse response = userClient.create(user);

        String body = response.extract().path("message");
        assertEquals("Email, password and name are required fields", body);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void userWithoutPasswordCannotBeCreated() {
        user = UserCenerator.getWithoutPassword();
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void userWithoutNameCannotBeCreated() {
        user = UserCenerator.getWithoutName();
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
    }
}
