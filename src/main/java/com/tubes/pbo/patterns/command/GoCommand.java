package com.tubes.pbo.patterns.command;

import com.tubes.pbo.logic.LogicalEngine;
import com.tubes.pbo.world.Room;

public class GoCommand implements Command {
    @Override
    public void execute(String[] args, LogicalEngine engine) {
        if (args.length < 2) {
            engine.setLastOutput("Gunakan 'go left' atau 'go right'.");
            return;
        }

        String direction = args[1];
        if (!direction.equals("left") && !direction.equals("right")) {
            engine.setLastOutput("Hanya bisa 'go left' atau 'go right'.");
            return;
        }

        Room nextRoom = engine.getCurrentRoom().getExit(direction);
        if (nextRoom != null) {
            engine.setCurrentRoom(nextRoom);
            engine.setLastOutput("Kamu berjalan ke " + direction + "...");
        } else {
            engine.setLastOutput("Dug! Tembok buntu.");
        }
    }
}