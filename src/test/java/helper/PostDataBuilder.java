package helper;

import dto.PostRequest;

public class PostDataBuilder {

    public static PostRequest buildPostRequest(String title, String content, String excerpt, String status) {
        return PostRequest.builder()
                .title(title)
                .content(content)
                .excerpt(excerpt)
                .status(status)
                .build();
    }

    public static PostRequest buildPostRequest(String title, String content, String excerpt) {
        return PostRequest.builder()
                .title(title)
                .content(content)
                .excerpt(excerpt)
                .build();
    }

    public static PostRequest buildPostRequest(String field, String typeField) throws Exception {
        switch (typeField) {
            case "title": return PostRequest.builder()
                                            .title(field)
                                            .build();
            case "content": return PostRequest.builder()
                                              .content(field)
                                              .build();
            case "excerpt": return PostRequest.builder()
                                              .excerpt(field)
                                              .build();
            default: throw new Exception("Неверный параметр typeField");
        }
    }

    public static PostRequest buildPostRequest() {
        return PostRequest.builder().build();
    }
}
