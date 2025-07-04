/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

import javax.swing.*;
import java.sql.*;

public class Login extends JFrame {
    private JTextField UsernameField;
    private JPasswordField PasswordField;
    private JButton LoginButton;

    public Login() {
        setTitle("Login");
        setSize(300, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel labelUser = new JLabel("Username:");
        labelUser.setBounds(30, 20, 80, 25);
        add(labelUser);

        UsernameField = new JTextField();
        UsernameField.setBounds(110, 20, 140, 25);
        add(UsernameField);

        JLabel labelPassword = new JLabel("Password:");
        labelPassword.setBounds(30, 55, 80, 25);
        add(labelPassword);

        PasswordField = new JPasswordField();
        PasswordField.setBounds(110, 55, 140, 25);
        add(PasswordField);

        LoginButton = new JButton("Login");
        LoginButton.setBounds(100, 95, 80, 30);
        add(LoginButton);

        LoginButton.addActionListener(e -> checkLogin());
    }
    private void checkLogin() {
        String username = UsernameField.getText();
        String password = new String(PasswordField.getPassword());

        Admin admin = LoginManager.login(username, password);

        if (admin != null) {
            Session.username = admin.getUsername();
            JOptionPane.showMessageDialog(this, "Login berhasil, Selamat Datang Tuan " + Session.getUsername());
            this.dispose();
            new Mavenproject3(Session.username).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Username atau password salah.");
        }
    }
}