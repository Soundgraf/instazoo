package com.example.instazoo.model.mapper;

import com.example.instazoo.model.dto.PostDto;
import com.example.instazoo.model.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

    public PostDto postToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setUsername(post.getUser().getUsername());
        postDto.setId(post.getId());
        postDto.setCaption(post.getCaption());
        postDto.setLikes(post.getLikes());
        postDto.setUsersLiked(post.getLikedUser());
        postDto.setLocation(post.getLocation());
        postDto.setTitle(post.getTitle());
        return postDto;
    }
}
