package com.agri.app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "user_addresses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends TimeStamps {

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
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

}