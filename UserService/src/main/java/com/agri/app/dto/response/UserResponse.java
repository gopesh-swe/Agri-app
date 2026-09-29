package com.agri.app.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        boolean emailVerified,
        String username,
        String phoneNumber,
        boolean phoneVerified,
        List<AddressResponse> addresses,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
) {}