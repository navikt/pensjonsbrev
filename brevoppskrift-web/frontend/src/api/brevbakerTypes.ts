export type TemplateDocumentation = {
  title: ContentOrControlStructure<Text>[];
  outline: ContentOrControlStructure<OutlineContent>[];
  attachments: Attachment[];
};

export type Attachment = {
  title: ContentOrControlStructure<Text>[];
  outline: ContentOrControlStructure<OutlineContent>[];
  include: Expression;
  attachmentData: Expression;
};
export enum ElementType {
  TITLE1 = "TITLE1",
  TITLE2 = "TITLE2",
  PARAGRAPH = "PARAGRAPH",
  PARAGRAPH_TEXT_LITERAL = "PARAGRAPH_TEXT_LITERAL",
  PARAGRAPH_TEXT_EXPRESSION = "PARAGRAPH_TEXT_EXPRESSION",
  PARAGRAPH_ITEMLIST = "PARAGRAPH_ITEMLIST",
  PARAGRAPH_ITEMLIST_ITEM = "PARAGRAPH_ITEMLIST_ITEM",
  PARAGRAPH_TABLE = "PARAGRAPH_TABLE",
  PARAGRAPH_TABLE_ROW = "PARAGRAPH_TABLE_ROW",
}

export type Element = OutlineContent | ParagraphContent;
export type OutlineContent = Title1 | Title2 | Paragraph;
export type Title1 = {
  elementType: ElementType.TITLE1;
  text: ContentOrControlStructure<Text>[];
};
export type Title2 = {
  elementType: ElementType.TITLE2;
  text: ContentOrControlStructure<Text>[];
};
export type Paragraph = {
  elementType: ElementType.PARAGRAPH;
  paragraph: ContentOrControlStructure<ParagraphContent>[];
};

export type ParagraphContent = Text | ItemList | Table | Item;
export type Text = TextLiteral | TextExpression;
export type TextLiteral = { text: string; elementType: ElementType.PARAGRAPH_TEXT_LITERAL };
export type TextExpression = { expression: Expression; elementType: ElementType.PARAGRAPH_TEXT_EXPRESSION };
export type ItemList = {
  elementType: ElementType.PARAGRAPH_ITEMLIST;
  items: ContentOrControlStructure<Item>[];
};
export type Item = {
  elementType: ElementType.PARAGRAPH_ITEMLIST_ITEM;
  text: ContentOrControlStructure<Text>[];
};
export type Table = {
  header: Row;
  rows: ContentOrControlStructure<Row>[];
  elementType: ElementType.PARAGRAPH_TABLE;
};
export type Row = {
  cells: Cell[];
  elementType: ElementType.PARAGRAPH_TABLE_ROW;
};
export type Cell = {
  text: ContentOrControlStructure<Text>[];
};

export type ContentOrControlStructure<E> = Content<E> | Conditional<E> | ForEach<E>;

export type Content<E> = {
  content: E;
  controlStructureType: ContentOrControlStructureType.CONTENT;
};
export type Conditional<E> = {
  controlStructureType: ContentOrControlStructureType.CONDITIONAL;
  predicate: Expression;
  showIf: ContentOrControlStructure<E>[];
  showElse: ContentOrControlStructure<E>[];
};
export type ForEach<E> = {
  controlStructureType: ContentOrControlStructureType.FOR_EACH;
  items: Expression;
  body: ContentOrControlStructure<E>[];
};

export enum ContentOrControlStructureType {
  CONTENT = "CONTENT",
  CONDITIONAL = "CONDITIONAL",
  FOR_EACH = "FOR_EACH",
}

export type Expression = Literal | LetterData | Invoke;
export type Literal = { value: string };
export type LetterData = { scopeName: string };
export type Invoke = {
  operator: Operation;
  first: Expression;
  second?: Expression;
  type?: string;
};
export type Operation = { text: string; syntax: Notation };
export type Notation = "PREFIX" | "INFIX" | "POSTFIX" | "FUNCTION";
