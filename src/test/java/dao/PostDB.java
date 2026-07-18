package dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDB {
    private int id;
    private String title;
    private String content;
    private String excerpt;
    private String status;
}
