import { type Draft, produceWithPatches } from "immer";

import { type BrevInfo, type SaksbehandlerValg } from "~/types/brev";
import { type AttachmentV2, type BrevResponseV2, type DocumentV2, type LetterV2 } from "~/types/brevV2";

import { type Caret, caret, containers, TITLE } from "../model/paths";
import { addPatches, emptyHistory, type History } from "./history";

export type Focus = Caret & { selectAll?: boolean };

export type SaveStatus = "DIRTY" | "SAVE_PENDING" | "SAVED";

export type EditorStateV2<D extends DocumentV2 = DocumentV2> = {
  info: BrevInfo;
  redigertBrev: D;
  redigertBrevHash: string;
  saksbehandlerValg: SaksbehandlerValg;
  saveStatus: SaveStatus;
  focus: Focus;
  history: History<D, SaksbehandlerValg>;
};

export type LetterStateV2 = EditorStateV2<LetterV2>;

export type Command<Args extends unknown[], D extends DocumentV2 = DocumentV2> = (
  draft: Draft<EditorStateV2<D>>,
  ...args: Args
) => void;

export type LetterCommand<Args extends unknown[]> = Command<Args, LetterV2>;

export type Updater<S> = (update: (previous: S) => S) => void;

function initialFocus(doc: DocumentV2): Focus {
  return caret(containers(doc)[0] ?? TITLE);
}

export function createLetterState(brev: BrevResponseV2): LetterStateV2 {
  return {
    info: brev.info,
    redigertBrev: brev.redigertBrev,
    redigertBrevHash: brev.redigertBrevHash,
    saksbehandlerValg: brev.saksbehandlerValg,
    saveStatus: "SAVED",
    focus: initialFocus(brev.redigertBrev),
    history: emptyHistory(),
  };
}

export function createAttachmentState(info: BrevInfo, vedlegg: AttachmentV2): EditorStateV2 {
  const document: DocumentV2 = { title: vedlegg.title, blocks: vedlegg.blocks, deletedBlocks: vedlegg.deletedBlocks };
  return {
    info,
    redigertBrev: document,
    redigertBrevHash: "",
    saksbehandlerValg: {},
    saveStatus: "SAVED",
    focus: initialFocus(document),
    history: emptyHistory(),
  };
}

export function run<D extends DocumentV2, S extends EditorStateV2<D>, Args extends unknown[]>(
  state: S,
  command: Command<Args, D>,
  ...args: Args
): S {
  const [next, patches, inversePatches] = produceWithPatches(state, (draft) => {
    command(draft, ...args);
  });
  if (next.redigertBrev === state.redigertBrev) return next;
  return { ...next, saveStatus: "DIRTY", history: addPatches(next.history, patches, inversePatches) };
}

export function apply<D extends DocumentV2, S extends EditorStateV2<D>, Args extends unknown[]>(
  update: Updater<S>,
  command: Command<Args, D>,
  ...args: Args
): void {
  update((state) => run(state, command, ...args));
}
