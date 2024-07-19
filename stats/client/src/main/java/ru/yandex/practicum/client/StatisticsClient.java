package ru.yandex.practicum.client;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.yandex.practicum.client.utils.ClientConstants;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.common.dto.StatisticsResponse;
import ru.yandex.practicum.common.utils.ApiPathConstants;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatisticsClient {

    private final WebClient webClient;
    @Value("${statistics.service.url}")
    private String baseUrl;
    private final DateTimeEncoder dateTimeEncoder;

    public void saveRequest(HitRequest hitRequest) {
        webClient.post()
                .uri(baseUrl + ApiPathConstants.HIT_PATH)
                .bodyValue(hitRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Collection<StatisticsResponse> getAllStatistics(@NonNull LocalDateTime start,
                                                           @NonNull LocalDateTime end) {
        return getStatistics(start, end, null, null);
    }

    public Collection<StatisticsResponse> getStatistics(@NonNull LocalDateTime start,
                                                        @NonNull LocalDateTime end,
                                                        @Nullable List<String> uris,
                                                        @Nullable Boolean unique) {
        String startParam = dateTimeEncoder.encodeDateTime(start);
        String endParam = dateTimeEncoder.encodeDateTime(end);

        StringBuilder url = new StringBuilder(baseUrl
                + ApiPathConstants.STATS_PATH
                + ClientConstants.START_REQUEST_PARAM + startParam
                + ClientConstants.END_REQUEST_PARAM + endParam);
        if (uris != null && !uris.isEmpty()) {
            url.append(ClientConstants.URIS_REQUEST_PARAM).append(String.join(",", uris));
        }
        if (unique != null) {
            url.append(ClientConstants.UNIQUE_REQUEST_PARAM).append(unique);
        }
        return webClient.get()
                .uri(url.toString())
                .retrieve()
                .bodyToFlux(StatisticsResponse.class)
                .collectList()
                .block();
    }
}

