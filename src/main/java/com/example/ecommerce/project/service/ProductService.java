package com.example.ecommerce.project.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ecommerce.project.dto.CreateProductDto;
import com.example.ecommerce.project.dto.PriceRangeDto;
import com.example.ecommerce.project.dto.UpdateProductDto;
import com.example.ecommerce.project.model.Price;
import com.example.ecommerce.project.model.PriceRange;
import com.example.ecommerce.project.model.Product;
import com.example.ecommerce.project.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private Cloudinary cloudinary;

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

    public String generateUniqueSlug(String productTitle){
        String baseSlug = productTitle.trim().toLowerCase().replaceAll("[^a-z0-9\\s-]","").replaceAll("\\s+", "-");

        String slug = baseSlug;
        int counter = 1;

        while(productRepository.findBySlug(slug).isPresent()){
            slug = baseSlug + "-" + counter;
            counter ++;
        }

        return slug;
    }

    public Product createProduct(CreateProductDto input) throws IOException {
        Product product = new Product();

        if(input.getTitle() != null || input.getTitle().isEmpty()){
            throw new IllegalArgumentException("Product name is required");
        }

        String slug = generateUniqueSlug(input.getTitle());
        product.setTitle(input.getTitle());
        product.setSlug(slug);
        product.setDescription(input.getDescription());
        product.setAvailableForSale(
                Optional.ofNullable(input.getAvailableForSale()).orElse(true)
        );
        product.setUpdatedAt(Instant.now());

        if(input.getPriceRange() != null){
            product.setPriceRange(mapPriceRange(input.getPriceRange()));
        }

        if(input.getFeaturedImage() != null && !input.getFeaturedImage().isEmpty()){
            String featuredImage = uploadToCloudinary(input.getFeaturedImage(), slug);
            product.setFeaturedImage(featuredImage);
        }

        if(input.getImages() != null && !input.getImages().isEmpty()){
            List<String> imageUrls = new ArrayList<>();
            for(MultipartFile file : input.getImages()){
                if(file != null && !file.isEmpty()){
                    imageUrls.add(uploadToCloudinary(file, slug));
                }
            }
            product.setImages(imageUrls);
        }


        return productRepository.save(product);
    }

    public Product updateProduct(String id, UpdateProductDto input) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (input.getTitle() != null && !input.getTitle().isEmpty()) {
            product.setTitle(input.getTitle());
            product.setSlug(generateUniqueSlug(input.getTitle()));
        }

        if (input.getDescription() != null) {
            product.setDescription(input.getDescription());
        }

        if (input.getAvailableForSale() != null) {
            product.setAvailableForSale(input.getAvailableForSale());
        }

        if (input.getPriceRange() != null) {
            product.setPriceRange(mapPriceRange(input.getPriceRange()));
        }

        if (input.getFeaturedImage() != null && !input.getFeaturedImage().isEmpty()) {
            String imageUrl = uploadToCloudinary(input.getFeaturedImage(), product.getSlug());
            product.setFeaturedImage(imageUrl);
        }

        if (input.getImages() != null && !input.getImages().isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : input.getImages()) {
                imageUrls.add(uploadToCloudinary(image, product.getSlug()));
            }
            product.setImages(imageUrls);
        }

        product.setUpdatedAt(Instant.now());
        return productRepository.save(product);
    }


    private PriceRange mapPriceRange(PriceRangeDto input){
        PriceRange priceRange = new PriceRange();

        Price minPrice = new Price();
        minPrice.setAmount(input.getMinVariantPrice().getAmount());
        minPrice.setCurrencyCode(input.getMinVariantPrice().getCurrencyCode());

        Price maxPrice = new Price();
        maxPrice.setAmount(input.getMaxVariantPrice().getAmount());
        maxPrice.setCurrencyCode(input.getMaxVariantPrice().getCurrencyCode());

        priceRange.setMinVariantPrice(maxPrice);
        priceRange.setMaxVariantPrice(minPrice);

        return priceRange;

    }

    public String uploadToCloudinary(MultipartFile image, String productSlug) throws IOException {
            if (image == null || image.isEmpty()) return null;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(image.getInputStream())
                    .size(600, 600)
                    .outputQuality(0.7)
                    .toOutputStream(outputStream);
            byte[] compressedImage = outputStream.toByteArray();

            Map uploadResult = cloudinary.uploader().upload(compressedImage, ObjectUtils.asMap(
                    "folder", "products/" + productSlug,
                    "resource_type", "image"
            ));

        return uploadResult.get("secure_url").toString();

    }

}
