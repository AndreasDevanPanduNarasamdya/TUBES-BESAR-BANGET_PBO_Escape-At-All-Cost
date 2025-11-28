package com.tubes.pbo.patterns.command;

import com.tubes.pbo.logic.LogicalEngine;

public class ExitCommand implements Command {
    @Override
    public void execute(String[] args, LogicalEngine engine) {
        engine.setLastOutput("Keluar dari game...");
        engine.stopGame(); // Stops the while(isRunning) loop in LogicalEngine
    }
}