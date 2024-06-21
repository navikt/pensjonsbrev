import type { Content, EditedLetter, LiteralValue, VariableValue } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

import type { LetterEditorState } from "../model/state";

export function cleanseText(text: string): string {
  return text.replaceAll("<br>", "").replaceAll("&nbsp;", " ");
}

export function isEditableContent(content: Content | undefined | null): boolean {
  return content != null && (content.type === VARIABLE || content.type === ITEM_LIST);
}

export function text<T extends LiteralValue | VariableValue | undefined>(
  content: T,
): string | (undefined extends T ? undefined : never) {
  if (content?.type === LITERAL) {
    return content.editedText ?? content.text;
  } else if (content?.type === VARIABLE) {
    return content.text;
  } else {
    return undefined as undefined extends T ? undefined : never;
  }
}

export function create(redigertBrev?: EditedLetter): LetterEditorState {
  return {
    redigertBrev: redigertBrev || {
      title: "",
      sakspart: {
        gjelderNavn: "",
        gjelderFoedselsnummer: "",
        saksnummer: "",
        dokumentDato: "",
      },
      blocks: [],
      signatur: {
        hilsenTekst: "",
        saksbehandlerRolleTekst: "",
        saksbehandlerNavn: "",
        navAvsenderEnhet: "",
      },
      deletedBlocks: [],
    },
    focus: { blockIndex: 0, contentIndex: 0 },
  };
}
