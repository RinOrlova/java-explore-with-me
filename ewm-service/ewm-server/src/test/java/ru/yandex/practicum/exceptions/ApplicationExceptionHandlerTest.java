package ru.yandex.practicum.exceptions;

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
    @RequestMapping
    public static class TestController {
        @GetMapping("/notFound/category")
        public void throwCategoryNotFoundException() {
            throw new CategoryNotFoundException(1L);
        }

        @GetMapping("/notFound/user")
        public void throwUserNotFoundException() {
            throw new UserNotFoundException(1L);
        }

        @GetMapping("/notFound/event")
        public void throwEventNotFoundException() {
            throw new EventNotFoundException(1L);
        }

        @GetMapping("/notFound/compilation")
        public void throwCompilationNotFoundException() {
            throw new CompilationNotFoundException(1L);
        }

        @GetMapping("/technical")
        public void throwRuntimeException() {
            throw new NullPointerException();
        }
    }

    @Test
    void handleNotFoundExceptions() throws Exception {
        mockMvc.perform(get("/notFound/category"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.reason").value("The required object was not found."))
                .andExpect(jsonPath("$.message").value("Category by id=1 not found"));
        mockMvc.perform(get("/notFound/user"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.reason").value("The required object was not found."))
                .andExpect(jsonPath("$.message").value("User by id=1 not found"));
        mockMvc.perform(get("/notFound/event"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.reason").value("The required object was not found."))
                .andExpect(jsonPath("$.message").value("Event by id=1 not found"));
        mockMvc.perform(get("/notFound/compilation"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.reason").value("The required object was not found."))
                .andExpect(jsonPath("$.message").value("Compilation by id=1 not found"));
    }

    @Test
    void handleUnknownRuntimeException() throws Exception {
        mockMvc.perform(get("/technical"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.reason").value("Server error"))
                .andExpect(jsonPath("$.message").value("Error occurred on server side."));
    }
}