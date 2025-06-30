/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 *
 * @author ASUS
 */


public class ProductManager {
    private static final String API_URL = "http://localhost:8080/api/products";
    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    public static List<Product> getProducts() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                    
                    // Parse JSON array into Product array
                    Product[] products = gson.fromJson(reader, Product[].class);
                    return Arrays.asList(products);
                }
            } else {
                System.err.println("Failed to fetch products. HTTP error code: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            e.printStackTrace();
        }
        return Collections.emptyList(); // Return empty list instead of null
    }

    public static void addProduct(Product product) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                String json = gson.toJson(product);
                os.write(json.getBytes());
            }
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed to add product");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to add product", e);
        }
    }

    public static void editProduct(int index, Product updatedProduct) {
        try {
            List<Product> products = getProducts();
            if (index >= 0 && index < products.size()) {
                Product existingProduct = products.get(index);
                updatedProduct.setId(existingProduct.getId());
                
                URL url = new URL(API_URL + "/" + existingProduct.getId());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                
                try (OutputStream os = conn.getOutputStream()) {
                    String json = gson.toJson(updatedProduct);
                    os.write(json.getBytes());
                }
                
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed to update product. HTTP Status: " + conn.getResponseCode());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product", e);
        }
    }

    public static void deleteProduct(int index) {
        try {
            List<Product> products = getProducts();
            if (index >= 0 && index < products.size()) {
                Product product = products.get(index);
                
                URL url = new URL(API_URL + "/" + product.getId());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                
                if (conn.getResponseCode() != 204) {
                    throw new RuntimeException("Failed to delete product");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete product", e);
        }
    }
    
public static boolean updateProductStock(int productId, int quantityToReduce) {
    try {
        // 1. Dapatkan produk yang ada
        Product existingProduct = getProductById(productId);
        if (existingProduct == null) return false;
        
        // 2. Hitung stok baru
        int newStock = existingProduct.getStock() - quantityToReduce;
        if (newStock < 0) return false;
        
        // 3. Buat produk yang diupdate
        Product updatedProduct = new Product(
            existingProduct.getId(),
            existingProduct.getCode(),
            existingProduct.getName(),
            existingProduct.getCategory(),
            existingProduct.getPrice(),
            newStock
        );
        
        // 4. Update via API
        URL url = new URL(API_URL + "/" + productId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        try (OutputStream os = conn.getOutputStream()) {
            String json = gson.toJson(updatedProduct);
            os.write(json.getBytes("UTF-8"));
        }
        
        return conn.getResponseCode() == 200;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

public static Product getProductById(int id) {
    try {
        URL url = new URL(API_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        if (conn.getResponseCode() == 200) {
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
                return gson.fromJson(reader, Product.class);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

public static Product findByName(String name) {
    try {
        List<Product> products = getProducts();
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) {  // Case-insensitive comparison
                return p;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;  // Return null jika tidak ditemukan atau error
}

public static int indexOfProduct(Product product) {
    List<Product> products = getProducts();
    for (int i = 0; i < products.size(); i++) {
        if (products.get(i).getId() == product.getId()) {
            return i;
        }
    }
    return -1;
}
    
    public static String getText() {
        List<Product> products = getProducts();
        if (products.isEmpty()) {
            return "Belum ada produk.";
        }
        StringBuilder text = new StringBuilder();
        for (Product product : products) {
            if (product.getStock() >= 1) {
                text.append(" | ").append(product.getName());
            }
        }
        return text.toString();
    }
    public static boolean updateProduct(Product product) {
    try {
        URL url = new URL(API_URL + "/" + product.getId());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");  // Pastikan method PUT
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            String json = gson.toJson(product);
            os.write(json.getBytes("UTF-8"));
        }
        return conn.getResponseCode() == 200;  // Cek status code
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}