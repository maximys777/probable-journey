package me.maximys777.Video.controller;

import lombok.RequiredArgsConstructor;
import me.maximys777.Video.entity.VideoEntity;
import me.maximys777.Video.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/SAO/video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/add")
    public ResponseEntity<VideoEntity> addVideo(
            @RequestParam("videoUrl") String videoUrl,
            @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("categoryName") String categoryName,
            @RequestParam("userName") String userName) {
        VideoEntity addVideoWithThumbnail = videoService.addVideo(videoUrl, thumbnailFile, title, description, categoryName, userName);
        return ResponseEntity.ok(addVideoWithThumbnail);
    }

    @GetMapping("/{title}")
    public ResponseEntity<VideoEntity> getVideo(@PathVariable String title) {
        VideoEntity video = videoService.getVideo(title);
        return ResponseEntity.ok(video);
    }

    @GetMapping
    public ResponseEntity<List<VideoEntity>> getAllVideos() {
        List<VideoEntity> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    @PutMapping("/{videoId}")
    public ResponseEntity<VideoEntity> updateVideo(@PathVariable Long videoId,
                                                   @RequestParam(value = "videoUrl", required = false) String videoUrl,
                                                   @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
                                                   @RequestParam(value = "title", required = false) String title,
                                                   @RequestParam(value = "description", required = false) String description,
                                                   @RequestParam(value = "categoryName", required = false) String categoryName) {
       VideoEntity updatedVideo = videoService.updateVideo(videoId, videoUrl, thumbnailFile, title, description, categoryName);
       return ResponseEntity.ok(updatedVideo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VideoEntity> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.ok().build();
    }
}
