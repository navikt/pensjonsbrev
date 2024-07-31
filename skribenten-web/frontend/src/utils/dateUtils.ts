import { format, formatISO, parseISO } from "date-fns";

export function formatDateWithoutTimezone(date: Date) {
  return formatISO(date, { representation: "date" });
}

export function parseDate(date: string) {
  return parseISO(date);
}

export function formatTime(datetime: string): string {
  return format(datetime, "HH:mm");
}
