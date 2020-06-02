package com.airforce.packet;

import java.io.Serializable;

public class StartGameRequestPacket implements Serializable {
    int level = 1;

    public StartGameRequestPacket(int level) {
        this.level = level;
    }
}
