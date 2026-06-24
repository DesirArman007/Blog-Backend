package com.desirArman.blog.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserDto {

    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Name can only contain letters, numbers, spaces, and hyphens")
    private String name;

    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Invalid email format"
    )
    private String email;

    @NotBlank(message = "You cannot leave the password blank")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must be 8+ characters with at least one digit, one uppercase, one lowercase, and one special character"
    )
    private String password;
}
