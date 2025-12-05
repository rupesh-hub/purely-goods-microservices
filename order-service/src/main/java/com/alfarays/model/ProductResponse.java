package com.alfarays.model;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private Map<String, String> specification;
    private CategoryResponse category;
    private List<ImageResponse> images;
}