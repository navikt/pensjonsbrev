// @vitest-environment happy-dom

import { cleanup, fireEvent, render, screen } from "@testing-library/react";
import { useState } from "react";
import { afterEach, describe, expect, test, vi } from "vitest";

import { newLiteral } from "~/Brevredigering/LetterEditor/actions/common";
import { ContentGroup } from "~/Brevredigering/LetterEditor/components/ContentGroup";
import { EditorStateContext } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { type LiteralValue, type ParagraphBlock } from "~/types/brevbakerTypes";

import { letter } from "../../utils";

const lit1 = newLiteral({ id: 101, text: "Første literal i avsnittet" });
const lit2 = newLiteral({ id: 102, text: "andre literal i avsnittet" });

const block: ParagraphBlock = {
  id: 1,
  parentId: null,
  editable: true,
  type: "PARAGRAPH",
  deletedContent: [],
  missingFromTemplate: false,
  content: [lit1, lit2],
};

const initialState = (): LetterEditorState => ({
  ...letter(block),
  focus: { blockIndex: 0, contentIndex: 0, cursorPosition: 5 },
});

function Harness(props: { initial: LetterEditorState }) {
  const [editorState, setEditorState] = useState(props.initial);
  return (
    <EditorStateContext.Provider
      value={{ freeze: false, error: false, editorState, setEditorState, undo: vi.fn(), redo: vi.fn() }}
    >
      <ContentGroup literalIndex={{ blockIndex: 0, contentIndex: 0 }} />
      <button
        data-testid="apply-save-response"
        onClick={() =>
          setEditorState((s) => ({
            ...s,
            redigertBrev: structuredClone(s.redigertBrev),
            redigertBrevHash: "ny-hash",
            saveStatus: "SAVED",
          }))
        }
        type="button"
      />
      <button
        data-testid="apply-save-response-changed-text"
        onClick={() =>
          setEditorState((s) => {
            const clone = structuredClone(s.redigertBrev);
            const paragraphBlock = clone.blocks[0] as ParagraphBlock;
            const literal = paragraphBlock.content[0] as LiteralValue;
            literal.editedText = `${literal.editedText ?? literal.text} `;
            return { ...s, redigertBrev: clone, redigertBrevHash: "ny-hash", saveStatus: "SAVED" };
          })
        }
        type="button"
      />
      <button
        data-testid="apply-save-response-inserted-content"
        onClick={() =>
          setEditorState((s) => {
            const clone = structuredClone(s.redigertBrev);
            const paragraphBlock = clone.blocks[0] as ParagraphBlock;
            paragraphBlock.content.unshift(newLiteral({ id: 900, text: "Ny literal fra ny render" }));
            return { ...s, redigertBrev: clone, redigertBrevHash: "ny-hash", saveStatus: "SAVED" };
          })
        }
        type="button"
      />
    </EditorStateContext.Provider>
  );
}

const placeCaret = (element: HTMLElement, offset: number) => {
  const range = document.createRange();
  range.setStart(element.childNodes[0], offset);
  range.collapse(true);
  const selection = globalThis.getSelection();
  selection?.removeAllRanges();
  selection?.addRange(range);
};

const caretInfo = () => {
  const selection = globalThis.getSelection();
  return {
    rangeCount: selection?.rangeCount ?? 0,
    offset: selection?.getRangeAt(0)?.startOffset,
    anchorText: selection?.getRangeAt(0)?.startContainer.textContent,
  };
};

describe("caret etter autolagring", () => {
  afterEach(() => {
    cleanup();
    globalThis.getSelection()?.removeAllRanges();
  });

  test("beholder markøren når lagret brev er strukturelt likt", () => {
    render(<Harness initial={initialState()} />);
    const span = screen.getByText(lit1.text);
    placeCaret(span, 5);
    expect(caretInfo().rangeCount).toBe(1);
    expect(caretInfo().offset).toBe(5);

    fireEvent.click(screen.getByTestId("apply-save-response"));

    const caret = caretInfo();
    expect(caret.rangeCount).toBe(1);
    expect(caret.offset).toBe(5);
    expect(caret.anchorText).toBe(lit1.text);
  });

  test("overskriver ikke tekst/markør i fokusert literal når server-svar er eldre enn skjermbilde", () => {
    const state = initialState();
    render(<Harness initial={state} />);
    const span = screen.getByText(lit1.text);
    placeCaret(span, 5);

    // Brukeren skriver et tegn; DOM oppdateres nativt og state lagrer ny tekst + cursorPosition.
    span.textContent = `${lit1.text}x`;
    placeCaret(span, 6);
    fireEvent.input(span);

    // Server-svaret (sendt før tastetrykket) ekkoer den gamle teksten tilbake.
    fireEvent.click(screen.getByTestId("apply-save-response"));

    const caret = caretInfo();
    expect(span.textContent).toBe(`${lit1.text}x`);
    expect(caret.rangeCount).toBe(1);
    expect(caret.offset).toBe(6);
  });

  // Dokumentert og akseptert begrensning i den minimale fiksen: har serveren FAKTISK endret teksten i
  // den fokuserte literalen, beholdes skjermteksten og den levende markøren (ingen tvungen sync).
  // Markøren forblir på sin nåværende plass i stedet for å gjenopprettes fra focus.cursorPosition.
  test("dokumentert begrensning: server har endret teksten i fokusert literal", () => {
    render(<Harness initial={initialState()} />);
    const span = screen.getByText(lit1.text);
    placeCaret(span, 5);

    fireEvent.click(screen.getByTestId("apply-save-response-changed-text"));

    const caret = caretInfo();
    expect(caret.rangeCount).toBe(1);
    expect(caret.offset).toBe(5);
    expect(caret.anchorText).toBe(lit1.text);
  });

  test("dokumentert begrensning: cursorPosition mangler i state (flyttet med piltaster) + endret tekst fra server", () => {
    render(<Harness initial={{ ...initialState(), focus: { blockIndex: 0, contentIndex: 0 } }} />);
    const span = screen.getByText(lit1.text);
    placeCaret(span, 5);

    fireEvent.click(screen.getByTestId("apply-save-response-changed-text"));

    // Uten lagret cursorPosition beholdes teksten på skjermen, men markøren gjenopprettes ikke
    // (i nettleseren havner den i starten av elementet etter textContent-sync).
    const caret = caretInfo();
    expect(span.textContent).toBe(lit1.text);
    expect(caret.rangeCount).toBe(1);
  });

  test("beholder mus-markering i literal som ikke er focus-target etter autolagring", () => {
    // State.focus peker på contentIndex 0 (siste redigering), men brukeren har
    // markert tekst i contentIndex 1 med musen.
    render(<Harness initial={initialState()} />);
    const span = screen.getByText(lit2.text);
    const range = document.createRange();
    range.setStart(span.childNodes[0], 2);
    range.setEnd(span.childNodes[0], 8);
    const selection = globalThis.getSelection();
    selection?.removeAllRanges();
    selection?.addRange(range);

    fireEvent.click(screen.getByTestId("apply-save-response"));

    const after = globalThis.getSelection();
    expect(after?.rangeCount).toBe(1);
    expect(after?.isCollapsed).toBe(false);
    expect(after?.getRangeAt(0).toString()).toBe(lit2.text.slice(2, 8));
  });

  test("server-merge setter inn innhold før fokusert literal (indeks-skift)", () => {
    render(<Harness initial={initialState()} />);
    const span = screen.getByText(lit1.text);
    placeCaret(span, 5);

    fireEvent.click(screen.getByTestId("apply-save-response-inserted-content"));

    const caret = caretInfo();
    expect(caret.rangeCount).toBe(1);
    expect(caret.offset).toBe(5);
    expect(caret.anchorText).toBe(lit1.text);
  });
});
