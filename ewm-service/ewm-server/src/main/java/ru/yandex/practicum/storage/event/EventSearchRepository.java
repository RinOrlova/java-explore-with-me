package ru.yandex.practicum.storage.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.search.AdminSearch;
import ru.yandex.practicum.dto.search.PublicSearch;

public interface EventSearchRepository {
    Page<EventEntity> findAllByAdminSearchParams(AdminSearch adminSearch, Pageable pageable);

    Page<EventEntity> findAllByPublicSearchParams(PublicSearch publicSearch, PageRequest pageRequest);
}
