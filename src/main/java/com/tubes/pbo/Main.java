package com.tubes.pbo;
import com.tubes.pbo.logic.LogicalEngine;
import com.tubes.pbo.world.Room;
import com.tubes.pbo.world.WorldBuilder;

public class Main {
    public static void main(String[] args) {
        // 1. Lakukan Setup Dunia menggunakan Builder
        WorldBuilder builder = new WorldBuilder();
        Room startRoom = builder.buildWorld();
        Room level2Room = builder.buildWorld2();

        // 2. Oper data ruangan awal ke Engine (Dependency Injection)
        LogicalEngine game = new LogicalEngine(startRoom);

        // 3. Mulai Game
        game.start();
    }
}