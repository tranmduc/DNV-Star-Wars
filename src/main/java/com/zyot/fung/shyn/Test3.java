package com.zyot.fung.shyn;

import com.zyot.fung.shyn.ui.ScreenManager;

public class Test3 {
    public static void main(String[] args) {
//        GameSetup game = new GameSetup("FuDuSkyWar", 500, 600);
//        game.start();
        ScreenManager window = ScreenManager.getInstance();
        window.display();
    }
}
