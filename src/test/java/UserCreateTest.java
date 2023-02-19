import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
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
    public void cleanUp(){
        if(accessToken != null)
            userClient.delete(accessToken);
    }

    @Test
    public void userCanBeCreatedAndCheckStatusCode(){
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString().substring(6).trim();

        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }

    @Test
    public void userCanBeCreatedAndCheckResponse() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString().substring(6).trim();

        boolean body = response.extract().path("success");
        assertEquals(true, body);
    }

    @Test
    public void twoIdenticalUsersCannotBeCreatedAndCheckStatusCode() {
        accessToken = userClient.create(user).extract().path("accessToken").toString().substring(6).trim();
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
    }

    @Test
    public void twoIdenticalUsersCannotBeCreatedAndCheckResponse() {
        accessToken = userClient.create(user).extract().path("accessToken").toString().substring(6).trim();
        ValidatableResponse response = userClient.create(user);

        String body = response.extract().path("message");
        assertEquals("User already exists", body);
    }

    @Test
    public void userWithoutEmailCannotBeCreatedAndCheckStatusCode() {
        user = UserCenerator.getWithoutEmail();
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
    }

    @Test
    public void userWithoutEmailCannotBeCreatedAndCheckResponse() {
        user = UserCenerator.getWithoutEmail();
        ValidatableResponse response = userClient.create(user);

        String body = response.extract().path("message");
        assertEquals("Email, password and name are required fields", body);
    }

    @Test
    public void userWithoutPasswordCannotBeCreated() {
        user = UserCenerator.getWithoutPassword();
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
    }

    @Test
    public void userWithoutNameCannotBeCreated() {
        user = UserCenerator.getWithoutName();
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
    }
}
