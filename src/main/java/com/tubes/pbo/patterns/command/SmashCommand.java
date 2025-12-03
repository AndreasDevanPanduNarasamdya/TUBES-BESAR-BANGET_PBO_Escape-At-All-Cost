package com.tubes.pbo.patterns.command;

import com.tubes.pbo.logic.LogicalEngine;
import com.tubes.pbo.models.Utilities;
import com.tubes.pbo.models.BreakableUtility;
import com.tubes.pbo.models.Item;

public class SmashCommand implements Command {
    @Override
    public void execute(String[] args, LogicalEngine engine) {
        // Format: hancurkan [nama_benda] [pakai_alat]
        // Contoh: hancurkan vas palu
        
        if (args.length < 2) {
            engine.setLastOutput("Hancurkan apa? (Contoh: hancurkan vas palu)");
            return;
        }

        String targetName = args[1];
        String toolName = (args.length > 2) ? args[2] : "";

        Utilities util = engine.getCurrentRoom().getUtility(targetName);

        if (util == null) {
            engine.setLastOutput("Tidak ada benda bernama '" + targetName + "' di sini.");
            return;
        }

        if (util instanceof BreakableUtility) {
            BreakableUtility breakable = (BreakableUtility) util;

            // Validasi: Apakah player benar-benar punya alat tersebut di tas?
            if (!toolName.isEmpty()) {
                boolean hasTool = engine.getTas().getItems().stream()
                        .anyMatch(i -> i.getName().equalsIgnoreCase(toolName));
                
                if (!hasTool) {
                    engine.setLastOutput("Kamu tidak memegang barang bernama '" + toolName + "'!");
                    return;
                }
            }

            // Lakukan aksi smash
            String result = breakable.smash(toolName);
            engine.setLastOutput(result);

        } else {
            engine.setLastOutput("Kamu tidak bisa menghancurkan " + targetName + ". Itu vandalisme!");
        }
    }
}