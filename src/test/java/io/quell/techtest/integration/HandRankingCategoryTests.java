package io.quell.techtest.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedInputStream;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith({OutputCaptureExtension.class})
class HandRankingCategoryTests {

    @Autowired
    WebApplicationContext webAppContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webAppContext)
                .build();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/singleHand.csv")
    void testGivenASingleHand_WhenAMatchIsFound_ReturnTheCorrectResponse(String csvHand) throws Exception {

        mockMvc.perform(post("/hand")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(csvHand))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..*", is(Collections.singletonList("Straight flush"))));
    }

    @Test
    void testGivenMultipleHands_WhenMatchesAreFound_ReturnTheCorrectResponses() throws Exception {

        ClassPathResource classPathResource = new ClassPathResource("multiHands.csv");
        BufferedInputStream contentStream = new BufferedInputStream(classPathResource.getInputStream());
        MockMultipartFile file = new MockMultipartFile("file", "multiHands.csv", "text/csv", contentStream);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..*", containsInRelativeOrder("One pair", "Royal flush", "Two pair", "flush", "Four of a kind", "Full house")));
    }

    @Test
    void testGivenASingleHand_WhenNoMatchIsFound_ReturnTheCorrectResponse(CapturedOutput output) throws Exception {

        mockMvc.perform(post("/hand")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content("2X 3X 4X 5X 6X" + System.lineSeparator()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"path\":\"/hand\",\"status\":400,\"message\":\"Bad Request\"}"));

        assertThat(output.toString()).contains("Exception caught by the Error Controller");
        assertThat(output.toString()).contains("Card field: suit failed due to violation: must match \"[HDCS]\"");
    }

    @Test
    void testGivenMultipleHands_WhenAHandDoesNotMatch_ReturnTheCorrectResponse(CapturedOutput output) throws Exception {
        final String badPayload = "2X 3Y 4Z 5D 6D";
        final String twoPair = "2D 2S 2C 2H 6D";

        final byte[] payload = (badPayload + System.lineSeparator() + twoPair + System.lineSeparator()).getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "someMulti.csv", "text/csv", payload);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"path\":\"/upload\",\"status\":400,\"message\":\"Bad Request\"}"));

        assertThat(output.toString()).contains("Exception caught by the Error Controller");
        assertThat(output.toString()).contains("Card field: suit failed due to violation: must match \"[HDCS]\"");
    }

    @Test
        // TODO - system currently fails out on first hand error. Would be useful to add per hand processing...
    void testGivenMultipleHands_WhenNoMatchesAreFound_ReturnTheCorrectResponses() {
        //given 2 hands that do not match any valid poker hands
        //when I send the hands to the system
        //then I receive the correct responses
    }

    @Test
        // TODO - system currently fails out on first hand error. Would be useful to add per hand processing and report on any valid hands
    void testGivenMultipleHand_WhenSomeMatchesAreFound_ReturnTheCorrectResponses() {
        //given 2 hands that do not match any valid poker hands
        //and 2 hands that do match any valid poker hands
        //when I send the hands to the system
        //then I receive the correct responses
    }
}
