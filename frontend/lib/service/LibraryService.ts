import {WatchStatus} from "@enum/WatchStatus";
import {Anime} from "@interfaces/Anime";
import {pocketBase} from "@pocketbase/PocketBase";
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
    
    static async addAnimeToLibrary(userId: string, anime: Anime): Promise<void> {
        LOGGER.debug("[LibraryService] Adding anime to library.");
        const data = {
            "user_id": userId,
            "anime_id": anime.id,
            "title": anime.title,
            "total_episodes": anime.totalEpisodes,
            "lasts_seen_episode": 0,
            "watch_status": WatchStatus.NOT_STARTED,
            "rating": anime.rating,
            "status": anime.status,
            "image": anime.cover,
            "cover": anime.image
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
    
    static async removeAnimeFromLibrary(record: Record): Promise<void> {
        LOGGER.debug("[LibraryService] Removing anime from library.");
        return await pocketBase.collection('library')
            .delete(record.id)
            .then(() => {
                LOGGER.debug("[LibraryService] Successfully removed anime from the library.");
            }).catch((e: any) => {
                    throw ("[LibraryService] Failed to remove anime from library: \n" + e.message);
                }
            );
    }
    
}
