package com.mycompany.mavenproject3;

import java.time.format.DateTimeFormatter;
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
    
    public static boolean decreasePromoStock(String promoName) {
    try {
        Promo promo = getPromoByName(promoName);
        if (promo == null || promo.getStokPromo() <= 0) return false;
        
        Promo updatedPromo = new Promo(
            promo.getNama(),
            promo.getDiskon(),
            promo.getStokPromo() - 1,
            promo.getTanggalAkhir().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        editPromo(getPromos().indexOf(promo), updatedPromo);
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
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