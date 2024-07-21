package ru.yandex.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
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
import ru.yandex.practicum.dto.comments.CommentRequest;
import ru.yandex.practicum.dto.comments.CommentResponse;
import ru.yandex.practicum.dto.comments.UpdateCommentRequest;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestStatus;
import ru.yandex.practicum.dto.participation.ParticipationStatusUpdateRequest;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;
import ru.yandex.practicum.utils.ApiPathConstants;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.utils.ApiPathConstants.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/application.properties")
public class CommentsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StatisticsClient statisticsClient;

    @Test
    void test_comment_operations() throws Exception {
        //create initiator
        User eventInitiator = new User();
        eventInitiator.setName("Event initiator2");
        eventInitiator.setEmail("initiator@domain.com");

        String initiatorUserResponseJson = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventInitiator)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var initiatorResultUser = objectMapper.readValue(initiatorUserResponseJson, UserFull.class);

        // Create category
        String categoryRequestJson = "{\"name\":\"CategoryName#@!$\"}";
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

        // Add comment to unpublished event
        CommentRequest commentRequest = CommentRequest.builder()
                .eventId(eventFull.getId())
                .text("Terrible event")
                .build();

        mockMvc.perform(post(USERS_PATH + "/" + initiatorResultUser.getId() + COMMENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isConflict());

        // Publish event by admin
        String publishedEvent = mockMvc.perform(patch("/admin/events/" + eventFull.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stateAction\": \"PUBLISH_EVENT\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        EventFull publishedEventFull = objectMapper.readValue(publishedEvent, EventFull.class);

        // Create user
        User user = new User();
        user.setName("user");
        user.setEmail("user@domain.com");

        String userResponseJson = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var resultUser = objectMapper.readValue(userResponseJson, UserFull.class);

        // Add comment by non participant
        CommentRequest commentRequestByNonParticipant = CommentRequest.builder()
                .eventId(eventFull.getId())
                .text("Beautiful event")
                .build();

        mockMvc.perform(post(USERS_PATH + "/" + resultUser.getId() + COMMENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestByNonParticipant)))
                .andExpect(status().isConflict());

        // Send request for the event
        String requestResponseJson = mockMvc.perform(post("/users/" + resultUser.getId() + "/requests?eventId=" + publishedEventFull.getId()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var toBeApprovedRequest = objectMapper.readValue(requestResponseJson, ParticipationRequestResponse.class);

        // Approve request
        ParticipationStatusUpdateRequest approveRequest = new ParticipationStatusUpdateRequest();
        approveRequest.setRequestIds(List.of(toBeApprovedRequest.getId()));
        approveRequest.setStatus(ParticipationRequestStatus.CONFIRMED);
        mockMvc.perform(patch("/users/" + initiatorResultUser.getId() + "/events/" + publishedEventFull.getId() + "/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(approveRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Add comment
        CommentRequest commentRequestByParticipant = CommentRequest.builder()
                .eventId(eventFull.getId())
                .text("Mediocre event")
                .build();

        String commentResponseJson = mockMvc.perform(post(USERS_PATH + "/" + resultUser.getId() + COMMENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestByParticipant)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        CommentResponse commentResponse = objectMapper.readValue(commentResponseJson, CommentResponse.class);
        assertEquals("Mediocre event", commentResponse.getText());
        assertNotNull(commentResponse.getCreated());
        assertEquals(eventFull.getId(), commentResponse.getEventId());
        assertEquals(resultUser.getId(), commentResponse.getUserId());
        assertNotNull(commentResponse.getEdited());

        // Update comment
        UpdateCommentRequest updateCommentRequest = UpdateCommentRequest.builder()
                .text("I wasn't there ha ha")
                .build();

        String updatedComment = mockMvc.perform(post(USERS_PATH + "/" + resultUser.getId() + COMMENT_PATH + "/" + commentResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCommentRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CommentResponse updatedCommentResponse = objectMapper.readValue(updatedComment, CommentResponse.class);
        assertEquals("I wasn't there ha ha", updatedCommentResponse.getText());
        assertNotNull(updatedCommentResponse.getCreated());
        assertEquals(eventFull.getId(), updatedCommentResponse.getEventId());
        assertEquals(resultUser.getId(), updatedCommentResponse.getUserId());
        assertNotNull(updatedCommentResponse.getEdited());

        // Search for a comment
        String eventComments = mockMvc.perform(get(COMMENT_PATH + "/" + eventFull.getId() + "?from=0&size=10"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<CommentResponse> commentResponses = objectMapper.readValue(eventComments, new TypeReference<>() {
        });
        assertEquals(1, commentResponses.size());
        CommentResponse commentResponseToCheck = commentResponses.iterator().next();
        assertEquals(updatedCommentResponse, commentResponseToCheck);

        // Delete comment
        mockMvc.perform(delete(USERS_PATH + "/" + resultUser.getId() + COMMENT_PATH + "/" + commentResponse.getId()))
                .andExpect(status().isNoContent());

        // Search for a comment
        String eventCommentsAfterDeletion = mockMvc.perform(get(COMMENT_PATH + "/" + eventFull.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<CommentResponse> commentResponsesAfterDeletion = objectMapper.readValue(eventCommentsAfterDeletion, new TypeReference<>() {
        });
        assertEquals(0, commentResponsesAfterDeletion.size());
    }
}
