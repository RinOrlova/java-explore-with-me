package ru.yandex.practicum.statistics.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<HitEntity, Long> {

    @Query(value = "SELECT h.app, h.uri, " +
            "COUNT(DISTINCT CASE WHEN :unique = true THEN h.ip ELSE h.id END) as count " +
            "FROM hits h " +
            "WHERE h.timestamp >= :start AND h.timestamp <= :end " +
            "GROUP BY h.app, h.uri", nativeQuery = true)
    List<Object[]> findAllStatistics(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end,
                                     @Param("unique") boolean unique);

    @Query(value = "SELECT h.app, h.uri, " +
            "COUNT(DISTINCT CASE WHEN :unique = true THEN h.ip ELSE h.id END) as count " +
            "FROM hits h " +
            "WHERE h.timestamp >= :start AND h.timestamp <= :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri", nativeQuery = true)
    List<Object[]> findStatisticsByURIs(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end,
                                        @Param("uris") List<String> uris,
                                        @Param("unique") boolean unique);
}
