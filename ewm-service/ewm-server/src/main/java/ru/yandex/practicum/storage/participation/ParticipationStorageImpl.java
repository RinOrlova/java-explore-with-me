package ru.yandex.practicum.storage.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.mapper.ParticipationMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipationStorageImpl implements ParticipationStorage {

    private final ParticipationRepository participationRepository;
    private final ParticipationMapper participationMapper;
}
