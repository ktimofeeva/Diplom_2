import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private static final String PATH = "api/orders";

    public ValidatableResponse createWithAuthWithIngridients(Order order, String accessToken) {
        return given()
                .header("authorization", "bearer " + accessToken)
                .spec(getSpec())
                .body(order)
                .when()
                .post(PATH)
                .then();
    }

    public ValidatableResponse createWithoutAuthWithIngridients(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(PATH)
                .then();
    }

    public ValidatableResponse createWithAuthWithoutIngridients(String accessToken) {
        return given()
                .header("authorization", "bearer " + accessToken)
                .spec(getSpec())
                .body("")
                .when()
                .post(PATH)
                .then();
    }

    public ValidatableResponse createWithoutAuthWithoutIngridients() {
        return given()
                .spec(getSpec())
                .body("")
                .when()
                .post(PATH)
                .then();
    }

    public ValidatableResponse listOrdersWithAuth(String accessToken) {
        return given()
                .header("authorization", "bearer " + accessToken)
                .spec(getSpec())
                .body("")
                .when()
                .get(PATH)
                .then();
    }

    public ValidatableResponse listOrdersWithoutAuth() {
        return given()
                .spec(getSpec())
                .body("")
                .when()
                .get(PATH)
                .then();
    }
}
