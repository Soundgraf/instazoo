package com.example.instazoo.repository;

import com.example.instazoo.model.Post;
import com.example.instazoo.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserOrderByCreatedAtDesc(User user);

    List<Post> findAllByOrderByCreatedAtDesc();

    Optional<Post> findPostByIdAndUser(Long id, User user);
}
