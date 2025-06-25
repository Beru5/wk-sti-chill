package com.mycompany.mavenproject3;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Promo {
    private String nama;
    private double diskon; 
    private int stokPromo;
    private LocalDate tanggalAkhir;

    public Promo(String nama, double diskon, int stokPromo, String tanggalAkhir) {
        this.nama = nama;
        this.diskon = diskon;
        this.stokPromo = stokPromo;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.tanggalAkhir = LocalDate.parse(tanggalAkhir, formatter);
    }

    public String getNama() {
        return nama;
    }

    public double getDiskon() {
        return diskon;
    }
    
    public int getStokPromo() {
        return stokPromo;
    }
        
    public LocalDate getTanggalAkhir() {
        return tanggalAkhir;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setDiskon(double diskon) {
        this.diskon = diskon;
    }
    
    public void setStokPromo(int stokPromo) {
        this.stokPromo = stokPromo;
    }
    
    public void setTanggalAkhir(String tanggalAkhir) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.tanggalAkhir = LocalDate.parse(tanggalAkhir, formatter);
    }

    @Override
    public String toString() {
        return nama;
    }
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public String tanggalAkhirToString(){
        String tanggalAkhirToString = tanggalAkhir.format(formatter);
        return tanggalAkhirToString;
    }
}