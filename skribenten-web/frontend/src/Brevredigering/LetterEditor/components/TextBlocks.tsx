import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";

import { Text } from "~/Brevredigering/LetterEditor/components/Text";
import type { AnyBlock, Content, TextContent } from "~/types/brevbakerTypes";

import type { LiteralIndex } from "../actions/model";
import { EditableText } from "./EditableText";

export const EditableBlockComposer = (props: { blocks: AnyBlock[] }) => {
  return (
    <>
      {props.blocks.map((block, blockIndex) => {
        switch (block.type) {
          case "TITLE1": {
            return (
              <TextContentComposer
                blockIndex={blockIndex}
                dataCy="TITLE1"
                key={`block-${blockIndex}`}
                textContent={block.content}
                textStyle={css`
                  font-size: 19.5px;
                  line-height: var(--a-font-line-height-heading-xsmall);
                  letter-spacing: 0.375px;
                  font-weight: var(--a-font-weight-bold);
                  margin-bottom: var(--a-spacing-1-alt);
                `}
              />
            );
          }
          case "TITLE2": {
            return (
              <TextContentComposer
                blockIndex={blockIndex}
                dataCy="TITLE2"
                key={`block-${blockIndex}`}
                textContent={block.content}
                textStyle={css`
                  font-size: 18px;
                  line-height: var(--a-font-line-height-heading-xsmall);
                  letter-spacing: 0.3px;
                  font-weight: var(--a-font-weight-bold);
                  margin-bottom: var(--a-spacing-1-alt);
                `}
              />
            );
          }
          case "PARAGRAPH": {
            return (
              <ContentComposer
                blockIndex={blockIndex}
                content={block.content}
                dataCy="PARAGRAPH"
                key={`block-${blockIndex}`}
              />
            );
          }
        }
      })}
    </>
  );
};

const ContentComposer = (props: { blockIndex: number; content: Content[]; dataCy?: string }) => {
  return (
    <div
      css={css`
        font-size: 16.5px;
        line-height: var(--a-font-line-height-heading-xsmall);
        margin-bottom: 27px;
      `}
      data-cy={props.dataCy ?? "PARAGRAPH"}
    >
      {props.content.map((content, contentIndex) => {
        switch (content.type) {
          case "ITEM_LIST": {
            return (
              <ul key={`block-${props.blockIndex}-content-${contentIndex}-ul`}>
                {content.items.map((item, itemIndex) => {
                  return (
                    <li key={`block-${props.blockIndex}-content-${contentIndex}-item-${itemIndex}`}>
                      <TextContentComposer
                        blockIndex={props.blockIndex}
                        existingContentIndex={contentIndex}
                        itemIndex={itemIndex}
                        textContent={item.content}
                      />
                    </li>
                  );
                })}
              </ul>
            );
          }
          case "LITERAL": {
            return (
              <EditableText
                content={content}
                key={`block-${props.blockIndex}-content-${contentIndex}`}
                literalIndex={{ blockIndex: props.blockIndex, contentIndex: contentIndex }}
              />
            );
          }
          case "VARIABLE":
          case "NEW_LINE": {
            return <Text content={content} key={`block-${props.blockIndex}-content-${contentIndex}`} />;
          }
        }
      })}
    </div>
  );
};

const TextContentComposer = (props: {
  textContent: TextContent[];
  blockIndex: number;
  existingContentIndex?: number;
  itemIndex?: number;
  textStyle?: SerializedStyles;
  dataCy?: string;
}) => {
  return (
    <div css={props.textStyle} data-cy={props.dataCy}>
      {props.textContent.map((content, contentIndex) => {
        switch (content.type) {
          case "LITERAL": {
            const updatedLiteralIndex: LiteralIndex =
              props.itemIndex === undefined
                ? { blockIndex: props.blockIndex, contentIndex: contentIndex }
                : {
                    blockIndex: props.blockIndex,
                    //skal ha en eksisterende contentIndex dersom vi er i en item-liste
                    contentIndex: props.existingContentIndex!,
                    itemIndex: props.itemIndex,
                    itemContentIndex: contentIndex,
                  };

            return (
              <EditableText
                content={content}
                key={`block-${props.blockIndex}-content-${contentIndex}-item-${props.itemIndex}-itemContent-${contentIndex}`}
                literalIndex={updatedLiteralIndex}
              />
            );
          }
          case "VARIABLE":
          case "NEW_LINE": {
            return (
              <Text
                content={content}
                key={`block-${props.blockIndex}-content-${contentIndex}-item-${props.itemIndex}-itemContent-${contentIndex}`}
              />
            );
          }
        }
      })}
    </div>
  );
};
