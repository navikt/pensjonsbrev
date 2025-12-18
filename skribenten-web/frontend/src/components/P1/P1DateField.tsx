import { TextField } from "@navikt/ds-react";
import { format } from "date-fns";

export const DateField = ({ ...props }) => {
  return (
    <TextField
      className="p1-seamless-textfield "
      label={props.label}
      size="small"
      {...props}
      defaultValue={(() => {
        const dateValue = props.date || props.value;
        if (!dateValue) return "";
        const date = new Date(dateValue);
        if (!date || Number.isNaN(date.getTime())) return "";
        return format(date, "dd.MM.yyyy");
      })()}
      readOnly
    />
  );
};
