import {
  type ContentOrControlStructure,
  ContentOrControlStructureType,
  type OutlineContent,
  type TemplateDocumentation,
} from "~/api/templateDocumentationTypes";

function outlineBlocks(items: ContentOrControlStructure<OutlineContent>[]): number[] {
  return items.flatMap((item) => {
    if (item.controlStructureType === ContentOrControlStructureType.CONTENT) {
      return [item.content.index];
    }
    if (item.controlStructureType === ContentOrControlStructureType.CONDITIONAL) {
      return [
        ...outlineBlocks(item.showIf),
        ...item.elseIf.flatMap((branch) => outlineBlocks(branch.showIf)),
        ...outlineBlocks(item.showElse),
      ];
    }
    return outlineBlocks(item.body);
  });
}

/** Every block index (titles and paragraphs) in a template and its
 *  attachments, to tell a block reference from the URL that no longer exists. */
export function blockIndexesOf(documentation: TemplateDocumentation): ReadonlySet<number> {
  return new Set([
    ...outlineBlocks(documentation.outline),
    ...documentation.attachments.flatMap((attachment) => outlineBlocks(attachment.outline)),
  ]);
}
