package com.jason.backend.controller;

import com.jason.backend.model.Player;
import com.jason.backend.model.Card;
import com.jason.backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinGame(@RequestParam String playerName) {
    boolean joined = gameService.addPlayer(playerName);
    if (!joined) {
        return ResponseEntity.badRequest().body("Game is already started or full. Cannot join.");
    }

    List<Player> players = gameService.getPlayers();
    int index = -1;
    for (int i = 0; i < players.size(); i++) {
        if (players.get(i).getName().equals(playerName)) {
            index = i;
            break;
        }
    }

        return ResponseEntity.ok(index);
    }

    @GetMapping("/hand/{playerIndex}")
    public List<Card> getPlayerHand(@PathVariable int playerIndex) {
        return gameService.getPlayerHand(playerIndex);
    }

    @GetMapping("/currentPlay")
    public List<Card> getCurrentPlay() {
        return gameService.getCurrentPlay();
    }

    @GetMapping("/turn")
    public int getCurrentPlayerIndex() {
        return gameService.getCurrentPlayerIndex();
    }

    @PostMapping("/play")
    public boolean playCards(@RequestParam int playerIndex, @RequestBody List<Card> cards) {
        return gameService.playCards(playerIndex, cards);
    }

    @GetMapping("/isOver")
    public boolean isGameOver() {
        return gameService.isGameOver();
    }

    @GetMapping("/isStarted")
    public boolean isGameStarted() {
        return gameService.isStarted();
    }

    @GetMapping("/endEvent") 
    public boolean getGameEndEventStatus() {
        boolean event = gameService.getGameEndEvent();
        if (event) {
            gameService.clearGameEndEvent();
        }
        return event;
    }

    @PostMapping("/leave")
    public ResponseEntity<?> leaveGame(@RequestParam int playerIndex) {
        gameService.playerLeft(playerIndex);
        return ResponseEntity.ok("Player left, game ended for everyone.");
    }

    @PostMapping("/restart")
    public void restartGame() {
        gameService.restartGame();
    }

    @GetMapping("/handSize/{playerIndex}")
    public int getHandSize(@PathVariable int playerIndex) {
        return gameService.getPlayerHand(playerIndex).size();
    }
}