package com.agri.app.controllers;

import com.agri.app.dto.request.ChangePasswordRequest;
import com.agri.app.dto.request.UpdateUserRequest;
import com.agri.app.dto.request.UserRequest;
import com.agri.app.dto.response.ApiResponse;
import com.agri.app.dto.response.UserResponse;
import com.agri.app.mappers.UserMapper;
import com.agri.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
    }

    /**
     * Create a new user
     * POST /api/v1/users
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.createUser(userRequest);
        ApiResponse<UserResponse> apiResponse = ApiResponse.ok("User Created Successfully at : " + createdUser.createdAt().toString(),createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * Get user by UUID
     * GET /api/v1/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
        UserResponse userResponse = userService.getUserResponseById(id);
        ApiResponse<UserResponse> apiResponse = ApiResponse.ok("User Found",userResponse);
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Get all users
     * GET /api/v1/users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> responses = userService.getAllUsers();
        ApiResponse<List<UserResponse>> apiResponse = ApiResponse.ok("List Of All Users",responses);
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Search user by username or email
     * GET /api/v1/users/search?identifier=...
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByIdentifier(@RequestParam("identifier") String identifier) {
        UserResponse userResponse = userService.getUserByUsernameOrEmail(identifier);
        ApiResponse<UserResponse> apiResponse = ApiResponse.ok("User found from identifier : " + identifier,userResponse);
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Update existing user by UUID
     * PUT /api/v1/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        UserResponse updatedUserResponse = userService.updateUser(id,updateUserRequest);
        ApiResponse<UserResponse> apiResponse = ApiResponse.ok("User updated successfully at : " + updatedUserResponse.lastUpdatedAt().toString(),updatedUserResponse);
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Delete user by UUID
     * DELETE /api/v1/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable UUID id) {
        boolean isDeleted = userService.deleteByUserId(id);
        ApiResponse<Void> apiResponse = isDeleted ? ApiResponse.ok("User Deleted Successfully") : ApiResponse.error("User Deletion Failed");
        return isDeleted ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    /**
     * Delete user by username or email
     * DELETE /api/v1/users/search?identifier=...
     */
    @DeleteMapping("/search")
    public ResponseEntity<ApiResponse<Void>> deleteUserByIdentifier(@RequestParam("identifier") String identifier) {
        boolean isDeleted = userService.deleteByUsernameOrEmail(identifier);
        ApiResponse<Void> apiResponse = isDeleted ? ApiResponse.ok("User Deleted Successfully") : ApiResponse.error("User Deletion Failed!");
        return isDeleted ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    @PutMapping("/{id}/password/change")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable UUID id,
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        userService.changePassword(id,changePasswordRequest);
        ApiResponse<Void> apiResponse = ApiResponse.ok("Password Changed Successfully");
        return ResponseEntity.ok(apiResponse);
    }
}

