import { css } from "@emotion/react";
import { useEffect, useRef } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { fontTypeOf } from "~/Brevredigering/LetterEditor/actions/common";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import {
  areAnyContentEditableSiblingsPlacedLower,
  findOnLineBelow,
  getCaretRect,
  gotoCoordinates,
} from "~/Brevredigering/LetterEditor/services/caretUtils";
import { type EditedLetter, FontType } from "~/types/brevbakerTypes";

export const EditedLetterTitle = ({ title }: { title: EditedLetter["title"] }) => {
  const contentEditableReference = useRef<HTMLSpanElement>(null);
  const { freeze, editorState, setEditorState } = useEditor();

  // TODO(stw) Tilbakestill malen must also update Title
  // TODO(stw) Undo

  // const shouldBeFocused = hasFocus(editorState.focus, literalIndex);
  const text = title.text[0].text;

  useEffect(() => {
    if (contentEditableReference.current !== null && contentEditableReference.current.textContent !== text) {
      contentEditableReference.current.textContent = text;
    }
  }, [text]);

  // useEffect(() => {
  //   if (
  //     !freeze &&
  //     // shouldBeFocused &&
  //     contentEditableReference.current !== null &&
  //     editorState.focus.cursorPosition !== undefined
  //   ) {
  //     focusAtOffset(contentEditableReference.current.childNodes[0], editorState.focus.cursorPosition);
  //   }
  // }, [editorState.focus.cursorPosition, freeze]);
  // // }, [editorState.focus.cursorPosition, shouldBeFocused, freeze]);

  /**
   * When changing lines with ArrowUp/ArrowDown we sometimes "artificially click" the next line.
   * If y-coord is exactly at the edge it sometimes misses. To avoid that we move the point a little bit away from the line.
   */
  const Y_COORD_SAFETY_MARGIN = 10;

  const handleMoveDown = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    event.preventDefault();
    const element = contentEditableReference.current;
    const caretCoordinates = getCaretRect();

    if (element === null || caretCoordinates === undefined) {
      return;
    }

    const shouldDoItOurselves = !areAnyContentEditableSiblingsPlacedLower(element);
    if (shouldDoItOurselves) {
      const next = findOnLineBelow(element);

      if (next) {
        gotoCoordinates({ x: caretCoordinates.x, y: next.top + Y_COORD_SAFETY_MARGIN });
        event.preventDefault();
      }
    }
  };

  return (
    <span
      contentEditable={!freeze}
      // TODO(stw) Add tests
      // TODO(stw) bold/italic irrellevant, block F/K when title has focus?
      // TODO(stw) F/K may operate on wrong selection if caret focus in Title
      // css={css`
      //   ${fontTypeOf(title.text[0]) === FontType.BOLD && "font-weight: bold;"}
      //   ${fontTypeOf(title.text[0]) === FontType.ITALIC && "font-style: italic;"}
      //   ${freeze && "color: blue"}
      // `}
      // onClick={handleOnclick}
      // onFocus={handleOnFocus}
      onInput={(event) => {
        applyAction(Actions.updateTitle, setEditorState, (event.target as HTMLSpanElement).textContent ?? "");
      }}
      onKeyDown={(event) => {
        if (event.key === "Enter" || event.key === "ArrowDown") {
          handleMoveDown(event);
          return;
        }
      }}
      ref={contentEditableReference}
      tabIndex={-1}
    />
  );
};
