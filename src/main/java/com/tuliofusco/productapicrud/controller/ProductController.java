package com.tuliofusco.productapicrud.controller;

import com.tuliofusco.productapicrud.model.Product;
import com.tuliofusco.productapicrud.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size){
        try{
            Pageable paging = PageRequest.of(page, size);
            Page<Product> pageProducts;

            if (name == null || name.isEmpty()){
                pageProducts = productRepository.findAll(paging);
            }else {
                pageProducts = productRepository.findByNameContainingIgnoreCase(name, paging);
            }

            return new ResponseEntity<>(pageProducts, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        Optional<Product> productData = productRepository.findById(id);

        if (productData.isPresent()){
            return new ResponseEntity<>(productData.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product productObj = productRepository.save(product);

        return new ResponseEntity<>(productObj, HttpStatus.OK);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProductById(@PathVariable Long id, @RequestBody Product newProductData){
        Optional<Product> oldProductData = productRepository.findById(id);

        if (oldProductData.isPresent()){
            Product updatedProductData = oldProductData.get();
            updatedProductData.setName(newProductData.getName());
            updatedProductData.setDescription(newProductData.getDescription());
            updatedProductData.setPrice(newProductData.getPrice());
            updatedProductData.setQuantityStock(newProductData.getQuantityStock());
            updatedProductData.setCategory(newProductData.getCategory());
            updatedProductData.setActive(newProductData.getActive());

            Product productObj = productRepository.save(updatedProductData);
            return new ResponseEntity<>(productObj, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProductById(@PathVariable Long id){
        Optional<Product> productData = productRepository.findById(id);

        if (productData.isPresent()){
            Product product = productData.get();
            product.setActive(false);
            productRepository.save(product);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
