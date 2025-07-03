package com.desirArman.blog.services.impl;

import com.desirArman.blog.domain.entities.Tag;
import com.desirArman.blog.mapper.TagMapper;
import com.desirArman.blog.repositories.TagRepository;
import com.desirArman.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Tag> getTags(){
        return tagRepository.findAllPostByTags();
    }

    @Transactional
    @Override
    public List<Tag> createTag(Set<String> tagNames) {
        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);

        Set<String> existingTagNames = existingTags
                .stream()
                .map(tag -> tag.getName())
                .collect(Collectors.toSet());

        List<Tag> newTags = tagNames.stream()
                 .filter(tagName -> !existingTagNames.contains(tagName))
                 .map(tagName -> Tag
                         .builder()
                         .name(tagName)
                         .posts(new HashSet<>())
                         .build())
                 .collect(Collectors.toList());

        List<Tag> savedTags = new ArrayList<>();
        if(!newTags.isEmpty()){
            savedTags = tagRepository.saveAll(newTags);
        }

        savedTags.addAll(existingTags);

        return savedTags;
    }

    @Override
    public void deleteTag(UUID id) {
        Optional<Tag> tag = tagRepository.findById(id);

        if(tag.isPresent()){
            if(!tag.get().getPosts().isEmpty()){
               throw new IllegalStateException("Tags has no post associated with it");
            }
            tagRepository.deleteById(id);
        }
    }

    @Override
    public Tag getTagById(UUID id) {
        return tagRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Tag not found with id: "+id));
    }

    @Override
    public List<Tag> getTagsById(Set<UUID> ids) {
        List<Tag> foundTags =  tagRepository.findAllById(ids);

        if(foundTags.size() != ids.size()){
            throw new EntityNotFoundException("Not all Specified tag ID's exsist");
        }
        return foundTags;
    }
}
