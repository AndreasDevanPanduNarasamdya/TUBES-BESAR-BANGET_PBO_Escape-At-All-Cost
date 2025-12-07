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
        Item kayu = new Item("BalokKayu", "Potongan kayu sisa bangunan.");
        Item besi = new Item("BalokBesi", "Potongan besi tua yang berat.");
        Item sekrup = new Item("Sekrup", "Sekrup kecil berkarat.");
        Item theonepiece = new Item("Theonepiece", "The One Piece is Real.");
        Item kunci = new Item("Kunci", "Kunci besi berkarat. Sepertinya penting.");

//        Utilities jaket = UtilityFactory.createContainer("Jaket", "Jaket kulit tergantung.", );
//        bedroom.addUtility(jaket);

        Utilities closet = UtilityFactory.createContainer("Lemari", "Lemari pakaian kayu.", balokkayu);
        bedroom.addUtility(closet);

        Utilities vas = UtilityFactory.createBreakable("Vas", "Terdapat benda sus didalam.", kertasKode, "Palu");
        kitchen.addUtility(vas);

        Utilities brankas = UtilityFactory.createSafe("Brankas", "Butuh 4 digit PIN",kunci, "1234");
        storageRoom.addUtility(brankas);

        Utilities pintuKeluar = UtilityFactory.createExit("Exit", "Pintu besi berat.", "Kunci");
        storageRoom.addUtility(pintuKeluar);

//        Utilities kulkas = UtilityFactory.createContainer("Kulkas", "Kulkas 1 pintu.", kunci);
//        kitchen.addUtility(kulkas);

        Utilities cabinet = UtilityFactory.createContainer("Kabinet", "Laci penyimpanan.", balokbesi);
        kitchen.addUtility(cabinet);

        bedroom.setExit("right", livingRoom);
        livingRoom.setExit("left", bedroom);
        livingRoom.setExit("right", kitchen);
        kitchen.setExit("left", livingRoom);
        kitchen.setExit("right", storageRoom);
        storageRoom.setExit("left", kitchen);

        return bedroom;
    }

    public Room buildLevel2() {
        System.out.println("Building Level 2: The Dungeon...");

        // 1. CREATE ROOMS
        Room cell = new Room("Dungeon Cell", "Dingin, lembab, dan bau busuk.");
        Room hallway = new Room("Lorong Bawah Tanah", "Lorong gelap dengan obor redup.");
        Room guardRoom = new Room("Pos Penjaga", "Meja berantakan bekas penjaga main kartu.");
        Room wardenOffice = new Room("Kantor Sipir", "Ruangan mewah di tengah penjara kumuh.");

        // 2. ITEMS
        Item spoon = new Item("Sendok_Besi", "Sendok makan yang sudah diasah tajam.");
        Item silverKey = new Item("Kunci_Perak", "Kunci kecil untuk lemari.");
        Item paperCode = new Item("Catatan_Sipir", "Kertas bertuliskan: PASSWORD BRANKAS = 7777");
        Item goldKey = new Item("Kunci_Emas", "Kunci besar berkilauan.");

        // 3. UTILITIES & PUZZLES

        // Puzzle 1: NPC (The Prisoner)
        // Dia memberikan 'Sendok_Besi' jika diajak bicara
        Utilities prisoner = UtilityFactory.createNPC("Tahanan_Tua",
                "Orang tua kurus di pojok ruangan.",
                spoon,
                "Sstt! Jangan berisik. Aku menyembunyikan sendok ini bertahun-tahun... ambillah, gunakan untuk menggali celah di dinding itu!");
        cell.addUtility(prisoner);

        // Puzzle 2: Breakable Wall (Needs Spoon)
        // Loot: Silver Key
        Utilities crack = UtilityFactory.createBreakable("Celah_Dinding",
                "Ada batu bata yang longgar di dinding.",
                silverKey,
                "Sendok_Besi");
        cell.addUtility(crack);

        // Puzzle 3: Locked Locker (Needs Silver Key)
        // Loot: Paper Code
        Utilities locker = UtilityFactory.createContainer("Loker_Senjata",
                "Lemari besi tempat menyimpan barang sitaan.",
                paperCode);
        // Kita kunci manual (karena createContainer defaultnya UNLOCKED)
        // Tapi di logic game Anda, container biasa itu 'OpenableUtility' yg tidak butuh kunci.
        // Jika ingin butuh kunci, kita harus pakai logic 'KeyUtility' tapi yg berisi Item.
        // Mari kita buat KeyUtility biasa yang berisi item:
        // (Kita modifikasi sedikit cara buatnya manual karena Factory terbatas)
        Utilities lockedLocker = new com.tubes.pbo.models.KeyUtility("Loker_Besi", "Terkunci rapat.", paperCode, "Kunci_Perak");
        guardRoom.addUtility(lockedLocker);

        // Puzzle 4: Safe (Needs Password)
        // Loot: Gold Key (Main Exit Key)
        Utilities safe = UtilityFactory.createSafe("Brankas_Rahasia",
                "Brankas kokoh di balik lukisan.",
                goldKey,
                "7777");
        wardenOffice.addUtility(safe);

        // EXIT DOOR
        Utilities mainGate = UtilityFactory.createExit("Gerbang_Utama",
                "Pintu besi raksasa menuju kebebasan.",
                "Kunci_Emas");
        wardenOffice.addUtility(mainGate);

        // 4. CONNECT ROOMS (Linear Map)
        // [Cell] <-> [Hallway] <-> [GuardRoom] <-> [WardenOffice]

        cell.setExit("right", hallway);
        hallway.setExit("left", cell);

        hallway.setExit("right", guardRoom);
        guardRoom.setExit("left", hallway);

        guardRoom.setExit("right", wardenOffice);
        wardenOffice.setExit("left", guardRoom);

        return cell; // Start position
    }
}

