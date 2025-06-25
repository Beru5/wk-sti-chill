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
import java.util.ArrayList;
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
        setSize(600, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Panel form pemesanan
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(new JLabel("Kode Barang"));
        codeField = new JTextField(3);
        formPanel.add(codeField);
        
        formPanel.add(new JLabel("Nama Barang:"));
        nameField = new JTextField(6);
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Kategori:"));
        categoryField = new JComboBox<>(new String[]{"Coffee", "Dairy", "Juice", "Soda", "Tea"});
        formPanel.add(categoryField);
        
        formPanel.add(new JLabel("Harga Jual:"));
        priceField = new JTextField(5);
        formPanel.add(priceField);
        
        formPanel.add(new JLabel("Stok Tersedia:"));
        stockField = new JTextField(2);
        formPanel.add(stockField);
        
        addButton = new JButton("Tambah");
        formPanel.add(addButton);
        editButton = new JButton("Edit");
        formPanel.add(editButton);
        deleteButton = new JButton("Hapus");
        formPanel.add(deleteButton);

        
        add(formPanel, BorderLayout.EAST);
        
        tableModel = new DefaultTableModel(new String[]{"Kode", "Nama", "Kategori", "Harga Jual", "Stok"}, 0);
        drinkTable = new JTable(tableModel);
        loadProductData();
        add(new JScrollPane(drinkTable), BorderLayout.CENTER);
        
        
        drinkTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
               @Override
               public void valueChanged(ListSelectionEvent e) {
                   int selectedRow = drinkTable.getSelectedRow();
                   if (selectedRow != -1) {
                       String selectedCode = tableModel.getValueAt(selectedRow, 0).toString();
                       String selectedName = tableModel.getValueAt(selectedRow, 1).toString();
                       String selectedCategory = tableModel.getValueAt(selectedRow, 2).toString();
                       String selectedPrice = tableModel.getValueAt(selectedRow, 3).toString(); 
                       String selectedStock = tableModel.getValueAt(selectedRow, 4).toString();

                       codeField.setText(selectedCode);
                       nameField.setText(selectedName);
                       categoryField.setSelectedItem(selectedCategory);
                       priceField.setText(selectedPrice);
                       stockField.setText(selectedStock);
                   }
               }
           });


         addButton.addActionListener(e -> {
           try {
               String code = codeField.getText();
               String name = nameField.getText();
               String category = categoryField.getSelectedItem().toString();
               double price = Double.parseDouble(priceField.getText());
               int stock = Integer.parseInt(stockField.getText());

               int id = ProductManager.getProducts().size() + 1;
               Product product = new Product(code, name, category, price, stock); 

               ProductManager.addProduct(product);
               tableModel.addRow(new Object[]{product.getCode(), product.getName(), product.getCategory(), product.getPrice(), product.getStock()});

               codeField.setText("");
               nameField.setText("");
               priceField.setText("");
               stockField.setText("");
           } catch (NumberFormatException ex) {
               JOptionPane.showMessageDialog(this, "Input harga hanya dalam bentuk angka!", "Error", JOptionPane.ERROR_MESSAGE);
           }
           loadProductData();
       });

         editButton.addActionListener(e -> {
           int selectedRow = drinkTable.getSelectedRow();
           if (selectedRow < 0) {
               JOptionPane.showMessageDialog(this, "Pilih produk yang ingin diedit.");
               return;
           }

           try {
               String code = codeField.getText();
               String name = nameField.getText();
               String category = (String) categoryField.getSelectedItem();
               double price = Double.parseDouble(priceField.getText());
               int stock = Integer.parseInt(stockField.getText());

               Product updatedProduct = new Product(code, name, category, price, stock);
               ProductManager.editProduct(selectedRow, updatedProduct);
               
               codeField.setText("");
               nameField.setText("");
               priceField.setText("");
               stockField.setText("");

           } catch (NumberFormatException ex) {
               JOptionPane.showMessageDialog(this, "Input tidak valid:\n" + ex);
           }
           loadProductData();
           });

          deleteButton.addActionListener(e -> {
           int selectedRow = drinkTable.getSelectedRow();
           if (selectedRow < 0) {
               JOptionPane.showMessageDialog(this, "Pilih produk yang ingin dihapus.");
               return;
           }

           try {
               ProductManager.deleteProduct(selectedRow);
               
               codeField.setText("");
               nameField.setText("");
               priceField.setText("");
               stockField.setText("");
           } catch (Exception ex) {
               JOptionPane.showMessageDialog(this, "error\n" + ex);
           }
           loadProductData();
           });
          
          

    }
    

    public void loadProductData() {
        tableModel.setRowCount(0);
        for (Product product : ProductManager.getProducts()) {
            tableModel.addRow(new Object[]{
                product.getCode(), product.getName(), product.getCategory(), product.getPrice(), product.getStock()
            });}
        gui.updateText();
    
}

    
    
}