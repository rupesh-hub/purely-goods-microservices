package com.alfarays.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {
    private Long id;
    private String path;
    private String name;
    private String originalName;
    private String contentType;
    private Long size;
}
