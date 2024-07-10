package storage.eventStorage;

import dto.event.EventShort;
import org.springframework.stereotype.Service;
import utils.Sort;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class EventStorageImpl implements EventStorage {
    @Override
    public Collection<EventShort> getEvents(String text, ArrayList<Integer> categoryId, Boolean paid, String rangeStart, String rangeEnd, String onlyAvailable, Sort sort, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventShort getEventById(Long id) {
        return null;
    }
}
