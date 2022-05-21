package com.plamendd.forum.controller;

import com.plamendd.forum.dto.ThemeDTO;
import com.plamendd.forum.service.ThemeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theme")
@AllArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @PostMapping
    ResponseEntity<ThemeDTO> createTheme(@RequestBody ThemeDTO themeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(themeService.save(themeDTO));
    }

    @GetMapping
    public ResponseEntity<List<ThemeDTO>> getAllThemes() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(themeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeDTO> getTheme(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(themeService.getTheme(id));
    }


}
