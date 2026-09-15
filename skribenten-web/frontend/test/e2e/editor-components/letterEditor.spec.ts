import { expect, type Page, test } from "@playwright/test";

import { type EditedLetter, type LiteralValue } from "~/types/brevbakerTypes";
import { baseSakspart, baseSignatur, editorInfo } from "~test/e2e/support/editorFixtures";
import { setupSakStubs } from "~test/e2e/support/helpers";
import { brevResponse, editedLetter } from "~test/support/brevFixtures";
import {
  literal as _literal,
  variable as _variable,
  item,
  itemList,
  paragraph,
  title1,
  title2,
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
   * together with the height of the visible area it happened in.
   */
  async function largestScrollJumpWhileWalking(page: Page, key: "ArrowDown" | "ArrowUp", steps: number) {
    const scrollTop = () => page.locator(".editor").evaluate((element) => element.parentElement?.scrollTop ?? -1);
    const containerHeight = await page
      .locator(".editor")
      .evaluate((element) => element.parentElement?.clientHeight ?? -1);

    let largestJump = 0;
    let largestJumpStep = -1;
    for (let step = 0; step < steps; step++) {
      const before = await scrollTop();
      await page.keyboard.press(key);
      await page.waitForTimeout(50);
      const jump = Math.abs((await scrollTop()) - before);

      if (jump > largestJump) {
        largestJump = jump;
        largestJumpStep = step;
      }
    }
    return { largestJump, largestJumpStep, containerHeight };
  }

  // Moving the caret one line must never scroll more than a small fraction of the visible area.
  // The reported bug scrolls roughly half the visible height when the caret crosses the <br>,
  // because the browser re-centres the caret instead of following the edge line by line.
  for (const { key, startParagraph, steps } of [
    { key: "ArrowDown" as const, startParagraph: 37, steps: 20 },
    { key: "ArrowUp" as const, startParagraph: 44, steps: 20 },
  ]) {
    test(`${key} past a manual line break must not jump half a page`, async ({ page }) => {
      await page.setViewportSize({ width: 1200, height: 600 });
      await insertManualLineBreak(page, textWithManualLineBreak, beforeNewLine.length);

      await page.getByText(paragraphText(startParagraph), { exact: true }).click();

      const { largestJump, largestJumpStep, containerHeight } = await largestScrollJumpWhileWalking(page, key, steps);

      expect(
        largestJump,
        `largest ${key} scroll jump was ${largestJump}px at step ${largestJumpStep} of a ${containerHeight}px tall viewport`,
      ).toBeLessThan(containerHeight / 4);
    });
  }
});
