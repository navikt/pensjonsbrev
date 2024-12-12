import capitalize from "lodash/capitalize";

export function humanizeName(s: string) {
  return s.split(" ").map(capitalize).join(" ");
}
