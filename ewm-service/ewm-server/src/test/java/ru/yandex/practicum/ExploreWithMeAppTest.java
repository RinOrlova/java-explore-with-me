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
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.dto.event.EventStatus;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;
import ru.yandex.practicum.utils.ApiPathConstants;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    void test_categoryOperations() throws Exception {
        // add new category
        Category category = new Category();
        category.setName("ca$tegory");
        mockMvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated());

        // get all categories paging 0-1000
        String getCategoriesPagingURL = "/categories?from=0&size=1000";
        String categoriesPagingResponseJson = mockMvc.perform(get(getCategoriesPagingURL))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Collection<Category> categories = objectMapper.readValue(categoriesPagingResponseJson, new TypeReference<>() {
        });
        Map<String, Category> categoriesGroupedByName = categories.stream().collect(Collectors.toMap(Category::getName, x -> x));
        Category categoryToCheck = categoriesGroupedByName.get("ca$tegory");
        assertNotNull(categoryToCheck);
        assertEquals("ca$tegory", categoryToCheck.getName());
        // update category
        Category categoryNewName = new Category();
        categoryNewName.setName("ca$teg#ry");
        String updatedCategoriesPagingResponseJson = mockMvc.perform(patch("/admin/categories/" + categoryToCheck.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryNewName)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Category updatedCategory = objectMapper.readValue(updatedCategoriesPagingResponseJson, Category.class);
        assertEquals("ca$teg#ry", updatedCategory.getName());
        // delete category
        mockMvc.perform(delete("/admin/categories/" + categoryToCheck.getId()))
                .andExpect(status().isNoContent());
        String categoriesGetAllResponseJson = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Collection<Category> categoriesGetAllResult = objectMapper.readValue(categoriesGetAllResponseJson, new TypeReference<>() {
        });
        Map<String, Category> categoriesGroupedByNameAllResult = categoriesGetAllResult.stream().collect(Collectors.toMap(Category::getName, x -> x));
        assertNull(categoriesGroupedByNameAllResult.get("ca$tegory"));
        assertNull(categoriesGroupedByNameAllResult.get("ca$teg#ry"));
    }

    @Test
    void test_admin_userOperations() throws Exception {
        User user = new User();
        user.setName("nZPoRD0ERJZT0rY7AJzBll7FMbnOQtbWMd4UletIviFjkOFAGlb6RsmqFLgMSc0Hn7CWji4gXu0emldK7Mg7Q1TSg9k7FGpPjKmVSgxxTSqEuBhvKNnFkrmWtJK1wuizKnK1lhSHEd61zdk3ctEl0aSOPhz4X4tXmhZ36FiotsLblVSsnbQgdYfNt0DMoZbJg9B4v2HCO3xgGNGRqYqzR7ISL5Q9iebRqYwoOcZhzbAjPs8MD7jrkShIta");
        user.setEmail("email@domain.com");
        User user2 = new User();
        user2.setName("User Name 2");
        user2.setEmail("email2@domain.com");

        // Create users
        String userJson1 = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        UserFull userFull1 = objectMapper.readValue(userJson1, UserFull.class);
        String userJson2 = mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        UserFull userFull2 = objectMapper.readValue(userJson2, UserFull.class);

        // Get all users
        String getUsersUrl = new StringBuilder()
                .append(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                .toString();
        String usersResponseJson = mockMvc.perform(get(getUsersUrl))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<UserFull> users = objectMapper.readValue(usersResponseJson, new TypeReference<>() {
        });
        // Get all users by id
        String getUsersByIdUrl = new StringBuilder()
                .append(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                .append("?ids=").append(userFull2.getId())
                .toString();
        String usersByIdResponseJson = mockMvc.perform(get(getUsersByIdUrl))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<UserFull> usersById = objectMapper.readValue(usersByIdResponseJson, new TypeReference<>() {
        });
        assertEquals(1, usersById.size());

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

        // Delete users
        String deleteFirstUserUrl = new StringBuilder()
                .append(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                .append("/").append(userFull2.getId())
                .toString();
        String deleteSecondUserUrl = new StringBuilder()
                .append(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                .append("/").append(userFull1.getId())
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
        Set<Long> userIds = usersAfterDeletion.stream()
                .map(UserFull::getId)
                .collect(Collectors.toSet());
        assertFalse(userIds.containsAll(List.of(userFull1.getId(), userFull2.getId())));

    }

    @Test
    void test_eventsOperations() throws Exception {
        User user = new User();
        user.setName("AlphaOmega 2");
        user.setEmail("alphaOmega2@domain.com");

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
                "  \"eventDate\": \"2024-12-31 20:52:21\",\n" +
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
        assertEquals("AlphaOmega 2", eventFull.getInitiator().getName());
        assertEquals("alphaOmega2@domain.com", eventFull.getInitiator().getEmail());
        assertEquals(LocalDateTime.parse("2024-12-31T20:52:21"), eventFull.getEventDate());
        assertFalse(eventFull.isPaid());
        assertTrue(eventFull.isRequestModeration());
        assertEquals(629, eventFull.getParticipantLimit());
        assertEquals("CategoryName", eventFull.getCategory().getName());
        assertEquals(EventStatus.PENDING, eventFull.getState());

        // Search event by initiator id;
        String responseSearchEventByInitiator = mockMvc.perform(get(USERS_PATH + "/" + userFull.getId() + EVENT_PATH))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Collection<EventShort> eventsShort = objectMapper.readValue(responseSearchEventByInitiator, new TypeReference<>() {
        });
        assertEquals(1, eventsShort.size());

        // Admin changes Event Status
        String adminEventPublishRequest = "{\"stateAction\":\"PUBLISH_EVENT\"}";
        String responseAdminEventUpdate = mockMvc.perform(patch("/admin/events/" + eventFull.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminEventPublishRequest))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        EventFull updatedEventFull = objectMapper.readValue(responseAdminEventUpdate, EventFull.class);
        assertEquals("Possimus ab esse.", updatedEventFull.getTitle());
        assertEquals("AlphaOmega 2", updatedEventFull.getInitiator().getName());
        assertEquals("alphaOmega2@domain.com", updatedEventFull.getInitiator().getEmail());
        assertEquals(LocalDateTime.parse("2024-12-31T20:52:21"), updatedEventFull.getEventDate());
        assertFalse(updatedEventFull.isPaid());
        assertTrue(updatedEventFull.isRequestModeration());
        assertEquals(629, updatedEventFull.getParticipantLimit());
        assertEquals("CategoryName", updatedEventFull.getCategory().getName());
        assertEquals(EventStatus.PUBLISHED, updatedEventFull.getState());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), updatedEventFull.getPublishedOn().truncatedTo(ChronoUnit.MINUTES));

        // Admin search
        String adminSearchResp = mockMvc.perform(get("/admin/events" +
                        "?states=PUBLISHED" +
                        "&rangeStart=" + LocalDateTime.parse("2022-01-06T13:30:38") +
                        "&rangeEnd=" + LocalDateTime.parse("2097-09-06T13:30:38") +
                        "&from=0&size=1000" +
                        "&users=" +
                        updatedEventFull.getInitiator().getId() +
                        "&categories=" +
                        updatedEventFull.getCategory().getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Collection<EventFull> resultAdminSearch = objectMapper.readValue(adminSearchResp, new TypeReference<>() {
        });
        assertFalse(resultAdminSearch.isEmpty());
    }
}