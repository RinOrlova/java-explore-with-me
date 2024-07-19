package ru.yandex.practicum.statistics.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.exceptions.InvalidDateFormatRequestedException;
import ru.yandex.practicum.exceptions.InvalidDateRequestedException;
import ru.yandex.practicum.statistics.utils.TestConstants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ApplicationExceptionHandlerTest.TestController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ApplicationExceptionHandler.class)
})
@ContextConfiguration(classes = {ApplicationExceptionHandler.class, ApplicationExceptionHandlerTest.TestController.class})
class ApplicationExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    @RequestMapping("/test")
    public static class TestController {
        @GetMapping("/invalidDateFormat")
        public void throwInvalidDateFormatRequestedException() {
            throw new InvalidDateFormatRequestedException("INVALID_TIME_FORMAT");
        }

        @GetMapping("/invalidDateRequested")
        public void throwInvalidDataInRequestException() {
            throw new InvalidDateRequestedException(TestConstants.DEFAULT_TIMESTAMP.plusSeconds(1), TestConstants.DEFAULT_TIMESTAMP);
        }
    }

    @Test
    void handleValidationException() throws Exception {
        mockMvc.perform(get("/test/invalidDateFormat"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid data in request."))
                .andExpect(jsonPath("$.description").value("Invalid Date param=INVALID_TIME_FORMAT requested"));
    }

    @Test
    void handleItemUnavailableException() throws Exception {
        mockMvc.perform(get("/test/invalidDateRequested"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid data in request."))
                .andExpect(jsonPath("$.description").value("Start=2024-12-12T12:00:01 must be before or equal end=2024-12-12T12:00"));
    }
}