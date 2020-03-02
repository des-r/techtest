package io.quell.techtest.helpers;

import io.quell.techtest.domain.Card;
import io.quell.techtest.domain.Hand;
import io.quell.techtest.domain.Hands;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Slf4j
public class PayloadValidator {

    public static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    public static final Validator validator = factory.getValidator();

    public static void validate(Card card) {
        Set<ConstraintViolation<Card>> violations = validator.validate(card);
        if (violations.size() > 0) {
            for (ConstraintViolation<Card> violation : violations) {
                log.error("Card field: " + violation.getPropertyPath() + " failed due to violation: " + violation.getMessage());
            }
            throw new RuntimeException("Invalid card values");
        }
    }

    public static void validate(Hand hand) {
        Set<ConstraintViolation<Hand>> violations = validator.validate(hand);
        if (violations.size() > 0) {
            for (ConstraintViolation<Hand> violation : violations) {
                log.error("Hand field: " + violation.getPropertyPath() + " failed due to violation: " + violation.getMessage());
            }
            throw new RuntimeException("Invalid hand values");
        }

        hand.getHand().forEach(PayloadValidator::validate);
    }

    public static void validate(Hands hands) {
        Set<ConstraintViolation<Hands>> violations = validator.validate(hands);
        if (violations.size() > 0) {
            for (ConstraintViolation<Hands> violation : violations) {
                log.error("Hands field: " + violation.getPropertyPath() + " failed due to violation: " + violation.getMessage());
            }
            throw new RuntimeException("Invalid hands values");
        }

        hands.getHands().forEach(PayloadValidator::validate);
    }
}
