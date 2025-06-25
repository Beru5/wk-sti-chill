/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Customer {
    public static int nextID = 1;
    private int id;
    private String nama;
    private String telp;
    private LocalDate ultah;

    public Customer(String nama, String telp, String ultah) {
        this.id = nextID++;
        this.nama = nama;
        this.telp = telp;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.ultah = LocalDate.parse(ultah, formatter);
    }
    
    public Customer(int id, String nama, String telp, String ultah) {
        this.id = id;
        this.nama = nama;
        this.telp = telp;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.ultah = LocalDate.parse(ultah, formatter);
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getTelp() {
        return telp;
    }
    
    public LocalDate getUltah (){
        return ultah;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }
    
    public void setUltah(String ultah) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.ultah = LocalDate.parse(ultah, formatter);
    }
    
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public String ultahToString(){
    String ultahToString = ultah.format(formatter);
    return ultahToString;
    }
}
