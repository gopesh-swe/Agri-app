package com.agri.app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "user_addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String houseNo;
    private String street;
    private String area;
    private String village;
    private String district;
    private String state;
    private String country;
    private String postalCode;

    private String addressType; // e.g. "RESIDENTIAL", "FARM_OFFICE", "WAREHOUSE"

    // Coordinate storage for the address
    @Column(name = "latitude", precision = 10, scale = 8)
    private Double latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}