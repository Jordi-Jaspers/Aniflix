package org.jordijaspers.aniflix.api.consumed.anizip.repository;

import org.jordijaspers.aniflix.api.consumed.anizip.model.AnizipInfo;

/**
 * The repository for the Jikan API.
 */
public interface AnizipRepository {

    /**
     * Returns all the episode information of an Anime specified by its Mal id.
     */
    AnizipInfo getAnizipInfoByMalId(int id);

    /**
     * Returns all the episode information of an Anime specified by its aniList id.
     */
    AnizipInfo getAnizipInfoByAniListId(int id);
}
