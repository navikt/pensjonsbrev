import { BodyShort, Button, Tooltip } from "@navikt/ds-react";
import { type ReactNode } from "react";

import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { FontType } from "~/types/brevbakerTypes";

import Actions from "../actions";
import { type FormattingState, formattingStateOf } from "../actions/switchFontType";
import { applyAction } from "../lib/actions";
import { tooltipText } from "../utils";

const EditorFonts = () => {
  const { editorState, freeze, setEditorState } = useEditor();

  return (
    <>
      <FontButton
        data-testid="fonttype-bold-button"
        disabled={freeze || editorState.focus.blockIndex < 0}
        onClick={() => {
          applyAction(Actions.switchFontType, setEditorState, FontType.BOLD);
        }}
        state={formattingStateOf(editorState, FontType.BOLD)}
        text={<BodyShort weight="semibold">F</BodyShort>}
        tooltip={tooltipText.bold}
      />
      <FontButton
        data-testid="fonttype-italic-button"
        disabled={freeze || editorState.focus.blockIndex < 0}
        onClick={() => {
          applyAction(Actions.switchFontType, setEditorState, FontType.ITALIC);
        }}
        state={formattingStateOf(editorState, FontType.ITALIC)}
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
  state: FormattingState;
  onClick: () => void;
  text: ReactNode;
  disabled?: boolean;
  "data-testid": string;
  tooltip: string;
}) => {
  return (
    <Tooltip content={props.tooltip}>
      <Button
        aria-pressed={props.state === "mixed" ? "mixed" : props.state === "on"}
        color="text-neutral"
        data-testid={props["data-testid"]}
        disabled={props.disabled}
        onClick={props.onClick}
        size="small"
        type="button"
        variant={props.state === "on" ? "primary-neutral" : "tertiary-neutral"}
      >
        {props.text}
      </Button>
    </Tooltip>
  );
};
