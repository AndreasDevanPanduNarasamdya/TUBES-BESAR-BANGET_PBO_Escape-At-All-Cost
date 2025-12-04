package com.tubes.pbo.models;

import com.tubes.pbo.models.Item;       // Explicit import
import com.tubes.pbo.models.Utilities;  // Explicit import
import com.tubes.pbo.patterns.state.UtilityState; // Explicit import to fix the error

public class NPCUtility extends Utilities {
    private String dialogue;
    private boolean hasTalked;

    public NPCUtility(String name, String desc, Item itemHeld, String dialogue) {
        super(name, desc, itemHeld);
        this.dialogue = dialogue;
        this.hasTalked = false;
        // Now UtilityState should be recognized correctly
        this.state = UtilityState.UNLOCKED; 
    }

    @Override
    public String solve(String input) {
        return "Jangan diapakan-apakan. Coba ajak 'bicara'.";
    }

    public String talk() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" berkata: \"").append(dialogue).append("\"");

        // Jika NPC memegang item, dia memberikannya saat diajak bicara pertama kali
        if (!hasTalked && itemInside != null) {
            sb.append("\n(Sambil bicara, dia menyerahkan: [").append(itemInside.getName()).append("] kepadamu)");
            // Kita set state jadi OPEN agar item bisa diambil oleh logic game (atau manual)
            this.state = UtilityState.OPEN; 
        }
        
        hasTalked = true;
        return sb.toString();
    }
}