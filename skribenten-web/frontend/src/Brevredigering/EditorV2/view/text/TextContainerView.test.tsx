// @vitest-environment happy-dom

import { fireEvent, screen } from "@testing-library/react";
import { afterEach, describe, expect, test } from "vitest";

import {
  cell,
  column,
  fritekst,
  item,
  list,
  literal,
  paragraph,
  row,
  stateV2,
  table,
  textBlockAt,
  texts,
  title2,
  variable,
} from "~test/support/editorV2TestUtils";
import { at, block, domCaret, renderLive, renderWithDispatchSpy } from "~test/support/editorV2ViewTestUtils";

import { addNewLine } from "../../commands/addNewLine";
import { merge } from "../../commands/merge";
import { split } from "../../commands/split";
import { updateText } from "../../commands/updateText";

const simpleState = () =>
  stateV2(
    paragraph(literal("Heisann"), literal("Velkommen")),
    paragraph(literal("Heisann"), literal("Velkommen")),
    paragraph(literal("Heisann"), literal("Velkommen")),
  );

const firstOf = (text: string) => screen.getAllByText(text)[0];

const complexState = () =>
  stateV2(
    paragraph(
      variable("Dokumentet starter med variable"),
      literal("første literal"),
      variable("X"),
      fritekst("andre literal"),
    ),
    paragraph(variable("Paragraf med kun variable")),
    paragraph(literal("paragraf med kun tekst")),
    list(
      item(literal("1. item")),
      item(variable("2. item variable")),
      item(literal("3. item"), variable("med variable")),
      item(literal("nth item")),
    ),
    paragraph(literal("Dokumentet avsluttes med literal")),
  );

afterEach(() => {
  globalThis.getSelection()?.removeAllRanges();
});

describe("updateText", () => {
  test("text changes are propagated", async () => {
    const { user, dispatch } = renderWithDispatchSpy(simpleState());
    await user.click(firstOf("Heisann"));
    await user.keyboard("{End} person");

    expect(dispatch).toHaveBeenLastCalledWith(updateText, at(block(0), 0, 14), "Heisann person");
  });

  test("enter is not propagated as br-element", async () => {
    const { user, dispatch } = renderWithDispatchSpy(simpleState());
    await user.click(firstOf("Heisann"));
    await user.keyboard("{End}{Enter}asd");

    expect(dispatch).toHaveBeenLastCalledWith(updateText, at(block(0), 0, 10), "Heisannasd");
  });

  test("space is not propagated as nbsp-entity", async () => {
    const { user, dispatch } = renderWithDispatchSpy(simpleState());
    await user.click(firstOf("Heisann"));
    await user.keyboard("{End}  asd");

    expect(dispatch).toHaveBeenLastCalledWith(updateText, at(block(0), 0, 12), "Heisann  asd");
  });
});

describe("delete", () => {
  test("delete after the last character of a container triggers merge forward", async () => {
    const { user, dispatch } = renderWithDispatchSpy(simpleState());
    await user.click(firstOf("Velkommen"));
    await user.keyboard("{End}{Delete}");

    expect(dispatch).toHaveBeenLastCalledWith(merge, at(block(0), 1, 9), "forward");
  });

  test("delete inside the last literal does not trigger merge", async () => {
    const { user, dispatch } = renderWithDispatchSpy(simpleState());
    await user.click(firstOf("Velkommen"));
    await user.keyboard("{End}{ArrowLeft}{ArrowLeft}{Delete}");

    expect(dispatch).not.toHaveBeenCalledWith(merge, expect.anything(), expect.anything());
  });

  test("delete after the last character of a literal that is not last in the container does not merge containers", async () => {
    const { user, state } = renderLive(simpleState());
    await user.click(firstOf("Heisann"));
    await user.keyboard("{End}{Delete}");

    expect(state().redigertBrev.blocks).toHaveLength(3);
    expect(texts(textBlockAt(state(), 0).content).join("")).toBe("HeisannVelkommen");
  });
});

describe("backspace", () => {
  test("backspace at the start of a container triggers merge backward", async () => {
    const { user, dispatch } = renderWithDispatchSpy(simpleState());
    await user.click(firstOf("Heisann"));
    await user.keyboard("{Home}{Backspace}");

    expect(dispatch).toHaveBeenLastCalledWith(merge, at(block(0), 0, 0), "backward");
  });

  test("backspace inside a literal does not trigger merge", async () => {
    const { user, dispatch } = renderWithDispatchSpy(simpleState());
    await user.click(firstOf("Heisann"));
    await user.keyboard("{End}{ArrowLeft}{ArrowLeft}{Backspace}");

    expect(dispatch).not.toHaveBeenCalledWith(merge, expect.anything(), expect.anything());
  });

  test("backspace before the first character of a literal that is not first in the container does not merge containers", async () => {
    const { user, state } = renderLive(simpleState());
    await user.click(screen.getAllByText("Velkommen")[1]);
    await user.keyboard("{Home}{Backspace}");

    expect(state().redigertBrev.blocks).toHaveLength(3);
    expect(texts(textBlockAt(state(), 0).content).join("")).toBe("HeisannVelkommen");
  });
});

describe("enter", () => {
  test("enter at the very end of a container triggers split at the end", async () => {
    const { user, dispatch } = renderWithDispatchSpy(simpleState());
    await user.click(firstOf("Velkommen"));
    await user.keyboard("{End}{Enter}");

    expect(dispatch).toHaveBeenLastCalledWith(split, at(block(0), 1, 9));
  });

  test("enter inside a literal triggers split at the caret", async () => {
    const { user, dispatch } = renderWithDispatchSpy(simpleState());
    await user.click(firstOf("Velkommen"));
    await user.keyboard("{End}{ArrowLeft}{ArrowLeft}{ArrowLeft}{Enter}");

    expect(dispatch).toHaveBeenLastCalledWith(split, at(block(0), 1, 6));
  });

  test("enter at the end of a literal that is not last in the container triggers split at that literal", async () => {
    const { user, dispatch } = renderWithDispatchSpy(simpleState());
    await user.click(firstOf("Heisann"));
    await user.keyboard("{End}{Enter}");

    expect(dispatch).toHaveBeenLastCalledWith(split, at(block(0), 0, 7));
  });

  test("shift-enter adds a new line instead of a new paragraph", async () => {
    const { user, dispatch } = renderWithDispatchSpy(simpleState());
    await user.click(firstOf("Heisann"));
    await user.keyboard("{End}{Shift>}{Enter}{/Shift}");

    expect(dispatch).toHaveBeenLastCalledWith(addNewLine, at(block(0), 0, 7));
  });
});

describe("ArrowLeft moves the caret to the previous editable literal", () => {
  test("unless already at the start of the document", async () => {
    const { user } = renderLive(complexState());
    await user.click(screen.getByText("første literal"));
    await user.keyboard("{Home}{ArrowLeft}");

    expect(domCaret()).toEqual({ text: "første literal", offset: 0 });
  });

  test("skipping variables in the same container", async () => {
    const { user } = renderLive(complexState());
    await user.click(screen.getByText("andre literal"));
    await user.keyboard("{Home}{ArrowLeft}");

    expect(domCaret()).toEqual({ text: "første literal", offset: 13 });
  });

  test("skipping a block without editable content", async () => {
    const { user } = renderLive(complexState());
    await user.click(screen.getByText("paragraf med kun tekst"));
    await user.keyboard("{Home}{ArrowLeft}");

    expect(domCaret().text).toBe("andre literal");
  });

  test("skipping a list item without editable content", async () => {
    const { user } = renderLive(complexState());
    await user.click(screen.getByText("3. item"));
    await user.keyboard("{Home}{ArrowLeft}");

    expect(domCaret()).toEqual({ text: "1. item", offset: 7 });
  });

  test("requiring two presses when a fritekst is selected", async () => {
    const { user } = renderLive(complexState());
    await user.click(screen.getByText("andre literal"));

    await user.keyboard("{ArrowLeft}");
    expect(domCaret().text).toBe("andre literal");

    await user.keyboard("{ArrowLeft}");
    expect(domCaret().text).toBe("første literal");
  });
});

describe("ArrowRight moves the caret to the next editable literal", () => {
  test("unless already at the end of the document", async () => {
    const { user } = renderLive(complexState());
    await user.click(screen.getByText("Dokumentet avsluttes med literal"));
    await user.keyboard("{End}{ArrowRight}");

    expect(domCaret()).toEqual({ text: "Dokumentet avsluttes med literal", offset: 32 });
  });

  test("skipping variables in the same container", async () => {
    const { user } = renderLive(complexState());
    await user.click(screen.getByText("første literal"));
    await user.keyboard("{End}{ArrowRight}");

    expect(domCaret().text).toBe("andre literal");
  });

  test("skipping a block without editable content", async () => {
    const { user } = renderLive(complexState());
    await user.click(screen.getByText("andre literal"));
    await user.keyboard("{ArrowRight}{ArrowRight}");

    expect(domCaret()).toEqual({ text: "paragraf med kun tekst", offset: 0 });
  });

  test("skipping a list item without editable content", async () => {
    const { user } = renderLive(complexState());
    await user.click(screen.getByText("1. item"));
    await user.keyboard("{End}{ArrowRight}");

    expect(domCaret()).toEqual({ text: "3. item", offset: 0 });
  });

  test("requiring two presses when a fritekst is selected", async () => {
    const { user } = renderLive(complexState());
    await user.click(screen.getByText("andre literal"));

    await user.keyboard("{ArrowRight}");
    expect(domCaret().text).toBe("andre literal");

    await user.keyboard("{ArrowRight}");
    expect(domCaret().text).toBe("paragraf med kun tekst");
  });
});

describe("clicking", () => {
  const threeLiterals = () =>
    stateV2(paragraph(literal("first literal"), literal("middle literal"), literal("last literal")));

  test.each(["first", "middle", "last"])(
    "triple-clicking the %s literal selects only that literal",
    async (position) => {
      const { user } = renderWithDispatchSpy(threeLiterals());

      await user.tripleClick(screen.getByText(`${position} literal`));

      expect(globalThis.getSelection()?.toString()).toBe(`${position} literal`);
    },
  );

  test("triple-click mousedown prevents native paragraph selection on a non-fritekst literal", () => {
    renderWithDispatchSpy(threeLiterals());

    const notPrevented = fireEvent.mouseDown(screen.getByText("middle literal"), { detail: 3 });

    expect(notPrevented).toBe(false);
    expect(globalThis.getSelection()?.toString()).toBe("middle literal");
  });

  test("triple-clicking a literal keeps it editable with the drag-select unifier mounted", async () => {
    const { user } = renderWithDispatchSpy(threeLiterals());
    const middle = screen.getByText("middle literal");

    await user.tripleClick(middle);
    document.dispatchEvent(new Event("selectionchange"));

    expect(globalThis.getSelection()?.toString()).toBe("middle literal");
    expect(middle.getAttribute("contenteditable")).toBe("true");
    expect(document.querySelector("[data-ce-orig]")).toBeNull();

    await user.keyboard("x");
    expect(middle.textContent).toBe("x");
  });

  test("triple-clicking literals in headings, list items and table cells selects only that literal", async () => {
    const { user } = renderWithDispatchSpy(
      stateV2(
        title2(literal("heading literal")),
        list(item(literal("first list literal"), literal("middle list literal"))),
        table(
          [column(literal("table heading"))],
          [row(cell(literal("first cell literal"), literal("middle cell literal")))],
        ),
      ),
    );

    await user.tripleClick(screen.getByText("heading literal"));
    expect(globalThis.getSelection()?.toString()).toBe("heading literal");

    await user.tripleClick(screen.getByText("middle list literal"));
    expect(globalThis.getSelection()?.toString()).toBe("middle list literal");

    await user.tripleClick(screen.getByText("middle cell literal"));
    expect(globalThis.getSelection()?.toString()).toBe("middle cell literal");
  });

  test("clicking an unedited fritekst selects the whole literal", async () => {
    const { user } = renderWithDispatchSpy(complexState());

    await user.click(screen.getByText("andre literal"));

    expect(globalThis.getSelection()?.toString()).toBe("andre literal");
  });

  test("double-clicking a focused fritekst without selection selects the whole literal", async () => {
    const { user } = renderWithDispatchSpy(complexState());
    const fritekstElement = screen.getByText("andre literal");
    await user.click(fritekstElement);
    await user.click(fritekstElement);
    expect(globalThis.getSelection()?.isCollapsed).toBe(true);

    await user.click(fritekstElement);
    expect(globalThis.getSelection()?.toString()).toBe("");

    await user.dblClick(fritekstElement);
    expect(globalThis.getSelection()?.toString()).toBe("andre literal");
  });

  test("clicking an edited fritekst does not select the whole literal", async () => {
    const { user } = renderWithDispatchSpy(
      stateV2(
        paragraph(
          literal("første literal"),
          variable("X"),
          literal({ text: "andre literal", editedText: "Hei på deg", fritekst: true }),
        ),
      ),
    );

    await user.click(screen.getByText("Hei på deg"));

    expect(globalThis.getSelection()?.toString()).toBe("");
  });
});

describe("focusing with tab", () => {
  test("tabbing to an unedited fritekst selects the whole literal so typing replaces it", async () => {
    const { user } = renderWithDispatchSpy(complexState());
    await user.click(screen.getByText("Dokumentet starter med variable"));
    expect(globalThis.getSelection()?.isCollapsed).toBe(true);

    await user.tab();

    const fritekstElement = screen.getByText("andre literal");
    expect(globalThis.getSelection()?.isCollapsed).toBe(false);
    expect(globalThis.getSelection()?.focusNode?.textContent).toBe("andre literal");

    await user.keyboard("abcdef");
    expect(fritekstElement.textContent).toBe("abcdef");
    expect(globalThis.getSelection()?.isCollapsed).toBe(true);
  });

  test("tabbing skips an edited fritekst", async () => {
    const { user } = renderWithDispatchSpy(
      stateV2(
        paragraph(
          literal("første literal"),
          variable("X"),
          literal({ text: "andre literal", editedText: "Denne skal ikke bli fokusert", fritekst: true }),
          fritekst("tredje literal"),
        ),
      ),
    );

    await user.click(screen.getByText("første literal"));
    await user.tab();

    expect(globalThis.getSelection()?.focusNode?.textContent).toBe("tredje literal");
  });
});

describe("Word Joiner before punctuation-starting literals", () => {
  const WORD_JOINER = "\u2060";

  test.each([
    ",tekst",
    ".tekst",
    ";tekst",
    ":tekst",
    "!tekst",
    "?tekst",
    "'tekst",
    '"tekst',
    "(tekst",
    ")tekst",
    "[tekst",
    "]tekst",
    "%",
    "\u00B0C",
    "\u2026",
    "\u00ABtekst",
    "\u00BB",
    "\u201Dtekst",
    "\u2019tekst",
  ])("inserts Word Joiner before a literal starting with %s", (text) => {
    const { container } = renderWithDispatchSpy(stateV2(paragraph(variable("verdi"), literal(text))));
    expect(container.textContent).toContain(WORD_JOINER);
  });

  test.each([
    ["a letter", [variable("verdi"), literal(" og noe mer tekst")]],
    ["a digit", [variable("verdi"), literal("100 kr")]],
    ["punctuation first in the container", [literal(",bare en literal")]],
  ])("does not insert Word Joiner before a literal starting with %s", (_, content) => {
    const { container } = renderWithDispatchSpy(stateV2(paragraph(...content)));
    expect(container.textContent).not.toContain(WORD_JOINER);
  });

  test("inserts Word Joiner before a punctuation literal following another literal", () => {
    const { container } = renderWithDispatchSpy(stateV2(paragraph(literal("første"), literal(", andre"))));
    expect(container.textContent).toContain(WORD_JOINER);
  });
});
