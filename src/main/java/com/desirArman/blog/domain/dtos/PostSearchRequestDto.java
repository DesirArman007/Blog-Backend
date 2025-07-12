package com.desirArman.blog.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostSearchRequestDto {

    private String keyword;

    private UUID authorId;

    private Set<String> tags;

    private Set<String> categories;

    private String sortBy="date";

    private String orderBy = "desc";

    private Integer page = 0;

    private Integer size = 10;

}
