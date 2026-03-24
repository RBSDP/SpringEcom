package com.example.SpringEcom.service;

import java.io.IOException;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.SpringEcom.model.Product;
import com.example.SpringEcom.repo.ProductRepo;

import javax.xml.transform.sax.SAXResult;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepo productRepo;
    private ChatClient chatClient;

    public ProductService(ChatClient chatClient){
        this.chatClient = chatClient;
    };

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(int id) {
        return productRepo.findById(id).orElse(null);
    }

    public Product addOrUpdateProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return productRepo.save(product);
    }

    public void deleteProduct(int id) {
       productRepo.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepo.searchProducts(keyword);
    }

    public String generateDescription(String name, String category) {
        String descPrompt = String.format("""
                
                Write a concise and professional product description for an e-commerce listing.
                
                                Product Name: %s
                                Category: %s
                
                                Keep it simple, engaging, and highlight its primary features or benefits.
                                Avoid technical jargon and keep it customer-friendly.
                                Limit the description to 250 characters maximum.
                
                
                """, name, category);
        String desc = chatClient.prompt(descPrompt).call().chatResponse().getResult().getOutput().getText();

        return desc;


    }
}