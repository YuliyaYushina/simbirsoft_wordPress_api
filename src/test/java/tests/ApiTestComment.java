package tests;

import dao.CommentDB;
import dto.*;
import helper.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class ApiTestComment extends BaseTest {

    @Test
    @DisplayName("Успешное создание комментария к существующей публикации")
    void successfullyCreateCommentTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Публикация для проверки создания комментария",
                "Успешное создание комментария к публикации",
                "Создание комментария",
                "publish");

        //Запрос на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Сохранение id публикации для удаления
        postIdToDelete = postId;

        //Создание объекта CommentRequest
        CommentRequest commentRequest = CommentDataBuilder.buildCommentRequest(postId,
                "Успешный комментарий к публикации");

        //Запрос на создание комментария
        CommentResponse commentResponse = CommentApiClient.createCommentApi(commentRequest, spec);

        //Сохранение id созданного комментария
        int commentId = commentResponse.getId();

        //Сохранение id комментария для удаления
        commentIdToDelete = commentId;

        //Запрос в БД
        CommentDB commentDB = CommentDBClient.selectWpComments(commentId);

        assertAll("Проверка данных в БД",
                () -> assertEquals(commentId, commentDB.getCommentId(), "ID комментария не совпадает"),
                () -> assertEquals(commentResponse.getContent().getRaw(), commentDB.getContent(), "Content не совпадает"),
                () -> assertEquals(commentResponse.getPost(), commentDB.getPostId(), "ID поста не совпадает")
        );
    }

    @Test
    @DisplayName("Ошибка создания комментария к существующей публикации без полей post и content")
    void exceptionCreateCommentWithoutPostAndContentTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Публикация для проверки создания комментария (негативный сценарий)",
                "Успешное создание комментария к публикации (негативный сценарий)",
                "Создание комментария (негативный сценарий)",
                "publish");

        //Запрос на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id публикации для удаления
        postIdToDelete = postResponse.getId();

        //Проверка количества комментариев в БД перед отправкой пустого тела запроса
        int countCommentsBeforePost = CommentDBClient.selectCountWpComments();

        //Создание объекта CommentRequest
        CommentRequest commentRequest = CommentDataBuilder.buildCommentRequest();

        //Отправка некорректного запроса на создание комментария
        ExceptionResponse exceptionResponse = ExceptionApiClient.createExceptionCommentApiCode403(commentRequest, spec);

        //Проверка ответа API
        assertAll("Проверка ответа API об ошибке 403",
                () -> assertEquals("rest_comment_invalid_post_id", exceptionResponse.getCode(), "Код ошибки не совпадает"),
                () -> assertEquals(403, exceptionResponse.getErrorData().getStatus(), "Статус в теле ответа не 400")
        );

        //Проверка количества комментариев в БД после отправки пустого тела запроса
        int countCommentsAfterPost = CommentDBClient.selectCountWpComments();

        assertEquals(countCommentsBeforePost, countCommentsAfterPost,
                "Количество комментариев изменилось, хотя запрос был ошибочный!");

    }


    @Test
    @DisplayName("Ошибка создания комментария к существующей публикации без поля content")
    void exceptionCreateCommentWithoutContentTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Публикация для проверки создания комментария (негативный сценарий)",
                "Успешное создание комментария к публикации (негативный сценарий)",
                "Создание комментария (негативный сценарий)",
                "publish");

        //Запрос на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Сохранение id публикации для удаления
        postIdToDelete = postId;

        //Проверка количества комментариев в БД перед отправкой пустого тела запроса
        int countCommentsBeforePost = CommentDBClient.selectCountWpComments();

        //Создание объекта CommentRequest
        CommentRequest commentRequest = CommentDataBuilder.buildCommentRequest(postId);

        //Отправка некорректного запроса на создание комментария
        ExceptionResponse exceptionResponse = given().
                spec(spec)
                .queryParam("context", "edit")
                .body(commentRequest)
                .when()
                .post("comments")
                .then()
                .statusCode(400)
                .extract()
                .as(ExceptionResponse.class);;

        //Проверка ответа API
        assertAll("Проверка ответа API об ошибке 400",
                () -> assertEquals("rest_comment_content_invalid", exceptionResponse.getCode(), "Код ошибки не совпадает"),
                () -> assertEquals(400, exceptionResponse.getErrorData().getStatus(), "Статус в теле ответа не 400")
        );

        //Проверка количества комментариев в БД после отправки пустого тела запроса
        int countCommentsAfterPost = CommentDBClient.selectCountWpComments();

        assertEquals(countCommentsBeforePost, countCommentsAfterPost,
                "Количество комментариев изменилось, хотя запрос был ошибочный!");
    }

    @Test
    @DisplayName("Ошибка создания комментария к существующей публикации без поля post")
    void exceptionCreateCommentWithoutPostTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Публикация для проверки создания комментария (негативный сценарий)",
                "Успешное создание комментария к публикации (негативный сценарий)",
                "Создание комментария (негативный сценарий)",
                "publish");

        //Запрос на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id публикации для удаления
        postIdToDelete = postResponse.getId();

        //Проверка количества комментариев в БД перед отправкой пустого тела запроса
        int countCommentsBeforePost = CommentDBClient.selectCountWpComments();

        //Создание объекта CommentRequest
        CommentRequest commentRequest = CommentDataBuilder.buildCommentRequest("Попытка создать комментарий к публикации");

        //Отправка некорректного запроса на создание комментария
        ExceptionResponse exceptionResponse = ExceptionApiClient.createExceptionCommentApiCode403(commentRequest, spec);

        //Проверка ответа API
        assertAll("Проверка ответа API об ошибке 403",
                () -> assertEquals("rest_comment_invalid_post_id", exceptionResponse.getCode(), "Код ошибки не совпадает"),
                () -> assertEquals(403, exceptionResponse.getErrorData().getStatus(), "Статус в теле ответа не 400")
        );

        //Проверка количества комментариев в БД после отправки пустого тела запроса
        int countCommentsAfterPost = CommentDBClient.selectCountWpComments();

        assertEquals(countCommentsBeforePost, countCommentsAfterPost,
                "Количество комментариев изменилось, хотя запрос был ошибочный!");

    }

    @Test
    @DisplayName("Ошибка создания комментария к существующей публикации с некорретным типом post")
    void exceptionCreateCommentWithIncorrectPostAndContentTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Публикация для проверки создания комментария (негативный сценарий)",
                "Успешное создание комментария к публикации (негативный сценарий)",
                "Создание комментария (негативный сценарий)",
                "publish");

        //Запрос на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id публикации для удаления
        postIdToDelete = postResponse.getId();;

        //Проверка количества комментариев в БД перед отправкой пустого тела запроса
        int countCommentsBeforePost = CommentDBClient.selectCountWpComments();

        //Создание объекта CommentRequest
        ExceptionRequestComment commentRequest = ExceptionRequestComment.builder()
                .post("один")
                .content("Попытка создать комментарий к публикации")
                .build();

        //Отправка некорректного запроса на создание комментария
        ExceptionResponse exceptionResponse = given().
                spec(spec)
                .queryParam("context", "edit")
                .body(commentRequest)
                .when()
                .post("comments")
                .then()
                .statusCode(400)
                .extract()
                .as(ExceptionResponse.class);;

        //Проверка ответа API
        assertAll("Проверка ответа API об ошибке 400",
                () -> assertEquals("rest_invalid_param", exceptionResponse.getCode(), "Код ошибки не совпадает"),
                () -> assertEquals(400, exceptionResponse.getErrorData().getStatus(), "Статус в теле ответа не 400")
        );

        //Проверка количества комментариев в БД после отправки пустого тела запроса
        int countCommentsAfterPost = CommentDBClient.selectCountWpComments();

        assertEquals(countCommentsBeforePost, countCommentsAfterPost,
                "Количество комментариев изменилось, хотя запрос был ошибочный!");
    }

    @Test
    @DisplayName("Успешное изменение комментария к существующей публикации")
    void successfullyUpdateCommentTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Публикация для проверки изменения комментария",
                "Успешное изменение комментария к публикации",
                "Изменение комментария",
                "publish");

        //Запрос на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Сохранение id публикации для удаления
        postIdToDelete = postId;

        //Создание объекта CommentRequest
        CommentRequest commentRequestBeforeUpdate = CommentDataBuilder.buildCommentRequest(postId, "Комментарий к публикации");

        //Запрос на создание комментария
        CommentResponse commentResponseBeforeUpdate = CommentApiClient.createCommentApi(commentRequestBeforeUpdate, spec);

        //Сохранение id созданного комментария
        int commentId = commentResponseBeforeUpdate.getId();

        //Сохранение id комментария для удаления
        commentIdToDelete = commentId;

        //Создание объекта CommentRequest для изменеия комментария
        CommentRequest commentRequestForUpdate = CommentDataBuilder.buildCommentRequest("Комментарий к публикации изменен");

        //Запрос на изменеие комментария
        CommentResponse commentResponseAfterUpdate = given()
                .spec(spec)
                .queryParam("context", "edit")
                .pathParam("id", commentId)
                .body(commentRequestForUpdate)
                .when()
                .post("comments/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(CommentResponse.class);

        //Запрос в БД
        CommentDB commentDB = CommentDBClient.selectWpComments(commentId);

        assertAll("Проверка данных в БД",
                () -> assertEquals(commentId, commentDB.getCommentId(), "ID комментария не совпадает"),
                () -> assertEquals(commentResponseAfterUpdate.getContent().getRaw(), commentDB.getContent(), "Content не совпадает"),
                () -> assertEquals(commentResponseAfterUpdate.getPost(), commentDB.getPostId(), "ID поста не совпадает")
        );
    }

    @Test
    @DisplayName("Успешное удаление комментария к существующей публикации")
    void successfullyDeleteCommentTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Публикация для проверки удаления комментария",
                "Успешное удаление комментария к публикации",
                "Удаление комментария",
                "publish");

        //Запрос на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Сохранение id публикации для удаления
        postIdToDelete = postId;

        //Создание объекта CommentRequest
        CommentRequest commentRequest = CommentDataBuilder.buildCommentRequest(postId, "Комментарий к публикации будет удален");

        //Запрос на создание комментария
        CommentResponse commentResponse = CommentApiClient.createCommentApi(commentRequest, spec);

        //Сохранение id созданного комментария
        int commentId = commentResponse.getId();

        //Сохранение id комментария для удаления
        commentIdToDelete = commentId;

        //Запрос на удаление комментария
        CommentResponse commentResponseDelete = CommentApiClient.deleteCommentApi(commentId, spec);

        //Запрос в БД
        CommentDB commentDB = CommentDBClient.selectWpComments(commentId);

        assertAll("Проверка данных в БД",
                () -> assertEquals(commentId, commentDB.getCommentId(), "ID комментария не совпадает"),
                () -> assertEquals("trash", commentDB.getStatus(), "Статус в БД должен быть trash")
        );
    }

    @Test
    @DisplayName("Удаление уже удаленного комментария к существующей публикации")
    void exceptionDeleteCommentTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Публикация для проверки удаления уже удаленного комментария",
                "Попытка удаления уже удаленного комментария к публикации",
                "Удаление уже удаленного комментария",
                "publish");

        //Запрос на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Сохранение id публикации для удаления
        postIdToDelete = postId;

        //Создание объекта CommentRequest
        CommentRequest commentRequest = CommentDataBuilder.buildCommentRequest(postId,
                "Попытка удаления уже удаленного комментария к публикации");

        //Запрос на создание комментария
        CommentResponse commentResponse = CommentApiClient.createCommentApi(commentRequest, spec);

        //Сохранение id созданного комментария
        int commentId = commentResponse.getId();

        //Сохранение id комментария для удаления
        commentIdToDelete = commentId;

        //Запрос на удаление комментария
        CommentResponse commentResponseDelete = CommentApiClient.deleteCommentApi(commentId, spec);

        //Запрос на удаление уже удаленного комментария
        ExceptionResponse exceptionResponse = given()
                .spec(spec)
                .queryParam("context", "edit")
                .pathParam("id", commentId)
                .when()
                .delete("comments/{id}")
                .then()
                .statusCode(410)
                .extract()
                .as(ExceptionResponse.class);

        //Проверка ответа API
        assertAll("Проверка ответа API об ошибке 410",
                () -> assertEquals("rest_already_trashed", exceptionResponse.getCode(), "Код ошибки не совпадает"),
                () -> assertEquals(410, exceptionResponse.getErrorData().getStatus(), "Статус в теле ответа не 410")
        );

        //Запрос в БД
        CommentDB commentDB = CommentDBClient.selectWpComments(commentId);

        assertAll("Проверка данных в БД",
                () -> assertEquals(commentId, commentDB.getCommentId(), "ID комментария не совпадает"),
                () -> assertEquals("trash", commentDB.getStatus(), "Статус в БД должен быть trash")
        );
    }

    @Test
    @DisplayName("Успешное получение созданного комментария")
    void getCommentTest() {
        //Запрос в БД на создание публикации и сохранение id созданной публикации
        int postId = PostDBClient.insertWpPost("Публикация для создания комментария",
                "Публикация для создания комментария из БД",
                "Публикация для комментария");

        //Сохранение id публикации для удаления
        postIdToDelete = postId;

        //Запрос в БД на создание комментария к публикации и сохранение id озданного комментария
        int commentId = CommentDBClient.insertWpComments(postId, "Текст комментария, созданного через SQL");

        //Запрос на получение комментария
        CommentResponse commentResponse = given()
                .spec(spec)
                .queryParam("context", "edit")
                .pathParam("id", commentId)
                .when()
                .get("comments/{id}")
                .then()
                .extract()
                .as(CommentResponse.class);

        //Сохранение id комментария для удаления
        commentIdToDelete = commentResponse.getId();

        //Запрос в БД
        CommentDB commentDB = CommentDBClient.selectWpComments(commentId);

        assertAll("Проверка данных в БД",
                () -> assertEquals(commentId, commentDB.getCommentId(), "ID комментария не совпадает"),
                () -> assertEquals(commentResponse.getContent().getRaw(), commentDB.getContent(), "Content не совпадает"),
                () -> assertEquals(commentResponse.getPost(), commentDB.getPostId(), "ID поста не совпадает")
        );
    }
}
