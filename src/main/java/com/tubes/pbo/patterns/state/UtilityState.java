package com.tubes.pbo.patterns.state;

public enum UtilityState {
    LOCKED,     // Terkunci (Butuh password/kunci)
    UNLOCKED,   // Sudah dibuka kuncinya, tapi belum diambil isinya
    OPEN        // Sudah kosong/terbuka sepenuhnya
}