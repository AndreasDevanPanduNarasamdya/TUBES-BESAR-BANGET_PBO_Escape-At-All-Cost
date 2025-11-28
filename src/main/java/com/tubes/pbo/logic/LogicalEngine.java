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

        // --- BUKA LOGIC (UPDATE: HAPUS KUNCI SETELAH DIPAKAI) ---
        else if (input.startsWith("buka ")) {
            String[] parts = input.split(" ");
            if (parts.length < 2) {
                lastOutput = "Format: 'buka [benda]' atau 'buka [benda] [kunci/password]'";
            } else {
                String utilityName = parts[1];
                String codeOrKey = (parts.length > 2) ? parts[2] : "";

                com.tubes.pbo.models.Utilities util = currentRoom.getUtility(utilityName);

                if (util != null) {
                    // Cek status terkunci SEBELUM mencoba membuka
                    boolean wasLocked = util.isLocked();

                    // --- VALIDASI KEY UTILITY ---
                    if (util instanceof com.tubes.pbo.models.KeyUtility) {
                        if (codeOrKey.isEmpty()) {
                            lastOutput = "Terkunci! Gunakan: buka " + utilityName + " [nama_kunci]";
                            return;
                        }

                        // Cek keberadaan item di tas
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
                    // ----------------------------

                    // Coba buka (Solve)
                    lastOutput = util.solve(codeOrKey);

                    // --- LOGIC HAPUS KUNCI ---
                    // Jika tadi terkunci, dan sekarang sudah TIDAK terkunci (berarti sukses dibuka)
                    // DAN benda itu tipe KeyUtility (bukan password)
                    if (wasLocked && !util.isLocked() && util instanceof com.tubes.pbo.models.KeyUtility) {
                        tas.removeItem(codeOrKey); // Hapus kunci dari Inventory
                        lastOutput += "\n(Item [" + codeOrKey + "] telah digunakan dan dibuang)";
                    }

                    // Logic Loot Drop
                    Item loot = util.lootItem();
                    if (loot != null) {
                        currentRoom.addItem(loot);
                        lastOutput += "\n[!] Sebuah " + loot.getName() + " terjatuh keluar!";
                    }
                } else {
                    lastOutput = "Tidak ada benda bernama '" + utilityName + "' di sini.";
                }
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