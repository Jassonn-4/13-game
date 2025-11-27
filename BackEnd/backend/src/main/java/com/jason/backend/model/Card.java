// Definition of the playing cards
package com.jason.backend.model;
import java.util.Objects;

public class Card implements Comparable<Card> {
    private String suit;
    private int rank;

    public Card(String suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    private int suitValue(Card c) {
        return switch (c.getSuit()) {
            case "Spades" -> 1;
            case "Clubs" -> 2;
            case "Diamonds" -> 3;
            case "Hearts" -> 4;
            default -> 0;
        };
    }

    @Override
    public int compareTo(Card other) {
        if (this.rank != other.rank) {
        return Integer.compare(this.rank, other.rank);
    }
    return Integer.compare(suitValue(this), suitValue(other));
    }

    @Override
    public String toString() {
        return rankToString(rank) + suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return rank == card.rank && Objects.equals(suit, card.suit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    private String rankToString(int rank) {
        return switch (rank) {
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            case 14 -> "A";
            case 15 -> "2";
            default -> String.valueOf(rank);
        };
    }
}