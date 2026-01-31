package com.shehta.ecommerce.controller;


import com.shehta.ecommerce.model.Product;
import com.shehta.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    private final ProductService service;
    public ProductController(ProductService service)
    {
        this.service=service;
    }
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts()
    {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable int productId)
    {
        Product product=service.getProductById(productId);
        if(product!=null)
        {
            return new ResponseEntity<>(product,HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile)
    {
        try{
            Product product1=service.addProduct(product,imageFile);
            return new ResponseEntity<>(product1,HttpStatus.CREATED);
        }
        catch (Exception e)
        {
           return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
         
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]>getImageByProductId(@PathVariable  int productId)
    {
        Product product= service.getProductById(productId);

        byte[] imageFile=product.getImageData();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body((imageFile));
    }


    @PutMapping("/product/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable int productId,@RequestPart Product product ,@RequestPart MultipartFile imageFile )  {

        Product product1= null;
        try {
            product1 = service.updateProduct(productId,product,imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Faild to update",HttpStatus.BAD_REQUEST);
        }
        if(product1 !=null)
        {
            return new ResponseEntity<>("update",HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Faild to update",HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable  int productId)
    {
        Product product1=service.getProductById(productId);
        if(product1 !=null)
        {
            service.deleteProduct(productId);
            return new ResponseEntity<>("Product Deleted",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Faild to delete this objecgt",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword)
    {
        List<Product> products=service.searchProduct(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);

    }

}
