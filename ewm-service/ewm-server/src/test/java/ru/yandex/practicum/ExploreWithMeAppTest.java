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
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;
import ru.yandex.practicum.utils.ApiPathConstants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.utils.ApiPathConstants.EVENT_PATH;
import static ru.yandex.practicum.utils.ApiPathConstants.USERS_PATH;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/application.properties")
class ExploreWithMeAppTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StatisticsClient statisticsClient;

    @Test
    void test_admin_userOperations() throws Exception {
        User user = new User();
        user.setName("User Name");
        user.setEmail("email@domain.com");
        User user2 = new User();
        user2.setName("User Name 2");
        user2.setEmail("email2@domain.com");

        // Create users
        mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
        mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isCreated());

        // Get all users
        String getUsersUrl = new StringBuilder()
                .append(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                .toString();
        String usersResponseJson = mockMvc.perform(get(getUsersUrl))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<UserFull> users = objectMapper.readValue(usersResponseJson, new TypeReference<>() {
        });
        assertEquals(2, users.size());
        assertThat(users).extracting("name").containsExactly("User Name", "User Name 2");
        assertThat(users).extracting("email").containsExactly("email@domain.com", "email2@domain.com");

        // Get all users by id
        String getUsersByIdUrl = new StringBuilder()
                .append(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                .append("?ids=").append(users.iterator().next().getId())
                .toString();
        String usersByIdResponseJson = mockMvc.perform(get(getUsersByIdUrl))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<UserFull> usersById = objectMapper.readValue(usersByIdResponseJson, new TypeReference<>() {
        });
        assertEquals(1, usersById.size());
        assertThat(usersById).extracting("name").containsExactly("User Name");
        assertThat(usersById).extracting("email").containsExactly("email@domain.com");

        // Get all users considering paging
        String getUsersPagingUrl = new StringBuilder()
                .append(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                .append("?from=").append(0)
                .append("&size=").append(1)
                .toString();
        String usersPagingResponseJson = mockMvc.perform(get(getUsersPagingUrl))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<UserFull> usersPaging = objectMapper.readValue(usersPagingResponseJson, new TypeReference<>() {
        });
        assertEquals(1, usersPaging.size());
        assertThat(usersPaging).extracting("name").containsExactly("User Name");
        assertThat(usersPaging).extracting("email").containsExactly("email@domain.com");

        // Delete users
        String deleteFirstUserUrl = new StringBuilder()
                .append(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                .append("/").append(new ArrayList<>(users).get(0).getId())
                .toString();
        String deleteSecondUserUrl = new StringBuilder()
                .append(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                .append("/").append(new ArrayList<>(users).get(1).getId())
                .toString();
        mockMvc.perform(delete(deleteFirstUserUrl))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete(deleteSecondUserUrl))
                .andExpect(status().isNoContent());

        String getUsersAfterDeletionUrl = new StringBuilder()
                .append(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                .toString();
        String usersAfterDeletionResponseJson = mockMvc.perform(get(getUsersAfterDeletionUrl))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<UserFull> usersAfterDeletion = objectMapper.readValue(usersAfterDeletionResponseJson, new TypeReference<>() {
        });
        assertEquals(0, usersAfterDeletion.size());

    }

    @Test
    void test_eventsOperations() throws Exception {
        User user = new User();
        user.setName("User Name 2");
        user.setEmail("email2@domain.com");

        // Create user
        String userResponseJson = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var userFull = objectMapper.readValue(userResponseJson, UserFull.class);

        // Create category
        String categoryRequestJson = "{\"name\":\"CategoryName\"}";
        String categoryResponseJson = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.CATEGORY_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryRequestJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var category = objectMapper.readValue(categoryResponseJson, Category.class);


        String jsonRequest = "{\n" +
                "  \"annotation\": \"Error sunt autem officia reprehenderit voluptas cum porro fuga vel. Magnam et sit. At quas recusandae quia. Provident incidunt officiis.\",\n" +
                "  \"category\":" + category.getId() + ",\n" +
                "  \"description\": \"Vero quo sed. Sit architecto quia cum qui voluptatem hic et. Fuga illum quibusdam iste. Voluptatibus officiis nulla nemo illo. Autem aliquam et possimus illo unde quam et eos.n rEa exercitationem ea non architecto. Quia ut tempora et iste accusantium ratione a voluptatibus voluptates. Eos reiciendis necessitatibus dolores voluptatem ducimus inventore. Sit provident dignissimos praesentium dolores. Porro ut aut. Qui cum ut quam veniam minus quia nemo voluptatem.n rMagnam porro ab. Voluptas in inventore dolores harum veritatis impedit consectetur expedita. Illo aut iure magni ut. Quia dolores quidem est amet dolores incidunt omnis facilis. Accusamus ipsa praesentium.\",\n" +
                "  \"eventDate\": \"2024-07-14 20:52:21\",\n" +
                "  \"location\": {\n" +
                "    \"lat\": -24.5189,\n" +
                "    \"lon\": 141.4601\n" +
                "  },\n" +
                "  \"paid\": \"false\",\n" +
                "  \"participantLimit\": \"629\",\n" +
                "  \"requestModeration\": \"true\",\n" +
                "  \"title\": \"Possimus ab esse.\"\n" +
                "}";

        String response = mockMvc.perform(post(USERS_PATH + "/" + userFull.getId() + EVENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        EventFull eventFull = objectMapper.readValue(response, EventFull.class);
        assertEquals("Possimus ab esse.", eventFull.getTitle());
        assertEquals("User Name 2", eventFull.getInitiator().getName());
        assertEquals("email2@domain.com", eventFull.getInitiator().getEmail());
        assertEquals(LocalDateTime.parse("2024-07-14T20:52:21"), eventFull.getEventDate());
        assertFalse(eventFull.isPaid());
        assertTrue(eventFull.isRequestModeration());
        assertEquals(629, eventFull.getParticipantLimit());
        assertEquals("CategoryName", eventFull.getCategory().getName());
    }
}