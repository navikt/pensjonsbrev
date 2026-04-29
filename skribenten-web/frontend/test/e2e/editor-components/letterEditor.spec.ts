import { expect, type Page, test } from "@playwright/test";

import { type EditedLetter, type LiteralValue } from "~/types/brevbakerTypes";

import { setupSakStubs } from "../utils/helpers";

const exampleLetter1 = {
  title: {
    text: [
      {
        id: 1,
        parentId: null,
        type: "LITERAL",
        text: "Informasjon om saksbehandlingstiden vår",
        editedText: null,
        fontType: "PLAIN",
        editedFontType: null,
        tags: [],
      },
    ],
    deletedContent: [],
  },
  sakspart: {
    gjelderNavn: "Test Testeson",
    gjelderFoedselsnummer: "12345678910",
    saksnummer: "1234",
    dokumentDato: "2024-03-15",
  },
  blocks: [
    {
      id: 1,
      parentId: null,
      editable: true,
      content: [
        {
          id: 11,
          parentId: 1,
          text: "Denne blokken[CP1-1] ",
          type: "LITERAL",
          fontType: "PLAIN",
          editedFontType: null,
          tags: [],
        },
        {
          id: 12,
          parentId: 1,
          text: "VARIABLE-MED-LITT-LENGDE",
          type: "VARIABLE",
          fontType: "PLAIN",
        },
        {
          id: 13,
          parentId: 1,
          text: "Er laget for å teste piltaster vertikalt i samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at markøren beveger seg til nærmeste side av variabelen.",
          type: "LITERAL",
          fontType: "PLAIN",
          editedFontType: null,
          tags: [],
        },
      ],
      deletedContent: [],
      type: "PARAGRAPH",
    },
    {
      id: 2,
      parentId: null,
      editable: true,
      content: [
        {
          id: 21,
          parentId: 2,
          text: "[CP2-1]Her vil vi teste at piltast opp/ned",
          type: "LITERAL",
          fontType: "PLAIN",
          editedFontType: null,
          tags: [],
        },
        {
          id: 22,
          parentId: 2,
          text: "ØVRE-VARIABLE",
          type: "VARIABLE",
          fontType: "PLAIN",
        },
        {
          id: 23,
          parentId: 2,
          text: "funker mellom avsnitt",
          type: "LITERAL",
          fontType: "PLAIN",
          editedFontType: null,
          tags: [],
        },
      ],
      deletedContent: [],
      type: "PARAGRAPH",
    },
    {
      id: 3,
      parentId: null,
      editable: true,
      content: [
        {
          id: 31,
          parentId: 3,
          text: "[CP2-2]Her vil vi",
          type: "LITERAL",
          fontType: "PLAIN",
          editedFontType: null,
          tags: [],
        },
        {
          id: 32,
          parentId: 3,
          text: "NEDRE-VARIABLE",
          type: "VARIABLE",
          fontType: "PLAIN",
        },
        {
          id: 33,
          parentId: 3,
          text: " teste at pil opp/ned krysser avsnitt [CP2-3]",
          type: "LITERAL",
          fontType: "PLAIN",
          editedFontType: null,
          tags: [],
        },
      ],
      deletedContent: [],
      type: "PARAGRAPH",
    },
    {
      id: 4,
      parentId: null,
      editable: true,
      content: [
        {
          id: 41,
          parentId: 4,
          text: "Tittel over punktliste",
          type: "LITERAL",
          fontType: "PLAIN",
          editedFontType: null,
          tags: [],
        },
      ],
      deletedContent: [],
      type: "TITLE1",
    },
    {
      id: 5,
      parentId: null,
      editable: true,
      content: [
        {
          id: 51,
          parentId: 5,
          items: [
            {
              id: 511,
              parentId: 51,
              content: [
                {
                  id: 5111,
                  parentId: 511,
                  text: "Punkt 1. Dette er et veldig langt punkt kun av den grunn at vi ønsker helst, mest sannsynlig, at denne skal brekke over to linjer[CP3-1]",
                  type: "LITERAL",
                  fontType: "PLAIN",
                  editedFontType: null,
                  tags: [],
                },
              ],
            },
            {
              id: 512,
              parentId: 51,
              content: [
                {
                  id: 5121,
                  parentId: 512,
                  text: "Punkt 2. Migrere brev[CP3-2]",
                  type: "LITERAL",
                  fontType: "PLAIN",
                  editedFontType: null,
                  tags: [],
                },
              ],
            },
            {
              id: 513,
              parentId: 51,
              content: [
                {
                  id: -5131,
                  parentId: 513,
                  text: "Punkt 3. Øk budsjettet[CP3-3]",
                  type: "LITERAL",
                  fontType: "PLAIN",
                  editedFontType: null,
                  tags: [],
                },
              ],
            },
          ],
          type: "ITEM_LIST",
        },
      ],
      deletedContent: [],
      type: "PARAGRAPH",
    },
    {
      id: 6,
      parentId: null,
      editable: true,
      content: [
        {
          id: 61,
          parentId: 6,
          text: "Tittel under punktliste",
          type: "LITERAL",
          fontType: "PLAIN",
          editedFontType: null,
          tags: [],
        },
      ],
      deletedContent: [],
      type: "TITLE2",
    },
    {
      id: 7,
      parentId: null,
      editable: true,
      content: [
        {
          id: 71,
          parentId: 7,
          text: "Siste avsnitt for å teste at pil ned tar oss til end of line.[CP4-1]",
          type: "LITERAL",
          fontType: "PLAIN",
          editedFontType: null,
          tags: [],
        },
      ],
      deletedContent: [],
      type: "PARAGRAPH",
    },
  ],
  signatur: {
    hilsenTekst: "Med vennlig hilsen",
    saksbehandlerRolleTekst: "Saksbehandler",
    saksbehandlerNavn: "Ole Saksbehandler",
    attesterendeSaksbehandlerNavn: "",
    navAvsenderEnhet: "Nav Familie- og pensjonsytelser Porsgrunn",
  },
  deletedBlocks: [],
} as EditedLetter;

function makeBrevResponse(redigertBrev: object) {
  return {
    info: {
      id: 1,
      brevkode: "BREV1",
      brevtittel: "Brev 1",
      opprettet: "2024-01-01",
      sistredigert: "2024-01-01",
      sistredigertAv: { id: "Z123", navn: "Z entotre" },
      opprettetAv: { id: "Z123", navn: "Z entotre" },
      status: {
        type: "UnderRedigering",
        redigeresAv: { id: "Z123", navn: "Z entotre" },
      },
      distribusjonstype: "SENTRALPRINT",
      mottaker: null,
      avsenderEnhet: {
        enhetNr: "0001",
        navn: "NAV Familie- og pensjonsytelser",
      },
      spraak: "NB",
      journalpostId: null,
      vedtaksId: null,
      brevtype: "INFORMASJONSBREV",
      saksId: "22981081",
    },
    redigertBrev,
    redigertBrevHash: "hash1",
    saksbehandlerValg: {},
    propertyUsage: null,
    valgteVedlegg: null,
  };
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
  const before = process.env.IS_UI_MODE ? await getCaretDebugInfo(page) : null;
  for (let i = 0; i < times; i++) {
    await page.keyboard.press(key);
  }
  const after = process.env.IS_UI_MODE ? await getCaretDebugInfo(page) : null;
  if (process.env.IS_UI_MODE) console.info(`${key} x${times}`, "\nbefore", before, "\nafter", after);
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

  if (process.env.IS_UI_MODE) {
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
      await move(page, "ArrowRight", 25);
      await assertCaret(page, "[CP2-1]", 25, { expectExact: true });
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
      // Start in "funker mellom avsnitt" on the CP2-1 line — this text is positioned
      // to the RIGHT of the NEDRE-VARIABLE midpoint on the CP2-2 line below
      await page.getByText("funker mellom avsnitt").click({ position: { x: 0, y: 0 } });
      await move(page, "ArrowRight", 5);
      await assertCaret(page, "funker mellom avsnitt", 5, { expectExact: true });
      await move(page, "ArrowDown", 1);
      // Caret should land in [CP2-3] (the text to the RIGHT of NEDRE-VARIABLE)
      await assertCaret(page, "[CP2-3]", 29);
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

      // Helper: inject a focus via React fiber internals
      const setFocus = async (focus: { blockIndex: number; contentIndex: number; cursorPosition: number }) => {
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
      };

      // First, set a valid cursor position and verify it takes effect
      await setFocus({ blockIndex: 0, contentIndex: 2, cursorPosition: validCursorPosition });

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
      await setFocus({ blockIndex: 0, contentIndex: 2, cursorPosition: invalidCursorPosition });
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
