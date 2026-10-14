import { BodyShort, Button, Tooltip } from "@navikt/ds-react";
import { type ReactNode } from "react";

import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { FontType } from "~/types/brevbakerTypes";

import Actions from "../actions";
import { isFocusInTableHeader } from "../actions/common";
import { getCurrentActiveFontTypeAtCursor } from "../actions/switchFontType";
import { applyAction } from "../lib/actions";
import { refocusEditableAtSelection } from "../services/caretUtils";
import { tooltipText } from "../utils";

const EditorFonts = () => {
  const { editorState, freeze, setEditorState } = useEditor();
  const activeFontType = getCurrentActiveFontTypeAtCursor(editorState);
  const boldDisabled = isFocusInTableHeader(editorState);

  const switchFontType = (fontType: FontType) => {
    // Must run before the action: the resulting focus event would otherwise overwrite the focus set by the action.
    refocusEditableAtSelection();
    applyAction(Actions.switchFontType, setEditorState, fontType);
  };

  return (
    <>
      <FontButton
        active={!boldDisabled && activeFontType === FontType.BOLD}
        data-testid="fonttype-bold-button"
        disabled={freeze || editorState.focus.blockIndex < 0 || boldDisabled}
        onClick={() => {
          switchFontType(FontType.BOLD);
        }}
        text={<BodyShort weight="semibold">F</BodyShort>}
        tooltip={tooltipText.bold}
      />
      <FontButton
        active={activeFontType === FontType.ITALIC}
        data-testid="fonttype-italic-button"
        disabled={freeze || editorState.focus.blockIndex < 0}
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
