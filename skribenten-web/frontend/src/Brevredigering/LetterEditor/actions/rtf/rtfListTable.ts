import { type RtfToken } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

/**
 * List formats from the RTF list tables, like Apache Tika's `ListDescriptor`:
 * `{\*\listtable{\list{\listlevel\levelnfcN …}…\listidN}}` defines the number format of each level,
 * and `{\*\listoverridetable{\listoverride\listidN…\lsM}}` maps the `\lsM` used in the body to a list.
 * `\lfolevel` overrides are ignored.
 */

/** Number format (`\levelnfc`) of each list level (`\ilvl`), by list override (`\ls`). */
export type ListLevelFormats = ReadonlyMap<number, readonly (number | undefined)[]>;

/** `\levelnfc23`: bullet. */
export const LEVEL_FORMAT_BULLET = 23;
/** `\levelnfc255`: no number, the paragraph is indented like a list item but is not one. */
export const LEVEL_FORMAT_NONE = 255;

/**
 * Calls `onControl` for every control word in the destination group that starts with `destination`,
 * with the path of destinations from that group to the current one, and `onGroupEnd` when a group in it ends.
 */
function scanDestination(
  tokens: readonly RtfToken[],
  destination: string,
  visit: {
    onControl: (path: readonly string[], word: string, param: number | undefined) => void;
    onGroupEnd: (path: readonly string[]) => void;
  },
) {
  const start = tokens.findIndex((token) => token.type === "control" && token.word === destination);
  if (start === -1) return;

  // The destination of each open group, from its first control word other than `\*`.
  const path: (string | undefined)[] = [destination];
  for (let index = start + 1; index < tokens.length && path.length > 0; index++) {
    const token = tokens[index];
    if (token.type === "groupStart") {
      path.push(undefined);
    } else if (token.type === "groupEnd") {
      visit.onGroupEnd(path.map((word) => word ?? ""));
      path.pop();
    } else if (token.type === "control" && token.word !== "*") {
      if (path.at(-1) === undefined) path[path.length - 1] = token.word;
      else
        visit.onControl(
          path.map((word) => word ?? ""),
          token.word,
          token.hasParam ? token.param : undefined,
        );
    }
  }
}

const isPath = (path: readonly string[], ...expected: string[]) =>
  path.length === expected.length && path.every((word, index) => word === expected[index]);

/** `listId → levelnfc` per level, from `\listtable`. */
function parseLists(tokens: readonly RtfToken[]): Map<number, (number | undefined)[]> {
  const lists = new Map<number, (number | undefined)[]>();
  let listId: number | undefined;
  let levels: (number | undefined)[] = [];
  let level: { nfc?: number; nfcn?: number } = {};

  scanDestination(tokens, "listtable", {
    onControl(path, word, param) {
      if (isPath(path, "listtable", "list") && word === "listid") listId = param;
      else if (isPath(path, "listtable", "list", "listlevel")) {
        if (word === "levelnfc") level.nfc = param;
        else if (word === "levelnfcn") level.nfcn = param;
      }
    },
    onGroupEnd(path) {
      if (isPath(path, "listtable", "list", "listlevel")) {
        levels.push(level.nfc ?? level.nfcn);
        level = {};
      } else if (isPath(path, "listtable", "list")) {
        if (listId !== undefined) lists.set(listId, levels);
        listId = undefined;
        levels = [];
      }
    },
  });
  return lists;
}

/** `ls → listId`, from `\listoverridetable`. */
function parseListOverrides(tokens: readonly RtfToken[]): Map<number, number> {
  const overrides = new Map<number, number>();
  let override: { listId?: number; ls?: number } = {};

  scanDestination(tokens, "listoverridetable", {
    onControl(path, word, param) {
      if (!isPath(path, "listoverridetable", "listoverride")) return;
      if (word === "listid") override.listId = param;
      else if (word === "ls") override.ls = param;
    },
    onGroupEnd(path) {
      if (!isPath(path, "listoverridetable", "listoverride")) return;
      if (override.listId !== undefined && override.ls !== undefined) overrides.set(override.ls, override.listId);
      override = {};
    },
  });
  return overrides;
}

/** Number format of each level of each `\ls`. Overrides that point to a missing list are left out. */
export function parseListLevelFormats(tokens: readonly RtfToken[]): ListLevelFormats {
  const lists = parseLists(tokens);
  const formats = new Map<number, readonly (number | undefined)[]>();
  for (const [ls, listId] of parseListOverrides(tokens)) {
    const levels = lists.get(listId);
    if (levels) formats.set(ls, levels);
  }
  return formats;
}
