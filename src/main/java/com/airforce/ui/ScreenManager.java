package com.airforce.ui;

import com.airforce.common.Constants;
import com.airforce.server.Room;
import com.airforce.ui.screens.HomeScreen;
import com.airforce.ui.screens.IngameScreen;
import com.airforce.ui.screens.RoomScreen;

import javax.swing.*;

import static com.airforce.common.Constants.*;

import java.awt.*;
import java.util.HashMap;

public class ScreenManager {
    private static ScreenManager instance;
    private HomeScreen homeScreen;
    private RoomScreen roomScreen;
    private JFrame window;

    private ScreenManager() {
        window = new JFrame("Star Wars");
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(false);
        window.getContentPane().setBackground(Color.BLACK);

        navigate(HOME_SCREEN);
    }

    public void display() {
        window.setVisible(true);
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            synchronized (ScreenManager.class) {
                if (null == instance) {
                    instance = new ScreenManager();
                }
            }
        }
        return instance;
    }

    public void navigate(String screenName, HashMap<String, Object> args) {
        if (screenName.equals(HOME_SCREEN)) {
            openNewScreen(getHomeScreen());
        } else if (screenName.equals(NEW_ROOM_SCREEN)) {
            String playerName;
            Boolean isRoomMaster = false;
            String ip = "";
            if (args != null) {
                if (args.containsKey("playerName")) {
                    playerName = args.get("playerName").toString();
                } else {
                    playerName = "PlayerNameDefault";
                }

                if (args.containsKey("isRoomMaster")) {
                    isRoomMaster = (Boolean) args.get("isRoomMaster");
                }

                if (args.containsKey("ip")) {
                    ip = (String) args.get("ip");
                }
            } else {
                playerName = "PlayerNameDefault";
            }

            openNewScreen(getNewRoomScreen(playerName, isRoomMaster, ip));
        } else if (screenName.equals(EXISTED_ROOM_SCREEN)) {
            openNewScreen(getRoomScreen());
        } else if (screenName.equals(INGAME_SCREEN)) {
            openNewScreen(getIngameScreen(args));
            Room.isInGame = true;
        }
    }

    public void navigate(String screenName) {
        navigate(screenName, null);
    }

    private void openNewScreen(JPanel screen) {
        window.getContentPane().removeAll();
        window.getContentPane().add(screen);
        window.revalidate();
        window.repaint();
    }

    private synchronized HomeScreen getHomeScreen() {
        if (homeScreen == null) {
            homeScreen = new HomeScreen(SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        return homeScreen;
    }

    private synchronized RoomScreen getRoomScreen() {
        if (roomScreen == null) {
            roomScreen = new RoomScreen(SCREEN_WIDTH, SCREEN_HEIGHT, null);
        } else {
            roomScreen.resetReadyStatus();
        }
        return roomScreen;
    }

    private synchronized RoomScreen getNewRoomScreen(String playerName, Boolean isRoomMaster, String ip) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("playerName", playerName);
        args.put("isRoomMaster", isRoomMaster);
        args.put("ip", ip);
        roomScreen = new RoomScreen(SCREEN_WIDTH, SCREEN_HEIGHT, args);
        return roomScreen;
    }

    private synchronized JPanel getIngameScreen(HashMap<String, Object> args) {
        return new IngameScreen(Constants.IN_GAME_SCREEN_WIDTH, Constants.IN_GAME_SCREEN_HEIGHT, args);
    }

    public JFrame getWindow() {
        return this.window;
    }
}
