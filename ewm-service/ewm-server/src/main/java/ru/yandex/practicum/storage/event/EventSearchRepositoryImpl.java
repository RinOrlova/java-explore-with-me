package ru.yandex.practicum.storage.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.dto.search.AdminSearch;
import ru.yandex.practicum.dto.search.PublicSearch;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EventSearchRepositoryImpl implements EventSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<EventEntity> findAllByAdminSearchParams(AdminSearch adminSearch, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> query = cb.createQuery(EventEntity.class);
        Root<EventEntity> event = query.from(EventEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (!CollectionUtils.isEmpty(adminSearch.getUserIds())) {
            predicates.add(event.get("initiator").get("id").in(adminSearch.getUserIds()));
        }

        if (!CollectionUtils.isEmpty(adminSearch.getStates())) {
            predicates.add(event.get("status").in(adminSearch.getStates()));
        }

        if (!CollectionUtils.isEmpty(adminSearch.getCategories())) {
            predicates.add(event.get("category").get("id").in(adminSearch.getCategories()));
        }

        if (adminSearch.getRangeStart() != null) {
            predicates.add(cb.greaterThanOrEqualTo(event.get("eventDate"), adminSearch.getRangeStart()));
        }

        if (adminSearch.getRangeEnd() != null) {
            predicates.add(cb.lessThanOrEqualTo(event.get("eventDate"), adminSearch.getRangeEnd()));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<EventEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<EventEntity> resultList = typedQuery.getResultList();
        long total = getCount(cb, predicates);

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public Page<EventEntity> findAllByPublicSearchParams(PublicSearch publicSearch, PageRequest pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> query = cb.createQuery(EventEntity.class);
        Root<EventEntity> event = query.from(EventEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (publicSearch.getPaid() != null) {
            predicates.add(cb.equal(event.get("paid"), publicSearch.getPaid()));
        }
        if (publicSearch.getText() != null && !publicSearch.getText().isEmpty()) {
            String searchText = "%" + publicSearch.getText().toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(event.get("description")), searchText),
                    cb.like(cb.lower(event.get("annotation")), searchText)
            ));
        }
        if (publicSearch.getRangeStart() != null) {
            predicates.add(cb.greaterThanOrEqualTo(event.get("eventDate"), publicSearch.getRangeStart()));
        }
        if (publicSearch.getRangeEnd() != null) {
            predicates.add(cb.lessThanOrEqualTo(event.get("eventDate"), publicSearch.getRangeEnd()));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<EventEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<EventEntity> resultList = typedQuery.getResultList();
        long total = getCount(cb, predicates);

        return new PageImpl<>(resultList, pageable, total);
    }

    private long getCount(CriteriaBuilder cb, List<Predicate> predicates) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<EventEntity> event = countQuery.from(EventEntity.class);
        countQuery.select(cb.count(event)).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
