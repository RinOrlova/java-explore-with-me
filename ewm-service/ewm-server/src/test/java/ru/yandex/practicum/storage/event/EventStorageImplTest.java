package ru.yandex.practicum.storage.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventRequest;
import ru.yandex.practicum.dto.location.Location;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.storage.category.CategoryEntity;
import ru.yandex.practicum.storage.category.CategoryStorage;
import ru.yandex.practicum.storage.location.LocationStorage;
import ru.yandex.practicum.storage.user.UserEntity;
import ru.yandex.practicum.storage.user.UserStorage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaRepositories(basePackages = "ru.yandex.practicum.storage")
@ComponentScan(basePackages = {"ru.yandex.practicum.storage", "ru.yandex.practicum.mapper"})
class EventStorageImplTest {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventStorage eventStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private CategoryStorage categoryStorage;
    @Autowired
    private LocationStorage locationStorage;

    @Test
    void addEvent() {
        User user = new User("name", "email@email.com");
        UserEntity initiator = getUserEntity(userStorage.addUser(user).getId());
        Category category = new Category();
        category.setName("categoryName");
        CategoryEntity categoryEntity = getCategoryEntity(categoryStorage.add(category).getId());

        LocalDateTime nowPlusOneMonth = LocalDateTime.now().plusMonths(1);
        EventRequest eventRequest = EventRequest.builder()
                .annotation("Sample Annotation")
                .description("Sample Description")
                .eventDate(nowPlusOneMonth)
                .paid(false)
                .participantLimit(100)
                .requestModeration(true)
                .title("Sample Title")
                .category(categoryEntity.getId())
                .location(getLocationDto())
                .build();

        EventFull eventFull = eventStorage.addEvent(eventRequest, initiator.getId());
        assertEquals("Sample Annotation", eventFull.getAnnotation());
        assertEquals("Sample Description", eventFull.getDescription());
        assertEquals(nowPlusOneMonth.truncatedTo(ChronoUnit.SECONDS), eventFull.getEventDate().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(categoryEntity.getId(), eventFull.getCategory().getId());
        assertEquals("Sample Title", eventFull.getTitle());
        assertEquals(100, eventFull.getParticipantLimit());
        assertFalse(eventFull.isPaid());
        assertTrue(eventFull.isRequestModeration());
        assertEquals(user.getEmail(), eventFull.getInitiator().getEmail());
        assertEquals(user.getName(), eventFull.getInitiator().getName());
        assertEquals(Double.valueOf("0.0"), eventFull.getLocation().getLat());
        assertEquals(Double.valueOf("0.0"), eventFull.getLocation().getLon());
    }


    private static UserEntity getUserEntity(Long userId) {
        return UserEntity.builder()
                .id(userId)
                .build();
    }

    private static CategoryEntity getCategoryEntity(Long categoryId) {
        return CategoryEntity.builder()
                .id(categoryId)
                .build();
    }

    private static Location getLocationDto() {
        Location location = new Location();
        location.setLat(Double.valueOf("0.0"));
        location.setLon(Double.valueOf("0.0"));
        return location;
    }

}