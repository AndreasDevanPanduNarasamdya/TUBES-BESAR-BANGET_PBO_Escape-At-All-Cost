package com.tubes.pbo.logic;

import com.tubes.pbo.models.Item;
import com.tubes.pbo.models.PasswordUtility;
import com.tubes.pbo.models.Utilities; // Make sure this matches your Utility class name
import com.tubes.pbo.patterns.singleton.Inventory;
import com.tubes.pbo.frontend.ConsoleUI;
import com.tubes.pbo.world.Room;

import java.util.Scanner;

public class LogicalEngine {
    // Game State Data
    private Room currentRoom;
    private Inventory tas;
    private boolean isRunning;
    private String lastOutput;
    private Scanner scanner;

    public LogicalEngine() {
        this.tas = Inventory.getInstance();
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        this.lastOutput = "Selamat datang! Ketik 'help' untuk bantuan.";

        // Setup the world immediately upon creation
        setupWorld();
    }

    private void setupWorld() {
        // --- 1. SETUP AREA (Moved from Main) ---
        Room livingRoom = new Room("Ruang Tamu", "Ruangan gelap dengan sofa berdebu.");
        Room kitchen = new Room("Dapur", "Bau amis tercium kuat. Ada lalat beterbangan.");
        Room bedroom = new Room("Kamar Tidur", "Kasur berantakan. Jendela tertutup rapat.");

        Item pedangNaga = new Item("Pedang Naga", "Pedang legendaris!");

        // Connect Rooms
        livingRoom.setExit("north", kitchen);
        kitchen.setExit("south", livingRoom);
        livingRoom.setExit("east", bedroom);
        bedroom.setExit("west", livingRoom);

        // Add Items
        livingRoom.addItem(new Item("Senter", "Senter kecil, baterainya masih ada."));
        kitchen.addItem(new Item("Pisau", "Pisau daging yang sangat tumpul."));
        bedroom.addItem(new Item("Kunci", "Kunci perak kecil. Mungkin untuk pintu depan?"));
        bedroom.addItem(new Item("Catatan", "Kertas lusuh bertuliskan: 'JANGAN KELUAR MALAM INI'"));

        // Add Utilities
        PasswordUtility brankas = new PasswordUtility("Brankas Besi", "Butuh 4 digit PIN", pedangNaga, "1234");
        livingRoom.addUtility(brankas);

        // Set starting room
        this.currentRoom = livingRoom;
    }

    public void start() {
        // --- 2. GAME LOOP (Moved from Main) ---
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
            String direction = input.substring(3);
            Room nextRoom = currentRoom.getExit(direction);

            if (nextRoom != null) {
                currentRoom = nextRoom;
                lastOutput = "Kamu berjalan ke arah " + direction + ".";
            } else {
                lastOutput = "Dug! Tidak ada jalan ke arah " + direction + ".";
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

                // Note: Make sure Room.java has getUtility() method
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
                    lastOutput = "Kamu mengambil [" + itemTaken.getName() + "] dan memasukkannya ke tas.";
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
            lastOutput = "Perintah: go [arah], ambil [nama_item], cek [nama_item], buka [benda] [kode], exit";
        } else {
            lastOutput = "Maaf, saya tidak mengerti perintah '" + input + "'.";
        }
    }
}