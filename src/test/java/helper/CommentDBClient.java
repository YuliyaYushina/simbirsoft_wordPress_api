package helper;

import connectorDB.ConnectorDB;
import dao.CommentDB;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.fail;

public class CommentDBClient {

    public static CommentDB selectWpComments(int commentID) {
        CommentDB commentDB = new CommentDB();
        String sql = "SELECT comment_ID, comment_post_ID, comment_content, comment_approved FROM wp_comments WHERE comment_ID = ?";

        try (Connection con = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setInt(1, commentID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                commentDB.setCommentId(resultSet.getInt("comment_ID"));
                commentDB.setPostId(resultSet.getInt("comment_post_ID"));
                commentDB.setContent(resultSet.getString("comment_content"));
                commentDB.setStatus(resultSet.getString("comment_approved"));

            } else {
                fail("Комментарий с ID " + commentID + " не найден в базе данных!");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }

        return commentDB;
    }

    public static int selectCountWpComments() {
        String sqlBeforePost = "SELECT count(*) as total FROM wp_comments";
        int countComments = 0;

        try (Connection con = ConnectorDB.getConnection();
             Statement statement = con.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sqlBeforePost);

            while (resultSet.next()) {
                countComments = resultSet.getInt("total");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при работе с БД", ex);
        }
        return countComments;
    }

    public static int insertWpComments(int postId, String content) {
        int commentId = 0;
        String sql = "INSERT INTO wp_comments (comment_post_ID, comment_author, comment_author_email, " +
                "comment_author_url, comment_author_IP, comment_date, comment_date_gmt, comment_content, " +
                "comment_karma, comment_approved, comment_agent, comment_type, comment_parent, user_id) VALUES (\n" +
                "?, 'admin', 'admin@example.com', '', '127.0.0.1', NOW(), NOW(), " +
                "?, 0, '1', 'Manual Insert', '', 0, 1);\n";

        try (Connection con = ConnectorDB.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, postId);
            preparedStatement.setString(2, content);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    commentId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Не удалось получить ID после вставки.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка БД при создании комментария", e);
        }
        return commentId;
    }
}
