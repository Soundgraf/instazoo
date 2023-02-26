package com.example.instazoo.service;

import com.example.instazoo.exception.PostNotFoundException;
import com.example.instazoo.model.ImageModel;
import com.example.instazoo.model.Post;
import com.example.instazoo.model.User;
import com.example.instazoo.model.dto.PostDto;
import com.example.instazoo.repository.ImageRepository;
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
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public Post createPost(PostDto postDto, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDto.getCaption());
        post.setLocation(postDto.getLocation());
        post.setTitle(postDto.getTitle());
        post.setLikes(0);

        log.info("Saving Post for User: {}", user.getEmail());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Post getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
            .orElseThrow(() -> new PostNotFoundException("Post cannot be found for user: " + user.getEmail()));
    }

    public List<Post> getAllPostsForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByCreatedAtDesc(user);
    }

    public Post likePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        Optional<String> userLiked = post.getLikedUser().stream()
            .filter(u -> u.equals(username))
            .findAny();
        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUser().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUser().add(username);
        }
        return postRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
        imageModel.ifPresent(imageRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found with Username: " + username));
    }
}
