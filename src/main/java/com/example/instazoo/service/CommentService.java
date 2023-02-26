package com.example.instazoo.service;

import com.example.instazoo.exception.PostNotFoundException;
import com.example.instazoo.model.Comment;
import com.example.instazoo.model.Post;
import com.example.instazoo.model.User;
import com.example.instazoo.model.dto.CommentDto;
import com.example.instazoo.repository.CommentRepository;
import com.example.instazoo.repository.PostRepository;
import com.example.instazoo.repository.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Comment saveComment(Long postId, CommentDto commentDto, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + user.getEmail()));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDto.getMessage());

        log.info("Saving comment for post: {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        return commentRepository.findAllByPost(post);
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found with Username: " + username));
    }
}
