package ru.yandex.practicum.storage.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventRequest;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.dto.participation.ParticipationRequestStatus;
import ru.yandex.practicum.dto.search.AdminSearch;
import ru.yandex.practicum.dto.search.PublicSearch;
import ru.yandex.practicum.exceptions.EventNotFoundException;
import ru.yandex.practicum.mapper.EventMapper;
import ru.yandex.practicum.storage.location.LocationEntity;
import ru.yandex.practicum.storage.location.LocationStorage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventStorageImpl implements EventStorage {

    private final EventRepository eventRepository;
    private final EventSearchRepository eventSearchRepository;
    private final EventMapper eventMapper;
    private final LocationStorage locationStorage;

    @Override
    public Collection<EventShort> searchEventsPublic(PublicSearch publicSearch) {
        PageRequest pageRequest = PageRequest.of(
                publicSearch.getFrom(),
                publicSearch.getSize(),
                getSortingType(publicSearch)
        );
        return eventSearchRepository.findAllByPublicSearchParams(publicSearch, pageRequest)
                .stream()
                .filter(entity -> freePlacesFilter(entity, publicSearch))
                .map(eventMapper::mapToEventFull)
                .collect(Collectors.toList());
    }

    private boolean freePlacesFilter(EventEntity entity, PublicSearch publicSearch) {
        if (publicSearch.isOnlyAvailable()) {
            return anyFreePlacesLeft(entity.getId());
        }
        return true;
    }

    private Sort getSortingType(PublicSearch publicSearch) {
        if (publicSearch.getSort() != null) {
            switch (publicSearch.getSort()) {
                case VIEWS:
                    return Sort.by(Sort.Direction.DESC, "views");
                case EVENT_DATE:
                    return Sort.by(Sort.Direction.DESC, "eventDate");
            }
        }
        return Sort.by(Sort.Direction.ASC, "id");
    }

    @Override
    public Collection<EventFull> searchEvents(AdminSearch adminSearch) {
        PageRequest pageRequest = PageRequest.of(
                adminSearch.getFrom(),
                adminSearch.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );
        return eventSearchRepository.findAllByAdminSearchParams(adminSearch, pageRequest)
                .stream()
                .map(eventMapper::mapToEventFull)
                .collect(Collectors.toList());
    }

    @Override
    public EventFull getEventByCreatorAndId(Long userId, Long eventId) {
        EventEntity eventEntity = eventRepository.findByIdAndInitiatorId(eventId, userId);
        return eventMapper.mapToEventFull(eventEntity);
    }

    @Override
    public List<EventShort> getEventByCreator(Long userId, int from, int size) {
        PageRequest pageRequest = PageRequest.of(
                from,
                size,
                Sort.by(Sort.Direction.ASC, "id")
        );
        Page<EventEntity> eventEntityPage = eventRepository.findByInitiatorId(userId, pageRequest);
        return eventEntityPage.stream()
                .map(eventMapper::mapToEventShort)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFull updateEvent(EventFull updatedEvent, Long userId) {
        LocationEntity locationEntity = locationStorage.addLocationEntityIfAbsent(updatedEvent.getLocation());
        EventEntity eventEntity = eventMapper.eventFullToEventEntity(updatedEvent, userId);
        eventEntity.setLocation(locationEntity);
        EventEntity eventFromStorage = eventRepository.saveAndFlush(eventEntity);
        eventRepository.refresh(eventFromStorage);
        return eventMapper.mapToEventFull(eventFromStorage);
    }

    @Override
    @Transactional
    public EventFull updateEventAdmin(EventFull updatedEvent) {
        EventEntity eventEntity = eventMapper.mapEventFullToEventEntity(updatedEvent);
        EventEntity eventFromStorage = eventRepository.saveAndFlush(eventEntity);
        eventRepository.refresh(eventFromStorage);
        return eventMapper.mapToEventFull(eventFromStorage);
    }

    @Override
    @Transactional
    public EventFull addEvent(EventRequest eventRequest, Long userId) {
        LocationEntity locationEntity = locationStorage.addLocationEntityIfAbsent(eventRequest.getLocation());
        EventEntity eventEntity = eventMapper.eventRequestToEventEntity(eventRequest, userId);
        eventEntity.setLocation(locationEntity);
        eventEntity.setCreatedOn(LocalDateTime.now());
        EventEntity eventFromStorage = eventRepository.saveAndFlush(eventEntity);
        eventRepository.refresh(eventFromStorage);
        return eventMapper.mapToEventFull(eventFromStorage);
    }

    @Override
    public boolean isUserOwnerOfEvent(Long userId, Long eventId) {
        return eventRepository.findById(eventId)
                .map(eventEntity -> eventEntity.getInitiator().getId().equals(userId))
                .orElse(false);
    }

    @Override
    public boolean anyFreePlacesLeft(Long eventId) {
        Optional<EventEntity> optEventEntity = eventRepository.findById(eventId);
        if (optEventEntity.isPresent()) {
            EventEntity eventEntity = optEventEntity.get();
            if (eventEntity.getParticipantLimit() == 0) {
                return true;
            } else {
                return eventRepository.canAcceptMoreParticipants(eventId);
            }
        }
        return false;
    }

    @Override
    public EventShort getEventShortById(Long id) {
        Optional<EventEntity> eventFromStorage = eventRepository.findById(id);
        if (eventFromStorage.isPresent()) {
            EventEntity eventEntity = eventFromStorage.get();
            setConfirmedRequestsToEventEntity(eventEntity);
            return eventMapper.mapToEventShort(eventEntity);
        }
        throw new EventNotFoundException(id);
    }

    @Override
    @Transactional
    public EventFull getEventFullPublishedById(Long id) {
        return Optional.ofNullable(eventRepository.findByIdAndStatusPublished(id))
                .map(entity -> getEventFull(entity, true))
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    @Override
    @Transactional
    public EventFull getEventFullById(Long id) {
        return eventRepository.findById(id)
                .map(entity -> getEventFull(entity, false))
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    private EventFull getEventFull(EventEntity eventFromStorage, boolean shouldIncreaseViews) {
        long views = eventFromStorage.getViews();
        if (shouldIncreaseViews) {
            eventFromStorage.setViews(views + 1);
            eventRepository.saveAndFlush(eventFromStorage);
            eventRepository.refresh(eventFromStorage);
        }
        setConfirmedRequestsToEventEntity(eventFromStorage);
        return eventMapper.mapToEventFull(eventFromStorage);
    }

    private void setConfirmedRequestsToEventEntity(EventEntity entity) {
        long confirmedRequests = getConfirmedRequests(entity);
        entity.setConfirmedRequests(confirmedRequests);
    }

    private long getConfirmedRequests(EventEntity entity) {
        return entity.getParticipationRequests()
                .stream()
                .filter(participationEntity -> ParticipationRequestStatus.CONFIRMED == participationEntity.getStatus())
                .count();

    }

}
