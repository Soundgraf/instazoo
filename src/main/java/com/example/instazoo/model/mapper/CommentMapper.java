package com.example.instazoo.model.mapper;

import com.example.instazoo.model.dto.CommentDto;
import com.example.instazoo.model.entity.Comment;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

    CommentDto commentToCommentDto(Comment comment);
}
