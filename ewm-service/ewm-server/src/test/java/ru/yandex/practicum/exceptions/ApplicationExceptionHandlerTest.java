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
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.storage.compilation.CompilationEntity;
import ru.yandex.practicum.storage.event.EventEntity;
import ru.yandex.practicum.storage.user.UserEntity;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

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
            throw new EntityNotFoundException(1L, Category.class);
        }

        @GetMapping("/notFound/user")
        public void throwUserNotFoundException() {
            throw new EntityNotFoundException(1L, UserEntity.class);
        }

        @GetMapping("/notFound/event")
        public void throwEventNotFoundException() {
            throw new EntityNotFoundException(1L, EventEntity.class);
        }

        @GetMapping("/notFound/compilation")
        public void throwCompilationNotFoundException() {
            throw new EntityNotFoundException(1L, CompilationEntity.class);
        }

        @GetMapping("/technical")
        public void throwRuntimeException() {
            throw new NullPointerException();
        }

        @GetMapping("/forbidden")
        public void throwForbiddenException() {
            throw new ForbiddenException("ForbiddenExceptionMessage");
        }

        @GetMapping("/forbiddenDefault")
        public void throwForbiddenExceptionDefault() {
            throw new ForbiddenException();
        }

        @GetMapping("/invalidDateRequestedException")
        public void throwInvalidDateRequestedException() {
            throw new InvalidDateRequestedException(LocalDateTime.parse("2024-12-31T20:52:21"), LocalDateTime.parse("2024-12-31T20:52:21"));
        }

        @GetMapping("/validationException")
        public void throwValidationException() {
            throw new ValidationException("Validation failed");
        }
    }

    @Test
    void handleNotFoundExceptions() throws Exception {
        mockMvc.perform(get("/notFound/category"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.reason").value("The required object was not found."))
                .andExpect(jsonPath("$.message").value("Entity ru.yandex.practicum.dto.category.Category by id=1 not found"));
        mockMvc.perform(get("/notFound/user"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.reason").value("The required object was not found."))
                .andExpect(jsonPath("$.message").value("Entity ru.yandex.practicum.storage.user.UserEntity by id=1 not found"));
        mockMvc.perform(get("/notFound/event"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.reason").value("The required object was not found."))
                .andExpect(jsonPath("$.message").value("Entity ru.yandex.practicum.storage.event.EventEntity by id=1 not found"));
        mockMvc.perform(get("/notFound/compilation"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.reason").value("The required object was not found."))
                .andExpect(jsonPath("$.message").value("Entity ru.yandex.practicum.storage.compilation.CompilationEntity by id=1 not found"));
    }

    @Test
    void handleUnknownRuntimeException() throws Exception {
        mockMvc.perform(get("/technical"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.reason").value("Server error"))
                .andExpect(jsonPath("$.message").value("Error occurred on server side."));
    }

    @Test
    void handleForbiddenException() throws Exception {
        mockMvc.perform(get("/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.reason").value("For the requested operation the conditions are not met."))
                .andExpect(jsonPath("$.message").value("ForbiddenExceptionMessage"));
        mockMvc.perform(get("/forbiddenDefault"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.reason").value("For the requested operation the conditions are not met."))
                .andExpect(jsonPath("$.message").value("Action forbidden"));
    }

    @Test
    void hanldeBasRequestException() throws Exception {
        mockMvc.perform(get("/invalidDateRequestedException"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.reason").value("Request validation failed."))
                .andExpect(jsonPath("$.message").value("Start=2024-12-31T20:52:21 must be before or equal end=2024-12-31T20:52:21"));
        mockMvc.perform(get("/validationException"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.reason").value("Request validation failed."))
                .andExpect(jsonPath("$.message").value("Validation failed"));


    }
}