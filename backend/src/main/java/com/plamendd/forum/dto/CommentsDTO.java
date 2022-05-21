package com.plamendd.forum.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDTO {
    private Long id;
    private Long postId;
    private String text;
    private String userName;
    private Instant createdDate;



}
