package helper;

import connectorDB.ConnectorDB;
import dao.PostDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
}
