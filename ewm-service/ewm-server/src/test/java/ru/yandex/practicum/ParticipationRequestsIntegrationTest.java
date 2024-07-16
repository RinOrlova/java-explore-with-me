package ru.yandex.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.client.StatisticsClient;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventStatus;
import ru.yandex.practicum.dto.participation.AllParticipationRequestsResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestStatus;
import ru.yandex.practicum.dto.participation.ParticipationStatusUpdateRequest;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;
import ru.yandex.practicum.utils.ApiPathConstants;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.utils.ApiPathConstants.EVENT_PATH;
import static ru.yandex.practicum.utils.ApiPathConstants.USERS_PATH;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/application.properties")
public class ParticipationRequestsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StatisticsClient statisticsClient;

    @Test
    void test_requestOperations() throws Exception {
        User eventInitiator = new User();
        eventInitiator.setName("Event initiator");
        eventInitiator.setEmail("initiaro@domain.com");

        User approvedUser = new User();
        approvedUser.setName("Approved user");
        approvedUser.setEmail("approved_user@domain.com");

        User notApprovedUser = new User();
        notApprovedUser.setName("Not approved user");
        notApprovedUser.setEmail("not_approved_user@domain.com");

        // Create user
        String initiatorUserResponseJson = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventInitiator)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var initiatorResultUser = objectMapper.readValue(initiatorUserResponseJson, UserFull.class);
        String approvedUserResponseJson = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(approvedUser)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var approvedResultUser = objectMapper.readValue(approvedUserResponseJson, UserFull.class);
        String notApprovedUserResponseJson = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notApprovedUser)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var notApprovedResultUser = objectMapper.readValue(notApprovedUserResponseJson, UserFull.class);

        // Create category
        String categoryRequestJson = "{\"name\":\"CategoryName#@$\"}";
        String categoryResponseJson = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.CATEGORY_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryRequestJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var category = objectMapper.readValue(categoryResponseJson, Category.class);

        // Create event
        String jsonRequest = "{\n" +
                "  \"annotation\": \"Error sunt autem officia reprehenderit voluptas cum porro fuga vel. Magnam et sit. At quas recusandae quia. Provident incidunt officiis.\",\n" +
                "  \"category\":" + category.getId() + ",\n" +
                "  \"description\": \"Vero quo sed. Sit architecto quia cum qui voluptatem hic et. Fuga illum quibusdam iste. Voluptatibus officiis nulla nemo illo. Autem aliquam et possimus illo unde quam et eos.n rEa exercitationem ea non architecto. Quia ut tempora et iste accusantium ratione a voluptatibus voluptates. Eos reiciendis necessitatibus dolores voluptatem ducimus inventore. Sit provident dignissimos praesentium dolores. Porro ut aut. Qui cum ut quam veniam minus quia nemo voluptatem.n rMagnam porro ab. Voluptas in inventore dolores harum veritatis impedit consectetur expedita. Illo aut iure magni ut. Quia dolores quidem est amet dolores incidunt omnis facilis. Accusamus ipsa praesentium.\",\n" +
                "  \"eventDate\": \"2024-12-31 20:52:21\",\n" +
                "  \"location\": {\n" +
                "    \"lat\": -24.5189,\n" +
                "    \"lon\": 141.4601\n" +
                "  },\n" +
                "  \"paid\": \"false\",\n" +
                "  \"participantLimit\": \"1\",\n" +
                "  \"requestModeration\": \"true\",\n" +
                "  \"title\": \"Possimus ab esse.\"\n" +
                "}";
        String response = mockMvc.perform(post(USERS_PATH + "/" + initiatorResultUser.getId() + EVENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        EventFull eventFull = objectMapper.readValue(response, EventFull.class);
        assertEquals(1, eventFull.getParticipantLimit());
        assertEquals(0, eventFull.getConfirmedRequests());
        assertEquals(EventStatus.PENDING, eventFull.getState());

        // Publish event by admin
        String publishedEvent = mockMvc.perform(patch("/admin/events/" + eventFull.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stateAction\": \"PUBLISH_EVENT\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        EventFull publishedEventFull = objectMapper.readValue(publishedEvent, EventFull.class);
        assertEquals(1, publishedEventFull.getParticipantLimit());
        assertEquals(0, publishedEventFull.getConfirmedRequests());
        assertEquals(EventStatus.PUBLISHED, publishedEventFull.getState());

        // Create participation requests
        String requestResponseJson = mockMvc.perform(post("/users/" + approvedResultUser.getId() + "/requests?eventId=" + publishedEventFull.getId()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var toBeApprovedRequest = objectMapper.readValue(requestResponseJson, ParticipationRequestResponse.class);
        assertEquals(ParticipationRequestStatus.PENDING, toBeApprovedRequest.getStatus());
        assertEquals(approvedResultUser.getId(), toBeApprovedRequest.getRequester());
        assertEquals(publishedEventFull.getId(), toBeApprovedRequest.getEvent());

        String eventFullWithNoApprovedRequestsJson = mockMvc.perform(get("/users/" + initiatorResultUser.getId() + "/events/" + publishedEventFull.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        EventFull eventFullWithNoApprovedRequests = objectMapper.readValue(eventFullWithNoApprovedRequestsJson, EventFull.class);
        assertEquals(1, eventFullWithNoApprovedRequests.getParticipantLimit());
        assertEquals(0, eventFullWithNoApprovedRequests.getConfirmedRequests());

        // Approve request
        ParticipationStatusUpdateRequest approveRequest = new ParticipationStatusUpdateRequest();
        approveRequest.setRequestIds(List.of(toBeApprovedRequest.getId()));
        approveRequest.setStatus(ParticipationRequestStatus.CONFIRMED);
        String allReqResponsesJson = mockMvc.perform(patch("/users/" + initiatorResultUser.getId() + "/events/" + publishedEventFull.getId() + "/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(approveRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        var allReqRespones = objectMapper.readValue(allReqResponsesJson, AllParticipationRequestsResponse.class);
        assertEquals(1, allReqRespones.getConfirmedRequests().size());
        assertEquals(0, allReqRespones.getRejectedRequests().size());

        String eventFullWithOneApprovedRequestsJson = mockMvc.perform(get("/users/" + initiatorResultUser.getId() + "/events/" + publishedEventFull.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        EventFull eventFullWithOneApprovedRequests = objectMapper.readValue(eventFullWithOneApprovedRequestsJson, EventFull.class);
        assertEquals(1, eventFullWithOneApprovedRequests.getParticipantLimit());
        assertEquals(1, eventFullWithOneApprovedRequests.getConfirmedRequests());

        // To be rejected request
        mockMvc.perform(post("/users/" + notApprovedResultUser.getId() + "/requests?eventId=" + publishedEventFull.getId()))
                .andExpect(status().isConflict());

    }

    @Test
    void test_requestOperations_declineRequest() throws Exception {
        User eventInitiator = new User();
        eventInitiator.setName("Event initiator1");
        eventInitiator.setEmail("initiaro1@domain.com");

        User approvedUser = new User();
        approvedUser.setName("Approved user1");
        approvedUser.setEmail("approved_user1@domain.com");

        User notApprovedUser = new User();
        notApprovedUser.setName("Not approved user1");
        notApprovedUser.setEmail("not_approved_user1@domain.com");

        // Create user
        String initiatorUserResponseJson = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventInitiator)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var initiatorResultUser = objectMapper.readValue(initiatorUserResponseJson, UserFull.class);
        String approvedUserResponseJson = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(approvedUser)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var approvedResultUser = objectMapper.readValue(approvedUserResponseJson, UserFull.class);
        String notApprovedUserResponseJson = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notApprovedUser)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var notApprovedResultUser = objectMapper.readValue(notApprovedUserResponseJson, UserFull.class);

        // Create category
        String categoryRequestJson = "{\"name\":\"CategoryName#@$1\"}";
        String categoryResponseJson = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.CATEGORY_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryRequestJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var category = objectMapper.readValue(categoryResponseJson, Category.class);

        // Create event
        String jsonRequest = "{\n" +
                "  \"annotation\": \"Error sunt autem officia reprehenderit voluptas cum porro fuga vel. Magnam et sit. At quas recusandae quia. Provident incidunt officiis.\",\n" +
                "  \"category\":" + category.getId() + ",\n" +
                "  \"description\": \"Vero quo sed. Sit architecto quia cum qui voluptatem hic et. Fuga illum quibusdam iste. Voluptatibus officiis nulla nemo illo. Autem aliquam et possimus illo unde quam et eos.n rEa exercitationem ea non architecto. Quia ut tempora et iste accusantium ratione a voluptatibus voluptates. Eos reiciendis necessitatibus dolores voluptatem ducimus inventore. Sit provident dignissimos praesentium dolores. Porro ut aut. Qui cum ut quam veniam minus quia nemo voluptatem.n rMagnam porro ab. Voluptas in inventore dolores harum veritatis impedit consectetur expedita. Illo aut iure magni ut. Quia dolores quidem est amet dolores incidunt omnis facilis. Accusamus ipsa praesentium.\",\n" +
                "  \"eventDate\": \"2024-12-31 20:52:21\",\n" +
                "  \"location\": {\n" +
                "    \"lat\": -24.5189,\n" +
                "    \"lon\": 141.4601\n" +
                "  },\n" +
                "  \"paid\": \"false\",\n" +
                "  \"participantLimit\": \"1\",\n" +
                "  \"requestModeration\": \"true\",\n" +
                "  \"title\": \"Possimus ab esse.\"\n" +
                "}";
        String response = mockMvc.perform(post(USERS_PATH + "/" + initiatorResultUser.getId() + EVENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        EventFull eventFull = objectMapper.readValue(response, EventFull.class);
        assertEquals(1, eventFull.getParticipantLimit());
        assertEquals(0, eventFull.getConfirmedRequests());
        assertEquals(EventStatus.PENDING, eventFull.getState());

        // Publish event by admin
        String publishedEvent = mockMvc.perform(patch("/admin/events/" + eventFull.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stateAction\": \"PUBLISH_EVENT\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        EventFull publishedEventFull = objectMapper.readValue(publishedEvent, EventFull.class);
        assertEquals(1, publishedEventFull.getParticipantLimit());
        assertEquals(0, publishedEventFull.getConfirmedRequests());
        assertEquals(EventStatus.PUBLISHED, publishedEventFull.getState());

        // Create participation requests
        String requestResponseJson = mockMvc.perform(post("/users/" + notApprovedResultUser.getId() + "/requests?eventId=" + publishedEventFull.getId()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var toBeRejectedRequest = objectMapper.readValue(requestResponseJson, ParticipationRequestResponse.class);
        assertEquals(ParticipationRequestStatus.PENDING, toBeRejectedRequest.getStatus());
        assertEquals(notApprovedResultUser.getId(), toBeRejectedRequest.getRequester());
        assertEquals(publishedEventFull.getId(), toBeRejectedRequest.getEvent());

        String eventFullWithNoApprovedRequestsJson = mockMvc.perform(get("/users/" + initiatorResultUser.getId() + "/events/" + publishedEventFull.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        EventFull eventFullWithNoApprovedRequests = objectMapper.readValue(eventFullWithNoApprovedRequestsJson, EventFull.class);
        assertEquals(1, eventFullWithNoApprovedRequests.getParticipantLimit());
        assertEquals(0, eventFullWithNoApprovedRequests.getConfirmedRequests());

        // Decline request
        ParticipationStatusUpdateRequest approveRequest = new ParticipationStatusUpdateRequest();
        approveRequest.setRequestIds(List.of(toBeRejectedRequest.getId()));
        approveRequest.setStatus(ParticipationRequestStatus.DECLINED);
        String allReqResponsesJson = mockMvc.perform(patch("/users/" + initiatorResultUser.getId() + "/events/" + publishedEventFull.getId() + "/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(approveRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        var allReqRespones = objectMapper.readValue(allReqResponsesJson, AllParticipationRequestsResponse.class);
        assertEquals(0, allReqRespones.getConfirmedRequests().size());
        assertEquals(1, allReqRespones.getRejectedRequests().size());

        String eventFullWithOneApprovedRequestsJson = mockMvc.perform(get("/users/" + initiatorResultUser.getId() + "/events/" + publishedEventFull.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        EventFull eventFullWithOneApprovedRequests = objectMapper.readValue(eventFullWithOneApprovedRequestsJson, EventFull.class);
        assertEquals(1, eventFullWithOneApprovedRequests.getParticipantLimit());
        assertEquals(0, eventFullWithOneApprovedRequests.getConfirmedRequests());

        // To be cancelled request
        String toBeApprovedReqJson = mockMvc.perform(post("/users/" + approvedResultUser.getId() + "/requests?eventId=" + publishedEventFull.getId()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var toBeCancelledRequest = objectMapper.readValue(toBeApprovedReqJson, ParticipationRequestResponse.class);
        assertEquals(ParticipationRequestStatus.PENDING, toBeCancelledRequest.getStatus());
        // cancel own request
        String cancelledReqJson = mockMvc.perform(patch("/users/" + approvedResultUser.getId() + "/requests/" + toBeCancelledRequest.getId() + "/cancel"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ParticipationRequestResponse cancelledReqResponse = objectMapper.readValue(cancelledReqJson, ParticipationRequestResponse.class);
        assertEquals(ParticipationRequestStatus.CANCELED, cancelledReqResponse.getStatus());


    }
}
