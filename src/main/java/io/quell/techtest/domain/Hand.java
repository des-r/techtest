package io.quell.techtest.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hand {

    @ApiModelProperty(
            required = true,
            value = "Container for a hand of cards"
    )
    @NotEmpty
    @Size(min = 5, max = 5)
    Set<Card> hand;

    public List<String> getHandRanks() {
        return hand.stream()
                .map(Card::getRank)
                .collect(Collectors.toList());
    }

    public List<String> getHandSuits() {
        return hand.stream()
                .map(Card::getSuit)
                .collect(Collectors.toList());
    }
}
