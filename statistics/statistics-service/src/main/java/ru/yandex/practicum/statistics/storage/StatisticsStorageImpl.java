package ru.yandex.practicum.statistics.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.common.dto.StatisticsResponse;
import ru.yandex.practicum.statistics.mapper.HitMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsStorageImpl implements StatisticsStorage {

    private final HitRepository hitRepository;
    private final HitMapper hitMapper;

    @Override
    public void addHitRequest(HitRequest hitRequest) {
        HitEntity hitEntity = hitMapper.map(hitRequest);
        hitRepository.saveAndFlush(hitEntity);
    }

    @Override
    public List<StatisticsResponse> getAllStatistics(LocalDateTime start, LocalDateTime end, boolean unique){
        return Collections.emptyList();
    }

    @Override
    public List<StatisticsResponse> getStatisticsByURIs(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique){
        return Collections.emptyList();
    }
}
