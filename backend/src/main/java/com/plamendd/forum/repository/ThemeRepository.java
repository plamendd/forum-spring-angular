package com.plamendd.forum.repository;

import com.plamendd.forum.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme,Long> {
   Optional<Theme> findByName(String themeName);


}
