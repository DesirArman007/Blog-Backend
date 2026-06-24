package com.desirArman.blog.services;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface StorageService {

    String store(MultipartFile file, String filename);

    Optional<Resource> loadAsResource(UUID id);
}
