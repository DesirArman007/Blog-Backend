package com.desirArman.blog.domain.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeToggleResponse {
    boolean liked;
    int likeCount;
    private UUID postId;
    private String message;
}
