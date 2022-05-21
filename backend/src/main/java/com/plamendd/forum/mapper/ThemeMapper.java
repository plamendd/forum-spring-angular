package com.plamendd.forum.mapper;


import com.plamendd.forum.dto.ThemeDTO;
import com.plamendd.forum.model.Post;
import com.plamendd.forum.model.Theme;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ThemeMapper {

    @Mapping(target = "postsCount", expression = "java(mapPosts(theme.getPosts()))")
    ThemeDTO mapThemeToDTO(Theme theme);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Theme mapDTOToTheme(ThemeDTO themeDTO);
}
