package com.jason.backend.service;

import com.jason.backend.model.GameRoom;
import com.jason.backend.model.GameInstance;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomService {
    private final Map<String, GameRoom> rooms = new ConcurrentHashMap<>();

    public synchronized GameRoom assignPlayerToRoom(String playerName) {
        Optional<GameRoom> availableRoom = rooms.values().stream()
            .filter(room -> !room.isFull())
            .findFirst();

        GameRoom room = availableRoom.orElseGet(() -> {
            GameRoom newRoom = new GameRoom();
            rooms.put(newRoom.getId(), newRoom);
            return newRoom;
        });

        room.addPlayer(playerName);

        // starts adds player in gameservice once full game starts
        room.getGameInstance().getGameService().addPlayer(playerName);
        return room;
    }

    public synchronized void removePlayer(String playerName) {
        for (GameRoom room : rooms.values()) {
            if (room.getPlayers().contains(playerName)) {
                room.removePlayer(playerName);
                if (room.isEmpty()) {
                    rooms.remove(room.getId());
                }
                break;
            }
        }
    }
    
    public synchronized void removeRoom(String roomId) {
        rooms.remove(roomId);
    }

    public synchronized GameInstance getGame(String roomId) {
        GameRoom room = rooms.get(roomId);
        return room != null ? room.getGameInstance() : null;
    }

    public synchronized GameService getService(String roomId) {
        GameInstance gameInstance = getGame(roomId);
        if (gameInstance == null) {
            return null;
        }
        GameService gameService = gameInstance.getGameService();
        return gameService;
    }

    public Collection<GameRoom> getAllRooms() {
        return rooms.values();
    }
}