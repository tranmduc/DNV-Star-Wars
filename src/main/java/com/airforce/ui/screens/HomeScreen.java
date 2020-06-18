package com.airforce.ui.screens;

import com.airforce.server.Utils;
import com.airforce.ui.ScreenManager;

import javax.swing.*;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import static com.airforce.common.Constants.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class HomeScreen extends JPanel implements ActionListener{

    private ScreenManager screenManager;

    private JButton createGameBtn;
    private JButton joinGameBtn;
    private JButton quitGameBtn;
    private JButton muteBtn;
    private JLabel titleLb;
    Font pixelMplus;
    
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
            File sound = new File ("src/main/resources/main_theme.wav");
//            File sound = new File ("src/main/resources/InTheEnd.wav");

            AudioInputStream ais = AudioSystem.getAudioInputStream(sound);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    class RoundedBorder implements Border {
        int radius;
        RoundedBorder(int radius) {
            this.radius = radius;
        }
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }
        public boolean isBorderOpaque() {
            return true;
        }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x,y,width-1,height-1,radius,radius);
        }
    }	
    
    private void initUI() {
        createGameBtn = new JButton("Host Game");
        joinGameBtn = new JButton("Join Game");
        quitGameBtn = new JButton("Exit");
      
        createGameBtn.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseEntered(java.awt.event.MouseEvent evt) {
        		createGameBtn.setBackground(Color.cyan);
        	}
        	public void mouseExited (java.awt.event.MouseEvent evt) {
        		createGameBtn.setBackground(UIManager.getColor("control"));
        	}
        });
        createGameBtn.setBorder(new RoundedBorder(20));
        createGameBtn.setFocusable(false);
        
        joinGameBtn.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseEntered(java.awt.event.MouseEvent evt) {
        		joinGameBtn.setBackground(Color.cyan);
        	}
        	public void mouseExited (java.awt.event.MouseEvent evt) {
        		joinGameBtn.setBackground(UIManager.getColor("control"));
        	}
        });
        joinGameBtn.setBorder(new RoundedBorder(20));
        joinGameBtn.setFocusable(false);
        
        quitGameBtn.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseEntered(java.awt.event.MouseEvent evt) {
        		quitGameBtn.setBackground(Color.cyan);
        	}
        	public void mouseExited (java.awt.event.MouseEvent evt) {
        		quitGameBtn.setBackground(UIManager.getColor("control"));
        	}
        });
        quitGameBtn.setBorder(new RoundedBorder(20));
        quitGameBtn.setFocusable(false);
        
        
        titleLb = new JLabel("DNV STAR WARS", SwingConstants.CENTER);
        titleLb.setForeground(Color.CYAN);
        

        createGameBtn.setBounds(500, 320, 280, 50);
        createGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));
//        createGameBtn.setForeground(Color.BLACK);
        joinGameBtn.setBounds(500, 396, 280, 50);
        joinGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));
        joinGameBtn.setForeground(Color.BLACK);
        quitGameBtn.setBounds(500, 472, 280, 50);
        quitGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));
        quitGameBtn.setForeground(Color.BLACK);   
        
        titleLb.setBounds(350, 160, 600, 70);
        
        
        try {
        	pixelMplus = Font.createFont(Font.TRUETYPE_FONT, new File("PixelMplus10-Regular.ttf")).deriveFont(80f);
        	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        	ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("PixelMplus10-Regular.ttf")));
        } catch (IOException | FontFormatException e) {
        	
        }
//        titleLb.setFont(new Font("Vertana", Font.BOLD, 46));
        titleLb.setFont(pixelMplus);

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
        String playerName = enterPlayerName();
        if (playerName == null){
            return;
        }

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
//        String playerName = enterPlayerName();
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
