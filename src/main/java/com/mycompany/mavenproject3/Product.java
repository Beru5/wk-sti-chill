/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author ASUS
 */
public class Product {
    private static int nextID = 1;
    private int id;
    private String code;
    private String name;
    private String category;
    private double price;
    private int stock;
    public int quantity;

    public Product(String code, String name, String category, double price, int stock) {
        this.id = nextID++;
        this.code = code;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int updatedStock(int quantity){
        return stock - quantity;
    }
}
