package me.maximys777.Video.controller;

import lombok.RequiredArgsConstructor;
import me.maximys777.Video.dto.request.CommentRequest;
import me.maximys777.Video.dto.response.CommentResponse;
import me.maximys777.Video.entity.CommentEntity;
import me.maximys777.Video.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SAO/video/{videoId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long videoId) {
        List<CommentResponse> comments = commentService.getComments(videoId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<String> addComment(@PathVariable Long videoId, @RequestBody String text) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            return new ResponseEntity<>("Вы не авторизованы.", HttpStatus.UNAUTHORIZED);
        }
        String username = userDetails.getUsername();
        String result = commentService.addComment(videoId, username, text);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/no-auth")
    public ResponseEntity<CommentResponse> addCommentWithoutAuth(@PathVariable Long videoId, @RequestBody String text, @RequestParam("name") String name) {
        CommentResponse commentResponse = commentService.addCommentWithoutAuth(videoId, text, name);
        return ResponseEntity.ok(commentResponse);
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId, @RequestBody String newText) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            return new ResponseEntity<>("Не авторизован", HttpStatus.UNAUTHORIZED);
        }
        String username = userDetails.getUsername();

        try {
            String result = commentService.updateComment(commentId, username, newText);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }

    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            return new ResponseEntity<>("Не авторизован", HttpStatus.UNAUTHORIZED);
        }
        String username = userDetails.getUsername();
        try {
            String result = commentService.deleteComment(commentId, username);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }


}
