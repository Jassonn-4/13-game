package com.jason.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameRoom {
    private String id;
    private List<String> players = new ArrayList<>();
    private final int MAX_PLAYERS = 3;
    private GameInstance gameInstance;

    public GameRoom() {
        this.id = UUID.randomUUID().toString();
        this.players = new ArrayList<>();
        this.gameInstance = new GameInstance(this.id);
    }

    public String getId() {
        return id;
    }

    public List<String> getPlayers() {
        return players;
    }

    public GameInstance getGameInstance() {
        return gameInstance;
    }

    public boolean isFull() {
        return players.size() >= MAX_PLAYERS;
    }

    public void addPlayer(String playerId) {
        if(!isFull()) {
            players.add(playerId);
        }
    }

    public void removePlayer(String playerId) {
        players.remove(playerId);
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }
}