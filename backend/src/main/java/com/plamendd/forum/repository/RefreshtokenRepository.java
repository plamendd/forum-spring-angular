package com.plamendd.forum.repository;

import com.plamendd.forum.model.Refreshtoken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshtokenRepository extends JpaRepository <Refreshtoken, Long> {
    Optional<Refreshtoken> findByToken(String token);

    void deleteByToken(String token);
}
