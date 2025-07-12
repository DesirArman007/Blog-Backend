package com.desirArman.blog.controllers;

import com.desirArman.blog.domain.dtos.CreateUserResponseDto;
import com.desirArman.blog.domain.entities.User;
import com.desirArman.blog.mapper.UserMapper;
import com.desirArman.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path ="/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")  // Adding "/me" or something to distinguish the endpoint
    public ResponseEntity<CreateUserResponseDto> getAuthenticatedUserInfo() {
        User user = userService.getAuthenticatedUser(); // call your service method
        CreateUserResponseDto dto = userMapper.toDto(user);
        return ResponseEntity.ok(dto);
    }


    @GetMapping(path = "/getUsers")
    public List<CreateUserResponseDto> listAllUsers(){
        return userService.allUser();
    }

    @GetMapping(path = "/byEmail")
    public ResponseEntity<CreateUserResponseDto> getUserByEmail(@RequestParam String email){
        User user = userService.getUserByEmail(email);
        CreateUserResponseDto dto = userMapper.toDto(user);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
