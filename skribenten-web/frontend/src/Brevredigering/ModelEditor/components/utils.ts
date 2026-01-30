/**
 * Converts a field path to a readable, human-friendly label.
 *
 * Processes the last segment of a dot-separated field path by splitting words,
 * capitalizing as a sentence, and replacing common abbreviations with Norwegian characters.
 *
 * @param field - A dot-separated field path (e.g., "user.address.streetName")
 * @returns A formatted, readable label with proper capitalization and Norwegian character substitutions
 *
 * @example
 * convertFieldToReadableLabel("user.firstName") // "First name"
 * convertFieldToReadableLabel("address.Ae") // "Address Æ"
 */

/**
 * Retrieves a default value from a nested defaults object using a dot-separated field name.
 *
 * Uses generic typing with T = unknown instead of 'any' to provide type safety while
 * maintaining flexibility. This approach:
 * - Preserves type information throughout the object hierarchy
 * - Allows the return type to be inferred from the defaults parameter
 * - Prevents accidental type coercion and catches type errors at compile time
 * - Makes the API contract explicit: callers know the return type depends on the input
 *
 * Compare to [x: string]: any, which loses all type information and disables TypeScript's
 * type checking, potentially hiding bugs and reducing IDE intellisense support.
 *
 * @template T - The value type stored in the defaults object (defaults to unknown)
 * @param defaults - A nested object containing default values, or undefined/null
 * @param fieldName - A dot-separated path to the desired field (e.g., "config.timeout")
 * @returns The default value at the specified path, or undefined if not found
 *
 * @example
 * const defaults = { user: { name: "John", age: 30 } };
 * getFieldDefaultValue(defaults, "user.name") // "John" (type: string)
 * getFieldDefaultValue(defaults, "user.age") // 30 (type: number)
 */

/**
 * Type guard to check if a field is a Boolean scalar type.
 *
 * @param field - The field type definition to check
 * @returns true if the field is a scalar Boolean type
 */

/**
 * Type guard to check if a field is either nullable or a Boolean type.
 *
 * @param field - The field type definition to check
 * @returns true if the field is nullable or a Boolean scalar type
 */
import type { FieldType } from "~/types/brevbakerTypes";

export function convertFieldToReadableLabel(field: string) {
  const lastFragment = field.split(".").at(-1) ?? "";
  return norskeTegn(capitalizeAsSentence(splitWords(lastFragment)));
}

const reAcronym = /^[A-ZÆØÅ][A-Z0-9ÆØÅ]+$/;
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
    .reduce((previous, current) => `${previous} ${current}`);
}

const reWords = /^[a-zæøå][a-z0-9æøå]+|[A-ZÆØÅ][a-z0-9æøå]+|[A-ZÆØÅ]([A-Z0-9ÆØÅ][^a-zæøå])+/g;
function splitWords(str: string) {
  const words = str.match(reWords) ?? [];
  return words.reduce((previous, current) => `${previous} ${current}`);
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

export function getFieldDefaultValue<T = unknown>(
  defaults: { [x: string]: T } | undefined,
  fieldName: string,
): T | undefined {
  if (defaults === undefined || defaults === null) {
    return defaults;
  }

  const dotIndex = fieldName.indexOf(".");
  if (dotIndex !== -1) {
    return getFieldDefaultValue(
      defaults[fieldName.slice(0, dotIndex)] as { [x: string]: T } | undefined,
      fieldName.slice(dotIndex + 1),
    );
  }
  return defaults[fieldName];
}

export const isBooleanField = (field: FieldType) => field.type === "scalar" && field.kind === "BOOLEAN";

export const isFieldNullableOrBoolean = (field: FieldType) => field.nullable || isBooleanField(field);
