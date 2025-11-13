package com.example.ecommerce.project.dto;

import com.example.ecommerce.project.model.Tags;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateProductDto {

    @NotBlank(message = "Product name is required")
    private String title;
    @NotBlank(message = "Product description is required")
    private String description;
    private Boolean availableForSale;
    private List<Tags> tagsList;
    @NotBlank(message = "Price is required")
    private PriceRangeDto priceRange;

    private MultipartFile featuredImage;
    private List<MultipartFile> images;
}
