import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ListOfOrdersTest {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        user = UserCenerator.getDefault();
        userClient = new UserClient();
        accessToken = userClient.create(user).extract().path("accessToken").toString().substring(6).trim();
        orderClient = new OrderClient();
    }

    @After
    public void cleanUp(){
        if(accessToken != null)
            userClient.delete(accessToken);
    }

    @Test
    public void getListOrderWithAuth(){
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        ValidatableResponse responseListOrders = orderClient.listOrdersWithAuth(accessToken);

        boolean body = responseListOrders.extract().path("success");
        assertEquals(true, body);
    }

    @Test
    public void getListOrderWithoutAuth(){
        ValidatableResponse responseListOrders = orderClient.listOrdersWithoutAuth();

        String body = responseListOrders.extract().path("message");
        assertEquals("You should be authorised", body);
    }
}
