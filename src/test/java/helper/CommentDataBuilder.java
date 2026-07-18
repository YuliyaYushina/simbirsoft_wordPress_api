package helper;

import dto.CommentRequest;

public class CommentDataBuilder {

    public static CommentRequest buildCommentRequest(int postId, String content) {
        return CommentRequest.builder()
                .content(content)
                .post(postId)
                .build();
    }

    public static CommentRequest buildCommentRequest() {
        return CommentRequest.builder().build();
    }

    public static CommentRequest buildCommentRequest(int postId) {
        return CommentRequest.builder()
                .post(postId)
                .build();
    }

    public static CommentRequest buildCommentRequest(String content) {
        return CommentRequest.builder()
                .content(content)
                .build();
    }
}
