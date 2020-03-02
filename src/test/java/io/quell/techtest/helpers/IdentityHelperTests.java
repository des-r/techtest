package io.quell.techtest.helpers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterAll;
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

    private static final AtomicInteger rfCount = new AtomicInteger(0);
    private static final AtomicInteger sfCount = new AtomicInteger(0);
    private static final AtomicInteger fourCount = new AtomicInteger(0);
    private static final AtomicInteger fhCount = new AtomicInteger(0);
    private static final AtomicInteger fCount = new AtomicInteger(0);
    private static final AtomicInteger sCount = new AtomicInteger(0);
    private static final AtomicInteger threeCount = new AtomicInteger(0);
    private static final AtomicInteger twoCount = new AtomicInteger(0);
    private static final AtomicInteger oneCount = new AtomicInteger(0);
    private static final AtomicInteger hcCount = new AtomicInteger(0);

    IdentityHelper classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new IdentityHelper();
    }

    @AfterAll
    static void tearDown() {
        System.out.println("DALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALE:" + rfCount);
        System.out.println(("DALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALE:" + sfCount));
        System.out.println(("DALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALE:" + fourCount));
        System.out.println(("DALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALE:" + fhCount));
        System.out.println(("DALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALE:" + fCount));
        System.out.println(("DALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALE:" + sCount));
        System.out.println(("DALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALE:" + threeCount));
        System.out.println(("DALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALE:" + twoCount));
        System.out.println(("DALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALE:" + oneCount));
        System.out.println(("DALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALEDALE:" + hcCount));
    }

    @Test
    void testIsRoyalFlush() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isRoyalFlush(hand.getHand());
                    rfCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(rfCount.get()).isEqualTo(MAX_ROYAL_FLUSH);
    }

    @Test
    void testIsStraightFlush() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isStraightFlush(hand.getHand());
                    sfCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(sfCount.get()).isEqualTo(MAX_STRAIGHT_FLUSH);
    }

    @Test
    void testIsFourOfAKind() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isFourOfAKind(hand.getHand());
                    fourCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(fourCount.get()).isEqualTo(MAX_FOUR_OF_A_KIND);
    }

    @Test
    void testIsFullHouse() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isFullHouse(hand.getHand());
                    fhCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(fhCount.get()).isEqualTo(MAX_FULL_HOUSE);
    }

    @Test
    void testIsFlush() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isFlush(hand.getHand());
                    fCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(fCount.get()).isEqualTo(MAX_FLUSH);
    }

    @Test
    void testIsStraight() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isStraight(hand.getHand());
                    sCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(sCount.get()).isEqualTo(MAX_STRAIGHT);
    }

    @Test
    void testIsThreeOfAKind() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isThreeOfAKind(hand.getHand());
                    threeCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(threeCount.get()).isEqualTo(MAX_THREE_OF_KIND);
    }

    @Test
    void testIsTwoPair() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isTwoPair(hand.getHand());
                    twoCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(twoCount.get()).isEqualTo(MAX_TWO_PAIR);
    }

    @Test
    void testIsOnePair() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isOnePair(hand.getHand());
                    oneCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(oneCount.get()).isEqualTo(MAX_ONE_PAIR);
    }

    @Test
    void testIsHighCard() {
        ALL_HANDS.parallelStream()
                .forEach(hand -> {
                    boolean result = classUnderTest.isHighCard(hand.getHand());
                    hcCount.addAndGet((result) ? 1 : 0);
                });

        assertThat(hcCount.get()).isEqualTo(MAX_HIGH_CARD);
    }
}