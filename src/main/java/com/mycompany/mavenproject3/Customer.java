package com.mycompany.mavenproject3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Customer {
    private int id;
    private String nama;
    private String telp;
    private LocalDate ultah;

    // Default constructor for JSON deserialization
    public Customer() {}

    public Customer(String nama, String telp, String ultah) {
        this.nama = nama;
        this.telp = telp;
        setUltah(ultah);
    }
    
    public Customer(int id, String nama, String telp, String ultah) {
        this.id = id;
        this.nama = nama;
        this.telp = telp;
        setUltah(ultah);
    }

    // Getters and setters
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public String getNama() { 
        return nama; 
    }
    
    public void setNama(String nama) { 
        this.nama = nama; 
    }
    
    public String getTelp() { 
        return telp; 
    }
    
    public void setTelp(String telp) { 
        this.telp = telp; 
    }
    
    public LocalDate getUltah() { 
        return ultah; 
    }
    
    public void setUltah(String ultah) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.ultah = LocalDate.parse(ultah, formatter);
    }
    
    public String getUltahAsString() {
        return ultah != null ? ultah.toString() : null;
    }
}