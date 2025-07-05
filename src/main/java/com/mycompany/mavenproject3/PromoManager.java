package com.mycompany.mavenproject3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;

public class PromoManager {
    private static final String API_URL = "http://localhost:8080/api/promos";
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .create();

    public static List<Promo> getPromos() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {

                    Promo[] promos = gson.fromJson(reader, Promo[].class);
                    return Arrays.asList(promos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static Promo getPromoById(int id) {
        try {
            URL url = new URL(API_URL + "/id/" + id); 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    return gson.fromJson(reader, Promo.class);
                }
            } else if (conn.getResponseCode() == 404) {
                System.out.println("Promo with ID '" + id + "' not found.");
                return null;
            } else {
                throw new RuntimeException("Failed to get promo by ID: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Promo getPromoByName(String name) {
        try {
            String encodedPromoName = URLEncoder.encode(name, "UTF-8");
            URL url = new URL(API_URL + "/" + encodedPromoName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    return gson.fromJson(reader, Promo.class); 
                }
            } else if (conn.getResponseCode() == 404) {
                System.out.println("Promo with name '" + name + "' not found.");
                return null;
            } else {
                throw new RuntimeException("Failed to get promo by name: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void addPromo(Promo promo) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                String json = gson.toJson(promo);
                os.write(json.getBytes());
            }

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed to add promo");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void editPromo(int index, Promo updatedPromo) {
        try {
            List<Promo> promos = getPromos();
            if (index >= 0 && index < promos.size()) {
                Promo existingPromo = promos.get(index);
                URL url = new URL(API_URL + "/" + existingPromo.getId());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream()) {
                    updatedPromo.setId(existingPromo.getId());
                    String json = gson.toJson(updatedPromo);
                    os.write(json.getBytes("UTF-8"));
                }

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed to update promo");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deletePromo(int index) {
        try {
            List<Promo> promos = getPromos();
            if (index >= 0 && index < promos.size()) {
                Promo promoToDelete = promos.get(index);

                URL url = new URL(API_URL + "/" + promoToDelete.getId());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");

                if (conn.getResponseCode() != 204) {
                    throw new RuntimeException("Failed to delete promo");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean decreasePromoStock(int id) {
        try {
            Promo promo = getPromoById(id);
            if (promo == null || promo.getStokPromo() <= 0) return false;

            Promo updatedPromo = new Promo(
                promo.getId(), 
                promo.getNama(),
                promo.getDiskon(),
                promo.getStokPromo() - 1,
                promo.getTanggalAkhir().toString()
            );

            URL url = new URL(API_URL + "/" + promo.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                String json = gson.toJson(updatedPromo);
                os.write(json.getBytes("UTF-8"));
            }

            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}