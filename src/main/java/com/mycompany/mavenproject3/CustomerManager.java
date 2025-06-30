/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

import java.io.*;
import java.net.*;
import com.google.gson.*;
import java.time.LocalDate;
import java.util.*;

public class CustomerManager {
    private static final String API_URL = "http://localhost:8080/api/customers";
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .create();

    public static List<Customer> getCustomers() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            if (conn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    
                    Customer[] customers = gson.fromJson(reader, Customer[].class);
                    return Arrays.asList(customers);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void addCustomer(Customer customer) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                String json = gson.toJson(customer);
                os.write(json.getBytes());
            }
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed to add customer");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void editCustomer(int index, Customer updatedCustomer) {
        try {
            URL url = new URL(API_URL + "/" + updatedCustomer.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                String json = gson.toJson(updatedCustomer);
                os.write(json.getBytes());
            }
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed to update customer");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteCustomer(int index) {
        List<Customer> customers = getCustomers();
        if (index >= 0 && index < customers.size()) {
            Customer customer = customers.get(index);
            try {
                URL url = new URL(API_URL + "/" + customer.getId());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                
                if (conn.getResponseCode() != 204) {
                    throw new RuntimeException("Failed to delete customer");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}