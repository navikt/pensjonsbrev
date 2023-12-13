import { formatISO } from "date-fns";

export function formatDateWithoutTimezone(date: Date) {
  return formatISO(date, { representation: "date" });
}
