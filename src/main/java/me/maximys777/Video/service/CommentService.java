package me.maximys777.Video.service;

import lombok.AllArgsConstructor;
import me.maximys777.Video.dto.response.CommentResponse;
import me.maximys777.Video.entity.CommentEntity;
import me.maximys777.Video.entity.UserEntity;
import me.maximys777.Video.entity.VideoEntity;
import me.maximys777.Video.repository.CommentRepository;
import me.maximys777.Video.repository.UserRepository;
import me.maximys777.Video.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    public List<CommentResponse> getComments(@PathVariable Long videoId) {
        return  commentRepository.findByVideoId(videoId).stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }

    public String addComment(Long videoId, String text, String name) {
        UserEntity user = userRepository.findByName(name).orElseThrow(() -> new RuntimeException("Пользователь не найден."));
        VideoEntity video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("Видео не найдено."));

        CommentEntity comment = new CommentEntity();
        comment.setUser(user);
        comment.setVideo(video);
        comment.setText(text);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);
        return "Коментария добавлен!";
    }

    public CommentResponse addCommentWithoutAuth(Long videoId, String text, String name) {
        UserEntity user = userRepository.findByName(name).orElseThrow(() -> new RuntimeException("Пользователь не найден."));
        VideoEntity video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("Видео не найдено."));

        CommentEntity comment = new CommentEntity();
        comment.setUser(user);
        comment.setVideo(video);
        comment.setText(text);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        // Возвращаем объект CommentResponse
        return new CommentResponse(comment);
    }


    public String updateComment(Long commentId, String newText, String name) {
        UserEntity currentUser = userRepository.findByName(name).orElseThrow(() -> new RuntimeException("Пользователь не найден."));
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Комментарий не найден"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Недостаточно прав для редактирования коментария.");
        }

        comment.setText(newText);
        commentRepository.save(comment);
        return "Комментарий обновлён!";
    }

    public String deleteComment(Long commentId, String name) {
        UserEntity currentUser = userRepository.findByName(name).orElseThrow(() -> new RuntimeException("Пользователь не найден."));
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Комментарий не найден."));

        boolean isAdmin = currentUser.getRoles().contains("ADMIN");
        if (!comment.getUser().getId().equals(currentUser.getId()) && !isAdmin) {
            throw new RuntimeException("Недостаточно прав для удаления комментария");
        }

        commentRepository.delete(comment);
        return "Комментарий удалён!";
    }

}
