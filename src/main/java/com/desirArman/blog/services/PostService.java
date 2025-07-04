package com.desirArman.blog.services;

import com.desirArman.blog.domain.CreatePostRequest;
import com.desirArman.blog.domain.UpdatePostRequest;
import com.desirArman.blog.domain.entities.Post;
import com.desirArman.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {

    Post getPost(UUID id);

    List<Post> getAllPost(UUID categoryId, UUID tagId);
    List<Post> getDraftPost(User user);

    Post createPost(User user, CreatePostRequest createPostRequest);

    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);

    public void deletePost(UUID id);
}
