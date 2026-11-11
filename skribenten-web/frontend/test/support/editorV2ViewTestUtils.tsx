import { render } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { type ReactNode, useCallback, useMemo, useState } from "react";
import { vi } from "vitest";

import { type ContainerPath } from "~/Brevredigering/EditorV2/model/paths";
import { apply, type EditorStateV2 } from "~/Brevredigering/EditorV2/state/editorState";
import { BlockView } from "~/Brevredigering/EditorV2/view/BlockView";
import { type Dispatch, EditorContextV2, useEditorV2 } from "~/Brevredigering/EditorV2/view/EditorContextV2";
import { RedigeringsflateProvider } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { useDragSelectUnifier } from "~/hooks/useDragSelectUnifier";

function Surface() {
  const { state } = useEditorV2();
  const [root, setRoot] = useState<HTMLDivElement | null>(null);
  useDragSelectUnifier(root);
  return (
    <RedigeringsflateProvider redigeringsflate="saksbehandler-redigering">
      <div data-editor-root ref={setRoot}>
        {state.redigertBrev.blocks.map((block, index) => (
          <BlockView block={block} index={index} key={index} />
        ))}
      </div>
    </RedigeringsflateProvider>
  );
}

export function renderWithDispatchSpy(state: EditorStateV2, children: ReactNode = <Surface />) {
  const spy = vi.fn();
  const dispatch: Dispatch = (command, ...args) => spy(command, ...args);
  const result = render(
    <EditorContextV2.Provider value={{ freeze: false, error: false, state, dispatch, undo: vi.fn(), redo: vi.fn() }}>
      {children}
    </EditorContextV2.Provider>,
  );
  return { user: userEvent.setup(), dispatch: spy, ...result };
}

function LiveEditor({ initial, onChange }: { initial: EditorStateV2; onChange: (state: EditorStateV2) => void }) {
  const [state, setState] = useState(initial);
  onChange(state);
  const dispatch = useCallback<Dispatch>((command, ...args) => apply(setState, command, ...args), []);
  const value = useMemo(
    () => ({ freeze: false, error: false, state, dispatch, undo: vi.fn(), redo: vi.fn() }),
    [state, dispatch],
  );
  return (
    <EditorContextV2.Provider value={value}>
      <Surface />
    </EditorContextV2.Provider>
  );
}

export function renderLive(initial: EditorStateV2, wrap: (editor: ReactNode) => ReactNode = (editor) => editor) {
  let current = initial;
  const result = render(
    wrap(
      <LiveEditor
        initial={initial}
        onChange={(state) => {
          current = state;
        }}
      />,
    ),
  );
  return { user: userEvent.setup(), state: () => current, ...result };
}

export function at(path: ContainerPath, content: number, offset: number) {
  return { path, content, offset };
}

export function block(index: number): ContainerPath {
  return { kind: "block", block: index };
}

export function selectText(element: HTMLElement, start: number, end = start) {
  const node = element.firstChild;
  if (!node) throw new Error("Expected text node");
  const range = document.createRange();
  range.setStart(node, start);
  range.setEnd(node, end);
  const selection = globalThis.getSelection();
  selection?.removeAllRanges();
  selection?.addRange(range);
}

export function domCaret(): { text: string | null | undefined; offset: number | undefined } {
  const selection = globalThis.getSelection();
  return { text: selection?.anchorNode?.textContent, offset: selection?.anchorOffset };
}
