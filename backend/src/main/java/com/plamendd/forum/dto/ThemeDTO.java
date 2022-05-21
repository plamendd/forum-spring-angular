package com.plamendd.forum.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThemeDTO {
    private Long id;
    private String name;
    private Integer postsCount;
    private String description;
}
