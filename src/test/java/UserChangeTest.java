import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class UserChangeTest {
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
    @DisplayName("Изменение имени пользователя")
    public void userCanBeChangeName() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString().substring(6).trim();

        ValidatableResponse userChange = userClient.change(accessToken, new User(user.getEmail(), user.getPassword(), "new" + user.getName()));

        String expectedResult = "new" + user.getName();
        String actualResult = userChange.extract().path("user.name");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Изменение email пользователя")
    public void userCanBeChangeEmail() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString().substring(6).trim();

        ValidatableResponse userChange = userClient.change(accessToken, new User("new" + user.getEmail(), user.getPassword(), user.getName()));

        String expectedResult = ("new" + user.getEmail()).toLowerCase(Locale.ROOT);
        String actualResult = userChange.extract().path("user.email");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Изменение пароля пользователя")
    public void userCanBeChangePassword() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString().substring(6).trim();

        ValidatableResponse userChange = userClient.change(accessToken, new User(user.getEmail(), "new" + user.getPassword(), user.getName()));

        boolean expectedResult = true;
        boolean actualResult = userChange.extract().path("success");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Изменение почты пользователя на уже существующую в системе")
    public void userCanNotBeChangeEmailExisting() {
        User userExisting = UserCenerator.getDefault();
        ValidatableResponse responseExisting = userClient.create(userExisting);
        String accessTokenUserExisting = responseExisting.extract().path("accessToken").toString().substring(6).trim();

        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString().substring(6).trim();

        ValidatableResponse userChange = userClient.change(accessToken, new User(userExisting.getEmail(), user.getPassword(), user.getName()));

        String expectedResult = "User with such email already exists";
        String actualResult = userChange.extract().path("message");
        assertEquals(expectedResult, actualResult);

        userClient.delete(accessTokenUserExisting);
    }

    @Test
    @DisplayName("Изменение пользователя без авторизации. Проверка тела ответа")
    public void userCanNotBeChangeWithoutAuthAndCheckResponse() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString().substring(6).trim();

        ValidatableResponse userChange = userClient.changeWithoutAuth(new User(user.getEmail(), user.getPassword(), "new" + user.getName()));

        String expectedResult = "You should be authorised";
        String actualResult = userChange.extract().path("message");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Изменение пользователя без авторизации. Проверка статус кода ответа")
    public void userCanNotBeChangeWithoutAuthAndStatusCode() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString().substring(6).trim();

        ValidatableResponse userChange = userClient.changeWithoutAuth(new User(user.getEmail(), user.getPassword(), "new" + user.getName()));

        int statusCode = userChange.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }
}
