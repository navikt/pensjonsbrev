import { describe, expect, test } from "vitest";

import {
  addHistoryEntry,
  createSaksbehandlerValgHistoryEntry,
  type History,
} from "~/Brevredigering/LetterEditor/history";
import { type EditedLetter } from "~/types/brevbakerTypes";

const brev = (text: string) => ({ text }) as unknown as EditedLetter;

describe("history", () => {
  test("stores saksbehandlerValg snapshots as one undoable history entry", () => {
    const before = {
      redigertBrev: brev("før"),
      redigertBrevHash: "hash-før",
      saksbehandlerValg: { ytelse: "alderspensjon" },
    };
    const after = {
      redigertBrev: brev("etter"),
      redigertBrevHash: "hash-etter",
      saksbehandlerValg: { ytelse: "Supplerende stønad" },
    };

    const entry = createSaksbehandlerValgHistoryEntry(before, after);

    expect(entry).toMatchObject({
      type: "SAKSBEHANDLER_VALG",
      before,
      after,
    });
  });

  test("drops redo entries when adding saksbehandlerValg history after undo", () => {
    const history: History = {
      entries: [
        {
          type: "PATCH",
          patches: [{ op: "replace", path: ["saveStatus"], value: "DIRTY" }],
          inversePatches: [{ op: "replace", path: ["saveStatus"], value: "SAVED" }],
        },
        {
          type: "PATCH",
          patches: [{ op: "replace", path: ["saveStatus"], value: "SAVE_PENDING" }],
          inversePatches: [{ op: "replace", path: ["saveStatus"], value: "DIRTY" }],
        },
      ],
      entryPointer: 0,
    };

    const nextEntry = createSaksbehandlerValgHistoryEntry(
      { redigertBrev: brev("før"), redigertBrevHash: "hash-før", saksbehandlerValg: { ytelse: "før" } },
      { redigertBrev: brev("etter"), redigertBrevHash: "hash-etter", saksbehandlerValg: { ytelse: "etter" } },
    );

    const result = addHistoryEntry(history, nextEntry);

    expect(result.entries).toEqual([history.entries[0], nextEntry]);
    expect(result.entryPointer).toBe(1);
  });
});
