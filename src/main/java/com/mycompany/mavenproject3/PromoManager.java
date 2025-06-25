package com.mycompany.mavenproject3;

import java.util.ArrayList;
import java.util.List;

public class PromoManager {
    private static List<Promo> promos = new ArrayList<>();

    static {
        promos.add(new Promo("Hadiah Pengguna Baru", 5.0, 2, "2099-01-01")); 
        promos.add(new Promo("Promo Ulang Tahun", 10.0, 1, "2069-12-21")); 
        promos.add(new Promo("Promo Hari Raya", 15.0, 1, "2045-12-12")); 
        promos.add(new Promo("Promo Istri 6", 25.0, 1, "2002-02-30")); 
    }

    public static void addPromo(Promo promo) {
        promos.add(promo);
    }

    public static List<Promo> getPromos() {
        return new ArrayList<>(promos);
    }

    public static Promo getPromoByName(String name) {
        for (Promo promo : promos) {
            if (promo.getNama().equalsIgnoreCase(name)) {
                return promo;
            }
        }
        return null;
    }

    public static void editPromo(int index, Promo updatedPromo) {
        if (index >= 0 && index < promos.size()) {
            promos.set(index, updatedPromo);
        }
    }

    public static void deletePromo(int index) {
        if (index >= 0 && index < promos.size()) {
            promos.remove(index);
        }
    }
}