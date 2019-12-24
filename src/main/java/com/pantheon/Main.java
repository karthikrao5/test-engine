package com.pantheon;

import com.pantheon.core.kernel.Game;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        game.getEngine().createWindow(800, 800);
        game.init();
        game.launch();
    }
}
