package com.jason.backend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        buildDeck();
        shuffle();
    }

   private void buildDeck() {
        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
        for (String suit : suits) {
            for (int rank = 3; rank <= 15; rank++) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public List<Card> deal(int handSize) {
        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < handSize && !cards.isEmpty(); i++) {
            hand.add(cards.remove(0)); 
        }
        return hand;
    }

    public int size() {
        return cards.size();
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public void reset() {
        cards.clear();
        buildDeck();
        shuffle();
    }
}