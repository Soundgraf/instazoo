package com.example.instazoo.controller;

import com.example.instazoo.model.dto.CommentDto;
import com.example.instazoo.model.entity.Comment;
import com.example.instazoo.model.mapper.CommentFacade;
import com.example.instazoo.model.mapper.CommentMapper;
import com.example.instazoo.payload.response.MessageResponse;
import com.example.instazoo.service.CommentService;
import com.example.instazoo.validation.ResponseErrorValidation;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
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
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final CommentFacade commentFacade;
    private final CommentMapper commentMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(
        @Valid @RequestBody CommentDto commentDto,
        @PathVariable("postId") String postId,
        BindingResult bindingResult,
        Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Comment comment = commentService.saveComment(Long.valueOf(postId), commentDto, principal);
        CommentDto createdComment = commentMapper.commentToCommentDto(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDto>> getAllCommentsToPost(@PathVariable String postId) {
        List<CommentDto> commentDTOList = commentService.getAllCommentsForPost(Long.valueOf(postId))
            .stream()
            .map(commentMapper::commentToCommentDto)
            .toList();
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(Long.valueOf(commentId));
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
