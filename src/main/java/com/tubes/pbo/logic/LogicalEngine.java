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

        // --- GO LOGIC (SIDE SCROLLING) ---
        else if (input.startsWith("go ")) {
            String direction = input.substring(3);

            // Validasi arah hanya left/right agar sesuai UI
            if (!direction.equals("left") && !direction.equals("right")) {
                lastOutput = "Game ini side-scrolling. Gunakan 'go left' atau 'go right'.";
            } else {
                Room nextRoom = currentRoom.getExit(direction);
                if (nextRoom != null) {
                    currentRoom = nextRoom;
                    lastOutput = "Kamu berjalan ke " + direction + "...";
                } else {
                    lastOutput = "Dug! Tembok buntu. Tidak ada jalan ke " + direction + ".";
                }
            }
        }

        // --- BUKA/SOLVE LOGIC ---
        else if (input.startsWith("buka ")) {
            String[] parts = input.split(" ", 3);
            if (parts.length < 3) {
                lastOutput = "Format salah! Gunakan: buka [nama_benda] [password/kunci]";
            } else {
                String utilityName = parts[1];
                String codeOrKey = parts[2];

                Utilities util = currentRoom.getUtility(utilityName);

                if (util != null) {
                    lastOutput = util.solve(codeOrKey);
                    Item loot = util.lootItem();
                    if (loot != null) {
                        tas.addItem(loot);
                        lastOutput += "\nKamu mendapatkan: " + loot.getName();
                    }
                } else {
                    lastOutput = "Tidak ada benda bernama " + utilityName + " di sini.";
                }
            }
        }

        // --- AMBIL LOGIC ---
        else if (input.startsWith("ambil ")) {
            String itemName = input.substring(6);
            Item itemTaken = currentRoom.removeItem(itemName);

            if (itemTaken != null) {
                if (tas.getItems().size() < 5) {
                    tas.addItem(itemTaken);
                    lastOutput = "Kamu mengambil [" + itemTaken.getName() + "].";
                } else {
                    currentRoom.addItem(itemTaken);
                    lastOutput = "Tas penuh! Tidak bisa mengambil " + itemTaken.getName();
                }
            } else {
                lastOutput = "Tidak ada benda bernama '" + itemName + "' di sini.";
            }
        }

        // --- CEK LOGIC ---
        else if (input.startsWith("cek ")) {
            String itemName = input.substring(4);
            boolean found = false;

            for (Item i : tas.getItems()) {
                if (i.getName().equalsIgnoreCase(itemName)) {
                    lastOutput = "Info [" + i.getName() + "]: " + i.getDescription();
                    found = true;
                    break;
                }
            }
            if (!found) {
                lastOutput = "Barang harus ada di tas untuk diperiksa.";
            }
        }

        // --- HELP LOGIC ---
        else if (input.equals("help")) {
            lastOutput = "Perintah: go [left/right], ambil [item], cek [item], buka [benda] [kode], exit";
        } else {
            lastOutput = "Maaf, saya tidak mengerti perintah '" + input + "'.";
        }
    }
}