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
@RequestMapping("/api/customers")
public class CustomerController {
    
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customer";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("telp"),
                    rs.getString("ultah")
                );
                customers.add(customer);
            }
            return ResponseEntity.ok(customers);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        String query = "INSERT INTO customer (nama, telp, ultah) VALUES (?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, customer.getNama());
            stmt.setString(2, customer.getTelp());
            stmt.setString(3, customer.getUltah().toString());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    customer.setId(rs.getInt(1));
                }
            }
            return ResponseEntity.ok(customer);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") int id, @RequestBody Customer customer) {
        String query = "UPDATE customer SET nama=?, telp=?, ultah=? WHERE id=?";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, customer.getNama());
            stmt.setString(2, customer.getTelp());
            stmt.setString(3, customer.getUltah().toString());
            stmt.setInt(4, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(customer);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") int id) {
        String query = "DELETE FROM customer WHERE id=?";
        
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