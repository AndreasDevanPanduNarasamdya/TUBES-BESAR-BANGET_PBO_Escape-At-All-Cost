package com.tubes.pbo.patterns.command;

import com.tubes.pbo.logic.LogicalEngine;

public class HelpCommand implements Command {
    @Override
    public void execute(String[] args, LogicalEngine engine) {
        // Simply sets the output to the help string
        engine.setLastOutput("Perintah: go [left/right], ambil [item], buang [item], cek [item], buka [benda], exit");
    }
}