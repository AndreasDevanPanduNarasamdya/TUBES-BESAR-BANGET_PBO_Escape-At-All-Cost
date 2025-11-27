package com.tubes.pbo.world;

import com.tubes.pbo.models.Item;
import com.tubes.pbo.models.PasswordUtility;

public class WorldBuilder {

    // Method ini mengembalikan Room awal (Starting Room)
    public Room buildWorld() {
        // --- 1. MEMBUAT OBJEK RUANGAN (LINEAR MAP) ---
        // Urutan: [KAMAR TIDUR] <-> [RUANG TAMU] <-> [DAPUR]

        Room bedroom = new Room("Kamar Tidur", "Tempat istirahat. Ada kasur berantakan.");
        Room livingRoom = new Room("Ruang Tamu", "Pusat rumah. Ada TV dan Sofa.");
        Room kitchen = new Room("Dapur", "Area memasak. Bau gas bocor.");

        // --- 2. SETUP ITEMS & UTILITIES ---
        Item pedangNaga = new Item("Pedang Naga", "Pedang legendaris!");

        // Isi Kamar Tidur (Paling Kiri)
        bedroom.addItem(new Item("Kunci", "Kunci perak kecil."));
        bedroom.addItem(new Item("Catatan", "Kertas bertuliskan: PIN BRANKAS ADALAH 1234"));

        // Isi Ruang Tamu (Tengah)
        livingRoom.addItem(new Item("Senter", "Senter kecil."));
        PasswordUtility brankas = new PasswordUtility("Brankas Besi", "Butuh 4 digit PIN", pedangNaga, "1234");
        livingRoom.addUtility(brankas);

        // Isi Dapur (Paling Kanan)
        kitchen.addItem(new Item("Pisau", "Pisau dapur tumpul."));

        // --- 3. MENGHUBUNGKAN RUANGAN (LEFT / RIGHT ONLY) ---

        // Kamar Tidur (Hanya bisa ke Kanan -> Ruang Tamu)
        bedroom.setExit("right", livingRoom);

        // Ruang Tamu (Kiri -> Kamar, Kanan -> Dapur)
        livingRoom.setExit("left", bedroom);
        livingRoom.setExit("right", kitchen);

        // Dapur (Hanya bisa ke Kiri -> Ruang Tamu)
        kitchen.setExit("left", livingRoom);

        // --- 4. RETURN STARTING ROOM ---
        // Kita mulai di Kamar Tidur (Ujung Kiri) sesuai tema "Bangun Tidur"
        return bedroom;
    }
}