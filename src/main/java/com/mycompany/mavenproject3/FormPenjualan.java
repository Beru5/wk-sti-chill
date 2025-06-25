package com.mycompany.mavenproject3;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.time.LocalDate; // Import for LocalDate
import java.time.format.DateTimeFormatter; // Import for DateTimeFormatter
import java.time.format.DateTimeParseException; // Import for DateTimeParseException

public class FormPenjualan extends JFrame {
    private JComboBox<String> namaBox;
    private JTextField quantityField;
    private JTextField priceField;
    private JTextField stockField;
    private JButton processButton;
    private JButton refreshButton;
    private JButton addToCartButton;

    private JTable cartTable;
    private DefaultTableModel cartTableModel;

    private JTextField subtotalDisplayField;
    private JTextField diskonDisplayField;
    private JLabel totalLabel;

    private JComboBox<Promo> promoComboBox;

    private JComboBox<String> gula;
    private JComboBox<String> susu;
    private JComboBox<String> es;
    private JComboBox<String> sirup;

    private JTextField customerTelpField;

    private Mavenproject3 gui;
    private ProductForm ProductForm;

    public FormPenjualan(Mavenproject3 gui, ProductForm ProductForm) {
        this.gui = gui;
        this.ProductForm = ProductForm;
        Locale.setDefault(new Locale("id", "ID"));

        setTitle("WK. Cuan | Jual Barang");
        setSize(1000, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rightPanel.add(new JLabel("Pilih Produk:"));
        namaBox = new JComboBox<>();
        rightPanel.add(namaBox);

        rightPanel.add(new JLabel("Stok Tersedia:"));
        stockField = new JTextField(5);
        stockField.setEditable(false);
        rightPanel.add(stockField);

        rightPanel.add(new JLabel("Harga Jual:"));
        priceField = new JTextField(5);
        priceField.setEditable(false);
        rightPanel.add(priceField);

        rightPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        rightPanel.add(quantityField);

        rightPanel.add(Box.createVerticalStrut(10));

        JPanel customizationPanel = new JPanel();
        customizationPanel.setLayout(new BoxLayout(customizationPanel, BoxLayout.Y_AXIS));
        customizationPanel.setBorder(BorderFactory.createTitledBorder("Customisasi Menu"));

        JPanel customizationRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        customizationRow1.add(new JLabel("Gula:"));
        gula = new JComboBox<>(new String[]{"Normal", "Kurang Gula", "Ekstra Gula"});
        customizationRow1.add(gula);
        customizationRow1.add(Box.createHorizontalStrut(20));

        customizationRow1.add(new JLabel("Susu:"));
        susu = new JComboBox<>(new String[]{"Tanpa", "Susu Oat", "Susu Kental Manis"});
        customizationRow1.add(susu);
        customizationPanel.add(customizationRow1);

        JPanel customizationRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        customizationRow2.add(new JLabel("Es Batu:"));
        es = new JComboBox<>(new String[]{"Normal", "Tanpa Es", "Extra Es"});
        customizationRow2.add(es);
        customizationRow2.add(Box.createHorizontalStrut(20));

        customizationRow2.add(new JLabel("Sirup:"));
        sirup = new JComboBox<>(new String[]{"Tanpa Syrup", "Sirup Vanilla", "Sirup Caramel", "Sirup Hazelnut"});
        customizationRow2.add(sirup);
        customizationPanel.add(customizationRow2);

        rightPanel.add(customizationPanel);

        rightPanel.add(Box.createVerticalStrut(10));

        addToCartButton = new JButton("Tambah ke Keranjang");
        rightPanel.add(addToCartButton);

        rightPanel.add(Box.createVerticalStrut(20));

        rightPanel.add(new JLabel("Pilih Promo:"));
        promoComboBox = new JComboBox<>();
        rightPanel.add(promoComboBox); // Add to panel here, content will be refreshed later

        processButton = new JButton("Process");
        rightPanel.add(processButton);

        refreshButton = new JButton("Refresh Produk");
        rightPanel.add(refreshButton);

        add(rightPanel, BorderLayout.EAST);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cartTableModel = new DefaultTableModel(new String[]{"Nama Produk", "Jumlah", "Harga", "Subtotal", "Custom"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartTableModel);
        JScrollPane scrollPane = new JScrollPane(cartTable);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        summaryPanel.add(new JLabel("No. Telp   :"));
        customerTelpField = new JTextField(); // Initialize customerTelpField
        summaryPanel.add(customerTelpField);

        summaryPanel.add(new JLabel("Sub    : Rp."));
        subtotalDisplayField = new JTextField("0"); // Initialize subtotalDisplayField
        subtotalDisplayField.setEditable(false);
        summaryPanel.add(subtotalDisplayField);

        summaryPanel.add(new JLabel("Disc   : Rp."));
        diskonDisplayField = new JTextField("0"); // Initialize diskonDisplayField
        diskonDisplayField.setEditable(false);
        summaryPanel.add(diskonDisplayField);

        summaryPanel.add(new JLabel("Total  : Rp."));
        totalLabel = new JLabel("0"); // Initialize totalLabel
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 16));
        summaryPanel.add(totalLabel);

        leftPanel.add(summaryPanel, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.CENTER);

        // Now that all fields are initialized, it's safe to call these methods
        refreshPromoComboBox();
        checkCustomerTelpStatusInitial();


        namaBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateFields();
            }
        });
        refreshComboBox();

        refreshButton.addActionListener(e -> refreshComboBox());

        addToCartButton.addActionListener(e -> {
            int selectedIndex = namaBox.getSelectedIndex();
            if (selectedIndex < 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada produk yang dipilih.");
                return;
            }
            Product selectedProduct = ProductManager.products.get(selectedIndex);

            int qty;
            try {
                qty = Integer.parseInt(quantityField.getText());
                if (qty <= 0) {
                    JOptionPane.showMessageDialog(this, "Kuantitas harus lebih dari 0");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Kuantitas harus berupa angka");
                return;
            }

            if (qty > selectedProduct.getStock()) {
                JOptionPane.showMessageDialog(this, "Stok tidak cukup!");
                return;
            }

            List<String> customOptions = new ArrayList<>();
            String gulaOption = (String) gula.getSelectedItem();
            if (!"Normal".equals(gulaOption)) {
                customOptions.add(gulaOption);
            }

            String susuOption = (String) susu.getSelectedItem();
            if (!"Tanpa".equals(susuOption)) {
                customOptions.add(susuOption);
            }

            String esOption = (String) es.getSelectedItem();
            if (!"Normal".equals(esOption)) {
                customOptions.add(esOption);
            }

            String sirupOption = (String) sirup.getSelectedItem();
            if (!"Tanpa Syrup".equals(sirupOption)) {
                customOptions.add( sirupOption);
            }

            String customization = String.join(", ", customOptions);


            boolean found = false;
            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                String prodNama = (String) cartTableModel.getValueAt(i, 0);
                String existingCustomization = (String) cartTableModel.getValueAt(i, 4);

                if (prodNama.equals(selectedProduct.getName()) && existingCustomization.equals(customization)) {
                    int currentQty = (int) cartTableModel.getValueAt(i, 1);
                    int newQty = currentQty + qty;

                    if (newQty > selectedProduct.getStock()) {
                        JOptionPane.showMessageDialog(this, "Total kuantitas melebihi stok!");
                        return;
                    }

                    cartTableModel.setValueAt(newQty, i, 1);
                    cartTableModel.setValueAt(newQty * selectedProduct.getPrice(), i, 3);
                    found = true;
                    break;
                }
            }

            if (!found) {
                cartTableModel.addRow(new Object[]{
                    selectedProduct.getName(),
                    qty,
                    selectedProduct.getPrice(),
                    qty * selectedProduct.getPrice(),
                    customization
                });
            }

            updateTotal();
            quantityField.setText("");
        });

        promoComboBox.addActionListener(e -> updateTotal());

        customerTelpField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                checkCustomerTelpStatus();
            }
            public void removeUpdate(DocumentEvent e) {
                checkCustomerTelpStatus();
            }
            public void insertUpdate(DocumentEvent e) {
                checkCustomerTelpStatus();
            }

            private void checkCustomerTelpStatus() {
                if (customerTelpField.getText().trim().isEmpty()) {
                    promoComboBox.setEnabled(false);
                    if (promoComboBox.getItemCount() > 0) {
                        promoComboBox.setSelectedIndex(0);
                    }
                } else {
                    promoComboBox.setEnabled(true);
                }
                updateTotal();
            }
        });
        // This is called after customerTelpField is initialized, so it's safe now.


        processButton.addActionListener(e -> {
            if (cartTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Keranjang kosong!");
                return;
            }

            String customerTelp = customerTelpField.getText().trim();

            Customer customer = null;

            if (customerTelp.isEmpty()) {
                promoComboBox.setSelectedIndex(0);
                updateTotal();
            } else {
                for (Customer c : CustomerManager.getCustomers()) {
                    if (c.getTelp().equals(customerTelp)) {
                        customer = c;
                        break;
                    }
                }

                if (customer == null) {
                    JOptionPane.showMessageDialog(this, "No. Telepon belum terdaftar sebagai pelanggan.", "Pelanggan Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                String prodNama = (String) cartTableModel.getValueAt(i, 0);
                int qty = (int) cartTableModel.getValueAt(i, 1);

                for (Product p : ProductManager.products) {
                    if (p.getName().equals(prodNama)) {
                        p.setStock(p.getStock() - qty);
                        break;
                    }
                }
            }

            // After successful transaction, check for promo usage and decrement stock if applicable
            Promo selectedPromo = (Promo) promoComboBox.getSelectedItem();
            if (selectedPromo != null && !selectedPromo.getNama().equals("Tidak Ada Promo")) {
                // Find the actual promo object in PromoManager to update its stock
                Promo actualPromoInManager = PromoManager.getPromoByName(selectedPromo.getNama());
                if (actualPromoInManager != null && actualPromoInManager.getStokPromo() > 0) {
                    int promoIndex = PromoManager.getPromos().indexOf(actualPromoInManager);
                    if (promoIndex != -1) {
                        Promo updatedPromo = new Promo(
                            actualPromoInManager.getNama(),
                            actualPromoInManager.getDiskon(),
                            actualPromoInManager.getStokPromo() - 1, // Decrement stock
                            actualPromoInManager.getTanggalAkhir().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        );
                        PromoManager.editPromo(promoIndex, updatedPromo);
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Transaksi berhasil!");
            cartTableModel.setRowCount(0);
            updateFields();
            updateTotal();
            ProductForm.loadProductData();
            refreshPromoComboBox(); // Refresh promos after potential stock change
            customerTelpField.setText("");
        });
    }

    private void checkCustomerTelpStatusInitial() {
        if (customerTelpField.getText().trim().isEmpty()) {
            promoComboBox.setEnabled(false);
            if (promoComboBox.getItemCount() > 0) {
                promoComboBox.setSelectedIndex(0);
            }
        } else {
            promoComboBox.setEnabled(true);
        }
        updateTotal();
    }

    public void updateFields() {
        int selectedIndex = namaBox.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= ProductManager.products.size()) {
            stockField.setText("");
            priceField.setText("");
            addToCartButton.setEnabled(false);
            return;
        }
        Product selectedProduct = ProductManager.products.get(selectedIndex);
        stockField.setText(String.valueOf(selectedProduct.getStock()));
        priceField.setText(String.format("%,.0f", selectedProduct.getPrice()));
        addToCartButton.setEnabled(true);
    }

    public void refreshComboBox() {
        namaBox.removeAllItems();
        if (ProductManager.products.isEmpty()) {
            stockField.setText("N/A");
            priceField.setText("N/A");
            addToCartButton.setEnabled(false);
            namaBox.revalidate();
            namaBox.repaint();
            return;
        }
        for (Product p : ProductManager.products) {
            namaBox.addItem(p.getName());
        }
        namaBox.setSelectedIndex(0);
        updateFields();
        addToCartButton.setEnabled(true);
        namaBox.revalidate();
        namaBox.repaint();
    }

    private void refreshPromoComboBox() {
        promoComboBox.removeAllItems();
        promoComboBox.addItem(new Promo("Tidak Ada Promo", 0.0, 1, "2019-12-01")); // Default "No Promo"

        LocalDate today = LocalDate.now();
        for (Promo p : PromoManager.getPromos()) {
            // Check if promo is not expired and has stock
            if ((p.getTanggalAkhir().isAfter(today) || p.getTanggalAkhir().isEqual(today)) && p.getStokPromo() > 0) {
                promoComboBox.addItem(p);
            }
        }

        // Check for birthday promo only if a customer is linked and has a birthday
        if (!customerTelpField.getText().trim().isEmpty()) {
            Customer currentCustomer = null;
            for (Customer c : CustomerManager.getCustomers()) {
                if (c.getTelp().equals(customerTelpField.getText().trim())) {
                    currentCustomer = c;
                    break;
                }
            }

            if (currentCustomer != null && currentCustomer.getUltah() != null) {
                if (currentCustomer.getUltah().getMonth() == today.getMonth() &&
                    currentCustomer.getUltah().getDayOfMonth() == today.getDayOfMonth()) {

                    Promo birthdayPromo = PromoManager.getPromoByName("Promo Ulang Tahun");
                    // Ensure birthday promo exists, is not expired, and has stock
                    if (birthdayPromo != null &&
                        (birthdayPromo.getTanggalAkhir().isAfter(today) || birthdayPromo.getTanggalAkhir().isEqual(today)) &&
                        birthdayPromo.getStokPromo() > 0) {

                        boolean alreadyAdded = false;
                        for (int i = 0; i < promoComboBox.getItemCount(); i++) {
                            if (promoComboBox.getItemAt(i).getNama().equals("Promo Ulang Tahun")) {
                                alreadyAdded = true;
                                break;
                            }
                        }
                        if (!alreadyAdded) {
                            promoComboBox.addItem(birthdayPromo);
                            JOptionPane.showMessageDialog(this, "Pelanggan berulang tahun! Promo Ulang Tahun tersedia.", "Promo Aktif", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        }

        if (promoComboBox.getItemCount() > 0) {
            promoComboBox.setSelectedIndex(0);
        }
        updateTotal();
    }

    private void updateTotal() {
        double subtotal = 0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            subtotal += ((Number) cartTableModel.getValueAt(i, 3)).doubleValue();
        }
        subtotalDisplayField.setText(String.format("%,.0f", subtotal));

        Promo selectedPromo = (Promo) promoComboBox.getSelectedItem();
        double promoDiskonPercentage = 0.0;

        if (selectedPromo != null && promoComboBox.isEnabled()) {
            promoDiskonPercentage = selectedPromo.getDiskon();
        }

        double diskonAmount = subtotal * (promoDiskonPercentage / 100);
        diskonDisplayField.setText(String.format("%,.0f", diskonAmount));
        double total = subtotal - diskonAmount;
        totalLabel.setText(String.format("%,.0f", total));
    }
}