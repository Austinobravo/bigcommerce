package com.example.ecommerce.project.service;

import com.example.ecommerce.project.model.Product;
import com.example.ecommerce.project.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts(String title, Boolean availableForSale, String tag, String minPrice, String maxPrice){
        List<Product> allProducts = (List<Product>) productRepository.findAll();
        return allProducts.stream()
                .filter(p -> title == null || p.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(p -> availableForSale == null || p.isAvailableForSale() == availableForSale)
                .filter(p -> tag == null || p.getTags().stream().anyMatch(t -> t.getName().toLowerCase().contains(tag.toLowerCase())))
                .filter(p -> {
                    if(minPrice == null && maxPrice == null) return true;

                    double min = (minPrice != null ) ? Double.parseDouble(minPrice) : 0.0;
                    double max = (maxPrice != null ) ? Double.parseDouble(maxPrice) : Double.MAX_VALUE;
                    double productMin = Double.parseDouble(p.getPriceRange().getMaxVariantPrice().getAmount());
                    double productMax = Double.parseDouble(p.getPriceRange().getMaxVariantPrice().getAmount());

//                    return min >= productMin || max <= productMax;
                    return productMin >= min && productMax <= max;
                }).toList();
    }

}
