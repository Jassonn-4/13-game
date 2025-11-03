package com.jason.backend.controller;

import com.jason.backend.model.*;
import com.jason.backend.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Test
    void testJoinGameReturnsRoomAndIndex() throws Exception {
        GameService mockGameService = mock(GameService.class);
        GameInstance mockInstance = mock(GameInstance.class);
        GameRoom mockRoom = mock(GameRoom.class);

        when(mockRoom.getGameInstance()).thenReturn(mockInstance);
        when(mockInstance.getGameService()).thenReturn(mockGameService);
        when(roomService.assignPlayerToRoom("Jason")).thenReturn(mockRoom);

        List<Player> players = List.of(
                new Player("Alice"),
                new Player("Bob"),
                new Player("Jason")
        );

        when(mockGameService.getPlayers()).thenReturn(players);
        when(mockRoom.getId()).thenReturn("room123");

        mockMvc.perform(post("/api/game/join").param("playerName", "Jason"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId").value("room123"))
                .andExpect(jsonPath("$.playerName").value("Jason"))
                .andExpect(jsonPath("$.playerIndex").value(2));
    }

    @Test
    void testGetPlayerHand() throws Exception {
        String roomId = "room123";
        int playerIndex = 0;

        GameService mockGameService = mock(GameService.class);
        when(roomService.getService(roomId)).thenReturn(mockGameService);

        List<Card> mockHand = List.of(new Card("Hearts", 12));
        when(mockGameService.getPlayerHand(playerIndex)).thenReturn(mockHand);

        mockMvc.perform(get("/api/game/hand/{roomId}/{playerIndex}", roomId, playerIndex))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].suit").value("Hearts"))
                .andExpect(jsonPath("$[0].rank").value(12));
    }

    @Test
    void testGetCurrentPlay() throws Exception {
        String roomId = "room123";
        GameService mockGameService = mock(GameService.class);
        when(roomService.getService(roomId)).thenReturn(mockGameService);

        List<Card> mockPlay = List.of(new Card("Spades", 10));
        when(mockGameService.getCurrentPlay()).thenReturn(mockPlay);

        mockMvc.perform(get("/api/game/currentPlay/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].suit").value("Spades"))
                .andExpect(jsonPath("$[0].rank").value(10));
    }

    @Test
    void testGetCurrentPlayerIndex() throws Exception {
        String roomId = "room123";
        GameService mockGameService = mock(GameService.class);
        when(roomService.getService(roomId)).thenReturn(mockGameService);
        when(mockGameService.getCurrentPlayerIndex()).thenReturn(1);

        mockMvc.perform(get("/api/game/turn/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void testPlayCards() throws Exception {
        String roomId = "room123";
        int playerIndex = 0;
        GameService mockGameService = mock(GameService.class);
        when(roomService.getService(roomId)).thenReturn(mockGameService);
        when(mockGameService.playCards(eq(playerIndex), anyList())).thenReturn(true);

        mockMvc.perform(post("/api/game/play")
                .param("roomId", roomId)
                .param("playerIndex", String.valueOf(playerIndex))
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"suit\":\"Hearts\",\"rank\":7}]"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testIsGameOver() throws Exception {
        String roomId = "room123";
        GameService mockGameService = mock(GameService.class);
        when(roomService.getService(roomId)).thenReturn(mockGameService);
        when(mockGameService.isGameOver()).thenReturn(false);

        mockMvc.perform(get("/api/game/isOver/{roomId}", roomId))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testRestartGame() throws Exception {
        String roomId = "room123";
        GameService mockGameService = mock(GameService.class);
        when(roomService.getService(roomId)).thenReturn(mockGameService);

        doNothing().when(mockGameService).restartGame();
        doNothing().when(roomService).removeRoom(roomId);

        mockMvc.perform(post("/api/game/restart").param("roomId", roomId))
                .andExpect(status().isOk());

        verify(mockGameService, times(1)).restartGame();
        verify(roomService, times(1)).removeRoom(roomId);
    }

    @Test
    void testGetHandSize() throws Exception {
        String roomId = "room123";
        int playerIndex = 0;

        GameService mockGameService = mock(GameService.class);
        when(roomService.getService(roomId)).thenReturn(mockGameService);

        List<Card> mockHand = List.of(new Card("Hearts", 10), new Card("Spades", 11));
        when(mockGameService.getPlayerHand(playerIndex)).thenReturn(mockHand);

        mockMvc.perform(get("/api/game/handSize/{roomId}/{playerIndex}", roomId, playerIndex))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }
}