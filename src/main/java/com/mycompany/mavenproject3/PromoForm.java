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
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PromoForm extends JFrame {
    private JTextField namaField;
    private JTextField diskonField;
    private JTextField stokField;
    private JTextField tanggalAkhirField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private int selectedPromoId = -1;

    public PromoForm() {
        setTitle("WK. Cuan | Kelola Promo");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 5, 5));

        formPanel.add(new JLabel("Nama Promo:"));
        namaField = new JTextField();
        formPanel.add(namaField);

        formPanel.add(new JLabel("Diskon (%):"));
        diskonField = new JTextField();
        formPanel.add(diskonField);

        formPanel.add(new JLabel("Stok Promo:"));
        stokField = new JTextField();
        formPanel.add(stokField);

        formPanel.add(new JLabel("Tanggal Akhir (YYYY-MM-DD):"));
        tanggalAkhirField = new JTextField();
        formPanel.add(tanggalAkhirField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Tambah");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Hapus");
        refreshButton = new JButton("Refresh");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama", "Diskon", "Stok", "Tanggal Akhir"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadPromoData();

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                selectedPromoId = (int) tableModel.getValueAt(selectedRow, 0);
                namaField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                diskonField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                stokField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                tanggalAkhirField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });

        addButton.addActionListener(e -> addPromo());
        editButton.addActionListener(e -> editPromo());
        deleteButton.addActionListener(e -> deletePromo());
        refreshButton.addActionListener(e -> loadPromoData());
    }

    private void addPromo() {
        try {
            String nama = namaField.getText().trim();
            double diskon = Double.parseDouble(diskonField.getText().trim());
            int stok = Integer.parseInt(stokField.getText().trim());
            String tanggalAkhir = tanggalAkhirField.getText().trim();
            LocalDate.parse(tanggalAkhir);

            Promo newPromo = new Promo(nama, diskon, stok, tanggalAkhir);
            PromoManager.addPromo(newPromo);
            loadPromoData();
            clearFields();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal tidak valid. Gunakan YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Diskon dan stok harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editPromo() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih promo yang ingin diedit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String nama = namaField.getText().trim();
            double diskon = Double.parseDouble(diskonField.getText().trim());
            int stok = Integer.parseInt(stokField.getText().trim());
            String tanggalAkhir = tanggalAkhirField.getText().trim();

            LocalDate.parse(tanggalAkhir);

            Promo updatedPromo = new Promo(selectedPromoId, nama, diskon, stok, tanggalAkhir);
            PromoManager.editPromo(selectedRow, updatedPromo); 
            loadPromoData();
            clearFields();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal tidak valid. Gunakan YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Diskon dan stok harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePromo() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih promo yang ingin dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus promo ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            PromoManager.deletePromo(selectedRow); 
            loadPromoData();
            clearFields();
        }
    }

    public void loadPromoData() {
        tableModel.setRowCount(0);
        for (Promo p : PromoManager.getPromos()) {
            tableModel.addRow(new Object[]{
                p.getId(), 
                p.getNama(),
                p.getDiskon(),
                p.getStokPromo(),
                p.getTanggalAkhir().toString()
            });
        }
    }

    private void clearFields() {
        namaField.setText("");
        diskonField.setText("");
        stokField.setText("");
        tanggalAkhirField.setText("");
        selectedPromoId = -1; 
    }
}