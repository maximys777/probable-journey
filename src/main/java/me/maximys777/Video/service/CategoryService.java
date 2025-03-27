package me.maximys777.Video.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.maximys777.Video.dto.request.CategoryRequest;
import me.maximys777.Video.dto.response.CategoryResponse;
import me.maximys777.Video.dto.response.VideoResponse;
import me.maximys777.Video.entity.CategoryEntity;
import me.maximys777.Video.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(CategoryRequest request) {
        checkIsValidName(request.getName());

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(request.getName());
        categoryEntity.setDescription(request.getDescription());

        LocalDateTime createdAt = LocalDateTime.now();
        categoryEntity.setCreatedAt(createdAt);

        CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);
        log.info("Категория с именем {} создана.", savedCategoryEntity.getName());

        return CategoryResponse.builder()
                .name(savedCategoryEntity.getName())
                .description(savedCategoryEntity.getDescription())
                .createdAt(savedCategoryEntity.getCreatedAt())
                .build();
    }

    public CategoryResponse updateCategory(CategoryRequest request, String name) {
        CategoryEntity categoryEntity = categoryRepository.findByName(name)
                .orElseThrow(()-> {
                    log.warn("Категория с именем {} не найдена", name);
                   return new RuntimeException("Категория не найдена.");
                });

        if(request.getDescription() != null && !request.getDescription().isBlank()) {
            categoryEntity.setDescription(request.getDescription());
        }
        categoryEntity.setUpdatedAt(LocalDateTime.now());

        CategoryEntity updatedCategory = categoryRepository.save(categoryEntity);
        log.info("Категория с именем {} обновлена.", updatedCategory.getName());

        return CategoryResponse.builder()
                .name(updatedCategory.getName())
                .description(updatedCategory.getDescription())
                .updatedAt(updatedCategory.getUpdatedAt())
                .build();
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> CategoryResponse.builder()
                        .name(c.getName())
                        .description(c.getDescription())
                        .createdAt(c.getCreatedAt())
                        .updatedAt(c.getUpdatedAt())
                        .videos(
                                c.getVideos().stream()
                                        .map(videoEntity -> VideoResponse.builder()
                                                .title(videoEntity.getTitle())
                                                .thumbnail(videoEntity.getThumbnail())
                                                .categoryName(videoEntity.getCategory().getName())
                                                .createdAt(videoEntity.getCreatedAt())
                                                .userName(videoEntity.getUser().getName())
                                                .userBanner(videoEntity.getUser().getProfilePic())
                                                .build()
                                        )
                                        .collect(Collectors.toList())
                        )
                        .build()
                )
                .collect(Collectors.toList());
    }


    public CategoryResponse getCategoryByName(String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Данное поле не может быть пустым.");
        }

        CategoryEntity entity = categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Категория с названием " + name + " не найдена."));
        log.info("Получены данные категории {}", entity.getName());

        return CategoryResponse.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .videos(
                        entity.getVideos().stream()
                                .map(videoEntity -> VideoResponse.builder()
                                        .title(videoEntity.getTitle())
                                        .thumbnail(videoEntity.getThumbnail())
                                        .description(videoEntity.getDescription())
                                        .categoryName(videoEntity.getCategory().getName())
                                        .views(videoEntity.getViews())
                                        .createdAt(videoEntity.getCreatedAt())
                                        .userName(videoEntity.getUser().getName())
                                        .userBanner(videoEntity.getUser().getProfilePic())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();
    }


    public void deleteCategory(String name) {
        CategoryEntity categoryEntity = categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Категория не найдена."));
        categoryRepository.delete(categoryEntity);
        log.info("Категория с именем {} успешно удалена", name);
    }



    public void checkIsValidName(String name) {
        if(Boolean.TRUE.equals(categoryRepository.existsByName(name))) throw new RuntimeException("Категория не может быть без имени или " + name + " уже существует.");
    }
}
