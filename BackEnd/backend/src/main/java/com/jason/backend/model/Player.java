// Defintions of a Player
package com.jason.backend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    protected List<Card> hand = new ArrayList<>();
    protected String name;

    public Player(String name) {
        this.name = name;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void addCards(List<Card> cards) {
        hand.addAll(cards);
    }

    public void removeCards(List<Card> cards) {
        hand.removeAll(cards);
    }

    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public void sortHand() {
        Collections.sort(hand);
    }
}