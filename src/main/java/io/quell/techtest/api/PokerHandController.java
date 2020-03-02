package io.quell.techtest.api;

import com.google.common.collect.Sets;
import io.quell.techtest.domain.Card;
import io.quell.techtest.domain.Hand;
import io.quell.techtest.domain.Hands;
import io.quell.techtest.helpers.PayloadValidator;
import io.quell.techtest.services.IdentificationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad payload"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseStatus(HttpStatus.OK)
public class PokerHandController {

    public static final String DELIMITER = " ";
    public static final String TEXT_CSV = "text/csv";
    public static final String UNABLE_TO_PROCESS_UPLOADED_FILE_OF_HANDS = "Unable to process uploaded file of hands";
    public static final String NULL_OR_INVALID_PAYLOAD = "Null or invalid payload";

    IdentificationService identificationService;

    @ApiOperation(
            value = "Upload a single 5 card poker hand",
            notes = "Returns the phrase meaning of the supplied poker hand"
    )
    @PostMapping(path = "/hand", consumes = MediaType.TEXT_PLAIN_VALUE)
    public Map<Hand, String> hand(@RequestBody String payload) {
        return Optional.ofNullable(payload)
                .map(cards -> {
                    Set<Card> cardValues = buildCards(cards);
                    final Hand hand = new Hand(cardValues);
                    final Hands hands = buildHands(hand);
                    PayloadValidator.validate(hands);
                    return process(hands);
                }).orElseThrow(() -> new RuntimeException(NULL_OR_INVALID_PAYLOAD));
    }

    @ApiOperation(
            value = "Upload a file of 5 card poker hands",
            notes = "Returns the phrase meaning of each poker hand in the supplied file"
    )
    @SneakyThrows
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<Hand, String> upload(@RequestPart MultipartFile file) {
        return Optional.ofNullable(file)
                .filter(PokerHandController::isPlainText)
                .map(this::buildHands)
                .map(hands -> {
                    PayloadValidator.validate(hands);
                    return hands;
                })
                .map(this::process)
                .orElseThrow(() -> new RuntimeException(NULL_OR_INVALID_PAYLOAD));
    }

    private Map<Hand, String> process(Hands hands) {
        return identificationService.identify(hands);
    }

    private Hands buildHands(Hand hand) {
        return new Hands(Collections.singletonList(hand));
    }

    private Hands buildHands(MultipartFile file) {
        String payload = null;
        List<Hand> handsAdt = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            while (null != (payload = reader.readLine())) {
                Set<Card> cards = buildCards(payload);
                handsAdt.add(new Hand(cards));
            }
            final Hands hands = new Hands(handsAdt);
            PayloadValidator.validate(hands);
            return hands;
        } catch (IOException e) {
            log.info(UNABLE_TO_PROCESS_UPLOADED_FILE_OF_HANDS + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Set<Card> buildCards(String cards) {
        return Sets.newHashSet(cards.split(DELIMITER)).stream()
                .map(Card::new)
                .collect(Collectors.toSet());
    }

    private static boolean isPlainText(MultipartFile file) {
        return TEXT_CSV.equalsIgnoreCase(file.getContentType());
    }
}


