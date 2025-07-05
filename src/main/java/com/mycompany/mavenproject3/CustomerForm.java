package com.mycompany.mavenproject3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import com.toedter.calendar.JDateChooser; 
import java.time.format.DateTimeFormatter;

public class CustomerForm extends JFrame {
    private JTextField namaField;
    private JTextField telpField;
    private JDateChooser ultahDateChooser;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    private Mavenproject3 gui;

    public CustomerForm(Mavenproject3 gui) {
        this.gui = gui;

        setTitle("WK. Cuan | Daftar Pelanggan");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 5, 5));

        formPanel.add(new JLabel("Nama Pelanggan:"));
        namaField = new JTextField();
        formPanel.add(namaField);

        formPanel.add(new JLabel("No. Telepon:"));
        telpField = new JTextField();
        formPanel.add(telpField);

        formPanel.add(new JLabel("Tanggal Ulang Tahun:"));
        ultahDateChooser = new JDateChooser();
        ultahDateChooser.setDateFormatString("yyyy-MM-dd"); 
        formPanel.add(ultahDateChooser); 

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Tambah");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Hapus");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama", "Telepon", "Tanggal Lahir"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadCustomerData();

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    int selectedRow = table.getSelectedRow();
                    namaField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    telpField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    String tanggalStr = tableModel.getValueAt(selectedRow, 3).toString();
                try {
                    LocalDate dateFromTable = LocalDate.parse(tanggalStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    ultahDateChooser.setDate(java.sql.Date.valueOf(dateFromTable));
                } 
                catch (Exception ex) {
                    System.err.println("Error parsing date from table: " + tanggalStr);
                    ultahDateChooser.setDate(null);
                }
            }
        }});

        addButton.addActionListener(e -> {
            String nama = namaField.getText().trim();
            String telp = telpField.getText().trim();
            Date selectedDate = ultahDateChooser.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Tanggal Akhir tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalDate ultahLocalDate = new java.sql.Date(selectedDate.getTime()).toLocalDate();
            String ultah = ultahLocalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


            if (nama.isEmpty() || telp.isEmpty() || ultah.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom harus diisi.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                LocalDate.parse(ultah);
                Customer newCustomer = new Customer(nama, telp, ultah);
                CustomerManager.addCustomer(newCustomer);
                loadCustomerData();
                namaField.setText("");
                telpField.setText("");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Format tanggal ulang tahun tidak valid. Gunakan YYYY-MM-DD.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Pilih customer yang ingin diedit.");
                return;
            }

            String nama = namaField.getText().trim();
            String telp = telpField.getText().trim();
            Date selectedDate = ultahDateChooser.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Tanggal Akhir tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalDate tanggalAkhirLocalDate = new java.sql.Date(selectedDate.getTime()).toLocalDate();
            String ultah = tanggalAkhirLocalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


            if (nama.isEmpty() || telp.isEmpty() || ultah.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom harus diisi.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                LocalDate.parse(ultah);
                int customerId = (int) tableModel.getValueAt(selectedRow, 0);
                Customer updatedCustomer = new Customer(customerId, nama, telp, ultah);
                CustomerManager.editCustomer(selectedRow, updatedCustomer);
                
                loadCustomerData();
                namaField.setText("");
                telpField.setText("");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Format tanggal ulang tahun tidak valid. Gunakan YYYY-MM-DD.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
           int selectedRow = table.getSelectedRow();
           if (selectedRow < 0) {
               JOptionPane.showMessageDialog(this, "Pilih customer yang ingin dihapus.");
           } else {
                CustomerManager.deleteCustomer(selectedRow); 
                loadCustomerData();
                namaField.setText("");
                telpField.setText("");
            }
        });
    }

    private void loadCustomerData() {
        tableModel.setRowCount(0);
        for (Customer c : CustomerManager.getCustomers()) {
            tableModel.addRow(new Object[]{c.getId(), c.getNama(), c.getTelp(), c.getUltah()}); 
        }
    }
}