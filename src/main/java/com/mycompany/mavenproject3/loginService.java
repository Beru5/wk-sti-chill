/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;import java.io.*;
import java.io.*;
import java.net.*;
import com.google.gson.*;

public class loginService {
    public static Admin login(String username, String password) {
        try {
            URL url = new URL("http://localhost:8080/api/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String urlParams = "username=" + URLEncoder.encode(username, "UTF-8") +
                               "&password=" + URLEncoder.encode(password, "UTF-8");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(urlParams.getBytes());
            }

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                Gson gson = new Gson();
                return gson.fromJson(response.toString(), Admin.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
