import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.junit.Assert.assertEquals;

public class OrderTest {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private OrderClient orderClient;
    private Order order;

    @Before
    public void setUp() {
        user = UserCenerator.getDefault();
        userClient = new UserClient();
        accessToken = userClient.create(user).extract().path("accessToken").toString().substring(6).trim();
        orderClient = new OrderClient();
    }

    @After
    public void cleanUp() {
        if (accessToken != null)
            userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов неавторизованным пользователем")
    public void orderCannotBeCreatedWithoutAuthWithoutIngridients() {
        ValidatableResponse responseOrderCreate = orderClient.createWithoutAuthWithoutIngridients();

        String body = responseOrderCreate.extract().path("message");
        assertEquals("Ingredient ids must be provided", body);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов авторизованным пользователем")
    public void orderCannotBeCreatedWithAuthWithoutIngridients() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        ValidatableResponse responseOrderCreate = orderClient.createWithAuthWithoutIngridients(accessToken);

        String body = responseOrderCreate.extract().path("message");
        assertEquals("Ingredient ids must be provided", body);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами авторизованным пользователем")
    public void orderCanBeCreatedWithAuthWithIngridients() {
        order = OrderGenerator.getDefault();

        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        ValidatableResponse responseOrderCreate = orderClient.createWithAuthWithIngridients(order, accessToken);

        boolean body = responseOrderCreate.extract().path("success");
        assertEquals(true, body);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами неавторизованным пользователем")
    public void orderCanBeCreateWithoutAuthWithIngridients() {
        order = OrderGenerator.getDefault();

        ValidatableResponse responseOrderCreate = orderClient.createWithoutAuthWithIngridients(order);

        boolean body = responseOrderCreate.extract().path("success");
        assertEquals(true, body);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void orderCannotBeCreatedWithErrorHashIngridients() {
        order = OrderGenerator.getWithErrorHash();

        ValidatableResponse responseOrderCreate = orderClient.createWithoutAuthWithIngridients(order);

        int statusCode = responseOrderCreate.extract().statusCode();
        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }
}
