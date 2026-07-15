package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Value
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostRequest {
    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("excerpt")
    private String excerpt;

    @JsonProperty("status")
    private String status;
}
