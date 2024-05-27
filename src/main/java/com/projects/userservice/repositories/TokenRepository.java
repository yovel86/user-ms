package com.projects.userservice.repositories;

import com.projects.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByValue(String value);

    @Query("SELECT COUNT(tkn) FROM Token AS tkn WHERE tkn.user.id = :userId AND tkn.isActive = true")
    int findNumberOfActiveSessions(long userId);

}
