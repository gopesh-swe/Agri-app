package com.agri.app.mappers;

import com.agri.app.dto.request.AddressRequest;
import com.agri.app.dto.request.UpdateUserRequest;
import com.agri.app.dto.request.UserRequest;
import com.agri.app.dto.response.AddressResponse;
import com.agri.app.dto.response.UserResponse;
import com.agri.app.entities.Address;
import com.agri.app.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public User toUserEntity(UserRequest userRequest, String encodedPassword) {
        if (userRequest == null) {
            return null;
        }

        User user = User.builder()
                .fullName(userRequest.fullName() != null ? userRequest.fullName().trim() : null)
                .email(userRequest.email() != null ? userRequest.email().toLowerCase().trim() : null)
                .username(userRequest.username() != null ? userRequest.username().trim() : null)
                .password(encodedPassword)
                .phoneNumber(userRequest.phoneNumber() != null ? userRequest.phoneNumber().trim() : null)
                .build();

        if (userRequest.addresses() != null) {
            userRequest.addresses().forEach(address -> user.addAddress(toAddressEntity(address)));
        }
        return user;
    }

    public Address toAddressEntity(AddressRequest addressRequest) {
        if (addressRequest == null) {
            return null;
        }
        Address address = Address.builder()
                .houseNo(addressRequest.houseNo())
                .area(addressRequest.area())
                .addressType(addressRequest.addressType())
                .village(addressRequest.village())
                .district(addressRequest.district())
                .state(addressRequest.state())
                .country(addressRequest.country())
                .postalCode(addressRequest.postalCode())
                .latitude(addressRequest.latitude())
                .longitude(addressRequest.longitude())
                .build();

        if (addressRequest.street() != null) {
            address.setStreet(addressRequest.street());
        }
        return address;
    }

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        List<AddressResponse> addressResponses = user.getAddresses() != null
                ? user.getAddresses().stream().map(this::toAddressResponse).toList()
                : List.of();

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.isEmailVerified(),
                user.getUsername(),
                user.getPhoneNumber(),
                user.isPhoneVerified(),
                addressResponses,
                user.getCreatedAt(),
                user.getLastUpdatedAt()
        );
    }

    public AddressResponse toAddressResponse(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressResponse(
                address.getId(),
                address.getHouseNo(),
                address.getStreet(),
                address.getArea(),
                address.getVillage(),
                address.getDistrict(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode(),
                address.getAddressType(),
                address.getLatitude(),
                address.getLongitude()
        );
    }
}
