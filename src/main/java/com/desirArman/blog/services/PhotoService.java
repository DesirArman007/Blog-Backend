package com.desirArman.blog.services;

import com.desirArman.blog.domain.entities.Photo;
import com.desirArman.blog.domain.entities.Post;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhotoService {

    Photo uploadPhoto(MultipartFile file);

    Optional<Resource> getPhotoAsResource(UUID id);

}