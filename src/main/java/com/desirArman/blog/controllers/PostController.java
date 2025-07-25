package com.desirArman.blog.controllers;

import com.desirArman.blog.domain.CreatePostRequest;
import com.desirArman.blog.domain.UpdatePostRequest;
import com.desirArman.blog.domain.dtos.CreatePostRequestDto;
import com.desirArman.blog.domain.dtos.PostDto;
import com.desirArman.blog.domain.dtos.PostSearchRequestDto;
import com.desirArman.blog.domain.dtos.UpdatePostRequestDto;
import com.desirArman.blog.domain.entities.Post;
import com.desirArman.blog.domain.entities.User;
import com.desirArman.blog.mapper.PostMapper;
import com.desirArman.blog.services.PostService;
import com.desirArman.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId){

        List<Post> posts = postService.getAllPost(categoryId, tagId);
        List<PostDto> fetchedPost = posts.stream().map(post -> postMapper.toDto(post)).toList();
        return ResponseEntity.ok(fetchedPost);

    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId){
            User loggedInUser = userService.getUserById(userId);
            List<Post> draftPosts = postService.getDraftPost(loggedInUser);

            List<PostDto> draftPostDtos = draftPosts.stream().map(post -> postMapper.toDto(post)).toList();
            return ResponseEntity.ok(draftPostDtos);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId
            ){
        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);

        Post createdPost = postService.createPost(loggedInUser,createPostRequest);
        PostDto createdPostDto =  postMapper.toDto(createdPost);

        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto){

      UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
      Post updatedPost = postService.updatePost(id, updatePostRequest);
      PostDto updatedPostDto = postMapper.toDto(updatedPost);

      return new ResponseEntity<>(updatedPostDto, HttpStatus.ACCEPTED);

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(
            @PathVariable UUID id ){

       Post post = postService.getPost(id);
       PostDto postDto = postMapper.toDto(post);

       return ResponseEntity.ok(postDto);
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/myPosts")
    public ResponseEntity<List<PostDto>> getMyPublishedPosts(@RequestAttribute UUID userId){
        User loggedInUser = userService.getUserById(userId);
        List<Post> userPosts = postService.getUserPosts(loggedInUser);
        List<PostDto> userPostsDto = userPosts.stream().map(post->postMapper.toDto(post)).toList();
        return ResponseEntity.ok(userPostsDto);
    }

}
