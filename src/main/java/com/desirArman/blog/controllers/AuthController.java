package com.desirArman.blog.controllers;


import com.desirArman.blog.domain.dtos.*;
import com.desirArman.blog.domain.entities.User;
import com.desirArman.blog.mapper.UserMapper;
import com.desirArman.blog.security.BlogUserDetails;
import com.desirArman.blog.services.AuthenticationService;
import com.desirArman.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping(path = "/signup")
    private ResponseEntity<CreateUserResponseDto> registerUser(@RequestBody CreateUserDto createUserDto){
        User registerUser = userService.signUp(createUserDto);
        CreateUserResponseDto registerUserDto = userMapper.toDto(registerUser);
        return new ResponseEntity<>(registerUserDto, HttpStatus.CREATED);
    }


    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        User userDetails = userService.authenticate(loginRequest);
        BlogUserDetails user = new BlogUserDetails(userDetails);
        String tokenValue = authenticationService.generateToken(user);
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(path = "/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto){
        userService.verifyUser(verifyUserDto);
        return ResponseEntity.ok("Account verified Successfully");
    }

    @PostMapping(path = "/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
        userService.resendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent! ");
    }



}
