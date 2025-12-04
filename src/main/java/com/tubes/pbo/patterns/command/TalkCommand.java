package com.tubes.pbo.patterns.command;

import com.tubes.pbo.logic.LogicalEngine;
import com.tubes.pbo.models.NPCUtility;
import com.tubes.pbo.models.Utilities;

public class TalkCommand implements Command {
    @Override
    public void execute(String[] args, LogicalEngine engine) {
        if (args.length < 2) {
            engine.setLastOutput("Bicara dengan siapa?");
            return;
        }

        String targetName = args[1];
        Utilities util = engine.getCurrentRoom().getUtility(targetName);

        if (util instanceof NPCUtility) {
            NPCUtility npc = (NPCUtility) util;
            String response = npc.talk();
            
            // Jika NPC drop item (status jadi OPEN), pindahkan item ke Room agar bisa di 'ambil'
            if (util.isOpen() && util.peekItem() != null) {
                engine.getCurrentRoom().addItem(util.lootItem());
                response += "\n(Item terjatuh ke lantai.)";
            }
            
            engine.setLastOutput(response);
        } else if (util != null) {
            engine.setLastOutput("Kamu bergumam ke " + targetName + "... tapi tidak ada jawaban.");
        } else {
            engine.setLastOutput("Tidak ada '" + targetName + "' di sini.");
        }
    }
}