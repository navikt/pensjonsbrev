import { capitalize, startCase } from "lodash";

import type { FieldType } from "~/types/brevbakerTypes";

export function convertFieldToReadableLabel(field: string) {
  const lastFragment = field.split(".").at(-1);
  return capitalize(startCase(lastFragment));
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function getFieldDefaultValue(defaults: { [x: string]: any } | undefined, fieldName: string) {
  if (defaults === undefined) {
    return undefined;
  }

  const dotIndex = fieldName.indexOf(".");
  if (dotIndex >= 0) {
    return getFieldDefaultValue(defaults[fieldName.slice(0, dotIndex)], fieldName.slice(dotIndex + 1));
  }
  return defaults[fieldName];
}

export const isBooleanField = (fieldType: FieldType) => fieldType.type === "scalar" && fieldType.kind === "BOOLEAN";

export const isFieldNullableOrBoolean = (fieldType: FieldType) => fieldType.nullable || isBooleanField(fieldType);
