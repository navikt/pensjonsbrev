import { BodyShort, Button } from "@navikt/ds-react";
import type { ReactNode } from "react";

import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { FontType } from "~/types/brevbakerTypes";

import Actions from "../actions";
import { getCurrentActiveFontTypeAtCursor } from "../actions/switchFontType";
import { applyAction } from "../lib/actions";

const EditorFonts = () => {
  const { editorState, freeze, setEditorState } = useEditor();
  const activeFontType = getCurrentActiveFontTypeAtCursor(editorState);

  return (
    <>
      <FontButton
        active={activeFontType === FontType.BOLD}
        dataCy="fonttype-bold"
        disabled={freeze || editorState.focus.blockIndex < 0}
        onClick={() => {
          applyAction(Actions.switchFontType, setEditorState, FontType.BOLD);
        }}
        text={<BodyShort weight="semibold">F</BodyShort>}
      />
      <FontButton
        active={activeFontType === FontType.ITALIC}
        dataCy="fonttype-italic"
        disabled={freeze || editorState.focus.blockIndex < 0}
        onClick={() => {
          applyAction(Actions.switchFontType, setEditorState, FontType.ITALIC);
        }}
        text={
          <BodyShort css={{ fontStyle: "italic" }} weight="semibold">
            K
          </BodyShort>
        }
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
  dataCy: string;
}) => {
  return (
    <Button
      color="text-neutral"
      data-cy={props.dataCy}
      disabled={props.disabled}
      onClick={props.onClick}
      size="small"
      type="button"
      variant={props.active ? "primary-neutral" : "tertiary-neutral"}
    >
      {props.text}
    </Button>
  );
};
