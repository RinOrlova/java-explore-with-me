package ru.yandex.practicum.statistics.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.common.dto.StatisticsResponse;
import ru.yandex.practicum.common.utils.ApiPathConstants;
import ru.yandex.practicum.common.utils.StatisticsConstants;
import ru.yandex.practicum.statistics.service.DataTimeParamsDecoder;
import ru.yandex.practicum.statistics.service.StatisticsService;
import ru.yandex.practicum.statistics.utils.TestConstants;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatisticsController.class)
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatisticsService statisticsService;

    @SpyBean
    private DataTimeParamsDecoder dataTimeParamsDecoder;

    @Test
    void saveRequest() throws Exception {
        HitRequest hitRequest = new HitRequest();
        hitRequest.setApp("app");
        hitRequest.setIp("127.0.0.1");
        hitRequest.setUri("/uri");
        hitRequest.setTimestamp(TestConstants.defaultTimestamp);

        mockMvc.perform(post(ApiPathConstants.HIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hitRequest)))
                .andExpect(status().isCreated());

        verify(statisticsService, times(1)).saveRequest(hitRequest);
    }

    @Test
    void getStatistics_nonGrouped() throws Exception {
        String urlTemplate = new StringBuilder()
                .append(ApiPathConstants.STATS_PATH)
                .append("?start=")
                .append(encodeDateForRequestURI(TestConstants.defaultTimestamp))
                .append("&end=")
                .append(encodeDateForRequestURI(TestConstants.defaultTimestamp.plusSeconds(1)))
                .toString();
        Collection<StatisticsResponse> statistics = List.of(
                new StatisticsResponse("app", "/uri", 1L),
                new StatisticsResponse("app", "/uri1", 2L)
        );
        when(statisticsService.getStatistics(any(), any(), any(), anyBoolean()))
                .thenReturn(statistics);

        String statisticsResponseJson = mockMvc.perform(get(urlTemplate))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<StatisticsResponse> statisticsResponses = objectMapper.readValue(statisticsResponseJson, new TypeReference<>() {
        });
        assertEquals(statistics, statisticsResponses);

        verify(statisticsService, times(1)).getStatistics(TestConstants.defaultTimestamp, TestConstants.defaultTimestamp.plusSeconds(1), null, false);
    }

    @Test
    void getStatistics_grouped() throws Exception {
        String urlTemplate = new StringBuilder()
                .append(ApiPathConstants.STATS_PATH)
                .append("?start=")
                .append(encodeDateForRequestURI(TestConstants.defaultTimestamp))
                .append("&end=")
                .append(encodeDateForRequestURI(TestConstants.defaultTimestamp.plusSeconds(1)))
                .append("&uris=")
                .append("/uri1,/uri2")
                .append("&unique=true")
                .toString();
        Collection<StatisticsResponse> statistics = List.of(
                new StatisticsResponse("app", "/uri1", 1L),
                new StatisticsResponse("app", "/uri2", 2L)
        );
        when(statisticsService.getStatistics(TestConstants.defaultTimestamp, TestConstants.defaultTimestamp.plusSeconds(1), List.of("/uri1", "/uri2"), true))
                .thenReturn(statistics);

        String statisticsResponseJson = mockMvc.perform(get(urlTemplate))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<StatisticsResponse> statisticsResponses = objectMapper.readValue(statisticsResponseJson, new TypeReference<>() {
        });
        assertEquals(statistics, statisticsResponses);

        verify(statisticsService, times(1)).getStatistics(TestConstants.defaultTimestamp, TestConstants.defaultTimestamp.plusSeconds(1), List.of("/uri1", "/uri2"), true);
    }

    private static String encodeDateForRequestURI(LocalDateTime localDateTime) {
        String formattedDateTime = localDateTime.format(StatisticsConstants.DATE_TIME_FORMATTER);
        return URLEncoder.encode(formattedDateTime, StandardCharsets.UTF_8);
    }
}