import "./editor.css";

import { css } from "@emotion/react";
import { Heading } from "@navikt/ds-react";
import type { Dispatch, SetStateAction } from "react";
import { createContext, useContext, useEffect, useState } from "react";

import { DebugPanel } from "~/Brevredigering/LetterEditor/components/DebugPanel";
import { applyAction, type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import type { AnyBlock } from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";
import { handleSwitchContent, handleSwitchTextContent } from "~/utils/brevbakerUtils";

import Actions from "./actions";
import type { LiteralIndex } from "./actions/model";
import { ContentGroup } from "./components/ContentGroup";
import { EditorMenu } from "./components/EditorMenu";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import type { Focus, LetterEditorState } from "./model/state";
import { focusAtOffset, getCursorOffset } from "./services/caretUtils";
import { useEditorKeyboardShortcuts } from "./utils";

const onKeyDown = (e: React.KeyboardEvent<HTMLSpanElement>) => {
  if (e.key === "Backspace") {
    handleBackspace(e);
  }

  if (e.key === "Delete") {
    handleDelete(e);
  }
};

const getAdjacentNodes = (): {
  currentNode: Nullable<Node>;
  previousNode: Nullable<Node>;
  nextNode: Nullable<Node>;
} => {
  const selection = window.getSelection();
  if (!selection || !selection.anchorNode) {
    return { currentNode: null, previousNode: null, nextNode: null };
  }
  const spanElement = selection.anchorNode.parentElement as HTMLSpanElement | null;
  if (!spanElement) {
    return { currentNode: null, previousNode: null, nextNode: null };
  }

  const previousNode = spanElement.previousSibling;
  const nextNode = spanElement.nextSibling;

  return { currentNode: spanElement, previousNode, nextNode };
};

const handleDelete = (e: React.KeyboardEvent<HTMLSpanElement>) => {
  const cursorPosition = getCursorOffset();
  const { currentNode, nextNode } = getAdjacentNodes();

  if (!currentNode && !nextNode) {
    return;
  }

  const isProtected = (nextNode as HTMLElement).dataset.protected === "true";

  if (isProtected && cursorPosition === currentNode?.textContent?.length) {
    // Prevent deleting protected variable
    e.preventDefault();
  }
};

const handleBackspace = (e: React.KeyboardEvent<HTMLSpanElement>) => {
  const cursorPosition = getCursorOffset();
  const { previousNode } = getAdjacentNodes();

  if (!previousNode) {
    return;
  }

  const isProtected = (previousNode as HTMLElement).dataset.protected === "true";

  if (isProtected && cursorPosition === 0) {
    // Prevent deleting protected variable
    e.preventDefault();
  }
};

const onBeforeInput = (e: React.FormEvent<HTMLDivElement>, blocks: AnyBlock[]): LiteralIndex => {
  const selection = window.getSelection();
  if (!selection) return { blockIndex: 0, contentIndex: 0 };

  const selectedNode = selection.anchorNode?.textContent;
  if (!selectedNode) return { blockIndex: 0, contentIndex: 0 };

  const matchedLiteralToNode = blocks
    .flatMap((block, blockIndex) => {
      return block.content.map((content, contentIndex) => {
        return handleSwitchContent({
          content: content,
          onLiteral: (literal) => {
            //TODO bruk text i utils
            return {
              result: literal.editedText === selectedNode || literal.text === selectedNode,
              blockIndex,
              contentIndex,
            };
          },
          onVariable: () => {
            return {
              result: false,
              blockIndex,
              contentIndex,
            };
          },
          onItemList: (il) => {
            return (
              il.items
                .flatMap((item, itemIndex) => {
                  return item.content.map((content, itemContentIndex) => {
                    return handleSwitchTextContent({
                      content: content,
                      onLiteral: (literal) => {
                        return {
                          result: literal.editedText === selectedNode || literal.text === selectedNode,
                          blockIndex,
                          contentIndex,
                          itemIndex,
                          itemContentIndex,
                        };
                      },
                      onVariable: () => {
                        return {
                          result: false,
                          blockIndex,
                          contentIndex,
                          itemIndex,
                          itemContentIndex,
                        };
                      },
                    });
                  });
                })
                .find((l) => l.result) ?? { result: false, blockIndex: 0, contentIndex: 0 }
            );
          },
        });
      });
    })
    .filter((l) => l.result);

  if (matchedLiteralToNode.length === 1) {
    return matchedLiteralToNode[0];
  }

  return { blockIndex: 0, contentIndex: 0 };
};

const onInput = (e: React.FormEvent<HTMLDivElement>, onInputAction: (s: string) => void) => {
  const selection = window.getSelection();
  if (!selection) return { blockIndex: 0, contentIndex: 0 };

  const nodeText = selection.anchorNode?.textContent;
  if (!nodeText) return;

  onInputAction(nodeText);
};

function hasFocus(focus: Focus, literalIndex: LiteralIndex) {
  const basicMatch = focus.blockIndex === literalIndex.blockIndex && focus.contentIndex === literalIndex.contentIndex;
  if ("itemIndex" in literalIndex && "itemIndex" in focus) {
    const itemMatch =
      focus.itemIndex === literalIndex.itemIndex && focus.itemContentIndex === literalIndex.itemContentIndex;
    return itemMatch && basicMatch;
  }
  return basicMatch;
}

const getNodeBasedOnLiteralIndex = (literalIndex: LiteralIndex): ChildNode | undefined => {
  const container = document.querySelector('[contenteditable="true"]');
  const isItemContentIndex = "itemIndex" in literalIndex && "itemContentIndex" in literalIndex;

  if (isItemContentIndex) {
    const childNode =
      container?.childNodes[literalIndex.blockIndex].childNodes[literalIndex.contentIndex].childNodes[
        literalIndex.itemIndex
      ].childNodes[literalIndex.itemContentIndex];

    return childNode;
  } else {
    const firstChild = container?.childNodes[literalIndex.blockIndex].childNodes[0];

    const secondChild = firstChild?.childNodes[literalIndex.contentIndex];

    return secondChild;
  }
};

export const LetterEditor = ({
  freeze,
  error,
  editorState,
  setEditorState,
  editorHeight,
  showDebug,
}: {
  freeze: boolean;
  error: boolean;
  editorState: LetterEditorState;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  editorHeight?: string;
  showDebug: boolean;
}) => {
  const letter = editorState.redigertBrev;
  const blocks = letter.blocks;
  const editorKeyboardShortcuts = useEditorKeyboardShortcuts(editorState, setEditorState);
  const [literalBeingEdited, setLiteralBeingEdited] = useState<LiteralIndex>({ blockIndex: 0, contentIndex: 0 });

  const shouldBeFocused = hasFocus(editorState.focus, literalBeingEdited);

  useEffect(() => {
    const childNode = getNodeBasedOnLiteralIndex(literalBeingEdited);
    if (!freeze && shouldBeFocused && editorState.focus.cursorPosition !== undefined && childNode) {
      focusAtOffset(childNode, editorState.focus.cursorPosition);
    }
  }, [editorState.focus.cursorPosition, shouldBeFocused, freeze, literalBeingEdited]);

  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
        align-items: center;
        height: ${editorHeight ?? "auto"};
        overflow-y: auto;
      `}
    >
      <EditorStateContext.Provider value={{ freeze, error, editorState, setEditorState }}>
        <div
          css={css`
            position: sticky;
            top: 0;
            width: 100%;
            z-index: 1;
          `}
        >
          <EditorMenu />
        </div>
        <div className="editor">
          <SakspartView sakspart={letter.sakspart} />
          <Heading
            css={css`
              margin: var(--a-spacing-8) 0;
            `}
            level="1"
            size="large"
          >
            {letter.title}
          </Heading>
          <div
            // NOTE: ideally this would be "plaintext-only", and it works in practice.
            // However, the tests will not work if set to plaintext-only. For some reason focus/input and other events will not be triggered by userEvent as expected.
            // This is not documented anywhere I could find and caused a day of frustration, beware
            contentEditable={!freeze}
            onBeforeInput={(e) => setLiteralBeingEdited(() => onBeforeInput(e, blocks))}
            onFocus={() => {
              setEditorState((oldState) => ({
                ...oldState,
                focus: literalBeingEdited,
              }));
            }}
            onInput={(e) => {
              onInput(e, (s) => {
                applyAction(Actions.updateContentText, setEditorState, literalBeingEdited, s);
                setEditorState((oldState) => ({
                  ...oldState,
                  focus: literalBeingEdited,
                }));
              });
            }}
            onKeyDown={(e) => {
              onKeyDown(e);
              editorKeyboardShortcuts(e);
            }}
            suppressContentEditableWarning
          >
            {blocks.map((block, blockIndex) => (
              <div className={block.type} key={blockIndex}>
                <ContentGroup literalIndex={{ blockIndex, contentIndex: 0 }} />
              </div>
            ))}
          </div>
          <SignaturView signatur={letter.signatur} />
        </div>
        {showDebug && <DebugPanel />}
      </EditorStateContext.Provider>
    </div>
  );
};

export const EditorStateContext = createContext<{
  freeze: boolean;
  error: boolean;
  editorState: LetterEditorState;
  setEditorState: CallbackReceiver<LetterEditorState>;
}>({
  freeze: false,
  error: false,
  editorState: {} as LetterEditorState,
  setEditorState: () => {},
});
export const useEditor = () => {
  return useContext(EditorStateContext);
};
