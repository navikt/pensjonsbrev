import { Checkbox, TextField } from "@navikt/ds-react";

import type { TScalar } from "~/types/brevbakerTypes";

export const ScalarEditor = ({ fieldType, field }: { field: string; fieldType: TScalar }) => {
  switch (fieldType.kind) {
    case "NUMBER": {
      return <TextField label={field} required={!fieldType.nullable} step={1} type="number" />;
    }
    case "DOUBLE": {
      return <TextField label={field} required={!fieldType.nullable} step={0.1} type="number" />;
    }
    case "STRING": {
      return <TextField label={field} required={!fieldType.nullable} type="text" />;
    }
    case "BOOLEAN": {
      return <Checkbox>{field}</Checkbox>;
    }
    case "DATE": {
      return <input required={!fieldType.nullable} type="date" />;
    }
  }
};
