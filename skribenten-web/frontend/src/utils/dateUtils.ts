import { formatISO, parseISO } from "date-fns";

export function formatDateWithoutTimezone(date: Date) {
  return formatISO(date, { representation: "date" });
}

export function parseDate(date: string) {
  return parseISO(date);
}
