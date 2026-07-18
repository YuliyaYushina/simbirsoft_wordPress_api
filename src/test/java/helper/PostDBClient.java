package helper;

import connectorDB.ConnectorDB;
import dao.PostDB;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.fail;

public class PostDBClient {

    public static PostDB selectWpPost(int postId) {
        PostDB postDB = new PostDB();
        String sql = "SELECT ID, post_title, post_content, post_excerpt, post_status FROM wp_posts WHERE ID = ?";

        try (Connection con = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                postDB.setId(resultSet.getInt("ID"));
                postDB.setTitle(resultSet.getString("post_title"));
                postDB.setContent(resultSet.getString("post_content"));
                postDB.setExcerpt(resultSet.getString("post_excerpt"));
                postDB.setStatus(resultSet.getString("post_status"));

            } else {
                fail("Запись с ID " + postId + " не найдена в базе данных!");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }

        return postDB;
    }

    public static int selectCountWpPost() {
        String sqlPost = "SELECT count(*) as total FROM wp_posts";
        int countPosts = 0;

        try (Connection con = ConnectorDB.getConnection();
             Statement statement = con.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sqlPost);

            while (resultSet.next()) {
                countPosts = resultSet.getInt("total");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }

        return countPosts;
    }

    public static int insertWpPost(String title, String content, String excerpt) {
        String sql = "INSERT INTO wp_posts (post_author, post_date, post_date_gmt, post_content, post_title, \n" +
                " post_excerpt, post_status, comment_status, ping_status, post_password, \n" +
                " post_name, to_ping, pinged, post_modified, post_modified_gmt, \n" +
                " post_content_filtered, post_parent, guid, menu_order, post_type, \n" +
                " post_mime_type, comment_count) VALUES (\n" +
                " 1, NOW(), NOW(), ?, ?, ?, 'publish', 'open', 'open', '', \n" +
                " 'post-from-db', '', '', NOW(), NOW(), '', 0, '', 0, 'post', '', 0);";

        int postId = 0;
        try (Connection con = ConnectorDB.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, content);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, excerpt);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    postId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Не удалось получить ID после вставки.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка БД при создании записи", e);
        }
        return postId;
    }
}
