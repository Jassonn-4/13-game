package com.jason.backend.model;

import java.util.HashMap;
import java.util.Map;
import com.jason.backend.service.GameService;

public class GameInstance {
    private String roomId;
    private boolean started = false;
    private final GameService gameService;

    public GameInstance(String roomId) {
        this.roomId = roomId;
        this.gameService = new GameService();
    }

    public void startGame() {
        if (!started) {
            gameService.initializeGame();
            this.started = true;
            System.out.println("game has started:" + roomId);
        }
    }

    public boolean isStarted() {
        return started;
    }

    public GameService getGameService() {
        return gameService;
    }

    public String getRoomId() {
        return roomId;
    }
}
