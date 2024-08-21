import { format, formatISO, isToday, parseISO } from "date-fns";

export function formatDateWithoutTimezone(date: Date) {
  return formatISO(date, { representation: "date" });
}

export function parseDate(date: string) {
  return parseISO(date);
}

export const formatStringDate = (date: string) => format(date, "dd.MM.yyyy");

export const formatStringDateWithTime = (date: string) => format(date, "dd.MM.yyyy HH:mm");

export const isDateToday = (date: string) => isToday(date);

export function formatTime(datetime: string): string {
  return format(datetime, "HH:mm");
}
