package com.desirArman.blog.services.impl;

import com.desirArman.blog.domain.dtos.CreateUserDto;
import com.desirArman.blog.domain.dtos.CreateUserResponseDto;
import com.desirArman.blog.domain.dtos.LoginRequest;
import com.desirArman.blog.domain.dtos.VerifyUserDto;
import com.desirArman.blog.domain.entities.User;
import com.desirArman.blog.exception.EmailSendingException;
import com.desirArman.blog.mapper.UserMapper;
import com.desirArman.blog.repositories.UserRepository;
import com.desirArman.blog.security.BlogUserDetails;
import com.desirArman.blog.services.EmailService;
import com.desirArman.blog.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public User signUp(CreateUserDto createUserDto) {
        User user = new User();
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return userRepository.save(user);
    }

    @Override
    public User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User don't exist with email: " + loginRequest.getEmail()));

        if (!user.isEnabled()) {
            throw new RuntimeException("User not verified. Please verify your account");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        return user;
    }

    @Override
    public void verifyUser(VerifyUserDto verifyUserDto) {
        User user = userRepository.findByEmail(verifyUserDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + verifyUserDto.getEmail()));

        if (user.isEnabled()) {
            throw new RuntimeException("User already verified.");
        }

        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code has expired.");
        }

        if (!user.getVerificationCode().equals(verifyUserDto.getVerificationCode())) {
            throw new RuntimeException("Invalid verification code.");
        }

        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);
    }

    @Override
    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        if (user.isEnabled()) {
            throw new RuntimeException("User already verified.");
        }

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10));
        sendVerificationEmail(user);
        userRepository.save(user);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999) + 999999;
        return String.valueOf(code);
    }

    private void sendVerificationEmail(User user) {
        String subject = "User Account verification Email";
        String verificationCode = user.getVerificationCode();

        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            throw new EmailSendingException("Failed to send verification email", e);
        }
    }

    @Override
    public List<CreateUserResponseDto> allUser() {
        Iterable<User> iterable = userRepository.findAll();

        List<User> users = StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());

        List<CreateUserResponseDto> usersList = users.stream()
                .map(user -> {
                    System.out.println("Mapping user: " + user.getEmail());
                    return userMapper.toDto(user);
                })
                .collect(Collectors.toList());
        return usersList;
    }


    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User don't exist with email: " + email));
        return user;

    }

    @Override
    public void deleteUser(UUID id) {
        UUID currentUserId = getCurrentUserId();
        // Check if the user is trying to delete their own account
        if (!currentUserId.equals(id)) {
            throw new AccessDeniedException("You are not allowed to delete another user's account.");
        }

        // Proceed to delete
        User user = getUserById(id);
        userRepository.deleteById(id);
        return;
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BlogUserDetails userDetails = (BlogUserDetails) authentication.getPrincipal();
        return userDetails.getId(); // Assuming BlogUserDetails has a getId() method returning UUID
    }


    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new EntityNotFoundException("No authenticated user found");
        }

        BlogUserDetails userDetails = (BlogUserDetails) authentication.getPrincipal();
        return userDetails.getUser(); // Fully authenticated User object
    }

}