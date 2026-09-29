package com.agri.app.service;

import com.agri.app.Repo.UserRepo;
import com.agri.app.dto.request.AddressRequest;
import com.agri.app.dto.request.ChangePasswordRequest;
import com.agri.app.dto.request.UpdateUserRequest;
import com.agri.app.dto.request.UserRequest;
import com.agri.app.dto.response.UserResponse;
import com.agri.app.entities.Address;
import com.agri.app.entities.User;
import com.agri.app.exception.ResourceNotFoundException;
import com.agri.app.mappers.UserMapper;
import com.agri.app.utils.IdentityValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo,UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User payload cannot be null to add user");
        }
        return userRepo.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserResponseById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userMapper.toUserResponse(userRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id)));
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepo.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByUsernameOrEmail(String value) {
        if (value == null || !IdentityValidator.isValidIdentifier(value)) {
            throw new IllegalArgumentException("Identifier must be a valid username or email");
        }
        Optional<User> userOptional = IdentityValidator.isUsername(value)
                ? userRepo.findByUsername(value)
                : userRepo.findByEmail(value);

        return userMapper.toUserResponse(userOptional.orElseThrow(() -> new NoSuchElementException("User not found with identifier: " + value)));
    }
    public boolean isUsernameUsed(String value) {
        if (!IdentityValidator.isUsername(value)) {
            throw new IllegalArgumentException("Identifier must be a valid username");
        }
        Optional<User> userOptional = userRepo.findByUsername(value);

        return userOptional.orElse(null) != null;
    }
    public boolean isEmailUsed(String value) {
        if (!IdentityValidator.isEmail(value)) {
            throw new IllegalArgumentException("Identifier must be a valid email");
        }
        Optional<User> userOptional = userRepo.findByEmail(value);

        return userOptional.orElse(null) != null;
    }

    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest updateUserRequest) {
        if (id == null || updateUserRequest == null) {
            throw new IllegalArgumentException("User ID or update payload cannot be null");
        }
        User existingUser = getUserById(id);

        if (updateUserRequest.fullName() != null && !updateUserRequest.fullName().isBlank()) {
            existingUser.setFullName(updateUserRequest.fullName().trim());
        }
        if (IdentityValidator.isEmail(updateUserRequest.email())) {
            existingUser.setEmail(updateUserRequest.email().toLowerCase().trim());
        }
        if (updateUserRequest.phoneNumber() != null && !updateUserRequest.phoneNumber().isBlank()) {
            existingUser.setPhoneNumber(updateUserRequest.phoneNumber().trim());
        }

        if (updateUserRequest.addresses() != null && !updateUserRequest.addresses().isEmpty()) {
            for (AddressRequest addressRequest : updateUserRequest.addresses()) {
                Address address = userMapper.toAddressEntity(addressRequest);
                addAddress(existingUser,address);
            }
        }

        return userMapper.toUserResponse(userRepo.save(existingUser));
    }

    public boolean deleteByUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User payload cannot be null to delete by user");
        }
        if (user.getId() != null && userRepo.existsById(user.getId())) {
            userRepo.delete(user);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteByUserId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null to delete by user UUID");
        }
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteByUsernameOrEmail(String value) {
        if (value == null || !IdentityValidator.isValidIdentifier(value)) {
            throw new IllegalArgumentException("User payload cannot be null to delete by username or email");
        }
        long deletedCount = IdentityValidator.isUsername(value)
                ? userRepo.deleteByUsername(value)
                : userRepo.deleteByEmail(value);
        return deletedCount > 0;
    }

    public void addAddress(User user, Address address) {
        user.addAddress(address);
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRequest == null) {
            throw new IllegalArgumentException("User payload cannot be null to add user");
        }
        if(isUsernameUsed(userRequest.username())){
            throw new IllegalArgumentException("Username already used");
        }
        if(isEmailUsed(userRequest.email())){
            throw new IllegalArgumentException("Email already used");
        }
        String hashedPassword = passwordEncoder.encode(userRequest.password());

        User finalUser = userMapper.toUserEntity(userRequest,hashedPassword);

        return userMapper.toUserResponse(userRepo.save(finalUser));
    }

    @Transactional
    public void changePassword(UUID id, ChangePasswordRequest changePasswordRequest) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        // 1. Verify that the current password matches
        if (!passwordEncoder.matches(changePasswordRequest.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match");
        }

        // 2. Prevent reusing the same password
        if (passwordEncoder.matches(changePasswordRequest.newPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }

        // 3. Hash and persist new password
        user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
    }
}

