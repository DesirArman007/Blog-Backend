package com.desirArman.blog.services.impl;

import com.desirArman.blog.domain.CreatePostRequest;
import com.desirArman.blog.domain.PostStatus;
import com.desirArman.blog.domain.UpdatePostRequest;
import com.desirArman.blog.domain.dtos.PostSearchRequestDto;
import com.desirArman.blog.domain.entities.Category;
import com.desirArman.blog.domain.entities.Post;
import com.desirArman.blog.domain.entities.Tag;
import com.desirArman.blog.domain.entities.User;
import com.desirArman.blog.repositories.PostRepository;
import com.desirArman.blog.security.BlogUserDetails;
import com.desirArman.blog.services.CategoryService;
import com.desirArman.blog.services.PostService;
import com.desirArman.blog.services.TagService;
import com.desirArman.blog.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    public final PostRepository postRepository;
    public final CategoryService categoryService;
    public final TagService tagService;
    public final UserService userService;

    private static final int WORD_PER_MINUTE=200;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPost(UUID categoryId, UUID tagId) {
        if(categoryId != null && tagId != null){
           Category category = categoryService.getCategoryById(categoryId);
           Tag tag = tagService.getTagById(tagId);
           return  postRepository.findAllByStatusAndCategoryAndTagsContaining(
                   PostStatus.PUBLISHED,
                   category,
                   tag
           );
        }

        if(categoryId != null){
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }

        if(tagId != null){
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag
            );
        }
        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }


    @Override
    public List<Post> getDraftPost(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
            newPost.setTitle(createPostRequest.getTitle());
            newPost.setContent(createPostRequest.getContent());
            newPost.setStatus(createPostRequest.getStatus());
            newPost.setAuthor(user);
            newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

            Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
            newPost.setCategory(category);

            Set<UUID> tagIds = createPostRequest.getTagIds();
            List<Tag> tags = tagService.getTagsById(tagIds);
            newPost.setTags(new HashSet<>(tags));

            return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Post not found with id: "+id));

        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)){
           Category category = categoryService.getCategoryById(updatePostRequestCategoryId);
           existingPost.setCategory(category);
        }

       Set<UUID> existingTagsIs = existingPost
               .getTags()
               .stream()
               .map(tag -> tag.getId())
               .collect(Collectors.toSet());

        Set<UUID> updatedTagsId = updatePostRequest.getTagIds();

        if(!existingTagsIs.equals(updatedTagsId)){
            List<Tag> tags = tagService.getTagsById(updatedTagsId);
            existingPost.setTags(new HashSet<>(tags));
        }

      return postRepository.save(existingPost);

    }

    private Integer calculateReadingTime(String content){
        if(content == null || content.isBlank()){
            return 0;
        }
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double)wordCount/WORD_PER_MINUTE);
    }

    @Override
    public Post getPost(UUID id) {
        return postRepository.findByIdWithAuthor(id)
                .orElseThrow(()-> new EntityNotFoundException("Post not found with id: "+id));
    }

    @Override
    public void deletePost(UUID id) {
        UUID currentUserId = getCurrentUserId();
        Post post = getPost(id);
        if (!post.getAuthor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not allowed to delete another user's post.");
        }
         postRepository.deleteById(id); ;
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BlogUserDetails userDetails = (BlogUserDetails) authentication.getPrincipal();
        return userDetails.getId(); // Assuming BlogUserDetails has a getId() method returning UUID
    }

    @Override
    public List<Post> getUserPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.PUBLISHED);
    }

    @Override
    public Post getPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
    }

}
