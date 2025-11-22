package com.tubes.pbo.world;

import com.tubes.pbo.models.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private String name;
    private String description;
    private Map<String, Room> exits;

    // UPDATE 1: List untuk menampung item yang ada di ruangan ini
    private List<Item> items;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
    }

    // --- MANAGEMENT EXITS ---
    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    // --- MANAGEMENT ITEMS (BARU) ---

    // Menaruh item di ruangan (misal saat inisialisasi game)
    public void addItem(Item item) {
        items.add(item);
    }

    // Mengambil item dari ruangan berdasarkan nama
    public Item removeItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                items.remove(item);
                return item; // Item ditemukan dan diserahkan ke caller
            }
        }
        return null; // Item tidak ada di ruangan ini
    }

    // Cek apakah ruangan punya item tertentu (tanpa mengambil)
    public boolean hasItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    // --- GETTERS FOR UI ---
    public String getName() {
        return name;
    }

    // UPDATE 2: Deskripsi kini menggabungkan teks cerita + daftar item
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(description);

        if (!items.isEmpty()) {
            sb.append("\n\n[ITEM DI SINI]: ");
            for (Item item : items) {
                sb.append(item.getName()).append(", ");
            }
            // Hapus koma terakhir biar rapi
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }
}