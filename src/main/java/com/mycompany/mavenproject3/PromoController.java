package com.mycompany.mavenproject3;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/promos")
public class PromoController {

    @GetMapping
    public ResponseEntity<List<Promo>> getAllPromos() {
        List<Promo> promos = new ArrayList<>();
        String query = "SELECT id, nama, diskon, stokPromo, tanggalAkhir FROM promo WHERE tanggalAkhir > CURDATE()";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Promo promo = new Promo(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getDouble("diskon"),
                    rs.getInt("stokPromo"),
                    rs.getDate("tanggalAkhir").toLocalDate().toString()
                );
                promos.add(promo);
            }
            return ResponseEntity.ok(promos);

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/id/{id}") 
    public ResponseEntity<Promo> getPromoById(@PathVariable("id") int id) {
        String query = "SELECT id, nama, diskon, stokPromo, tanggalAkhir FROM promo WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Promo promo = new Promo(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getDouble("diskon"),
                    rs.getInt("stokPromo"),
                    rs.getDate("tanggalAkhir").toLocalDate().toString()
                );
                System.out.println("Found promo: " + promo.getNama());
                return ResponseEntity.ok(promo);
            } else {
                System.out.println("No promo found with ID: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            System.out.println("Database error: ");
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    @PostMapping
    public ResponseEntity<Promo> addPromo(@RequestBody Promo promo) {
        String query = "INSERT INTO promo (nama, diskon, stokPromo, tanggalAkhir) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, promo.getNama());
            stmt.setDouble(2, promo.getDiskon());
            stmt.setInt(3, promo.getStokPromo());
            stmt.setDate(4, Date.valueOf(promo.getTanggalAkhir()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                promo.setId(rs.getInt(1));
            }

            return ResponseEntity.ok(promo);

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}") 
    @ResponseBody
    public ResponseEntity<Promo> updatePromo(
            @PathVariable("id") int id, 
            @RequestBody Promo promo) {

        String query = "UPDATE promo SET nama=?, diskon=?, stokPromo=?, tanggalAkhir=? WHERE id=?"; 

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, promo.getNama());
            stmt.setDouble(2, promo.getDiskon());
            stmt.setInt(3, promo.getStokPromo());
            stmt.setDate(4, Date.valueOf(promo.getTanggalAkhir()));
            stmt.setInt(5, id); 

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.notFound().build();
            }
            promo.setId(id); 
            return ResponseEntity.ok(promo);

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> deletePromo(@PathVariable("id") int id) { 
        String query = "DELETE FROM promo WHERE id=?"; 

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id); 
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}