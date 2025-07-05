package com.mycompany.mavenproject3;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Promo {
    private int id; 
    private String nama;
    private double diskon;
    private int stokPromo;
    private LocalDate tanggalAkhir;

    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Promo() {
    }

    public Promo(String nama, double diskon, int stokPromo, String tanggalAkhir) {
        this.nama = nama;
        this.diskon = diskon;
        this.stokPromo = stokPromo;
        this.tanggalAkhir = LocalDate.parse(tanggalAkhir, formatter);
    }

    public Promo(int id, String nama, double diskon, int stokPromo, String tanggalAkhir) {
        this.id = id;
        this.nama = nama;
        this.diskon = diskon;
        this.stokPromo = stokPromo;
        this.tanggalAkhir = LocalDate.parse(tanggalAkhir, formatter);
    }

    public int getId() { 
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        this.tanggalAkhir = LocalDate.parse(tanggalAkhir, formatter);
    }

    @Override
    public String toString() {
        return nama;
    }

    public String tanggalAkhirToString(){
        String tanggalAkhirToString = tanggalAkhir.format(formatter);
        return tanggalAkhirToString;
    }
}