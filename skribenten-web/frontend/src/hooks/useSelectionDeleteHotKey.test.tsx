import { fireEvent, render } from "@testing-library/react";
import { useState } from "react";
import { afterEach, describe, expect, test, vi } from "vitest";

import { useSelectionDeleteHotkey } from "./useSelectionDeleteHotKey";

function Editor({
  onDelete,
  onDiffEditIntent,
  decorated,
}: {
  onDelete: () => void;
  onDiffEditIntent: () => void;
  decorated: boolean;
}) {
  const [root, setRoot] = useState<HTMLDivElement | null>(null);
  useSelectionDeleteHotkey(root, onDelete, true, onDiffEditIntent);
  return (
    <div data-editor-root ref={setRoot}>
      <div className="PARAGRAPH">
        <span contentEditable data-literal-index='{"blockIndex":0,"contentIndex":0}' suppressContentEditableWarning>
          Første del{" "}
        </span>
        <span
          contentEditable
          data-diff-version={decorated ? "1" : undefined}
          data-literal-index='{"blockIndex":0,"contentIndex":1}'
          suppressContentEditableWarning
        >
          andre del
        </span>
      </div>
    </div>
  );
}

function setup(decorated: boolean) {
  const onDelete = vi.fn();
  const onDiffEditIntent = vi.fn();
  const { container } = render(
    <Editor decorated={decorated} onDelete={onDelete} onDiffEditIntent={onDiffEditIntent} />,
  );
  const [first, second] = container.querySelectorAll<HTMLSpanElement>("[data-literal-index]");

  const range = document.createRange();
  range.setStart(first.firstChild!, 7);
  range.setEnd(second.firstChild!, 5);
  globalThis.getSelection()?.removeAllRanges();
  globalThis.getSelection()?.addRange(range);

  return { first, onDelete, onDiffEditIntent };
}

afterEach(() => {
  globalThis.getSelection()?.removeAllRanges();
});

describe("useSelectionDeleteHotkey i diff-modus", () => {
  test.each(["x", "Enter", "Backspace", "Delete"])(
    "%s over en markering som berører diff-markering slår av diff-modus uten å slette",
    (key) => {
      const { first, onDelete, onDiffEditIntent } = setup(true);

      const notPrevented = fireEvent.keyDown(first, { key });

      expect(notPrevented).toBe(false);
      expect(onDiffEditIntent).toHaveBeenCalledTimes(1);
      expect(onDelete).not.toHaveBeenCalled();
    },
  );

  test.each(["x", "Backspace"])("%s over en markering uten diff-markering sletter markeringen", (key) => {
    const { first, onDelete, onDiffEditIntent } = setup(false);

    fireEvent.keyDown(first, { key });

    expect(onDelete).toHaveBeenCalledTimes(1);
    expect(onDiffEditIntent).not.toHaveBeenCalled();
  });
});
