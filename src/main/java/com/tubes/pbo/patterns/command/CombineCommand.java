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

        // Cek keberadaan item
        boolean hasItem1 = tas.getItems().stream().anyMatch(i -> i.getName().equalsIgnoreCase(name1));
        boolean hasItem2 = tas.getItems().stream().anyMatch(i -> i.getName().equalsIgnoreCase(name2));

        if (!hasItem1 || !hasItem2) {
            engine.setLastOutput("Kamu harus punya kedua barang itu di tas untuk menggabungkannya.");
            return;
        }

        // --- RESEP CRAFTING (Hardcoded Logic) ---
        // Contoh: Senter + Baterai = SenterNyala
        if ((name1.equalsIgnoreCase("Senter") && name2.equalsIgnoreCase("Baterai")) ||
            (name1.equalsIgnoreCase("Baterai") && name2.equalsIgnoreCase("Senter"))) {
            
            // Hapus bahan lama
            tas.removeItem(name1);
            tas.removeItem(name2);

            // Tambah item baru
            Item senterNyala = new Item("SenterNyala", "Senter yang menyala terang. Bisa dipakai di tempat gelap.");
            tas.addItem(senterNyala);

            engine.setLastOutput("KLIK! Kamu memasang baterai ke senter. Sekarang senternya menyala!");
        } 
        else {
            engine.setLastOutput("Kedua benda itu tidak bisa digabungkan.");
        }
    }
}