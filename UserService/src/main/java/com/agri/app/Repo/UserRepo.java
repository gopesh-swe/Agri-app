package com.agri.app.Repo;

import com.agri.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User,String> {

    boolean deleteByUserId(UUID id);

    boolean deleteByUser(User user);

    boolean deleteByUsername(String username);

    boolean deleteByEmail(String value);
}
