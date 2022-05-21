package com.plamendd.forum.service;

import com.plamendd.forum.dto.ThemeDTO;
import com.plamendd.forum.exeptions.ForumException;
import com.plamendd.forum.mapper.ThemeMapper;
import com.plamendd.forum.model.Theme;
import com.plamendd.forum.repository.ThemeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final ThemeMapper themeMapper;


    @Transactional
    public ThemeDTO save(ThemeDTO themeDTO) {
        Theme theme = themeRepository.save(
                themeMapper.mapDTOToTheme(themeDTO));
        themeDTO.setId(theme.getId());
        return themeDTO;
    }

    public ThemeDTO getTheme(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new ForumException("There is no theme with id" + id));
        return themeMapper.mapThemeToDTO(theme);
    }

    public List<ThemeDTO> getAll() {
        return themeRepository.findAll()
                .stream()
                .map(themeMapper::mapThemeToDTO)
                .collect(toList());
    }
}
