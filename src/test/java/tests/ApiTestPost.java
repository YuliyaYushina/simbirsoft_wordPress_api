package tests;

import dao.PostDB;
import dto.ExceptionResponse;
import dto.PostRequest;
import dto.PostResponse;
import helper.PostApiClient;
import helper.PostDBClient;
import helper.PostDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


public class ApiTestPost extends BaseTest {

    @Test
    @DisplayName("Успешное создание публикации при заполнении полей title, content, excerpt")
    void successfullyCreatePostALLFieldsTest() {
        //Создание объекта PostRequest для запроса
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Все поля заполнены title",
                "Все поля заполнены content",
                "Все поля заполнены excerpt");

        //Отправка запроса на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Сохранение id для удаления из БД
        postIdToDelete = postId;

        //Запрос в БД
        PostDB postDB = PostDBClient.selectWpPost(postId);

        assertAll("Проверка данных в БД",
                () -> assertEquals(postId, postDB.getId(), "ID не совпадает"),
                () -> assertEquals(postResponse.getTitle().getRaw(), postDB.getTitle(), "Title не совпадает"),
                () -> assertEquals(postResponse.getContent().getRaw(), postDB.getContent(), "Content не совпадает"),
                () -> assertEquals(postResponse.getExcerpt().getRaw(), postDB.getExcerpt(), "Excerpt не совпадает")
        );
    }

    @Test
    @DisplayName("Успешное создание публикации при заполнении поля title")
    void successfullyCreatePostTitleTest() throws Exception {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Заполнено только поле title", "title");

        //Отправка запроса на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Сохранение id для удаления из БД
        postIdToDelete = postId;

        //Запрос в БД
        PostDB postDB = PostDBClient.selectWpPost(postId);

        assertAll("Проверка данных в БД",
                () -> assertEquals(postId, postDB.getId(), "ID не совпадает"),
                () -> assertEquals(postResponse.getTitle().getRaw(), postDB.getTitle(), "Title не совпадает")
        );
    }

    @Test
    @DisplayName("Успешное создание публикации при заполнении поля content")
    void successfullyCreatePostContentTest() throws Exception {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Заполнено только поле content", "content");

        //Отправка запроса на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Сохранение id для удаления из БД
        postIdToDelete = postId;

        //Запрос в БД
        PostDB postDB = PostDBClient.selectWpPost(postId);

        assertAll("Проверка данных в БД",
                () -> assertEquals(postId, postDB.getId(), "ID не совпадает"),
                () -> assertEquals(postResponse.getContent().getRaw(), postDB.getContent(), "Content не совпадает")
        );
    }

    @Test
    @DisplayName("Успешное создание публикации при заполнении поля excerpt")
    void successfullyCreatePostExcerptTest() throws Exception {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Заполнено только поле excerpt", "excerpt");

        //Отправка запроса на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Сохранение id для удаления из БД
        postIdToDelete = postId;

        //Запрос в БД
        PostDB postDB = PostDBClient.selectWpPost(postId);

        assertAll("Проверка данных в БД",
                () -> assertEquals(postId, postDB.getId(), "ID не совпадает"),
                () -> assertEquals(postResponse.getExcerpt().getRaw(), postDB.getExcerpt(), "Excerpt не совпадает")
        );
    }

    @Test
    @DisplayName("Запрос на создание публикации при отправке некорректного запроса на создание публикации")
    void exceptionCreatePostTest() {
        //Запрос в БД на количество записей перед отправкой некорректного запроса на создание публикации
        int countPostsBeforePost = PostDBClient.selectCountWpPost();

        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest();

        //Отправка некорректного запроса на создание публикации
        ExceptionResponse exceptionResponse = given()
                .spec(spec)
                .queryParam("context", "edit")
                .body(postRequest)
                .when()
                .post("posts")
                .then()
                .statusCode(400)
                .extract()
                .as(ExceptionResponse.class);

        //Проверка ответа API
        assertAll("Проверка ответа API об ошибке 400",
                () -> assertEquals("empty_content", exceptionResponse.getCode(), "Код ошибки не совпадает"),
                () -> assertEquals(400, exceptionResponse.getErrorData().getStatus(), "Статус в теле ответа не 400")
        );

        //Запрос в БД на количество записей после отправки некорректного запроса на создание публикации
        int countPostsAfterPost = PostDBClient.selectCountWpPost();

        assertEquals(countPostsBeforePost, countPostsAfterPost, "Количество записей в БД изменилось, хотя запрос был ошибочным!");
    }

    @Test
    @DisplayName("Успешное изменение существующей публикации")
    void successfullyUpdatePostTest() throws Exception {
        //Создание объекта PostRequest
        PostRequest postRequestBeforeUpdate = PostDataBuilder.buildPostRequest("Изменение публикации",
                "Успешное изменение публикации",
                "Изменение публикации");

        //Запрос на создание публикации
        PostResponse postResponseUpdate = PostApiClient.createPostApi(postRequestBeforeUpdate, spec);

        //Сохранение id созданной публикации
        int postId = postResponseUpdate.getId();

        //Сохранение id для удаления из БД
        postIdToDelete = postId;

        //Создание объекта PostRequest для изменения поля title
        PostRequest postRequestForUpdate = PostDataBuilder.buildPostRequest("Поле title изменено", "title");

        //Запрос на изменение публикации
        PostResponse postResponseAfterUpdate = given()
                .spec(spec)
                .queryParam("context", "edit")
                .pathParam("id", postId)
                .body(postRequestForUpdate)
                .when()
                .post("posts/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(PostResponse.class);

        //Запрос в БД
        PostDB postDB = PostDBClient.selectWpPost(postId);

        assertAll("Проверка данных в БД",
                () -> assertEquals(postId, postDB.getId(), "ID не совпадает"),
                () -> assertEquals(postResponseAfterUpdate.getTitle().getRaw(), postDB.getTitle(), "Title не совпадает"),
                () -> assertEquals(postResponseAfterUpdate.getContent().getRaw(), postDB.getContent(), "Content не совпадает"),
                () -> assertEquals(postResponseAfterUpdate.getExcerpt().getRaw(), postDB.getExcerpt(), "Excerpt не совпадает")
        );
    }

    @Test
    @DisplayName("Успешное удаление уже созданной публикации")
    void successfullyDeleteTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Удаление публикации",
                "Успешное удаление публикации",
                "Удаление публикации");

        //Запрос на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Сохранение id для удаления из БД
        postIdToDelete = postId;

        //Запрос на удаление публикации
        PostResponse postResponseDelete = PostApiClient.deletePostApi(postId, spec);

        //Запрос в БД
        PostDB postDB = PostDBClient.selectWpPost(postId);

        assertAll("Проверка данных в БД",
                () -> assertEquals(postId, postDB.getId(), "ID не совпадает"),
                () -> assertEquals("trash", postDB.getStatus(), "Статус в БД должен быть trash")
        );
    }

    @Test
    @DisplayName("Удаление уже удаленной публикации")
    void exceptionDeleteTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostDataBuilder.buildPostRequest("Удаление публикации",
                "Успешное удаление публикации",
                "Удаление публикации");

        //Запрос на создание публикации
        PostResponse postResponse = PostApiClient.createPostApi(postRequest, spec);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Сохранение id для удаления из БД
        postIdToDelete = postId;

        //Запрос на удаление публикации
        PostResponse postResponseDelete = PostApiClient.deletePostApi(postId, spec);

        //Запрос на удаление уже удаленной публикации
        ExceptionResponse exceptionResponse = given()
                .spec(spec)
                .queryParam("context", "edit")
                .pathParam("id", postId)
                .when()
                .delete("posts/{id}")
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
        PostDB postDB = PostDBClient.selectWpPost(postId);

        assertAll("Проверка данных в БД",
                () -> assertEquals(postId, postDB.getId(), "ID не совпадает"),
                () -> assertEquals("trash", postDB.getStatus(), "Статус в БД должен быть trash")
        );
    }

    @Test
    @DisplayName("Успешное получение созданной публикации")
    void getPostTest() {
        //Запрос в БД на создание публикации и сохранение id созданной публикации
        int postId = PostDBClient.insertWpPost("Получение публикации из БД",
                "Публикация для получения из БД",
                "Публикация в БД");

        //Запрос на получение публикации
        PostResponse postResponse = given()
                .spec(spec)
                .queryParam("context", "edit")
                .pathParam("id", postId)
                .when()
                .get("posts/{id}")
                .then()
                .extract()
                .as(PostResponse.class);

        //Сохранение id для удаления
        postIdToDelete = postResponse.getId();

        //Запрос в БД
        PostDB postDB = PostDBClient.selectWpPost(postId);

        assertAll("Проверка данных из БД и метода GET",
                () -> assertEquals(postId, postDB.getId(), "ID не совпадает"),
                () -> assertEquals(postResponse.getTitle().getRaw(), postDB.getTitle(), "Title не совпадает"),
                () -> assertEquals(postResponse.getContent().getRaw(), postDB.getContent(), "Content не совпадает"),
                () -> assertEquals(postResponse.getExcerpt().getRaw(), postDB.getExcerpt(), "Excerpt не совпадает")
        );
    }
}
