package com.shehta.ecommerce.service;

import com.shehta.ecommerce.model.Product;
import com.shehta.ecommerce.repo.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepo repo;

    public ProductService (ProductRepo repo)
    {
        this.repo=repo;
    }

    public List<Product> getAllProducts() {
       return repo.findAll();
    }

    public Product getProductById(int productId) {
        return repo.findById(productId).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return repo.save(product);
    }

    public  Product updateProduct(int productId,Product product,MultipartFile imageFile) throws IOException {
        product.setImageData(imageFile.getBytes());
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        return repo.save(product);
    }

    public void deleteProduct(int productId) {
         repo.deleteById(productId);
    }

    public List<Product> searchProduct(String keyword) {
        return repo.searchProductByName(keyword);
    }
}
