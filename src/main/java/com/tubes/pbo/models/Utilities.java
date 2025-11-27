package com.tubes.pbo.models;

public abstract class Utilities {
    protected String name;
    protected String description;
    protected UtilityState state;
    protected Item itemInside; // Item hadiah jika puzzle selesai

    public Utilities(String name, String description, Item itemInside) {
        this.name = name;
        this.description = description;
        this.itemInside = itemInside;
        this.state = UtilityState.LOCKED; // Default terkunci
    }

    public String getName() { return name; }

    public String getDescription() {
        if (state == UtilityState.OPEN) {
            return name + " sudah terbuka dan kosong.";
        }
        return description;
    }

    // Method Abstrak: Setiap benda punya cara buka kunci yang beda
    // Return String sebagai pesan feedback ke user
    public abstract String solve(String input);

    // Method untuk mengambil item di dalamnya
    public Item lootItem() {
        if (state == UtilityState.UNLOCKED || state == UtilityState.OPEN) {
            if (itemInside != null) {
                Item temp = itemInside;
                itemInside = null; // Item diambil
                state = UtilityState.OPEN;
                return temp;
            }
        }
        return null; // Gagal ambil
    }

    public boolean isLocked() {
        return state == UtilityState.LOCKED;
    }
}