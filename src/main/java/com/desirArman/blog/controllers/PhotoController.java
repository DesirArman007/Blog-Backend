package com.desirArman.blog.controllers;

import com.desirArman.blog.domain.dtos.PhotoDto;
import com.desirArman.blog.domain.entities.Photo;
import com.desirArman.blog.domain.entities.Post;
import com.desirArman.blog.mapper.PhotoMapper;
import com.desirArman.blog.services.PhotoService;
import com.desirArman.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final PostService postService;
    private final PhotoMapper photoMapper;


    @PostMapping
    public PhotoDto uploadPhoto(@RequestParam("file") MultipartFile file){
        Photo savedPhoto = photoService.uploadPhoto(file);
        return photoMapper.toDto(savedPhoto);
    }

    @GetMapping(path = "/{id:.+}" )
    public ResponseEntity<Resource> getPhoto(@PathVariable UUID id){
        return  photoService.getPhotoAsResource(id).map(photo ->
                ResponseEntity.ok()
                        .contentType(
                                MediaTypeFactory.getMediaType(photo)
                                        .orElse(MediaType.APPLICATION_OCTET_STREAM)
                        )
                        .header(HttpHeaders.CONTENT_DISPOSITION,"inline")
                        .body(photo)
        ).orElse(ResponseEntity.notFound().build());
    }

}
