package com.desirArman.blog.controllers;


import com.desirArman.blog.domain.dtos.CreateTagsRequest;
import com.desirArman.blog.domain.dtos.TagDto;
import com.desirArman.blog.domain.entities.Tag;
import com.desirArman.blog.mapper.TagMapper;
import com.desirArman.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags(){
       List<Tag> tags =  tagService.getTags();
       List<TagDto> tagRespons = tags.stream().map(tag -> tagMapper.toTagResponse(tag)).toList();
       return ResponseEntity.ok(tagRespons);
    }

    @PostMapping
    public ResponseEntity<List<TagDto>> createTags(
             @RequestBody CreateTagsRequest createTagsRequest){
      List<Tag> savedTags =  tagService.createTag(createTagsRequest.getNames());
      List<TagDto> createdTagRespons = savedTags.stream().map(tag -> tagMapper.toTagResponse(tag)).toList();
      return new ResponseEntity<>(
              createdTagRespons,
              HttpStatus.CREATED
      );
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id){
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
