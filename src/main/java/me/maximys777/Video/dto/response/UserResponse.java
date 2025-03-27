package me.maximys777.Video.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.maximys777.Video.entity.VideoEntity;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class UserResponse {
    private String name;
    private String password;
    private String profilePic;
    private LocalDateTime createdAt;
    private String role;
    private List<VideoEntity> videos;
}
