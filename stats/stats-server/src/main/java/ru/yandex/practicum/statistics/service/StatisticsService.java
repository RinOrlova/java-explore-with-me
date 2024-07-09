package ru.yandex.practicum.statistics.service;

import org.springframework.lang.Nullable;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.common.dto.StatisticsResponse;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatisticsService {

    void saveRequest(HitRequest hitRequest);

    Collection<StatisticsResponse> getStatistics(LocalDateTime start,
                                                 LocalDateTime end,
                                                 @Nullable List<String> uris,
                                                 boolean unique);
}
