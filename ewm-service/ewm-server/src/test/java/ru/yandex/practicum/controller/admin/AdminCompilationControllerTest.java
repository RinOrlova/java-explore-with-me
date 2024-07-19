package ru.yandex.practicum.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.dto.compilation.CompilationResponse;
import ru.yandex.practicum.service.compilation.CompilationService;
import ru.yandex.practicum.stats.StatisticsService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCompilationController.class)
class AdminCompilationControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatisticsService statisticsService;
    @MockBean
    private CompilationService compilationService;

    @Test
    void test_updateCompilation() throws Exception {
        String requestUpdateCompilationJson = "{\n" +
                "  \"events\": [],\n" +
                "  \"id\": 35,\n" +
                "  \"pinned\": true,\n" +
                "  \"title\": \"Ipsum natus eius eaque commodi enim tempore tempor\"\n" +
                "}";

        when(compilationService.updateCompilation(any(), any()))
                .thenReturn(new CompilationResponse());

        mockMvc.perform(patch("/admin/compilations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestUpdateCompilationJson))
                .andExpect(status().isOk());
    }


}