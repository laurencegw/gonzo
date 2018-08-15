import moment from "moment"

export const DATE_TIME_FORMAT = "YYYY-M-D H:m"

/**
 * Format a Date to a string
 * @param date
 */
export function fDate(date: Date): string {
    return moment(date).format(DATE_TIME_FORMAT)
}

export function nowString(): string {
    return moment().format(DATE_TIME_FORMAT)
}
