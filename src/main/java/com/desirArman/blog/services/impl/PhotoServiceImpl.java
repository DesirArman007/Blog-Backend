package com.desirArman.blog.services.impl;

import com.desirArman.blog.domain.entities.Photo;
import com.desirArman.blog.domain.entities.Post;
import com.desirArman.blog.repositories.PhotoRepository;
import com.desirArman.blog.services.PhotoService;
import com.desirArman.blog.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final StorageService storageService;
    private final PhotoRepository photoRepository;

    @Override
    public Photo uploadPhoto(MultipartFile file) {
        UUID id = UUID.randomUUID();
        String pId = id.toString();
        String url = storageService.store(file, pId);
        log.info("Uploaded photo url: {}",url);
        Photo photo =  Photo.builder()
                .url(url)
                .uploadDate(LocalDateTime.now())
                .build();
        log.info("Id of the photo is : {}",photo.getId());
        photoRepository.save(photo);
        return photo;
    }

    @Override
    public Optional<Resource> getPhotoAsResource(UUID id) {
        return storageService.loadAsResource(id);
    }


}