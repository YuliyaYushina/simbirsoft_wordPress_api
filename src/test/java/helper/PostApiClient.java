package helper;

import dto.PostRequest;
import dto.PostResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class PostApiClient {

    public static PostResponse createPostApi(PostRequest request, RequestSpecification spec) {
        return given()
                .spec(spec)
                .queryParam("context", "edit")
                .body(request)
                .when()
                .post("posts")
                .then()
                .statusCode(201)
                .extract()
                .as(PostResponse.class);
    }

    public static PostResponse deletePostApi(int postId, RequestSpecification spec) {
        return given()
                .spec(spec)
                .queryParam("context", "edit")
                .pathParam("id", postId)
                .when()
                .delete("posts/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(PostResponse.class);
    }
}
