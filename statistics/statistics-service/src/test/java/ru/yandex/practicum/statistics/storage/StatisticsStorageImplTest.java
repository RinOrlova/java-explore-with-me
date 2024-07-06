package ru.yandex.practicum.statistics.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.common.dto.StatisticsResponse;
import ru.yandex.practicum.statistics.mapper.HitMapper;
import ru.yandex.practicum.statistics.utils.TestConstants;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class StatisticsStorageImplTest {

    @Autowired
    private HitRepository hitRepository;

    private final HitMapper hitMapper = Mappers.getMapper(HitMapper.class);

    private StatisticsStorageImpl statisticsStorage;

    @BeforeEach
    void setUp() {
        statisticsStorage = new StatisticsStorageImpl(hitRepository, hitMapper);
    }


    @Test
    void test_addHitRequest() {
        HitRequest hitRequest = new HitRequest();
        hitRequest.setApp("app");
        hitRequest.setUri("/uri");
        hitRequest.setIp("127.0.0.1");
        hitRequest.setTimestamp(TestConstants.defaultTimestamp);

        statisticsStorage.addHitRequest(hitRequest);

        List<HitEntity> hits = hitRepository.findAll();
        assertEquals(1, hits.size());
        HitEntity hitEntity = hits.get(0);
        assertEquals("app", hitEntity.getApp());
        assertEquals("/uri", hitEntity.getUri());
        assertEquals("127.0.0.1", hitEntity.getIp());
        assertEquals(TestConstants.defaultTimestamp, hitEntity.getTimestamp());
    }

    @Test
    void testGetAllStatistics_sameApp_differentIPs_differentURIs() {
        // Add setup data to the repository
        HitEntity hit1 = new HitEntity();
        hit1.setApp("app1");
        hit1.setUri("/uri1");
        hit1.setIp("127.0.0.1");
        hit1.setTimestamp(LocalDateTime.parse("2024-12-12T00:00:00"));

        HitEntity hit2 = new HitEntity();
        hit2.setApp("app1");
        hit2.setUri("/uri1");
        hit2.setIp("127.0.0.2");
        hit2.setTimestamp(LocalDateTime.parse("2024-12-12T00:00:00"));

        hitRepository.saveAll(List.of(hit1, hit2));

        List<StatisticsResponse> statistics = statisticsStorage.getAllStatistics(
                LocalDateTime.parse("2024-12-11T00:00:00"),
                LocalDateTime.parse("2024-12-13T00:00:00"),
                true);

        assertEquals(1, statistics.size());
        StatisticsResponse response = statistics.get(0);
        assertEquals("app1", response.getApp());
        assertEquals("/uri1", response.getUri());
        assertEquals(2, response.getHits());
    }

    @Test
    void testGetStatisticsByURIs_sameApp_sameIPs_differentURIs() {
        HitEntity hit1 = new HitEntity();
        hit1.setApp("app");
        hit1.setUri("/uri1");
        hit1.setIp("127.0.0.1");
        hit1.setTimestamp(LocalDateTime.parse("2024-12-12T00:00:00"));

        HitEntity hit2 = new HitEntity();
        hit2.setApp("app");
        hit2.setUri("/uri2");
        hit2.setIp("127.0.0.1");
        hit2.setTimestamp(LocalDateTime.parse("2024-12-12T00:00:00"));

        hitRepository.saveAll(List.of(hit1, hit2));

        List<StatisticsResponse> statistics = statisticsStorage.getStatisticsByURIs(
                LocalDateTime.parse("2024-12-11T23:59:59"),
                LocalDateTime.parse("2024-12-12T00:00:01"),
                List.of("/uri1", "/uri2"),
                true);

        assertEquals(2, statistics.size());
        assertEquals("/uri1", statistics.get(0).getUri());
        assertEquals(1L, statistics.get(0).getHits());
        assertEquals("/uri2", statistics.get(1).getUri());
        assertEquals(1L, statistics.get(1).getHits());
    }
}