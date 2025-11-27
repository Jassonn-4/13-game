package com.jason.backend.service;

import com.jason.backend.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {
    private final Deck deck;
    private final List<Player> players;
    private final List<Card> currentPlay;
    private int currentPlayerIndex;
    private int playerCount;
    private boolean gameStarted = false;
    private boolean gameEndEvent = false;

    public GameService() {
        this.deck = new Deck();
        this.players = new ArrayList<>();
        this.currentPlay = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameStarted = false;
        this.gameEndEvent = false;
    }

//waits for 3 players to start game
    public boolean addPlayer(String name) {
        if (gameStarted || players.size() >= 3) {
            return false;
        }
        players.add(new Player(name));
        if (players.size() == 3) {
            initializeGame();
        }
        return true;
    }

    public void initializeGame() {
        deck.reset();
        deck.shuffle();
        dealCards();
        gameStarted = true;
    }

// deals each players one card at a time until deck is empty
    private void dealCards() {
        int cardsPerPlayer = 13;
        for (int i = 0; i < cardsPerPlayer; i++) {
            for (Player player : players) {
                if (!deck.getCards().isEmpty()) {
                    Card card = deck.deal(1).get(0);
                    player.addCard(card);
                }
            }
        }
        players.forEach(Player::sortHand);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }

// makes sure players hand is valid, clears the hand and puts the tables hand as the one played and goes to next player
    public boolean playCards(int playerIndex, List<Card> cards) {
        //checks if player has cards its trying to play
        Player player = players.get(playerIndex);

        if (cards.isEmpty()) {
            playerCount++;

            if (playerCount >= players.size() - 1) {
                currentPlay.clear();
                playerCount = 0;
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            return true;
        }

        if (!player.getHand().containsAll(cards)) {
            return false;
        }
        //checks if the hand trying to be played is valid
        if (!isValidPlay(cards, currentPlay)) {
            return false;
        }
        //updates game state 
            player.removeCards(cards);
            currentPlay.clear();
            currentPlay.addAll(new ArrayList<>(cards));
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            playerCount = 0;
            return true;
    }

    public List<Card> getCurrentPlay() {
        return currentPlay;
    }

    public boolean isValidPlay(List<Card> newPlay, List<Card> currentPlay) {
        //figures out what type of play: triple,straight etc...
        PlayType newType = PlayTypeClassifier.classify(newPlay);
        PlayType currentType = PlayTypeClassifier.classify(currentPlay);
        
        if (newType == PlayType.INVALID) return false;
        if (currentPlay == null || currentPlay.isEmpty()) return true;
        if (newType != currentType) return false;

        List<Card> sortedNew = new ArrayList<>(newPlay);
        List<Card> sortedCurrent = new ArrayList<>(currentPlay);
        Collections.sort(sortedNew);
        Collections.sort(sortedCurrent);

        switch (newType) {
            case SINGLE:
                return sortedNew.get(0).compareTo(sortedCurrent.get(0)) > 0;
            case PAIR:
            case TRIPLE:
                return sortedNew.get(sortedNew.size() - 1).compareTo(
                       sortedCurrent.get(sortedCurrent.size() - 1)) > 0;
            case FOUR_OF_A_KIND:
                return sortedNew.get(0).compareTo(sortedCurrent.get(0)) > 0;
            case FULL_HOUSE:
                int newTripleRank = PlayTypeClassifier.getTripleRank(sortedNew);
                int currentTripleRank = PlayTypeClassifier.getTripleRank(sortedCurrent);
                return newTripleRank > currentTripleRank;
            case STRAIGHT:
                return sortedNew.get(sortedNew.size() - 1).compareTo(
                       sortedCurrent.get(sortedCurrent.size() - 1)) > 0;
            default:
                return false;
        }
    }

    public void playerLeft(int playerIndex) {
        gameEndEvent = true;
    }

    public boolean isGameOver() {
        if (gameStarted){
        boolean Winner = players.stream().anyMatch(player -> player.getHand().isEmpty());
        if (Winner) {
            gameEndEvent = true;
        }
        return Winner;
        } else {
            return false;
        }
    }

    public boolean getGameEndEvent() {
        return gameEndEvent;
    }

    public boolean isStarted() {
        return gameStarted;
    }

    public void clearGameEndEvent() {
        gameEndEvent = false;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void restartGame() {
        players.clear();
        currentPlay.clear();
        currentPlayerIndex = 0;
        gameStarted = false;
        gameEndEvent = false;
        deck.reset();
    }

    public List<Card> getPlayerHand(int index) {
    return players.get(index).getHand();
}
}