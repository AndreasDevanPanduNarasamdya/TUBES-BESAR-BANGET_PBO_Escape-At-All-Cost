package com.tubes.pbo.patterns.command;

import com.tubes.pbo.logic.LogicalEngine;
import com.tubes.pbo.models.*;

public class OpenCommand implements Command {
    @Override
    public void execute(String[] args, LogicalEngine engine) {
        if (args.length < 2) {
            engine.setLastOutput("Buka apa?");
            return;
        }

        String utilityName = args[1];
        String codeOrKey = (args.length > 2) ? args[2] : "";

        Utilities util = engine.getCurrentRoom().getUtility(utilityName);

        if (util != null) {
            boolean wasLocked = util.isLocked();

            // Key Validation
            if (util instanceof KeyUtility) {
                if (codeOrKey.isEmpty()) {
                    engine.setLastOutput("Terkunci! Gunakan: buka " + utilityName + " [nama_kunci]");
                    return;
                }
                // Check if player has key
                boolean hasItem = engine.getTas().getItems().stream()
                        .anyMatch(i -> i.getName().equalsIgnoreCase(codeOrKey));

                if (!hasItem) {
                    engine.setLastOutput("Kamu tidak punya barang bernama '" + codeOrKey + "'!");
                    return;
                }
            }

            // Solve
            String result = util.solve(codeOrKey);
            engine.setLastOutput(result);

            // Remove Key if used
            if (wasLocked && !util.isLocked() && util instanceof KeyUtility) {
                engine.getTas().removeItem(codeOrKey);
                engine.appendOutput("\n(Item [" + codeOrKey + "] dibuang)");
            }

            // Win Condition
            if (util instanceof ExitDoor && !util.isLocked()) {
                engine.appendOutput("\n\nTEKAN ENTER UNTUK KELUAR...");
                // In a real CLI, we might wait for input, but here we just stop the engine loop
                // You might need a way to pause, but stopping is enough for logic.
                engine.stopGame();
                return;
            }

            // Peek Item
            if (!util.isLocked() && util.peekItem() != null) {
                engine.appendOutput("\nKamu melihat [" + util.peekItem().getName() + "] di dalamnya.");
            }

        } else {
            engine.setLastOutput("Tidak ada benda bernama '" + utilityName + "' di sini.");
        }
    }
}