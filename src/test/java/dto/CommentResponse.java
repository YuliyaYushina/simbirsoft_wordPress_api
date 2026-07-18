package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentResponse {
    @JsonProperty("id")
    private int id;

    @JsonProperty("content")
    private Content content;

    @JsonProperty("post")
    private Integer post;

    @JsonProperty("status")
    private String status;
}
