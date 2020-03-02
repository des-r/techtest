package io.quell.techtest.helpers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static io.quell.techtest.AppConfig.ALL_HANDS;
import static org.assertj.core.api.Assertions.assertThat;

@FieldDefaults(level = AccessLevel.PRIVATE)
class IdentityHelperTests {

    public static final int MAX_ROYAL_FLUSH = 4;
    public static final int MAX_STRAIGHT_FLUSH = 36;
    public static final int MAX_FOUR_OF_A_KIND = 624;
    public static final int MAX_FULL_HOUSE = 3744;
    public static final int MAX_FLUSH = 5108;
    public static final int MAX_STRAIGHT = 10_200;
    public static final int MAX_THREE_OF_KIND = 54_912;
    public static final int MAX_TWO_PAIR = 123_552;
    public static final int MAX_ONE_PAIR = 1_098_240;
    public static final int MAX_HIGH_CARD = 1_302_540;

    private static final AtomicInteger royalFlushCount = new AtomicInteger(0);
    private static final AtomicInteger straightFlush = new AtomicInteger(0);
    private static final AtomicInteger fourOfAKindCount = new AtomicInteger(0);
    private static final AtomicInteger fullHouseCount = new AtomicInteger(0);
    private static final AtomicInteger flushCount = new AtomicInteger(0);
    private static final AtomicInteger straightCount = new AtomicInteger(0);
    private static final AtomicInteger threeOfAKindCount = new AtomicInteger(0);
    private static final AtomicInteger twoPairCount = new AtomicInteger(0);
    private static final AtomicInteger onePairCount = new AtomicInteger(0);
    private static final AtomicInteger highCardCount = new AtomicInteger(0);

    IdentityHelper classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new IdentityHelper();
    }

    @Test
    void testIsRoyalFlush() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isRoyalFlush(hand.getHand());
                    royalFlushCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(royalFlushCount.get()).isEqualTo(MAX_ROYAL_FLUSH);
    }

    @Test
    void testIsStraightFlush() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isStraightFlush(hand.getHand());
                    straightFlush.addAndGet((result) ? 1 : 0);
                });

        assertThat(straightFlush.get()).isEqualTo(MAX_STRAIGHT_FLUSH);
    }

    @Test
    void testIsFourOfAKind() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isFourOfAKind(hand.getHand());
                    fourOfAKindCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(fourOfAKindCount.get()).isEqualTo(MAX_FOUR_OF_A_KIND);
    }

    @Test
    void testIsFullHouse() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isFullHouse(hand.getHand());
                    fullHouseCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(fullHouseCount.get()).isEqualTo(MAX_FULL_HOUSE);
    }

    @Test
    void testIsFlush() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isFlush(hand.getHand());
                    flushCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(flushCount.get()).isEqualTo(MAX_FLUSH);
    }

    @Test
    void testIsStraight() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isStraight(hand.getHand());
                    straightCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(straightCount.get()).isEqualTo(MAX_STRAIGHT);
    }

    @Test
    void testIsThreeOfAKind() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isThreeOfAKind(hand.getHand());
                    threeOfAKindCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(threeOfAKindCount.get()).isEqualTo(MAX_THREE_OF_KIND);
    }

    @Test
    void testIsTwoPair() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isTwoPair(hand.getHand());
                    twoPairCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(twoPairCount.get()).isEqualTo(MAX_TWO_PAIR);
    }

    @Test
    void testIsOnePair() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isOnePair(hand.getHand());
                    onePairCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(onePairCount.get()).isEqualTo(MAX_ONE_PAIR);
    }

    @Test
    void testIsHighCard() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isHighCard(hand.getHand());
                    highCardCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(highCardCount.get()).isEqualTo(MAX_HIGH_CARD);
    }
}