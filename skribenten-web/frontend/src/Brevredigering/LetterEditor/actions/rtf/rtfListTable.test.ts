import { describe, expect, test } from "vitest";

import { parseListLevelFormats } from "~/Brevredigering/LetterEditor/actions/rtf/rtfListTable";
import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

const level = (nfc: number) =>
  `{\\listlevel\\levelnfc${nfc}\\levelnfcn${nfc}{\\leveltext\\'02\\'00.;}{\\levelnumbers\\'01;}}`;
const list = (listId: number, ...nfcs: number[]) =>
  `{\\list\\listtemplateid99${nfcs.map(level).join("")}{\\listname ;}\\listid${listId}}`;
const override = (listId: number, ls: number) => `{\\listoverride\\listid${listId}\\listoverridecount0\\ls${ls}}`;

const parse = (header: string) => parseListLevelFormats(tokenizeRtf(`{\\rtf1\\ansi${header}\\pard Tekst\\par}`));

describe("parseListLevelFormats", () => {
  test("maps each \\ls to the level formats of its list", () => {
    const formats = parse(
      `{\\*\\listtable${list(10, 23, 0, 4)}${list(20, 0)}}` +
        `{\\*\\listoverridetable${override(20, 1)}${override(10, 2)}}`,
    );

    expect(formats).toEqual(
      new Map([
        [1, [0]],
        [2, [23, 0, 4]],
      ]),
    );
  });

  test("uses \\levelnfcn when \\levelnfc is missing", () => {
    const formats = parse(
      `{\\*\\listtable{\\list{\\listlevel\\levelnfcn23}\\listid1}}{\\*\\listoverridetable${override(1, 1)}}`,
    );

    expect(formats.get(1)).toEqual([23]);
  });

  test("ignores level overrides in \\lfolevel", () => {
    const formats = parse(
      `{\\*\\listtable${list(1, 0)}}` +
        "{\\*\\listoverridetable{\\listoverride\\listid1\\listoverridecount1{\\lfolevel\\listoverrideformat{\\listlevel\\levelnfc23}}\\ls1}}",
    );

    expect(formats.get(1)).toEqual([0]);
  });

  // Modelled on Apache Tika's testRTFCorruptListOverride: overrides that point nowhere are left out.
  test("leaves out overrides of lists that are not in the list table", () => {
    const formats = parse(`{\\*\\listtable${list(1, 23)}}{\\*\\listoverridetable${override(2, 1)}${override(1, 2)}}`);

    expect(formats).toEqual(new Map([[2, [23]]]));
  });

  test("is empty without list tables, or with only one of them", () => {
    expect(parse("").size).toBe(0);
    expect(parse(`{\\*\\listtable${list(1, 23)}}`).size).toBe(0);
    expect(parse(`{\\*\\listoverridetable${override(1, 1)}}`).size).toBe(0);
  });

  test("tolerates an unterminated list table", () => {
    const tokens = tokenizeRtf(
      `{\\rtf1\\ansi{\\*\\listoverridetable${override(1, 1)}}{\\*\\listtable{\\list{\\listlevel\\levelnfc23`,
    );

    expect(parseListLevelFormats(tokens).size).toBe(0);
  });
});
