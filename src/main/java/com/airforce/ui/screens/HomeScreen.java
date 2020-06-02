package com.airforce.ui.screens;

import com.airforce.server.Utils;
import com.airforce.ui.ScreenManager;

import javax.swing.*;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import static com.airforce.common.Constants.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class HomeScreen extends JPanel implements ActionListener{

    private ScreenManager screenManager;

    private JButton createGameBtn;
    private JButton joinGameBtn;
    private JButton quitGameBtn;
    private JLabel titleLb;

    public HomeScreen(int width, int height) {
        setSize(width, height);
        setLayout(null);
        initUI();
        setVisible(true);
        initSound();
        setBackground(Color.BLACK);
    }

    private void initSound(){
        try {
            //File sound = new File ("src/main/resources/main_theme.wav");
            File sound = new File ("src/main/resources/InTheEnd.wav");

            AudioInputStream ais = AudioSystem.getAudioInputStream(sound);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void initUI() {
        createGameBtn = new JButton("Host Game");
        joinGameBtn = new JButton("Join Game");
        quitGameBtn = new JButton("Quit");

        titleLb = new JLabel("DNV STAR WARS", SwingConstants.CENTER);
        titleLb.setForeground(Color.CYAN);

        createGameBtn.setBounds(500, 320, 280, 50);
        createGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));
        createGameBtn.setForeground(Color.BLACK);
        joinGameBtn.setBounds(500, 396, 280, 50);
        joinGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));
        joinGameBtn.setForeground(Color.BLACK);
        quitGameBtn.setBounds(500, 472, 280, 50);
        quitGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));
        quitGameBtn.setForeground(Color.BLACK);

        titleLb.setBounds(450, 160, 390, 70);
        titleLb.setFont(new Font("Serif", Font.BOLD, 46));

        quitGameBtn.addActionListener(this);
        createGameBtn.addActionListener(this);
        joinGameBtn.addActionListener(this);

        add(createGameBtn);
        add(joinGameBtn);
        add(quitGameBtn);
        add(titleLb);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitGameBtn) {
            System.exit(0);
        } else if (e.getSource() == createGameBtn) {
            joinRoom(true);
        } else if (e.getSource() == joinGameBtn) {
            joinRoom(false);
        }
    }
    private String enterPlayerName() {
        String name = JOptionPane.showInputDialog(this, "Enter player name:", "Player InGame Name", JOptionPane.QUESTION_MESSAGE);
        if (name == null) {
            JOptionPane.showMessageDialog(this, "Please enter a nickname before starting game!");
        } else if (name.length() < 3) {
            JOptionPane.showMessageDialog(this, "Your nickname is too short(must be longer than 3)!");
        } else if (name.length() > 16) {
            JOptionPane.showMessageDialog(this, "Your nickname is too long(must be shorter than 16!");
        } else {
            return name;
        }
        return null;
    }


    private void joinRoom(Boolean isRoomMaster) {
        String ip = "";
        if (!isRoomMaster) {
            ip = JOptionPane.showInputDialog(this, "Enter room ID:", "Room ID", JOptionPane.QUESTION_MESSAGE);
            if (!Utils.validateIP(ip)) {
                JOptionPane.showMessageDialog(this, "Wrong room ID! Please try again!");
                return;
            }

            if (Utils.availablePort(ip, HOST_PORT)) {
                JOptionPane.showMessageDialog(this, "Server not found!");
                return;
            }
        }
        String playerName = enterPlayerName();
        if (playerName != null) {
            HashMap<String, Object> args = new HashMap<>();
            args.put("playerName", playerName);
            args.put("isRoomMaster", isRoomMaster);

            if (!ip.equals("")) {
                args.put("ip", ip);
            }

            if (screenManager == null)
                screenManager = ScreenManager.getInstance();
            screenManager.navigate(NEW_ROOM_SCREEN, args);
        }
    }
}
