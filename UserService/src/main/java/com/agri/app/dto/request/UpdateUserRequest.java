package com.agri.app.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateUserRequest(

        @NotBlank(message = "FullName cannot be blank")
        @Size(min = 3,max = 100,message = "Size must be in between 3 and 100")
        String fullName,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be in valid format")
        String email,
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid E.164 phone number format")
        String phoneNumber,
        @Valid
        List<AddressRequest> addresses

) {}
