package com.desirArman.blog.domain.dtos;

import com.desirArman.blog.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostRequestDto {

    @NotBlank(message = "Post title is required")
    @Size(min = 2, max = 298, message = "Post title must be in between {min} and {max} characters")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Post title can only contain Letters, numbers, spaces and hyphens")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 2, max = 298, message = "Content must be in between {min} and {max} characters")
    private String content;

    @NotNull(message = "Category Id is required")
    private UUID categoryId;

    @Builder.Default
    @Size(max=10, message = "Maximum {max} tags Allowed")
    private Set<UUID> tagIds = new HashSet<>();

    @NotNull(message = "Status is Required")
    private PostStatus status;
}
