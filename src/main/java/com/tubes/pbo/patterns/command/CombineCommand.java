package com.tubes.pbo.patterns.command;

import com.tubes.pbo.logic.LogicalEngine;
import com.tubes.pbo.models.Item;
import com.tubes.pbo.patterns.singleton.Inventory;

public class CombineCommand implements Command {
    @Override
    public void execute(String[] args, LogicalEngine engine) {
        // Format: gabung [item1] [item2]
        if (args.length < 3) {
            engine.setLastOutput("Gabung apa? Gunakan: 'gabung [item1] [item2]'");
            return;
        }

        String name1 = args[1];
        String name2 = args[2];
        Inventory tas = engine.getTas();

        // 1. Cek apakah player punya kedua barang tersebut
        boolean hasItem1 = tas.getItems().stream().anyMatch(i -> i.getName().equalsIgnoreCase(name1));
        boolean hasItem2 = tas.getItems().stream().anyMatch(i -> i.getName().equalsIgnoreCase(name2));

        if (!hasItem1 || !hasItem2) {
            engine.setLastOutput("Kamu harus punya kedua barang itu di tas untuk menggabungkannya.");
            return;
        }

        // --- LOGIC KOMBINASI (Di sini kita atur resepnya) ---

        // RESEP 1: Senter + Baterai = SenterNyala
        if ((name1.equalsIgnoreCase("Senter") && name2.equalsIgnoreCase("Baterai")) ||
                (name1.equalsIgnoreCase("Baterai") && name2.equalsIgnoreCase("Senter"))) {

            tas.removeItem(name1);
            tas.removeItem(name2);
            tas.addItem(new Item("SenterNyala", "Senter yang menyala terang."));
            engine.setLastOutput("KLIK! Kamu memasang baterai ke senter. Sekarang menyala!");
        }
        // RESEP 2: BalokKayu + BalokBesi = Palu (INI YANG BARU)
        else if ((name1.equalsIgnoreCase("BalokKayu") && name2.equalsIgnoreCase("BalokBesi")) ||
                (name1.equalsIgnoreCase("BalokBesi") && name2.equalsIgnoreCase("BalokKayu"))) {

            tas.removeItem(name1);
            tas.removeItem(name2);

            // Hadiahnya adalah item "Palu"
            tas.addItem(new Item("Palu", "Palu godam rakitan. Sangat berat dan kuat."));

            engine.setLastOutput("DUENG! Kamu mengikat besi ke kayu dengan kuat. Jadilah PALU!");
        }
        // Jika tidak ada resep yang cocok
        else {
            engine.setLastOutput("Kedua benda itu tidak bisa digabungkan.");
        }
    }
}