package com.tubes.pbo.patterns.command;

import com.tubes.pbo.logic.LogicalEngine;
import com.tubes.pbo.models.Item;
import com.tubes.pbo.models.Utilities;

public class TakeCommand implements Command {
    @Override
    public void execute(String[] args, LogicalEngine engine) {
        if (args.length < 2) {
            engine.setLastOutput("Ambil apa?");
            return;
        }

        // Handle names with spaces if needed, but for now take args[1]
        // Ideally, join the rest of args: String itemName = String.join(" ", ...);
        String itemName = args[1]; // Simplified

        // 1. Check Floor
        Item itemTaken = engine.getCurrentRoom().removeItem(itemName);

        // 2. Check Open Containers
        if (itemTaken == null) {
            for (Utilities u : engine.getCurrentRoom().getUtilities()) {
                if (u.isOpen() && u.peekItem() != null && u.peekItem().getName().equalsIgnoreCase(itemName)) {
                    itemTaken = u.lootItem();
                    break;
                }
            }
        }

        if (itemTaken != null) {
            if (engine.getTas().getItems().size() < 5) {
                engine.getTas().addItem(itemTaken);
                engine.setLastOutput("Kamu mengambil [" + itemTaken.getName() + "].");
            } else {
                engine.getCurrentRoom().addItem(itemTaken);
                engine.setLastOutput("Tas penuh! Item terjatuh kembali.");
            }
        } else {
            engine.setLastOutput("Tidak ada '" + itemName + "' yang bisa diambil.");
        }
    }
}