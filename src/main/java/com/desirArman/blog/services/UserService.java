package com.desirArman.blog.services;

import aj.org.objectweb.asm.commons.Remapper;
import com.desirArman.blog.domain.dtos.CreateUserDto;
import com.desirArman.blog.domain.dtos.CreateUserResponseDto;
import com.desirArman.blog.domain.dtos.LoginRequest;
import com.desirArman.blog.domain.dtos.VerifyUserDto;
import com.desirArman.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
    User signUp(CreateUserDto createUserDto);
    User authenticate(LoginRequest loginRequest);
    void verifyUser(VerifyUserDto verifyUserDto);
    void resendVerificationCode(String email);


    List<CreateUserResponseDto> allUser();
    User getUserByEmail(String email);
    void deleteUser(UUID id);
    User getAuthenticatedUser();

}
