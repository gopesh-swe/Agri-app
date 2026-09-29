package com.agri.app.dto.response;

import java.util.UUID;

public record AddressResponse(
        UUID id,
        String houseNo,
        String street,
        String area,
        String village,
        String district,
        String state,
        String country,
        String postalCode,
        String addressType,
        Double latitude,
        Double longitude
) {}