package com.tubes.pbo.world;

import com.tubes.pbo.models.Item;
import com.tubes.pbo.models.Utilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private String name;
    private String description;
    private Map<String, Room> exits;
    private List<Utilities> utilities;
    private List<Item> items;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.utilities = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    // --- MANAGEMENT EXITS ---
    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    // --- MANAGEMENT ITEMS ---
    public void addItem(Item item) {
        items.add(item);
    }

    public Item removeItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                items.remove(item);
                return item;
            }
        }
        return null;
    }

    public boolean hasItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) return true;
        }
        return false;
    }

    // --- MANAGEMENT UTILITIES ---
    public void addUtility(Utilities util) {
        utilities.add(util);
    }

    public Utilities getUtility(String name) {
        for (Utilities util : utilities) {
            if (util.getName().equalsIgnoreCase(name)) return util;
        }
        return null;
    }

    // --- GETTERS FOR UI ---
    public String getName() {
        return name;
    }

    // Deskripsi digabung dengan list item/utility untuk teks narasi
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(description);

        if (!utilities.isEmpty()) {
            sb.append("\n[INTERAKSI]: ");
            for (Utilities util : utilities) {
                sb.append("[").append(util.getName()).append("] ");
            }
        }

        if (!items.isEmpty()) {
            sb.append("\n[ITEM DI SINI]: ");
            for (Item item : items) {
                sb.append(item.getName()).append(", ");
            }
            sb.setLength(sb.length() - 2); // Hapus koma terakhir
        }
        return sb.toString();
    }
}