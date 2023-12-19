import capitalize from "lodash/capitalize";
export function convertFieldToReadableLabel(field: string) {
  const lastFragment = field.split(".").at(-1);
  return capitalize(lastFragment);
}
