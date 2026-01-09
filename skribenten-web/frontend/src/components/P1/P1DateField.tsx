import { TextField, type TextFieldProps } from "@navikt/ds-react";

import { isoToDisplayDate } from "~/utils/dateUtils";

/**
 * Read-only date field (to be used in tab 2 and tab 5) that displays ISO date strings in Norwegian format.
 * Accepts date prop as ISO string (yyyy-MM-dd) and displays as dd.MM.yyyy.
 */
interface DateFieldProps extends Omit<TextFieldProps, "defaultValue" | "readOnly"> {
  date?: string;
}

export const DateField = ({ date, value, ...props }: DateFieldProps) => {
  const isoDate = date || (typeof value === "string" ? value : "") || "";
  const displayValue = isoToDisplayDate(isoDate);

  return <TextField className="p1-seamless-textfield" size="small" {...props} defaultValue={displayValue} readOnly />;
};
