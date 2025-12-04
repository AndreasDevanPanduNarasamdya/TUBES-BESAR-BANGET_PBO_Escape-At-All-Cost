package com.tubes.pbo.models;

import com.tubes.pbo.patterns.state.UtilityState;

public class PasswordUtility extends Utilities {
    private String correctPassword;

    public PasswordUtility(String name, String desc, Item loot, String password) {
        super(name, desc, loot);
        this.correctPassword = password;
    }

    @Override
    public String solve(String input) {
        if (!isLocked()) return "Benda ini sudah terbuka kok.";

        if (input.equalsIgnoreCase(correctPassword)) {
            this.state = UtilityState.OPEN;
            return "KLIK! Password benar. " + name + " terbuka! (Ketik 'ambil' untuk mengambil isinya)";
        } else {
            return "Password salah! " + name + " tidak bergeming.";
        }
    }
}