package com.agri.app.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record UserRequest (
        @NotBlank(message = "FullName cannot be blank")
        @Size(min = 3,max = 100,message = "Size must be in between 3 and 100")
        String fullName,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be in valid format")
        String email,
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid E.164 phone number format")
        String phoneNumber,
        @NotBlank(message = "Username is required")
        @Pattern(regexp = "^[a-zA-Z0-9._-]{3,20}$", message = "Username must be 3-20 alphanumeric characters")
        String username,
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,
        @Valid
        List<AddressRequest> addresses
)
{}

