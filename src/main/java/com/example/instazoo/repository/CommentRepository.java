package com.example.instazoo.repository;

import com.example.instazoo.model.Comment;
import com.example.instazoo.model.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    Comment findByIdAndUserId(Long commentId, Long userId);
}
