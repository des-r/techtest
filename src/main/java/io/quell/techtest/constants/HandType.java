package io.quell.techtest.constants;

import io.quell.techtest.domain.Hand;
import io.quell.techtest.helpers.IdentityHelper;

import java.util.function.Predicate;

public enum HandType {

    ROYAL_FLUSH {
        @Override
        public String getHandType() {
            return "Royal flush";
        }

        @Override
        public Predicate<Hand> identifyHand() {
            return (hand) -> delegate.isRoyalFlush(hand.getHand());
        }
    },
    STRAIGHT_FLUSH {
        @Override
        public String getHandType() {
            return "Straight flush";
        }

        @Override
        public Predicate<Hand> identifyHand() {
            return (hand) -> delegate.isStraightFlush(hand.getHand());
        }
    },
    FOUR_OF_A_KIND {
        @Override
        public String getHandType() {
            return "Four of a kind";
        }

        @Override
        public Predicate<Hand> identifyHand() {
            return (hand) -> delegate.isFourOfAKind(hand.getHand());
        }
    },
    FULL_HOUSE {
        @Override
        public String getHandType() {
            return "Full house";
        }

        @Override
        public Predicate<Hand> identifyHand() {
            return (hand) -> delegate.isFullHouse(hand.getHand());
        }
    },
    FLUSH {
        @Override
        public String getHandType() {
            return "flush";
        }

        @Override
        public Predicate<Hand> identifyHand() {
            return (hand) -> delegate.isFlush(hand.getHand());
        }
    },
    STRAIGHT {
        @Override
        public String getHandType() {
            return "Straight";
        }

        @Override
        public Predicate<Hand> identifyHand() {
            return (hand) -> delegate.isStraight(hand.getHand());
        }
    },
    THREE_OF_A_KIND {
        @Override
        public String getHandType() {
            return "Three of a kind";
        }

        @Override
        public Predicate<Hand> identifyHand() {
            return (hand) -> delegate.isThreeOfAKind(hand.getHand());
        }
    },
    TWO_PAIR {
        @Override
        public String getHandType() {
            return "Two pair";
        }

        @Override
        public Predicate<Hand> identifyHand() {
            return (hand) -> delegate.isTwoPair(hand.getHand());
        }
    },
    ONE_PAIR {
        @Override
        public String getHandType() {
            return "One pair";
        }

        @Override
        public Predicate<Hand> identifyHand() {
            return (hand) -> delegate.isOnePair(hand.getHand());
        }
    },
    HIGH_CARD {
        @Override
        public String getHandType() {
            return "High card";
        }

        @Override
        public Predicate<Hand> identifyHand() {
            return (hand) -> delegate.isHighCard(hand.getHand());
        }
    };

    private static final IdentityHelper delegate = new IdentityHelper();

    public abstract String getHandType();

    public abstract Predicate<Hand> identifyHand();
}
