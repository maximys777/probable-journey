package me.maximys777.Video.service;

import lombok.RequiredArgsConstructor;
import me.maximys777.Video.entity.CategoryEntity;
import me.maximys777.Video.entity.UserEntity;
import me.maximys777.Video.entity.VideoEntity;
import me.maximys777.Video.repository.CategoryRepository;
import me.maximys777.Video.repository.UserRepository;
import me.maximys777.Video.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final CloudinaryService cloudinaryService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public VideoEntity addVideo(String videoUrl,
                                MultipartFile thumbnailFile,
                                String title,
                                String description,
                                String categoryName,
                                String userName) {
        // Загрузка превью
            String thumbnailUrl = cloudinaryService.uploadFile(thumbnailFile, "thumbnails", "image");

            CategoryEntity category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new RuntimeException("Категория " + categoryName + " не найдена."));

            UserEntity user = userRepository.findByName(userName)
                    .orElseThrow(() -> new RuntimeException("Пользователь " + userName + " не найден."));

            VideoEntity video = new VideoEntity();
            video.setTitle(title);
            video.setDescription(description);
            video.setThumbnail(thumbnailUrl);
            video.setCategory(category);
            video.setUser(user);
            video.setViews(0L);
            video.setLink(videoUrl);

            return videoRepository.save(video);
    }

    public VideoEntity updateVideo(Long videoId,
                                   String videoUrl,
                                   MultipartFile thumbnailFile,
                                   String title,
                                   String description,
                                   String categoryName) {
        VideoEntity video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Видео не найдено."));

        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            String thumbnailUrl = cloudinaryService.uploadFile(thumbnailFile, "thumbnails", "image");
            video.setThumbnail(thumbnailUrl);
        }
        if (videoUrl != null && !videoUrl.isBlank()) {
            video.setLink(videoUrl);
        }
        if (title != null && !title.isBlank()) {
            video.setTitle(title);
        }
        if (description != null && !description.isBlank()) {
            video.setDescription(description);
        }
        if (categoryName != null && !categoryName.isBlank()) {
            CategoryEntity category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new RuntimeException("Категория " + categoryName + " не найдена."));
            video.setCategory(category);
        }

        return videoRepository.save(video);
    }

    public void deleteVideo(Long id) {
        VideoEntity video = videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Видео не найдено."));
        videoRepository.delete(video);
    }

    public VideoEntity getVideo(String title) {
        return videoRepository.findByTitle(title).orElseThrow(() -> new RuntimeException("Видео " + title + "не найдено."));
    }

    public List<VideoEntity> getAllVideos() {
        return videoRepository.findAll();
    }
}
