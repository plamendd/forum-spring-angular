package com.plamendd.forum.repository;


import com.plamendd.forum.model.Post;
import com.plamendd.forum.model.Theme;
import com.plamendd.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByTheme(Theme theme);
    List<Post> findByUser(User user);
}
