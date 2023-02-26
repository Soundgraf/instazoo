package com.example.instazoo.model.mapper;

import com.example.instazoo.model.dto.PostDto;
import com.example.instazoo.model.entity.Post;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostMapper {

    PostDto postToPostDto(Post post);
}
