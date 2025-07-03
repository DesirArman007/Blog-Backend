package com.desirArman.blog.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTagsRequest {

    @NotBlank(message = "Tag name is required")
    @Size(max = 18, message = "Maximum {max} tags allowed")
    private Set<
            @Size(min = 2, max = 30, message = "Tag name must be between {min} and {max} characte@Pattern")
            @Pattern (regexp="^[\\w\\s-]+$", message = "Tag name can only contain letters, numbers, spaces, and Hyphons ")
            String> names;

}
