import { type ClipboardEvent, type FocusEvent, type MouseEvent, useLayoutEffect, useRef } from "react";

import {
  diffSegmentSignature,
  getEditableCharacterOffset,
  renderDiffSegments,
  renderPlainText,
} from "~/Brevredigering/LetterEditor/diff/DiffSegments";
import {
  isTekstValgHighlighted,
  useInsertedTekstValgHighlight,
} from "~/Brevredigering/LetterEditor/InsertedTekstValgHighlight";
import { type LiteralV2 } from "~/types/brevV2";

import {
  caretAtDomSelection,
  domSelection,
  placeCaret,
  selectAllIn,
  textAttributes,
  ZERO_WIDTH_SPACE,
} from "../../caret/domCaret";
import { paste } from "../../commands/paste";
import { pasteOverSelection } from "../../commands/pasteOverSelection";
import { moveFocus, updateText } from "../../commands/updateText";
import { useAttestantDiffV2, useDiffSegmentsV2 } from "../../diff/AttestantDiffContextV2";
import { firstUneditedFritekst } from "../../model/fritekst";
import { type Caret, type ContainerPath, pathKey, samePath } from "../../model/paths";
import { fontTypeOf, isUneditedFritekst, textOf } from "../../model/text";
import { useEditorV2 } from "../EditorContextV2";
import { type PasteMethod, trackPaste } from "./trackPaste";
import { isTextMutatingKey, useLiteralKeyboard } from "./useLiteralKeyboard";

const fritekstStyle = {
  color: "var(--ax-text-accent-subtle)",
  backgroundColor: "var(--ax-bg-brand-blue-moderate)",
  borderRadius: "var(--ax-radius-2)",
  fontWeight: 600,
  padding: "0 var(--ax-space-2)",
  textDecoration: "underline",
  textDecorationThickness: "from-font",
  textUnderlinePosition: "from-font",
  cursor: "pointer",
} as const;

function selectWholeText(element: HTMLElement): void {
  element.focus();
  selectAllIn(element);
}

function isWhollySelected(element: HTMLElement): boolean {
  const selection = globalThis.getSelection();
  if (!selection || selection.rangeCount === 0 || selection.isCollapsed) return false;
  const range = selection.getRangeAt(0);
  return (
    element.contains(range.startContainer) &&
    element.contains(range.endContainer) &&
    range.startOffset === 0 &&
    range.endOffset === (element.textContent ?? "").length
  );
}

export function EditableLiteral({ path, index, literal }: { path: ContainerPath; index: number; literal: LiteralV2 }) {
  const element = useRef<HTMLSpanElement>(null);
  const pasteMethod = useRef<PasteMethod>("annet");
  const { freeze, state, dispatch } = useEditorV2();
  const highlighted = isTekstValgHighlighted(useInsertedTekstValgHighlight(), literal);
  const onKeyDown = useLiteralKeyboard(element, { path, content: index });

  const value = textOf(literal);
  const text = value || ZERO_WIDTH_SPACE;
  const fritekst = isUneditedFritekst(literal);
  const focus = state.focus;
  const focused = samePath(focus.path, path) && focus.content === index;
  const firstFritekst = firstUneditedFritekst(state.redigertBrev);
  const tabEntry =
    !freeze && path.kind === "title" && !firstFritekst && index === firstLiteralIndex(state.redigertBrev.title.text);

  const { diffHash, disableDiffMode } = useAttestantDiffV2();
  const segments = useDiffSegmentsV2(path, index, value);
  const diffVersion =
    diffHash && segments ? `${diffHash}:${pathKey(path)}:${index}:${diffSegmentSignature(segments)}` : undefined;

  useLayoutEffect(() => {
    const span = element.current;
    if (!span) return;
    if (segments && diffVersion) {
      renderDiffSegments(span, segments, diffVersion);
      return;
    }
    if (diffHash !== undefined || span.dataset.diffVersion !== undefined) renderPlainText(span, text);
    else if (span.textContent !== text) span.textContent = text;

    if (freeze || !focused) return;
    if (fritekst && (focus.selectAll || isWhollySelected(span))) {
      if (!isWhollySelected(span)) selectWholeText(span);
      return;
    }
    placeCaret(span, Math.min(focus.offset, value.length));
  }, [text, value, focused, focus, freeze, fritekst, segments, diffVersion, diffHash]);

  const at = (offset: number): Caret => ({ path, content: index, offset });

  const disableDiffAndKeepCaret = () => {
    const span = element.current;
    if (!span) return;
    const offset = getEditableCharacterOffset(span);
    disableDiffMode();
    dispatch(moveFocus, at(offset));
  };

  const handleInput = () => {
    const span = element.current;
    if (!span) return;
    if (segments) {
      disableDiffAndKeepCaret();
      return;
    }
    const caret = caretAtDomSelection();
    dispatch(updateText, at(caret?.offset ?? 0), (span.textContent ?? "").replaceAll(ZERO_WIDTH_SPACE, ""));
  };

  const handlePaste = (event: ClipboardEvent<HTMLSpanElement>) => {
    event.preventDefault();
    if (segments) {
      disableDiffAndKeepCaret();
      return;
    }
    const root = event.currentTarget.closest<HTMLElement>("[data-editor-root]") ?? document.body;
    const selection = domSelection(root);
    trackPaste(state.info, event.clipboardData, pasteMethod.current, !!selection);
    pasteMethod.current = "annet";
    if (selection) dispatch(pasteOverSelection, selection, event.clipboardData);
    else dispatch(paste, caretAtDomSelection() ?? at(0), event.clipboardData);
  };

  const handleFocus = (event: FocusEvent<HTMLSpanElement>) => {
    const caret = caretAtDomSelection();
    const offset = caret && samePath(caret.path, path) && caret.content === index ? caret.offset : 0;
    if (!focused || focus.offset !== offset) dispatch(moveFocus, at(offset));
    if (fritekst) {
      event.preventDefault();
      selectWholeText(event.currentTarget);
    }
  };

  const handleMouseDown = (event: MouseEvent<HTMLSpanElement>) => {
    pasteMethod.current = "annet";
    if (!fritekst && event.detail === 3) {
      event.preventDefault();
      selectAllIn(event.currentTarget);
      return;
    }
    if (!fritekst) return;
    const selection = globalThis.getSelection();
    if (selection && !selection.isCollapsed && selection.containsNode(event.currentTarget, true)) {
      selection.collapse(event.currentTarget);
    }
    if (event.detail === 2) event.preventDefault();
  };

  const handleClick = (event: MouseEvent<HTMLSpanElement>) => {
    if (!fritekst) return;
    event.preventDefault();
    const selection = globalThis.getSelection();
    const span = event.currentTarget;
    if (!selection || (span.contains(selection.anchorNode) && span.contains(selection.focusNode))) return;
    if (selection.isCollapsed && event.detail !== 2) selectAllIn(span);
  };

  const handleDoubleClick = (event: MouseEvent<HTMLSpanElement>) => {
    if (!fritekst) return;
    event.preventDefault();
    event.stopPropagation();
    selectAllIn(event.currentTarget);
  };

  return (
    // biome-ignore lint/a11y/noStaticElementInteractions: redigerbar tekst
    <span
      {...textAttributes(path, index)}
      className={highlighted ? "inserted-flash-text" : undefined}
      contentEditable={!freeze}
      css={{
        ...(fritekst && fritekstStyle),
        ...(fontTypeOf(literal) === "BOLD" && { fontWeight: "bold" }),
        ...(fontTypeOf(literal) === "ITALIC" && { fontStyle: "italic" }),
      }}
      data-empty={value ? undefined : ""}
      onBeforeInput={
        segments
          ? (event) => {
              event.preventDefault();
              disableDiffAndKeepCaret();
            }
          : undefined
      }
      onClick={handleClick}
      onContextMenu={() => {
        pasteMethod.current = "kontekstmeny";
      }}
      onDoubleClick={handleDoubleClick}
      onFocus={handleFocus}
      onInput={handleInput}
      onKeyDown={(event) => {
        if (segments && isTextMutatingKey(event)) {
          event.preventDefault();
          event.stopPropagation();
          disableDiffAndKeepCaret();
          return;
        }
        const modifier = event.metaKey || event.ctrlKey;
        if ((modifier && event.key.toLowerCase() === "v") || (event.shiftKey && event.key === "Insert")) {
          pasteMethod.current = "tastatursnarvei";
        }
        onKeyDown(event);
      }}
      onMouseDown={handleMouseDown}
      onPaste={handlePaste}
      ref={element}
      tabIndex={fritekst || tabEntry ? 0 : -1}
    />
  );
}

function firstLiteralIndex(content: { type: string }[]): number {
  return content.findIndex((text) => text.type === "LITERAL");
}
