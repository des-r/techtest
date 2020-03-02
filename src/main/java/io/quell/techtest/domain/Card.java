package io.quell.techtest.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {

    @ApiModelProperty(
            required = true,
            value = "Card rank value"
    )
    @Pattern(regexp = "[2-9TJQKA]")
    @Size(min = 1, max = 1)
    String rank;

    @ApiModelProperty(
            required = true,
            value = "Card suit value"
    )
    @Pattern(regexp = "[HDCS]")
    @Size(min = 1, max = 1)
    String suit;

    public Card(String value) {
        this.rank = value.substring(0, 1);
        this.suit = value.substring(1, 2);
    }
}
