package ru.yandex.practicum.storage.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompilationRepository extends JpaRepository<CompilationEntity, Long> {


    @Query("SELECT i FROM CompilationEntity i WHERE i.pinned = :pinned")
    Page<CompilationEntity> findAllConsideringPinned(boolean pinned, Pageable pageable);


}
