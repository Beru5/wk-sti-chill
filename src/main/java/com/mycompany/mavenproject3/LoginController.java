package com.mycompany.mavenproject3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    static class LoginRequest {
        public String username;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wk sti", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM admin WHERE username = ? AND password = ?")) {

            stmt.setString(1, loginRequest.username);
            stmt.setString(2, loginRequest.password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Map<String, String> result = new HashMap<>();
                result.put("Welcome, ", rs.getString("username"));
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login gagal");
    }
}
