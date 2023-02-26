package com.example.instazoo.model.mapper;

import com.example.instazoo.model.dto.CommentDto;
import com.example.instazoo.model.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {

    public CommentDto commentToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setUsername(comment.getUsername());
        commentDto.setMessage(comment.getMessage());
        return commentDto;
    }
}
