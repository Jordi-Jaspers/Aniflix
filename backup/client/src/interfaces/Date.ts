export interface Date {
    year: number,
    month: number,
    day: number,
}

export function getStringDate(date: Date | undefined): string {
    return date === undefined ? '' : `${date.year}-${date.month}-${date.day}`;
}
