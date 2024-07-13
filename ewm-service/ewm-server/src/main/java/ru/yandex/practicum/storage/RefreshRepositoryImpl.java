package ru.yandex.practicum.storage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class RefreshRepositoryImpl implements RefreshRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void refresh(Object o) {
        entityManager.refresh(o);
    }
}
