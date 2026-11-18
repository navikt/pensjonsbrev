import { describe, expect, it } from "vitest";

import { capabilityState } from "./capabilities";
import { addComment, addReply, isOrphaned, isResolved, setResolved } from "./comments";
import { diffDrafts, wordDiff } from "./diff";
import { newDraft } from "./draftOps";
import { type Block, type Draft, type Field } from "./types";
import { hasBlockingIssues, validateDraft } from "./validate";

const meta = { name: "Mal", type: "AUTOBREV", targetModule: "UFOERE", languages: ["NB", "NN", "EN"] } as const;
const para = (id: string, nb: string, nn = "nn", en = "en"): Block => ({
  type: "PARAGRAPH",
  id,
  text: [{ type: "TEXT", text: { NB: nb, NN: nn, EN: en }, fontType: "PLAIN" }],
});
const base = (outline: Block[]): Draft => ({
  ...newDraft({ ...meta, languages: [...meta.languages] }),
  title: { NB: "T", NN: "T", EN: "T" },
  outline,
});

describe("validateDraft", () => {
  it("blocks on missing languages and points at the block", () => {
    const draft = base([para("p", "Hei", "", "Hi")]);
    const issues = validateDraft(draft);
    expect(hasBlockingIssues(issues)).toBe(true);
    expect(issues.find((i) => i.blockId === "p")?.language).toBe("NN");
  });

  it("accepts a complete draft", () => {
    expect(validateDraft(base([para("p", "Hei")]))).toEqual([]);
  });

  it("flags unknown fields, list children outside repeats and non-list repeats", () => {
    const fields: Field[] = [
      { id: "s", name: "navn", kind: "STRING", nullable: false, children: [], enumValues: [] },
      {
        id: "l",
        name: "rader",
        kind: "LIST",
        nullable: false,
        enumValues: [],
        children: [{ id: "x", name: "belop", kind: "NUMBER", nullable: false, children: [], enumValues: [] }],
      },
    ];
    const draft: Draft = {
      ...base([
        {
          type: "PARAGRAPH",
          id: "p1",
          text: [
            { type: "FIELD", fieldId: "x", formatter: "DEFAULT" },
            { type: "FIELD", fieldId: "ukjent", formatter: "DEFAULT" },
          ],
        },
        { type: "REPEAT", id: "r1", fieldId: "s", body: [] },
        {
          type: "REPEAT",
          id: "r2",
          fieldId: "l",
          body: [{ type: "PARAGRAPH", id: "p2", text: [{ type: "FIELD", fieldId: "x", formatter: "CURRENCY" }] }],
        },
      ]),
      fields,
    };
    const issues = validateDraft(draft);
    expect(issues.filter((i) => i.blockId === "p1")).toHaveLength(2);
    expect(issues.some((i) => i.blockId === "r1")).toBe(true);
    expect(issues.some((i) => i.blockId === "p2")).toBe(false);
  });

  it("flags duplicate block ids and ragged tables", () => {
    const table: Block = { type: "TABLE", id: "t", header: [{ NB: "a", NN: "a", EN: "a" }], rows: [[[], []]] };
    const issues = validateDraft(base([para("d", "x"), para("d", "y"), table]));
    expect(issues.some((i) => i.message.includes("samme ID"))).toBe(true);
    expect(issues.some((i) => i.blockId === "t")).toBe(true);
  });
});

describe("diffDrafts", () => {
  it("reports added, removed, changed (with word diff per language) and moved blocks", () => {
    const before = base([para("a", "Hei verden"), para("b", "To"), para("c", "Tre")]);
    const after = base([para("c", "Tre"), para("a", "Hei kjære verden"), para("n", "Ny")]);
    const diff = diffDrafts(before, after);
    expect(diff.blocks.get("n")).toBe("added");
    expect(diff.blocks.get("a")).toBe("changed");
    expect(diff.items.some((i) => i.change === "removed" && i.label.includes("To"))).toBe(true);
    const changed = diff.items.find((i) => i.blockId === "a");
    const nb = changed?.words?.find((w) => w.language === "NB");
    expect(nb?.parts.filter((p) => p.type === "added").map((p) => p.text.trim())).toContain("kjære");
    expect(["moved", "changed"]).toContain(diff.blocks.get("c"));
  });

  it("ignores scenarios and comments", () => {
    const before = base([para("a", "Hei")]);
    const after = {
      ...before,
      scenarios: [{ id: "s", name: "S", values: { x: 1 }, choices: {} }],
      comments: addComment([], { anchor: { type: "DOCUMENT" }, text: "hei" }),
    };
    expect(diffDrafts(before, after).items).toEqual([]);
  });

  it("reports field, choice and metadata changes", () => {
    const before = base([para("a", "x")]);
    const after: Draft = {
      ...before,
      metadata: { ...before.metadata, name: "Nytt navn" },
      fields: [{ id: "f", name: "nytt", kind: "STRING", nullable: false, children: [], enumValues: [] }],
      choices: [{ id: "c", name: "valg", prompt: { NB: "a", NN: "a", EN: "a" } }],
    };
    const areas = diffDrafts(before, after).items.map((i) => i.area);
    expect(areas).toEqual(expect.arrayContaining(["Datafelt", "Valg", "Metadata"]));
  });
});

describe("wordDiff", () => {
  it("marks insertions and deletions", () => {
    const parts = wordDiff("a b c", "a x c");
    expect(parts.filter((p) => p.type === "removed").map((p) => p.text)).toEqual(["b"]);
    expect(parts.filter((p) => p.type === "added").map((p) => p.text)).toEqual(["x"]);
  });
});

describe("comments", () => {
  it("never stamps author/time, appends replies and state history", () => {
    let comments = addComment([], { anchor: { type: "BLOCK", blockId: "a" }, text: "  Se her ", role: "Jurist" });
    expect(comments[0]).toMatchObject({ text: "Se her", role: "Jurist", replies: [], states: [] });
    expect(comments[0].author).toBeUndefined();
    const id = comments[0].id;
    comments = addReply(comments, id, "Svar");
    expect(comments[0].replies).toHaveLength(1);
    expect(isResolved(comments[0])).toBe(false);
    comments = setResolved(comments, id, true);
    expect(isResolved(comments[0])).toBe(true);
    comments = setResolved(comments, id, true);
    expect(comments[0].states).toHaveLength(1);
    comments = setResolved(comments, id, false);
    expect(comments[0].states).toHaveLength(2);
    expect(isResolved(comments[0])).toBe(false);
  });

  it("rejects empty comments and keeps orphans visible", () => {
    expect(addComment([], { anchor: { type: "DOCUMENT" }, text: "  " })).toEqual([]);
    const draft = base([para("a", "x")]);
    const [orphan] = addComment([], { anchor: { type: "BLOCK", blockId: "slettet" }, text: "x" });
    const [ok] = addComment([], { anchor: { type: "BLOCK", blockId: "a" }, text: "x" });
    expect(isOrphaned(orphan, draft)).toBe(true);
    expect(isOrphaned(ok, draft)).toBe(false);
  });
});

describe("capabilityState", () => {
  it("reads booleans, strings and objects and treats unknown as off", () => {
    expect(capabilityState(true).enabled).toBe(true);
    expect(capabilityState("ENABLED").enabled).toBe(true);
    expect(capabilityState({ enabled: false }).enabled).toBe(false);
    expect(capabilityState({ status: "available" }).enabled).toBe(true);
    expect(capabilityState(undefined).enabled).toBe(false);
    expect(capabilityState({ foo: 1 }).label).toBe("Ukjent");
  });
});
