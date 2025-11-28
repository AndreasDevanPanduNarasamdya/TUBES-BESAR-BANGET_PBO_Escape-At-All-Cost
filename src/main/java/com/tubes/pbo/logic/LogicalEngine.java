package com.tubes.pbo.logic;

import com.tubes.pbo.patterns.command.*; // Import your commands
import com.tubes.pbo.patterns.singleton.Inventory;
import com.tubes.pbo.ui.ConsoleUI;
import com.tubes.pbo.world.Room;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LogicalEngine {
    private Room currentRoom;
    private Inventory tas;
    private boolean isRunning;
    private String lastOutput;
    private Scanner scanner;

    // NEW: Map to store commands
    private Map<String, Command> commandMap;

    public LogicalEngine(Room startRoom) {
        this.currentRoom = startRoom;
        this.tas = Inventory.getInstance();
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        this.lastOutput = "Bangun... Kamu harus keluar dari rumah ini.";

        // Initialize Commands
        initCommands();
    }

    private void initCommands() {
        commandMap = new HashMap<>();
        commandMap.put("go", new GoCommand());
        commandMap.put("ambil", new TakeCommand());
        commandMap.put("buang", new DropCommand());
        commandMap.put("buka", new OpenCommand());
        commandMap.put("cek", new CheckCommand());
        commandMap.put("help", new HelpCommand());
        commandMap.put("exit", new ExitCommand());
    }

    public void start() {
        while (isRunning) {
            ConsoleUI.render(currentRoom, tas, lastOutput);
            String input = scanner.nextLine().toLowerCase().trim();
            processInput(input);
        }
    }

    private void processInput(String input) {
        if (input.isEmpty()) return;

        String[] parts = input.split(" ");
        String commandWord = parts[0];

        if (commandMap.containsKey(commandWord)) {
            // Execute the command via interface
            commandMap.get(commandWord).execute(parts, this);
        } else {
            lastOutput = "Maaf, saya tidak mengerti perintah '" + commandWord + "'.";
        }
    }

    // --- GETTERS & SETTERS (Required for Commands to work) ---
    public Room getCurrentRoom() { return currentRoom; }
    public void setCurrentRoom(Room room) { this.currentRoom = room; }

    public Inventory getTas() { return tas; }

    public void setLastOutput(String text) { this.lastOutput = text; }
    public void appendOutput(String text) { this.lastOutput += text; } // Helper for appending

    public void stopGame() { this.isRunning = false; }
}