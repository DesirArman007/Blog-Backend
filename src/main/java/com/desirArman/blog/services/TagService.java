package com.desirArman.blog.services;


import com.desirArman.blog.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {
    List<Tag> getTags();
    List<Tag> createTag(Set<String> tagNames);
    public void deleteTag(UUID id);

    Tag getTagById(UUID id);
    List<Tag> getTagsById(Set<UUID> ids);
}
