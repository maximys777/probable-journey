package me.maximys777.Video.repository;

import me.maximys777.Video.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, Long> {

    Optional<VideoEntity> findByTitle(String title);
}
