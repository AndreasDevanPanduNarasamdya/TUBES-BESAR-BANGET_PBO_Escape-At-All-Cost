package com.tubes.pbo.models;

public class OpenableUtility extends Utilities {

    public OpenableUtility(String name, String desc, Item loot) {
        super(name, desc, loot);
        // Default state untuk benda ini adalah UNLOCKED (Bisa langsung dibuka)
        this.state = UtilityState.UNLOCKED;
    }

    @Override
    public String solve(String input) {
        // Tidak butuh input password/kunci
        if (state == UtilityState.OPEN) {
            return name + " sudah terbuka.";
        }

        // Ubah status jadi OPEN agar item bisa diambil
        this.state = UtilityState.OPEN;
        return "Kamu membuka " + name + ".";
    }
}