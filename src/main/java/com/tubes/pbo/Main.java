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
                    // 2. Mulai Game (Logic Lama Pindah ke Sini)
                    startGame();
                    // Setelah game over/tamat, loop akan kembali ke menu (optional)
                    // Jika ingin langsung keluar setelah tamat, set inMenu = false;
                } else if (input.equals("2") || input.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye...");
                    inMenu = false;
                } else {
                    // Input salah, loop lagi
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