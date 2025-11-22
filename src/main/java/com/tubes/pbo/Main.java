package com.tubes.pbo;

import com.tubes.pbo.models.Item;
import com.tubes.pbo.patterns.singleton.Inventory;
import com.tubes.pbo.ui.ConsoleUI;
import com.tubes.pbo.world.Room;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // ==========================================
        // 1. SETUP AREA (Inisialisasi Dunia Game)
        // ==========================================

        // Buat Ruangan
        Room livingRoom = new Room("Ruang Tamu", "Ruangan gelap dengan sofa berdebu.");
        Room kitchen = new Room("Dapur", "Bau amis tercium kuat. Ada lalat beterbangan.");
        Room bedroom = new Room("Kamar Tidur", "Kasur berantakan. Jendela tertutup rapat.");

        // Hubungkan Ruangan (Exit)
        // Ruang Tamu <-> Dapur (Utara)
        livingRoom.setExit("north", kitchen);
        kitchen.setExit("south", livingRoom);

        // Ruang Tamu <-> Kamar Tidur (Timur)
        livingRoom.setExit("east", bedroom);
        bedroom.setExit("west", livingRoom);

        // Buat Item & Taruh di Ruangan
        livingRoom.addItem(new Item("Senter", "Senter kecil, baterainya masih ada."));
        kitchen.addItem(new Item("Pisau", "Pisau daging yang sangat tumpul."));
        bedroom.addItem(new Item("Kunci", "Kunci perak kecil. Mungkin untuk pintu depan?"));
        bedroom.addItem(new Item("Catatan", "Kertas lusuh bertuliskan: 'JANGAN KELUAR MALAM INI'"));

        // ==========================================
        // 2. PLAYER SETUP
        // ==========================================
        Room currentRoom = livingRoom;
        Inventory tas = Inventory.getInstance();
        Scanner scanner = new Scanner(System.in);

        // Variabel untuk menyimpan pesan feedback ke user
        String lastOutput = "Selamat datang! Ketik 'help' untuk bantuan.";
        boolean isRunning = true;

        // ==========================================
        // 3. GAME LOOP
        // ==========================================
        while (isRunning) {
            // A. Render Tampilan (UI)
            ConsoleUI.render(currentRoom, tas, lastOutput);

            // B. Baca Input User
            String input = scanner.nextLine().toLowerCase().trim();

            // Reset pesan feedback
            lastOutput = "";

            // C. Logika Perintah (Command Processing)
            if (input.equals("exit")) {
                isRunning = false;
                System.out.println("Keluar dari game...");
            }

            // --- FITUR 1: PERGERAKAN (GO) ---
            else if (input.startsWith("go ")) {
                String direction = input.substring(3); // Ambil kata setelah "go "
                Room nextRoom = currentRoom.getExit(direction);

                if (nextRoom != null) {
                    currentRoom = nextRoom;
                    lastOutput = "Kamu berjalan ke arah " + direction + ".";
                } else {
                    lastOutput = "Dug! Tidak ada jalan ke arah " + direction + ".";
                }
            }

            // --- FITUR 2: MENGAMBIL ITEM (AMBIL) ---
            else if (input.startsWith("ambil ")) {
                String itemName = input.substring(6);

                // Cek apakah item ada di ruangan
                Item itemTaken = currentRoom.removeItem(itemName);

                if (itemTaken != null) {
                    // Coba masukkan ke tas (Cek kapasitas)
                    if (tas.getItems().size() < 5) { // Asumsi max 5
                        tas.addItem(itemTaken);
                        lastOutput = "Kamu mengambil [" + itemTaken.getName() + "] dan memasukkannya ke tas.";
                    } else {
                        // Tas penuh, kembalikan item ke ruangan
                        currentRoom.addItem(itemTaken);
                        lastOutput = "Tas penuh! Tidak bisa mengambil " + itemTaken.getName();
                    }
                } else {
                    lastOutput = "Tidak ada benda bernama '" + itemName + "' di sini.";
                }
            }

            // --- FITUR 3: INSPEKSI ITEM (CEK) ---
            else if (input.startsWith("cek ")) {
                String itemName = input.substring(4);
                boolean found = false;

                // Cek di Tas dulu
                for (Item i : tas.getItems()) {
                    if (i.getName().equalsIgnoreCase(itemName)) {
                        lastOutput = "Info [" + i.getName() + "]: " + i.getDescription();
                        found = true;
                        break;
                    }
                }
                // Kalau gak ada di tas, cek di ruangan (tanpa mengambil)
                if (!found) {
                    // Kita butuh method helper di Room untuk ini,
                    // tapi sementara kita anggap user harus ambil dulu untuk cek detail
                    lastOutput = "Barang harus ada di tas untuk diperiksa (atau barang tidak ditemukan).";
                }
            }

            // --- FITUR BANTUAN ---
            else if (input.equals("help")) {
                lastOutput = "Perintah: go [arah], ambil [nama_item], cek [nama_item], exit";
            }

            // --- PERINTAH TIDAK DIKENAL ---
            else {
                lastOutput = "Maaf, saya tidak mengerti perintah '" + input + "'.";
            }
        }
    }
}