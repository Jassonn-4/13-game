package com.jason.backend.model;

import java.util.*;
import java.util.stream.Collectors;

public class PlayTypeClassifier {

    public static PlayType classify(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            return PlayType.INVALID;
        }

        cards.sort(Card::compareTo); // Ensure cards are sorted for checking straight

        int size = cards.size();

        if (size == 1) {
            return PlayType.SINGLE;
        }

        if (size == 2 && isSameRank(cards)) {
            return PlayType.PAIR;
        }

        if (size == 3 && isSameRank(cards)) {
            return PlayType.TRIPLE;
        }

        if (size == 4 && isSameRank(cards)) {
            return PlayType.FOUR_OF_A_KIND;
        }

        if (size == 5) {
            if (isFullHouse(cards)) {
                return PlayType.FULL_HOUSE;
            }

            if (isStraight(cards)) {
                return PlayType.STRAIGHT;
            }
        }

        return PlayType.INVALID;
    }

    private static boolean isSameRank(List<Card> cards) {
        int rank = cards.get(0).getRank();
        return cards.stream().allMatch(card -> card.getRank() == rank);
    }

    private static boolean isStraight(List<Card> cards) {
        for (int i = 1; i < cards.size(); i++) {
            int prevRank = cards.get(i - 1).getRank();
            int currRank = cards.get(i).getRank();

            // disallow 2s and wraparounds like Q-K-A-2-3
            if (prevRank == 15 || currRank == 15 || currRank != prevRank + 1) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFullHouse(List<Card> cards) {
        Map<Integer, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        return rankCounts.size() == 2 &&
                (rankCounts.containsValue(3L) && rankCounts.containsValue(2L));
    }

    public static int getTripleRank(List<Card> cards) {
        Map<Integer, Long> rankCounts = cards.stream()
            .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        return rankCounts.entrySet().stream()
            .filter(e -> e.getValue() == 3)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(-1);
    }
}