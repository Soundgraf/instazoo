package com.example.instazoo.repository;

import com.example.instazoo.model.ImageModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, Long> {

    Optional<ImageModel> findByUserId(Long id);

    Optional<ImageModel> findByPostId(Long id);
}
