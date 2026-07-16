package helper;

import connectorDB.ConnectorDB;
import dao.CommentDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
}
