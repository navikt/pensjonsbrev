import { createContext, useContext } from "react";

import { type BlockChange } from "../diff";
import { type FieldInfo, type MoveTargetOption } from "../draftOps";
import { type CommentAnchor, type Draft, type Language } from "../types";
import { type ValidationIssue } from "../validate";

export type EditorContextValue = {
  draft: Draft;
  languages: Language[];
  /** Språket som redigeres nå. */
  language: Language;
  /** Vis alle språk samtidig for hver tekst. */
  showAllLanguages: boolean;
  canEdit: boolean;
  /** Falsk for importerte kildeprofiler: da kan bare tekst og oversettelser endres, ikke struktur, datafelt, vilkår eller metadata. */
  canEditStructure: boolean;
  aiEnabled: boolean;
  fieldIndex: Map<string, FieldInfo>;
  issues: ValidationIssue[];
  blockChanges: Map<string, BlockChange>;
  /** Åpner gjennomgang med nytt kommentarutkast forankret her. */
  onComment: (anchor: CommentAnchor) => void;
  commentCounts: Map<string, number>;
  /** Merker neste endring som tasting i ett felt, så angre slår dem sammen. */
  typing: (key: string) => void;
  /** Lister blokken kan flyttes til (aldri den selv eller en etterkommer). */
  moveTargets: (blockId: string) => MoveTargetOption[];
  /** Flytter blokken til slutten av valgt liste. */
  onRelocate: (blockId: string, option: MoveTargetOption) => void;
};

const EditorContext = createContext<EditorContextValue | undefined>(undefined);

export const EditorProvider = EditorContext.Provider;

export function useEditor(): EditorContextValue {
  const value = useContext(EditorContext);
  if (!value) {
    throw new Error("useEditor må brukes inni EditorProvider");
  }
  return value;
}
