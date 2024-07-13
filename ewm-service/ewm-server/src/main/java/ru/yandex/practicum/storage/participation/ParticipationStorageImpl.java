package ru.yandex.practicum.storage.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.participation.AllParticipationRequestsResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestStatus;
import ru.yandex.practicum.exceptions.ParticipationNotFoundException;
import ru.yandex.practicum.mapper.ParticipationMapper;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipationStorageImpl implements ParticipationStorage {

    private final ParticipationRepository participationRepository;
    private final ParticipationMapper participationMapper;

    @Override
    public ParticipationRequestResponse addDefaultParticipationRequest(Long userId, Long eventId) {
        ParticipationEntity participationEntity = participationMapper.mapDefaultUserEventParamsToEntity(userId, eventId);
        ParticipationEntity entityFromStorage = participationRepository.saveAndFlush(participationEntity);
        return participationMapper.mapEntityToParticipationRequestResponse(entityFromStorage);
    }

    @Override
    public ParticipationRequestResponse addApprovedParticipationRequest(Long userId, Long eventId) {
        ParticipationEntity participationEntity = participationMapper.mapApprovedUserEventParamsToEntity(userId, eventId);
        ParticipationEntity entityFromStorage = participationRepository.saveAndFlush(participationEntity);
        return participationMapper.mapEntityToParticipationRequestResponse(entityFromStorage);
    }

    @Override
    public Collection<ParticipationRequestResponse> getAllRequestsForUserAndEventId(Long userId, Long eventId) {
        Collection<ParticipationEntity> allByRequesterIdAndEventId = participationRepository.findAllByRequesterIdAndEventId(userId, eventId);
        return mapAllResults(allByRequesterIdAndEventId);
    }

    @Override
    public ParticipationRequestResponse getRequestById(Long requestId) {
        return participationRepository.findById(requestId)
                .map(participationMapper::mapEntityToParticipationRequestResponse)
                .orElseThrow(() -> new ParticipationNotFoundException(requestId));
    }

    @Override
    public Collection<ParticipationRequestResponse> findAllRequestsByIds(Collection<Long> requestIds) {
        Collection<ParticipationEntity> allById = participationRepository.findAllById(requestIds);
        return mapAllResults(allById);
    }

    @Override
    public Collection<ParticipationRequestResponse> getAllRequestsForUser(Long userId) {
        Collection<ParticipationEntity> allByRequesterId = participationRepository.findAllByRequesterIdAndEventInitiatorNotRequester(userId);
        return mapAllResults(allByRequesterId);
    }

    @Override
    public ParticipationRequestResponse cancelRequest(Long requestById) {
        participationRepository.cancelRequestById(requestById);
        log.info("Request by id={} is confirmed.", requestById);
        return null;
    }

    private Collection<ParticipationRequestResponse> mapAllResults(Collection<ParticipationEntity> entities) {
        return entities.stream()
                .map(participationMapper::mapEntityToParticipationRequestResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isRequestPresentInStatusPending(Long id) {
        return participationRepository.findById(id)
                .map(request -> request.getStatus() == ParticipationRequestStatus.PENDING)
                .orElse(false);
    }

    @Override
    public void declineAllRequests(Collection<Long> requestIds) {
        int declinedRequests = participationRepository.declineParticipationByIds(requestIds);
        log.info("Number of requests declined: {}.", declinedRequests);
    }

    @Override
    public void declineAllPendingRequestsForEvent(Long eventId) {
        int declinedRequests = participationRepository.declineAllRequestsForEvent(eventId);
        log.info("Number of requests declined: {}.", declinedRequests);
    }

    @Override
    public void confirmRequest(Long id) {
        participationRepository.confirmRequestById(id);
        log.info("Request by id={} is confirmed.", id);
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
}
