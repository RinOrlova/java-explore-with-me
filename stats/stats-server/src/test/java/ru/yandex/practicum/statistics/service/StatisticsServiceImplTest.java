package ru.yandex.practicum.statistics.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.statistics.exceptions.InvalidDateRequestedException;
import ru.yandex.practicum.statistics.storage.StatisticsStorage;
import ru.yandex.practicum.statistics.utils.TestConstants;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {

    @InjectMocks
    private StatisticsServiceImpl subject;

    @Mock
    private StatisticsStorage statisticsStorage;

    @Test
    void saveRequest() {
        HitRequest hitRequest = new HitRequest();
        hitRequest.setApp("app");
        hitRequest.setUri("/uri");
        hitRequest.setIp("127.0.0.1");
        hitRequest.setTimestamp(TestConstants.DEFAULT_TIMESTAMP);

        subject.saveRequest(hitRequest);

        verify(statisticsStorage, times(1)).addHitRequest(hitRequest);
        verify(statisticsStorage, never()).getStatisticsByURIs(any(), any(), any(), anyBoolean());
        verify(statisticsStorage, never()).getAllStatistics(any(), any(), anyBoolean());
    }

    @Test
    void positive_getStatistics_nullableURIs() {
        LocalDateTime end = TestConstants.DEFAULT_TIMESTAMP.plusSeconds(1);
        LocalDateTime start = TestConstants.DEFAULT_TIMESTAMP.minusSeconds(1);
        when(statisticsStorage.getAllStatistics(any(LocalDateTime.class), any(LocalDateTime.class), anyBoolean()))
                .thenReturn(emptyList());

        subject.getStatistics(start, end, null, false);
        subject.getStatistics(start, end, null, true);

        verify(statisticsStorage, times(1)).getAllStatistics(start, end, false);
        verify(statisticsStorage, times(1)).getAllStatistics(start, end, true);
        verify(statisticsStorage, never()).getStatisticsByURIs(any(), any(), any(), anyBoolean());
    }

    @Test
    void positive_getStatisticsByURIs_nonEmptyURIs() {
        LocalDateTime end = TestConstants.DEFAULT_TIMESTAMP.plusSeconds(1);
        LocalDateTime start = TestConstants.DEFAULT_TIMESTAMP.minusSeconds(1);
        List<String> uris = List.of("/uri", "/uri2");
        when(statisticsStorage.getStatisticsByURIs(any(LocalDateTime.class), any(LocalDateTime.class), anyList(), anyBoolean()))
                .thenReturn(emptyList());

        subject.getStatistics(start, end, uris, false);
        subject.getStatistics(start, end, uris, true);

        verify(statisticsStorage, times(1)).getStatisticsByURIs(start, end, uris, false);
        verify(statisticsStorage, times(1)).getStatisticsByURIs(start, end, uris, true);
        verify(statisticsStorage, never()).getAllStatistics(any(), any(), anyBoolean());
    }

    @Test
    void negative_getStatisticsByURIs_emptyURIs() {
        LocalDateTime end = TestConstants.DEFAULT_TIMESTAMP.plusSeconds(1);
        LocalDateTime start = TestConstants.DEFAULT_TIMESTAMP.minusSeconds(1);
        List<String> uris = emptyList();
        when(statisticsStorage.getAllStatistics(any(LocalDateTime.class), any(LocalDateTime.class), anyBoolean()))
                .thenReturn(emptyList());

        subject.getStatistics(start, end, uris, false);
        subject.getStatistics(start, end, uris, true);

        verify(statisticsStorage, times(1)).getAllStatistics(start, end, false);
        verify(statisticsStorage, times(1)).getAllStatistics(start, end, true);
        verify(statisticsStorage, never()).getStatisticsByURIs(any(), any(), any(), anyBoolean());

    }

    @Test
    void negative_invalidInputDateParams() {
        LocalDateTime endParam = TestConstants.DEFAULT_TIMESTAMP.minusSeconds(1);
        LocalDateTime startParam = TestConstants.DEFAULT_TIMESTAMP.plusSeconds(1);
        assertThrows(InvalidDateRequestedException.class, () -> subject.getStatistics(startParam, endParam, null, false));
        assertThrows(InvalidDateRequestedException.class, () -> subject.getStatistics(startParam, endParam, null, true));
        assertThrows(InvalidDateRequestedException.class, () -> subject.getStatistics(startParam, endParam, emptyList(), true));
        assertThrows(InvalidDateRequestedException.class, () -> subject.getStatistics(startParam, endParam, List.of("/uri"), true));
    }
}