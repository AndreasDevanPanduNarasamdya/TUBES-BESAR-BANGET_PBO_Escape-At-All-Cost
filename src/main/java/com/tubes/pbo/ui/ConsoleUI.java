package com.tubes.pbo.ui;

import com.tubes.pbo.models.Item;
import com.tubes.pbo.patterns.singleton.Inventory;
import com.tubes.pbo.world.Room;

import java.io.IOException;
import java.util.List;

public class ConsoleUI {
    private static final int WIDTH = 80;

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("\n\n\n\n\n");
        }
    }

    private static void printLine() {
        System.out.print("╠");
        for (int i = 0; i < WIDTH - 2; i++) System.out.print("═");
        System.out.println("╣");
    }

    private static void printBorderTop() {
        System.out.print("╔");
        for (int i = 0; i < WIDTH - 2; i++) System.out.print("═");
        System.out.println("╗");
    }

    private static void printBorderBottom() {
        System.out.print("╚");
        for (int i = 0; i < WIDTH - 2; i++) System.out.print("═");
        System.out.println("╝");
    }

    // Method Render Utama
    public static void render(Room currentRoom, Inventory inventory, String lastMessage) {
        clearScreen();

        printBorderTop();

        // === BAGIAN BARU: LIST COMMAND DI ATAS ===
        // Saya format agar pas di tengah atau rapi di kiri
        System.out.printf("║ %-76s ║\n", "BANTUAN:");
        System.out.printf("║ [GO <arah>]   [AMBIL <benda>]   [CEK <benda>]   [EXIT]                     ║\n");
        System.out.printf("║ [TAS]         [HELP]                                                       ║\n");
        printLine();
        // =========================================

        // 1. HEADER (Lokasi)
        System.out.printf("║ LOKASI: %-50s JAM: 23:00 ║\n", currentRoom.getName().toUpperCase());
        printLine();

        // 2. VISUAL AREA
        System.out.printf("║ %-76s ║\n", " ");
        System.out.printf("║      [UTARA: %-15s]                                         ║\n",
                (currentRoom.getExit("north") != null ? "Ada Jalan" : "Tembok"));
        System.out.printf("║ %-76s ║\n", " ");
        System.out.printf("║ [BARAT]        ( KAMU )        [TIMUR]                              ║\n");
        System.out.printf("║ %-76s ║\n", " ");
        System.out.printf("║      [SELATAN: %-15s]                                       ║\n",
                (currentRoom.getExit("south") != null ? "Ada Jalan" : "Tembok"));
        System.out.printf("║ %-76s ║\n", " ");
        printLine();

        // 3. DESKRIPSI
        System.out.printf("║ %-76s ║\n", "NARASI:");
        // Trik sederhana untuk handling teks panjang (biar tidak merusak border)
        String desc = currentRoom.getDescription();
        if (desc.length() > 74) {
            System.out.printf("║ > %-74s ║\n", desc.substring(0, 74));
            System.out.printf("║   %-74s ║\n", desc.substring(74)); // Baris kedua
        } else {
            System.out.printf("║ > %-74s ║\n", desc);
        }

        System.out.printf("║ %-76s ║\n", " ");

        // 4. RESPON SISTEM
        if (!lastMessage.isEmpty()) {
            System.out.printf("║ SYSTEM: %-74s ║\n", lastMessage);
        } else {
            System.out.printf("║ %-76s ║\n", " ");
        }
        printLine();

        // 5. HUD (Inventory)
        System.out.printf("║ TAS: %-60s       ║\n", getInventoryString());
        printBorderBottom();

        System.out.print("   COMMAND > ");
    }

    private static String getInventoryString() {
        List<Item> items = Inventory.getInstance().getItems();
        if (items.isEmpty()) return "(Kosong)";

        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append("[").append(item.getName()).append("] ");
        }
        return sb.toString();
    }
}