package com.plamendd.forum.repository;

import com.plamendd.forum.model.Post;
import com.plamendd.forum.model.User;
import com.plamendd.forum.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User user);
}
