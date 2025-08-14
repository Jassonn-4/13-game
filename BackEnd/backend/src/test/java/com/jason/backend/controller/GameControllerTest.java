package com.jason.backend.controller;

import com.jason.backend.service.GameService;
import com.jason.backend.model.Card;
import com.jason.backend.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    void testJoinGameReturnsIndex() throws Exception {
    // Mock player addition
    when(gameService.addPlayer("Jason")).thenReturn(true);

    // Mock players list
    List<Player> mockPlayers = List.of(
        new Player("Alice"),
        new Player("Bob"),
        new Player("Jason")  // Jason is index 2
    );
    when(gameService.getPlayers()).thenReturn(mockPlayers);

    mockMvc.perform(post("/api/game/join")
            .param("playerName", "Jason"))
            .andExpect(status().isOk())
            .andExpect(content().string("2")); // Index 2 as string
}

    @Test
    void testGetHand() throws Exception {
        Player p = new Player("Jason");
        p.addCard(new Card("Hearts", 12));
        when(gameService.getPlayerHand(0)).thenReturn(p.getHand());

        mockMvc.perform(get("/api/game/hand/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].suit").value("Hearts"))
                .andExpect(jsonPath("$[0].rank").value(12));
    }

    @Test
    void testGetCurrentPlay() throws Exception {
        List<Card> currentPlay = List.of(new Card("Spades", 10));
        when(gameService.getCurrentPlay()).thenReturn(currentPlay);

        mockMvc.perform(get("/api/game/currentPlay"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].suit").value("Spades"))
            .andExpect(jsonPath("$[0].rank").value(10));
    }

    @Test
    void testGetCurrentPlayerIndex() throws Exception {
        when(gameService.getCurrentPlayerIndex()).thenReturn(1);

        mockMvc.perform(get("/api/game/turn"))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }

    @Test
    void testPlayCards() throws Exception {
        List<Card> cardsToPlay = List.of(new Card("Hearts", 7));
        when(gameService.playCards(eq(0), anyList())).thenReturn(true);

        mockMvc.perform(post("/api/game/play")
            .param("playerIndex", "0")
            .contentType("application/json")
            .content("[{\"suit\":\"Hearts\",\"rank\":7}]"))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
    }

    @Test
    void testIsGameOver() throws Exception {
        when(gameService.isGameOver()).thenReturn(false);

        mockMvc.perform(get("/api/game/isOver"))
            .andExpect(status().isOk())
            .andExpect(content().string("false"));
    }

    @Test
    void testRestartGame() throws Exception {
        doNothing().when(gameService).restartGame();

        mockMvc.perform(post("/api/game/restart"))
            .andExpect(status().isOk());

        verify(gameService, times(1)).restartGame();
    }

    @Test
    void testGetHandSize() throws Exception {
        int playerIndex = 0;
        List<Card> hand = List.of(
            new Card("hearts", 10),
            new Card("spades", 11)
    );

    when(gameService.getPlayerHand(playerIndex)).thenReturn(hand);

    mockMvc.perform(get("/api/game/handSize/{playerIndex}", playerIndex))
        .andExpect(status().isOk())
        .andExpect(content().string("2"));
}
}