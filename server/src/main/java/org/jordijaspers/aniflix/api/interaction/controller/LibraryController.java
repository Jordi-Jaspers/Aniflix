package org.jordijaspers.aniflix.api.interaction.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.anime.model.mapper.AnimeMapper;
import org.jordijaspers.aniflix.api.anime.model.response.AnimeResponse;
import org.jordijaspers.aniflix.api.interaction.model.request.KetsuData;
import org.jordijaspers.aniflix.api.interaction.model.request.LibrarySearchRequest;
import org.jordijaspers.aniflix.api.interaction.service.LibraryService;
import org.jordijaspers.aniflix.common.mappers.model.PageResource;
import org.jordijaspers.aniflix.security.principal.UserTokenPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.jordijaspers.aniflix.api.Paths.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The controller for the library.
 */
@RestController
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    private final AnimeMapper animeMapper;

    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = ANIME_LIBRARY_SEARCH_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResource<AnimeResponse>> searchLibrary(@RequestBody final LibrarySearchRequest request) {
        final Page<Anime> library = libraryService.searchInLibraryOfUser(request);
        return ResponseEntity.status(OK).body(animeMapper.toPageResource(library));
    }

    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = REMOVE_FROM_LIBRARY_PATH)
    public ResponseEntity<Void> removeFromLibrary(@PathVariable("id") final int anilistId) {
        libraryService.removeFromLibrary(anilistId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = ADD_TO_LIBRARY_PATH)
    public ResponseEntity<Void> addToLibrary(@PathVariable("id") final int anilistId) {
        libraryService.addToLibrary(anilistId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = LIBRARY_IMPORT_PATH, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importLibrary(@RequestBody final List<KetsuData> library,
                                           @AuthenticationPrincipal final UserTokenPrincipal principal) {
        libraryService.importLibrary(library, principal.getUser());
        return ResponseEntity.status(OK).build();
    }
}
