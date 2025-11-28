package com.tubes.pbo.models;

public class ExitDoor extends KeyUtility {

    public ExitDoor(String name, String desc, String requiredKeyName) {
        // Pintu keluar tidak berisi item (null), tapi butuh kunci
        super(name, desc, null, requiredKeyName);
    }

    @Override
    public String solve(String inputKeyName) {
        // Panggil logic pengecekan kunci milik orang tua (KeyUtility)
        // Kita cek manual di sini agar bisa return pesan CUSTOM
        if (!isLocked()) return "Pintu ini sudah terbuka lebar. DUNIA LUAR MENUNGGU!";

        if (inputKeyName.equalsIgnoreCase(getRequiredKeyName())) {
            this.state = UtilityState.UNLOCKED;
            // INI BAGIAN PENTING: Pesan Kemenangan
            return "\n=========================================\n" +
                    "KLIK! Kunci berputar...\n" +
                    "Cahaya matahari menyilaukan mata menyeruak masuk.\n" +
                    "KAMU BERHASIL KABUR! (THE END)\n" +
                    "=========================================";
        } else {
            return "Kunci itu tidak bisa membuka Pintu Keluar ini.";
        }
    }

    // Getter helper karena field requiredKeyName di parent bersifat private
    // Atau Anda bisa ubah visibility di KeyUtility.java menjadi 'protected'
    private String getRequiredKeyName() {
        // Jika field di KeyUtility private dan tidak ada getter,
        // kita terpaksa hardcode logika cek di sini atau ubah parent.
        // ASUMSI: Ubah dulu KeyUtility.java agar 'requiredKeyName' jadi PROTECTED
        return super.requiredKeyName;
    }
}