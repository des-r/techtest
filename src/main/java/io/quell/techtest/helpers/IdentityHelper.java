package io.quell.techtest.helpers;

import io.quell.techtest.domain.Card;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.quell.techtest.AppConfig.UNMODIFIABLE_RANKS;

@Slf4j
public class IdentityHelper {

    public static final List<Card> ROYAL_FLUSH_HEARTS = Collections.unmodifiableList(Arrays.asList(
            new Card("AH"),
            new Card("KH"),
            new Card("QH"),
            new Card("JH"),
            new Card("TH")
    ));
    public static final List<Card> ROYAL_FLUSH_CLUBS = Collections.unmodifiableList(Arrays.asList(
            new Card("AC"),
            new Card("KC"),
            new Card("QC"),
            new Card("JC"),
            new Card("TC")
    ));
    public static final List<Card> ROYAL_FLUSH_SPADES = Collections.unmodifiableList(Arrays.asList(
            new Card("AS"),
            new Card("KS"),
            new Card("QS"),
            new Card("JS"),
            new Card("TS")
    ));
    public static final List<Card> ROYAL_FLUSH_DIAMONDS = Collections.unmodifiableList(Arrays.asList(
            new Card("AD"),
            new Card("KD"),
            new Card("QD"),
            new Card("JD"),
            new Card("TD")
    ));

    public static final List<List<Card>> ALL_ROYAL_FLUSH_HANDS = Arrays.asList(
            ROYAL_FLUSH_CLUBS,
            ROYAL_FLUSH_DIAMONDS,
            ROYAL_FLUSH_SPADES,
            ROYAL_FLUSH_HEARTS
    );

    public static final int TWO = 2;
    public static final long THREE = 3;
    public static final int FOUR = 4;

    public static final List<String> allRanks = Arrays.asList("A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A");
    public static final List<Set<String>> SEQUENTIAL_BLOCKS = new ArrayList<>();
    public static final int FIVE = 5;

    public static final int ZERO = 0;

    static {
        for (int i = ZERO; i < allRanks.indexOf("J"); i++) {
            Set<String> work = new HashSet<>(Arrays.asList(allRanks.get(i), allRanks.get(i + 1), allRanks.get(i + TWO), allRanks.get(i + 3), allRanks.get(i + FOUR)));
            SEQUENTIAL_BLOCKS.add(work);
        }
    }

    public boolean isRoyalFlush(Set<Card> cards) {
        return ALL_ROYAL_FLUSH_HANDS.stream().anyMatch(p -> p.containsAll(cards));
    }

    public boolean isStraightFlush(Set<Card> cards) {
        return !isRoyalFlush(cards) && suitsMatch(cards) && isHandInSequence(cards);
    }

    public boolean isFourOfAKind(Set<Card> cards) {
        return doesHandContain(cards, FOUR, Card::getRank);
    }

    public boolean isFullHouse(Set<Card> cards) {
        Map<String, Long> collect = groupByFunctionAndCount(cards, Card::getRank)
                .entrySet().stream()
                .filter(map -> map.getValue() >= TWO)
                .collect(HashMap::new, (map, group) -> map.put(group.getKey(), group.getValue()), HashMap::putAll);

        return (collect.size() == TWO && collect.containsValue(THREE));
    }

    public boolean isFlush(Set<Card> cards) {
        return !isRoyalFlush(cards) && !isStraightFlush(cards) && suitsMatch(cards);
    }

    public boolean isStraight(Set<Card> cards) {
        return !suitsMatch(cards) && isHandInSequence(cards);
    }

    public boolean isThreeOfAKind(Set<Card> cards) {
        return doesHandContain(cards, (int) THREE, Card::getRank) &&
                !doesHandContain(cards, FOUR, Card::getRank) &&
                !isFullHouse(cards);
    }

    public boolean isTwoPair(Set<Card> cards) {

        int[] cardValues = new int[14];
        int numberOfPairs = ZERO;

        for (Card card : cards) {
            cardValues[UNMODIFIABLE_RANKS.indexOf(card.getRank())]++;
        }

        for (int cardValue : cardValues) {
            if (cardValue == TWO) {
                numberOfPairs++;
            }
        }

        return numberOfPairs == TWO;
    }

    public boolean isOnePair(Set<Card> cards) {

        int[] cardValues = new int[14];

        for (Card card : cards) {
            cardValues[UNMODIFIABLE_RANKS.indexOf(card.getRank())]++;
        }

        for (int cardValue : cardValues) {
            if (cardValue == TWO) {
                if (!isTwoPair(cards) && !isFullHouse(cards)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isHighCard(Set<Card> cards) {
        return !isRoyalFlush(cards) &&
                !isStraightFlush(cards) &&
                !isFourOfAKind(cards) &&
                !isFullHouse(cards) &&
                !isFlush(cards) &&
                !isStraight(cards) &&
                !isThreeOfAKind(cards) &&
                !isTwoPair(cards) &&
                !isOnePair(cards);
    }

    private boolean isHandInSequence(Set<Card> cards) {
        return FIVE == countSequential(cards);
    }

    private boolean suitsMatch(Set<Card> cards) {
        return doesHandContain(cards, cards.size(), Card::getSuit);
    }

    private int countSequential(Set<Card> cards) {

        Set<String> collect = cards.stream()
                .collect(HashSet::new, (set, card) -> set.add(card.getRank()), HashSet::addAll);

        if (collect.size() < cards.size()) {
            return ZERO;
        }

        if (SEQUENTIAL_BLOCKS.contains(collect)) {
            return FIVE;
        }

        return ZERO;
    }

    private boolean doesHandContain(Set<Card> cards, int countMatch, Function<Card, String> cardFn) {
        return groupByFunctionAndCount(cards, cardFn)
                .entrySet().stream()
                .anyMatch(anyGroupsOf(countMatch));
    }

    private Predicate<Map.Entry<String, Long>> anyGroupsOf(int count) {
        return entry -> entry.getValue() == count;
    }

    private Map<String, Long> groupByFunctionAndCount(Set<Card> cards, Function<Card, String> cardFn) {
        return cards.stream()
                .collect(Collectors.groupingBy(cardFn, Collectors.counting()));
    }
}
