package storage.eventStorage;

import dto.event.EventShort;
import utils.Sort;

import java.util.ArrayList;
import java.util.Collection;

public interface EventStorage {
    Collection<EventShort> getEvents(String text, ArrayList<Integer> categoryId, Boolean paid, String rangeStart, String rangeEnd, String onlyAvailable, Sort sort, Integer from, Integer size);

    EventShort getEventById(Long id);
}
