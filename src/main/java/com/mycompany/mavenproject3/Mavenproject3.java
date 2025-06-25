package com.mycompany.mavenproject3;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Mavenproject3 extends JFrame implements Runnable {
    private String text;
    private int x;
    private int width;
    private BannerPanel bannerPanel;
    private JButton addProductButton;
    private JButton sellProductButton;
    private JButton customerButton;
    private ProductForm ProductForm;
    private FormPenjualan FormPenjualan;
    private CustomerForm CustomerForm;

    public Mavenproject3(String text) {
        this.text = text;
        setTitle("WK. STI Chill");
        setSize(600, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel teks berjalan
        bannerPanel = new BannerPanel();
        add(bannerPanel, BorderLayout.CENTER);

        // Tombol "Kelola Produk"
        JPanel bottomPanel = new JPanel();
        addProductButton = new JButton("Kelola Produk");
        bottomPanel.add(addProductButton);
        sellProductButton = new JButton("Jual Produk");
        bottomPanel.add(sellProductButton);
        customerButton = new JButton("Kelola Pelanggan");
        bottomPanel.add(customerButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        
        ProductForm = new ProductForm(this);
        FormPenjualan = new FormPenjualan(this, ProductForm); 
        CustomerForm = new CustomerForm(this); 
        
        addProductButton.addActionListener(e -> {
            ProductForm.setVisible(true);
        });
        sellProductButton.addActionListener(e -> {
            FormPenjualan.setVisible(true);
        });
        customerButton.addActionListener(e -> {
            CustomerForm.setVisible(true);
        });
        

        setVisible(true);

        Thread thread = new Thread(this);
        thread.start();
    }

    class BannerPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString(text, x, getHeight() / 2);
        }
    }

    @Override
    public void run() {
        width = getWidth();
        while (true) {
            x += 5;
            if (x > width) {
                x = -getFontMetrics(new Font("Arial", Font.BOLD, 18)).stringWidth(text);
            }
            bannerPanel.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    public void updateText() {
    text = "Menu yang tersedia:";
    for (Product p : ProductManager.getProducts()) {
        if (p.getStock() < 1){
            text += "";
        }
        else{
        text += " | " + p.getName();
        }
    }
    bannerPanel.repaint(); 
}
    
    public void gui(){
        String text = "";
            for (Product p : ProductManager.getProducts()) {
                if (p.getStock() < 1){
                    text += "";
                }
            else{
                text += " | " + p.getName();
                }
            }

            Mavenproject3 gui = new Mavenproject3("Menu yang tersedia: " + text);
    }



        
    
}
    

