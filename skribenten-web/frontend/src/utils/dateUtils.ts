import { format, formatISO, isToday, parseISO } from "date-fns";

export function formatDateWithoutTimezone(date: Date) {
  return formatISO(date, { representation: "date" });
}

export function parseDate(date: string) {
  return parseISO(date);
}

/**
 * Converts ISO date string (yyyy-MM-dd) to display format (dd.MM.yyyy).
 * Returns empty string if input is empty or invalid.
 */
export function isoToDisplayDate(isoDate: string): string {
  if (!isoDate) return "";
  try {
    const date = parseISO(isoDate);
    if (Number.isNaN(date.getTime())) return "";
    return format(date, "dd.MM.yyyy");
  } catch {
    return "";
  }
}

/**
 * Parses an ISO date string to a Date object.
 * Returns undefined if input is empty or invalid.
 */
export function parseIsoDateToDate(isoDate: string): Date | undefined {
  if (!isoDate) return undefined;
  try {
    const date = parseISO(isoDate);
    return Number.isNaN(date.getTime()) ? undefined : date;
  } catch {
    return undefined;
  }
}

export const formatStringDate = (date: string) => format(date, "dd.MM.yyyy");

export const formatStringDateWithTime = (date: string) => format(date, "dd.MM.yyyy HH:mm");

export const isDateToday = (date: string) => isToday(date);

export function formatTime(datetime: string): string {
  return format(datetime, "HH:mm");
}
