package ru.yandex.practicum.statistics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.common.dto.StatisticsResponse;
import ru.yandex.practicum.common.utils.ApiPathConstants;
import ru.yandex.practicum.common.utils.StatisticsConstants;
import ru.yandex.practicum.statistics.service.DataTimeParamsEncoder;
import ru.yandex.practicum.statistics.service.StatisticsService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final DataTimeParamsEncoder dataTimeParamsEncoder;

    @PostMapping(ApiPathConstants.HIT_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRequest(@RequestBody HitRequest hitRequest) {
        statisticsService.saveRequest(hitRequest);
    }

    @GetMapping(ApiPathConstants.STATS_PATH)
    public Collection<StatisticsResponse> getStatistics(@DateTimeFormat(pattern = StatisticsConstants.DATE_FORMAT)
                                                  @RequestParam(value = "start") String startParam,
                                                        @DateTimeFormat(pattern = StatisticsConstants.DATE_FORMAT)
                                                  @RequestParam(value = "end") String endParam,
                                                        @RequestParam(required = false) List<String> uris,
                                                        @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        LocalDateTime start = dataTimeParamsEncoder.getLocalDateTimeParamOfEncodedValue(startParam);
        LocalDateTime end = dataTimeParamsEncoder.getLocalDateTimeParamOfEncodedValue(endParam);
        return statisticsService.getStatistics(start, end, uris, unique);
    }

}
