// ProductUpdateDto.java
package com.example.ecommerce.project.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateProductDto {
    private String title;
    private String description;
    private Boolean availableForSale;
    private List<String> tags;
    private PriceRangeDto priceRange;

    private MultipartFile featuredImage;
    private List<MultipartFile> images;
}
