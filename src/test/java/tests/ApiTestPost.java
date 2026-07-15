package tests;

import connectorDB.ConnectorDB;
import dto.ExceptionResponse;
import dto.PostRequest;
import dto.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


public class ApiTestPost extends BaseTest {

    @Test
    @DisplayName("Успешное создание публикации при заполнении полей title, content, excerpt")
    void successfullyCreatePostALLFieldsTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostRequest.builder()
                .title("Все поля заполнены title")
                .content("Все поля заполнены content")
                .excerpt("Все поля заполнены excerpt")
                .build();

        PostResponse postResponse = given()
                .spec(spec)
                .queryParam("context", "edit")
                .body(postRequest)
                .when()
                .post("posts")
                .then()
                .statusCode(201)
                .extract()
                .as(PostResponse.class);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Запрос в БД
        String sql = "SELECT ID, post_title, post_content, post_excerpt FROM wp_posts WHERE ID = ?";

        try (Connection con = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int postIdBD = resultSet.getInt("ID");
                String postTitleBD = resultSet.getString("post_title");
                String postContentBD = resultSet.getString("post_content");
                String postExcerptBD = resultSet.getString("post_excerpt");

                assertAll("Проверка данных в БД",
                        () -> assertEquals(postId, postIdBD, "ID не совпадает"),
                        () -> assertEquals(postResponse.getTitle().getRaw(), postTitleBD, "Title не совпадает"),
                        () -> assertEquals(postResponse.getContent().getRaw(), postContentBD, "Content не совпадает"),
                        () -> assertEquals(postResponse.getExcerpt().getRaw(), postExcerptBD, "Excerpt не совпадает")
                );
            } else {
                fail("Запись с ID " + postId + " не найдена в базе данных!");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }
    }

    @Test
    @DisplayName("Успешное создание публикации при заполнении поля title")
    void successfullyCreatePostTitleTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostRequest.builder()
                .title("Заполнено только поле title")
                .build();

        PostResponse postResponse = given()
                .spec(spec)
                .queryParam("context", "edit")
                .body(postRequest)
                .when()
                .post("posts")
                .then()
                .statusCode(201)
                .extract()
                .as(PostResponse.class);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Запрос в БД
        String sql = "SELECT ID, post_title FROM wp_posts WHERE ID = ?";

        try (Connection con = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int postIdBD = resultSet.getInt("ID");
                String postTitleBD = resultSet.getString("post_title");

                assertAll("Проверка данных в БД",
                        () -> assertEquals(postId, postIdBD, "ID не совпадает"),
                        () -> assertEquals(postResponse.getTitle().getRaw(), postTitleBD, "Title не совпадает")
                );
            } else {
                fail("Запись с ID " + postId + " не найдена в базе данных!");
            }


        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }
    }

    @Test
    @DisplayName("Успешное создание публикации при заполнении поля content")
    void successfullyCreatePostContentTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostRequest.builder()
                .content("Заполнено только поле content")
                .build();

        PostResponse postResponse = given()
                .spec(spec)
                .queryParam("context", "edit")
                .body(postRequest)
                .when()
                .post("posts")
                .then()
                .statusCode(201)
                .extract()
                .as(PostResponse.class);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Запрос в БД
        String sql = "SELECT ID, post_content FROM wp_posts WHERE ID = ?";

        try (Connection con = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int postIdBD = resultSet.getInt("ID");
                String postContentBD = resultSet.getString("post_content");

                assertAll("Проверка данных в БД",
                        () -> assertEquals(postId, postIdBD, "ID не совпадает"),
                        () -> assertEquals(postResponse.getContent().getRaw(), postContentBD, "Content не совпадает")
                );
            } else {
                fail("Запись с ID " + postId + " не найдена в базе данных!");
            }


        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }
    }

    @Test
    @DisplayName("Успешное создание публикации при заполнении поля excerpt")
    void successfullyCreatePostExcerptTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostRequest.builder()
                .excerpt("Заполнено только поле excerpt")
                .build();

        PostResponse postResponse = given()
                .spec(spec)
                .queryParam("context", "edit")
                .body(postRequest)
                .when()
                .post("posts")
                .then()
                .statusCode(201)
                .extract()
                .as(PostResponse.class);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        //Запрос в БД
        String sql = "SELECT ID, post_title, post_content, post_excerpt FROM wp_posts WHERE ID = ?";

        try (Connection con = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int postIdBD = resultSet.getInt("ID");
                String postExcerptBD = resultSet.getString("post_excerpt");

                assertAll("Проверка данных в БД",
                        () -> assertEquals(postId, postIdBD, "ID не совпадает"),
                        () -> assertEquals(postResponse.getExcerpt().getRaw(), postExcerptBD, "Excerpt не совпадает")
                );
            } else {
                fail("Запись с ID " + postId + " не найдена в базе данных!");
            }


        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }
    }

    @Test
    @DisplayName("Запрос на создание публикации при отправке пустого тела запроса")
    void exceptionCreatePostTest() {
        //Проверка количества записей в БД перед отправкой пустого тела запроса
        String sqlBeforePost = "SELECT count(*) as total FROM wp_posts";
        int countPostsBeforePost = 0;

        try (Connection con = ConnectorDB.getConnection();
             Statement statement = con.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sqlBeforePost);

            while (resultSet.next()) {
                countPostsBeforePost = resultSet.getInt("total");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }


        //Создание объекта PostRequest
        PostRequest postRequest = PostRequest.builder().build();

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


        //Проверка количества записей в БД после отправки пустого тела запроса
        String sqlAfterPost = "SELECT count(*) as total FROM wp_posts";
        int countPostsAfterPost = 0;

        try (Connection con = ConnectorDB.getConnection();
             Statement statement = con.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sqlBeforePost);

            while (resultSet.next()) {
                countPostsAfterPost = resultSet.getInt("total");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }

        assertEquals(countPostsBeforePost, countPostsAfterPost, "Количество записей в БД изменилось, хотя запрос был ошибочным!");
    }

    @Test
    @DisplayName("Успешное изменение существующей публикации")
    void successfullyUpdatePostTest() {
        //Создание объекта PostRequest
        PostRequest postRequestBeforeUpdate = PostRequest.builder()
                .title("Изменение публикации")
                .content("Успешное изменение публикации")
                .excerpt("Изменение публикации")
                .build();

        PostResponse postResponseUpdate = given()
                .spec(spec)
                .queryParam("context", "edit")
                .body(postRequestBeforeUpdate)
                .when()
                .post("posts")
                .then()
                .statusCode(201)
                .extract()
                .as(PostResponse.class);

        //Сохранение id созданной публикации
        int postId = postResponseUpdate.getId();

        //Создание объекта для изменения поля title
        PostRequest postRequestForUpdate = PostRequest.builder()
                .title("Поле title изменено")
                .build();

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
        String sql = "SELECT ID, post_title, post_content, post_excerpt FROM wp_posts WHERE ID = ?";

        try (Connection con = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int postIdBD = resultSet.getInt("ID");
                String postTitleBD = resultSet.getString("post_title");
                String postContentBD = resultSet.getString("post_content");
                String postExcerptBD = resultSet.getString("post_excerpt");

                assertAll("Проверка данных в БД",
                        () -> assertEquals(postId, postIdBD, "ID не совпадает"),
                        () -> assertEquals(postResponseAfterUpdate.getTitle().getRaw(), postTitleBD, "Title не совпадает"),
                        () -> assertEquals(postResponseAfterUpdate.getContent().getRaw(), postContentBD, "Content не совпадает"),
                        () -> assertEquals(postResponseAfterUpdate.getExcerpt().getRaw(), postExcerptBD, "Excerpt не совпадает")
                );
            } else {
                fail("Запись с ID " + postId + " не найдена в базе данных!");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }
    }

    @Test
    @DisplayName("Успешное удаление уже созданной публикации")
    void successfullyDeleteTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostRequest.builder()
                .title("Удаление публикации")
                .content("Успешное удаление публикации")
                .excerpt("Удаление публикации")
                .build();

        //Создание публикации
        PostResponse postResponse = given()
                .spec(spec)
                .queryParam("context", "edit")
                .body(postRequest)
                .when()
                .post("posts")
                .then()
                .statusCode(201)
                .extract()
                .as(PostResponse.class);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        PostResponse postResponseDelete = given()
                .spec(spec)
                .queryParam("context", "edit")
                .pathParam("id", postId)
                .when()
                .delete("posts/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(PostResponse.class);

        //Запрос в БД
        String sql = "SELECT ID, post_status FROM wp_posts WHERE ID = ?";

        try (Connection con = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int postIdBD = resultSet.getInt("ID");
                String postStatusDB = resultSet.getString("post_status");

                assertAll("Проверка данных в БД",
                        () -> assertEquals(postId, postIdBD, "ID не совпадает"),
                        () -> assertEquals(postResponseDelete.getStatus(), postStatusDB, "Title не совпадает")
                );
            } else {
                fail("Запись с ID " + postId + " не найдена в базе данных!");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }
    }

    @Test
    @DisplayName("Удаление уже удаленной публикации")
    void exceptionDeleteTest() {
        //Создание объекта PostRequest
        PostRequest postRequest = PostRequest.builder()
                .title("Удаление публикации")
                .content("Успешное удаление публикации")
                .excerpt("Удаление публикации")
                .build();

        //Создание публикации
        PostResponse postResponse = given()
                .spec(spec)
                .queryParam("context", "edit")
                .body(postRequest)
                .when()
                .post("posts")
                .then()
                .statusCode(201)
                .extract()
                .as(PostResponse.class);

        //Сохранение id созданной публикации
        int postId = postResponse.getId();

        PostResponse postResponseDelete = given()
                .spec(spec)
                .queryParam("context", "edit")
                .pathParam("id", postId)
                .when()
                .delete("posts/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(PostResponse.class);

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

        assertAll("Проверка ответа API об ошибке 410",
                () -> assertEquals("rest_already_trashed", exceptionResponse.getCode(), "Код ошибки не совпадает"),
                () -> assertEquals(410, exceptionResponse.getErrorData().getStatus(), "Статус в теле ответа не 410")
        );

        //Запрос в БД
        String sql = "SELECT ID, post_status FROM wp_posts WHERE ID = ?";

        try (Connection con = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int postIdBD = resultSet.getInt("ID");
                String postStatusDB = resultSet.getString("post_status");

                assertAll("Проверка данных в БД",
                        () -> assertEquals(postId, postIdBD, "ID не совпадает"),
                        () -> assertEquals("trash", postStatusDB, "Статус в БД должен быть trash")
                );
            } else {
                fail("Запись с ID " + postId + " не найдена в базе данных!");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }
    }
}
