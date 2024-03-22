package org.jordijaspers.aniflix.api.interaction.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.api.interaction.model.mapper.InteractionMapper;
import org.jordijaspers.aniflix.api.interaction.model.request.InteractionRequest;
import org.jordijaspers.aniflix.api.interaction.model.request.KetsuData;
import org.jordijaspers.aniflix.api.interaction.model.response.InteractionResponse;
import org.jordijaspers.aniflix.api.interaction.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.jordijaspers.aniflix.api.Paths.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    private final InteractionMapper interactionMapper;

    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = ANIME_LIBRARY_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InteractionResponse>> getFullLibrary() {
        final List<Interaction> library = libraryService.getFullLibraryForUser();
        return ResponseEntity.status(OK).body(interactionMapper.toBasicResponse(library));
    }

    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = ANIME_LIBRARY_PATH, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addToLibrary(@RequestBody final InteractionRequest request) {
        libraryService.addToLibrary(new Anime(request.getAnilistId()));
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(path = ANIME_LIBRARY_PATH, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeFromLibrary(@RequestBody final InteractionRequest request) {
        libraryService.removeFromLibrary(new Anime(request.getAnilistId()));
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = ANIME_LIBRARY_SEARCH_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InteractionResponse>> searchLibrary(@RequestParam("q") final String title) {
        final List<Interaction> library = libraryService.searchInLibraryOfUser(title);
        return ResponseEntity.status(OK).body(interactionMapper.toBasicResponse(library));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = LIBRARY_IMPORT_PATH, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importLibrary(@RequestBody final List<KetsuData> library) {
        libraryService.importLibrary(library);
        return ResponseEntity.status(OK).build();
    }
}
