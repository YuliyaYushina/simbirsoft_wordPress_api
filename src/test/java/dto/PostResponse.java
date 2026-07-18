package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostResponse {
    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private Title title;

    @JsonProperty("content")
    private Content content;

    @JsonProperty("excerpt")
    private Excerpt excerpt;

    @JsonProperty("status")
    private String status;
}
