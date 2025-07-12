package com.desirArman.blog.services.impl;

import com.desirArman.blog.domain.dtos.LikeToggleResponse;
import com.desirArman.blog.domain.entities.Like;
import com.desirArman.blog.domain.entities.Post;
import com.desirArman.blog.domain.entities.User;
import com.desirArman.blog.repositories.LikeRepository;
import com.desirArman.blog.security.BlogUserDetails;
import com.desirArman.blog.services.LikeService;
import com.desirArman.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final PostService postService;
    private final LikeRepository likeRepository;

    @Override
    public LikeToggleResponse toggleLike(UUID id) {
        User currentUser = getCurrentUser();
        Post post = postService.getPostById(id); // Make sure this method throws if not found

        Optional<Like> existingLike = likeRepository.findByPostAndAuthor(post, currentUser); // ✅ FIXED

        boolean liked;

        if(existingLike.isPresent()){
            likeRepository.delete(existingLike.get());
            liked=false;
        } else{
            Like newLike = new Like();
            newLike.setPost(post);
            newLike.setAuthor(currentUser);
            likeRepository.save(newLike);
            liked = true;
        }

        int likeCount = likeRepository.countByPost(post);

        return new LikeToggleResponse(
                liked,
                likeCount,
                post.getId(),
                liked ? "Post liked successfully" : "Post unliked successfully"
        );
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BlogUserDetails userDetails = (BlogUserDetails) authentication.getPrincipal();
        return userDetails.getUser(); // Assuming BlogUserDetails has a getId() method returning UUID
    }


}
