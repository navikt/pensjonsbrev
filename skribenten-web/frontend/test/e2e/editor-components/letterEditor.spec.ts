import { type BrowserContext, expect, type Locator, type Page, test } from "@playwright/test";

import { type EditedLetter, ElementTags, FontType, ListType, type LiteralValue } from "~/types/brevbakerTypes";
import { baseSakspart, baseSignatur, editorInfo } from "~test/e2e/support/editorFixtures";
import { setupSakStubs } from "~test/e2e/support/helpers";
import { brevResponse, editedLetter } from "~test/support/brevFixtures";
import {
  literal as _literal,
  variable as _variable,
  cell,
  item,
  itemList,
  paragraph,
  row,
  table,
  title1,
  title2,
  title3,
} from "~test/support/letterEditorTestUtils";

function makeLiteral(id: number, parentId: number | null, text: string) {
  return { ..._literal({ id, text }), parentId };
}

function makeVariable(id: number, parentId: number | null, text: string) {
  return { ..._variable(text), id, parentId };
}

const exampleLetter1 = editedLetter({
  title: {
    text: [makeLiteral(1, null, "Informasjon om saksbehandlingstiden vår")],
    deletedContent: [],
  },
  sakspart: baseSakspart(),
  blocks: [
    paragraph({
      id: 1,
      content: [
        makeLiteral(11, 1, "Denne blokken[CP1-1] "),
        makeVariable(12, 1, "VARIABLE-MED-LITT-LENGDE"),
        makeLiteral(
          13,
          1,
          "Er laget for å teste piltaster vertikalt i samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at markøren beveger seg til nærmeste side av variabelen.",
        ),
      ],
    }),
    paragraph({
      id: 2,
      content: [
        makeLiteral(21, 2, "[CP2-1]Her vil vi teste at piltast opp/ned"),
        makeVariable(22, 2, "ØVRE-VARIABLE"),
        makeLiteral(23, 2, "funker mellom avsnitt"),
      ],
    }),
    paragraph({
      id: 3,
      content: [
        makeLiteral(31, 3, "[CP2-2]Her vil vi"),
        makeVariable(32, 3, "NEDRE-VARIABLE"),
        makeLiteral(33, 3, " teste at pil opp/ned krysser avsnitt [CP2-3]"),
      ],
    }),
    title1({
      id: 4,
      content: [makeLiteral(41, 4, "Tittel over punktliste")],
    }),
    paragraph({
      id: 5,
      content: [
        {
          ...itemList({
            id: 51,
            items: [
              {
                ...item({
                  id: 511,
                  content: [
                    makeLiteral(
                      5111,
                      511,
                      "Punkt 1. Dette er et veldig langt punkt kun av den grunn at vi ønsker helst, mest sannsynlig, at denne skal brekke over to linjer[CP3-1]",
                    ),
                  ],
                }),
                parentId: 51,
              },
              {
                ...item({
                  id: 512,
                  content: [makeLiteral(5121, 512, "Punkt 2. Migrere brev[CP3-2]")],
                }),
                parentId: 51,
              },
              {
                ...item({
                  id: 513,
                  content: [makeLiteral(-5131, 513, "Punkt 3. Øk budsjettet[CP3-3]")],
                }),
                parentId: 51,
              },
            ],
          }),
          parentId: 5,
        },
      ],
    }),
    title2({
      id: 6,
      content: [makeLiteral(61, 6, "Tittel under punktliste")],
    }),
    paragraph({
      id: 7,
      content: [makeLiteral(71, 7, "Siste avsnitt for å teste at pil ned tar oss til end of line.[CP4-1]")],
    }),
  ],
  signatur: baseSignatur(),
}) as EditedLetter;

function makeBrevResponse(redigertBrev: EditedLetter) {
  return brevResponse({ info: editorInfo(), redigertBrev });
}

async function setupBrevRoute(page: Page, brevResponse: object) {
  await setupSakStubs(page);

  await page.route("**/bff/skribenten-backend/sak/123456/brev/1**", (route) => {
    if (route.request().url().includes("/pdf")) {
      return route.fulfill({ path: "test/e2e/fixtures/helloWorldPdf.txt", contentType: "application/json" });
    }
    return route.fulfill({ json: brevResponse, contentType: "application/json" });
  });

  // Stub save/update calls
  await page.route("**/bff/skribenten-backend/sak/123456/brev/1", (route) => {
    if (route.request().method() === "PUT" || route.request().method() === "PATCH") {
      return route.fulfill({ json: brevResponse, contentType: "application/json" });
    }
    return route.fallback();
  });
}

async function navigateToEditor(page: Page) {
  await page.goto("/saksnummer/123456/brev/1");
  // Wait for the editor to be ready
  await page.locator(".editor").waitFor({ state: "visible", timeout: 15_000 });
  // Wait for fonts to load
  await page.evaluate(() => document.fonts.ready);
}

async function copyText(page: Page, source: Locator) {
  await source.click();
  await source.evaluate((element) => {
    const textNode = element.firstChild;
    const selection = globalThis.getSelection();
    if (textNode?.nodeType !== Node.TEXT_NODE || !selection) {
      throw new Error("Missing copy source text node or selection");
    }
    const range = document.createRange();
    range.selectNodeContents(textNode);
    selection.removeAllRanges();
    selection.addRange(range);
  });
  await page.keyboard.press("ControlOrMeta+c");
  await expect(source).toHaveAttribute("contenteditable", "true");
}

// On macOS the native context menu opens on pointerdown and consumes the pointerup.
async function swallowNextPointerUp(page: Page) {
  await page.evaluate(() => {
    globalThis.addEventListener("pointerup", (event) => event.stopImmediatePropagation(), {
      capture: true,
      once: true,
    });
  });
}

async function nativePaste(context: BrowserContext, page: Page) {
  const cdp = await context.newCDPSession(page);
  await cdp.send("Input.dispatchKeyEvent", { type: "rawKeyDown", commands: ["Paste"] });
  await cdp.detach();
}

async function expectEditableCaretAtEndAfterMouseMove(page: Page, editable: Locator, pastedText: string) {
  const bounds = await editable.boundingBox();
  if (!bounds) throw new Error("Missing pasted text bounds");
  await page.mouse.move(bounds.x + 2, bounds.y + bounds.height / 2);
  await page.mouse.move(bounds.x + bounds.width + 40, bounds.y + bounds.height / 2);

  await expect(page.locator(".editor-surface")).not.toHaveClass(/is-drag-selecting/);
  await expect(editable).toHaveAttribute("contenteditable", "true");
  await expect(editable).toBeFocused();
  await assertCaret(page, pastedText, pastedText.length, { expectExact: true });
}

async function getCaretDebugInfo(page: Page) {
  await page.evaluate(
    () =>
      new Promise<void>((resolve) => {
        requestAnimationFrame(() => requestAnimationFrame(() => resolve()));
      }),
  );
  return page.evaluate(() => {
    const selection = globalThis.getSelection();
    if (!selection || selection.rangeCount === 0) {
      return null;
    }

    const range = selection.getRangeAt(0).cloneRange();
    const container = range.startContainer;
    const containerText = container.textContent ?? "";
    const offset = range.startOffset;

    const contextStart = Math.max(0, offset - 5);
    const contextEnd = Math.min(containerText.length, offset + 5);

    const rect = (() => {
      if (!range.collapsed) {
        const selectionRect = range.getBoundingClientRect();
        return {
          x: Math.round((selectionRect.left + selectionRect.right) / 2),
          y: Math.round((selectionRect.top + selectionRect.bottom) / 2),
          source: "selection",
        };
      }

      if (container.nodeType === Node.TEXT_NODE) {
        if (offset < containerText.length) {
          const probe = range.cloneRange();
          probe.setEnd(container, offset + 1);
          const nextCharRect = probe.getClientRects()[0];
          if (nextCharRect) {
            return {
              x: Math.round(nextCharRect.left),
              y: Math.round((nextCharRect.top + nextCharRect.bottom) / 2),
              source: "next-char",
            };
          }
        }

        if (offset > 0) {
          const probe = range.cloneRange();
          probe.setStart(container, offset - 1);
          const previousCharRects = probe.getClientRects();
          const previousCharRect = previousCharRects[previousCharRects.length - 1];
          if (previousCharRect) {
            return {
              x: Math.round(previousCharRect.right),
              y: Math.round((previousCharRect.top + previousCharRect.bottom) / 2),
              source: "previous-char",
            };
          }
        }
      }

      const fallbackRect = range.getBoundingClientRect();
      return {
        x: Math.round((fallbackRect.left + fallbackRect.right) / 2),
        y: Math.round((fallbackRect.top + fallbackRect.bottom) / 2),
        source: "range",
      };
    })();

    return {
      // activeText: document.activeElement?.textContent,
      caretOffset: offset,
      visualized: `${containerText.slice(contextStart, offset)}|${containerText.slice(offset, contextEnd)}`,
      position: { x: rect.x, y: rect.y },
    };
  });
}

async function move(page: Page, key: string, times: number) {
  const before = process.env.E2E_UI_MODE ? await getCaretDebugInfo(page) : null;
  for (let i = 0; i < times; i++) {
    await page.keyboard.press(key);
  }
  const after = process.env.E2E_UI_MODE ? await getCaretDebugInfo(page) : null;
  if (process.env.E2E_UI_MODE) console.info(`${key} x${times}`, "\nbefore", before, "\nafter", after);
}

async function insertManualLineBreak(page: Page, text: string, offset: number) {
  await page.getByText(text, { exact: true }).evaluate((element, caretOffset) => {
    const textNode = element.firstChild;
    if (!textNode) throw new Error("Could not find text node");

    element.focus({ preventScroll: true });
    const range = document.createRange();
    range.setStart(textNode, caretOffset);
    range.collapse(true);
    const selection = globalThis.getSelection();
    selection?.removeAllRanges();
    selection?.addRange(range);
  }, offset);
  await page.keyboard.press("Shift+Enter");
  await expect(page.locator("br[data-literal-index]")).toHaveCount(1);
}

async function setEditorFocus(page: Page, focus: { blockIndex: number; contentIndex: number; cursorPosition: number }) {
  await page.evaluate((focusArg) => {
    const editorEl = document.querySelector(".editor");
    if (!editorEl) throw new Error("Editor element not found");

    const fiberKey = Object.keys(editorEl).find((key) => key.startsWith("__reactFiber$"));
    if (!fiberKey) throw new Error("React fiber not found on editor element");

    let fiber = (editorEl as unknown as Record<string, unknown>)[fiberKey] as Record<string, unknown> | null;
    let setEditorState: ((fn: (prev: unknown) => unknown) => void) | null = null;

    while (fiber) {
      const memoizedProps = fiber.memoizedProps as Record<string, unknown> | undefined;
      if (
        memoizedProps?.value &&
        typeof (memoizedProps.value as Record<string, unknown>).setEditorState === "function"
      ) {
        setEditorState = (memoizedProps.value as Record<string, unknown>).setEditorState as (
          fn: (prev: unknown) => unknown,
        ) => void;
        break;
      }
      fiber = fiber.return as Record<string, unknown> | null;
    }

    if (!setEditorState) throw new Error("setEditorState not found in React fiber tree");
    setEditorState((prev: unknown) => ({
      ...(prev as Record<string, unknown>),
      focus: focusArg,
    }));
  }, focus);
}

async function assertCaret(
  page: Page,
  expectedContent: string,
  expectedCaretOffset: number,
  precision?: { expectExact: boolean },
) {
  // Verify focused element contains the expected text
  const focused = page.locator(":focus");
  await expect(focused).toContainText(expectedContent);

  // Check caret offset with tolerance of ±15 to account for font rendering differences
  const actualOffset = await page.evaluate(() => {
    const selection = globalThis.getSelection();
    return selection?.rangeCount ? selection.getRangeAt(0).startOffset : -1;
  });

  if (process.env.E2E_UI_MODE) {
    console.info("ASSERT");
    console.info("focused:", await focused.allTextContents());
    console.info(await getCaretDebugInfo(page));
    console.info("expectedContent:", expectedContent);
    console.info("expectedOffset:", expectedCaretOffset);
    console.info("actualOffset:", actualOffset);
  }

  // assert
  if (precision?.expectExact) {
    expect(actualOffset).toBe(expectedCaretOffset);
  } else {
    expect(actualOffset).toBeGreaterThanOrEqual(expectedCaretOffset - 15);
    expect(actualOffset).toBeLessThanOrEqual(expectedCaretOffset + 15);
  }
}

test.describe("LetterEditor", () => {
  test.beforeEach(async ({ page }) => {
    const brevResponse = makeBrevResponse(exampleLetter1);
    await setupBrevRoute(page, brevResponse);
    await navigateToEditor(page);
  });

  test.describe("Navigation", () => {
    test("ArrowUp works within contenteditables", async ({ page }) => {
      // Playwright's click() places caret on element's first line, override with "position"
      await page.getByText("CP1-3").click({ position: { x: 320, y: 36 } });
      await move(page, "End", 1);
      await move(page, "ArrowLeft", 10);
      await move(page, "ArrowUp", 1);
      await assertCaret(page, "[CP1-2]", 20);
    });

    test("ArrowDown works between contenteditables", async ({ page }) => {
      await page.getByText("CP1-1").click();
      await move(page, "ArrowRight", 5);
      await move(page, "ArrowDown", 1);
      await assertCaret(page, "[CP1-2]", 48);
    });

    test("ArrowUp moves to the right of a variable if that is closest", async ({ page }) => {
      // Playwright's click() places caret on element's first line, override with "position"
      await page.getByText("CP1-3").click({ position: { x: 0, y: 36 } });
      await move(page, "ArrowRight", 45);
      await move(page, "ArrowUp", 1);
      await assertCaret(page, "[CP1-2]", 0, { expectExact: true });
    });

    test("ArrowUp moves to the left of a variable if that is closest", async ({ page }) => {
      // Playwright's click() places caret on element's first line, override with "position"
      await page.getByText("CP1-3").click({ position: { x: 0, y: 36 } });
      await move(page, "ArrowRight", 40);
      await move(page, "ArrowUp", 1);
      await assertCaret(page, "[CP1-1]", 21, { expectExact: true });
    });

    test("ArrowUp works between paragraphs", async ({ page }) => {
      await page.getByText("CP2-2").click({ position: { x: 0, y: 0 } });
      await move(page, "ArrowRight", 10);
      await move(page, "ArrowUp", 1);
      await assertCaret(page, "[CP2-1]", 10);
    });

    test("ArrowDown works between paragraphs", async ({ page }) => {
      await page.getByText("CP2-1").click({ position: { x: 0, y: 0 } });
      await move(page, "ArrowRight", 10);
      await move(page, "ArrowDown", 1);
      await assertCaret(page, "[CP2-2]", 10);
    });

    test("ArrowDown moves between paragraphs and to the nearest side of a variable [LEFT]", async ({ page }) => {
      await page.getByText("CP2-1").click({ position: { x: 0, y: 0 } });
      await move(page, "ArrowRight", 27);
      await assertCaret(page, "[CP2-1]", 27, { expectExact: true });
      await move(page, "ArrowDown", 1);
      await assertCaret(page, "[CP2-2]", 17, { expectExact: true });
    });

    test("ArrowUp moves between paragraphs and to the nearest side of a variable [LEFT]", async ({ page }) => {
      await page.getByText("CP2-3").click({ position: { x: 0, y: 0 } });
      await move(page, "ArrowRight", 15);
      await assertCaret(page, "[CP2-3]", 15, { expectExact: true });
      await move(page, "ArrowUp", 1);
      await assertCaret(page, "[CP2-1]", 42, { expectExact: true });
    });

    test("ArrowDown moves between paragraphs and to the nearest side of a variable [RIGHT]", async ({ page }) => {
      await page.getByText("CP2-1").click({ position: { x: 0, y: 0 } });
      await move(page, "ArrowRight", 28);
      await assertCaret(page, "[CP2-1]", 28, { expectExact: true });
      await move(page, "ArrowDown", 1);
      await assertCaret(page, "[CP2-3]", 0, { expectExact: true });
    });

    test("Can move up an itemlist", async ({ page }) => {
      await page.getByText("CP3-3").click();
      await move(page, "ArrowUp", 1);
      await assertCaret(page, "[CP3-2]", 14);
      await move(page, "ArrowUp", 1);
      await assertCaret(page, "[CP3-1]", 97);
      await move(page, "ArrowUp", 1);
      await assertCaret(page, "[CP3-1]", 14);
      await move(page, "ArrowUp", 1);
      await assertCaret(page, "Tittel over punktliste", 16);
    });

    test("Can move down an itemlist", async ({ page }) => {
      await page.getByText("Tittel over punktliste").click({ position: { x: 0, y: 0 } });
      await move(page, "ArrowRight", 10);
      await assertCaret(page, "Tittel over punktliste", 10);
      await move(page, "ArrowDown", 1);
      await assertCaret(page, "[CP3-1]", 5);
      await move(page, "ArrowDown", 1);
      await assertCaret(page, "[CP3-1]", 87);
      await move(page, "ArrowDown", 1);
      await assertCaret(page, "[CP3-2]", 5);
      await move(page, "ArrowDown", 1);
      await assertCaret(page, "[CP3-3]", 5);
      await move(page, "ArrowDown", 1);
      await assertCaret(page, "Tittel under punktliste", 10);
    });

    test("ArrowUp at first node moves caret to the beginning", async ({ page }) => {
      await page.getByText("Informasjon om saksbehandlingstiden vår").click();
      await assertCaret(page, "Informasjon om saksbehandlingstiden vår", 19);
      await move(page, "ArrowUp", 1);
      await assertCaret(page, "Informasjon om saksbehandlingstiden vår", 0, { expectExact: true });
    });

    test("ArrowDown at last node moves caret to the end", async ({ page }) => {
      await page.getByText("CP4-1").click();
      await move(page, "ArrowLeft", 10);
      await move(page, "ArrowDown", 1);
      await assertCaret(page, "CP4-1", 68);
    });

    test("ArrowUp moves caret to start of previous line even if it is displaced rightwards (list item)", async ({
      page,
    }) => {
      await page.getByText("CP4-1").click({ position: { x: 0, y: 0 } });
      await move(page, "ArrowUp", 2);
      await assertCaret(page, "CP3-3", 0, { expectExact: true });
    });

    test("ArrowDown moves caret to start of next line even if it is displaced rightwards (list item)", async ({
      page,
    }) => {
      await page.getByText("Tittel over punktliste").click({ position: { x: 0, y: 0 } });
      await move(page, "ArrowDown", 1);
      await assertCaret(page, "CP3-1", 0);
    });
  });

  test.describe("Focus", () => {
    test("invalid focus is ignored", async ({ page }) => {
      // This replicates the Cypress component test that mounted with an out-of-bounds cursorPosition,
      // verifying the clamping at ContentGroup.tsx (Math.min(cursorPosition, text.length)).
      const targetText = (exampleLetter1.blocks[0].content[2] as LiteralValue).text;
      const validCursorPosition = 5;
      const invalidCursorPosition = targetText.length + 10;

      // First, set a valid cursor position and verify it takes effect
      await setEditorFocus(page, { blockIndex: 0, contentIndex: 2, cursorPosition: validCursorPosition });

      // Wait for React to re-render and place the cursor
      await expect
        .poll(async () => {
          return page.evaluate(() => {
            const sel = globalThis.getSelection();
            if (!sel || sel.rangeCount === 0) return { offset: -1, text: "" };
            return {
              offset: sel.getRangeAt(0).startOffset,
              text: sel.anchorNode?.parentElement?.getAttribute("contenteditable") ?? "",
            };
          });
        })
        .toEqual({ offset: validCursorPosition, text: "true" });

      // Now set an invalid cursor position (beyond text length) — should not crash
      await setEditorFocus(page, { blockIndex: 0, contentIndex: 2, cursorPosition: invalidCursorPosition });
      await expect(page.getByText("Informasjon om saksbehandlingstiden vår")).toBeVisible();
      await expect(page.getByText("CP1-2")).toBeVisible();

      // Verify the cursor was clamped to text length rather than placed at the invalid offset
      const clampedOffset = await page.evaluate(() => {
        const sel = globalThis.getSelection();
        return sel?.rangeCount ? sel.getRangeAt(0).startOffset : -1;
      });
      expect(clampedOffset).toBeLessThanOrEqual(targetText.length);
    });
  });

  test.describe("Presentation", () => {
    // These tests involve extra navigation and can be slow when running the full suite
    test.slow();

    test("displays verge only when verge is present", async ({ page }) => {
      // Test with verge (annenMottakerNavn)
      const letterWithVerge = {
        ...exampleLetter1,
        sakspart: {
          gjelderNavn: "Test Testeson",
          gjelderFoedselsnummer: "12345678910",
          annenMottakerNavn: "Vergio Vergburg",
          saksnummer: "1234",
          dokumentDato: "2024-03-15",
        },
      };
      const brevResponseWithVerge = makeBrevResponse(letterWithVerge);

      // Re-setup routes with verge data
      await page.route("**/bff/skribenten-backend/sak/123456/brev/1**", (route) => {
        if (route.request().url().includes("/pdf")) {
          return route.fulfill({ path: "test/e2e/fixtures/helloWorldPdf.txt", contentType: "application/json" });
        }
        return route.fulfill({ json: brevResponseWithVerge, contentType: "application/json" });
      });

      await page.goto("/saksnummer/123456/brev/1");
      await page.locator(".editor").waitFor({ state: "visible", timeout: 15_000 });

      await expect(page.getByText("Mottaker:")).toBeVisible();
      await expect(page.getByText("Vergio Vergburg")).toBeVisible();
      await expect(page.getByText("Navn:")).not.toBeVisible();
      await expect(page.getByText("Saken gjelder:")).toBeVisible();
      await expect(page.getByText("Test Testeson")).toBeVisible();
    });

    test("displays name without verge", async ({ page }) => {
      // Default sakspart does not have annenMottakerNavn, so verge UI should not show
      await expect(page.getByText("Mottaker:")).not.toBeVisible();
      await expect(page.getByText("Navn:")).toBeVisible();
      await expect(page.getByText("Test Testeson")).toBeVisible();
      await expect(page.getByText("Saken gjelder:")).not.toBeVisible();
    });
  });
});

test.describe("LetterEditor copy and paste within the editor", () => {
  test("a selection across a heading and a paragraph survives mouseup and right-click, and copies in full", async ({
    page,
  }) => {
    await page.context().grantPermissions(["clipboard-read", "clipboard-write"]);
    await setupBrevRoute(
      page,
      makeBrevResponse(
        editedLetter({
          blocks: [title2(_literal("Heading to copy")), paragraph([_literal("Paragraph to copy")])],
        }),
      ),
    );
    await navigateToEditor(page);

    const heading = page.locator(".editor-surface > .TITLE2 [data-literal-index]");
    const paragraphText = page.locator(".editor-surface > .PARAGRAPH [data-literal-index]");
    const headingBox = (await heading.boundingBox())!;
    const paragraphBox = (await paragraphText.boundingBox())!;
    const selectedText = () => page.evaluate(() => globalThis.getSelection()?.toString());

    await page.mouse.move(headingBox.x + 1, headingBox.y + headingBox.height / 2);
    await page.mouse.down();
    await page.mouse.move(paragraphBox.x + paragraphBox.width - 1, paragraphBox.y + paragraphBox.height / 2, {
      steps: 5,
    });
    await page.mouse.up();
    await expect.poll(selectedText).toBe("Heading to copy\nParagraph to copy");

    await page.mouse.click(paragraphBox.x + 10, paragraphBox.y + paragraphBox.height / 2, { button: "right" });
    await expect.poll(selectedText).toBe("Heading to copy\nParagraph to copy");

    const clipboardText = () => page.evaluate(() => navigator.clipboard.readText());
    // Like the context menu's Copy, this fires the copy event without a keydown.
    await page.evaluate(() => document.execCommand("copy"));
    await expect.poll(clipboardText).toBe("Heading to copy\nParagraph to copy");

    await page.evaluate(() => navigator.clipboard.writeText(""));
    await page.keyboard.press("ControlOrMeta+c");
    await expect.poll(clipboardText).toBe("Heading to copy\nParagraph to copy");

    await page.keyboard.press("Escape");
    await paragraphText.click();
    await expect(paragraphText).toHaveAttribute("contenteditable", "true");
    await page.keyboard.press("End");
    await page.keyboard.type("!");
    await expect(paragraphText).toHaveText("Paragraph to copy!");
  });

  async function setupHeadingAndEmptyLine(
    page: Page,
    heading: ReturnType<typeof title1 | typeof title2 | typeof title3>,
  ) {
    await page.context().grantPermissions(["clipboard-read", "clipboard-write"]);
    await setupBrevRoute(page, makeBrevResponse(editedLetter({ blocks: [heading, paragraph([_literal("")])] })));
    await navigateToEditor(page);
  }

  async function copyHeadingText(page: Page, className: string, range?: { start: number; end: number }) {
    await page.locator(`.editor-surface > .${className} [data-literal-index]`).evaluate((element, offsets) => {
      const selectionRange = document.createRange();
      if (offsets) {
        selectionRange.setStart(element.firstChild!, offsets.start);
        selectionRange.setEnd(element.firstChild!, offsets.end);
      } else {
        selectionRange.selectNodeContents(element);
      }
      globalThis.getSelection()!.removeAllRanges();
      globalThis.getSelection()!.addRange(selectionRange);
    }, range);
    await page.keyboard.press("ControlOrMeta+c");
  }

  async function pasteIntoEmptyLine(page: Page) {
    const emptyLine = page.locator(".editor-surface > .PARAGRAPH:last-child > [data-literal-index]");
    await emptyLine.focus();
    await expect(emptyLine).toBeFocused();
    await page.keyboard.press("ControlOrMeta+v");
  }

  for (const [className, level, heading] of [
    ["TITLE1", 2, title1(_literal("Knirkende stoler i rom 4A"))],
    ["TITLE2", 3, title2(_literal("Knirkende stoler i rom 4A"))],
    ["TITLE3", 4, title3(_literal("Knirkende stoler i rom 4A"))],
  ] as const) {
    test(`a whole ${className} copied on its own pastes as the same heading`, async ({ page }) => {
      await setupHeadingAndEmptyLine(page, heading);
      await copyHeadingText(page, className);
      await pasteIntoEmptyLine(page);

      await expect(page.locator(`.editor-surface > .${className}`)).toHaveCount(2);
      await expect(page.locator(`.editor-surface > .${className}`).nth(1)).toHaveText("Knirkende stoler i rom 4A");
      await expect(page.getByRole("heading", { level, name: "Knirkende stoler i rom 4A" })).toHaveCount(2);
    });
  }

  test("a whole heading missing from the template still pastes as a heading", async ({ page }) => {
    await setupHeadingAndEmptyLine(page, {
      ...title2(_literal("Knirkende stoler i rom 4A")),
      decision: { type: "MISSING_FROM_RENDERED", status: "PENDING" },
    });
    await expect(page.getByRole("button", { name: "Behold" })).toBeVisible();
    await copyHeadingText(page, "TITLE2");
    await pasteIntoEmptyLine(page);

    await expect(page.locator(".editor-surface > .TITLE2")).toHaveCount(2);
    await expect(page.locator(".editor-surface > .TITLE2").nth(1)).toHaveText("Knirkende stoler i rom 4A");
    await expect(page.getByRole("heading", { level: 3, name: "Knirkende stoler i rom 4A", exact: true })).toHaveCount(
      2,
    );
  });

  test("part of a heading pastes as plain paragraph text", async ({ page }) => {
    await setupHeadingAndEmptyLine(page, title1(_literal("Knirkende stoler i rom 4A")));
    await copyHeadingText(page, "TITLE1", { start: 0, end: 9 });
    await pasteIntoEmptyLine(page);

    await expect(page.locator(".editor-surface > .TITLE1")).toHaveCount(1);
    const pasted = page.locator(".editor-surface > .PARAGRAPH").getByText("Knirkende", { exact: true });
    await expect(pasted).toHaveCSS("font-weight", "400");
  });

  test("a heading and a paragraph with bold text keep their structure and formatting", async ({ page }) => {
    await page.context().grantPermissions(["clipboard-read", "clipboard-write"]);
    await setupBrevRoute(
      page,
      makeBrevResponse(
        editedLetter({
          blocks: [
            title2(_literal("Knirkende stoler i rom 4A")),
            paragraph([_literal("Noen stoler "), { ..._literal("knirker"), fontType: FontType.BOLD }, _literal(".")]),
            paragraph([_literal("")]),
          ],
        }),
      ),
    );
    await navigateToEditor(page);

    await page.evaluate(() => {
      const literals = document.querySelectorAll(".editor-surface [data-literal-index]");
      const range = document.createRange();
      range.setStart(literals[0].firstChild!, 0);
      range.setEnd(literals[3].firstChild!, 1);
      globalThis.getSelection()!.removeAllRanges();
      globalThis.getSelection()!.addRange(range);
    });
    await page.keyboard.press("ControlOrMeta+c");

    const emptyParagraph = page.locator(".editor-surface > .PARAGRAPH:nth-child(3) > [data-literal-index]");
    await emptyParagraph.focus();
    await expect(emptyParagraph).toBeFocused();
    await page.keyboard.press("ControlOrMeta+v");

    const pastedHeading = page.locator(".editor-surface > .TITLE2").nth(1);
    await expect(pastedHeading).toHaveText("Knirkende stoler i rom 4A");
    const sourceParagraph = page.locator(".editor-surface > .TITLE2 + .PARAGRAPH").first();
    const pastedParagraph = page.locator(".editor-surface > .TITLE2 + .PARAGRAPH").nth(1);
    await expect(pastedParagraph).toHaveText((await sourceParagraph.textContent()) ?? "");
    await expect(pastedParagraph.getByText("knirker", { exact: true })).toHaveCSS("font-weight", "700");
    await expect(pastedParagraph.getByText("Noen stoler", { exact: true })).toHaveCSS("font-weight", "400");
  });

  test("copying within numbered lists and tables keeps list numbering and cell boundaries", async ({ page }) => {
    await page.context().grantPermissions(["clipboard-read", "clipboard-write"]);
    await setupBrevRoute(
      page,
      makeBrevResponse(
        editedLetter({
          blocks: [
            paragraph([
              itemList({
                listType: ListType.NUMMERERT_LISTE,
                items: [item({ content: [_literal("Første")] }), item({ content: [_literal("Andre")] })],
              }),
            ]),
            paragraph([
              table([cell(_literal("H1")), cell(_literal("H2"))], [row(cell(_literal("A1")), cell(_literal("B1")))]),
            ]),
            paragraph([_literal("")]),
            paragraph([_literal("")]),
          ],
        }),
      ),
    );
    await navigateToEditor(page);

    const listItems = page.locator(".editor-surface ol li");
    const firstItemBox = (await listItems.nth(0).boundingBox())!;
    const secondItemBox = (await listItems.nth(1).boundingBox())!;
    await page.mouse.move(firstItemBox.x + 2, firstItemBox.y + firstItemBox.height / 2);
    await page.mouse.down();
    await page.mouse.move(secondItemBox.x + secondItemBox.width - 2, secondItemBox.y + secondItemBox.height / 2, {
      steps: 5,
    });
    await page.mouse.up();
    await page.keyboard.press("ControlOrMeta+c");

    const firstTarget = page.locator(".editor-surface > .PARAGRAPH:nth-child(3) > [data-literal-index]");
    await firstTarget.click();
    await page.keyboard.press("ControlOrMeta+v");

    await expect(page.locator(".editor-surface ol")).toHaveCount(2);
    await expect(page.locator(".editor-surface ol").nth(1).locator("li")).toHaveText(["Første", "Andre", "\u200B"]);

    const tableCells = page.locator(".editor-surface tbody td");
    const firstCellBox = (await tableCells.nth(0).boundingBox())!;
    const secondCellBox = (await tableCells.nth(1).boundingBox())!;
    await page.mouse.move(firstCellBox.x + 2, firstCellBox.y + firstCellBox.height / 2);
    await page.mouse.down();
    await page.mouse.move(secondCellBox.x + secondCellBox.width - 2, secondCellBox.y + secondCellBox.height / 2, {
      steps: 5,
    });
    await page.mouse.up();
    await page.keyboard.press("ControlOrMeta+c");

    const secondTarget = page.locator(".editor-surface > .PARAGRAPH:last-child > [data-literal-index]");
    await secondTarget.click();
    await page.keyboard.press("ControlOrMeta+v");

    await expect(page.getByTestId("letter-table")).toHaveCount(2);
    const pastedTable = page.getByTestId("letter-table").nth(1);
    await expect(pastedTable.locator("th")).toHaveText(["A1", "B1"]);
  });
});

test.describe("LetterEditor empty-line context menu", () => {
  test.beforeEach(async ({ page }) => {
    await setupBrevRoute(
      page,
      makeBrevResponse(
        editedLetter({
          blocks: [
            paragraph([_literal("Copy me")]),
            paragraph([_literal("")]),
            title1(_literal("")),
            title2(_literal("")),
            title3(_literal("")),
            paragraph([itemList({ items: [item({ content: [_literal("")] })] })]),
            paragraph([itemList({ listType: ListType.NUMMERERT_LISTE, items: [item({ content: [_literal("")] })] })]),
          ],
        }),
      ),
    );
    await navigateToEditor(page);
  });

  for (const { name, selector } of [
    { name: "paragraph", selector: ".PARAGRAPH:nth-child(2)" },
    { name: "heading 1", selector: ".TITLE1" },
    { name: "heading 2", selector: ".TITLE2" },
    { name: "heading 3", selector: ".TITLE3" },
    { name: "bullet item", selector: "ul > li" },
    { name: "numbered item", selector: "ol > li" },
  ]) {
    for (const { position, fraction } of [
      { position: "top left", fraction: 0.1 },
      { position: "center", fraction: 0.5 },
      { position: "bottom right", fraction: 0.9 },
    ]) {
      test(`empty ${name}: right-click at ${position} targets the caret for native paste`, async ({
        page,
        context,
      }) => {
        const source = page.locator(".editor-surface > .PARAGRAPH:first-child > [data-literal-index]");
        await copyText(page, source);

        const line = page.locator(`.editor-surface ${selector}`);
        const editable = line.locator("[data-literal-index]");
        await expect(editable).toHaveAttribute("data-empty", "");
        const bounds = await line.boundingBox();
        if (!bounds) throw new Error("Missing empty line bounds");
        const point = { x: bounds.x + bounds.width * fraction, y: bounds.y + bounds.height * fraction };

        expect(
          await editable.evaluate((element, { x, y }) => {
            const target = document.elementFromPoint(x, y);
            return target === element && target instanceof HTMLElement && target.isContentEditable;
          }, point),
        ).toBe(true);

        await swallowNextPointerUp(page);
        await page.mouse.click(point.x, point.y, { button: "right" });
        await expect(editable).toBeFocused();
        await expect
          .poll(() =>
            editable.evaluate((element) => {
              const selection = globalThis.getSelection();
              return !!selection?.isCollapsed && element.contains(selection.anchorNode);
            }),
          )
          .toBe(true);
        await page.keyboard.press("Escape");
        await nativePaste(context, page);

        await expect(line).toHaveText("Copy me");
        await expect(source).toHaveText("Copy me");
        await expect(editable).not.toHaveAttribute("data-empty");
        await expect(editable).toHaveCSS("display", "inline");
        await expectEditableCaretAtEndAfterMouseMove(page, editable, "Copy me");

        await page.getByRole("button", { name: "Angre (Undo)" }).click();
        await expect(editable).toHaveAttribute("data-empty", "");
        await expect(editable).toHaveCSS("display", "block");
        await expect(editable).toBeFocused();

        await page.keyboard.type("x");
        await expect(line).toHaveText("x");
        await page.keyboard.press("Backspace");
        await expect(editable).toHaveAttribute("data-empty", "");
        await expect(editable).toHaveCSS("display", "block");
      });
    }
  }

  test("ctrl-click context menu (macOS) leaves the caret editable at the end of the pasted text", async ({
    page,
    context,
  }) => {
    const source = page.locator(".editor-surface > .PARAGRAPH:first-child > [data-literal-index]");
    await copyText(page, source);
    const editable = page.locator(".editor-surface > .PARAGRAPH:nth-child(2) > [data-literal-index]");

    await swallowNextPointerUp(page);
    await page.keyboard.down("Control");
    await editable.click();
    await page.keyboard.up("Control");
    await nativePaste(context, page);

    await expect(editable).toHaveText("Copy me");
    await expectEditableCaretAtEndAfterMouseMove(page, editable, "Copy me");
    await page.keyboard.type("!");
    await expect(editable).toHaveText("Copy me!");
  });
});

test.describe("LetterEditor text fields", () => {
  test.beforeEach(async ({ page }) => {
    await setupBrevRoute(
      page,
      makeBrevResponse(
        editedLetter({
          blocks: [
            paragraph([
              _literal({ text: "Fyll inn fritekst", tags: [ElementTags.FRITEKST] }),
              _literal(" "),
              _variable("Variabelverdi"),
            ]),
            paragraph([_literal("Tom variabel: "), _variable(""), _literal(" slutt")]),
          ],
        }),
      ),
    );
    await navigateToEditor(page);
  });

  test("fritekst matches the blue design until replaced with ordinary text", async ({ page }) => {
    const placeholder = page.locator(".editor").getByText("Fyll inn fritekst", { exact: true });
    await expect(placeholder).toHaveCSS("color", "rgb(0, 91, 182)");
    await expect(placeholder).toHaveCSS("background-color", "rgb(227, 239, 247)");
    await expect(placeholder).toHaveCSS("font-weight", "600");
    await expect(placeholder).toHaveCSS("border-radius", "2px");
    await expect(placeholder).toHaveCSS("padding-inline-start", "2px");
    await expect(placeholder).toHaveCSS("text-decoration-line", "underline");
    await expect(placeholder).toHaveCSS("text-decoration-style", "solid");
    await expect(placeholder).toHaveCSS("text-decoration-thickness", "from-font");
    await expect(placeholder).toHaveCSS("text-underline-position", "from-font");

    await page.locator(".editor").getByText("Tom variabel:", { exact: true }).click();
    await placeholder.click();
    await expect.poll(() => page.evaluate(() => globalThis.getSelection()?.toString())).toBe("Fyll inn fritekst");
    await page.keyboard.type("Utfylt tekst");
    const filled = page.locator(".editor").getByText("Utfylt tekst", { exact: true });
    await expect(filled).toHaveCSS("background-color", "rgba(0, 0, 0, 0)");
    await expect(filled).toHaveCSS("color", "rgb(32, 39, 51)");
    await expect(filled).toHaveCSS("font-weight", "400");
    await expect(filled).toHaveCSS("padding-inline-start", "0px");
    await expect(filled).toHaveCSS("text-decoration-line", "none");
  });

  test("variables match the gray design while retaining focus and empty-field behavior", async ({ page }, testInfo) => {
    const value = page.locator(".editor").getByText("Variabelverdi", { exact: true });
    await expect(value).toHaveCSS("background-color", "rgb(236, 237, 239)");
    await expect(value).toHaveCSS("color", "rgb(32, 39, 51)");
    await expect(value).toHaveCSS("font-weight", "400");
    await expect(value).toHaveCSS("outline", "rgb(111, 119, 133) solid 1px");
    await expect(value).toHaveCSS("border-radius", "2px");
    await expect(value).toHaveCSS("padding-inline-start", "2px");

    const empty = page.locator(".editor .PARAGRAPH").nth(1).locator("[data-literal-index]:not([contenteditable])");
    await expect(empty).toHaveCSS("background-color", "rgba(0, 0, 0, 0)");
    await expect(empty).toHaveCSS("outline-style", "none");
    await expect(empty).toHaveCSS("padding-inline-start", "0px");

    const baselineBounds = await value.boundingBox();
    await value.click();
    await expect(value).toHaveCSS("outline-width", "2px");
    expect(await value.boundingBox()).toEqual(baselineBounds);
    await page.locator(".editor").getByText("Tom variabel:", { exact: true }).click();
    await expect(value).toHaveCSS("outline", "rgb(111, 119, 133) solid 1px");
    await testInfo.attach("fritekst-and-variable-fields", {
      body: await page.screenshot({ fullPage: true }),
      contentType: "image/png",
    });
  });
});

test.describe("LetterEditor scrolling navigation", () => {
  const padded = (n: number) => String(n).padStart(2, "0");
  const paragraphText = (n: number) => `Avsnitt nummer ${n} [P${padded(n)}]`;
  // The literal that follows the manual line break must wrap over many lines. The reported jump
  // only happens when the caret enters a tall multi-line element at the edge of the visible area;
  // with a single-line literal the container never scrolls more than one line height.
  const beforeNewLine = `FOER-START ${"Tekst foran linjeskiftet som fyller ut linjen. ".repeat(4)}FOER-SLUTT`;
  const afterNewLine = `ETTER-START ${"Tekst etter linjeskiftet som fyller ut mange linjer. ".repeat(24)}ETTER-SLUTT`;
  const textWithManualLineBreak = `${beforeNewLine}${afterNewLine}`;

  const longLetter = editedLetter({
    title: {
      text: [makeLiteral(1, null, "Langt brev som ikke får plass på én skjerm")],
      deletedContent: [],
    },
    sakspart: baseSakspart(),
    blocks: [
      ...Array.from({ length: 40 }, (_, i) =>
        paragraph({
          id: 100 + i,
          content: [makeLiteral(1000 + i, 100 + i, paragraphText(i + 1))],
        }),
      ),
      paragraph({
        id: 160,
        content: [makeLiteral(1060, 160, textWithManualLineBreak)],
      }),
      ...Array.from({ length: 5 }, (_, i) =>
        paragraph({
          id: 161 + i,
          content: [makeLiteral(2000 + i, 161 + i, paragraphText(i + 41))],
        }),
      ),
    ],
    signatur: baseSignatur(),
  }) as EditedLetter;

  test.beforeEach(async ({ page }) => {
    await setupBrevRoute(page, makeBrevResponse(longLetter));
    await navigateToEditor(page);
  });

  test("ArrowDown keeps the caret and scrolls when the next line is below the visible area", async ({ page }) => {
    await page.getByText(paragraphText(1), { exact: true }).click();
    await assertCaret(page, "[P01]", 0);

    await move(page, "ArrowDown", 30);

    // The caret must survive the trip past the bottom edge of the visible area ...
    await assertCaret(page, "[P31]", 0);

    // ... and the editor must actually have scrolled to keep the caret visible
    const scrollTop = await page.locator(".editor").evaluate((el) => el.parentElement?.scrollTop ?? -1);
    expect(scrollTop).toBeGreaterThan(0);
  });

  test("ArrowUp keeps the caret and scrolls when the next line is above the visible area", async ({ page }) => {
    await page.getByText(paragraphText(35), { exact: true }).click();
    await assertCaret(page, "[P35]", 0);

    await move(page, "ArrowUp", 30);

    await assertCaret(page, "[P05]", 0);
  });

  /**
   * Walks the caret one line at a time and returns the largest single-step scroll movement,
   * together with the height of the visible area it happened in. Stops as soon as the caret has
   * entered `until`, so the walk is guaranteed to have covered the line break.
   */
  async function largestScrollJumpWhileWalking(page: Page, key: "ArrowDown" | "ArrowUp", until: string) {
    const scrollTop = () => page.locator(".editor").evaluate((element) => element.parentElement?.scrollTop ?? -1);
    const focusedText = () => page.evaluate(() => document.activeElement?.textContent ?? "");
    const containerHeight = await page
      .locator(".editor")
      .evaluate((element) => element.parentElement?.clientHeight ?? -1);

    let largestJump = 0;
    let largestJumpStep = -1;
    let reachedTarget = false;
    // Generous upper bound; the walk normally ends much earlier, when the caret reaches `until`.
    const maxSteps = 60;

    for (let step = 0; step < maxSteps && !reachedTarget; step++) {
      const before = await scrollTop();
      await page.keyboard.press(key);
      await page.waitForTimeout(50);
      const jump = Math.abs((await scrollTop()) - before);

      if (jump > largestJump) {
        largestJump = jump;
        largestJumpStep = step;
      }
      reachedTarget = (await focusedText()) === until;
    }
    return { largestJump, largestJumpStep, containerHeight, reachedTarget };
  }

  // Moving the caret one line must never scroll more than a small fraction of the visible area.
  // The reported bug scrolls roughly half the visible height when the caret crosses the <br>,
  // because the browser re-centres the caret instead of following the edge line by line.
  for (const { key, startParagraph, until } of [
    { key: "ArrowDown" as const, startParagraph: 37, until: afterNewLine },
    { key: "ArrowUp" as const, startParagraph: 44, until: beforeNewLine },
  ]) {
    test(`${key} past a manual line break must not jump half a page`, async ({ page }) => {
      await page.setViewportSize({ width: 1200, height: 600 });
      await insertManualLineBreak(page, textWithManualLineBreak, beforeNewLine.length);

      await page.getByText(paragraphText(startParagraph), { exact: true }).click();

      const { largestJump, largestJumpStep, containerHeight, reachedTarget } = await largestScrollJumpWhileWalking(
        page,
        key,
        until,
      );

      // Without this the test could pass by never reaching the line break at all.
      expect(reachedTarget, `the ${key} walk never crossed the line break`).toBe(true);

      expect(
        largestJump,
        `largest ${key} scroll jump was ${largestJump}px at step ${largestJumpStep} of a ${containerHeight}px tall viewport`,
      ).toBeLessThan(containerHeight / 4);
    });
  }
});
