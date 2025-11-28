package com.tubes.pbo.patterns.command;

import com.tubes.pbo.logic.LogicalEngine;
import com.tubes.pbo.models.Item;

public class CheckCommand implements Command {
    @Override
    public void execute(String[] args, LogicalEngine engine) {
        // Validation: Did the user type just "cek"?
        if (args.length < 2) {
            engine.setLastOutput("Mau cek apa? Ketik 'cek [nama_item]'.");
            return;
        }

        String itemName = args[1]; // Ideally handle multi-word names if needed
        boolean found = false;

        // Loop through inventory to find the item
        for (Item i : engine.getTas().getItems()) {
            if (i.getName().equalsIgnoreCase(itemName)) {
                engine.setLastOutput("Info [" + i.getName() + "]: " + i.getDescription());
                found = true;
                break;
            }
        }

        if (!found) {
            engine.setLastOutput("Barang '" + itemName + "' tidak ada di TAS kamu.");
        }
    }
}