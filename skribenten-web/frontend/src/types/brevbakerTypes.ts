export type LetterModelSpecification = {
  readonly types: ObjectTypeSpecifications;
  readonly letterModelTypeName: string;
};

export type ObjectTypeSpecifications = {
  readonly [name: string]: ObjectTypeSpecification;
};

export type ObjectTypeSpecification = {
  readonly [field: string]: FieldType;
};

export type FieldType = TScalar | TEnum | TArray | TObject;

export type TScalar = {
  readonly type: "scalar";
  readonly nullable: boolean;
  readonly kind: ScalarKind;
};
export type TEnum = {
  readonly type: "enum";
  readonly nullable: boolean;
  readonly values: string[];
};
export type TArray = {
  readonly type: "array";
  readonly nullable: boolean;
  readonly items: FieldType;
};
export type TObject = {
  readonly type: "object";
  readonly nullable: boolean;
  readonly typeName: string;
};

export type ScalarKind = "NUMBER" | "DOUBLE" | "STRING" | "BOOLEAN" | "DATE";

export type LanguageCode = "BOKMAL" | "NYNORSK" | "ENGLISH";

export type Sakspart = {
  readonly gjelderNavn: string;
  readonly gjelderFoedselsnummer: string;
  readonly saksnummer: string;
  readonly dokumentDato: string;
};

export type Signatur = {
  readonly hilsenTekst: string;
  readonly saksbehandlerRolleTekst: string;
  readonly saksbehandlerNavn: string;
  readonly attesterendeSaksbehandlerNavn?: string;
  readonly navAvsenderEnhet: string;
};

export type Identifiable = {
  readonly id: number | null;
  readonly parentId: number | null;
};

export enum ElementTags {
  FRITEKST = "FRITEKST",
}

export const LITERAL = "LITERAL";
export type LiteralValue = Identifiable & {
  readonly type: typeof LITERAL;
  readonly text: string;
  readonly editedText: string | null;
  readonly tags: ElementTags[];
};
export const VARIABLE = "VARIABLE";
export type VariableValue = Identifiable & {
  readonly type: typeof VARIABLE;
  readonly name?: string;
  readonly text: string;
};

export const ITEM_LIST = "ITEM_LIST";
export type ItemList = Identifiable & {
  readonly type: typeof ITEM_LIST;
  readonly items: Item[];
  readonly deletedItems: number[];
};
export type Item = Identifiable & {
  readonly content: TextContent[];
  readonly deletedContent: number[];
};

export const TABLE = "TABLE";
export type Table = Identifiable & {
  readonly type: typeof TABLE;
  readonly rows: Row[];
  readonly header: Header;
  readonly deletedRows: number[];
};
export type Row = Identifiable & {
  readonly cells: Cell[];
};
export type Cell = Identifiable & {
  readonly text: TextContent[];
};
export type Header = Identifiable & {
  readonly colSpec: ColumnSpec[];
};
export type ColumnSpec = Identifiable & {
  readonly headerContent: Cell;
  readonly alignment: ColumnAlignment;
  readonly span: number;
};
export type ColumnAlignment = "LEFT" | "RIGHT";

export type TextContent = LiteralValue | VariableValue;
export type Content = ItemList | TextContent;

export type Block = Identifiable & {
  readonly type: string;
  readonly locked?: boolean;
  readonly editable?: boolean;
  readonly deletedContent: number[];
  readonly originalType?: typeof PARAGRAPH | typeof TITLE1 | typeof TITLE2;
};

export const PARAGRAPH = "PARAGRAPH";
export type ParagraphBlock = Block & {
  readonly type: typeof PARAGRAPH;
  readonly content: Content[];
};

export const TITLE1 = "TITLE1";
export type Title1Block = Block & {
  readonly type: typeof TITLE1;
  readonly content: TextContent[];
};

export const TITLE2 = "TITLE2";
export type Title2Block = Block & {
  readonly type: typeof TITLE2;
  readonly content: TextContent[];
};

export type AnyBlock = Title1Block | Title2Block | ParagraphBlock;

export interface EditedLetter {
  readonly title: string;
  readonly sakspart: Sakspart;
  readonly blocks: AnyBlock[];
  readonly signatur: Signatur;
  readonly deletedBlocks: number[];
}
