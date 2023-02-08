export enum WatchStatus {
    NOT_STARTED = "NOT_STARTED",
    WATCHING = "WATCHING",
    COMPLETED = "COMPLETED",
}

export const WATCH_STATUS_LIST: string[] = Object.values(WatchStatus);
