import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class UserLoginTest {
    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserCenerator.getDefault();
        userClient = new UserClient();
        accessToken = userClient.create(user).extract().path("accessToken").toString().substring(6).trim();
    }

    @After
    public void cleanUp() {
        if (accessToken != null)
            userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Авторизация существующего пользователя. Проверка статус кода ответа")
    public void userCanBeLoginAndCheckStatusCode() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }

    @Test
    @DisplayName("Авторизация существующего пользователя. Проверка тела ответа")
    public void userCanBeLoginAndCheckResponse() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));

        boolean body = loginResponse.extract().path("success");
        assertEquals(true, body);
    }

    @Test
    @DisplayName("Авторизация с неверным логином. Проверка статус кода ответа")
    public void userWithErrorInEmailCannotLogInAndCheckStatusCode() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(user.getEmail() + "error", user.getPassword()));

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }

    @Test
    @DisplayName("Авторизация с неверным email. Проверка тела ответа")
    public void userWithErrorInEmailCannotLogInAndCheckResponse() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(user.getEmail() + "error", user.getPassword()));

        boolean body = loginResponse.extract().path("success");
        assertEquals(false, body);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем. Проверка статус кода ответа")
    public void userWithErrorInPasswordCannotLogInAndCheckStatusCode() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(user.getEmail(), user.getPassword() + "error"));

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем. Проверка тела ответа")
    public void userWithErrorInPasswordCannotLogInAndCheckResponse() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(user.getEmail(), user.getPassword() + "error"));

        String body = getResponseBody(loginResponse);
        comparingBodyAndExpectedResult("email or password are incorrect",body);
    }

    @Step("Get response body")
    public String getResponseBody(ValidatableResponse response){
        return response.extract().path("message");
    }

    @Step("Сomparing the response body with the expected result")
    public void comparingBodyAndExpectedResult(String expectedResult, String body){
        assertEquals(expectedResult, body);
    }
}
