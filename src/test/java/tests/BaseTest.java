package tests;


import config.Configuration;
import connectorDB.ConnectorDB;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BaseTest {

        protected int postIdToDelete = 0;
        protected int commentIdToDelete = 0;
        protected static final Configuration config = ConfigFactory.create(Configuration.class);

        protected final RequestSpecification spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setBaseUri(config.baseUrl())
                .setBasePath(config.basePath())
                .setAuth(RestAssured.preemptive().basic(config.user(), config.password()))
                .build();

        @AfterEach
        void cleanup() {
                if (postIdToDelete == 0 && commentIdToDelete == 0) return;

                String sqlComments = "DELETE FROM wp_comments WHERE comment_ID = ?";
                String sqlPosts = "DELETE FROM wp_posts WHERE ID = ?";

                try (Connection con = ConnectorDB.getConnection();
                     PreparedStatement psComment = con.prepareStatement(sqlComments);
                     PreparedStatement psPost = con.prepareStatement(sqlPosts)) {

                        //Удаление комментария к публикации
                        psComment.setInt(1, commentIdToDelete);
                        psComment.executeUpdate();

                        //Удаление публикации
                        psPost.setInt(1, postIdToDelete);
                        psPost.executeUpdate();

                } catch (SQLException ex) {
                        System.err.println("Ошибка при очистке БД: " + ex.getMessage());
                } finally {
                        //Восстановление значений id для удаления по умолчанию
                        postIdToDelete = 0;
                        commentIdToDelete = 0;
                }
        }
}
