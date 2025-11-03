package com.jason.backend.controller;

import com.jason.backend.model.GameInstance;
import com.jason.backend.model.GameRoom;
import com.jason.backend.service.GameService;
import com.jason.backend.service.RoomService;
import com.jason.backend.model.Card;
import com.jason.backend.model.Player;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final RoomService roomService;

    @Autowired
    public GameController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinGame(@RequestParam String playerName) {
        // assign player and get corresponding gameroom
        GameRoom room = roomService.assignPlayerToRoom(playerName);

        // gets game service
        GameService gameService = room.getGameInstance().getGameService();

        // find player index in the game
        List<Player> players = gameService.getPlayers();
        int playerIndex = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(playerName)) {
                playerIndex = i;
                break;
            }
        }

        if (playerIndex == -1) {
            return ResponseEntity.badRequest().body("Failed to assign player to room");
        }

        //respond with relevant info
        Map<String, Object> response = new HashMap<>();
        response.put("roomId", room.getId());
        response.put("playerName", playerName);
        response.put("playerIndex", playerIndex);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/hand/{roomId}/{playerIndex}")
    public ResponseEntity<?> getPlayerHand(@PathVariable String roomId, @PathVariable int playerIndex) {
        GameService gameService = roomService.getService(roomId);
        if (gameService == null) {
            return ResponseEntity.badRequest().body("instance does not exist");
        }
        return ResponseEntity.ok(gameService.getPlayerHand(playerIndex));
    }

    @GetMapping("/currentPlay/{roomId}")
    public ResponseEntity<?> getCurrentPlay(@PathVariable String roomId) {
        GameService gameService = roomService.getService(roomId);
        if (gameService == null) {
            return ResponseEntity.badRequest().body("instance does not exist");
        }
        return ResponseEntity.ok(gameService.getCurrentPlay());
    }

    @GetMapping("/turn/{roomId}")
    public ResponseEntity<?> getCurrentPlayerIndex(@PathVariable String roomId) {
        GameService gameService = roomService.getService(roomId);
        if (gameService == null) {
            return ResponseEntity.badRequest().body("instance does not exist");
        }
        return ResponseEntity.ok(gameService.getCurrentPlayerIndex());
    }

    @PostMapping("/play")
    public ResponseEntity<?> playCards(@RequestParam String roomId, @RequestParam int playerIndex, @RequestBody List<Card> cards) {
        GameService gameService = roomService.getService(roomId);
        if (gameService == null) {
            return ResponseEntity.badRequest().body("instance does not exist");
        }
        boolean success = gameService.playCards(playerIndex, cards);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/isOver/{roomId}")
    public ResponseEntity<?> isGameOver(@PathVariable String roomId) {
        GameService gameService = roomService.getService(roomId);
        if (gameService == null) {
            return ResponseEntity.badRequest().body("instance does not exist");
        }
        boolean success = gameService.isGameOver();
        return ResponseEntity.ok(success);
    }

    @GetMapping("/isStarted/{roomId}")
    public ResponseEntity<?> isGameStarted(@PathVariable String roomId) {
        GameService gameService = roomService.getService(roomId);
        if (gameService == null) {
            return ResponseEntity.badRequest().body("instance does not exist");
        }
        boolean success = gameService.isStarted();
        return ResponseEntity.ok(success);
    }

    @GetMapping("/endEvent/{roomId}") 
    public ResponseEntity<?> getGameEndEventStatus(@PathVariable String roomId) {
        GameService gameService = roomService.getService(roomId);
        if (gameService == null) {
            return ResponseEntity.badRequest().body("instance does not exist");
        }
        boolean event = gameService.getGameEndEvent();
        if (event) {
            gameService.clearGameEndEvent();
        }
        return ResponseEntity.ok(event);
    }

    @PostMapping("/leave")
    public ResponseEntity<?> leaveGame(@RequestParam String roomId, @RequestParam int playerIndex) {
        GameService gameService = roomService.getService(roomId);
        if (gameService == null) {
            return ResponseEntity.badRequest().body("instance does not exist");
        }
        gameService.playerLeft(playerIndex);
        roomService.removeRoom(roomId);
        return ResponseEntity.ok("Player left, game ended for everyone.");
    }

    @PostMapping("/restart")
    public ResponseEntity<?> restartGame(@RequestParam String roomId) {
        GameService gameService = roomService.getService(roomId);
        if (gameService == null) {
            return ResponseEntity.badRequest().body("instance does not exist");
        }
        gameService.restartGame();
        return ResponseEntity.ok("game sucessfully deleted");
    }

    @GetMapping("/handSize/{roomId}/{playerIndex}")
    public ResponseEntity<?> getHandSize(@PathVariable String roomId, @PathVariable int playerIndex) {
        GameService gameService = roomService.getService(roomId);
        if (gameService == null) {
            return ResponseEntity.badRequest().body("instance does not exist");
        }
        return ResponseEntity.ok(gameService.getPlayerHand(playerIndex).size());
    }
}