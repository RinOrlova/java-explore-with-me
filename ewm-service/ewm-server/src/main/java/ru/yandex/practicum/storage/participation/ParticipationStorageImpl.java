package ru.yandex.practicum.storage.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.participation.AllParticipationRequestsResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.exceptions.EntityNotFoundException;
import ru.yandex.practicum.mapper.ParticipationMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipationStorageImpl implements ParticipationStorage {

    private final ParticipationRepository participationRepository;
    private final ParticipationMapper participationMapper;

    @Override
    @Transactional
    public ParticipationRequestResponse addDefaultParticipationRequest(Long userId, Long eventId) {
        ParticipationEntity participationEntity = participationMapper.mapDefaultUserEventParamsToEntity(userId, eventId);
        ParticipationEntity entityFromStorage = participationRepository.saveAndFlush(participationEntity);
        participationRepository.refresh(entityFromStorage);
        return participationMapper.mapEntityToParticipationRequestResponse(entityFromStorage);
    }

    @Override
    @Transactional
    public ParticipationRequestResponse addApprovedParticipationRequest(Long userId, Long eventId) {
        ParticipationEntity participationEntity = participationMapper.mapApprovedUserEventParamsToEntity(userId, eventId);
        ParticipationEntity entityFromStorage = participationRepository.saveAndFlush(participationEntity);
        participationRepository.refresh(entityFromStorage);
        return participationMapper.mapEntityToParticipationRequestResponse(entityFromStorage);
    }

    @Override
    public Collection<ParticipationRequestResponse> getAllRequestsForUserAndEventId(Long userId, Long eventId) {
        Collection<ParticipationEntity> allByRequesterIdAndEventId = participationRepository.findAllByUserInEvent(userId, eventId);
        return mapAllResults(allByRequesterIdAndEventId);
    }

    @Override
    public Collection<ParticipationRequestResponse> findAllRequestsForEventOwner(Long userId, Long eventId) {
        Collection<ParticipationEntity> allRequestsForEventOwner = participationRepository.findAllRequestsForEventOwner(userId, eventId);
        return mapAllResults(allRequestsForEventOwner);
    }

    @Override
    public ParticipationRequestResponse getRequestById(Long requestId) {
        return participationRepository.findById(requestId)
                .map(participationMapper::mapEntityToParticipationRequestResponse)
                .orElseThrow(() -> new EntityNotFoundException(requestId, ParticipationEntity.class));
    }

    @Override
    public Collection<ParticipationRequestResponse> getAllRequestsForUser(Long userId) {
        Collection<ParticipationEntity> allByRequesterId = participationRepository.findAllByRequesterIdAndEventInitiatorNotRequester(userId);
        return mapAllResults(allByRequesterId);
    }

    @Override
    @Transactional
    public ParticipationRequestResponse cancelRequest(Long requestId) {
        participationRepository.cancelRequestById(requestId);
        log.info("Request by id={} is confirmed.", requestId);
        Optional<ParticipationEntity> optParticipationEntity = participationRepository.findById(requestId);
        if (optParticipationEntity.isPresent()) {
            var participationEntity = optParticipationEntity.get();
            participationRepository.refresh(participationEntity);
            return participationMapper.mapEntityToParticipationRequestResponse(participationEntity);
        }
        throw new EntityNotFoundException(requestId, ParticipationEntity.class);
    }

    private Collection<ParticipationRequestResponse> mapAllResults(Collection<ParticipationEntity> entities) {
        return entities.stream()
                .map(participationMapper::mapEntityToParticipationRequestResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ParticipationRequestResponse> getAllRequestsByIds(Collection<Long> requestIds) {
        return mapAllResults(participationRepository.findAllById(requestIds));
    }

    @Override
    @Transactional
    public void declineAllRequests(Collection<Long> requestIds) {
        int declinedRequests = participationRepository.declineParticipationByIds(requestIds);
        requestIds.forEach(id -> {
            ParticipationEntity participationEntity = participationRepository.findById(id).get();
            participationRepository.refresh(participationEntity);
        });
        log.info("Number of requests declined: {}.", declinedRequests);
    }

    @Override
    @Transactional
    public void confirmAllRequests(List<Long> requestIds) {
        int declinedRequests = participationRepository.confirmParticipationByIds(requestIds);
        requestIds.forEach(id -> {
            ParticipationEntity participationEntity = participationRepository.findById(id).get();
            participationRepository.refresh(participationEntity);
        });
        log.info("Number of requests declined: {}.", declinedRequests);
    }

    @Override
    @Transactional
    public void declineAllPendingRequestsForEvent(Long eventId) {
        int declinedRequests = participationRepository.declineAllRequestsForEvent(eventId);
        log.info("Number of requests declined: {}.", declinedRequests);
    }

    @Override
    public AllParticipationRequestsResponse getAllParticipationRequestsResponsesForEvent(Long eventId) {
        AllParticipationRequestsResponse allParticipationRequestsResponse = AllParticipationRequestsResponse.builder().build();
        participationRepository.findAllByEventId(eventId)
                .stream()
                .map(participationMapper::mapEntityToParticipationRequestResponse)
                .collect(Collectors.groupingBy(ParticipationRequestResponse::getStatus))
                .forEach(allParticipationRequestsResponse::addRequests);
        return allParticipationRequestsResponse;
    }

    @Override
    public ConfirmedRequestsProjection getConfirmedRequestsForEvent(Long eventId) {
        return participationRepository.getConfirmedRequestsForEventNumber(eventId);
    }

    @Override
    public Map<Long, Long> getAllConfirmedRequestsNumber() {
        return participationRepository.getAllConfirmedRequestsNumber()
                .stream()
                .collect(Collectors.toMap(ConfirmedRequestsProjection::getEventId, ConfirmedRequestsProjection::getConfirmedRequestsNumber));
    }
}
