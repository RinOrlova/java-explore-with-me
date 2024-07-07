package ru.yandex.practicum.statistics.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.common.dto.StatisticsResponse;
import ru.yandex.practicum.statistics.utils.TestConstants;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class StatisticsStorageTest {

    @Autowired
    private HitRepository hitRepository;

    @Test
    void test_addHitRequest() {
        HitEntity hitEntity = getDefaultHitEntity();
        HitEntity savedEntity = hitRepository.save(hitEntity);
        Long savedEntityId = savedEntity.getId();

        Optional<HitEntity> optHitEntity = hitRepository.findById(savedEntityId);
        assertTrue(optHitEntity.isPresent());
        HitEntity hitEntityFromStorage = optHitEntity.get();
        assertEquals("app", hitEntityFromStorage.getApp());
        assertEquals("/uri", hitEntityFromStorage.getUri());
        assertEquals("127.0.0.1", hitEntityFromStorage.getIp());
        assertEquals(TestConstants.defaultTimestamp, hitEntityFromStorage.getTimestamp());
    }

    @Test
    void test_findAllStatistics_timeRangeCoversHitTimestamp_singleHit() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<Object[]> allStatistics = hitRepository.findAllStatistics(timestampBefore, timestampAfter, false);
        assertEquals(1, allStatistics.size());
        Object[] statistics = allStatistics.get(0);
        StatisticsResponse statisticsResponse = new StatisticsResponse((String) statistics[0], (String) statistics[1], ((Number) statistics[2]).longValue());
        assertEquals("app", statisticsResponse.getApp());
        assertEquals("/uri", statisticsResponse.getUri());
        assertEquals(1L, statisticsResponse.getHits());
    }

    @Test
    void test_findAllStatistics_timeRangeCoversHitTimestamp_multipleHits_nonGrouped() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);
        HitEntity hitEntity2 = getDefaultHitEntity();
        hitRepository.save(hitEntity2);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<Object[]> allStatistics = hitRepository.findAllStatistics(timestampBefore, timestampAfter, false);
        assertEquals(1, allStatistics.size());
        Object[] statistics = allStatistics.get(0);
        StatisticsResponse statisticsResponse = new StatisticsResponse((String) statistics[0], (String) statistics[1], ((Number) statistics[2]).longValue());
        assertEquals("app", statisticsResponse.getApp());
        assertEquals("/uri", statisticsResponse.getUri());
        assertEquals(2L, statisticsResponse.getHits());
    }

    @Test
    void test_findAllStatistics_timeRangeCoversHitTimestamp_multipleHits_grouped() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);
        HitEntity hitEntity2 = getDefaultHitEntity();
        hitRepository.save(hitEntity2);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<Object[]> allStatistics = hitRepository.findAllStatistics(timestampBefore, timestampAfter, true);
        assertEquals(1, allStatistics.size());
        Object[] statistics = allStatistics.get(0);
        StatisticsResponse statisticsResponse = new StatisticsResponse((String) statistics[0], (String) statistics[1], ((Number) statistics[2]).longValue());
        assertEquals("app", statisticsResponse.getApp());
        assertEquals("/uri", statisticsResponse.getUri());
        assertEquals(1L, statisticsResponse.getHits());
    }

    @Test
    void test_findAllStatistics_timeRangeCoversHitTimestamp_multipleHits_differentIPs_grouped() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);
        HitEntity hitEntity2 = getDefaultHitEntity();
        hitEntity2.setIp("127.0.0.2");
        hitRepository.save(hitEntity2);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<Object[]> allStatistics = hitRepository.findAllStatistics(timestampBefore, timestampAfter, true);
        assertEquals(1, allStatistics.size());
        Object[] statistics = allStatistics.get(0);
        StatisticsResponse statisticsResponse = new StatisticsResponse((String) statistics[0], (String) statistics[1], ((Number) statistics[2]).longValue());
        assertEquals("app", statisticsResponse.getApp());
        assertEquals("/uri", statisticsResponse.getUri());
        assertEquals(2L, statisticsResponse.getHits());
    }

    @Test
    void test_findAllStatistics_timeRangeCoversHitTimestamp_multipleHits_differentApps_grouped() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);
        HitEntity hitEntity2 = getDefaultHitEntity();
        hitEntity2.setApp("app2");
        hitRepository.save(hitEntity2);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<Object[]> allStatistics = hitRepository.findAllStatistics(timestampBefore, timestampAfter, true);
        assertEquals(2, allStatistics.size());
        List<StatisticsResponse> statisticsResponses = allStatistics.stream()
                .map(statistics -> new StatisticsResponse((String) statistics[0], (String) statistics[1], ((Number) statistics[2]).longValue()))
                .sorted(Comparator.comparing(StatisticsResponse::getApp))
                .collect(Collectors.toList());
        StatisticsResponse statisticsResponse1 = statisticsResponses.get(0);
        assertEquals("app", statisticsResponse1.getApp());
        assertEquals("/uri", statisticsResponse1.getUri());
        assertEquals(1L, statisticsResponse1.getHits());
        StatisticsResponse statisticsResponse2 = statisticsResponses.get(1);
        assertEquals("app2", statisticsResponse2.getApp());
        assertEquals("/uri", statisticsResponse2.getUri());
        assertEquals(1L, statisticsResponse2.getHits());
    }

    @Test
    void test_findAllStatistics_timeRangeCoversHitTimestamp_multipleHits_differentURIs_grouped() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);
        HitEntity hitEntity2 = getDefaultHitEntity();
        hitEntity2.setUri("/uri2");
        hitRepository.save(hitEntity2);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<Object[]> allStatistics = hitRepository.findAllStatistics(timestampBefore, timestampAfter, true);
        assertEquals(2, allStatistics.size());
        List<StatisticsResponse> statisticsResponses = allStatistics.stream()
                .map(statistics -> new StatisticsResponse((String) statistics[0], (String) statistics[1], ((Number) statistics[2]).longValue()))
                .sorted(Comparator.comparing(StatisticsResponse::getApp))
                .collect(Collectors.toList());
        StatisticsResponse statisticsResponse1 = statisticsResponses.get(0);
        assertEquals("app", statisticsResponse1.getApp());
        assertEquals("/uri", statisticsResponse1.getUri());
        assertEquals(1L, statisticsResponse1.getHits());
        StatisticsResponse statisticsResponse2 = statisticsResponses.get(1);
        assertEquals("app", statisticsResponse2.getApp());
        assertEquals("/uri2", statisticsResponse2.getUri());
        assertEquals(1L, statisticsResponse2.getHits());
    }

    @Test
    void negative_test_findAllStatistics_timeRangeIsBeforeHitTimestamp() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(2);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.minusSeconds(1);

        List<Object[]> allStatisticsNonGrouped = hitRepository.findAllStatistics(timestampBefore, timestampAfter, false);
        assertTrue(allStatisticsNonGrouped.isEmpty());
        List<Object[]> allStatisticsGrouped = hitRepository.findAllStatistics(timestampBefore, timestampAfter, true);
        assertTrue(allStatisticsGrouped.isEmpty());
    }

    @Test
    void negative_test_findAllStatistics_timeRangeIsAfterHitTimestamp() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.plusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(2);

        List<Object[]> allStatisticsNonGrouped = hitRepository.findAllStatistics(timestampBefore, timestampAfter, false);
        assertTrue(allStatisticsNonGrouped.isEmpty());
        List<Object[]> allStatisticsGrouped = hitRepository.findAllStatistics(timestampBefore, timestampAfter, true);
        assertTrue(allStatisticsGrouped.isEmpty());
    }

    @Test
    void test_findStatisticsByURIs_timeRangeCoversHitTimestamp_singleHit() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<String> uris = List.of("/uri", "/non_existing_uri");
        List<Object[]> allStatistics = hitRepository.findStatisticsByURIs(timestampBefore, timestampAfter, uris, false);
        assertEquals(1, allStatistics.size());
        Object[] statistics = allStatistics.get(0);
        StatisticsResponse statisticsResponse = new StatisticsResponse((String) statistics[0], (String) statistics[1], ((Number) statistics[2]).longValue());
        assertEquals("app", statisticsResponse.getApp());
        assertEquals("/uri", statisticsResponse.getUri());
        assertEquals(1L, statisticsResponse.getHits());
    }

    @Test
    void test_findStatisticsByURIs_timeRangeCoversHitTimestamp_multipleHits_nonGrouped() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);
        HitEntity hitEntity2 = getDefaultHitEntity();
        hitRepository.save(hitEntity2);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<String> uris = List.of("/uri", "/non_existing_uri");
        List<Object[]> allStatistics = hitRepository.findStatisticsByURIs(timestampBefore, timestampAfter, uris, false);
        assertEquals(1, allStatistics.size());
        Object[] statistics = allStatistics.get(0);
        StatisticsResponse statisticsResponse = new StatisticsResponse((String) statistics[0], (String) statistics[1], ((Number) statistics[2]).longValue());
        assertEquals("app", statisticsResponse.getApp());
        assertEquals("/uri", statisticsResponse.getUri());
        assertEquals(2L, statisticsResponse.getHits());
    }

    @Test
    void test_findStatisticsByURIs_timeRangeCoversHitTimestamp_multipleHits_grouped() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);
        HitEntity hitEntity2 = getDefaultHitEntity();
        hitRepository.save(hitEntity2);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<String> uris = List.of("/uri", "/non_existing_uri");
        List<Object[]> allStatistics = hitRepository.findStatisticsByURIs(timestampBefore, timestampAfter, uris, true);
        assertEquals(1, allStatistics.size());
        Object[] statistics = allStatistics.get(0);
        StatisticsResponse statisticsResponse = new StatisticsResponse((String) statistics[0], (String) statistics[1], ((Number) statistics[2]).longValue());
        assertEquals("app", statisticsResponse.getApp());
        assertEquals("/uri", statisticsResponse.getUri());
        assertEquals(1L, statisticsResponse.getHits());
    }

    @Test
    void test_findStatisticsByURIs_timeRangeCoversHitTimestamp_multipleHits_grouped_differentIPs() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);
        HitEntity hitEntity2 = getDefaultHitEntity();
        hitEntity2.setIp("127.0.0.2");
        hitRepository.save(hitEntity2);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<String> uris = List.of("/uri", "/non_existing_uri");
        List<Object[]> allStatistics = hitRepository.findStatisticsByURIs(timestampBefore, timestampAfter, uris, true);
        assertEquals(1, allStatistics.size());
        Object[] statistics = allStatistics.get(0);
        StatisticsResponse statisticsResponse = new StatisticsResponse((String) statistics[0], (String) statistics[1], ((Number) statistics[2]).longValue());
        assertEquals("app", statisticsResponse.getApp());
        assertEquals("/uri", statisticsResponse.getUri());
        assertEquals(2L, statisticsResponse.getHits());
    }

    @Test
    void test_findStatisticsByURIs_timeRangeCoversHitTimestamp_multipleHits_grouped_differentApps() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);
        HitEntity hitEntity2 = getDefaultHitEntity();
        hitEntity2.setApp("app2");
        hitRepository.save(hitEntity2);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<String> uris = List.of("/uri");
        List<Object[]> allStatistics = hitRepository.findStatisticsByURIs(timestampBefore, timestampAfter, uris, true);
        assertEquals(2, allStatistics.size());
        List<StatisticsResponse> statisticsResponses = allStatistics.stream()
                .map(statistics -> new StatisticsResponse((String) statistics[0], (String) statistics[1], ((Number) statistics[2]).longValue()))
                .sorted(Comparator.comparing(StatisticsResponse::getApp))
                .collect(Collectors.toList());
        StatisticsResponse statisticsResponse1 = statisticsResponses.get(0);
        assertEquals("app", statisticsResponse1.getApp());
        assertEquals("/uri", statisticsResponse1.getUri());
        assertEquals(1L, statisticsResponse1.getHits());
        StatisticsResponse statisticsResponse2 = statisticsResponses.get(1);
        assertEquals("app2", statisticsResponse2.getApp());
        assertEquals("/uri", statisticsResponse2.getUri());
        assertEquals(1L, statisticsResponse2.getHits());
    }

    @Test
    void test_findStatisticsByURIs_timeRangeCoversHitTimestamp_multipleHits_grouped_differentURIs() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);
        HitEntity hitEntity2 = getDefaultHitEntity();
        hitEntity2.setUri("/uri2");
        hitRepository.save(hitEntity2);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<String> uris = List.of("/uri", "/uri2");
        List<Object[]> allStatistics = hitRepository.findStatisticsByURIs(timestampBefore, timestampAfter, uris, true);
        assertEquals(2, allStatistics.size());
        List<StatisticsResponse> statisticsResponses = allStatistics.stream()
                .map(statistics -> new StatisticsResponse((String) statistics[0], (String) statistics[1], ((Number) statistics[2]).longValue()))
                .sorted(Comparator.comparing(StatisticsResponse::getUri))
                .collect(Collectors.toList());
        StatisticsResponse statisticsResponse1 = statisticsResponses.get(0);
        assertEquals("app", statisticsResponse1.getApp());
        assertEquals("/uri", statisticsResponse1.getUri());
        assertEquals(1L, statisticsResponse1.getHits());
        StatisticsResponse statisticsResponse2 = statisticsResponses.get(1);
        assertEquals("app", statisticsResponse2.getApp());
        assertEquals("/uri2", statisticsResponse2.getUri());
        assertEquals(1L, statisticsResponse2.getHits());
    }

    @Test
    void negative_test_findStatisticsByURIs_timeRangeCoversHitTimestamp_notFoundByURI_nonGrouped() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(1);

        List<String> uris = List.of("/non_existing_uri");
        List<Object[]> allStatisticsNonGrouped = hitRepository.findStatisticsByURIs(timestampBefore, timestampAfter, uris, false);
        assertTrue(allStatisticsNonGrouped.isEmpty());
        List<Object[]> allStatisticsGrouped = hitRepository.findStatisticsByURIs(timestampBefore, timestampAfter, uris, true);
        assertTrue(allStatisticsGrouped.isEmpty());
    }

    @Test
    void negative_test_findStatisticsByURIs_timeRangeIsBeforeHitTimestamp() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.minusSeconds(2);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.minusSeconds(1);

        List<String> uris = List.of("/uri");
        List<Object[]> allStatisticsNonGrouped = hitRepository.findStatisticsByURIs(timestampBefore, timestampAfter, uris, false);
        assertTrue(allStatisticsNonGrouped.isEmpty());
        List<Object[]> allStatisticsGrouped = hitRepository.findStatisticsByURIs(timestampBefore, timestampAfter, uris, true);
        assertTrue(allStatisticsGrouped.isEmpty());
    }

    @Test
    void negative_test_findStatisticsByURIs_timeRangeIsAfterHitTimestamp() {
        HitEntity hitEntity = getDefaultHitEntity();
        hitRepository.save(hitEntity);

        LocalDateTime timestampBefore = TestConstants.defaultTimestamp.plusSeconds(1);
        LocalDateTime timestampAfter = TestConstants.defaultTimestamp.plusSeconds(2);

        List<String> uris = List.of("/uri");
        List<Object[]> allStatisticsNonGrouped = hitRepository.findStatisticsByURIs(timestampBefore, timestampAfter, uris, false);
        assertTrue(allStatisticsNonGrouped.isEmpty());
        List<Object[]> allStatisticsGrouped = hitRepository.findStatisticsByURIs(timestampBefore, timestampAfter, uris, true);
        assertTrue(allStatisticsGrouped.isEmpty());

    }

    private static HitEntity getDefaultHitEntity() {
        HitEntity hitEntity = new HitEntity();
        hitEntity.setApp("app");
        hitEntity.setUri("/uri");
        hitEntity.setIp("127.0.0.1");
        hitEntity.setTimestamp(TestConstants.defaultTimestamp);
        return hitEntity;
    }
}