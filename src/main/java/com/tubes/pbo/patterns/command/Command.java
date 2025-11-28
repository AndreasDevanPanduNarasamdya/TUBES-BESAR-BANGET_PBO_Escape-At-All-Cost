package com.tubes.pbo.patterns.command;

import com.tubes.pbo.logic.LogicalEngine;

public interface Command {
    // We pass 'args' (the user input split by space) and the 'engine' (to access state)
    void execute(String[] args, LogicalEngine engine);
}