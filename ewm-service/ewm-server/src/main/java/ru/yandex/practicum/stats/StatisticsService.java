package ru.yandex.practicum.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.client.StatisticsClient;
import ru.yandex.practicum.common.dto.HitRequest;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StatisticsService {

    private static final String APPLICATION_NAME = "ewm-service";

    private final StatisticsClient statisticsClient;

    public void sendHitRequest(String requestedUri, String remoteAddress) {
        HitRequest hitRequest = createHitRequest(requestedUri, remoteAddress);
        statisticsClient.saveRequest(hitRequest);
    }

    private HitRequest createHitRequest(String requestedUri, String remoteAddress) {
        HitRequest hitRequest = new HitRequest();
        hitRequest.setApp(APPLICATION_NAME);
        hitRequest.setUri(requestedUri);
        hitRequest.setIp(remoteAddress);
        hitRequest.setTimestamp(LocalDateTime.now());
        return hitRequest;
    }

}
