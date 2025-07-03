package com.desirArman.blog.services.impl;

import com.desirArman.blog.domain.entities.User;
import com.desirArman.blog.repositories.UserRepository;
import com.desirArman.blog.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(()->  new EntityNotFoundException("User not found with id: "+id));
    }
}
