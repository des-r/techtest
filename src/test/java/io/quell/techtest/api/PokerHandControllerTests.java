package io.quell.techtest.api;

import io.quell.techtest.domain.Card;
import io.quell.techtest.domain.Hand;
import io.quell.techtest.domain.Hands;
import io.quell.techtest.services.IdentificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class PokerHandControllerTests {

    @Mock
    IdentificationService identificationServiceMock;

    @InjectMocks
    PokerHandController classUnderTest;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(classUnderTest)
                .build();
    }

    @Test
    void testSingleHand() throws Exception {
        Set<Card> hand = Stream.of("2D 3D 4D 5D 6D").map(Card::new).collect(Collectors.toSet());

        when(identificationServiceMock.identify(any(Hands.class))).thenReturn(Collections.singletonMap(new Hand(hand), "Straight flush"));

        mockMvc.perform(post("/hand")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content("2D 3D 4D 5D 6D" + System.lineSeparator()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..*", is(Collections.singletonList("Straight flush"))));
    }

    @Test
    void testSingleHandWhenPassingBadPayload(CapturedOutput output) {
        assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/hand")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content("BAD PAYLOAD"))
        );

        assertThat(output.toString()).contains("java.lang.RuntimeException: Invalid hand values");
        assertThat(output.toString()).contains("Hand field: hand failed due to violation: size must be between 5 and 5");
    }

    @Test
    void testUpload() throws Exception {
        final String straightFlush = "2D 3D 4D 5D 6D";
        final String twoPair = "2D 2S 2C 2H 6D";

        Set<Card> straightFlushHand = Stream.of(straightFlush.split(" ")).map(Card::new).collect(Collectors.toSet());
        Set<Card> twoPairHand = Stream.of(twoPair.split(" ")).map(Card::new).collect(Collectors.toSet());

        final byte[] payload = (straightFlush + System.lineSeparator() + twoPair + System.lineSeparator()).getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "some.csv", "text/csv", payload);

        Map<Hand, String> response = new HashMap<Hand, String>() {{
            put(new Hand(straightFlushHand), "Straight flush");
            put(new Hand(twoPairHand), "Two pair");
        }};

        when(identificationServiceMock.identify(any(Hands.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..*", containsInAnyOrder("Straight flush", "Two pair")));
    }

    @Test
    void testUploadWhenPassingBadPayload(CapturedOutput output) {
        final String badPayload = "2X 3Y 4Z 5D 6D";
        final String twoPair = "2D 2S 2C 2H 6D";

        final byte[] payload = (badPayload + System.lineSeparator() + twoPair + System.lineSeparator()).getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "some.csv", "text/csv", payload);

        assertThrows(NestedServletException.class,
                () -> mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file))
        );

        assertThat(output.toString()).contains("java.lang.RuntimeException: Invalid card values");
        assertThat(output.toString()).contains("Card field: suit failed due to violation: must match \"[HDCS]\"");
    }

    @Test
    void testUploadWhenPassingBadPayloadSize(CapturedOutput output) {
        final String badSizedPayload = "2D 3D 4D";
        final String twoPair = "2D 2S 2C 2H 6D";

        final byte[] payload = (badSizedPayload + System.lineSeparator() + twoPair + System.lineSeparator()).getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "some.csv", "text/csv", payload);

        assertThrows(NestedServletException.class,
                () -> mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file))
        );

        assertThat(output.toString()).contains("java.lang.RuntimeException: Invalid hand values");
        assertThat(output.toString()).contains("Hand field: hand failed due to violation: size must be between 5 and 5");
    }
}