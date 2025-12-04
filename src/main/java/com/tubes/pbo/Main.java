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
            // --- LEVEL 2 ---
            System.out.println("\n\n=== LEVEL 1 CLEARED! ===");
            System.out.println("Kamu berhasil keluar rumah, tapi terjatuh ke lubang...");
            System.out.println("Memulai Level 2...");
            try { Thread.sleep(2000); } catch (Exception e) {} // Jeda biar dramatis

            Room level2 = builder.buildLevel2();
            // Buat engine baru untuk Level 2 (Inventory tetap sama karena Singleton)
            game = new LogicalEngine(level2);
            game.start();

            if (game.isWon()) {
                System.out.println("\n\n=== CONGRATULATIONS! ALL LEVELS COMPLETED ===");
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