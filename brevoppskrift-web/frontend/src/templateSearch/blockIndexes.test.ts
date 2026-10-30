import { describe, expect, it } from "vitest";

import {
  type ContentOrControlStructure,
  ContentOrControlStructureType,
  ElementType,
  type Expr,
  type OutlineContent,
  type TemplateDocumentation,
} from "~/api/templateDocumentationTypes";
import { blockIndexesOf } from "~/templateSearch/blockIndexes";

const predicate = {} as Expr;

function title(index: number): ContentOrControlStructure<OutlineContent> {
  return {
    controlStructureType: ContentOrControlStructureType.CONTENT,
    content: { elementType: ElementType.TITLE1, index, text: [] },
  };
}

function paragraph(index: number): ContentOrControlStructure<OutlineContent> {
  return {
    controlStructureType: ContentOrControlStructureType.CONTENT,
    content: { elementType: ElementType.PARAGRAPH, index, paragraph: [] },
  };
}

function documentation(
  outline: ContentOrControlStructure<OutlineContent>[],
  attachmentOutlines: ContentOrControlStructure<OutlineContent>[][] = [],
): TemplateDocumentation {
  return {
    title: [],
    outline,
    attachments: attachmentOutlines.map((attachmentOutline) => ({
      title: [],
      outline: attachmentOutline,
      include: predicate,
      attachmentData: predicate,
    })),
  } as unknown as TemplateDocumentation;
}

describe("blockIndexesOf", () => {
  it("collects titles and paragraphs in the outline", () => {
    expect(blockIndexesOf(documentation([title(0), paragraph(1)]))).toEqual(new Set([0, 1]));
  });

  it("collects blocks in every branch of a conditional and inside loops", () => {
    const outline: ContentOrControlStructure<OutlineContent>[] = [
      {
        controlStructureType: ContentOrControlStructureType.CONDITIONAL,
        predicate,
        showIf: [paragraph(0)],
        elseIf: [{ predicate, showIf: [paragraph(1)] }],
        showElse: [paragraph(2)],
      },
      {
        controlStructureType: ContentOrControlStructureType.FOR_EACH,
        items: predicate,
        body: [title(3), paragraph(4)],
      },
    ];

    expect(blockIndexesOf(documentation(outline))).toEqual(new Set([0, 1, 2, 3, 4]));
  });

  it("collects blocks in attachments", () => {
    expect(blockIndexesOf(documentation([paragraph(0)], [[title(1)], [paragraph(2)]]))).toEqual(new Set([0, 1, 2]));
  });
});
