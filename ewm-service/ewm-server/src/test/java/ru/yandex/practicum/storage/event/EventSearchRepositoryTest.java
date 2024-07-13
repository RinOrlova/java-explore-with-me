package ru.yandex.practicum.storage.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.dto.event.EventStatus;
import ru.yandex.practicum.dto.location.Location;
import ru.yandex.practicum.dto.search.AdminSearch;
import ru.yandex.practicum.dto.search.PublicSearch;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.storage.category.CategoryEntity;
import ru.yandex.practicum.storage.category.CategoryStorage;
import ru.yandex.practicum.storage.location.LocationEntity;
import ru.yandex.practicum.storage.location.LocationStorage;
import ru.yandex.practicum.storage.user.UserEntity;
import ru.yandex.practicum.storage.user.UserStorage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaRepositories(basePackages = "ru.yandex.practicum.storage")
@ComponentScan(basePackages = {"ru.yandex.practicum.storage", "ru.yandex.practicum.mapper"})
public class EventSearchRepositoryTest {

    @Autowired
    private EventSearchRepository eventSearchRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private CategoryStorage categoryStorage;
    @Autowired
    private LocationStorage locationStorage;

    @Test
    public void testFindAllByAdminSearchParams_withUserIds() {
        User user = new User("name", "email@email.com");
        UserEntity initiator = getUserEntity(userStorage.addUser(user).getId());
        Category category = new Category();
        category.setName("categoryName");
        CategoryEntity categoryEntity = getCategoryEntity(categoryStorage.add(category).getId());
        LocationEntity locationEntity = locationStorage.addLocationEntityIfAbsent(getLocationEntity());

        EventEntity event = EventEntity.builder()
                .id(1L)
                .annotation("Sample Annotation")
                .description("Sample Description")
                .createdOn(LocalDateTime.now().minusDays(1))
                .eventDate(LocalDateTime.now())
                .publishedOn(null)
                .paid(false)
                .participantLimit(100)
                .requestModeration(true)
                .status(EventStatus.PENDING)
                .title("Sample Title")
                .category(categoryEntity)
                .initiator(initiator)
                .location(locationEntity)
                .build();
        eventRepository.save(event);

        // Search with matching UserId
        AdminSearch adminSearch = AdminSearch.builder()
                .userIds(Collections.singletonList(1L))
                .build();

        Page<EventEntity> result = eventSearchRepository.findAllByAdminSearchParams(adminSearch, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getInitiator().getId()).isEqualTo(1L);

        // Search with mismatching UserId
        AdminSearch adminSearchMismatchByUserId = AdminSearch.builder()
                .userIds(Collections.singletonList(2L))
                .build();

        Page<EventEntity> resultMismatchByUserId = eventSearchRepository.findAllByAdminSearchParams(adminSearchMismatchByUserId, PageRequest.of(0, 10));
        assertTrue(resultMismatchByUserId.isEmpty());

        // Create AdminSearch object with states
        AdminSearch adminSearchStates = AdminSearch.builder()
                .states(Collections.singletonList(EventStatus.PENDING))
                .build();
        Page<EventEntity> resultStates = eventSearchRepository.findAllByAdminSearchParams(adminSearchStates, PageRequest.of(0, 10));
        assertThat(resultStates.getContent()).hasSize(1);
        assertThat(resultStates.getContent().get(0).getStatus()).isEqualTo(EventStatus.PENDING);
    }

    @Test
    public void testFindAllByAdminSearchParams_withStates() {
        User user = new User("name", "email@email.com");
        UserEntity initiator = getUserEntity(userStorage.addUser(user).getId());
        Category category = new Category();
        category.setName("categoryName");
        CategoryEntity categoryEntity = getCategoryEntity(categoryStorage.add(category).getId());
        LocationEntity locationEntity = locationStorage.addLocationEntityIfAbsent(getLocationEntity());

        EventEntity event = EventEntity.builder()
                .id(1L)
                .annotation("Sample Annotation")
                .description("Sample Description")
                .createdOn(LocalDateTime.now().minusDays(1))
                .eventDate(LocalDateTime.now())
                .publishedOn(null)
                .paid(false)
                .participantLimit(100)
                .requestModeration(true)
                .status(EventStatus.PUBLISHED)
                .title("Sample Title")
                .category(categoryEntity)
                .initiator(initiator)
                .location(locationEntity)
                .build();
        eventRepository.save(event);

        // Create AdminSearch with states
        AdminSearch adminSearchStates = AdminSearch.builder()
                .states(Collections.singletonList(EventStatus.PUBLISHED))
                .build();
        Page<EventEntity> resultStates = eventSearchRepository.findAllByAdminSearchParams(adminSearchStates, PageRequest.of(0, 10));
        assertThat(resultStates.getContent()).hasSize(1);
        assertThat(resultStates.getContent().get(0).getStatus()).isEqualTo(EventStatus.PUBLISHED);

        // Create AdminSearch mismatching states
        AdminSearch adminSearchMismatchingStates = AdminSearch.builder()
                .states(Collections.singletonList(EventStatus.CANCELLED))
                .build();
        Page<EventEntity> resultMismatchingStates = eventSearchRepository.findAllByAdminSearchParams(adminSearchMismatchingStates, PageRequest.of(0, 10));
        assertTrue(resultMismatchingStates.isEmpty());
    }

    @Test
    public void testFindAllByAdminSearchParams_withCategories() {
        User user = new User("name", "email@email.com");
        UserEntity initiator = getUserEntity(userStorage.addUser(user).getId());
        Category category = new Category();
        category.setName("categoryName");
        CategoryEntity categoryEntity = getCategoryEntity(categoryStorage.add(category).getId());
        LocationEntity locationEntity = locationStorage.addLocationEntityIfAbsent(getLocationEntity());

        EventEntity event = EventEntity.builder()
                .id(1L)
                .annotation("Sample Annotation")
                .description("Sample Description")
                .createdOn(LocalDateTime.now().minusDays(1))
                .eventDate(LocalDateTime.now())
                .publishedOn(null)
                .paid(false)
                .participantLimit(100)
                .requestModeration(true)
                .status(EventStatus.PUBLISHED)
                .title("Sample Title")
                .category(categoryEntity)
                .initiator(initiator)
                .location(locationEntity)
                .build();
        eventRepository.save(event);

        // Create AdminSearch with categories
        AdminSearch adminSearch = AdminSearch.builder()
                .categories(Collections.singletonList(categoryEntity.getId()))
                .build();
        Page<EventEntity> result = eventSearchRepository.findAllByAdminSearchParams(adminSearch, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCategory().getId()).isEqualTo(categoryEntity.getId());
        // Create AdminSearch with categories
        AdminSearch adminSearchMismatchCatId = AdminSearch.builder()
                .categories(Collections.singletonList(999L))
                .build();
        Page<EventEntity> resultMismatchCatId = eventSearchRepository.findAllByAdminSearchParams(adminSearchMismatchCatId, PageRequest.of(0, 10));
        assertTrue(resultMismatchCatId.isEmpty());
    }

    @Test
    public void testFindAllByAdminSearchParams_withDateRange() {
        User user = new User("name", "email@email.com");
        UserEntity initiator = getUserEntity(userStorage.addUser(user).getId());
        Category category = new Category();
        category.setName("categoryName");
        CategoryEntity categoryEntity = getCategoryEntity(categoryStorage.add(category).getId());
        LocationEntity locationEntity = locationStorage.addLocationEntityIfAbsent(getLocationEntity());

        EventEntity event = EventEntity.builder()
                .id(1L)
                .annotation("Sample Annotation")
                .description("Sample Description")
                .createdOn(LocalDateTime.now())
                .eventDate(LocalDateTime.now().plusHours(2))
                .publishedOn(null)
                .paid(false)
                .participantLimit(100)
                .requestModeration(true)
                .status(EventStatus.PUBLISHED)
                .title("Sample Title")
                .category(categoryEntity)
                .initiator(initiator)
                .location(locationEntity)
                .build();
        eventRepository.save(event);

        // Create AdminSearch within date range
        AdminSearch adminSearch = AdminSearch.builder()
                .rangeStart(LocalDateTime.now().plusHours(1))
                .rangeEnd(LocalDateTime.now().plusHours(3))
                .build();
        Page<EventEntity> result = eventSearchRepository.findAllByAdminSearchParams(adminSearch, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
        // Create AdminSearch out of date range
        AdminSearch adminSearchOutOfRange = AdminSearch.builder()
                .rangeStart(LocalDateTime.now().plusHours(5))
                .rangeEnd(LocalDateTime.now().plusHours(6))
                .build();
        Page<EventEntity> resultOutOfRange = eventSearchRepository.findAllByAdminSearchParams(adminSearchOutOfRange, PageRequest.of(0, 10));
        assertTrue(resultOutOfRange.isEmpty());
    }

    @Test
    public void testFindAllByAdminSearchParams_withMultipleCriteria() {
        User user = new User("name", "email@email.com");
        UserEntity initiator = getUserEntity(userStorage.addUser(user).getId());
        Category category = new Category();
        category.setName("categoryName");
        CategoryEntity categoryEntity = getCategoryEntity(categoryStorage.add(category).getId());
        LocationEntity locationEntity = locationStorage.addLocationEntityIfAbsent(getLocationEntity());

        EventEntity event = EventEntity.builder()
                .id(1L)
                .annotation("Sample Annotation")
                .description("Sample Description")
                .createdOn(LocalDateTime.now())
                .eventDate(LocalDateTime.now().plusHours(2))
                .publishedOn(null)
                .paid(false)
                .participantLimit(100)
                .requestModeration(true)
                .status(EventStatus.PUBLISHED)
                .title("Sample Title")
                .category(categoryEntity)
                .initiator(initiator)
                .location(locationEntity)
                .build();
        eventRepository.save(event);


        // Create AdminSearch object with multiple criteria
        AdminSearch adminSearch = AdminSearch.builder()
                .userIds(Collections.singletonList(initiator.getId()))
                .states(Collections.singletonList(EventStatus.PUBLISHED))
                .categories(Collections.singletonList(categoryEntity.getId()))
                .rangeStart(LocalDateTime.now().plusHours(1))
                .rangeEnd(LocalDateTime.now().plusHours(3))
                .build();

        // Perform the search
        Page<EventEntity> result = eventSearchRepository.findAllByAdminSearchParams(adminSearch, PageRequest.of(0, 10));

        // Validate the search results
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getInitiator().getId()).isEqualTo(initiator.getId());
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(EventStatus.PUBLISHED);
        assertThat(result.getContent().get(0).getCategory().getId()).isEqualTo(categoryEntity.getId());
    }

    @Test
    void findAllByPublicSearchParams_text() {
        User user = new User("name", "email@email.com");
        UserEntity initiator = getUserEntity(userStorage.addUser(user).getId());
        Category category = new Category();
        category.setName("categoryName");
        CategoryEntity categoryEntity = getCategoryEntity(categoryStorage.add(category).getId());
        LocationEntity locationEntity = locationStorage.addLocationEntityIfAbsent(getLocationEntity());

        EventEntity event = EventEntity.builder()
                .id(1L)
                .annotation("Sample Event Annotation")
                .description("Sample Event Description")
                .createdOn(LocalDateTime.now().minusDays(1))
                .eventDate(LocalDateTime.now())
                .publishedOn(null)
                .paid(false)
                .participantLimit(100)
                .requestModeration(true)
                .status(EventStatus.PUBLISHED)
                .title("Sample Title")
                .category(categoryEntity)
                .initiator(initiator)
                .location(locationEntity)
                .build();
        eventRepository.save(event);

        PublicSearch publicSearch = PublicSearch.builder()
                .text("sAmPLe EvEnT")
                .build();
        Page<EventEntity> result = eventSearchRepository.findAllByPublicSearchParams(publicSearch, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
        PublicSearch publicSearchMismatch = PublicSearch.builder()
                .text("Mismatch EvEnT")
                .build();
        Page<EventEntity> resultMismatch = eventSearchRepository.findAllByPublicSearchParams(publicSearchMismatch, PageRequest.of(0, 10));
        assertTrue(resultMismatch.isEmpty());
    }

    @Test
    void findAllByPublicSearchParams__withDateRange() {
        User user = new User("name", "email@email.com");
        UserEntity initiator = getUserEntity(userStorage.addUser(user).getId());
        Category category = new Category();
        category.setName("categoryName");
        CategoryEntity categoryEntity = getCategoryEntity(categoryStorage.add(category).getId());
        LocationEntity locationEntity = locationStorage.addLocationEntityIfAbsent(getLocationEntity());

        EventEntity event = EventEntity.builder()
                .id(1L)
                .annotation("Sample Event Annotation")
                .description("Sample Event Description")
                .createdOn(LocalDateTime.now().minusDays(1))
                .eventDate(LocalDateTime.now().plusHours(2))
                .publishedOn(null)
                .paid(false)
                .participantLimit(100)
                .requestModeration(true)
                .status(EventStatus.PUBLISHED)
                .title("Sample Title")
                .category(categoryEntity)
                .initiator(initiator)
                .location(locationEntity)
                .build();
        eventRepository.save(event);

        PublicSearch publicSearch = PublicSearch.builder()
                .rangeStart(LocalDateTime.now().plusHours(1))
                .rangeEnd(LocalDateTime.now().plusHours(3))
                .build();
        Page<EventEntity> result = eventSearchRepository.findAllByPublicSearchParams(publicSearch, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
        PublicSearch publicSearchOutOfRange = PublicSearch.builder()
                .rangeStart(LocalDateTime.now().plusHours(5))
                .rangeEnd(LocalDateTime.now().plusHours(6))
                .build();
        Page<EventEntity> resultOutOfRange = eventSearchRepository.findAllByPublicSearchParams(publicSearchOutOfRange, PageRequest.of(0, 10));
        assertTrue(resultOutOfRange.isEmpty());
    }

    @Test
    void findAllByPublicSearchParams_paid() {
        User user = new User("name", "email@email.com");
        UserEntity initiator = getUserEntity(userStorage.addUser(user).getId());
        Category category = new Category();
        category.setName("categoryName");
        CategoryEntity categoryEntity = getCategoryEntity(categoryStorage.add(category).getId());
        LocationEntity locationEntity = locationStorage.addLocationEntityIfAbsent(getLocationEntity());

        EventEntity event = EventEntity.builder()
                .id(1L)
                .annotation("Sample Event Annotation")
                .description("Sample Event Description")
                .createdOn(LocalDateTime.now().minusDays(1))
                .eventDate(LocalDateTime.now())
                .publishedOn(null)
                .paid(false)
                .participantLimit(100)
                .requestModeration(true)
                .status(EventStatus.PUBLISHED)
                .title("Sample Title")
                .category(categoryEntity)
                .initiator(initiator)
                .location(locationEntity)
                .build();
        EventEntity eventPaid = EventEntity.builder()
                .id(2L)
                .annotation("Sample Paid Event Annotation")
                .description("Sample Paid Event Description")
                .createdOn(LocalDateTime.now().minusDays(1))
                .eventDate(LocalDateTime.now())
                .publishedOn(null)
                .paid(true)
                .participantLimit(100)
                .requestModeration(true)
                .status(EventStatus.PUBLISHED)
                .title("Sample Paid Title")
                .category(categoryEntity)
                .initiator(initiator)
                .location(locationEntity)
                .build();
        eventRepository.saveAll(List.of(event, eventPaid));


        PublicSearch publicSearchPaid = PublicSearch.builder()
                .paid(true)
                .build();
        Page<EventEntity> resultPaid = eventSearchRepository.findAllByPublicSearchParams(publicSearchPaid, PageRequest.of(0, 10));
        assertThat(resultPaid.getContent()).hasSize(1);
        List<EventEntity> contentPaid = resultPaid.getContent();
        assertThat(contentPaid).hasSize(1);
        assertThat(contentPaid.get(0).getAnnotation()).isEqualTo("Sample Paid Event Annotation");
        PublicSearch publicSearchFree = PublicSearch.builder()
                .paid(false)
                .build();
        Page<EventEntity> resultFree = eventSearchRepository.findAllByPublicSearchParams(publicSearchFree, PageRequest.of(0, 10));
        List<EventEntity> contentFree = resultFree.getContent();
        assertThat(contentFree).hasSize(1);
        assertThat(contentFree.get(0).getAnnotation()).isEqualTo("Sample Event Annotation");
        PublicSearch publicSearchAll = PublicSearch.builder()
                .build();
        Page<EventEntity> resultAll = eventSearchRepository.findAllByPublicSearchParams(publicSearchAll, PageRequest.of(0, 10));
        assertThat(resultAll.getContent()).hasSize(2);

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

    private static Location getLocationEntity() {
        Location location = new Location();
        location.setLat(Double.valueOf("0.0"));
        location.setLon(Double.valueOf("0.0"));
        return location;
    }
}
