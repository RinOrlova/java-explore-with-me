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
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;
import ru.yandex.practicum.utils.ApiPathConstants;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        // Get all users by id
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
}