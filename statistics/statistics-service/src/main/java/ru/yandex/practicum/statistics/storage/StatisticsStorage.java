package ru.yandex.practicum.statistics.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.common.dto.StatisticsResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatisticsStorage {

    void addHitRequest(HitRequest hitRequest);

    List<StatisticsResponse> getAllStatistics(LocalDateTime start, LocalDateTime end, boolean unique);

    List<StatisticsResponse> getStatisticsByURIs(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
