package me.maximys777.Video.controller;

import lombok.RequiredArgsConstructor;
import me.maximys777.Video.entity.UserEntity;
import me.maximys777.Video.repository.UserRepository;
import me.maximys777.Video.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/SAO/user")
@RequiredArgsConstructor
public class UserController {
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;


    @PostMapping("/{id}/profile-picture")
    public ResponseEntity<String> uploadProfilePic(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден."));

        String profilePicUrl = cloudinaryService.uploadFile(file, "user_profile_pic");

        user.setProfilePic(profilePicUrl);
        userRepository.save(user);
        return ResponseEntity.ok("Фото профиля успешно обновлено");
    }
}
