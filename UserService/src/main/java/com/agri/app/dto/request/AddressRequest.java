package com.agri.app.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequest(
        @NotBlank(message = "House No. is required")
         String houseNo,
         String street,
         @NotBlank(message = "area is required")
         String area,
         @NotBlank(message = "Village is required")
         String village,
         @NotBlank(message = "District is required")
         String district,
         @NotBlank(message = "State is required")
         String state,
         @NotBlank(message = "Country is required")
         String country,
         @NotBlank(message = "Postal Code is required")
         String postalCode,

         @NotBlank(message = "Address Type is required")
         String addressType,

         @NotNull(message = "Latitude is required")
         @DecimalMin(value = "-90.0", message = "Latitude must be >= -90.0")
         @DecimalMax(value = "90.0", message = "Latitude must be <= 90.0")
        Double latitude,
         @NotNull(message = "Longitude is required")
         @DecimalMin(value = "-180.0", message = "Longitude must be >= -180.0")
         @DecimalMax(value = "180.0", message = "Longitude must be <= 180.0")
        Double longitude
) {}
