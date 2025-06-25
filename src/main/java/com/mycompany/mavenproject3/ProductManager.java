/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author ASUS
 */

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    static final List<Product> products = new ArrayList<>();
        
    static {
        products.add(new Product( "P001", "Americano", "Coffee", 18000, 0));
        products.add(new Product( "P002", "Pandan Latte", "Coffee", 15000, 8));
    }

    public static void addProduct(Product product) {
        products.add(product);
    }
    
 
    public static List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public static void editProduct(int index, Product updatedProduct) {
        if (index >= 0 && index < products.size()) {
                products.set(index, updatedProduct);
            }
        }
    
    public static void sellProduct(int index, Product updatedProduct) {
        if (index >= 0 && index < products.size()) {
                products.set(index, updatedProduct);
            }
        }

    public static void deleteProduct(int index) {
        if (index >= 0 && index < products.size()) {
            products.remove(index);
        }
    }
    
    public static String getText() {
        if (products.isEmpty()) {
            return "Belum ada produk.";
        }
        String text = "";
        
        for (Product product : products) {
            text += " | " + product.getName() + " | ";
        }
        return text;
    }
    
    public static Product findByName(String name) {
    for (Product p : products) {
        if (p.getName().equals(name)) return p;
    }
    return null;
}

}
