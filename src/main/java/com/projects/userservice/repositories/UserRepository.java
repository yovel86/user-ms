package com.projects.userservice.repositories;

import com.projects.userservice.dtos.UserProjection;
import com.projects.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    Optional<UserProjection> findProjectedById(long id);

}
