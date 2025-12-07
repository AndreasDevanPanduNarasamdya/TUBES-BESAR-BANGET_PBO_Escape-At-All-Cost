package com.tubes.pbo;
import com.tubes.pbo.logic.LogicalEngine;
import com.tubes.pbo.world.Room;
import com.tubes.pbo.world.WorldBuilder;
import com.tubes.pbo.ui.ConsoleUI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Lakukan Setup Dunia menggunakan Builder
        Scanner scanner = new Scanner(System.in);
        boolean inMenu = true;

        while (inMenu) {
                // 1. Tampilkan Menu
                ConsoleUI.printTitleScreen();
                String input = scanner.nextLine().trim();

                if (input.equals("1") || input.equalsIgnoreCase("start")) {
                    startCampaign(); // Masuk ke Mode Cerita
                } else if (input.equals("2") || input.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye...");
                    inMenu = false;
                } else {
                    // Input salah, loop lagi
                }
            }
        }

    private static void startCampaign() {
        WorldBuilder builder = new WorldBuilder();

        // --- LEVEL 1 ---
        System.out.println("\nloading Level 1...");
        Room level1 = builder.buildWorld();
        LogicalEngine game = new LogicalEngine(level1);
        game.start(); // Mainkan Level 1 sampai selesai

        // Cek apakah stop karena MENANG atau EXIT manual?
        if (game.isWon()) {
// --- LEVEL 2 START ---
            ConsoleUI.clearScreen();
            System.out.println("\n\n=== LEVEL 1 CLEARED! ===");
            System.out.println("Kamu membuka pintu keluar dan berlari sekuat tenaga...");
            System.out.println("Tiba-tiba... BUGH! Seseorang memukul kepalamu dari belakang.");
            System.out.println("Pandanganmu gelap.");

            try { Thread.sleep(4000); } catch (Exception e) {}

            System.out.println("\n... Kamu terbangun di tempat yang dingin ...");
            System.out.println("... LEVEL 2: THE DUNGEON ...");
            try { Thread.sleep(2000); } catch (Exception e) {}

            // Initialize Level 2
            Room level2 = builder.buildLevel2();

            // Re-initialize engine with new room (Inventory keeps items from Level 1,
            // if you want to clear inventory, call Inventory.getInstance().clear() if you implement it)
            game = new LogicalEngine(level2);
            game.start();

            if (game.isWon()) {
                ConsoleUI.printWinScreen();
                System.out.println("Tekan Enter untuk kembali ke menu...");
                new Scanner(System.in).nextLine();
            }
        }
    }
    private static void startGame() {
        WorldBuilder builder = new WorldBuilder();
        Room startRoom = builder.buildWorld();

        // 2. Oper data ruangan awal ke Engine (Dependency Injection)
        LogicalEngine game = new LogicalEngine(startRoom);

        // 3. Mulai Game
        game.start();
    }
}