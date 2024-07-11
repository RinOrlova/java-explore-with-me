package ru.yandex.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.compilation.CompilationRequest;
import ru.yandex.practicum.dto.compilation.CompilationResponse;
import ru.yandex.practicum.service.compilation.CompilationService;
import ru.yandex.practicum.utils.ApiPathConstants;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.ADMIN_PATH + ApiPathConstants.COMPILATION_PATH)
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponse createCompilation(@RequestBody CompilationRequest compilationRequest){
        return compilationService.addCompilation(compilationRequest);
    }


    @DeleteMapping(ApiPathConstants.BY_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@Positive @PathVariable (name = "id") Long id) {
        compilationService.deleteCompilation(id);
    }

    @PatchMapping(ApiPathConstants.BY_ID_PATH)
    public CompilationResponse updateCompilation(@PathVariable(name="id") @Positive Long id,
                                                 @RequestBody CompilationRequest compilationRequest){
        return compilationService.updateCompilation(id, compilationRequest);
    }

}
