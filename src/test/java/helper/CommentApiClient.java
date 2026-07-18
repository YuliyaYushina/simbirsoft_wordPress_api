package helper;

import dto.CommentRequest;
import dto.CommentResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class CommentApiClient {
    public static CommentResponse createCommentApi(CommentRequest request, RequestSpecification spec) {
        return given().
                spec(spec)
                .queryParam("context", "edit")
                .body(request)
                .when()
                .post("comments")
                .then()
                .statusCode(201)
                .extract()
                .as(CommentResponse.class);
    }

    public static CommentResponse deleteCommentApi(int commentId, RequestSpecification spec) {
        return given()
                .spec(spec)
                .queryParam("context", "edit")
                .pathParam("id", commentId)
                .when()
                .delete("comments/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(CommentResponse.class);
    }
}
