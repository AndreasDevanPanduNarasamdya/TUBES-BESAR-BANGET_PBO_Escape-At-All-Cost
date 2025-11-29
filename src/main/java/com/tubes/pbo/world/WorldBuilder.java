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
        Item baterai = new Item("Baterai", "Baterai AA tipe Alkaline.");
        //Item senter = new Item("Senter", "Senter LED terang (Butuh baterai?).");
        //Item jeruk = new Item("Jeruk", "Buah jeruk segar.");
        Item sekrup = new Item("Sekrup", "Sekrup kecil berkarat.");
        Item theonepiece = new Item("Theonepiece", "The One Piece is Real.");
        Item kunci = new Item("Kunci", "Kunci besi berkarat. Sepertinya penting.");


        // --- 3. ISI KAMAR TIDUR (PAKAI FACTORY) ---
        // Sebelum: OpenableUtility jaket = new OpenableUtility(...)
        // Sesudah:
        Utilities jaket = UtilityFactory.createContainer("Jaket", "Jaket kulit tergantung.", kertasKode);
        bedroom.addUtility(jaket);

        Utilities closet = UtilityFactory.createContainer("Lemari", "Lemari pakaian kayu.", baterai);
        bedroom.addUtility(closet);

        Utilities brankas = UtilityFactory.createSafe("Brankas", "Butuh 4 digit PIN", theonepiece, "1234");
        storageRoom.addUtility(brankas);

        Utilities pintuKeluar = UtilityFactory.createExit("Exit", "Pintu besi berat.", "Kunci");
        storageRoom.addUtility(pintuKeluar);

        Utilities kulkas = UtilityFactory.createContainer("Kulkas", "Kulkas 1 pintu.", kunci);
        kitchen.addUtility(kulkas);

        Utilities cabinet = UtilityFactory.createContainer("Kabinet", "Laci penyimpanan.", sekrup);
        kitchen.addUtility(cabinet);

        livingRoom.setExit("left", bedroom);
        livingRoom.setExit("right", kitchen);
        kitchen.setExit("left", livingRoom);
        kitchen.setExit("right", storageRoom);
        storageRoom.setExit("left", kitchen);

        return bedroom;
    }

    public Room buildWorld2() {
        // --- 1. MEMBUAT RUANGAN ---
        Room bedroom = new Room("Kamar Tidur", "Tempat istirahat. Ada kasur berantakan.");
        Room livingRoom = new Room("Ruang Tamu", "Pusat rumah. Ada TV dan Sofa.");
        Room kitchen = new Room("Dapur", "Area memasak. Bau gas bocor.");
        Room storageRoom = new Room("Gudang", "Ruangan pengap penuh debu dan sarang laba-laba."); // Ruangan Baru

        // --- 2. MEMBUAT ITEM ---
        Item kertasKode = new Item("Kertas", "Secarik kertas bertuliskan: 1234");
        Item baterai = new Item("Baterai", "Baterai AA tipe Alkaline.");
        //Item senter = new Item("Senter", "Senter LED terang (Butuh baterai?).");
        //Item jeruk = new Item("Jeruk", "Buah jeruk segar.");
        Item sekrup = new Item("Sekrup", "Sekrup kecil berkarat.");
        Item theonepiece = new Item("Theonepiece", "The One Piece is Real.");
        Item kunci = new Item("Kunci", "Kunci besi berkarat. Sepertinya penting.");


        // --- 3. ISI KAMAR TIDUR (PAKAI FACTORY) ---
        // Sebelum: OpenableUtility jaket = new OpenableUtility(...)
        // Sesudah:
        Utilities jaket = UtilityFactory.createContainer("Jaket", "Jaket kulit tergantung.", kertasKode);
        bedroom.addUtility(jaket);

        Utilities closet = UtilityFactory.createContainer("Lemari", "Lemari pakaian kayu.", baterai);
        bedroom.addUtility(closet);

        Utilities brankas = UtilityFactory.createSafe("Brankas", "Butuh 4 digit PIN", theonepiece, "1234");
        storageRoom.addUtility(brankas);

        Utilities pintuKeluar = UtilityFactory.createExit("Exit", "Pintu besi berat.", "Kunci");
        storageRoom.addUtility(pintuKeluar);

        Utilities kulkas = UtilityFactory.createContainer("Kulkas", "Kulkas 1 pintu.", kunci);
        kitchen.addUtility(kulkas);

        Utilities cabinet = UtilityFactory.createContainer("Kabinet", "Laci penyimpanan.", sekrup);
        kitchen.addUtility(cabinet);

        livingRoom.setExit("left", bedroom);
        livingRoom.setExit("right", kitchen);
        kitchen.setExit("left", livingRoom);
        kitchen.setExit("right", storageRoom);
        storageRoom.setExit("left", kitchen);

        return bedroom;
    }
}

