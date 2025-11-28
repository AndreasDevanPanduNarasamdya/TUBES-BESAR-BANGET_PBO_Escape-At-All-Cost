package com.tubes.pbo.models;

public class KeyUtility extends Utilities {
    String requiredKeyName;

    public KeyUtility(String name, String desc, Item loot, String keyName) {
        super(name, desc, loot);
        this.requiredKeyName = keyName;
    }

    @Override
    public String solve(String inputKeyName) {
        if (!isLocked()) return "Sudah tidak terkunci.";

        // Cek apakah nama item yang dipakai user cocok dengan kunci yang diminta
        if (inputKeyName.equalsIgnoreCase(requiredKeyName)) {
            this.state = UtilityState.UNLOCKED;
            return "Krek... Kunci cocok! " + name + " terbuka.";
        } else {
            return "Kunci itu tidak pas untuk " + name + ".";
        }
    }
}