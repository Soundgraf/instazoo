package com.example.instazoo.controller;

import com.example.instazoo.model.dto.PostDto;
import com.example.instazoo.model.entity.Post;
import com.example.instazoo.model.mapper.PostFacade;
import com.example.instazoo.model.mapper.PostMapper;
import com.example.instazoo.payload.response.MessageResponse;
import com.example.instazoo.service.PostService;
import com.example.instazoo.validation.ResponseErrorValidation;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final PostFacade postFacade;
    private final PostMapper postMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody
    PostDto postDto, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        Post post = postService.createPost(postDto, principal);
        PostDto createdPost = postMapper.postToPostDto(post);
        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> postDtoList = postService.getAllPosts()
            .stream()
            .map(postMapper::postToPostDto)
            .toList();
        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDto>> getAllPostsForUser(Principal principal) {
        List<PostDto> postDtoList = postService.getAllPostsForUser(principal)
            .stream()
            .map(postMapper::postToPostDto)
            .toList();
        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostDto> likePost(
        @PathVariable("postId") String postId,
        @PathVariable("username") String username) {
        Post post = postService.likePost(Long.valueOf(postId), username);
        PostDto postDTO = postMapper.postToPostDto(post);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable String postId, Principal principal) {
        postService.deletePost(Long.valueOf(postId), principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
