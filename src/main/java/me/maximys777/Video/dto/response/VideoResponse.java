package me.maximys777.Video.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class VideoResponse {
    private String title;
    private String thumbnail;
    private String description;
    private String categoryName;
    private Long views;
    private LocalDateTime createdAt;
    private String userName;
    private String userBanner;
    private List<CommentResponse> comments;
}