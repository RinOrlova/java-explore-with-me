package ru.yandex.practicum.statistics.mapper;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.statistics.storage.HitEntity;
import ru.yandex.practicum.statistics.utils.TestConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HitMapperTest {

    private final HitMapper hitMapper = new HitMapperImpl();

    @Test
    void test_mapHitRequest() {
        HitRequest hitRequest = new HitRequest();
        hitRequest.setApp("app");
        hitRequest.setIp("127.0.0.1");
        hitRequest.setUri("/uri");
        hitRequest.setTimestamp(TestConstants.defaultTimestamp);

        HitEntity hitEntity = hitMapper.map(hitRequest);
        assertEquals(hitRequest.getApp(), hitEntity.getApp());
        assertEquals(hitRequest.getIp(), hitEntity.getIp());
        assertEquals(hitRequest.getUri(), hitEntity.getUri());
        assertEquals(hitRequest.getTimestamp(), hitEntity.getTimestamp());
        assertNull(hitEntity.getId());
    }

    @Test
    void negative_mapHitRequest_nullInput() {
        HitEntity hitEntity = hitMapper.map(null);
        assertNull(hitEntity);
    }
}