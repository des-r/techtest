package io.quell.techtest;

import com.google.common.collect.Sets;
import io.quell.techtest.domain.Card;
import io.quell.techtest.domain.Hand;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {

    public static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
    public static final String[] SUITS = {"C", "D", "H", "S"};
    private static final int SIZE = SUITS.length * RANKS.length;
    private static final int HAND_SIZE = 5;

    public static final List<String> UNMODIFIABLE_RANKS = Collections.unmodifiableList(Arrays.asList(RANKS));
    public static final Set<Card> FULL_DECK = buildDeck();
    public static final List<Hand> ALL_HANDS = buildHands();

    private static List<Hand> buildHands() {
        //noinspection UnstableApiUsage
        return Sets.combinations(FULL_DECK, HAND_SIZE).parallelStream()
                .map(Hand::new)
                .collect(Collectors.toList());
    }

    private static Set<Card> buildDeck() {
        final Card[] deck = new Card[SIZE];

        for (int i = 0; i < RANKS.length; i++) {
            for (int j = 0; j < SUITS.length; j++) {
                deck[SUITS.length * i + j] = new Card(RANKS[i] + SUITS[j]);
            }
        }

        return new HashSet<>(Arrays.asList(deck));
    }
}
