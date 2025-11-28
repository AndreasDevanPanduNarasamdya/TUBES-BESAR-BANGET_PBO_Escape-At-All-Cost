package com.tubes.pbo.patterns.command;

import com.tubes.pbo.logic.LogicalEngine;
import com.tubes.pbo.models.Item;

public class DropCommand implements Command {
    @Override
    public void execute(String[] args, LogicalEngine engine) {
        if (args.length < 2) {
            engine.setLastOutput("Buang apa?");
            return;
        }

        String itemName = args[1];
        Item itemToDrop = null;

        for (Item i : engine.getTas().getItems()) {
            if (i.getName().equalsIgnoreCase(itemName)) {
                itemToDrop = i;
                break;
            }
        }

        if (itemToDrop != null) {
            engine.getTas().removeItem(itemName);
            engine.getCurrentRoom().addItem(itemToDrop);
            engine.setLastOutput("Kamu membuang [" + itemToDrop.getName() + "] ke lantai.");
        } else {
            engine.setLastOutput("Kamu tidak punya barang itu.");
        }
    }
}