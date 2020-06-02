package com.airforce.ui.screens;

import com.airforce.client.EventBuz;
import com.airforce.client.Player;
import com.airforce.packet.*;
import com.airforce.server.ClientInRoom;
import com.airforce.server.Room;
import com.airforce.server.Utils;
import com.airforce.ui.PlayerHolder;
import com.airforce.ui.ScreenManager;
import com.google.common.eventbus.Subscribe;

import javax.swing.*;

import static com.airforce.common.Constants.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class RoomScreen extends JPanel implements ActionListener {
    private ScreenManager screenManager;
    private JButton exitBtn;
    private JButton startGameBtn;
    private JButton readyBtn;
    private JSeparator separator;
    private JLabel roomIDLb;
    private ArrayList<PlayerHolder> playerHolders;
    private int[] playerHolderLocations = {95, 490, 885};
    private JComboBox levelSelector;

    private Vector<String> levels;
    private Room room;
    private String host;

    private Player player;
    public String playerName;



    public RoomScreen(int width, int height, HashMap<String, Object> args) {
        levels = new Vector<>();
        levels.add("Easy");
        levels.add("Medium");
        levels.add("Hard");
        levels.add("Super");
        setSize(width, height);
        setLayout(null);
        initUI();
        setVisible(false);
        setBackground(Color.BLACK);
        if (args != null) {
            boolean isMaster = false;
            if (args.containsKey("isRoomMaster")) {
                if ((Boolean)args.get("isRoomMaster")) {
                    initRoomServer();
                    isMaster = (boolean) args.get("isRoomMaster");
                    this.host = Utils.getLocalAddress();

                    renderUIofMaster();
                } else {
                    if (args.containsKey("ip")) {
                        this.host = (String) args.get("ip");
                    }
                }
            }

            if (args.containsKey("playerName")) {
                this.playerName = args.get("playerName").toString();
                initPlayer(isMaster, this.host);
            }
        }
    }

    private void renderUIofMaster() {
        startGameBtn = new JButton("Start Game");
        startGameBtn.setBounds(1000, 600, 220, 50);
        startGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));
        startGameBtn.setForeground(Color.BLACK);
        startGameBtn.addActionListener(this);

        roomIDLb = new JLabel("Room ID: " + this.host);
        roomIDLb.setBounds(540, 50, 300, 25);
        roomIDLb.setForeground(Color.CYAN);
        roomIDLb.setForeground(Color.CYAN);
        roomIDLb.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));


        levelSelector.setEnabled(true);

        add(roomIDLb);
        add(startGameBtn);
        remove(readyBtn);
    }

    private void initRoomServer() {
        room = new Room();
        room.start();
    }

    private void initPlayer(boolean isMaster, String host) {
        player = new Player(host, HOST_PORT);
        player.isReady = isMaster;
        player.playerName = this.playerName;
        player.connect();
        try {
            AddConnectionRequestPacket addConnectionRequestPacket = new AddConnectionRequestPacket(playerName, isMaster);
            player.sendObject(addConnectionRequestPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        exitBtn = new JButton("Exit Room");
        readyBtn = new JButton("Ready");
        separator = new JSeparator();
        playerHolders = new ArrayList<>();
        for (int i = 0; i < playerHolderLocations.length; i++) {
            playerHolders.add(new PlayerHolder(playerHolderLocations[i], 210));
            add(playerHolders.get(i));
        }

        levelSelector = new JComboBox(levels);
        levelSelector.setBounds(1110, 10, 150, 25);
        levelSelector.setSelectedIndex(0);
        levelSelector.setEditable(false);
        levelSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String levelName = e.getItem().toString();
                requestChangeGameLevel(levelName);
            }
        });
        levelSelector.setEnabled(false);

        exitBtn.setBounds(60, 600, 220, 50);
        exitBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 14));
        exitBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));
        exitBtn.setForeground(Color.BLACK);
        readyBtn.setBounds(1000, 600, 220, 50);
        readyBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));
        readyBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));
        readyBtn.setForeground(Color.BLACK);
        //separator.setBounds(20, 525, 860, 10);

        exitBtn.addActionListener(this);
        readyBtn.addActionListener(this);

        //add(levelSelector);
        add(exitBtn);
        add(readyBtn);
        add(separator);

        EventBuz.getInstance().register(this);
    }

    @Subscribe
    public void onReceiveAddConnectionResponse(AddConnectionResponsePacket packet) {
        if (packet.isConnectSuccess) {
            display();
        } else {
            JOptionPane.showMessageDialog(this, packet.message, "Server Message", JOptionPane.WARNING_MESSAGE);
            player.close();
            backToHome();
        }
    }

    private void display() {
        setVisible(true);
    }

    @Subscribe
    public void onUpdateRoomInfoEvent(UpdateRoomInfoPacket updateRoomInfoPacket) {
        renderPlayerList(updateRoomInfoPacket.clients);
        renderGameLevel(updateRoomInfoPacket.level);
    }

    @Subscribe
    public void onClosedServerEvent(ClosedServerNotificationPacket closedServerNotificationPacket) {
        backToHome();
        JOptionPane.showMessageDialog(this, closedServerNotificationPacket.message, "Room Closed", JOptionPane.WARNING_MESSAGE);
    }

    @Subscribe
    public void onStartGameEvent(StartGameResponsePacket startGameEvent) {
        startGame();
    }

    private void requestChangeGameLevel(String levelName) {
        ChangeGameLevelPacket packet = new ChangeGameLevelPacket(levels.indexOf(levelName));
        player.sendObject(packet);
    }

    @Subscribe
    public void onNotReadyWarningEvent(NotReadyWarningPacket notReadyWarningPacket) {
        JOptionPane.showMessageDialog(this, notReadyWarningPacket.message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    @Subscribe
    public void onServerNotFoundEvent(ServerNotFoundPacket serverNotFoundPacket) {
        JOptionPane.showMessageDialog(this, serverNotFoundPacket.message, "Warning", JOptionPane.WARNING_MESSAGE);
        backToHome();
    }

    public void renderPlayerList(ArrayList<ClientInRoom> clients) {
        System.out.println("-----------------------------RENDER-----------------------------");
        for (int i = 0; i < MAX_ROOM_SIZE; i++) {
            if (i < clients.size()) {
                ClientInRoom client = clients.get(i);
                if (client != null) {
                    PlayerHolder holder = playerHolders.get(i);
                    holder.setPlayerName(client.playerName);
                    if (client.playerName.equals(this.playerName)) {
                        holder.setFocusPlayer(true);
                    } else {
                        holder.setFocusPlayer(false);
                    }
                    holder.setReadyIcon(client.isReady);
                    holder.setImage(true);
                }
            } else {
                PlayerHolder holder = playerHolders.get(i);
                holder.setPlayerName("No Player");
                holder.setImage(false);
                holder.setReadyIcon(false);
                holder.setFocusPlayer(false);
            }
        }
    }

    private void renderGameLevel(int level) {
        levelSelector.setSelectedIndex(level);
    }

    private void exitRoom() {
        room.doBeforeClose();
        room.shutdown();
    }

    private void backToHome() {
        if (screenManager == null)
            screenManager = ScreenManager.getInstance();
        screenManager.navigate(HOME_SCREEN);
        if (room != null)
            exitRoom();
        if (player != null)
            player.close();
        exitScreen();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitBtn) {
            backToHome();
        } else if (e.getSource() == startGameBtn) {
            player.sendStartGameRequest(1);
        } else if (e.getSource() == readyBtn) {
            player.notifyReadyState(!player.isReady);
        }
    }

    private void startGame() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("player", player);
        ScreenManager.getInstance().navigate(INGAME_SCREEN, args);
    }

    private void exitScreen() {
        EventBuz.getInstance().unregister(this);
    }

    public void resetReadyStatus() {
        if (player.getId() != 0) {  // room master always has id = 0
            player.notifyReadyState(false);
        } else {
            player.notifyReadyState(true);
        }
    }
}
