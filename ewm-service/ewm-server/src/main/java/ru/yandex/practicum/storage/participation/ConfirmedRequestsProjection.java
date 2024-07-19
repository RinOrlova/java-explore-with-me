package ru.yandex.practicum.storage.participation;

public interface ConfirmedRequestsProjection {
    Long getEventId();

    Long getConfirmedRequestsNumber();
}
