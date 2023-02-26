package com.example.instazoo.model.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDto {

    Long id;
    @NotEmpty
    String title;
    @NotEmpty
    String caption;
    String location;
    String username;
    Integer likes;
    Set<String> usersLiked;
}
