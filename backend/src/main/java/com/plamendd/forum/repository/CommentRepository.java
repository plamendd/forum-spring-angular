package com.plamendd.forum.repository;

import com.plamendd.forum.model.Comment;
import com.plamendd.forum.model.Post;
import com.plamendd.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUser(User user);

    List<Comment> findByPost(Post post);
}
