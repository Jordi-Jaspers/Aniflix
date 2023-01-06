export interface Page<T> {
    currentPage: number,
    hasNextPage: boolean,
    results: T[],
}
