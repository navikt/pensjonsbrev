import type { SerializedStyles } from "@emotion/react";
import { css } from "@emotion/react";

import { NonEditableText } from "~/Brevredigering/LetterEditor/components/NonEditableText";
import type { AnyBlock, Content, TextContent } from "~/types/brevbakerTypes";
import { handleSwitchBlock, handleSwitchContent, handleSwitchTextContent } from "~/utils/brevbakerUtils";

import type { LiteralIndex } from "../actions/model";
import { EditableText } from "./EditableText";

const sharedTitleStyle = css`
  line-height: var(--a-font-line-height-heading-xsmall);
  font-weight: var(--a-font-weight-bold);
  margin-bottom: var(--a-spacing-1-alt);
`;

export const EditableBlockComposer = (props: { blocks: AnyBlock[] }) =>
  props.blocks.map((block, blockIndex) =>
    handleSwitchBlock({
      block: block,
      onTitle1: (title1) => (
        <TextContentComposer
          blockIndex={blockIndex}
          key={`block-${blockIndex}`}
          textContent={title1.content}
          textStyle={css`
            font-size: 19.5px;
            letter-spacing: 0.375px;
            ${sharedTitleStyle}
          `}
        />
      ),
      onTitle2: (title2) => (
        <TextContentComposer
          blockIndex={blockIndex}
          key={`block-${blockIndex}`}
          textContent={title2.content}
          textStyle={css`
            font-size: 18px;
            letter-spacing: 0.3px;
            ${sharedTitleStyle}
          `}
        />
      ),
      onParagraph: (paragraph) => (
        <ContentComposer blockIndex={blockIndex} content={paragraph.content} key={`block-${blockIndex}`} />
      ),
    }),
  );

const ContentComposer = (props: { blockIndex: number; content: Content[] }) => (
  <div
    css={css`
      font-size: 16.5px;
      line-height: var(--a-font-line-height-heading-xsmall);
      margin-bottom: 27px;
    `}
  >
    {props.content.map((content, contentIndex) =>
      handleSwitchContent({
        content,
        onLiteral: (literal) => (
          <EditableText
            content={literal}
            key={`block-${props.blockIndex}-content-${contentIndex}`}
            literalIndex={{ blockIndex: props.blockIndex, contentIndex: contentIndex }}
          />
        ),
        onVariable: (variable) => (
          <NonEditableText
            content={variable}
            key={`block-${props.blockIndex}-content-${contentIndex}`}
            literalIndex={{ blockIndex: props.blockIndex, contentIndex: contentIndex }}
          />
        ),
        onNewLine: (newLine) => (
          <NonEditableText
            content={newLine}
            key={`block-${props.blockIndex}-content-${contentIndex}`}
            literalIndex={{ blockIndex: props.blockIndex, contentIndex: contentIndex }}
          />
        ),
        onItemList: (itemList) => (
          <ul key={`block-${props.blockIndex}-content-${contentIndex}-ul`}>
            {itemList.items.map((item, itemIndex) => (
              <li key={`block-${props.blockIndex}-content-${contentIndex}-item-${itemIndex}`}>
                <TextContentComposer
                  blockIndex={props.blockIndex}
                  existingContentIndex={contentIndex}
                  itemIndex={itemIndex}
                  textContent={item.content}
                />
              </li>
            ))}
          </ul>
        ),
      }),
    )}
  </div>
);

const TextContentComposer = (props: {
  textContent: TextContent[];
  blockIndex: number;
  existingContentIndex?: number;
  itemIndex?: number;
  textStyle?: SerializedStyles;
}) => (
  <div css={props.textStyle}>
    {props.textContent.map((content, contentIndex) => {
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

      return handleSwitchTextContent({
        content: content,
        onLiteral: (literal) => (
          <EditableText
            content={literal}
            key={`block-${props.blockIndex}-content-${contentIndex}-item-${props.itemIndex}-itemContent-${contentIndex}`}
            literalIndex={updatedLiteralIndex}
          />
        ),
        onVariable: (variable) => (
          <NonEditableText
            content={variable}
            key={`block-${props.blockIndex}-content-${contentIndex}-item-${props.itemIndex}-itemContent-${contentIndex}`}
            literalIndex={updatedLiteralIndex}
          />
        ),
        onNewLine: (newLine) => (
          <NonEditableText
            content={newLine}
            key={`block-${props.blockIndex}-content-${contentIndex}-item-${props.itemIndex}-itemContent-${contentIndex}`}
            literalIndex={updatedLiteralIndex}
          />
        ),
      });
    })}
  </div>
);
