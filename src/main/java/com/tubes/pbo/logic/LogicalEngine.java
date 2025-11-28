package com.tubes.pbo.logic;

import com.tubes.pbo.models.Item;
import com.tubes.pbo.models.Utilities;
import com.tubes.pbo.patterns.singleton.Inventory;
import com.tubes.pbo.ui.ConsoleUI;
import com.tubes.pbo.world.Room;

import java.util.Scanner;

public class LogicalEngine {
    // Game State Data
    private Room currentRoom;
    private Inventory tas;
    private boolean isRunning;
    private String lastOutput;
    private Scanner scanner;

    // CONSTRUCTOR BARU: Menerima startRoom dari Main
    public LogicalEngine(Room startRoom) {
        this.currentRoom = startRoom;
        this.tas = Inventory.getInstance();
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        this.lastOutput = "Bangun... Kamu harus keluar dari rumah ini.";
    }

    public void start() {
        while (isRunning) {
            // Render UI
            ConsoleUI.render(currentRoom, tas, lastOutput);

            // Read Input
            String input = scanner.nextLine().toLowerCase().trim();

            // Process Logic
            processInput(input);
        }
    }

    private void processInput(String input) {
        lastOutput = ""; // Reset feedback

        if (input.equals("exit")) {
            isRunning = false;
            System.out.println("Keluar dari game...");
        }

        // --- GO LOGIC ---
        else if (input.startsWith("go ")) {
            String direction = input.substring(3).trim(); // Tambah trim() biar aman
            if (!direction.equals("left") && !direction.equals("right")) {
                lastOutput = "Gunakan 'go left' atau 'go right'.";
            } else {
                Room nextRoom = currentRoom.getExit(direction);
                if (nextRoom != null) {
                    currentRoom = nextRoom;
                    lastOutput = "Berjalan ke " + direction + "...";
                } else {
                    lastOutput = "Buntu.";
                }
            }
        }

// --- BUKA LOGIC (REVISI: ITEM TETAP DI DALAM) ---
        else if (input.startsWith("buka ")) {
            String[] parts = input.split(" ");
            if (parts.length < 2) {
                lastOutput = "Format: 'buka [benda]'";
            } else {
                String utilityName = parts[1];
                String codeOrKey = (parts.length > 2) ? parts[2] : "";

                com.tubes.pbo.models.Utilities util = currentRoom.getUtility(utilityName);

                if (util != null) {
                    // Cek status lock sebelum mencoba buka
                    boolean wasLocked = util.isLocked();

                    // Logic kunci (KeyUtility) - Sama seperti sebelumnya
                    if (util instanceof com.tubes.pbo.models.KeyUtility) {
                        if (codeOrKey.isEmpty()) {
                            lastOutput = "Terkunci! Gunakan: buka " + utilityName + " [nama_kunci]";
                            return;
                        }
                        boolean hasItem = false;
                        for (Item i : tas.getItems()) {
                            if (i.getName().equalsIgnoreCase(codeOrKey)) {
                                hasItem = true;
                                break;
                            }
                        }
                        if (!hasItem) {
                            lastOutput = "Kamu tidak punya barang bernama '" + codeOrKey + "'!";
                            return;
                        }
                    }

                    // Lakukan Solve
                    lastOutput = util.solve(codeOrKey);

                    // Logic Hapus Kunci setelah dipakai
                    if (wasLocked && !util.isLocked() && util instanceof com.tubes.pbo.models.KeyUtility) {
                        tas.removeItem(codeOrKey);
                        lastOutput += "\n(Item [" + codeOrKey + "] telah digunakan dan dibuang)";
                    }

                    // CEK ENDING (Pintu Keluar)
                    if (util instanceof com.tubes.pbo.models.ExitDoor && !util.isLocked()) {
                        ConsoleUI.render(currentRoom, tas, lastOutput);
                        System.out.println("\nPress Enter to exit...");
                        scanner.nextLine();
                        isRunning = false;
                        return;
                    }

                    // --- PERUBAHAN DI SINI ---
                    // Item TIDAK dijatuhkan ke ruangan (currentRoom.addItem dihapus).
                    // Cukup beri info ke pemain.
                    // TAMBAHKAN SYARAT: !util.isLocked()
                    // Artinya: Hanya tampilkan isi jika benda TIDAK TERKUNCI (Berhasil dibuka)
                    if (!util.isLocked() && util.peekItem() != null) {
                        lastOutput += "\nKamu melihat [" + util.peekItem().getName() + "] di dalamnya.";
                    }

                } else {
                    lastOutput = "Tidak ada benda bernama '" + utilityName + "' di sini.";
                }
            }
        }

        // --- AMBIL LOGIC (REVISI: BISA AMBIL DARI CONTAINER) ---
        else if (input.startsWith("ambil ")) {
            String itemName = input.substring(6).trim();
            Item itemTaken = null;

            // 1. Cek di Lantai dulu
            itemTaken = currentRoom.removeItem(itemName);

            // 2. Jika tidak ada di lantai, Cek di Container/Utilities yang TERBUKA
            if (itemTaken == null) {
                for (com.tubes.pbo.models.Utilities u : currentRoom.getUtilities()) {
                    // UBAH DI SINI: Syaratnya harus u.isOpen(), bukan !u.isLocked()
                    // Jadi kalau statusnya UNLOCKED (seperti Jaket baru ketemu), tetap gak bisa diambil isinya.
                    if (u.isOpen() && u.peekItem() != null && u.peekItem().getName().equalsIgnoreCase(itemName)) {
                        itemTaken = u.lootItem();
                        break;
                    }
                }
            }

            // Proses Masuk Tas
            if (itemTaken != null) {
                if (tas.getItems().size() < 5) {
                    tas.addItem(itemTaken);
                    lastOutput = "Kamu mengambil [" + itemTaken.getName() + "].";
                } else {
                    // Jika tas penuh, kembalikan item ke tempat asalnya (Lantai)
                    // Note: Agak ribet balikin ke container, jadi defaultnya jatuh ke lantai aja
                    currentRoom.addItem(itemTaken);
                    lastOutput = "Tas penuh! Item terjatuh ke lantai.";
                }
            } else {
                lastOutput = "Tidak ada benda bernama '" + itemName + "' yang bisa diambil (Coba buka dulu tempatnya?).";
            }
        }

        // --- AMBIL LOGIC ---
        else if (input.startsWith("ambil ")) {
            String itemName = input.substring(6).trim(); // Tambah trim()
            Item itemTaken = currentRoom.removeItem(itemName);

            if (itemTaken != null) {
                if (tas.getItems().size() < 5) {
                    tas.addItem(itemTaken);
                    lastOutput = "Kamu mengambil [" + itemTaken.getName() + "].";
                } else {
                    currentRoom.addItem(itemTaken);
                    lastOutput = "Tas penuh!";
                }
            } else {
                lastOutput = "Tidak ada '" + itemName + "' di sini.";
            }
        }

        // --- CEK LOGIC (DIPERBAIKI) ---
        else if (input.startsWith("cek")) { // Cek startsWith tanpa spasi dulu
            // Handle jika user cuma ketik "cek" doang
            if (input.length() <= 3) {
                lastOutput = "Mau cek apa? Ketik 'cek [nama_item]'.";
            } else {
                String itemName = input.substring(3).trim(); // Ambil nama & buang spasi
                boolean found = false;

                // Cari di tas
                for (Item i : tas.getItems()) {
                    if (i.getName().equalsIgnoreCase(itemName)) {
                        lastOutput = "Info [" + i.getName() + "]: " + i.getDescription();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    lastOutput = "Barang '" + itemName + "' tidak ada di TAS kamu.";
                }
            }
        }

        // --- HELP LOGIC ---
        else if (input.equals("help")) {
            lastOutput = "Perintah: go [left/right], ambil [item], cek [item], buka [benda], exit";
        } else {
            lastOutput = "Perintah tidak dikenal.";
        }
    }
}