import capitalize from "lodash/capitalize";

export function capitalizeString(s: string) {
  return s.split(" ").map(capitalize).join(" ");
}
