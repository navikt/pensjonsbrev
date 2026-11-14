import { BodyShort, Button, Tooltip } from "@navikt/ds-react";
import { type ReactNode } from "react";

import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { FontType } from "~/types/brevbakerTypes";

import { isFocusInTableHeader, isFocusInTitle } from "../actions/common";
import { getCurrentActiveFontTypeAtCursor } from "../actions/switchFontType";
import { useAttestantDiff } from "../diff/AttestantDiffContext";
import { refocusEditableAtSelection } from "../services/caretUtils";
import { switchFontTypeAtSelection, tooltipText } from "../utils";

const EditorFonts = () => {
  const { editorState, freeze, setEditorState } = useEditor();
  const activeFontType = getCurrentActiveFontTypeAtCursor(editorState);
  const disabled = freeze || editorState.focus.blockIndex < 0 || isFocusInTitle(editorState);
  // Tabelloverskrifter er allerede fete
  const boldDisabled = disabled || isFocusInTableHeader(editorState);

  const { disableDiffMode } = useAttestantDiff();

  const switchFontType = (fontType: FontType) => {
    // Refocusing must run before the action: the resulting focus event would otherwise overwrite the focus set by it.
    switchFontTypeAtSelection(setEditorState, fontType, editorRoot(), disableDiffMode, refocusEditableAtSelection);
  };

  return (
    <>
      <FontButton
        active={!boldDisabled && activeFontType === FontType.BOLD}
        data-testid="fonttype-bold-button"
        disabled={boldDisabled}
        onClick={() => {
          switchFontType(FontType.BOLD);
        }}
        text={<BodyShort weight="semibold">F</BodyShort>}
        tooltip={tooltipText.bold}
      />
      <FontButton
        active={!disabled && activeFontType === FontType.ITALIC}
        data-testid="fonttype-italic-button"
        disabled={disabled}
        onClick={() => {
          switchFontType(FontType.ITALIC);
        }}
        text={
          <BodyShort css={{ fontStyle: "italic" }} weight="semibold">
            K
          </BodyShort>
        }
        tooltip={tooltipText.italic}
      />
    </>
  );
};

export default EditorFonts;

const FontButton = (props: {
  active: boolean;
  onClick: () => void;
  text: ReactNode;
  disabled?: boolean;
  "data-testid": string;
  tooltip: string;
}) => {
  return (
    <Tooltip content={props.tooltip}>
      <Button
        color="text-neutral"
        data-testid={props["data-testid"]}
        disabled={props.disabled}
        onClick={props.onClick}
        // Keep focus and selection in the editor when clicking
        onMouseDown={(event) => event.preventDefault()}
        size="small"
        type="button"
        variant={props.active ? "primary-neutral" : "tertiary-neutral"}
      >
        {props.text}
      </Button>
    </Tooltip>
  );
};

const editorRoot = () => {
  const node = globalThis.getSelection()?.anchorNode;
  return (
    (node instanceof HTMLElement ? node : node?.parentElement)?.closest<HTMLElement>("[data-editor-root]") ?? undefined
  );
};
