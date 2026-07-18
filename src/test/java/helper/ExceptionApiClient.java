package helper;

import dto.CommentRequest;
import dto.CommentResponse;
import dto.ExceptionResponse;
import dto.PostRequest;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ExceptionApiClient {

    public static ExceptionResponse createExceptionCommentApiCode403(CommentRequest request, RequestSpecification spec) {
        return given().
                spec(spec)
                .queryParam("context", "edit")
                .body(request)
                .when()
                .post("comments")
                .then()
                .statusCode(403)
                .extract()
                .as(ExceptionResponse.class);
    }

}
