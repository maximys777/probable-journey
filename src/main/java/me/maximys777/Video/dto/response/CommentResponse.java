package me.maximys777.Video.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.maximys777.Video.entity.CommentEntity;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String name;
    private String profilePic;
    private String text;
    private LocalDateTime createdAt;

    public CommentResponse(CommentEntity comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.name = comment.getUser().getName();
        this.profilePic = comment.getUser().getProfilePic();
        this.createdAt = comment.getCreatedAt();
    }

}
