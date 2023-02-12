
import AnimeService from "@consumet/AnimeService";
import {WatchStatus} from "@enum/WatchStatus";
import {pocketBase} from "@pocketbase/PocketBase";
import UserService from "@service/UserService";
import {LOGGER} from "@util/Logger";

import {Record} from "pocketbase";

export default class LibraryService {
    
    static async getLibrary(): Promise<Record[]> {
        LOGGER.debug("[LibraryService] Getting personal anime library of the user.");
        return await pocketBase.collection('library')
            .getFullList(200)
            .then((response) => {
                LOGGER.debug("[LibraryService] Successfully retrieved '%d' saved anime.", response.length);
                return response;
            }).catch((e: any) => {
                    throw ("[LibraryService] Failed to retrieve all saved anime: \n" + e.message);
                }
            );
    }
    
    static async getAnimeFromLibrary(animeId: string): Promise<Record | null> {
        LOGGER.debug("[LibraryService] Trying to find anime with id '%s' in library.", animeId);
        return await pocketBase.collection('library')
            .getFirstListItem(`anime_id = '${animeId}'`)
            .then((response) => {
                LOGGER.debug("[LibraryService] Successfully found anime with id '%s' in library.", animeId);
                return response;
            }).catch((e: any) => {
                LOGGER.debug("[LibraryService] Failed to find anime with id '%s' in library: %s", animeId);
                return null;
            });
    }
    
    static async addAnimeToLibrary(anime_id: string): Promise<void> {
        LOGGER.debug("[LibraryService] Trying to add anime with id '%s' to library.", anime_id);
        const userId = UserService.getUserInformation()?.id;
        if (!userId) throw ("[LibraryService] Failed to add anime to library: \n" + "User is not logged in.");
        
        const anime = await AnimeService.getAnimeDetails(anime_id);
        if (!anime) throw ("[LibraryService] Failed to add anime to library: \n" + "Anime with id '" + anime_id + "' does not exist.");
        
        const data = {
            "user_id": userId,
            "anime_id": anime.id,
            "title": anime.title.romaji,
            "total_episodes": anime.totalEpisodes,
            "lasts_seen_episode": 0,
            "watch_status": WatchStatus.NOT_STARTED,
            "rating": anime.rating,
            "status": anime.status.toUpperCase(),
            "image": anime.image,
            "cover": anime.cover
        };
        
        return await pocketBase.collection('library')
            .create(data)
            .then(() => {
                LOGGER.debug("[LibraryService] Successfully added anime to the library.");
            }).catch((e: any) => {
                    throw ("[LibraryService] Failed to add anime to library: \n" + e.message);
                }
            );
    }
    
    static async removeAnimeFromLibrary(anime_id: string): Promise<void> {
        LOGGER.debug("[LibraryService] Trying to remove anime with id '%s' to library.", anime_id);
        const userId = UserService.getUserInformation()?.id;
        if (!userId) throw ("[LibraryService] Failed to remove anime to library: \n" + "User is not logged in.");
        
        const record = await this.getAnimeFromLibrary(anime_id);
        if (!record) throw ("[LibraryService] Failed to remove anime to library: \n" + "Anime with id '" + anime_id + "' does not exist.");
        
        return await pocketBase.collection('library')
            .delete(record.id)
            .then(() => {
                LOGGER.debug("[LibraryService] Successfully removed anime from the library.");
            }).catch((e: any) => {
                throw ("[LibraryService] Failed to remove anime from library: \n" + e.message);
            });
    }
    
}
