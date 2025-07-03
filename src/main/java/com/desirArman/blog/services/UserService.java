package com.desirArman.blog.services;

import com.desirArman.blog.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
