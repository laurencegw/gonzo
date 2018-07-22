import moment from "moment"
/**
 * Format a Date to a string
 * @param date
 */
export function fDate(date: Date): string {
    return moment(date).format("YYYY-M-D H:m")
}
