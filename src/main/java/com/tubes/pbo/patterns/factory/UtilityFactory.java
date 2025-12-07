package com.tubes.pbo.patterns.factory;

import com.tubes.pbo.models.*;

public class UtilityFactory {

    // Method untuk membuat benda yang butuh password (Brankas)
    public static Utilities createSafe(String name, String desc, Item loot, String pin) {
        return new PasswordUtility(name, desc, loot, pin);
    }

    // Method untuk membuat benda yang bisa dibuka langsung (Lemari, Kulkas)
    public static Utilities createContainer(String name, String desc, Item loot) {
        return new OpenableUtility(name, desc, loot);
    }

    // Method untuk membuat Pintu Keluar
    public static Utilities createExit(String name, String desc, String keyName) {
        return new ExitDoor(name, desc, keyName);
    }
    // Import BreakableUtility first if not auto-imported
    public static Utilities createBreakable(String name, String desc, Item loot, String toolNeeded) {
    return new BreakableUtility(name, desc, loot, toolNeeded);
    }

    public static Utilities createNPC(String name, String desc, Item itemHeld, String dialogue) {
        return new NPCUtility(name, desc, itemHeld, dialogue);
    }
}