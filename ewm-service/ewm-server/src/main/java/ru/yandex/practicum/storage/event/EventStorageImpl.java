package ru.yandex.practicum.storage.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.enums.Sort;
import ru.yandex.practicum.exceptions.EventNotFoundException;
import ru.yandex.practicum.mapper.EventMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventStorageImpl implements EventStorage {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserEventViewRepository userEventViewRepository;

    @Override
    public Collection<EventShort> getEvents(String text,
                                            List<Integer> categoryId,
                                            Boolean paid,
                                            String rangeStart,
                                            String rangeEnd,
                                            boolean onlyAvailable,
                                            Sort sort,
                                            Integer from,
                                            Integer size) {
        return null;
    }

    @Override
    public EventFull getEventByCreatorAndId(Long userId, Long eventId) {
        EventEntity eventEntity = eventRepository.findByIdAndInitiatorId(eventId, userId);
        return eventMapper.mapToEventFull(eventEntity);
    }

    @Override
    public EventShort getEventShortById(Long id) {
        Optional<EventEntity> eventFromStorage = eventRepository.findById(id);
        if (eventFromStorage.isPresent()) {
            EventEntity eventEntity = eventFromStorage.get();
            setViewsToEventEntity(eventEntity);
            setConfirmedRequestsToEventEntity(eventEntity);
            return eventMapper.mapToEventShort(eventEntity);
        }
        throw new EventNotFoundException(id);
    }

    @Override
    public EventFull getEventFullById(Long id) {
        Optional<EventEntity> eventFromStorage = eventRepository.findById(id);
        if (eventFromStorage.isPresent()) {
            EventEntity eventEntity = eventFromStorage.get();
            setViewsToEventEntity(eventEntity);
            setConfirmedRequestsToEventEntity(eventEntity);
            return eventMapper.mapToEventFull(eventEntity);
        }
        throw new EventNotFoundException(id);
    }

    private void setViewsToEventEntity(EventEntity entity) {
        long viewForEvent = getViewForEvent(entity);
        entity.setViews(viewForEvent);
    }

    private long getViewForEvent(EventEntity entity) {
        return userEventViewRepository.countByEvent(entity);
    }

    private void setConfirmedRequestsToEventEntity(EventEntity entity) {
        long confirmedRequests = getConfirmedRequests(entity);
        entity.setConfirmedRequests(confirmedRequests);
    }

    private long getConfirmedRequests(EventEntity entity){
        return entity.getParticipationRequests()
                .stream()
                .filter(participationEntity -> isConfirmedStatus(participationEntity.getStatus()))
                .count();

    }

    private boolean isConfirmedStatus(String status){
        return "CONFIRMED".equals(status);
    }


}
