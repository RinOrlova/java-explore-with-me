package ru.yandex.practicum.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.service.users.UserService;
import ru.yandex.practicum.stats.StatisticsService;
import ru.yandex.practicum.utils.ApiPathConstants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AdminUserController.class})
class AdminUserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StatisticsService statisticsService;

    @Test
    void test_negative_addUser_invalidEmail() throws Exception {
        String userInvalidJson = "{\"name\":\"Sherry Jerde\",\"email\":\"\"}";
        String userInvalidJson2 = "{\"name\":\"Sherry Jerde\",\"email\":\"email.com\"}";
        String userInvalidJson3 = "{\"name\":\"Sherry Jerde\"}";
        String userInvalidJson4 = "{\n" +
                "  \"name\": \"Cody MacGyver\",\n" +
                "  \"email\": \"f@19MsP4Nn577QXvfsLoa6AeQMZT23mqbJIaYn9rQA4VC2GNsKVfaf88Uxf8OEANj.LD5513V6ze9JRliFoXgNS9TfppEGTaMpWpZ7OTMtSzi0jIbzWbPeIuMbTpRcw7E.Clw9cv9YBGQRKM6dC6QWRbop7FjfdsXm0dkywlGdTtWrM0cXuqEcsdhGrXpCZWG.ffEJj3HA88t24cO2GCksRFw8vHnXUa1aiupAXgJvijmt38XYYRmeF4mJP4gaP\"\n" +
                "}";
        String userInvalidJson5 = "{\n" +
                "  \"name\": \"Jill Kassulke\",\n" +
                "  \"email\": \"z@a.r\"\n" +
                "}";
        mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInvalidJson))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInvalidJson2))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInvalidJson3))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInvalidJson4))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userInvalidJson5))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_positive_addUser() throws Exception {
        String userValidJson = "{\"name\":\"Sherry Jerde\",\"email\":\"email@email.com\"}";
        String userValidJson2 = "{\n" +
                "  \"name\": \"Sharon Aufderhar\",\n" +
                "  \"email\": \"s@MRbV6tBmtCiPcQCL4t90Gte3DjXGsyGJemVMAVoyuYkDRjR7hPVR57pQnkxrWsb.iOVtizpvaeKkyGBYbGzIRMIRKGmW5B8PhfWR7SFmnmZxkA4QPhgErgX1zxE1p6R.rFDYho2ipdRTnwi2Pwuu1vITPyJ9IYfHpu4hpeYcr3LeiKKqtRp9gzhHpbDxtYQ.PNHSlvWQGal13OEtgJUNTNznR7WatLqaP2vU1E9bRFUZqopiAJzmtPEHpTd9\"\n" +
                "}";
        mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userValidJson))
                .andExpect(status().isCreated());
        mockMvc.perform(post(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userValidJson2))
                .andExpect(status().isCreated());
    }
}