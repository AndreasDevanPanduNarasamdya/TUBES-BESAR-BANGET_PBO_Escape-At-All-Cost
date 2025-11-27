package com.tubes.pbo.frontend;

import com.tubes.pbo.models.Item;
import com.tubes.pbo.patterns.singleton.Inventory;
import com.tubes.pbo.world.Room;

import java.util.List;

public class ConsoleUI {
    private static final int WIDTH = 80;

    // ... method clearScreen() sama seperti sebelumnya ...
    public static void clearScreen() { /* ... kode lama ... */ }

    // ... method printBorder dll sama seperti sebelumnya ...
    private static void printBorderTop() { /* ... */ }
    private static void printBorderBottom() { /* ... */ }
    private static void printLine() { /* ... */ }

    // === RENDER BARU DENGAN MAP 2D ===
    public static void render(Room currentRoom, Inventory inventory, String lastMessage) {
        clearScreen(); // Panggil method clearScreen kamu yang lama

        printBorderTop();

        // 1. VISUAL MAP (2D GROUND VIEW)
        // Kita hardcode urutan map-nya: Kamar -> Ruang Tamu -> Dapur
        String mapVisual = generateMapStrip(currentRoom.getName());
        System.out.printf("║ MAP: %-71s ║\n", mapVisual);
        printLine();

        // 2. VISUAL RUANGAN (SIDE SCROLL VIEW)
        // Menampilkan apa yang ada di kiri dan kanan
        String leftView = (currentRoom.getExit("left") != null) ? "< PINTU KIRI" : "| TEMBOK |";
        String rightView = (currentRoom.getExit("right") != null) ? "PINTU KANAN >" : "| TEMBOK |";

        System.out.printf("║ %-76s ║\n", " ");
        // Layout:  [KIRI]      ( KAMU )      [KANAN]
        System.out.printf("║  %-15s             ( KAMU )             %15s  ║\n", leftView, rightView);
        System.out.printf("║ %-76s ║\n", " ");
        printLine();

        // 3. DESKRIPSI & ITEM
        System.out.printf("║ LOKASI: %-68s ║\n", currentRoom.getName().toUpperCase());

        // Logic print deskripsi (wrap text)
        String desc = currentRoom.getDescription(); // Deskripsi + List Item
        // Kita pecah per baris baru (\n) kalau ada dari Room.java
        String[] lines = desc.split("\n");
        for (String line : lines) {
            // Potong jika terlalu panjang (simple trimming)
            if (line.length() > 74) line = line.substring(0, 74);
            System.out.printf("║ > %-74s ║\n", line);
        }

        System.out.printf("║ %-76s ║\n", " ");

        // 4. FEEDBACK SYSTEM
        if (!lastMessage.isEmpty()) {
            System.out.printf("║ [INFO]: %-68s ║\n", lastMessage);
        } else {
            System.out.printf("║ %-76s ║\n", " ");
        }
        printLine();

        // 5. INVENTORY
        System.out.printf("║ TAS: %-60s       ║\n", getInventoryString());
        printBorderBottom();

        System.out.print("   COMMAND > ");
    }

    // Helper untuk membuat Map Strip
    private static String generateMapStrip(String currentRoomName) {
        // Logika sederhana: Ganti tampilan berdasarkan nama ruangan
        String r1 = "KAMAR";
        String r2 = "R.TAMU";
        String r3 = "DAPUR";

        // Highlight ruangan aktif dengan tanda [* ... *]
        if (currentRoomName.equalsIgnoreCase("Kamar Tidur")) {
            r1 = "[*KAMAR*]";
            r2 = " R.TAMU ";
            r3 = " DAPUR  ";
        } else if (currentRoomName.equalsIgnoreCase("Ruang Tamu")) {
            r1 = " KAMAR  ";
            r2 = "[*R.TAMU*]";
            r3 = " DAPUR  ";
        } else if (currentRoomName.equalsIgnoreCase("Dapur")) {
            r1 = " KAMAR  ";
            r2 = " R.TAMU ";
            r3 = "[*DAPUR*]";
        }

        // Gabungkan jadi strip
        return r1 + " <====> " + r2 + " <====> " + r3;
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