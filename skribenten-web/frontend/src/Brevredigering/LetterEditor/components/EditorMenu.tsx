import { css } from "@emotion/react";
import { CheckmarkCircleFillIcon, ExclamationmarkTriangleFillIcon } from "@navikt/aksel-icons";
import { Button, HStack, Label, Loader, Select } from "@navikt/ds-react";
import { format, isToday } from "date-fns";
import { memo, useEffect, useRef, useState } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import { VerticalDivider } from "~/components/Divider";
import { FontType } from "~/types/brevbakerTypes";
import { getLiteralEditedFontTypeForBlock } from "~/utils/brevbakerUtils";
import { formatTime } from "~/utils/dateUtils";

import type { CallbackReceiver } from "../lib/actions";
import { applyAction } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { getCursorOffset } from "../services/caretUtils";
import type { Typography } from "../utils";
import { TypographyToText } from "../utils";

const SelectTypography = (props: {
  editorState: LetterEditorState;
  setEditorState: CallbackReceiver<LetterEditorState>;
}) => {
  const changeableContent = isTextContent(
    props.editorState.redigertBrev.blocks[props.editorState.focus.blockIndex]?.content?.[
      props.editorState.focus.contentIndex
    ],
  );

  return (
    <Select
      data-cy="typography-select"
      hideLabel
      label="Tekst stil"
      onChange={(e) => {
        applyAction(
          Actions.switchTypography,
          props.setEditorState,
          props.editorState.focus,
          e.target.value as Typography,
        );
        //setter fokuset tilbake til editor etter valgt tekststil
        applyAction(Actions.cursorPosition, props.setEditorState, getCursorOffset());
      }}
      readOnly={!changeableContent}
      size="small"
      value={props.editorState.redigertBrev.blocks[props.editorState.focus.blockIndex]?.type}
    >
      {Object.entries(TypographyToText).map(([key, value]) => (
        <option key={key} value={key}>
          {value}
        </option>
      ))}
    </Select>
  );
};

export const EditorMenu = () => {
  const { freeze, error, editorState, setEditorState } = useEditor();

  return (
    <div
      css={css`
        border-bottom: 1px solid var(--a-gray-200);
        background: var(--a-white);
        padding: var(--a-spacing-2) var(--a-spacing-4);
        display: flex;
        gap: var(--a-spacing-2);
        align-self: stretch;
        align-items: center;
        justify-content: space-between;
      `}
    >
      <HStack gap="5">
        <EditorFonts editorState={editorState} setEditorState={setEditorState} />
        <VerticalDivider />
        <SelectTypography editorState={editorState} setEditorState={setEditorState} />
      </HStack>

      <LagretTidspunkt
        datetime={editorState.info.sistredigert}
        error={error}
        freeze={freeze}
        isDirty={editorState.isDirty}
      />
    </div>
  );
};

//delay = millisekunder
const useTimeoutValue = (argz: { initialValue: React.ReactNode; newValue: React.ReactNode; delay: number }) => {
  const [value, setValue] = useState(argz.initialValue);
  const isMountingReference = useRef(true);

  useEffect(() => {
    isMountingReference.current = false;
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => {
      if (isMountingReference.current) {
        setValue(null);
        return;
      }
      setValue(argz.newValue);
    }, argz.delay);

    return () => {
      return clearTimeout(timer);
    };
  }, [argz.delay, argz.newValue]);

  return isMountingReference.current ? null : value;
};

const LagringSuccess = memo((properties: { dateTime: string }) => {
  const ikon = useTimeoutValue({
    initialValue: <CheckmarkCircleFillIcon color="#007C2E" fontSize="1.5rem" title="error-ikon" />,
    newValue: null,
    delay: 2500,
  });

  const tekst = isToday(properties.dateTime)
    ? `Lagret kl ${formatTime(properties.dateTime)}`
    : `Lagret ${format(properties.dateTime, "dd.MM.yyyy HH:mm")}`;

  return (
    <HStack gap="1">
      {ikon}
      {tekst}
    </HStack>
  );
});

const LagretTidspunkt = memo(
  ({ freeze, error, datetime, isDirty }: { freeze: boolean; error: boolean; datetime: string; isDirty: boolean }) => {
    if (freeze) {
      return (
        <HStack gap="1">
          <Loader title="Lagrer..." />
          Lagrer...
        </HStack>
      );
    } else {
      if (isDirty && error) {
        const tekst = isToday(datetime)
          ? `Klarte ikke lagre kl ${formatTime(datetime)}`
          : `Klarte ikke lagre ${format(datetime, "dd.MM.yyyy HH:mm")}`;

        return (
          <HStack gap="1">
            <ExclamationmarkTriangleFillIcon color="#FF9100" fontSize="1.5rem" title="error-ikon" />
            {tekst}
          </HStack>
        );
      }

      return <LagringSuccess dateTime={datetime} />;
    }
  },
);

export const EditorFonts = (props: {
  editorState: LetterEditorState;
  setEditorState: CallbackReceiver<LetterEditorState>;
}) => {
  const changeableContent = isTextContent(
    props.editorState.redigertBrev.blocks[props.editorState.focus.blockIndex]?.content?.[
      props.editorState.focus.contentIndex
    ],
  );

  const activeFontType = getLiteralEditedFontTypeForBlock(props.editorState);

  return (
    <div>
      <FontButton
        active={activeFontType === FontType.BOLD}
        disabled={!changeableContent}
        onClick={() => {
          if (activeFontType === FontType.BOLD) {
            applyAction(Actions.switchFontType, props.setEditorState, props.editorState.focus, FontType.PLAIN);
          } else {
            applyAction(Actions.switchFontType, props.setEditorState, props.editorState.focus, FontType.BOLD);
          }
          //setter fokuset tilbake til editor etter valgt fonttype
          applyAction(Actions.cursorPosition, props.setEditorState, getCursorOffset());
        }}
        text={<Label>F</Label>}
      />
      <FontButton
        active={activeFontType === FontType.ITALIC}
        disabled={!changeableContent}
        onClick={() => {
          if (activeFontType === FontType.ITALIC) {
            applyAction(Actions.switchFontType, props.setEditorState, props.editorState.focus, FontType.PLAIN);
          } else {
            applyAction(Actions.switchFontType, props.setEditorState, props.editorState.focus, FontType.ITALIC);
          }
          //setter fokuset tilbake til editor etter valgt fonttype
          applyAction(Actions.cursorPosition, props.setEditorState, getCursorOffset());
        }}
        text={
          <Label
            css={css`
              font-style: italic;
            `}
          >
            K
          </Label>
        }
      />
    </div>
  );
};

const FontButton = (props: { active: boolean; onClick: () => void; text: React.ReactNode; disabled: boolean }) => {
  return (
    <Button
      css={css`
        color: var(--a-text-default);
        width: 32px;
        height: 32px;

        &:hover {
          color: var(--a-text-default);
        }

        /*
          TODO - style knappen etter ønsket design.
        */
        ${props.active &&
        css`
          background-color: #ccc;
          color: #000;
          border-color: #999;
          box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
        `}
      `}
      disabled={props.disabled}
      onClick={props.onClick}
      size="small"
      type="button"
      variant="tertiary"
    >
      {props.text}
    </Button>
  );
};
