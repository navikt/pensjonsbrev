import type React from "react";
import { useEffect, useLayoutEffect, useRef } from "react";
import { flushSync } from "react-dom";

import Actions from "~/Brevredigering/LetterEditor/actions";
import {
  isBlockContentIndex,
  isItemContentIndex,
  isTable,
  isUneditedFritekst,
  text as textOf,
} from "~/Brevredigering/LetterEditor/actions/common";
import { updateFocus, updateSelection } from "~/Brevredigering/LetterEditor/actions/cursorPosition";
import { MergeTarget } from "~/Brevredigering/LetterEditor/actions/merge";
import { logPastedClipboard } from "~/Brevredigering/LetterEditor/actions/paste";
import { useAttestantDiff } from "~/Brevredigering/LetterEditor/diff/AttestantDiffContext";
import { getEditableCharacterOffset } from "~/Brevredigering/LetterEditor/diff/DiffSegments";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import { type Focus, type LiteralIndex, type SelectionIndex } from "~/Brevredigering/LetterEditor/model/state";
import { isTableCellIndex, ZERO_WIDTH_SPACE } from "~/Brevredigering/LetterEditor/model/utils";
import {
  applyModelSelectionToDom,
  closestEditableLiteralEl,
  type DomSelectionSnapshot,
  ensureAdjacentLineVisible,
  findIndexElementInRoot,
  getCaretRect,
  getCleanOffsetInLiteral,
  getCleanTextLength,
  gotoCoordinates,
  isEditableLiteralEl,
  isProgrammaticFocus,
  matchesDomSelectionSnapshot,
  parseLiteralIndex,
  resolveDomSelectionToModel,
  revealCaretInScrollContainer,
  snapshotDomSelection,
  staticRangeToModelSelection,
} from "~/Brevredigering/LetterEditor/services/caretUtils";
import { writeSemanticClipboard } from "~/Brevredigering/LetterEditor/services/semanticCopy";
import {
  addRow,
  adjacentTableEntryFocus,
  determineTableCellDeleteAction,
  exitTable,
  getTableArrowNavigationFocus,
  isAtLastTableCell,
  nextTableFocus,
} from "~/Brevredigering/LetterEditor/services/tableCaretUtils";
import { isMac } from "~/Brevredigering/LetterEditor/utils";
import { type EditedDocument, FontType, type LiteralValue, TITLE_INDEX } from "~/types/brevbakerTypes";
import { trackEvent } from "~/utils/innblikk";
import { getPasteMetadata, hasPasteContent } from "~/utils/pasteTracking";

const isTextMutatingKey = (event: React.KeyboardEvent<HTMLElement>): boolean =>
  !event.ctrlKey &&
  !event.altKey &&
  !event.metaKey &&
  (event.key.length === 1 || event.key === "Backspace" || event.key === "Delete" || event.key === "Enter");

function getLiteralFromState(doc: EditedDocument, index: LiteralIndex): LiteralValue | undefined {
  if (index.blockIndex === TITLE_INDEX) {
    const node = doc.title.text[index.contentIndex];
    return node?.type === "LITERAL" ? node : undefined;
  }
  const block = doc.blocks[index.blockIndex];
  const content = block?.content[index.contentIndex];
  if (!content) return undefined;
  if (isTableCellIndex(index) && isTable(content)) {
    const cell =
      index.rowIndex === -1
        ? content.header.colSpec[index.cellIndex]?.headerContent
        : content.rows[index.rowIndex]?.cells[index.cellIndex];
    const node = cell?.text[index.cellContentIndex];
    return node?.type === "LITERAL" ? node : undefined;
  }
  if (isItemContentIndex(index) && content.type === "ITEM_LIST") {
    const node = content.items[index.itemIndex]?.content[index.itemContentIndex];
    return node?.type === "LITERAL" ? node : undefined;
  }
  return content.type === "LITERAL" ? content : undefined;
}

function getLogicalLiteralText(literal: LiteralValue): string {
  const raw = textOf(literal);
  return raw === "" || raw === ZERO_WIDTH_SPACE ? "" : raw;
}

function stripFocusMeta(focus: Focus): LiteralIndex {
  const { cursorPosition: _c, selectAll: _s, ...literalIndex } = focus;
  return literalIndex;
}

function isSameLiteral(a: LiteralIndex, b: LiteralIndex): boolean {
  if (isTableCellIndex(a) && isTableCellIndex(b)) {
    return (
      a.blockIndex === b.blockIndex &&
      a.contentIndex === b.contentIndex &&
      a.rowIndex === b.rowIndex &&
      a.cellIndex === b.cellIndex &&
      a.cellContentIndex === b.cellContentIndex
    );
  }
  if (isItemContentIndex(a) && isItemContentIndex(b)) {
    return (
      a.blockIndex === b.blockIndex &&
      a.contentIndex === b.contentIndex &&
      a.itemIndex === b.itemIndex &&
      a.itemContentIndex === b.itemContentIndex
    );
  }
  if (isBlockContentIndex(a) && isBlockContentIndex(b)) {
    return a.blockIndex === b.blockIndex && a.contentIndex === b.contentIndex;
  }
  return false;
}

function isSameSelection(a: SelectionIndex | undefined, b: SelectionIndex | undefined): boolean {
  if (a === b) return true;
  if (!a || !b) return false;
  return (
    isSameLiteral(a.start, b.start) &&
    a.start.cursorPosition === b.start.cursorPosition &&
    isSameLiteral(a.end, b.end) &&
    a.end.cursorPosition === b.end.cursorPosition
  );
}

function prevCodePointOffset(str: string, offset: number): number {
  if (offset <= 0) return 0;
  if (offset >= 2) {
    const high = str.charCodeAt(offset - 2);
    const low = str.charCodeAt(offset - 1);
    if (high >= 0xd800 && high <= 0xdbff && low >= 0xdc00 && low <= 0xdfff) {
      return offset - 2;
    }
  }
  return offset - 1;
}

function nextCodePointOffset(str: string, offset: number): number {
  if (offset >= str.length) return str.length;
  if (offset + 1 < str.length) {
    const high = str.charCodeAt(offset);
    const low = str.charCodeAt(offset + 1);
    if (high >= 0xd800 && high <= 0xdbff && low >= 0xdc00 && low <= 0xdfff) {
      return offset + 2;
    }
  }
  return offset + 1;
}

function wordBoundaryBackward(str: string, offset: number): number {
  let i = Math.min(offset, str.length);
  while (i > 0 && /\s/.test(str[i - 1])) i--;
  while (i > 0 && !/\s/.test(str[i - 1])) i--;
  return i;
}

function wordBoundaryForward(str: string, offset: number): number {
  let i = Math.max(0, offset);
  while (i < str.length && /\s/.test(str[i])) i++;
  while (i < str.length && !/\s/.test(str[i])) i++;
  return i;
}

export type EditableSurfaceProps = {
  surfaceRef?: React.Ref<HTMLDivElement>;
} & React.ComponentPropsWithoutRef<"div">;

export function EditableSurface({
  surfaceRef,
  onKeyDown: externalOnKeyDown,
  onFocus: externalOnFocus,
  children,
  tabIndex,
  ...rest
}: EditableSurfaceProps) {
  const hostRef = useRef<HTMLDivElement | null>(null);
  const lastAppliedDomSelectionRef = useRef<DomSelectionSnapshot | null>(null);
  const hasCompletedInitialLayoutRef = useRef(false);
  const pointerDownInsideRef = useRef(false);
  const clickedVariableRef = useRef(false);
  const contextClickOverSelectionRef = useRef(false);
  const pasteViaKeyboardRef = useRef(false);
  const pasteViaContextMenuRef = useRef(false);
  const fritekstMouseDownRef = useRef<{ element: HTMLElement; wasInside: boolean } | null>(null);
  const shiftKeyDownRef = useRef(false);
  const lastKeyWasTabRef = useRef(false);
  const lastKeyWasArrowRef = useRef(false);
  // While the browser composes (dead key or IME), its caret counts text the model doesn't have yet, so it isn't synced.
  const isComposingRef = useRef(false);

  const { freeze, editorState, setEditorState, undo, redo } = useEditor();
  const { diffHash, disableDiffMode } = useAttestantDiff();
  const prevFocusRef = useRef<Focus>(editorState.focus);
  const prevSelectionRef = useRef<SelectionIndex | undefined>(editorState.selection);
  // A focus set while frozen (e.g. after a tekstvalg toggle inserts text) is applied once editing is enabled again.
  const focusChangedWhileFrozenRef = useRef(false);
  const editorStateRef = useRef(editorState);
  editorStateRef.current = editorState;
  const freezeRef = useRef(freeze);
  freezeRef.current = freeze;

  const setRefs = (el: HTMLDivElement | null) => {
    hostRef.current = el;
    if (typeof surfaceRef === "function") {
      surfaceRef(el);
    } else if (surfaceRef && "current" in surfaceRef) {
      (surfaceRef as React.MutableRefObject<HTMLDivElement | null>).current = el;
    }
  };

  const fullRangeForLiteral = (literalIndex: LiteralIndex, length: number): SelectionIndex => ({
    start: { ...literalIndex, cursorPosition: 0 },
    end: { ...literalIndex, cursorPosition: length },
  });

  useLayoutEffect(() => {
    const root = hostRef.current;
    if (!root) return;

    const isInitialRender = !hasCompletedInitialLayoutRef.current;
    hasCompletedInitialLayoutRef.current = true;

    const focusOrSelectionChanged =
      editorState.focus !== prevFocusRef.current || editorState.selection !== prevSelectionRef.current;
    prevFocusRef.current = editorState.focus;
    prevSelectionRef.current = editorState.selection;
    const applyFocusSetWhileFrozen = !freeze && focusChangedWhileFrozenRef.current;
    focusChangedWhileFrozenRef.current = freeze && (focusChangedWhileFrozenRef.current || focusOrSelectionChanged);

    if (!freeze) {
      const { focus, selection, redigertBrev } = editorState;
      const ownerTarget = selection ? selection.start : focus;
      const targetEl = findIndexElementInRoot(root, ownerTarget);

      if (targetEl) {
        if (!selection && focus.selectAll) {
          const literal = getLiteralFromState(redigertBrev, focus);
          if (literal && isUneditedFritekst(literal)) {
            const len = getLogicalLiteralText(literal).length;
            const idx = stripFocusMeta(focus);
            const fullSel = fullRangeForLiteral(idx, len);
            const nextFocus: Focus = { ...idx, cursorPosition: len };
            lastAppliedDomSelectionRef.current = applyModelSelectionToDom(root, nextFocus, fullSel, true);
            applyAction(updateSelection, setEditorState, fullSel, nextFocus);
            return;
          }
        }

        const hostHasFocus = document.activeElement === root;
        const effectiveTabIndex = tabIndex ?? (freeze ? -1 : 0);
        if (isInitialRender && !selection && focus.cursorPosition === undefined && effectiveTabIndex >= 0) {
          lastAppliedDomSelectionRef.current = snapshotDomSelection(globalThis.getSelection());
        } else if (isInitialRender || hostHasFocus || focusOrSelectionChanged || applyFocusSetWhileFrozen) {
          lastAppliedDomSelectionRef.current = applyModelSelectionToDom(root, focus, selection, true);
          if (applyFocusSetWhileFrozen) revealCaretInScrollContainer(root);
        }
      }
    }
  });

  useEffect(() => {
    const onDocKeyDown = (event: KeyboardEvent) => {
      lastKeyWasTabRef.current = event.key === "Tab";
      lastKeyWasArrowRef.current = event.key.startsWith("Arrow") && !event.shiftKey;
      shiftKeyDownRef.current = event.shiftKey || event.key === "Shift";
    };
    const onDocPointerDown = () => {
      lastKeyWasTabRef.current = false;
      lastKeyWasArrowRef.current = false;
    };
    const clearPointer = () => {
      pointerDownInsideRef.current = false;
    };
    const onSelectionChange = () => {
      const root = hostRef.current;
      if (
        !root ||
        freezeRef.current ||
        isComposingRef.current ||
        document.activeElement !== root ||
        isProgrammaticFocus() ||
        clickedVariableRef.current
      ) {
        return;
      }

      const domSelection = globalThis.getSelection();
      if (!domSelection || domSelection.rangeCount === 0) return;
      if (matchesDomSelectionSnapshot(domSelection, lastAppliedDomSelectionRef.current)) {
        return;
      }

      const range = domSelection.getRangeAt(0);
      if (!root.contains(range.startContainer) || !root.contains(range.endContainer)) return;

      const mapped = resolveDomSelectionToModel(root);
      if (!mapped) return;

      const cur = editorStateRef.current;
      if (mapped.selection) {
        if (isSameSelection(cur.selection, mapped.selection) && isSameLiteral(cur.focus, mapped.focus)) {
          lastAppliedDomSelectionRef.current = snapshotDomSelection(domSelection);
          return;
        }
        applyAction(updateSelection, setEditorState, mapped.selection, mapped.focus);
      } else {
        if (!domSelection.isCollapsed) {
          lastAppliedDomSelectionRef.current = applyModelSelectionToDom(root, mapped.focus, undefined, false);
        }
        if (lastKeyWasArrowRef.current && !isSameLiteral(cur.focus, mapped.focus)) {
          const literal = getLiteralFromState(cur.redigertBrev, mapped.focus);
          if (literal && isUneditedFritekst(literal)) {
            lastKeyWasArrowRef.current = false;
            applyAction(updateFocus, setEditorState, { ...mapped.focus, selectAll: true });
            return;
          }
        }
        if (
          cur.selection === undefined &&
          isSameLiteral(cur.focus, mapped.focus) &&
          cur.focus.cursorPosition === mapped.focus.cursorPosition
        ) {
          lastAppliedDomSelectionRef.current = snapshotDomSelection(domSelection);
          return;
        }
        applyAction(updateFocus, setEditorState, mapped.focus);
      }
    };

    document.addEventListener("keydown", onDocKeyDown, true);
    document.addEventListener("pointerdown", onDocPointerDown, true);
    document.addEventListener("pointerup", clearPointer, true);
    document.addEventListener("pointercancel", clearPointer, true);
    document.addEventListener("selectionchange", onSelectionChange);
    return () => {
      document.removeEventListener("keydown", onDocKeyDown, true);
      document.removeEventListener("pointerdown", onDocPointerDown, true);
      document.removeEventListener("pointerup", clearPointer, true);
      document.removeEventListener("pointercancel", clearPointer, true);
      document.removeEventListener("selectionchange", onSelectionChange);
    };
  }, [setEditorState]);

  const getEffectiveModelSelection = (): {
    focus: Focus & { cursorPosition: number };
    selection: SelectionIndex | undefined;
  } | null => {
    const root = hostRef.current;
    const state = editorStateRef.current;
    if (!root) return null;

    const domSelection = globalThis.getSelection();
    if (
      domSelection &&
      domSelection.rangeCount > 0 &&
      !matchesDomSelectionSnapshot(domSelection, lastAppliedDomSelectionRef.current)
    ) {
      const range = domSelection.getRangeAt(0);
      if (root.contains(range.startContainer) && root.contains(range.endContainer)) {
        const mapped = resolveDomSelectionToModel(root);
        if (mapped) {
          return { focus: mapped.focus, selection: mapped.selection };
        }
      }
    }

    if (state.selection && findIndexElementInRoot(root, state.selection.start)) {
      return {
        focus: { ...state.selection.start },
        selection: state.selection,
      };
    }

    if (findIndexElementInRoot(root, state.focus)) {
      const literal = getLiteralFromState(state.redigertBrev, state.focus);
      const fallbackLen = literal ? getLogicalLiteralText(literal).length : 0;
      const cursorPosition =
        state.focus.cursorPosition === undefined || state.focus.cursorPosition < 0
          ? fallbackLen
          : Math.min(state.focus.cursorPosition, fallbackLen);
      return {
        focus: { ...stripFocusMeta(state.focus), cursorPosition },
        selection: undefined,
      };
    }

    return null;
  };

  const getActiveLiteralContext = (): {
    literalEl: HTMLElement;
    literalIndex: LiteralIndex;
    content: LiteralValue | undefined;
    hasDiffDecoration: boolean;
    cursorPosition: number;
    selection: SelectionIndex | undefined;
  } | null => {
    const root = hostRef.current;
    if (!root) return null;

    const effective = getEffectiveModelSelection();
    if (!effective) return null;

    const literalIndex = stripFocusMeta(effective.focus);
    const el = findIndexElementInRoot(root, literalIndex);
    if (!el || !isEditableLiteralEl(el)) return null;

    const content = getLiteralFromState(editorStateRef.current.redigertBrev, literalIndex);
    const hasDiffDecoration =
      el.dataset.diffVersion !== undefined ||
      (diffHash !== undefined && el.querySelector("[data-diff-deletion],[data-diff-insertion]") != null);
    return {
      literalEl: el,
      literalIndex,
      content,
      hasDiffDecoration,
      cursorPosition: effective.focus.cursorPosition,
      selection: effective.selection,
    };
  };

  const handleEditIntent = (literalEl?: HTMLElement, literalIndex?: LiteralIndex) => {
    const cursorPosition = literalEl ? getEditableCharacterOffset(literalEl) : 0;
    disableDiffMode();
    if (literalIndex) {
      applyAction(updateFocus, setEditorState, { ...literalIndex, cursorPosition });
    }
  };

  const selectFullLiteralInModel = (literalEl: HTMLElement) => {
    const idx = parseLiteralIndex(literalEl);
    if (!idx) return;
    const len = getCleanTextLength(literalEl);
    const fullSel = fullRangeForLiteral(idx, len);
    const nextFocus: Focus = { ...idx, cursorPosition: len };
    const root = hostRef.current;
    if (root) {
      lastAppliedDomSelectionRef.current = applyModelSelectionToDom(root, nextFocus, fullSel, true);
    }
    applyAction(updateSelection, setEditorState, fullSel, nextFocus);
  };

  const handleBeforeInput = (nativeEvent: InputEvent) => {
    const root = hostRef.current;
    if (!root || freezeRef.current) {
      if (nativeEvent.cancelable) nativeEvent.preventDefault();
      return;
    }

    const inputType = nativeEvent.inputType ?? "";

    const ctx = getActiveLiteralContext();
    if (ctx?.hasDiffDecoration) {
      if (nativeEvent.cancelable) nativeEvent.preventDefault();
      handleEditIntent(ctx.literalEl, ctx.literalIndex);
      return;
    }

    if (nativeEvent.isComposing || inputType === "insertCompositionText") {
      return;
    }

    if (inputType === "historyUndo") {
      if (nativeEvent.cancelable) nativeEvent.preventDefault();
      undo();
      return;
    }
    if (inputType === "historyRedo") {
      if (nativeEvent.cancelable) nativeEvent.preventDefault();
      redo();
      return;
    }
    if (inputType.startsWith("format")) {
      if (nativeEvent.cancelable) nativeEvent.preventDefault();
      if (inputType === "formatBold") {
        applyAction(Actions.switchFontType, setEditorState, FontType.BOLD);
      } else if (inputType === "formatItalic") {
        applyAction(Actions.switchFontType, setEditorState, FontType.ITALIC);
      }
      return;
    }
    if (inputType === "insertFromDrop" || inputType === "deleteByDrag") {
      if (nativeEvent.cancelable) nativeEvent.preventDefault();
      return;
    }
    if (inputType === "insertParagraph" || inputType === "insertLineBreak") {
      if (nativeEvent.cancelable) nativeEvent.preventDefault();
      if (!ctx) return;
      if (ctx.selection) {
        const breakType = inputType === "insertLineBreak" ? "lineBreak" : "paragraph";
        applyAction(Actions.replaceSelectionWithBreak, setEditorState, ctx.selection, breakType);
        return;
      }
      if (inputType === "insertLineBreak") {
        applyAction(Actions.addNewLine, setEditorState, {
          ...ctx.literalIndex,
          cursorPosition: ctx.cursorPosition,
        });
      } else {
        applyAction(Actions.split, setEditorState, ctx.literalIndex, ctx.cursorPosition);
      }
      return;
    }
    if (inputType === "insertFromPaste") {
      if (nativeEvent.cancelable) nativeEvent.preventDefault();
      if (nativeEvent.dataTransfer && ctx) {
        if (ctx.selection) {
          applyAction(Actions.pasteReplacingSelection, setEditorState, ctx.selection, nativeEvent.dataTransfer);
        } else {
          applyAction(Actions.paste, setEditorState, ctx.literalIndex, ctx.cursorPosition, nativeEvent.dataTransfer);
        }
      }
      return;
    }

    if (inputType === "insertReplacementText") {
      if (nativeEvent.cancelable) nativeEvent.preventDefault();
      const replacement = (nativeEvent.data ?? nativeEvent.dataTransfer?.getData("text/plain") ?? "").replaceAll(
        "\u00A0",
        " ",
      );

      const targetStaticRange = nativeEvent.getTargetRanges?.()[0];
      const targetModel = targetStaticRange ? staticRangeToModelSelection(root, targetStaticRange) : undefined;
      const effectiveSelection = targetModel?.selection ?? ctx?.selection;
      const effectiveFocus =
        targetModel?.focus ?? (ctx ? { ...ctx.literalIndex, cursorPosition: ctx.cursorPosition } : undefined);

      if (effectiveSelection) {
        applyAction(Actions.replaceSelectionWithText, setEditorState, effectiveSelection, replacement);
      } else if (effectiveFocus) {
        const literal = getLiteralFromState(editorStateRef.current.redigertBrev, effectiveFocus);
        if (!literal) return;
        const curText = getLogicalLiteralText(literal);
        const pos = Math.min(Math.max(0, effectiveFocus.cursorPosition), curText.length);
        const nextText = curText.slice(0, pos) + replacement + curText.slice(pos);
        applyAction(
          Actions.updateContentText,
          setEditorState,
          stripFocusMeta(effectiveFocus),
          nextText,
          pos + replacement.length,
        );
      }
      return;
    }

    if (inputType === "insertText") {
      if (nativeEvent.cancelable) nativeEvent.preventDefault();
      const inserted = (nativeEvent.data ?? "").replaceAll("\u00A0", " ");

      if (ctx?.selection) {
        applyAction(Actions.replaceSelectionWithText, setEditorState, ctx.selection, inserted);
        return;
      }
      if (!ctx?.content) {
        // The focus is on a variable or line break (or not rendered as a literal), so insert at the model focus.
        applyAction(Actions.insertText, setEditorState, inserted);
        return;
      }

      const curText = getLogicalLiteralText(ctx.content);
      const pos = Math.min(Math.max(0, ctx.cursorPosition), curText.length);
      const nextText = curText.slice(0, pos) + inserted + curText.slice(pos);
      applyAction(Actions.updateContentText, setEditorState, ctx.literalIndex, nextText, pos + inserted.length);
      return;
    }

    if (inputType.startsWith("delete")) {
      if (nativeEvent.cancelable) nativeEvent.preventDefault();
      const effective = getEffectiveModelSelection();
      if (!effective) return;

      if (effective.selection) {
        applyAction(Actions.deleteSelection, setEditorState, effective.selection);
        return;
      }

      const literalIndex = stripFocusMeta(effective.focus);
      const literal = getLiteralFromState(editorStateRef.current.redigertBrev, literalIndex);
      if (!literal) return;

      const curText = getLogicalLiteralText(literal);
      const pos = Math.min(Math.max(0, effective.focus.cursorPosition), curText.length);
      const isForward = inputType.includes("Forward");

      const isGranularDelete =
        inputType.startsWith("deleteWord") ||
        inputType.startsWith("deleteSoftLine") ||
        inputType.startsWith("deleteHardLine") ||
        inputType === "deleteEntireSoftLine";

      if (isGranularDelete) {
        if (!isForward && pos === 0) {
          if (isTableCellStartBlocked(editorStateRef.current, literalIndex, pos)) return;
          applyAction(Actions.merge, setEditorState, literalIndex, MergeTarget.PREVIOUS);
          return;
        }
        if (isForward && pos >= curText.length) {
          applyAction(Actions.merge, setEditorState, literalIndex, MergeTarget.NEXT);
          return;
        }

        const targetStaticRange = nativeEvent.getTargetRanges?.()[0];
        const targetModel = targetStaticRange ? staticRangeToModelSelection(root, targetStaticRange) : undefined;
        if (
          targetModel?.selection &&
          isSameLiteral(targetModel.selection.start, literalIndex) &&
          isSameLiteral(targetModel.selection.end, literalIndex)
        ) {
          const delStart = Math.min(targetModel.selection.start.cursorPosition, curText.length);
          const delEnd = Math.min(targetModel.selection.end.cursorPosition, curText.length);
          const nextText = curText.slice(0, delStart) + curText.slice(delEnd);
          applyAction(Actions.updateContentText, setEditorState, literalIndex, nextText, delStart);
          return;
        }

        if (inputType.startsWith("deleteWord")) {
          const delStart = isForward ? pos : wordBoundaryBackward(curText, pos);
          const delEnd = isForward ? wordBoundaryForward(curText, pos) : pos;
          const nextText = curText.slice(0, delStart) + curText.slice(delEnd);
          applyAction(Actions.updateContentText, setEditorState, literalIndex, nextText, delStart);
          return;
        }

        const delStart = isForward && inputType !== "deleteEntireSoftLine" ? pos : 0;
        const delEnd = !isForward && inputType !== "deleteEntireSoftLine" ? pos : curText.length;
        const nextText = curText.slice(0, delStart) + curText.slice(delEnd);
        applyAction(Actions.updateContentText, setEditorState, literalIndex, nextText, delStart);
        return;
      }

      if (isForward) {
        if (pos >= curText.length) {
          applyAction(Actions.merge, setEditorState, literalIndex, MergeTarget.NEXT);
        } else {
          const endPos = nextCodePointOffset(curText, pos);
          const nextText = curText.slice(0, pos) + curText.slice(endPos);
          applyAction(Actions.updateContentText, setEditorState, literalIndex, nextText, pos);
        }
      } else {
        if (isTableCellStartBlocked(editorStateRef.current, literalIndex, pos)) {
          return;
        }
        if (pos === 0) {
          applyAction(Actions.merge, setEditorState, literalIndex, MergeTarget.PREVIOUS);
        } else {
          const startPos = prevCodePointOffset(curText, pos);
          const nextText = curText.slice(0, startPos) + curText.slice(pos);
          applyAction(Actions.updateContentText, setEditorState, literalIndex, nextText, startPos);
        }
      }
    }
  };

  const beforeInputHandlerRef = useRef(handleBeforeInput);
  beforeInputHandlerRef.current = handleBeforeInput;

  useLayoutEffect(() => {
    const root = hostRef.current;
    if (!root) return;
    const listener = (event: Event) => {
      beforeInputHandlerRef.current(event as InputEvent);
    };
    root.addEventListener("beforeinput", listener);
    return () => root.removeEventListener("beforeinput", listener);
  }, []);

  /**
   * Dead keys (é, ü on a Norwegian keyboard) and IMEs are the one kind of input the browser applies to the DOM itself:
   * their beforeinput can't be cancelled. When the composition ends, the caret is in the literal the browser composed
   * into, so that literal's text is read back into the model.
   */
  const handleCompositionStart = () => {
    const root = hostRef.current;
    if (!root || freezeRef.current) return;

    const effective = getEffectiveModelSelection();
    if (effective?.selection) {
      // Otherwise the browser deletes the selection itself, which can remove whole literals. Delete it through the
      // model first, so the composition starts at a caret in a DOM that matches the model.
      const selection = effective.selection;
      flushSync(() => applyAction(Actions.deleteSelection, setEditorState, selection));
    }
    isComposingRef.current = true;
  };

  const handleCompositionEnd = () => {
    isComposingRef.current = false;
    const root = hostRef.current;
    if (!root || freezeRef.current) return;

    const domSelection = globalThis.getSelection();
    const range = domSelection && domSelection.rangeCount > 0 ? domSelection.getRangeAt(0) : undefined;
    const literalEl = range ? closestEditableLiteralEl(range.startContainer) : null;
    const literalIndex = literalEl && root.contains(literalEl) ? parseLiteralIndex(literalEl) : undefined;
    if (!range || !literalEl || !literalIndex) return;

    if (literalEl.dataset.diffVersion !== undefined) {
      handleEditIntent(literalEl, literalIndex);
      return;
    }
    const literal = getLiteralFromState(editorStateRef.current.redigertBrev, literalIndex);
    const domText = (literalEl.textContent ?? "").replaceAll(ZERO_WIDTH_SPACE, "").replaceAll("\u00A0", " ");
    if (!literal || domText === getLogicalLiteralText(literal)) return;

    const cursorPosition = getCleanOffsetInLiteral(literalEl, range.startContainer, range.startOffset);
    // The browser may leave the text split over several text nodes; the caret code expects one.
    literalEl.textContent = domText || ZERO_WIDTH_SPACE;
    applyAction(Actions.updateContentText, setEditorState, literalIndex, domText, cursorPosition);
  };

  const handleEnter = (
    event: React.KeyboardEvent<HTMLElement>,
    literalIndex: LiteralIndex,
    cursorPosition: number,
    selection: SelectionIndex | undefined,
  ) => {
    event.preventDefault();
    if (selection) {
      applyAction(
        Actions.replaceSelectionWithBreak,
        setEditorState,
        selection,
        event.shiftKey ? "lineBreak" : "paragraph",
      );
      return;
    }
    if (event.shiftKey) {
      applyAction(Actions.addNewLine, setEditorState, {
        ...literalIndex,
        cursorPosition,
      });
    } else {
      applyAction(Actions.split, setEditorState, literalIndex, cursorPosition);
    }
  };

  const handleVerticalArrow = (
    event: React.KeyboardEvent<HTMLDivElement>,
    literalEl: HTMLElement,
    f: LiteralIndex,
    direction: "up" | "down",
  ) => {
    if (event.shiftKey || event.altKey || event.ctrlKey || event.metaKey) return;

    if (isTableCellIndex(f)) {
      const block = editorState.redigertBrev.blocks[f.blockIndex];
      const content = block?.content[f.contentIndex];

      if (isTable(content)) {
        const next = getTableArrowNavigationFocus(literalEl, f, content, direction);
        if (next === undefined) return;
        event.preventDefault();
        if (next === "exit") {
          setEditorState(exitTable(direction === "up" ? "backward" : "forward"));
        } else {
          applyAction(updateFocus, setEditorState, next);
        }
        return;
      }
    }

    if (isBlockContentIndex(f) && f.blockIndex !== TITLE_INDEX) {
      const tableEntry = adjacentTableEntryFocus(f, editorState.redigertBrev.blocks, direction);
      if (tableEntry) {
        const caretCoordinates = getCaretRect();
        if (!caretCoordinates) return;
        const elementRect = literalEl.getBoundingClientRect();
        const atEdge =
          direction === "up"
            ? caretCoordinates.top <= elementRect.top + 1
            : caretCoordinates.bottom >= elementRect.bottom - 1;
        if (!atEdge) return;

        event.preventDefault();
        applyAction(updateFocus, setEditorState, tableEntry);
        return;
      }
      ensureAdjacentLineVisible(literalEl, direction);
    }
  };

  const handleTab = (event: React.KeyboardEvent<HTMLElement>): boolean => {
    const { focus } = editorState;

    if (
      isTableCellIndex(focus) &&
      isTable(editorState.redigertBrev.blocks[focus.blockIndex]?.content[focus.contentIndex])
    ) {
      const direction = event.shiftKey ? "backward" : "forward";
      event.preventDefault();
      if (direction === "forward" && isAtLastTableCell(editorState)) {
        addRow(editorState, setEditorState, event as React.KeyboardEvent<HTMLSpanElement>);
        return true;
      }

      const next = nextTableFocus(editorState, direction);
      const didMove =
        isTableCellIndex(next) &&
        (next.rowIndex !== focus.rowIndex ||
          next.cellIndex !== focus.cellIndex ||
          next.cellContentIndex !== focus.cellContentIndex);

      if (didMove) {
        applyAction(updateFocus, setEditorState, next);
      } else {
        setEditorState(exitTable(direction));
      }
      return true;
    }

    const scope = hostRef.current?.closest<HTMLElement>(".editor") ?? hostRef.current;
    if (!scope) return false;

    const fritekstEls = [...scope.querySelectorAll<HTMLElement>("[data-fritekst]")];
    if (fritekstEls.length === 0) return false;

    const domSelection = globalThis.getSelection();
    const anchor: Node | null =
      findIndexElementInRoot(scope, focus) ??
      (domSelection && domSelection.rangeCount > 0 && scope.contains(domSelection.anchorNode)
        ? domSelection.anchorNode
        : null);

    let targetEl: HTMLElement | undefined;
    if (!anchor) {
      targetEl = event.shiftKey ? fritekstEls[fritekstEls.length - 1] : fritekstEls[0];
    } else if (event.shiftKey) {
      for (let i = fritekstEls.length - 1; i >= 0; i--) {
        const candidate = fritekstEls[i];
        if (
          !candidate.contains(anchor) &&
          anchor.compareDocumentPosition(candidate) & Node.DOCUMENT_POSITION_PRECEDING
        ) {
          targetEl = candidate;
          break;
        }
      }
    } else {
      for (const candidate of fritekstEls) {
        if (
          !candidate.contains(anchor) &&
          anchor.compareDocumentPosition(candidate) & Node.DOCUMENT_POSITION_FOLLOWING
        ) {
          targetEl = candidate;
          break;
        }
      }
    }

    if (!targetEl) return false;

    event.preventDefault();
    selectFullLiteralInModel(targetEl);
    return true;
  };

  const handleOnPaste = (event: React.ClipboardEvent<HTMLElement>) => {
    event.preventDefault();
    const root = hostRef.current;
    if (!root || freeze) return;

    const ctx = getActiveLiteralContext();
    if (ctx?.hasDiffDecoration) {
      handleEditIntent(ctx.literalEl, ctx.literalIndex);
      return;
    }

    logPastedClipboard(event.clipboardData);

    const limInnMetode = pasteViaKeyboardRef.current
      ? "tastatursnarvei"
      : pasteViaContextMenuRef.current
        ? "kontekstmeny"
        : "annet";
    pasteViaKeyboardRef.current = false;
    pasteViaContextMenuRef.current = false;

    const effective = getEffectiveModelSelection();
    if (!effective) return;

    if (!effective.selection) {
      const literalIndex = stripFocusMeta(effective.focus);
      const offset = effective.focus.cursorPosition;

      const pastedText = event.clipboardData.getData("text/plain");
      const pasteLength = pastedText.length;
      if (hasPasteContent(event.clipboardData)) {
        const pasteMetadata = getPasteMetadata(event.clipboardData);
        trackEvent("tekst limt inn", {
          brevkode: editorState.info.brevkode,
          spraak: editorState.info.spraak,
          antallTegn: pasteLength,
          merEnn200: pasteLength > 200,
          limInnMetode,
          ...pasteMetadata,
        });
      }

      applyAction(Actions.paste, setEditorState, literalIndex, offset, event.clipboardData);
    } else {
      const pastedText = event.clipboardData.getData("text/plain");
      const pasteLength = pastedText.length;
      if (hasPasteContent(event.clipboardData)) {
        const pasteMetadata = getPasteMetadata(event.clipboardData);
        trackEvent("tekst erstattet", {
          brevkode: editorState.info.brevkode,
          spraak: editorState.info.spraak,
          antallTegn: pasteLength,
          merEnn200: pasteLength > 200,
          limInnMetode,
          ...pasteMetadata,
        });
      }

      applyAction(Actions.pasteReplacingSelection, setEditorState, effective.selection, event.clipboardData);
    }
  };

  const handleOnCut = (event: React.ClipboardEvent<HTMLElement>) => {
    const root = hostRef.current;
    if (!root || freeze) return;

    const ctx = getActiveLiteralContext();
    if (ctx?.hasDiffDecoration) {
      event.preventDefault();
      handleEditIntent(ctx.literalEl, ctx.literalIndex);
      return;
    }

    const effective = getEffectiveModelSelection();
    if (!effective?.selection) return;

    writeSemanticClipboard(event, root);
    event.preventDefault();
    applyAction(Actions.deleteSelection, setEditorState, effective.selection);
  };

  const handleOnCopy = (event: React.ClipboardEvent<HTMLElement>) => {
    const root = hostRef.current;
    if (!root) return;
    if (writeSemanticClipboard(event, root)) event.preventDefault();
  };

  const flushPendingDomSelectionToModel = () => {
    const root = hostRef.current;
    if (!root || freezeRef.current || clickedVariableRef.current) return;
    const domSelection = globalThis.getSelection();
    if (!domSelection || domSelection.rangeCount === 0) return;
    if (matchesDomSelectionSnapshot(domSelection, lastAppliedDomSelectionRef.current)) return;

    const range = domSelection.getRangeAt(0);
    if (!root.contains(range.startContainer) || !root.contains(range.endContainer)) return;

    const mapped = resolveDomSelectionToModel(root);
    if (!mapped) return;

    const cur = editorStateRef.current;
    if (mapped.selection) {
      if (!isSameSelection(cur.selection, mapped.selection) || !isSameLiteral(cur.focus, mapped.focus)) {
        applyAction(updateSelection, setEditorState, mapped.selection, mapped.focus);
      }
    } else if (
      cur.selection !== undefined ||
      !isSameLiteral(cur.focus, mapped.focus) ||
      cur.focus.cursorPosition !== mapped.focus.cursorPosition
    ) {
      applyAction(updateFocus, setEditorState, mapped.focus);
    }
  };

  const handleOnKeyDown = (e: React.KeyboardEvent<HTMLDivElement>) => {
    // Keys during a composition belong to the IME, e.g. Enter that commits it.
    if (e.nativeEvent.isComposing) return;
    pasteViaContextMenuRef.current = false;
    const isFirstKeyAfterVariableClick = clickedVariableRef.current;
    clickedVariableRef.current = false;
    shiftKeyDownRef.current = e.shiftKey || e.key === "Shift";

    if (
      (e.target as HTMLElement | null)?.closest('button, [role="menu"], [role="menuitem"], [contenteditable="false"]')
    ) {
      return;
    }

    // A clicked variable has no DOM caret, and the browser invents one at the start of the editor before this event.
    // That caret must not reach the model, so type through the model, which puts the text right after the variable.
    const isCharacterKey = e.key.length === 1 && !e.ctrlKey && !e.metaKey && !e.altKey && !e.nativeEvent.isComposing;
    const { focus, redigertBrev } = editorStateRef.current;
    if (!freeze && isFirstKeyAfterVariableClick && isCharacterKey && !getLiteralFromState(redigertBrev, focus)) {
      e.preventDefault();
      applyAction(Actions.insertText, setEditorState, e.key);
      return;
    }

    if (externalOnKeyDown) {
      flushPendingDomSelectionToModel();
      externalOnKeyDown(e);
    }
    if (e.defaultPrevented || freeze) return;

    const ctx = getActiveLiteralContext();
    if (ctx?.hasDiffDecoration && isTextMutatingKey(e)) {
      e.preventDefault();
      e.stopPropagation();
      handleEditIntent(ctx.literalEl, ctx.literalIndex);
      return;
    }

    const hasMod = isMac ? e.metaKey : e.ctrlKey || e.metaKey;
    const isUndo = hasMod && e.key.toLowerCase() === "z" && !e.shiftKey;
    const isRedo = hasMod && (e.key.toLowerCase() === "y" || (e.key.toLowerCase() === "z" && e.shiftKey));
    const isPasteShortcut =
      ((isMac ? e.metaKey : e.ctrlKey) && e.key.toLowerCase() === "v") || (e.shiftKey && e.key === "Insert");

    if (isPasteShortcut) {
      pasteViaKeyboardRef.current = true;
    }

    if (isUndo) {
      e.preventDefault();
      e.stopPropagation();
      undo();
      return;
    }
    if (isRedo) {
      e.preventDefault();
      e.stopPropagation();
      redo();
      return;
    }

    if (e.key === "Tab") {
      handleTab(e);
      return;
    }

    if (!ctx) return;
    const { literalEl, literalIndex, cursorPosition, selection } = ctx;

    if ((e.key === "Backspace" || e.key === "Delete") && !selection) {
      const tableDeleteAction = determineTableCellDeleteAction(e as React.KeyboardEvent<HTMLSpanElement>, editorState);
      if (tableDeleteAction === "DELETE_TABLE") {
        e.preventDefault();
        applyAction(Actions.removeTable, setEditorState);
        e.stopPropagation();
        return;
      }
      if (tableDeleteAction === "DELETE_ROW") {
        e.preventDefault();
        applyAction(Actions.removeTableRow, setEditorState);
        e.stopPropagation();
        return;
      }
      if (tableDeleteAction === "BLOCK_DEFAULT") {
        e.preventDefault();
        e.stopPropagation();
        return;
      }
    }

    if (e.key === "Enter") {
      handleEnter(e, literalIndex, cursorPosition, selection);
      e.stopPropagation();
      return;
    }
    if (e.altKey && e.shiftKey && (e.key === "ArrowUp" || e.key === "ArrowDown")) {
      if (isTableCellIndex(editorState.focus)) {
        e.preventDefault();
        e.stopPropagation();
        applyAction(Actions.moveTableRow, setEditorState, e.key === "ArrowUp" ? "up" : "down");
      }
      return;
    }
    if (!selection && (e.key === "ArrowUp" || e.key === "ArrowDown")) {
      handleVerticalArrow(e, literalEl, literalIndex, e.key === "ArrowUp" ? "up" : "down");
    }
  };

  const handleOnKeyUp = (e: React.KeyboardEvent<HTMLElement>) => {
    pasteViaKeyboardRef.current = false;
    if (e.key === "Shift") shiftKeyDownRef.current = false;
  };

  const isPointOverCurrentSelection = (clientX: number, clientY: number): boolean => {
    const root = hostRef.current;
    const selection = globalThis.getSelection();
    if (!root || !selection || selection.rangeCount === 0 || selection.isCollapsed) return false;
    const range = selection.getRangeAt(0);
    if (!root.contains(range.startContainer) && !root.contains(range.endContainer)) return false;
    return Array.from(range.getClientRects()).some(
      (rect) =>
        rect.width > 0 &&
        clientX >= rect.left - 4 &&
        clientX <= rect.right + 4 &&
        clientY >= rect.top - 4 &&
        clientY <= rect.bottom + 4,
    );
  };

  const handleOnPointerDown = (e: React.PointerEvent<HTMLElement>) => {
    pointerDownInsideRef.current = true;
    if (e.button === 2 || e.ctrlKey) {
      contextClickOverSelectionRef.current = isPointOverCurrentSelection(e.clientX, e.clientY);
    }
  };

  const handleOnMouseDown = (e: React.MouseEvent<HTMLElement>) => {
    pasteViaContextMenuRef.current = false;
    pointerDownInsideRef.current = true;

    const targetEl = e.target as HTMLElement | null;
    clickedVariableRef.current = !!targetEl?.closest("[data-variable]");
    if (!targetEl || targetEl.closest('[contenteditable="false"]')) {
      fritekstMouseDownRef.current = null;
      return;
    }

    const literalEl = closestEditableLiteralEl(targetEl);
    const erFritekst = !!literalEl?.hasAttribute("data-fritekst");

    if (literalEl && !erFritekst && e.detail === 3) {
      e.preventDefault();
      selectFullLiteralInModel(literalEl);
      fritekstMouseDownRef.current = null;
      return;
    }

    if (!literalEl || !erFritekst) {
      fritekstMouseDownRef.current = null;
      return;
    }

    const literalIdx = parseLiteralIndex(literalEl);
    const curState = editorStateRef.current;
    const wasInside =
      !!literalIdx &&
      ((isSameLiteral(curState.focus, literalIdx) && curState.selection !== undefined) ||
        (!!globalThis.getSelection()?.rangeCount &&
          literalEl.contains(globalThis.getSelection()!.anchorNode) &&
          literalEl.contains(globalThis.getSelection()!.focusNode)));
    fritekstMouseDownRef.current = { element: literalEl, wasInside };

    const domSel = globalThis.getSelection();
    if (domSel && !domSel.isCollapsed && domSel.containsNode(literalEl, true)) {
      domSel.collapse(literalEl, 0);
    }

    if (e.detail >= 2) {
      e.preventDefault();
    }
  };

  const placeContextMenuCaretAt = (clientX: number, clientY: number) => {
    const root = hostRef.current;
    if (!root) return;
    const hasPoint = clientX !== 0 || clientY !== 0;

    if (hasPoint && !contextClickOverSelectionRef.current) {
      gotoCoordinates({ x: clientX, y: clientY });
    }
    contextClickOverSelectionRef.current = false;

    const mapped = resolveDomSelectionToModel(root);
    if (mapped) {
      if (mapped.selection) {
        applyAction(updateSelection, setEditorState, mapped.selection, mapped.focus);
      } else {
        applyAction(updateFocus, setEditorState, mapped.focus);
      }
    }
  };

  const handleOnMouseUp = (e: React.MouseEvent<HTMLElement>) => {
    if (freeze || !e.ctrlKey || e.button !== 0) return;
    placeContextMenuCaretAt(e.clientX, e.clientY);
  };

  const handleOnClick = (e: React.MouseEvent<HTMLElement>) => {
    pointerDownInsideRef.current = false;
    const root = hostRef.current;
    if (!root || freeze) return;

    const targetEl = e.target as HTMLElement | null;
    if (targetEl?.closest('[contenteditable="false"]')) return;

    if (e.ctrlKey && e.button === 0) {
      pasteViaContextMenuRef.current = true;
      placeContextMenuCaretAt(e.clientX, e.clientY);
      return;
    }

    const literalEl = closestEditableLiteralEl(targetEl);
    const fritekstState = fritekstMouseDownRef.current;
    fritekstMouseDownRef.current = null;

    if (literalEl?.hasAttribute("data-fritekst")) {
      const domSel = globalThis.getSelection();
      if (domSel?.isCollapsed && e.detail === 1 && !fritekstState?.wasInside) {
        e.preventDefault();
        selectFullLiteralInModel(literalEl);
        return;
      }
      const mapped = resolveDomSelectionToModel(root);
      if (mapped && !mapped.selection) {
        applyAction(updateFocus, setEditorState, mapped.focus);
      }
      return;
    }

    const mapped = resolveDomSelectionToModel(root);
    if (mapped) {
      if (mapped.selection) {
        applyAction(updateSelection, setEditorState, mapped.selection, mapped.focus);
      } else {
        applyAction(updateFocus, setEditorState, mapped.focus);
      }
    }
  };

  const handleOnDoubleClick = (e: React.MouseEvent<HTMLElement>) => {
    const literalEl = closestEditableLiteralEl(e.target as Node);
    if (!literalEl?.hasAttribute("data-fritekst")) return;
    e.preventDefault();
    e.stopPropagation();
    selectFullLiteralInModel(literalEl);
  };

  const handleOnContextMenu = (e: React.MouseEvent<HTMLElement>) => {
    if (e.defaultPrevented) return;
    pasteViaContextMenuRef.current = true;
    const root = hostRef.current;
    if (!root || freeze) return;
    if ((e.target as HTMLElement | null)?.closest('[contenteditable="false"]')) return;

    placeContextMenuCaretAt(e.clientX, e.clientY);
  };

  const handleOnFocus = (e: React.FocusEvent<HTMLDivElement>) => {
    externalOnFocus?.(e);
    const root = hostRef.current;
    if (!root || freeze) return;

    const fromOutside = !e.relatedTarget || !root.contains(e.relatedTarget as Node);
    if (fromOutside && lastKeyWasTabRef.current && !pointerDownInsideRef.current && !isProgrammaticFocus()) {
      const fritekstEls = [...root.querySelectorAll<HTMLElement>("[data-fritekst]")];
      if (fritekstEls.length > 0) {
        const targetFritekst = shiftKeyDownRef.current ? fritekstEls[fritekstEls.length - 1] : fritekstEls[0];
        selectFullLiteralInModel(targetFritekst);
        return;
      }

      const mapped = resolveDomSelectionToModel(root);
      if (mapped && !mapped.selection) {
        applyAction(updateFocus, setEditorState, mapped.focus);
        return;
      }

      const firstLiteral = root.querySelector<HTMLElement>("[data-literal]");
      const idx = firstLiteral ? parseLiteralIndex(firstLiteral) : undefined;
      if (idx) {
        applyAction(updateFocus, setEditorState, { ...idx, cursorPosition: 0 });
      }
    }
  };

  return (
    // biome-ignore lint/a11y/useSemanticElements: A textarea cannot contain the structured letter.
    <div
      {...rest}
      aria-label="Brevinnhold"
      aria-multiline="true"
      aria-readonly={freeze}
      contentEditable={!freeze}
      onClick={handleOnClick}
      onCompositionEnd={handleCompositionEnd}
      onCompositionStart={handleCompositionStart}
      onContextMenu={handleOnContextMenu}
      onCopy={handleOnCopy}
      onCut={handleOnCut}
      onDoubleClick={handleOnDoubleClick}
      onDragOver={(e: React.DragEvent<HTMLElement>) => {
        e.preventDefault();
        e.dataTransfer.dropEffect = "none";
      }}
      onDragStart={(e: React.DragEvent<HTMLElement>) => e.preventDefault()}
      onDrop={(e: React.DragEvent<HTMLElement>) => e.preventDefault()}
      onFocus={handleOnFocus}
      onKeyDown={handleOnKeyDown}
      onKeyUp={handleOnKeyUp}
      onMouseDown={handleOnMouseDown}
      onMouseUp={handleOnMouseUp}
      onPaste={handleOnPaste}
      onPointerDown={handleOnPointerDown}
      ref={setRefs}
      role="textbox"
      suppressContentEditableWarning
      tabIndex={tabIndex ?? (freeze ? -1 : 0)}
    >
      {children}
    </div>
  );
}

function isTableCellStartBlocked(
  editorState: { redigertBrev: EditedDocument },
  literalIndex: LiteralIndex,
  cursorPosition: number,
): boolean {
  if (!isTableCellIndex(literalIndex)) return false;
  const block = editorState.redigertBrev.blocks[literalIndex.blockIndex];
  const contentAtFocus = block?.content[literalIndex.contentIndex];
  if (!isTable(contentAtFocus)) return false;
  const cell =
    literalIndex.rowIndex === -1
      ? contentAtFocus.header.colSpec[literalIndex.cellIndex]?.headerContent
      : contentAtFocus.rows[literalIndex.rowIndex]?.cells[literalIndex.cellIndex];
  if (!cell) return false;
  const cellIsEmpty = cell.text.every(
    (node) => node.type === "LITERAL" && getLogicalLiteralText(node).trim().length === 0,
  );
  return (literalIndex.cellContentIndex === 0 && cursorPosition === 0) || cellIsEmpty;
}
