import React, { useLayoutEffect, useRef } from "react";

import { fontTypeOf, isUneditedFritekst, text as textOf } from "~/Brevredigering/LetterEditor/actions/common";
import { composingLiteralElementRef } from "~/Brevredigering/LetterEditor/components/EditableSurface";
import TableView from "~/Brevredigering/LetterEditor/components/TableView";
import { Text } from "~/Brevredigering/LetterEditor/components/Text";
import { useAttestantDiff, useDiffSegmentsForLiteral } from "~/Brevredigering/LetterEditor/diff/AttestantDiffContext";
import {
  DeletedContentAt,
  DeletedItemContentAt,
  DeletedItemsAt,
} from "~/Brevredigering/LetterEditor/diff/DeletedMarkup";
import {
  diffSegmentSignature,
  renderDiffSegments,
  renderPlainText,
} from "~/Brevredigering/LetterEditor/diff/DiffSegments";
import { diffKey } from "~/Brevredigering/LetterEditor/diff/diffModel";
import {
  isTekstValgHighlighted,
  useInsertedTekstValgHighlight,
} from "~/Brevredigering/LetterEditor/InsertedTekstValgHighlight";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import {
  type Content,
  type EditedDocument,
  FontType,
  ListType,
  type LiteralValue,
  TITLE_INDEX,
} from "~/types/brevbakerTypes";

import { effectiveListType, ZERO_WIDTH_SPACE } from "../model/utils";

const WORD_JOINER = "\u2060";
const NO_BREAK_PUNCTUATION = /^[.,;:!?'"()[\]{}°…«»%\u201D\u2019]/;

function startsWithPunctuation(content: Content): boolean {
  if (content.type !== "LITERAL") return false;
  return NO_BREAK_PUNCTUATION.test(textOf(content));
}

function getContent(letter: EditedDocument, literalIndex: LiteralIndex) {
  if (literalIndex.blockIndex === TITLE_INDEX) {
    return letter.title.text;
  }
  const content = letter.blocks[literalIndex.blockIndex].content;
  const contentValue = content[literalIndex.contentIndex];
  if ("itemIndex" in literalIndex && contentValue.type === "ITEM_LIST") {
    return contentValue.items[literalIndex.itemIndex].content;
  }
  return content;
}

export function ContentGroup({ literalIndex }: { literalIndex: LiteralIndex }) {
  const { editorState } = useEditor();
  const contents = getContent(editorState.redigertBrev, literalIndex);
  const isItemContainer = "itemIndex" in literalIndex;

  // A deleted node is keyed by its position among the surviving siblings.
  // Render it before the survivor at that index, or at the end when no such survivor exists.
  const deletedAt = (contentIndex: number, trailing = false) =>
    isItemContainer ? (
      <DeletedItemContentAt
        blockIndex={literalIndex.blockIndex}
        contentIndex={literalIndex.contentIndex}
        itemContentIndex={contentIndex}
        itemIndex={literalIndex.itemIndex}
        trailing={trailing}
      />
    ) : (
      <DeletedContentAt blockIndex={literalIndex.blockIndex} contentIndex={contentIndex} trailing={trailing} />
    );

  return (
    <>
      {contents.map((content, contentIndex) => {
        const needsWordJoiner = contentIndex > 0 && startsWithPunctuation(content);

        const rendered = (() => {
          switch (content.type) {
            case "LITERAL": {
              const updatedLiteralIndex =
                "itemIndex" in literalIndex
                  ? { ...literalIndex, itemContentIndex: contentIndex }
                  : { ...literalIndex, contentIndex: contentIndex };
              return (
                <>
                  {needsWordJoiner && (
                    <span contentEditable={false} data-word-joiner="">
                      {WORD_JOINER}
                    </span>
                  )}
                  <EditableText content={content} literalIndex={updatedLiteralIndex} />
                </>
              );
            }
            case "NEW_LINE":
            case "VARIABLE": {
              return (
                <Text
                  content={content}
                  literalIndex={{
                    blockIndex: literalIndex.blockIndex,
                    contentIndex: contentIndex,
                  }}
                />
              );
            }
            case "ITEM_LIST": {
              const ListTag = effectiveListType(content) === ListType.PUNKTLISTE ? "ul" : "ol";
              return (
                <ListTag>
                  {content.items.map((_item, itemIndex) => (
                    <React.Fragment key={itemIndex}>
                      <DeletedItemsAt
                        blockIndex={literalIndex.blockIndex}
                        contentIndex={contentIndex}
                        itemIndex={itemIndex}
                      />
                      <li>
                        <ContentGroup
                          literalIndex={{
                            blockIndex: literalIndex.blockIndex,
                            contentIndex: contentIndex,
                            itemIndex: itemIndex,
                          }}
                        />
                      </li>
                    </React.Fragment>
                  ))}
                  <DeletedItemsAt
                    blockIndex={literalIndex.blockIndex}
                    contentIndex={contentIndex}
                    itemIndex={content.items.length}
                    trailing
                  />
                </ListTag>
              );
            }
            case "TABLE": {
              return <TableView blockIndex={literalIndex.blockIndex} contentIndex={contentIndex} node={content} />;
            }
            default:
              return null;
          }
        })();

        return (
          <React.Fragment key={contentIndex}>
            {deletedAt(contentIndex)}
            {rendered}
          </React.Fragment>
        );
      })}
      {deletedAt(contents.length, true)}
    </>
  );
}

export function EditableText({ literalIndex, content }: { literalIndex: LiteralIndex; content: LiteralValue }) {
  const contentEditableReference = useRef<HTMLSpanElement>(null);
  const highlightedIds = useInsertedTekstValgHighlight();
  const isInserted = isTekstValgHighlighted(highlightedIds, content);

  const { diffHash } = useAttestantDiff();
  const rawText = textOf(content);
  const isPlaceholderEmpty = rawText === "" || rawText === ZERO_WIDTH_SPACE;
  const logicalText = isPlaceholderEmpty ? "" : rawText;
  const diffSegments = useDiffSegmentsForLiteral(literalIndex, logicalText);
  const hasDiffDecoration = diffSegments != null;
  const literalDiffKey = diffKey(literalIndex);
  const diffVersion =
    diffHash && diffSegments ? `${diffHash}:${literalDiffKey}:${diffSegmentSignature(diffSegments)}` : undefined;

  const erFritekst = isUneditedFritekst(content);
  const text = logicalText || ZERO_WIDTH_SPACE;

  useLayoutEffect(() => {
    const element = contentEditableReference.current;
    if (!element) return;
    if (composingLiteralElementRef.current === element) return;

    if (hasDiffDecoration && diffVersion) {
      renderDiffSegments(element, diffSegments, diffVersion);
      return;
    }

    if (diffHash !== undefined || element.dataset.diffVersion !== undefined) {
      renderPlainText(element, text);
    } else if (element.textContent !== text) {
      element.textContent = text;
    }
  }, [text, hasDiffDecoration, diffSegments, diffVersion, diffHash]);

  return (
    <span
      className={isInserted ? "inserted-flash-text" : undefined}
      css={{
        ...(erFritekst && {
          color: "var(--ax-text-accent-subtle)",
          backgroundColor: "var(--ax-bg-brand-blue-moderate)",
          borderRadius: "var(--ax-radius-2)",
          fontWeight: 600,
          padding: "0 var(--ax-space-2)",
          textDecoration: "underline",
          textDecorationThickness: "from-font",
          textUnderlinePosition: "from-font",
          cursor: "pointer",
        }),
        ...(fontTypeOf(content) === FontType.BOLD && { fontWeight: "bold" }),
        ...(fontTypeOf(content) === FontType.ITALIC && { fontStyle: "italic" }),
      }}
      data-empty={text === ZERO_WIDTH_SPACE ? "" : undefined}
      data-font-type={fontTypeOf(content) === FontType.PLAIN ? undefined : fontTypeOf(content)}
      data-fritekst={erFritekst ? "" : undefined}
      data-literal=""
      data-literal-index={JSON.stringify(literalIndex)}
      ref={contentEditableReference}
    />
  );
}
