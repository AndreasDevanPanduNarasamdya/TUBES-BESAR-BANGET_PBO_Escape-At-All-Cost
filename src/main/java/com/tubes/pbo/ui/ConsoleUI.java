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
            System.out.println("\n");
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

    // ==========================================
    // MAIN RENDER (REVISI TOTAL)
    // ==========================================
    public static void render(Room currentRoom, Inventory inventory, String lastMessage) {
        clearScreen();
        printBorderTop();

        // 1. COMMAND LIST (DIKEMBALIKAN KE PALING ATAS)
        System.out.printf("║ BANTUAN: %-65s ║\n", " ");
        System.out.printf("║ [GO <left/right>] [AMBIL <item>] [CEK <item>] [BUKA <benda> <kunci>]   ║\n");
        System.out.printf("║ [TAS]             [HELP]         [EXIT]   [HANCURKAN <benda> <alat>]   ║\n");
        printLine();

        // 2. HEADER INFO (HAPUS TIMER/MOOD -> GANTI JADI FULL INVENTORY LIST)
        System.out.printf("║ LOKASI: %-66s ║\n", currentRoom.getName().toUpperCase());
        System.out.printf("║ TAS: %-69s ║\n", getInventoryString()); // <--- List Item Lengkap Disini
        printLine();

        // 3. ROOM SCENE (GAMBAR ASCII)
        drawRoomScene(currentRoom);

        // 4. NAVIGATION HELPER (Penunjuk Arah Bawah Gambar)
        drawNavigationHelper(currentRoom);

        // 5. MESSAGE BOX / SUBTITLE
        printLine();
        System.out.printf("║ %-76s ║\n", " ");
        if (!lastMessage.isEmpty()) {
            System.out.printf("║ > %-74s ║\n", lastMessage.toUpperCase());
        } else {
            // Deskripsi singkat ruangan
            String desc = currentRoom.getDescription().split("\n")[0];
            if(desc.length() > 74) desc = desc.substring(0, 74);
            System.out.printf("║ INFO: %-68s ║\n", desc);
        }
        System.out.printf("║ %-76s ║\n", " ");
        printBorderBottom();

        // 6. INPUT PROMPT
        System.out.print("   COMMAND > ");
    }

    // ==========================================
    // HELPERS
    // ==========================================

    // Helper untuk menampilkan semua nama item di tas (BUKAN CUMA JUMLAH)
    private static String getInventoryString() {
        List<Item> items = Inventory.getInstance().getItems();
        if (items.isEmpty()) return "(KOSONG)";

        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append("[").append(item.getName()).append("] ");
        }
        return sb.toString();
    }

    private static void drawNavigationHelper(Room room) {
        Room leftRoom = room.getExit("left");
        Room rightRoom = room.getExit("right");

        String leftText = (leftRoom != null) ? "< KE " + leftRoom.getName().toUpperCase() : "| BUNTU |";
        String rightText = (rightRoom != null) ? "KE " + rightRoom.getName().toUpperCase() + " >" : "| BUNTU |";

        // Menggunakan karakter border agar menyatu
        System.out.printf("║  %-35s      %35s  ║\n", leftText, rightText);
    }

    // ==========================================
    // SCENE GENERATOR (ASCII ART TETAP ADA)
    // ==========================================
    private static void drawRoomScene(Room room) {
        String name = room.getName().toLowerCase();
        String[] art = getEmptyRoomArt();

        if (name.contains("kamar") || name.contains("bedroom")) {
            art = getBedroomArt();
        } else if (name.contains("tamu") || name.contains("living")) {
            art = getLivingRoomArt();
        } else if (name.contains("dapur") || name.contains("kitchen")) {
            art = getKitchenArt();
        }
        else if (name.contains("gudang") || name.contains("storage")) { // TAMBAHAN
            art = getStorageArt();
        }
        // Level 2 Checks (NEW)
        else if (name.contains("cell") || name.contains("dungeon")) art = getDungeonCellArt();
        else if (name.contains("lorong") || name.contains("hallway")) art = getDungeonHallArt();
        else if (name.contains("guard") || name.contains("jaga")) art = getGuardRoomArt();
        else if (name.contains("sipir") || name.contains("office")) art = getWardenOfficeArt();
        for (String line : art) {
            System.out.printf("║ %-76s ║\n", line);
        }
    }

    public static void printTitleScreen() {
        clearScreen();
        printBorderTop();
        System.out.printf("║ %-76s ║\n", " ");
        System.out.printf("║   ███████╗███████╗ ██████╗ █████╗ ██████╗ ███████╗                        ║\n");
        System.out.printf("║   ██╔════╝██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝                        ║\n");
        System.out.printf("║   █████╗  ███████╗██║     ███████║██████╔╝█████╗                          ║\n");
        System.out.printf("║   ██╔══╝  ╚════██║██║     ██╔══██║██╔═══╝ ██╔══╝                          ║\n");
        System.out.printf("║   ███████╗███████║╚██████╗██║  ██║██║     ███████╗                        ║\n");
        System.out.printf("║   ╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝     ╚══════╝                        ║\n");
        System.out.printf("║ %-76s ║\n", " ");
        System.out.printf("║        █████╗ ████████╗     █████╗ ██╗     ██╗                            ║\n");
        System.out.printf("║       ██╔══██╗╚══██╔══╝    ██╔══██╗██║     ██║                            ║\n");
        System.out.printf("║       ███████║   ██║       ███████║██║     ██║                            ║\n");
        System.out.printf("║       ██╔══██║   ██║       ██╔══██║██║     ██║                            ║\n");
        System.out.printf("║       ██║  ██║   ██║       ██║  ██║███████╗███████╗                       ║\n");
        System.out.printf("║       ╚═╝  ╚═╝   ╚═╝       ╚═╝  ╚═╝╚══════╝╚══════╝                       ║\n");
        System.out.printf("║ %-76s ║\n", " ");
        System.out.printf("║     ██████╗ ██████╗ ███████╗████████╗                                     ║\n");
        System.out.printf("║    ██╔════╝██╔═══██╗██╔════╝╚══██╔══╝                                     ║\n");
        System.out.printf("║    ██║     ██║   ██║███████╗   ██║                                        ║\n");
        System.out.printf("║    ██║     ██║   ██║╚════██║   ██║                                        ║\n");
        System.out.printf("║    ╚██████╗╚██████╔╝███████║   ██║                                        ║\n");
        System.out.printf("║     ╚═════╝ ╚═════╝ ╚══════╝   ╚═╝                                        ║\n");
        System.out.printf("║ %-76s ║\n", " ");
        printLine();
        System.out.printf("║ %-76s ║\n", " ");
        System.out.printf("║                  「 A Text-Based Horror Adventure 」                       ║\n");
        System.out.printf("║ %-76s ║\n", " ");
        printLine();
        System.out.printf("║ %-76s ║\n", " ");
        System.out.printf("║           ┌──────────────────────────────────────────┐                     ║\n");
        System.out.printf("║           │  [1] ► START GAME                        │                     ║\n");
        System.out.printf("║           │                                          │                     ║\n");
        System.out.printf("║           │  [2] ► EXIT                              │                     ║\n");
        System.out.printf("║           └──────────────────────────────────────────┘                     ║\n");
        System.out.printf("║                                                                            ║\n");
        System.out.printf("║ %-76s ║\n", " ");
        printBorderBottom();
        System.out.print("\n   「 MENU 」 ► ");
    }

    // --- ART ASSETS ---
    private static String[] getBedroomArt() {
        return new String[]{
                "||==========================================================================||",
                "||                                                                          ||",
                "||      [ LEMARI ]             [  JENDELA  ]                 ( JAKET )      ||",
                "||      __________             | . . . . . |                    / \\         ||",
                "||     |  ______  |            | . POHON . |                   /   \\        ||",
                "||     | |______| |            | . . . . . |                   |   |        ||",
                "||     | |______| |            |___________|                   |___|        ||",
                "||     | |______| |                                                         ||",
                "||     |__________|                                      [ KASUR KAYU ]     ||",
                "||                                                      (_______________)   ||",
                "||__________________________________________________________________________||",
                "||//////////////////////////////////////////////////////////////////////////||"
        };
    }

    private static String[] getLivingRoomArt() {
        return new String[]{
                "||==========================================================================||",
                "||                                                                          ||",
                "||                        [ LUKISAN ]                                       ||",
                "||                        |_________|                                       ||",
                "||                                                       [ TV RUSAK ]       ||",
                "||       [ SOFA TUA ]                                     |========|        ||",
                "||      /____________\\              ( MEJA )              |  xxxx  |        ||",
                "||     |              |            ________               |________|        ||",
                "||     |______________|           |        |                 |  |           ||",
                "||                                |________|                 |__|           ||",
                "||__________________________________________________________________________||",
                "||//////////////////////////////////////////////////////////////////////////||"
        };
    }

    private static String[] getKitchenArt() {
        return new String[]{
                "||==========================================================================||",
                "||    { PANCI }                                                             ||",
                "||      \\___/               [ KABINET DAPUR ]                               ||",
                "||                          |===============|                               ||",
                "||                          | | | | | | | | |                               ||",
                "||                          |===============|             [  Vas  ]        ||",
                "||                                                        |========|        ||",
                "||      [ KOMPOR ]             [ WASTAFEL ]                |      |         ||",
                "||      | o  o   |             |   ____   |                 | __ |        ||",
                "||      |________|             |__|    |__|                  |__|        ||",
                "||__________________________________________________________________________||",
                "||//////////////////////////////////////////////////////////////////////////||"
        };
    }

    private static String[] getEmptyRoomArt() {
        return new String[]{
                "||==========================================================================||",
                "||                                                                          ||",
                "||                                                                          ||",
                "||                   ( RUANGAN INI GELAP DAN KOSONG )                       ||",
                "||                                                                          ||",
                "||__________________________________________________________________________||",
                "||//////////////////////////////////////////////////////////////////////////||"
        };
    }
    private static String[] getStorageArt() {
        return new String[]{
                "||==========================================================================||",
                "||                                                                          ||",
                "||                      _ __________ _                                      ||",
                "||                     | |          | |                                     ||",
                "||                     | |  PINTU   | |                                     ||",
                "||      ( BRANKAS )    | |  KELUAR  | |                                     ||",
                "||       | [###] |     | |          | |                                     ||",
                "||       |_______|     |_|____o_____|_|                                     ||",
                "||                                                                          ||",
                "||      (GUDANG)                                                            ||",
                "||                                                                          ||",
                "||__________________________________________________________________________||",
                "||//////////////////////////////////////////////////////////////////////////||"
        };
    }

    // --- NEW ART ASSETS FOR LEVEL 2 ---
    private static String[] getDungeonCellArt() {
        return new String[]{
                "||==========================================================================||",
                "||   |||   |||                                                              ||",
                "||   |||   |||           [ CELAH DINDING ]                                  ||",
                "||   |||   |||             (Retak...)                                       ||",
                "||   |||   |||              | / / |                                         ||",
                "||                           |/ /|             ( TAHANAN TUA )              ||",
                "||                                                 O___                     ||",
                "||      [ JERUJI BESI ]                            /|  \\                    ||",
                "||      || || || || ||                            / \\                       ||",
                "||      || || || || ||                                                      ||",
                "||__________________________________________________________________________||",
                "||//////////////////////////////////////////////////////////////////////////||"
        };
    }

    private static String[] getDungeonHallArt() {
        return new String[]{
                "||==========================================================================||",
                "||                                                                          ||",
                "||      ( OBOR )                                              ( OBOR )      ||",
                "||        ) (                                                   ) (         ||",
                "||       |___|               _  _  _  _  _  _                  |___|        ||",
                "||        | |               | || || || || || |                  | |         ||",
                "||        | |               |    GELAP...    |                  | |         ||",
                "||                          |_||_||_||_||_||_|                              ||",
                "||                                                                          ||",
                "||__________________________________________________________________________||",
                "||//////////////////////////////////////////////////////////////////////////||"
        };
    }

    private static String[] getGuardRoomArt() {
        return new String[]{
                "||==========================================================================||",
                "||                                                  [ LOKER BESI ]          ||",
                "||         [ MEJA ]                                  |==========|           ||",
                "||        ________                                   |  [O]     |           ||",
                "||       /   ()   \\                                  |          |           ||",
                "||      |  KARTU   |                                 |          |           ||",
                "||      |__________|                                 |__________|           ||",
                "||        |      |                                                          ||",
                "||       _|      |_                                                         ||",
                "||__________________________________________________________________________||",
                "||//////////////////////////////////////////////////////////////////////////||"
        };
    }

    private static String[] getWardenOfficeArt() {
        return new String[]{
                "||==========================================================================||",
                "||                                                                          ||",
                "||    [ LUKISAN ]                                     [ GERBANG UTAMA ]     ||",
                "||    | (Brankas)|                                     |=============|      ||",
                "||    |__________|            [ MEJA MEWAH ]           | || || || || |      ||",
                "||                            _____________            | || || || || |      ||",
                "||                           |             |           | || || || || |      ||",
                "||                           |   [DOCS]    |           |=============|      ||",
                "||                           |_____________|                                ||",
                "||                                                                          ||",
                "||__________________________________________________________________________||",
                "||//////////////////////////////////////////////////////////////////////////||"
        };
    }

    public static void printWinScreen() {
        clearScreen();
        printBorderTop();
        System.out.printf("║ %-76s ║\n", " ");
        System.out.printf("║             V I C T O R Y   A C H I E V E D ! ! !                         ║\n");
        System.out.printf("║ %-76s ║\n", " ");
        System.out.printf("║                  ___________                                              ║\n");
        System.out.printf("║                 '._==_==_=_.'                                             ║\n");
        System.out.printf("║                 .-\\:      /-.                                             ║\n");
        System.out.printf("║                | (|:.     |) |                                            ║\n");
        System.out.printf("║                 '-|:.     |-'                                             ║\n");
        System.out.printf("║                   \\::.    /                                               ║\n");
        System.out.printf("║                    '::. .'                                                ║\n");
        System.out.printf("║                      ) (                                                  ║\n");
        System.out.printf("║                    _.' '._                                                ║\n");
        System.out.printf("║                   `\"\"\"\"\"\"\"`                                               ║\n");
        System.out.printf("║ %-76s ║\n", " ");
        printLine();
        System.out.printf("║ %-76s ║\n", " ");
        System.out.printf("║       CONGRATULATIONS! YOU HAVE ESCAPED ALL OBSTACLES.                    ║\n");
        System.out.printf("║           THANK YOU FOR PLAYING 'ESCAPE AT ALL COST'                      ║\n");
        System.out.printf("║ %-76s ║\n", " ");
        printBorderBottom();

        System.out.println("\nPress [ENTER] to return to the main menu...");
    }

}