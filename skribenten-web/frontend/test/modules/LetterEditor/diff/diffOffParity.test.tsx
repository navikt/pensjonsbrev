import { render } from "@testing-library/react";
import { type ComponentProps, type ReactNode } from "react";
import { describe, expect, it, vi } from "vitest";

import { ContentGroup } from "~/Brevredigering/LetterEditor/components/ContentGroup";
import { AttestantDiffProvider } from "~/Brevredigering/LetterEditor/diff/AttestantDiffContext";
import { type UnifiedLetterDiff } from "~/Brevredigering/LetterEditor/diff/diffModel";
import { EditorStateContext } from "~/Brevredigering/LetterEditor/LetterEditor";

import { cell, item, itemList, letter, literal, newLine, paragraph, row, table, variable } from "../utils";

const DIFF_ARTIFACT_SELECTOR =
  "[data-diff-deleted],[data-diff-deleted-block],[data-diff-insertion],[data-diff-deletion],[data-diff-version]";

// useId counts up per render pass, so the two renders being compared always differ on those ids alone.
const normalize = (html: string) => html.replaceAll(/_r_[\da-z]+_/g, "_r_");

// Built once: the helpers assign random ids, so both renders must share the same state instance.
const editorState = letter(
  paragraph([
    literal({ text: "Første avsnitt" }),
    variable("MED VARIABEL"),
    newLine(),
    literal({ text: ", og litt mer tekst" }),
  ]),
  paragraph([
    itemList({
      items: [item(literal({ text: "punkt en" })), item(literal({ text: "punkt to" }), variable("V"))],
    }),
  ]),
  paragraph([table([cell(literal({ text: "Kolonne" }))], [row(cell(literal({ text: "Rad" })))])]),
);

const setEditorState = vi.fn();

const renderEditor = (wrap: (children: ReactNode) => ReactNode) =>
  render(
    <EditorStateContext.Provider
      value={{ freeze: false, error: false, editorState, setEditorState, undo: vi.fn(), redo: vi.fn() }}
    >
      {wrap(
        editorState.redigertBrev.blocks.map((block, blockIndex) => (
          <div className={block.type} key={blockIndex}>
            <ContentGroup literalIndex={{ blockIndex, contentIndex: 0 }} />
          </div>
        )),
      )}
    </EditorStateContext.Provider>,
  );

const withoutProvider = () => renderEditor((children) => children);

const withProvider = (
  overrides: Partial<ComponentProps<typeof AttestantDiffProvider>> = {},
): ReturnType<typeof render> =>
  renderEditor((children) => (
    <AttestantDiffProvider
      diff={undefined}
      diffHash={undefined}
      disableDiff={vi.fn()}
      reportRejectedLiteral={vi.fn()}
      {...overrides}
    >
      {children}
    </AttestantDiffProvider>
  ));

// Marks "Første avsnitt" as inserted so the positive control proves the parity assertions can fail.
const diffTouchingFirstLiteral: UnifiedLetterDiff = {
  editedBlocks: {
    0: {
      contentEdits: { 0: { edit: { inserts: [{ startOffset: 0, endOffset: 6 }], deletes: [] } } },
      deletedContent: {},
    },
  },
  deletedBlocks: {},
};

describe("diff-avslått gir uendret redigeringsflate", () => {
  it("renders identical DOM whether or not the diff provider is mounted", () => {
    const baseline = normalize(withoutProvider().container.innerHTML);
    const withInactiveProvider = normalize(withProvider().container.innerHTML);

    expect(withInactiveProvider).toBe(baseline);
  });

  it("leaves no diff artifacts in the DOM when the diff is inactive", () => {
    expect(withProvider().container.querySelectorAll(DIFF_ARTIFACT_SELECTOR)).toHaveLength(0);
  });

  it("renders identical DOM when the diff is present but the hash is gone", () => {
    const baseline = normalize(withoutProvider().container.innerHTML);
    const withoutHash = withProvider({
      diff: diffTouchingFirstLiteral,
      diffHash: undefined,
    }).container;

    expect(normalize(withoutHash.innerHTML)).toBe(baseline);
    expect(withoutHash.querySelectorAll(DIFF_ARTIFACT_SELECTOR)).toHaveLength(0);
  });

  it("renders identical DOM when the diff has been turned off after an edit", () => {
    const baseline = normalize(withoutProvider().container.innerHTML);
    const disabled = withProvider({
      diff: undefined,
      diffHash: "hash-1",
    }).container;

    expect(normalize(disabled.innerHTML)).toBe(baseline);
    expect(disabled.querySelectorAll(DIFF_ARTIFACT_SELECTOR)).toHaveLength(0);
  });

  it("positive control: an active diff does decorate the letter", () => {
    const baseline = normalize(withoutProvider().container.innerHTML);
    const active = withProvider({ diff: diffTouchingFirstLiteral, diffHash: "hash-1" }).container;

    expect(normalize(active.innerHTML)).not.toBe(baseline);
    expect(active.querySelectorAll("[data-diff-insertion]").length).toBeGreaterThan(0);
  });
});
