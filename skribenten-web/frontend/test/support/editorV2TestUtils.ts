import { createLetterState, type EditorStateV2, type LetterStateV2 } from "~/Brevredigering/EditorV2/state/editorState";
import {
  type BlockV2,
  type CellV2,
  type ColumnSpecV2,
  type DocumentV2,
  type FontTypeV2,
  type ItemV2,
  type LetterV2,
  type ListBlockV2,
  type LiteralV2,
  type NewLineV2,
  type ParagraphV2,
  type RowV2,
  type TableV2,
  type TextBlockTypeV2,
  type TextBlockV2,
  type TextV2,
  type VariableV2,
} from "~/types/brevV2";

import { brevInfo } from "./brevFixtures";

let nextId = 1000;
const id = () => nextId++;

type Ids = { id?: number | null; parentId?: number | null };

function adopt<T extends Ids>(children: T[], parentId: number | null | undefined): T[] {
  return children.map((child) => ({ ...child, parentId: child.parentId ?? parentId ?? null }));
}

export function literal(
  args:
    | string
    | (Ids & {
        text: string;
        editedText?: string | null;
        fontType?: FontTypeV2;
        editedFontType?: FontTypeV2 | null;
        fritekst?: boolean;
      }),
): LiteralV2 {
  const a = typeof args === "string" ? { text: args } : args;
  return {
    type: "LITERAL",
    id: a.id === undefined ? id() : a.id,
    parentId: a.parentId ?? null,
    text: a.text,
    editedText: a.editedText ?? null,
    fontType: a.fontType ?? "PLAIN",
    editedFontType: a.editedFontType ?? null,
    editBehaviour: a.fritekst ? "FRITEKST" : null,
  };
}

export function fritekst(text: string): LiteralV2 {
  return literal({ text, fritekst: true });
}

export function newText(text: string, fontType: FontTypeV2 = "PLAIN"): LiteralV2 {
  return literal({ id: null, text: "", editedText: text, fontType });
}

export function variable(text: string, fontType: FontTypeV2 = "PLAIN"): VariableV2 {
  return { type: "VARIABLE", id: id(), parentId: null, text, fontType, editBehaviour: null };
}

export function newLine(): NewLineV2 {
  return { type: "NEW_LINE", id: id(), parentId: null, text: "", fontType: "PLAIN" };
}

type ContainerArgs = Ids & { content: TextV2[]; deletedContent?: number[] };

function containerArgs(args: TextV2[] | ContainerArgs): ContainerArgs {
  return Array.isArray(args) ? { content: args } : args;
}

function textBlock(type: TextBlockTypeV2, args: TextV2[] | ContainerArgs): TextBlockV2 {
  const a = containerArgs(args);
  const blockId = a.id === undefined ? id() : a.id;
  const base = {
    id: blockId,
    parentId: a.parentId ?? null,
    content: adopt(a.content, blockId),
    deletedContent: a.deletedContent ?? [],
    missingFromTemplate: false,
  };
  switch (type) {
    case "PARAGRAPH":
      return { ...base, type };
    case "TITLE2":
      return { ...base, type };
    case "TITLE3":
      return { ...base, type };
    case "TITLE4":
      return { ...base, type };
  }
}

export const paragraph = (...content: TextV2[]): ParagraphV2 => ({
  ...textBlock("PARAGRAPH", content),
  type: "PARAGRAPH",
});
export const paragraphWith = (args: ContainerArgs): TextBlockV2 => textBlock("PARAGRAPH", args);
export const title2 = (...content: TextV2[]) => textBlock("TITLE2", content);
export const title3 = (...content: TextV2[]) => textBlock("TITLE3", content);
export const title4 = (...content: TextV2[]) => textBlock("TITLE4", content);

export function item(...content: TextV2[]): ItemV2 {
  const itemId = id();
  return { id: itemId, parentId: null, content: adopt(content, itemId), deletedContent: [] };
}

export function itemWith(args: ContainerArgs): ItemV2 {
  const itemId = args.id === undefined ? id() : args.id;
  return {
    id: itemId,
    parentId: args.parentId ?? null,
    content: adopt(args.content, itemId),
    deletedContent: args.deletedContent ?? [],
  };
}

export function list(...items: ItemV2[]): ListBlockV2 {
  return listOf("ITEM_LIST", items);
}

export function numberedList(...items: ItemV2[]): ListBlockV2 {
  return listOf("NUMBERED_LIST", items);
}

export function listOf(
  type: ListBlockV2["type"],
  items: ItemV2[],
  args: Ids & { deletedItems?: number[] } = {},
): ListBlockV2 {
  const listId = args.id === undefined ? id() : args.id;
  return {
    id: listId,
    parentId: args.parentId ?? null,
    type,
    items: adopt(items, listId),
    deletedItems: args.deletedItems ?? [],
    missingFromTemplate: false,
  };
}

export function column(...content: TextV2[]): ColumnSpecV2 {
  const columnId = id();
  return {
    id: columnId,
    parentId: null,
    alignment: "LEFT",
    span: 1,
    content: adopt(content, columnId),
    deletedContent: [],
  };
}

export function cell(...content: TextV2[]): CellV2 {
  const cellId = id();
  return { id: cellId, parentId: null, content: adopt(content, cellId), deletedContent: [] };
}

export function row(...cells: CellV2[]): RowV2 {
  const rowId = id();
  return { id: rowId, parentId: null, cells: adopt(cells, rowId), deletedCells: [] };
}

export function table(header: ColumnSpecV2[], rows: RowV2[]): TableV2 {
  const tableId = id();
  const headerId = id();
  return {
    id: tableId,
    parentId: null,
    type: "TABLE",
    header: { id: headerId, parentId: tableId, colSpec: adopt(header, headerId), deletedColSpecs: [] },
    rows: adopt(rows, tableId),
    deletedRows: [],
    missingFromTemplate: false,
  };
}

export function missingFromTemplate<T extends BlockV2>(block: T): T {
  return { ...block, missingFromTemplate: true };
}

export function letterV2(args: { title?: TextV2[]; blocks: BlockV2[] }): LetterV2 {
  return {
    title: { text: args.title ?? [literal("Tittel")], deletedContent: [] },
    blocks: args.blocks,
    deletedBlocks: [],
    saksinformasjon: {
      gjelderNavn: "Test Testesen",
      gjelderPersonidentifikator: "12345678910",
      saksnummer: "22981081",
      dokumentDato: "2024-09-25",
    },
    signatur: { saksbehandlerNavn: "Sak S. Behandler", navAvsenderEnhet: "Nav Arbeid og ytelser Sørlandet" },
    version: 1,
  };
}

export function stateV2(...blocks: BlockV2[]): LetterStateV2 {
  return stateOf(letterV2({ blocks }));
}

export function stateOf(redigertBrev: LetterV2): LetterStateV2 {
  return createLetterState({
    info: { ...brevInfo(), markupVersjon: "V2" },
    redigertBrev,
    redigertBrevHash: "hash",
    saksbehandlerValg: {},
  });
}

export function blockAt<S extends EditorStateV2<DocumentV2>>(state: S, index: number): BlockV2 {
  return state.redigertBrev.blocks[index];
}

export function textBlockAt<S extends EditorStateV2<DocumentV2>>(state: S, index: number): TextBlockV2 {
  const block = state.redigertBrev.blocks[index];
  switch (block?.type) {
    case "PARAGRAPH":
    case "TITLE2":
    case "TITLE3":
    case "TITLE4":
      return block;
    default:
      throw new Error(`Block ${index} is not a text block`);
  }
}

export function listAtIndex<S extends EditorStateV2<DocumentV2>>(state: S, index: number): ListBlockV2 {
  const block = state.redigertBrev.blocks[index];
  if (block?.type === "ITEM_LIST" || block?.type === "NUMBERED_LIST") return block;
  throw new Error(`Block ${index} is not a list`);
}

export function tableAtIndex<S extends EditorStateV2<DocumentV2>>(state: S, index: number): TableV2 {
  const block = state.redigertBrev.blocks[index];
  if (block?.type === "TABLE") return block;
  throw new Error(`Block ${index} is not a table`);
}

export function texts(content: TextV2[]): string[] {
  return content.map((text) => (text.type === "LITERAL" ? (text.editedText ?? text.text) : text.text));
}
