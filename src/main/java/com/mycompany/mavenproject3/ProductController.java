/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("id"),
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                );
                products.add(product);
            }
            return ResponseEntity.ok(products);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/{id}")
public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
    System.out.println("GET product by ID: " + id);
    String query = "SELECT * FROM product WHERE id = ?";
    
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
         PreparedStatement stmt = conn.prepareStatement(query)) {
         
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            Product product = new Product(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getDouble("price"),
                rs.getInt("stock")
            );
            System.out.println("Found product: " + product.getName());
            return ResponseEntity.ok(product);
        } else {
            System.out.println("No product found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    } catch (SQLException e) {
        System.out.println("Database error: ");
        e.printStackTrace();
        return ResponseEntity.status(500).build();
    }
}
    
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        String query = "INSERT INTO product (code, name, category, price, stock) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, product.getCode());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getStock());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getInt(1));
                }
            }
            return ResponseEntity.ok(product);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
@PutMapping("/{id}")
@ResponseBody
public ResponseEntity<Product> updateProduct(
        @PathVariable("id") int id, 
        @RequestBody Product product) {
    
    String query = "UPDATE product SET code=?, name=?, category=?, price=?, stock=? WHERE id=?";
    
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, product.getCode());
        stmt.setString(2, product.getName());
        stmt.setString(3, product.getCategory());
        stmt.setDouble(4, product.getPrice());
        stmt.setInt(5, product.getStock());
        stmt.setInt(6, id);
        
        int affectedRows = stmt.executeUpdate();
        if (affectedRows == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
        
    } catch (SQLException e) {
        e.printStackTrace();
        return ResponseEntity.status(500).build();
    }
}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") int id) {
        String query = "DELETE FROM product WHERE id=?";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}