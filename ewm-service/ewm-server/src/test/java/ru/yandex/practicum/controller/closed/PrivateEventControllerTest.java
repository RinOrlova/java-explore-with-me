package ru.yandex.practicum.controller.closed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventRequest;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.dto.event.UpdateEventRequest;
import ru.yandex.practicum.dto.location.Location;
import ru.yandex.practicum.dto.participation.AllParticipationRequestsResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestStatus;
import ru.yandex.practicum.dto.participation.ParticipationStatusUpdateRequest;
import ru.yandex.practicum.service.event.EventService;
import ru.yandex.practicum.service.participation.ParticipationService;
import ru.yandex.practicum.stats.StatisticsService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.utils.ApiPathConstants.EVENT_PATH;
import static ru.yandex.practicum.utils.ApiPathConstants.USERS_PATH;

@WebMvcTest(controllers = {PrivateEventController.class})
class PrivateEventControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EventService eventService;
    @MockBean
    private ParticipationService participationService;
    @MockBean
    private StatisticsService statisticsService;

    @Test
    void test_addEvent() throws Exception {
        Location location = getLocation();

        EventRequest eventRequest = EventRequest.builder()
                .annotation("AnnotationAnnotationAnnotationAnnotation")
                .description("DescriptionDescriptionDescriptionDescription")
                .eventDate(LocalDateTime.now().plusDays(1))
                .category(1L)
                .paid(true)
                .title("Title")
                .location(location)
                .requestModeration(false)
                .participantLimit(1000)
                .build();

        EventFull eventFull = EventFull.builder().build();
        when(eventService.addEvent(1L, eventRequest))
                .thenReturn(eventFull);
        mockMvc.perform(post(USERS_PATH + "/1" + EVENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isCreated());
        verify(eventService, times(1)).addEvent(1L, eventRequest);
    }

    @Test
    void test_addEvent_swaggerData() throws Exception {
        String jsonRequest = "{\n" +
                "  \"annotation\": \"Сплав на байдарках похож на полет.\",\n" +
                "  \"category\": 2,\n" +
                "  \"description\": \"Сплав на байдарках похож на полет. На спокойной воде — это парение. На бурной, порожистой — выполнение фигур высшего пилотажа. И то, и другое дарят чувство обновления, феерические эмоции, яркие впечатления.\",\n" +
                "  \"eventDate\": \"2024-12-31 15:10:05\",\n" +
                "  \"location\": {\n" +
                "    \"lat\": 55.754167,\n" +
                "    \"lon\": 37.62\n" +
                "  },\n" +
                "  \"paid\": true,\n" +
                "  \"participantLimit\": 10,\n" +
                "  \"requestModeration\": false,\n" +
                "  \"title\": \"Сплав на байдарках\"\n" +
                "}";

        EventRequest eventRequest = objectMapper.readValue(jsonRequest, EventRequest.class);
        EventFull eventFull = EventFull.builder().build();
        when(eventService.addEvent(1L, eventRequest))
                .thenReturn(eventFull);
        String response = mockMvc.perform(post(USERS_PATH + "/1" + EVENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        verify(eventService, times(1)).addEvent(1L, eventRequest);
        EventFull responseEventFull = objectMapper.readValue(response, EventFull.class);
        assertNotNull(responseEventFull);
    }

    private static Location getLocation() {
        Location location = new Location();
        location.setLat(Double.valueOf("0.0"));
        location.setLon(Double.valueOf("0.0"));
        return location;
    }

    @Test
    void test_getEventByCreatorAndId() throws Exception {
        String urlTemplate = "/users/1/events/1";
        EventFull mockEventFull = new EventFull();
        when(eventService.getEventByCreatorAndId(1L, 1L)).thenReturn(mockEventFull);
        String responseJson = mockMvc.perform(get(urlTemplate))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        verify(eventService, times(1)).getEventByCreatorAndId(1L, 1L);
        EventFull responseEventFull = objectMapper.readValue(responseJson, EventFull.class);
        assertNotNull(responseEventFull);
    }

    @Test
    void test_getEventsByCreator() throws Exception {
        String urlTemplate = "/users/1/events" + "?from=0&size=5";
        EventShort mockEventShort = new EventShort();
        when(eventService.getEventsByCreator(1L, 0, 5)).thenReturn(List.of(mockEventShort));
        String responseJson = mockMvc.perform(get(urlTemplate))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        verify(eventService, times(1)).getEventsByCreator(1L, 0, 5);
        Collection<EventShort> responseEventShort = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        assertNotNull(responseEventShort);
    }

    @Test
    void test_updateEvent() throws Exception {
        EventFull mockEventFull = new EventFull();
        UpdateEventRequest eventRequest = UpdateEventRequest.builder()
                .annotation("AnnotationAnnotationAnnotationAnnotation")
                .description("DescriptionDescriptionDescriptionDescription")
                .eventDate(LocalDateTime.now().plusDays(1))
                .category(1L)
                .paid(true)
                .title("Title")
                .location(getLocation())
                .requestModeration(false)
                .participantLimit(1000)
                .build();
        String urlTemplate = "/users/1/events/1";
        when(eventService.updateEvent(1L, 1L, eventRequest)).thenReturn(mockEventFull);
        String responseJson = mockMvc.perform(patch(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        verify(eventService, times(1)).updateEvent(1L, 1L, eventRequest);
        EventFull responseEvent = objectMapper.readValue(responseJson, EventFull.class);
        assertNotNull(responseEvent);
    }

    @Test
    void test_createParticipationRequest() throws Exception {
        String urlTemplate = "/users/1/events/1/requests";
        when(participationService.getRequestsByUserAndEventIds(1L, 1L)).thenReturn(emptyList());
        String responseJson = mockMvc.perform(get(urlTemplate))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        verify(participationService, times(1)).getRequestsByUserAndEventIds(1L, 1L);
        Collection<ParticipationRequestResponse> responseEventShort = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        assertNotNull(responseEventShort);
    }

    @Test
    void test_changeParticipationStatus() throws Exception {
        ParticipationStatusUpdateRequest request = new ParticipationStatusUpdateRequest();
        request.setStatus(ParticipationRequestStatus.CONFIRMED);
        request.setRequestIds(List.of(1L, 2L));
        var response = AllParticipationRequestsResponse.builder().build();

        when(participationService.changeParticipationStatus(1L, 1L, request)).thenReturn(response);

        String urlTemplate = "/users/1/events/1/requests";
        String responseJson = mockMvc.perform(patch(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        verify(participationService, times(1)).changeParticipationStatus(1L, 1L, request);
        AllParticipationRequestsResponse responseEvent = objectMapper.readValue(responseJson, AllParticipationRequestsResponse.class);
        assertNotNull(responseEvent);
    }
}