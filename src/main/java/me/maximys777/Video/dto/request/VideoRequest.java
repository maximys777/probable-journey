package me.maximys777.Video.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VideoRequest {
    private String title;
    private String description;
    private String thumbnail;
    private LocalDateTime createdAt;
    private String categoryName;
}
