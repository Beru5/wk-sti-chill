/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author ASUS
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

public class ProductForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField codeField;
    private JTextField nameField;
    private JComboBox<String> categoryField;
    private JTextField priceField;
    private JTextField stockField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    
    private Mavenproject3 gui;
    
    public ProductForm(Mavenproject3 gui) {  
        this.gui = gui;
        
        setTitle("WK. Cuan | Stok Barang");
        setSize(700, 450); // Increased width to accommodate ID column
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Panel form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("Kode Barang:"));
        codeField = new JTextField();
        formPanel.add(codeField);
        
        formPanel.add(new JLabel("Nama Barang:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Kategori:"));
        categoryField = new JComboBox<>(new String[]{"Coffee", "Dairy", "Juice", "Soda", "Tea"});
        formPanel.add(categoryField);
        
        formPanel.add(new JLabel("Harga Jual:"));
        priceField = new JTextField();
        formPanel.add(priceField);
        
        formPanel.add(new JLabel("Stok Tersedia:"));
        stockField = new JTextField();
        formPanel.add(stockField);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Tambah");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Hapus");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        formPanel.add(buttonPanel);
        add(formPanel, BorderLayout.EAST);
        
        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Kode", "Nama", "Kategori", "Harga Jual", "Stok"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        drinkTable = new JTable(tableModel);
        drinkTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = drinkTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        codeField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                        nameField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                        categoryField.setSelectedItem(tableModel.getValueAt(selectedRow, 3));
                        priceField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                        stockField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(drinkTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load initial data
        loadProductData();
        
        // Button actions
        setupButtonActions();
    }
    
    private void setupButtonActions() {
        addButton.addActionListener(e -> {
            try {
                Product product = new Product(
                    codeField.getText(),
                    nameField.getText(),
                    categoryField.getSelectedItem().toString(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(stockField.getText())
                );
                
                ProductManager.addProduct(product);
                loadProductData();
                clearFields();
            } catch (NumberFormatException ex) {
                showError("Input harga/stok harus berupa angka!");
            } catch (Exception ex) {
                showError("Gagal menambah produk: " + ex.getMessage());
            }
        });
        
        editButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow < 0) {
                showError("Pilih produk yang ingin diedit.");
                return;
            }

            try {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Product updatedProduct = new Product(
                    id,
                    codeField.getText(),
                    nameField.getText(),
                    categoryField.getSelectedItem().toString(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(stockField.getText())
                );
                
                ProductManager.editProduct(selectedRow, updatedProduct);
                loadProductData();
                clearFields();
            } catch (NumberFormatException ex) {
                showError("Input harga/stok harus berupa angka!");
            } catch (Exception ex) {
                showError("Gagal mengupdate produk: " + ex.getMessage());
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow < 0) {
                showError("Pilih produk yang ingin dihapus.");
                return;
            }

            try {
                ProductManager.deleteProduct(selectedRow);
                loadProductData();
                clearFields();
            } catch (Exception ex) {
                showError("Gagal menghapus produk: " + ex.getMessage());
            }
        });
    }
    
    public void loadProductData() {
        tableModel.setRowCount(0);
        List<Product> products = ProductManager.getProducts();
        for (Product product : products) {
            tableModel.addRow(new Object[]{
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getCategory(),
                String.format("%,.0f", product.getPrice()),
                product.getStock()
            });
        }
        gui.updateText();
    }
    
    private void clearFields() {
        codeField.setText("");
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
        drinkTable.clearSelection();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}