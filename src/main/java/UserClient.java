import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    private static final String PATH_CREATE = "api/auth/register";
    private static final String PATH_LOGIN = "api/auth/login";
    private static final String PATH_USER = "api/auth/user";

    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(PATH_CREATE)
                .then();
    }

    public ValidatableResponse login(UserCredentials userCredentials){
        return given()
                .spec(getSpec())
                .body(userCredentials)
                .when()
                .post(PATH_LOGIN)
                .then();
    }

    public ValidatableResponse delete(String accessToken){
        return given()
                .header("authorization", "bearer " + accessToken)
                .spec(getSpec())
                .when()
                .delete(PATH_USER)
                .then();
    }

    public ValidatableResponse change(String accessToken, User userChange) {
        return given()
                .header("authorization", "bearer " + accessToken)
                .spec(getSpec())
                .body(userChange)
                .when()
                .patch(PATH_USER)
                .then();
    }

    public ValidatableResponse changeWithoutAuth(User userChange) {
        return given()
                .spec(getSpec())
                .body(userChange)
                .when()
                .patch(PATH_USER)
                .then();
    }
}






