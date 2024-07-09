package ru.yandex.practicum.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.common.dto.StatisticsResponse;
import ru.yandex.practicum.statistics.exceptions.InvalidDateRequestedException;
import ru.yandex.practicum.statistics.storage.StatisticsStorage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsStorage statisticsStorage;

    @Override
    public void saveRequest(HitRequest hitRequest) {
        statisticsStorage.addHitRequest(hitRequest);
    }

    @Override
    public Collection<StatisticsResponse> getStatistics(LocalDateTime start,
                                                        LocalDateTime end,
                                                        @Nullable List<String> uris,
                                                        boolean unique) {
        validateInputDateTimeParams(start, end);
        return getStatisticsFromStorage(start, end, uris, unique);
    }

    private Collection<StatisticsResponse> getStatisticsFromStorage(LocalDateTime start,
                                                                    LocalDateTime end,
                                                                    @Nullable List<String> uris,
                                                                    boolean unique) {
        if (uris == null || uris.isEmpty()) {
            return statisticsStorage.getAllStatistics(start, end, unique);
        }
        return statisticsStorage.getStatisticsByURIs(start, end, uris, unique);
    }

    private void validateInputDateTimeParams(LocalDateTime start,
                                             LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new InvalidDateRequestedException(start, end);
        }
    }
}
