package com.agri.app.service;

import com.agri.app.Repo.UserRepo;
import com.agri.app.entities.Address;
import com.agri.app.entities.User;
import com.agri.app.utils.IdentityValidator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User payload cannot be null to add user");
        }
        return userRepo.save(user);
    }
    public boolean deleteByUser(User user){
        if (user == null) {
            throw new IllegalArgumentException("User payload cannot be null to delete by user");
        }
        return userRepo.deleteByUser(user);
    }

    public boolean deleteByUserId(UUID id){
        if (id == null) {
            throw new IllegalArgumentException("User payload cannot be null to delete by user UUID");
        }
        return userRepo.deleteByUserId(id);
    }

    public boolean deleteByUsernameOrEmail(String value){
        if (value == null || !IdentityValidator.isValidIdentifier(value)) {
            throw new IllegalArgumentException("User payload cannot be null to delete by username or email");
        }
        return IdentityValidator.isUsername(value) ? userRepo.deleteByUsername(value) : userRepo.deleteByEmail(value);
    }
    public void addAddress(User user,Address address){
        user.addAddress(address);
    }

}
