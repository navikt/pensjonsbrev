import { describe, expect, it } from "vitest";

import {
  blockMoveTargets,
  canMergeWithNext,
  cloneBlockWithNewIds,
  containsLockedSource,
  countFieldReferences,
  duplicateBlockAt,
  fieldsInScope,
  flattenBlocks,
  indexFields,
  languagesWithContent,
  mergeWithNext,
  moveItem,
  newDraft,
  relocateBlock,
  removeBlockAt,
  splitBlock,
  withTranslation,
} from "./draftOps";
import { canRedo, canUndo, createHistory, pushHistory, redo, undo } from "./history";
import { type Block, type Draft, type Field, type Inline } from "./types";

const text = (nb: string, nn = "", en = ""): Inline => ({
  type: "TEXT",
  text: { NB: nb, NN: nn, EN: en },
  fontType: "PLAIN",
});

describe("moving, copying and splitting blocks", () => {
  const a: Block = { type: "PARAGRAPH", id: "a", text: [text("A", "AN", "AE")] };
  const b: Block = { type: "PARAGRAPH", id: "b", text: [text("B")] };
  const cond: Block = {
    type: "CONDITIONAL",
    id: "c",
    predicate: { type: "IS_NOT_NULL", fieldId: "f" },
    ifTrue: [a],
    ifFalse: [b],
  };

  it("keeps ids when moving", () => {
    expect(moveItem([a, b], 0, 1).map((x) => (x as Block).id)).toEqual(["b", "a"]);
    expect(moveItem([a, b], 0, -1)).toEqual([a, b]);
  });

  it("gives copies new ids all the way down but keeps all translations", () => {
    const copy = cloneBlockWithNewIds(cond);
    const originalIds = flattenBlocks([cond]).map((f) => f.block.id);
    const copyIds = flattenBlocks([copy]).map((f) => f.block.id);
    expect(copyIds).toHaveLength(3);
    expect(new Set([...originalIds, ...copyIds]).size).toBe(6);
    const inner = flattenBlocks([copy])[1].block;
    expect(inner.type === "PARAGRAPH" && inner.text[0]).toEqual(text("A", "AN", "AE"));
    // original is untouched
    expect(cond.id).toBe("c");
  });

  it("duplicates after the original", () => {
    const result = duplicateBlockAt([a, b], 0);
    expect(result).toHaveLength(3);
    expect(result[0].id).toBe("a");
    expect(result[1].id).not.toBe("a");
    expect(result[2].id).toBe("b");
  });

  it("splits a paragraph so the first part keeps the id and no translation is lost", () => {
    const p: Block = { type: "PARAGRAPH", id: "p", text: [text("En", "Ein", "One"), text("To", "To", "Two")] };
    const parts = splitBlock(p, 1);
    expect(parts).toBeDefined();
    const [first, second] = parts as [Block, Block];
    expect(first.id).toBe("p");
    expect(second.id).not.toBe("p");
    expect(first.type === "PARAGRAPH" && first.text).toEqual([text("En", "Ein", "One")]);
    expect(second.type === "PARAGRAPH" && second.text).toEqual([text("To", "To", "Two")]);
    expect(splitBlock(p, 0)).toBeUndefined();
  });

  it("splits lists and merges them back losslessly", () => {
    const list: Block = { type: "ITEM_LIST", id: "l", numbered: true, items: [[text("1")], [text("2")], [text("3")]] };
    const [first, second] = splitBlock(list, 2) as [Block, Block];
    expect(first.id).toBe("l");
    expect(second.id).not.toBe("l");
    expect(canMergeWithNext([first, second], 0)).toBe(true);
    const merged = mergeWithNext([first, second], 0);
    expect(merged).toHaveLength(1);
    expect(merged?.[0]).toEqual(list);
  });

  it("does not merge unlike blocks", () => {
    expect(mergeWithNext([a, cond], 0)).toBeUndefined();
  });
});

describe("translations and fields", () => {
  it("withTranslation changes one language only", () => {
    expect(withTranslation({ NB: "a", NN: "b", EN: "c" }, "NN", "x")).toEqual({ NB: "a", NN: "x", EN: "c" });
  });

  const fields: Field[] = [
    { id: "s", name: "navn", kind: "STRING", nullable: false, children: [], enumValues: [] },
    {
      id: "l",
      name: "perioder",
      kind: "LIST",
      nullable: false,
      enumValues: [],
      children: [{ id: "x", name: "belop", kind: "NUMBER", nullable: false, children: [], enumValues: [] }],
    },
  ];

  it("restricts list children to repeats over that list", () => {
    const index = indexFields(fields);
    expect(fieldsInScope(index, []).map((f) => f.field.id)).toEqual(["s", "l"]);
    expect(fieldsInScope(index, ["l"]).map((f) => f.field.id)).toEqual(["s", "l", "x"]);
    expect(index.get("x")?.path).toBe("perioder.belop");
  });

  it("counts references, including predicates and repeats", () => {
    const draft: Draft = {
      ...newDraft({ name: "t", type: "AUTOBREV", targetModule: "UFOERE", languages: ["NB", "NN", "EN"] }),
      fields,
      outline: [
        { type: "PARAGRAPH", id: "p", text: [{ type: "FIELD", fieldId: "s", formatter: "DEFAULT" }] },
        {
          type: "CONDITIONAL",
          id: "c",
          predicate: { type: "NOT", predicate: { type: "IS_NOT_NULL", fieldId: "s" } },
          ifTrue: [],
          ifFalse: [],
        },
        { type: "REPEAT", id: "r", fieldId: "l", body: [] },
      ],
    };
    expect(countFieldReferences(draft, "s")).toBe(2);
    expect(countFieldReferences(draft, "l")).toBe(1);
    expect(countFieldReferences(draft, "x")).toBe(0);
  });

  it("finds which languages have content", () => {
    const draft = newDraft({ name: "t", type: "AUTOBREV", targetModule: "UFOERE", languages: ["NB", "NN", "EN"] });
    expect(languagesWithContent(draft).size).toBe(0);
    draft.title = { NB: "Tittel", NN: "", EN: "Title" };
    expect([...languagesWithContent(draft)].sort()).toEqual(["EN", "NB"]);
  });
});

describe("history", () => {
  it("undoes, redoes and coalesces keyed edits", () => {
    let h = createHistory(0);
    h = pushHistory(h, 1, "typing");
    h = pushHistory(h, 2, "typing");
    h = pushHistory(h, 3);
    expect(h.past).toEqual([0, 2]);
    h = undo(h);
    expect(h.present).toBe(2);
    expect(canRedo(h)).toBe(true);
    h = undo(h);
    expect(h.present).toBe(0);
    expect(canUndo(h)).toBe(false);
    h = redo(h);
    expect(h.present).toBe(2);
    h = pushHistory(h, 9);
    expect(canRedo(h)).toBe(false);
  });

  it("ignores identical values", () => {
    const h = createHistory({ a: 1 });
    expect(pushHistory(h, h.present)).toBe(h);
  });
});

describe("moving blocks between containers", () => {
  const a: Block = { type: "PARAGRAPH", id: "a", text: [text("A", "AN", "AE")] };
  const b: Block = { type: "PARAGRAPH", id: "b", text: [text("B")] };
  const inner: Block = { type: "REPEAT", id: "r", fieldId: "list", body: [b] };
  const cond: Block = {
    type: "CONDITIONAL",
    id: "c",
    predicate: { type: "IS_NOT_NULL", fieldId: "f" },
    ifTrue: [inner],
    ifFalse: [],
  };
  const outline: Block[] = [a, cond];

  it("moves a block into a condition branch keeping id and translations", () => {
    const next = relocateBlock(outline, "a", { containerId: "c", branch: "ifFalse", index: 0 });
    expect(next?.map((x) => x.id)).toEqual(["c"]);
    const moved = flattenBlocks(next ?? []).find((f) => f.block.id === "a");
    expect(moved?.parentId).toBe("c");
    expect(moved?.branch).toBe("ifFalse");
    expect(moved?.block).toBe(a);
  });

  it("moves a nested block out to the top level", () => {
    const next = relocateBlock(outline, "b", { containerId: null, branch: "root", index: 1 });
    expect(next?.map((x) => x.id)).toEqual(["a", "b", "c"]);
    const repeat = flattenBlocks(next ?? []).find((f) => f.block.id === "r")?.block;
    expect(repeat?.type === "REPEAT" && repeat.body).toEqual([]);
  });

  it("refuses to move a block into itself or its descendants", () => {
    expect(relocateBlock(outline, "c", { containerId: "c", branch: "ifTrue", index: 0 })).toBeUndefined();
    expect(relocateBlock(outline, "c", { containerId: "r", branch: "body", index: 0 })).toBeUndefined();
    expect(relocateBlock(outline, "a", { containerId: "missing", branch: "body", index: 0 })).toBeUndefined();
  });

  it("offers only legal targets", () => {
    expect(blockMoveTargets(outline, "c").map((o) => o.key)).toEqual(["root:root"]);
    expect(blockMoveTargets(outline, "a").map((o) => o.key)).toEqual(["root:root", "c:ifTrue", "r:body", "c:ifFalse"]);
  });

  it("does not mutate the input", () => {
    const snapshot = structuredClone(outline);
    relocateBlock(outline, "a", { containerId: "r", branch: "body", index: 0 });
    expect(outline).toEqual(snapshot);
  });
});

describe("locked source blocks", () => {
  const locked: Block = { type: "LOCKED_SOURCE", id: "l", bindingId: "binding-1" };
  const wrapper: Block = {
    type: "CONDITIONAL",
    id: "w",
    predicate: { type: "IS_NOT_NULL", fieldId: "f" },
    ifTrue: [locked],
    ifFalse: [],
  };

  it("detects locked blocks, also nested", () => {
    expect(containsLockedSource(locked)).toBe(true);
    expect(containsLockedSource(wrapper)).toBe(true);
    expect(containsLockedSource({ type: "PARAGRAPH", id: "p", text: [] })).toBe(false);
  });

  it("never clones or duplicates them, and leaves the list unchanged", () => {
    expect(() => cloneBlockWithNewIds(locked)).toThrow();
    expect(() => cloneBlockWithNewIds(wrapper)).toThrow();
    expect(duplicateBlockAt([locked, wrapper], 0)).toEqual([locked, wrapper]);
    expect(duplicateBlockAt([wrapper], 0)).toEqual([wrapper]);
  });

  it("never deletes them or containers around them", () => {
    expect(removeBlockAt([locked, wrapper], 0)).toEqual([locked, wrapper]);
    expect(removeBlockAt([locked, wrapper], 1)).toEqual([locked, wrapper]);
    expect(removeBlockAt([{ type: "PARAGRAPH", id: "p", text: [] }], 0)).toEqual([]);
  });

  it("never relocates the locked block itself, and keeps it unchanged when others move", () => {
    expect(relocateBlock([locked, wrapper], "l", { containerId: null, branch: "root", index: 1 })).toBeUndefined();
    const p: Block = { type: "PARAGRAPH", id: "p", text: [] };
    const next = relocateBlock([p, wrapper], "p", { containerId: "w", branch: "ifFalse", index: 0 });
    expect(flattenBlocks(next ?? []).find((f) => f.block.id === "l")?.block).toBe(locked);
  });
});
