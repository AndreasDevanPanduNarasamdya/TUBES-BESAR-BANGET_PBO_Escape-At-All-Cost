package com.tubes.pbo.patterns.singleton;

import com.tubes.pbo.models.Item;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    // 1. Static instance variable
    private static Inventory instance;

    // 2. Collection untuk menyimpan item
    private List<Item> items;
    private final int MAX_CAPACITY = 5; // Aturan tugas

    // 3. Private Constructor (biar gak bisa di-new sembarangan)
    private Inventory() {
        items = new ArrayList<>();
    }

    // 4. Public Static method untuk akses global
    public static Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items); // Return copy agar aman
    }

    public void addItem(Item item) {
        if (items.size() < MAX_CAPACITY) {
            items.add(item);
            System.out.println("Berhasil mengambil: " + item.getName());
        } else {
            System.out.println("Tas penuh! Buang sesuatu dulu.");
        }
    }

    public void removeItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                items.remove(item);
                System.out.println(item.getName() + " dihapus dari tas.");
                return;
            }
        }
    }

    public void showInventory() {
        System.out.println("=== ISI TAS ===");
        if (items.isEmpty()) {
            System.out.println("(Kosong)");
        } else {
            for (Item item : items) {
                System.out.println("- " + item.getName());
            }
        }
    }
}