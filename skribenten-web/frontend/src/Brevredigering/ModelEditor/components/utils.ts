import type { FieldType } from "~/types/brevbakerTypes";

export function convertFieldToReadableLabel(field: string) {
  const lastFragment = field.split(".").at(-1) ?? "";
  return norskeTegn(capitalizeAsSentence(splitWords(lastFragment)));
}

const reAcronym = /^[A-Z][A-Z0-9]+$/;
function capitalizeAsSentence(str: string) {
  return str
    .split(" ")
    .map((value, index) => {
      if (reAcronym.test(value)) {
        return value;
      } else if (index === 0) {
        return value.length > 0 ? value[0].toUpperCase() + value.slice(1) : value;
      } else {
        return value.length > 0 ? value[0].toLowerCase() + value.slice(1) : value;
      }
    })
    .reduce((previous, current) => previous + " " + current);
}

const reWords = /^[a-z][a-z0-9]+|[A-Z][a-z0-9]+|[A-Z]([A-Z0-9][^a-z])+/g;
function splitWords(str: string) {
  const words = str.match(reWords) ?? [];
  return words.reduce((previous, current) => previous + " " + current);
}

function norskeTegn(str: string) {
  return str
    .replaceAll("Ae", "Æ")
    .replaceAll("ae", "æ")
    .replaceAll("Oe", "Ø")
    .replaceAll("oe", "ø")
    .replaceAll("Aa", "Å")
    .replaceAll("aa", "å");
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function getFieldDefaultValue(defaults: { [x: string]: any } | undefined, fieldName: string) {
  if (defaults === undefined || defaults === null) {
    return defaults;
  }

  const dotIndex = fieldName.indexOf(".");
  if (dotIndex !== -1) {
    return getFieldDefaultValue(defaults[fieldName.slice(0, dotIndex)], fieldName.slice(dotIndex + 1));
  }
  return defaults[fieldName];
}

export const isBooleanField = (fieldType: FieldType) => fieldType.type === "scalar" && fieldType.kind === "BOOLEAN";

export const isFieldNullableOrBoolean = (fieldType: FieldType) => fieldType.nullable || isBooleanField(fieldType);
