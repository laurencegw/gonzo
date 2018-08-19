import moment from "moment"

export const DATE_FORMAT = "YYYY-MM-DD"

/**
 * Format a Date to a string
 * @param date
 */
export function fDate(date: Date): string {
    return moment(date).format(DATE_FORMAT)
}

export function nowString(): string {
    return moment().format(DATE_FORMAT)
}
