package com.desirArman.blog.services;

import com.desirArman.blog.domain.dtos.LikeToggleResponse;
import com.desirArman.blog.domain.entities.Like;

import java.util.UUID;

public interface LikeService {

    LikeToggleResponse toggleLike(UUID id);
}
