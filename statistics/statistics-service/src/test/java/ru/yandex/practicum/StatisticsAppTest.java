package ru.yandex.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.common.dto.StatisticsResponse;
import ru.yandex.practicum.common.utils.ApiPathConstants;
import ru.yandex.practicum.common.utils.StatisticsConstants;
import ru.yandex.practicum.statistics.utils.TestConstants;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/application.properties")
class StatisticsAppTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test_addHit_getStats() throws Exception {
        HitRequest hitRequest = new HitRequest();
        hitRequest.setApp("app");
        hitRequest.setIp("127.0.0.1");
        hitRequest.setUri("/uri");
        hitRequest.setTimestamp(TestConstants.defaultTimestamp);

        // Add hit from the same IP twice
        mockMvc.perform(post(ApiPathConstants.HIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hitRequest)))
                .andExpect(status().isCreated());
        mockMvc.perform(post(ApiPathConstants.HIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hitRequest)))
                .andExpect(status().isCreated());

        // Get statistics for non-unique IPs
        String getStatisticsUrl = new StringBuilder()
                .append(ApiPathConstants.STATS_PATH)
                .append("?start=").append(encodeDateForRequestURI(TestConstants.defaultTimestamp.minusSeconds(1)))
                .append("&end=").append(encodeDateForRequestURI(TestConstants.defaultTimestamp.plusSeconds(1)))
                .append("&uris=").append("/uri")
                .toString();

        String statisticsResponseJson = mockMvc.perform(get(getStatisticsUrl))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<StatisticsResponse> statisticsResponses = objectMapper.readValue(statisticsResponseJson, new TypeReference<>() {
        });
        assertEquals(1, statisticsResponses.size());
        StatisticsResponse statisticsResponse = statisticsResponses.iterator().next();
        assertEquals("app", statisticsResponse.getApp());
        assertEquals("/uri", statisticsResponse.getUri());
        assertEquals(2L, statisticsResponse.getHits());

        // Get statistics for unique IPs
        String getStatisticsUrlUnique = new StringBuilder()
                .append(ApiPathConstants.STATS_PATH)
                .append("?start=").append(encodeDateForRequestURI(TestConstants.defaultTimestamp.minusSeconds(1)))
                .append("&end=").append(encodeDateForRequestURI(TestConstants.defaultTimestamp.plusSeconds(1)))
                .append("&uris=").append("/uri")
                .append("&unique=").append(true)
                .toString();

        String statisticsResponseJsonUnique = mockMvc.perform(get(getStatisticsUrlUnique))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<StatisticsResponse> statisticsResponsesUnique = objectMapper.readValue(statisticsResponseJsonUnique, new TypeReference<>() {
        });
        assertEquals(1, statisticsResponsesUnique.size());
        StatisticsResponse statisticsResponseUnique = statisticsResponsesUnique.iterator().next();
        assertEquals("app", statisticsResponseUnique.getApp());
        assertEquals("/uri", statisticsResponseUnique.getUri());
        assertEquals(1L, statisticsResponseUnique.getHits());

        // Get statistics - not found by uri
        String getStatisticsUrlNotFoundByUri = new StringBuilder()
                .append(ApiPathConstants.STATS_PATH)
                .append("?start=").append(encodeDateForRequestURI(TestConstants.defaultTimestamp.minusSeconds(1)))
                .append("&end=").append(encodeDateForRequestURI(TestConstants.defaultTimestamp.plusSeconds(1)))
                .append("&uris=").append("/nonExistingUri")
                .toString();

        String statisticsResponseJsonNotFoundByUri = mockMvc.perform(get(getStatisticsUrlNotFoundByUri))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Collection<StatisticsResponse> statisticsResponsesNotFoundByUri = objectMapper.readValue(statisticsResponseJsonNotFoundByUri, new TypeReference<>() {
        });
        assertTrue(statisticsResponsesNotFoundByUri.isEmpty());

        // Get statistics - not found by dateTime
        String getStatisticsUrlNotFoundByDateTime = new StringBuilder()
                .append(ApiPathConstants.STATS_PATH)
                .append("?start=").append(encodeDateForRequestURI(TestConstants.defaultTimestamp.minusSeconds(2)))
                .append("&end=").append(encodeDateForRequestURI(TestConstants.defaultTimestamp.minusSeconds(1)))
                .append("&uris=").append("/uri")
                .toString();

        String statisticsResponseJsonNotFoundByDateTime = mockMvc.perform(get(getStatisticsUrlNotFoundByDateTime))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Collection<StatisticsResponse> statisticsResponsesNotFoundByDateTime = objectMapper.readValue(statisticsResponseJsonNotFoundByDateTime, new TypeReference<>() {
        });
        assertTrue(statisticsResponsesNotFoundByDateTime.isEmpty());
    }

    private static String encodeDateForRequestURI(LocalDateTime localDateTime) {
        String formattedDateTime = localDateTime.format(StatisticsConstants.DATE_TIME_FORMATTER);
        return URLEncoder.encode(formattedDateTime, StandardCharsets.UTF_8);
    }
}