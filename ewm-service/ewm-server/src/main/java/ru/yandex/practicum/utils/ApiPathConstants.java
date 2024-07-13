package ru.yandex.practicum.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPathConstants {
    public static final String COMPILATION_PATH = "/compilations";
    public static final String BY_ID_PATH = "/{id}";
    public static final String EVENT_BY_ID_PATH = "/{eventId}";
    public static final String CATEGORY_PATH = "/categories";
    public static final String REQUESTS_PATH = "/requests";
    public static final String REQUESTS_ID_PATH = "/{requestId}";
    public static final String EVENT_PATH = "/events";
    public static final String ADMIN_PATH = "/admin";
    public static final String USERS_PATH = "/users";
    public static final String CANCEL_PATH = "/cancel";
}
