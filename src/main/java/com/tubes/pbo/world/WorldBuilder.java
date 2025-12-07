package com.tubes.pbo.world;

import com.tubes.pbo.models.Item;
import com.tubes.pbo.models.ExitDoor;
import com.tubes.pbo.models.OpenableUtility;
import com.tubes.pbo.models.PasswordUtility;
import com.tubes.pbo.models.Utilities; // Gunakan tipe data parent
import com.tubes.pbo.patterns.factory.UtilityFactory; // Import Factory

public class WorldBuilder {

    public Room buildWorld() {
        // --- 1. MEMBUAT RUANGAN ---
        Room bedroom = new Room("Kamar Tidur", "Tempat istirahat. Ada kasur berantakan.");
        Room livingRoom = new Room("Ruang Tamu", "Pusat rumah. Ada TV dan Sofa.");
        Room kitchen = new Room("Dapur", "Area memasak. Bau gas bocor.");
        Room storageRoom = new Room("Gudang", "Ruangan pengap penuh debu dan sarang laba-laba."); // Ruangan Baru

        // --- 2. MEMBUAT ITEM ---
        Item kertasKode = new Item("Kertas", "Secarik kertas bertuliskan: 1234");
        Item balokkayu = new Item("balokkayu", "Sebuah balok kayu");
        Item balokbesi = new Item("balokbesi", "Sebuah balok besi");
        Item palu = new Item("Palu", "Palu besi yang kuat.");
        //Item senter = new Item("Senter", "Senter LED terang (Butuh baterai?).");
        //Item jeruk = new Item("Jeruk", "Buah jeruk segar.");
        Item sekrup = new Item("Sekrup", "Sekrup kecil berkarat.");
        Item theonepiece = new Item("Theonepiece", "The One Piece is Real.");
        Item kunci = new Item("Kunci", "Kunci besi berkarat. Sepertinya penting.");

//        Utilities jaket = UtilityFactory.createContainer("Jaket", "Jaket kulit tergantung.", );
//        bedroom.addUtility(jaket);

        Utilities closet = UtilityFactory.createContainer("Lemari", "Lemari pakaian kayu.", palu);
        bedroom.addUtility(closet);

        Utilities vas = UtilityFactory.createBreakable("Vas", "Terdapat benda sus didalam.", kertasKode, "Palu");
        kitchen.addUtility(vas);

        Utilities brankas = UtilityFactory.createSafe("Brankas", "Butuh 4 digit PIN",kunci, "1234");
        storageRoom.addUtility(brankas);

        Utilities pintuKeluar = UtilityFactory.createExit("Exit", "Pintu besi berat.", "Kunci");
        storageRoom.addUtility(pintuKeluar);

//        Utilities kulkas = UtilityFactory.createContainer("Kulkas", "Kulkas 1 pintu.", kunci);
//        kitchen.addUtility(kulkas);

        Utilities cabinet = UtilityFactory.createContainer("Kabinet", "Laci penyimpanan.", sekrup);
        kitchen.addUtility(cabinet);

        bedroom.setExit("right", livingRoom);
        livingRoom.setExit("left", bedroom);
        livingRoom.setExit("right", kitchen);
        kitchen.setExit("left", livingRoom);
        kitchen.setExit("right", storageRoom);
        storageRoom.setExit("left", kitchen);

        return bedroom;
    }

//    public Room buildLevel2() {
//        // --- 1. SETUP RUANGAN (DUNGEON MAP) ---
//        Room cell = new Room("Sel Penjara", "Tempatmu dikurung. Dingin dan lembab.");
//        Room hallway = new Room("Lorong Bawah Tanah", "Lorong gelap dengan obor redup di dinding.");
//        Room guardRoom = new Room("Pos Penjaga", "Meja berantakan bekas penjaga main kartu.");
//        Room Dungeonstorage = new Room("Logistik", "Tumpukan barang bekas dan debu tebal.");
//        Room exitGate = new Room("Gerbang Utama", "Cahaya kebebasan mengintip dari celah pintu.");
//
//        // --- 2. SETUP ITEM ---
//        // Item Clue
//        Item kertasKode = new Item("Sobekan_Kertas", "Tulisan tangan jelek: 'Sandi Brankas: 5555'");
//
//        // Item Alat (Reward dari Brankas)
//        Item palu = new Item("Palu_Godam", "Palu besar untuk menghancurkan benda keras.");
//
//        // Item Kunci (Reward dari menghancurkan benda)
//        Item kunciUtama = new Item("Kunci_Gerbang", "Kunci besi besar berkarat.");
//
//        // --- 3. ISI SEL PENJARA (START) ---
//        // Clue ditaruh di sini agar pemain harus 'cek' lingkungan awal
//        // Kita taruh di lantai saja biar mudah, atau di dalam kasur (Container biasa)
//        Utilities kasur = UtilityFactory.createContainer("Kasur_Jerami", "Tumpukan jerami bau apek.", kertasKode);
//        cell.addUtility(kasur);
//
//        // --- 4. ISI POS PENJAGA ---
//        // Ada Brankas yang butuh kode dari kertas tadi.
//        // Hadiahnya adalah PALU.
//        Utilities brankas = UtilityFactory.createSafe("Brankas_Besi", "Kotak penyimpanan senjata.", palu, "5555");
//        guardRoom.addUtility(brankas);
//
//        // --- 5. ISI GUDANG LOGISTIK ---
//        // Ada Gentong Besar yang mencurigakan. Harus dihancurkan pakai PALU.
//        // Hadiahnya adalah KUNCI UTAMA.
//        Utilities gentong = UtilityFactory.createBreakable("Gentong", "Gentong tanah liat retak.", kunciUtama, "Palu_Godam");
//        Dungeonstorage.addUtility(gentong);
//
//        // --- 6. ISI GERBANG UTAMA ---
//        // Pintu Exit. Butuh KUNCI UTAMA.
//        Utilities pintuKeluar = UtilityFactory.createExit("Pintu_Besi", "Pintu keluar yang kokoh.", "Kunci_Gerbang");
//        exitGate.addUtility(pintuKeluar);
//
//        cell.setExit("right", hallway);
//        hallway.setExit("left", cell);
//
//        // Lorong <-> Gerbang (Kanan Ujung)
//        hallway.setExit("right", exitGate);
//        exitGate.setExit("left", hallway);
//
//        // Lorong <-> Pos Penjaga (Kita pakai 'right' dari lorong seolah-olah belok,
//        // tapi karena keterbatasan command 'left/right', kita buat ini Linear bercabang semu)
//        // Agar simple di UI Side Scrolling:
//        // Kita ubah susunan: [Sel] -> [Gudang] -> [Lorong] -> [Pos] -> [Gerbang]
//        // Ini membuat player harus jalan jauh bolak balik.
//
//        // REVISI MAP LINEAR TAPI JAUH (HARDER NAVIGATION):
//        // [Sel] <-> [Gudang] <-> [Lorong] <-> [Pos Penjaga] <-> [Gerbang]
//
//        // Reset connection
//        cell.setExit("right", Dungeonstorage);
//
//        Dungeonstorage.setExit("left", cell);
//        Dungeonstorage.setExit("right", hallway);
//
//        hallway.setExit("left", Dungeonstorage);
//        hallway.setExit("right", guardRoom);
//
//        guardRoom.setExit("left", hallway);
//        guardRoom.setExit("right", exitGate);
//
//        exitGate.setExit("left", guardRoom);
//
//        return cell; // Mulai di Sel
//    }
}

