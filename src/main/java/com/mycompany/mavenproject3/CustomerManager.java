/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;
import java.util.ArrayList;
import java.util.List;

public class CustomerManager {
    private static java.util.List<Customer> customers = new ArrayList<>();

    public static void addCustomer(Customer customer) {
        customers.add(customer);
    }
    
    public static void editCustomer(int index, Customer updatedCustomer) {
        if (index >= 0 && index < customers.size()) {
            customers.set(index, updatedCustomer);
            }
        }
    
   public static void deleteCustomer(int index) {
        if (index >= 0 && index < customers.size()) {
            customers.remove(index);
        }
    }
    

    public static List<Customer> getCustomers() {
        return customers;
    }

    public static Customer getCustomerByNama(String nama) {
        for (Customer c : customers) {
            if (c.getNama().equalsIgnoreCase(nama)) {
                return c;
            }
        }
        return null;
    }
}
