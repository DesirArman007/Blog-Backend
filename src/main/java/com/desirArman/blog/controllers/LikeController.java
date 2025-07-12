package com.desirArman.blog.controllers;

import com.desirArman.blog.domain.dtos.LikeToggleResponse;
import com.desirArman.blog.services.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping(path = "/{id}")
    public ResponseEntity<LikeToggleResponse> likePost(@PathVariable UUID id){
       LikeToggleResponse like =  likeService.toggleLike(id);
       return ResponseEntity.ok(like);
    }
}
