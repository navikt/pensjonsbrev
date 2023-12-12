import type { MouseEventHandler } from "react";
import React from "react";

import type { Content, TextContent } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

import Actions from "../actions";
import type { ContentId } from "../actions/model";
import { MergeTarget } from "../actions/model";
import type { BoundAction, CallbackReceiver } from "../lib/actions";
import { applyAction, bindActionWithCallback, combine } from "../lib/actions";
import type { CursorPosition, LetterEditorState } from "../model/state";
import { isTextContent } from "../model/utils";
import { SelectionService } from "../services/SelectionService";
import styles from "./Content.module.css";
import { EditableText } from "./EditableText";
import { ItemList } from "./ItemList";
import { Text } from "./Text";

const selectService = new SelectionService(true);

type ContentGroupState = never;

export type BlockID = { blockId: number };
export type ItemID = { blockId: number; contentId: number; itemId: number };

function toContentId(id: BlockID | ItemID, contentId: number): ContentId {
  return "itemId" in id ? { ...id, itemContentId: contentId } : { ...id, contentId: contentId };
}

export type ContentGroupProperties<T> = {
  id: BlockID | ItemID;
  updateLetter: CallbackReceiver<LetterEditorState>;
  content: (T & Content)[];
  editable: boolean | undefined;
  stealFocus: CursorPosition | undefined;
  focusStolen: BoundAction<[]>;
  onFocus: BoundAction<[]>;
};

type ContentReferences = { [nodeId: number]: HTMLElement | undefined | null };

export class ContentGroup<T extends Content | TextContent> extends React.Component<
  ContentGroupProperties<T>,
  ContentGroupState
> {
  private childRefs: ContentReferences = {};

  constructor(properties: ContentGroupProperties<T>) {
    super(properties);
  }

  setChildRef(contentId: number, node: HTMLElement | null) {
    const previous = this.childRefs[contentId];

    if (previous) {
      // eslint-disable-next-line unicorn/prefer-add-event-listener
      previous.onkeydown = null;
    }

    if (node != null) {
      let keyHandler = this.enterHandler.bind(this, contentId);

      // Første node skal ha backspaceHandler og siste node skal ha deleteHandler. Dette kan være samme node.
      if (contentId === 0) {
        keyHandler = combine(keyHandler, this.backspaceHandler.bind(this, 0));
      }
      if (contentId === this.props.content.length - 1) {
        keyHandler = combine(keyHandler, this.deleteHandler);
      }
      if (this.props.content[contentId - 1]?.type === ITEM_LIST) {
        keyHandler = combine(keyHandler, this.backspaceHandler.bind(this, contentId));
      }

      node.addEventListener("keydown", keyHandler);

      if (this.props.stealFocus && this.props.stealFocus.contentId === contentId) {
        if (node.childNodes.length === 0) {
          selectService.focusStartOfNode(node, this.props.focusStolen);
        } else {
          selectService.focusAtOffset(node.childNodes[0], this.props.stealFocus.startOffset, this.props.focusStolen);
        }
      }
    }

    this.childRefs[contentId] = node;
  }

  componentDidUpdate() {
    const span = this.props.stealFocus == null ? null : this.childRefs[this.props.stealFocus.contentId];
    if (this.props.stealFocus && span) {
      if (span.childNodes.length === 0) {
        selectService.focusStartOfNode(span, this.props.focusStolen);
      } else {
        selectService.focusAtOffset(span.childNodes[0], this.props.stealFocus.startOffset, this.props.focusStolen);
      }
    }
  }

  stealFocusHandler: MouseEventHandler<HTMLDivElement> = (event) => {
    if (event.target === event.currentTarget) {
      const nodes = Object.entries(this.childRefs)
        .map((element) => element[1])
        .filter((n): n is HTMLElement => n != null);

      if (nodes.length > 0) {
        selectService.focusEndOfClickedLine(nodes, { x: event.clientX, y: event.clientY }, this.props.focusStolen);
      } else {
        // eslint-disable-next-line no-console
        console.warn("Cannot steal focus for click because we don't have any childRefs.");
      }
    }
  };

  enterHandler = (contentId: number, event: KeyboardEvent) => {
    if (event.key === "Enter") {
      event.preventDefault();
      const offset = selectService.getCursorOffset();

      applyAction(Actions.split, this.props.updateLetter, toContentId(this.props.id, contentId), offset);
    }
  };

  backspaceHandler = (contentId: number, event: KeyboardEvent) => {
    if (event.key === "Backspace") {
      const span = this.childRefs[contentId];
      const cursorPosition = selectService.getCursorOffset();
      // If the cursor is at the beginning of the content (while we ignore any ZWSP)
      if (cursorPosition === 0 || (span?.textContent?.startsWith("​") && cursorPosition === 1)) {
        event.preventDefault();
        applyAction(
          Actions.merge,
          this.props.updateLetter,
          toContentId(this.props.id, contentId),
          MergeTarget.PREVIOUS,
        );
      }
    }
  };

  deleteHandler = (event: KeyboardEvent) => {
    if (event.key === "Delete") {
      const lastContentId = this.props.content.length - 1;
      const lastContent = this.props.content[lastContentId];
      if (isTextContent(lastContent) && selectService.getCursorOffset() >= lastContent.text.length) {
        event.preventDefault();
        applyAction(
          Actions.merge,
          this.props.updateLetter,
          toContentId(this.props.id, lastContentId),
          MergeTarget.NEXT,
        );
      }
    }
  };

  render() {
    const { id, content, editable, onFocus, updateLetter, focusStolen, stealFocus } = this.props;
    return editable ? (
      <div className={styles.content} onClick={this.stealFocusHandler} onFocus={onFocus}>
        {content.map((c, contentId) => {
          switch (c.type) {
            case LITERAL: {
              return (
                <EditableText
                  content={c}
                  innerRef={this.setChildRef.bind(this, contentId)}
                  key={contentId}
                  updateText={bindActionWithCallback(
                    Actions.updateContentText,
                    updateLetter,
                    toContentId(id, contentId),
                  )}
                />
              );
            }
            case VARIABLE: {
              return <Text content={c} key={contentId} />;
            }
            case ITEM_LIST: {
              return (
                <ItemList
                  editable={editable}
                  focusStolen={focusStolen}
                  id={{ ...id, contentId }}
                  itemList={c}
                  key={contentId}
                  onFocus={onFocus}
                  stealFocus={stealFocus?.contentId === contentId ? stealFocus : undefined}
                  updateLetter={updateLetter}
                />
              );
            }
          }
        })}
      </div>
    ) : (
      <div className={styles.notEditable}>
        {content.map((c, contentId) => {
          switch (c.type) {
            case LITERAL:
            case VARIABLE: {
              return <Text content={c} key={contentId} />;
            }
            case ITEM_LIST: {
              return (
                <ItemList
                  editable={false}
                  focusStolen={focusStolen}
                  id={{ ...id, contentId }}
                  itemList={c}
                  key={contentId}
                  onFocus={onFocus}
                  updateLetter={updateLetter}
                />
              );
            }
          }
        })}
      </div>
    );
  }
}
