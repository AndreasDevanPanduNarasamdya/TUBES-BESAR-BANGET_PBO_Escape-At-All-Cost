package com.tubes.pbo.world;

import com.tubes.pbo.models.Item;
import com.tubes.pbo.models.ExitDoor;
import com.tubes.pbo.models.OpenableUtility;
import com.tubes.pbo.models.PasswordUtility;

public class WorldBuilder {

    public Room buildWorld() {
        // --- 1. MEMBUAT RUANGAN ---
        Room bedroom = new Room("Kamar Tidur", "Tempat istirahat. Ada kasur berantakan.");
        Room livingRoom = new Room("Ruang Tamu", "Pusat rumah. Ada TV dan Sofa.");
        Room kitchen = new Room("Dapur", "Area memasak. Bau gas bocor.");
        Room storageRoom = new Room("Gudang", "Ruangan pengap penuh debu dan sarang laba-laba."); // Ruangan Baru

        // --- 2. MEMBUAT ITEM ---
        Item kertasKode = new Item("Kertas", "Secarik kertas bertuliskan: 1234");
        Item baterai = new Item("Baterai", "Baterai AA tipe Alkaline.");
        Item senter = new Item("Senter", "Senter LED terang (Butuh baterai?).");
        Item jeruk = new Item("Jeruk", "Buah jeruk segar.");
        Item sekrup = new Item("Sekrup", "Sekrup kecil berkarat.");
        Item theonepiece = new Item("Theonepiece", "The One Piece is Real.");
        Item kunci = new Item("Kunci", "Kunci besi berkarat. Sepertinya penting.");

        ExitDoor pintuKeluar = new ExitDoor("Exit", "Pintu besi berat berkarat.", "Kunci");


        // --- 3. ISI KAMAR TIDUR ---
        // Jaket (Utility Biasa) -> Isi Kertas
        OpenableUtility jaket = new OpenableUtility("Jaket", "Jaket kulit tergantung.", kertasKode);
        bedroom.addUtility(jaket);

        // Lemari/Closet (Utility Biasa) -> Isi Baterai
        OpenableUtility closet = new OpenableUtility("Lemari", "Lemari pakaian kayu.", baterai);
        bedroom.addUtility(closet);


        // --- 4. ISI RUANG TAMU ---
        // UBAH BARIS INI (Hapus kata " Besi")
        PasswordUtility brankas = new PasswordUtility("Brankas", "Butuh 4 digit PIN", theonepiece, "1234");
        storageRoom.addUtility(brankas);

        storageRoom.addUtility(pintuKeluar);

        // --- 5. ISI DAPUR ---
        // Kulkas (Utility Biasa) -> Isi Jeruk
        OpenableUtility kulkas = new OpenableUtility("Kulkas", "Kulkas 1 pintu.", kunci);
        kitchen.addUtility(kulkas);

        // Kabinet (Utility Biasa) -> Isi Sekrup
        OpenableUtility cabinet = new OpenableUtility("Kabinet", "Laci penyimpanan alat dapur.", sekrup);
        kitchen.addUtility(cabinet);

        // Kitchen <-> Storage (NEW CONNECTION)
        kitchen.setExit("right", storageRoom);
        storageRoom.setExit("left", kitchen);

        // --- 6. HUBUNGKAN RUANGAN ---
        bedroom.setExit("right", livingRoom);
        livingRoom.setExit("left", bedroom);
        livingRoom.setExit("right", kitchen);
        kitchen.setExit("left", livingRoom);

        return bedroom;
    }
}