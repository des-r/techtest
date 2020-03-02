package io.quell.techtest.config;

import io.quell.techtest.constants.HandType;
import io.quell.techtest.domain.Hand;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceConfig {

    @Bean
    Function<Hand, String> identify(List<Predicate<Hand>> identifyHands) {
        final HandType[] values = HandType.values();

        return (hand) -> {
            Integer integer = Stream.iterate(0, n -> n + 1).limit(identifyHands.size())
                    .filter(p -> identifyHands.get(p).test(hand))
                    .findFirst()
                    .orElse(identifyHands.size() - 1);

            return values[integer].getHandType();
        };
    }

    @Bean
    List<Predicate<Hand>> identifyHands() {
        return Stream.of(HandType.values())
                .collect(ArrayList::new,
                        (list, handType) -> list.add(handType.ordinal(), handType.identifyHand()),
                        ArrayList::addAll
                );
    }
}
