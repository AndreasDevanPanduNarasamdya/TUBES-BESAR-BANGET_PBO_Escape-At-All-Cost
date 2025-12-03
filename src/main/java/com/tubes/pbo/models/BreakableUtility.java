package com.tubes.pbo.models;

public class BreakableUtility extends Utilities {
    private String requiredToolName; // Nama alat yang dibutuhkan, misal "Palu"

    public BreakableUtility(String name, String description, Item loot, String requiredToolName) {
        super(name, description, loot);
        this.requiredToolName = requiredToolName;
        // Kita anggap LOCKED = Masih Utuh, OPEN = Sudah Pecah
        this.state = UtilityState.LOCKED;
    }

    @Override
    public String solve(String input) {
        // BreakableUtility tidak dibuka dengan command 'buka', tapi 'hancurkan'
        return "Benda ini tidak bisa dibuka baik-baik. Sepertinya harus dihancurkan.";
    }

    // Method khusus untuk logic menghancurkan
    public String smash(String toolUsed) {
        if (state == UtilityState.OPEN) {
            return name + " sudah hancur berkeping-keping.";
        }

        // Cek apakah alatnya benar (atau jika null/empty berarti pakai tangan kosong)
        if (requiredToolName != null && !requiredToolName.isEmpty()) {
            if (!requiredToolName.equalsIgnoreCase(toolUsed)) {
                return "Tangan kosong sakit! Kamu butuh alat seperti [" + requiredToolName + "] untuk memecahkan ini.";
            }
        }

        // State Buat Berhasil Pecah
        this.state = UtilityState.OPEN;
        return "PRANG!! Kamu menghancurkan " + name + " dengan " + toolUsed + ".\n" +
               "Di antara pecahan beling, kamu melihat ada sesuatu!";
    }

    @Override
    public String getDescription() {
        if (state == UtilityState.OPEN) {
            return "Pecahan " + name + " berserakan di lantai.";
        }
        return description + " (Kelihatannya rapuh)";
    }
}