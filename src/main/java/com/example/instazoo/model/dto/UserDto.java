package com.example.instazoo.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long id;
    @NotEmpty
    String firstname;
    @NotEmpty
    String lastname;
    @NotEmpty
    String username;
    String bio;
}
