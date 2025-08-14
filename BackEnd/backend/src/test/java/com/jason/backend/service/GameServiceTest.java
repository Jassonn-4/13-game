package com.jason.backend.service;

import com.jason.backend.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import com.jason.backend.model.PlayType;
import com.jason.backend.model.PlayTypeClassifier;

class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    @Test
    void testAddPlayer() {
        assertTrue(gameService.addPlayer("Alice"));
        assertTrue(gameService.addPlayer("Bob"));
        assertTrue(gameService.addPlayer("Charlie"));
        assertFalse(gameService.addPlayer("Dave")); // Game only allows 3 players
    }

    @Test
    void testPlayerReceivesCards() {
        gameService.addPlayer("A");
        gameService.addPlayer("B");
        gameService.addPlayer("C");

        assertFalse(gameService.getPlayers().get(0).getHand().isEmpty());
    }

    @Test
    void testValidPlay() {
        gameService.addPlayer("A");
        gameService.addPlayer("B");
        gameService.addPlayer("C");

        var hand = gameService.getPlayers().get(0).getHand();
        var play = new ArrayList<>(hand.subList(0, 1));

        assertTrue(gameService.playCards(0, play));
    }

    @Test
    void testPlayPair() {
        gameService.addPlayer("A");
        gameService.addPlayer("B");
        gameService.addPlayer("C");

        List<Card> pair = new ArrayList<>(List.of(
            new Card("Hearts", 7),
            new Card("Clubs", 7)
        ));

        var player = gameService.getPlayers().get(0);
        pair.forEach(player::addCard);

        assertEquals(PlayType.PAIR, PlayTypeClassifier.classify(pair));
        assertTrue(gameService.playCards(0, new ArrayList<>(pair)));
    }

    @Test
    void testPlayTriple() {

        gameService.addPlayer("A");
        gameService.addPlayer("B");
        gameService.addPlayer("C");

        List<Card> triple = new ArrayList<>(List.of(
            new Card("Hearts", 5),
            new Card("Diamonds", 5),
            new Card("Clubs", 5)
        ));

        gameService.getPlayers().get(0).addCard(triple.get(0));
        gameService.getPlayers().get(0).addCard(triple.get(1));
        gameService.getPlayers().get(0).addCard(triple.get(2));

        assertEquals(PlayType.TRIPLE, PlayTypeClassifier.classify(triple));
        assertTrue(gameService.playCards(0, new ArrayList<>(triple)));
    }

    @Test
    void testPlayFullHouse() {
        gameService.addPlayer("A");
        gameService.addPlayer("B");
        gameService.addPlayer("C");

        List<Card> fullHouse = new ArrayList<>(List.of(
            new Card("Hearts", 8),
            new Card("Diamonds", 8),
            new Card("Clubs", 8),
            new Card("Spades", 6),
            new Card("Hearts", 6)
        ));

        var player = gameService.getPlayers().get(0);
        fullHouse.forEach(player::addCard);

        assertEquals(PlayType.FULL_HOUSE, PlayTypeClassifier.classify(fullHouse));
        assertTrue(gameService.playCards(0, new ArrayList<>(fullHouse)));
    }

    @Test
    void testPlayStraight() {
        gameService.addPlayer("A");
        gameService.addPlayer("B");
        gameService.addPlayer("C");

        List<Card> straight = new ArrayList<>(List.of(
            new Card("Hearts", 3),
            new Card("Diamonds", 4),
            new Card("Clubs", 5),
            new Card("Spades", 6),
            new Card("Hearts", 7)
        ));

        var player = gameService.getPlayers().get(0);
        straight.forEach(player::addCard);

        // Check classification is straight
        assertEquals(PlayType.STRAIGHT, PlayTypeClassifier.classify(straight));

        // Test that playing this straight works
        assertTrue(gameService.playCards(0, new ArrayList<>(straight)));
    }
}