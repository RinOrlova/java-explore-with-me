package ru.yandex.practicum.storage.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilationRepository extends JpaRepository<CompilationEntity, Long> {


}
