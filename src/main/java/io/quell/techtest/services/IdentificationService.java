package io.quell.techtest.services;

import io.quell.techtest.domain.Hand;
import io.quell.techtest.domain.Hands;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentificationService {

    Function<Hand, String> identify;

    public Map<Hand, String> identify(Hands hands) {
        return hands.getHands().stream()
                .collect(
                        HashMap::new,
                        (hashMap, hand) -> hashMap.put(hand, identify.apply(hand)),
                        HashMap::putAll
                );
    }
}
