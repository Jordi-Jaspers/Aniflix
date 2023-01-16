export interface Page<T> {
    currentPage: number,
    hasNextPage: boolean,
    totalPages?: number,
    totalResults?: number,
    results: T[],
}
