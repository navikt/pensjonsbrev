import { expect, type Page, test } from "@playwright/test";

import { dataE2E, setupSakStubs } from "../utils/helpers";

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
          text: "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av variable.",
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
          text: "[CP2-1]Her vil vi teste om piltast opp/ned",
          type: "LITERAL",
          fontType: "PLAIN",
          editedFontType: null,
          tags: [],
        },
        { id: 22, parentId: 2, text: "ØVRE-VARIABLE", type: "VARIABLE", fontType: "PLAIN" },
        {
          id: 23,
          parentId: 2,
          text: "fungerer mellom egne avsnitt",
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
        { id: 32, parentId: 3, text: "NEDRE-VARIABLE", type: "VARIABLE", fontType: "PLAIN" },
        {
          id: 33,
          parentId: 3,
          text: " teste om pil opp/ned fungerer mellom avsnitt [CP2-3]",
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
                  text: "Punkt 3. Øk budjsettet[CP3-3]",
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
        { id: 61, text: "Tittel under punktliste", type: "LITERAL", fontType: "PLAIN", editedFontType: null, tags: [] },
      ],
      deletedContent: [],
      type: "TITLE2",
    },
    {
      id: 7,
      editable: true,
      content: [
        {
          id: 71,
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
};

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
      status: { type: "UnderRedigering", redigeresAv: { id: "Z123", navn: "Z entotre" } },
      distribusjonstype: "SENTRALPRINT",
      mottaker: null,
      avsenderEnhet: { enhetNr: "0001", navn: "NAV Familie- og pensjonsytelser" },
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

  await page.route("**/bff/skribenten-backend/sak/123456/brev/1", (route) => {
    if (route.request().method() === "PUT" || route.request().method() === "PATCH") {
      return route.fulfill({ json: brevResponse, contentType: "application/json" });
    }
    return route.fallback();
  });
}

async function navigateToEditor(page: Page) {
  await page.goto("/saksnummer/123456/brev/1");
  await page.locator(".editor").waitFor({ state: "visible", timeout: 15_000 });
  await page.evaluate(() => document.fonts.ready);
}

/**
 * Programmatically selects text within an element found by text content.
 * @param targetSubstring The substring to select within the element
 * @param elementText Text used to locate the element (via getByText)
 */
async function selectTextInElement(page: Page, elementText: string, targetSubstring: string) {
  await page.evaluate(
    ({ elementText, targetSubstring }) => {
      // Find the element containing the text
      const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null);
      let node: Text | null = null;
      while (walker.nextNode()) {
        const textNode = walker.currentNode as Text;
        if (textNode.data.includes(elementText)) {
          node = textNode;
          break;
        }
      }
      if (!node) throw new Error(`Could not find text node containing "${elementText}"`);

      const fullText = node.data;
      const start = fullText.indexOf(targetSubstring);
      if (start === -1) throw new Error(`"${targetSubstring}" not found in "${fullText}"`);

      const range = document.createRange();
      range.setStart(node, start);
      range.setEnd(node, start + targetSubstring.length);
      const selection = globalThis.getSelection()!;
      selection.removeAllRanges();
      selection.addRange(range);
      (node.parentElement as HTMLElement)?.focus();
    },
    { elementText, targetSubstring },
  );
}

/**
 * Selects the full text of an element found by getByText
 */
async function selectFullElement(page: Page, text: string) {
  await page.evaluate((text) => {
    const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null);
    let node: Text | null = null;
    while (walker.nextNode()) {
      const textNode = walker.currentNode as Text;
      if (textNode.data.includes(text)) {
        node = textNode;
        break;
      }
    }
    if (!node) throw new Error(`Could not find text node containing "${text}"`);

    const range = document.createRange();
    range.setStart(node, 0);
    range.setEnd(node, node.data.length);
    const selection = globalThis.getSelection()!;
    selection.removeAllRanges();
    selection.addRange(range);
    (node.parentElement as HTMLElement)?.focus();
  }, text);
}

/**
 * Places a collapsed caret right before a target substring within a text node.
 * Uses programmatic selection to avoid rendering-dependent dblclick behavior.
 */
async function placeCaretBefore(page: Page, elementText: string, targetSubstring: string) {
  await page.evaluate(
    ({ elementText, targetSubstring }) => {
      const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null);
      let node: Text | null = null;
      while (walker.nextNode()) {
        const textNode = walker.currentNode as Text;
        if (textNode.data.includes(elementText)) {
          node = textNode;
          break;
        }
      }
      if (!node) throw new Error(`Could not find text node containing "${elementText}"`);

      const offset = node.data.indexOf(targetSubstring);
      if (offset === -1) throw new Error(`"${targetSubstring}" not found in "${node.data}"`);

      const range = document.createRange();
      range.setStart(node, offset);
      range.collapse(true);
      const selection = globalThis.getSelection()!;
      selection.removeAllRanges();
      selection.addRange(range);
      (node.parentElement as HTMLElement)?.focus();
    },
    { elementText, targetSubstring },
  );
}

const longLiteralText =
  "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av variable.";
const afterSelectionText =
  "opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av variable.";

test.describe("Switch font type", () => {
  test.describe("literals", () => {
    test.describe("marked text", () => {
      test("handles switching plain/bold/plain", async ({ page }) => {
        const brevResponse = makeBrevResponse(exampleLetter1);
        await setupBrevRoute(page, brevResponse);
        await navigateToEditor(page);

        // Verify initial structure: 3 child nodes in first paragraph
        const firstParagraph = page.locator(".PARAGRAPH").first();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);

        // Select "teste piltast" within the literal
        await selectTextInElement(page, longLiteralText, "teste piltast");

        // Verify selection
        const selectedText = await page.evaluate(() => document.getSelection()?.toString());
        expect(selectedText).toBe("teste piltast");

        // Click bold button
        await dataE2E(page, "fonttype-bold").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");

        // Re-select and unbold
        await selectFullElement(page, "teste piltast");
        await dataE2E(page, "fonttype-bold").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "400");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");

        // With shortcut keys - bold on
        await selectFullElement(page, "teste piltast");
        await page.keyboard.press("Control+f");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");

        // With shortcut keys - bold off
        await selectFullElement(page, "teste piltast");
        await page.keyboard.press("Control+f");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "400");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");

        // Integrity maintained: paragraph should now have 5 child nodes
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(5);
      });

      test("handles switching plain/italic/plain", async ({ page }) => {
        const brevResponse = makeBrevResponse(exampleLetter1);
        await setupBrevRoute(page, brevResponse);
        await navigateToEditor(page);

        const firstParagraph = page.locator(".PARAGRAPH").first();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);

        await selectTextInElement(page, longLiteralText, "teste piltast");
        const selectedText = await page.evaluate(() => document.getSelection()?.toString());
        expect(selectedText).toBe("teste piltast");

        // Italic on with toolbar
        await dataE2E(page, "fonttype-italic").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "italic");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Italic off with toolbar
        await selectFullElement(page, "teste piltast");
        await dataE2E(page, "fonttype-italic").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "normal");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Italic on with shortcut
        await selectFullElement(page, "teste piltast");
        await page.keyboard.press("Control+i");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "italic");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Italic off with shortcut
        await selectFullElement(page, "teste piltast");
        await page.keyboard.press("Control+i");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "normal");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(5);
      });

      test("handles switching plain/bold/italic", async ({ page }) => {
        const brevResponse = makeBrevResponse(exampleLetter1);
        await setupBrevRoute(page, brevResponse);
        await navigateToEditor(page);

        const firstParagraph = page.locator(".PARAGRAPH").first();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);

        await selectTextInElement(page, longLiteralText, "teste piltast");
        const selectedText = await page.evaluate(() => document.getSelection()?.toString());
        expect(selectedText).toBe("teste piltast");

        // Bold on
        await dataE2E(page, "fonttype-bold").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "normal");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Switch to italic (removes bold, applies italic)
        await selectFullElement(page, "teste piltast");
        await dataE2E(page, "fonttype-italic").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "400");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "italic");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Shortcut keys: bold on
        await selectFullElement(page, "teste piltast");
        await page.keyboard.press("Control+f");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "normal");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Shortcut keys: switch to italic
        await selectFullElement(page, "teste piltast");
        await page.keyboard.press("Control+i");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "400");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "italic");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");
      });
    });

    test.describe("unmarked text", () => {
      test("endring av fonttype av en literal inne i et punkt skal bevare all content i punkten", async ({ page }) => {
        const redigertBrev = {
          title: {
            text: [
              {
                id: 1,
                parentId: null,
                type: "LITERAL",
                text: "Title",
                editedText: null,
                fontType: "PLAIN",
                editedFontType: null,
                tags: [],
              },
            ],
            deletedContent: [],
          },
          sakspart: {
            gjelderNavn: "Test",
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
                  type: "LITERAL",
                  text: "Jeg er en literal,",
                  editedText: "Jeg er en literal,",
                  fontType: "PLAIN",
                  editedFontType: null,
                  tags: [],
                },
                {
                  id: 12,
                  parentId: 1,
                  type: "LITERAL",
                  text: " som blir fulgt opp av en annen literal,",
                  editedText: " som blir fulgt opp av en annen literal,",
                  fontType: "PLAIN",
                  editedFontType: null,
                  tags: [],
                },
                {
                  id: 13,
                  parentId: 1,
                  type: "LITERAL",
                  text: " og til slutt, en siste literal",
                  editedText: " og til slutt, en siste literal",
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
            navAvsenderEnhet: "Nav",
          },
          deletedBlocks: [],
        };
        const brevResponse = makeBrevResponse(redigertBrev);
        await setupBrevRoute(page, brevResponse);
        await navigateToEditor(page);

        const fullText = "Jeg er en literal, som blir fulgt opp av en annen literal, og til slutt, en siste literal";
        await expect(page.getByText(fullText)).toBeVisible();
        await page.getByText(fullText).click();
        await dataE2E(page, "editor-bullet-list").click();
        await dataE2E(page, "fonttype-bold").click();
        await expect(page.getByText(fullText)).toBeVisible();
      });

      test("handles switching plain/bold/plain", async ({ page }) => {
        const brevResponse = makeBrevResponse(exampleLetter1);
        await setupBrevRoute(page, brevResponse);
        await navigateToEditor(page);

        const firstParagraph = page.locator(".PARAGRAPH").first();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);

        // Place caret right before "variable." in the third literal
        await placeCaretBefore(page, longLiteralText, "variable.");

        // Click bold
        await dataE2E(page, "fonttype-bold").click();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(4);

        const mainText =
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av";
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "700");

        // Unbold by clicking on bold element and toggling
        await page.getByText("variable.").click();
        await dataE2E(page, "fonttype-bold").click();
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "400");

        // With shortcut
        await page.keyboard.press("Control+f");
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "700");
        await page.keyboard.press("Control+f");
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "400");

        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(4);
      });

      test("handles switching plain/italic/plain", async ({ page }) => {
        const brevResponse = makeBrevResponse(exampleLetter1);
        await setupBrevRoute(page, brevResponse);
        await navigateToEditor(page);

        const firstParagraph = page.locator(".PARAGRAPH").first();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);

        // Place caret right before "variable." in the third literal
        await placeCaretBefore(page, longLiteralText, "variable.");

        await dataE2E(page, "fonttype-italic").click();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(4);

        const mainText =
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av";
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "italic");

        await page.getByText("variable.").click();
        await dataE2E(page, "fonttype-italic").click();
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "normal");

        // With shortcut
        await page.keyboard.press("Control+i");
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "italic");
        await page.keyboard.press("Control+i");
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "normal");

        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(4);
      });

      test("handles switching plain/bold/italic", async ({ page }) => {
        const brevResponse = makeBrevResponse(exampleLetter1);
        await setupBrevRoute(page, brevResponse);
        await navigateToEditor(page);

        const firstParagraph = page.locator(".PARAGRAPH").first();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);

        // Place caret right before "variable." in the third literal
        await placeCaretBefore(page, longLiteralText, "variable.");

        await dataE2E(page, "fonttype-bold").click();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(4);

        const mainText =
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av";
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "normal");

        // Switch to italic
        await page.getByText("variable.").click();
        await dataE2E(page, "fonttype-italic").click();
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "400");
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "italic");

        // Shortcut keys
        await page.keyboard.press("Control+f");
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "normal");

        await page.keyboard.press("Control+i");
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "400");
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "italic");

        await page.keyboard.press("Control+i");
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "400");
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "normal");

        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(4);
      });
    });
  });

  test.describe("variables", () => {
    test("plain/bold/plain", async ({ page }) => {
      const brevResponse = makeBrevResponse(exampleLetter1);
      await setupBrevRoute(page, brevResponse);
      await navigateToEditor(page);

      const firstParagraph = page.locator(".PARAGRAPH").first();
      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);

      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");
      await page.getByText("VARIABLE-MED-LITT-LENGDE").click();
      await dataE2E(page, "fonttype-bold").click();

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");
      await dataE2E(page, "fonttype-bold").click();
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
    });

    test("plain/italic/plain", async ({ page }) => {
      const brevResponse = makeBrevResponse(exampleLetter1);
      await setupBrevRoute(page, brevResponse);
      await navigateToEditor(page);

      const firstParagraph = page.locator(".PARAGRAPH").first();
      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);

      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-style", "normal");
      await page.getByText("VARIABLE-MED-LITT-LENGDE").click();
      await dataE2E(page, "fonttype-italic").click();

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-style", "normal");
      await dataE2E(page, "fonttype-italic").click();
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-style", "normal");

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
    });

    test("plain/bold/italic", async ({ page }) => {
      const brevResponse = makeBrevResponse(exampleLetter1);
      await setupBrevRoute(page, brevResponse);
      await navigateToEditor(page);

      const firstParagraph = page.locator(".PARAGRAPH").first();
      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);

      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-style", "normal");
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");

      await page.getByText("VARIABLE-MED-LITT-LENGDE").click();
      await dataE2E(page, "fonttype-bold").click();

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-style", "normal");
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");

      await dataE2E(page, "fonttype-italic").click();
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-style", "normal");
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
    });

    test("merker deler av en variabel skal endre fonttypen på hele", async ({ page }) => {
      const brevResponse = makeBrevResponse(exampleLetter1);
      await setupBrevRoute(page, brevResponse);
      await navigateToEditor(page);

      const firstParagraph = page.locator(".PARAGRAPH").first();
      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);

      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");

      // Select part of the variable
      await page.getByText("VARIABLE-MED-LITT-LENGDE").click();
      await page.evaluate(() => {
        const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null);
        let node: Text | null = null;
        while (walker.nextNode()) {
          const textNode = walker.currentNode as Text;
          if (textNode.data.includes("VARIABLE-MED-LITT-LENGDE")) {
            node = textNode;
            break;
          }
        }
        if (!node) throw new Error("Variable text node not found");
        const range = document.createRange();
        range.setStart(node, 5);
        range.setEnd(node, 10);
        const selection = globalThis.getSelection()!;
        selection.removeAllRanges();
        selection.addRange(range);
        (node.parentElement as HTMLElement)?.focus();
      });

      await dataE2E(page, "fonttype-bold").click();

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");
    });

    test("endring av fonttype av en variable inne i et punkt skal bevare all content i punkten", async ({ page }) => {
      // Use default nyBrevResponse content
      const redigertBrev = {
        title: {
          text: [
            {
              id: 1,
              parentId: null,
              type: "LITERAL",
              text: "Information about application processing time",
              editedText: null,
              fontType: "PLAIN",
              editedFontType: null,
              tags: [],
            },
          ],
          deletedContent: [],
        },
        sakspart: {
          gjelderNavn: "TRYGG ANBEFALING",
          gjelderFoedselsnummer: "21418744917",
          saksnummer: "22981081",
          dokumentDato: "2024-09-25",
        },
        blocks: [
          {
            id: 272720182,
            parentId: null,
            editable: true,
            content: [
              {
                id: 1507865607,
                parentId: 272720182,
                text: "We received your application for ",
                type: "LITERAL",
                fontType: "PLAIN",
                editedFontType: null,
                tags: [],
              },
              { id: -726051414, parentId: 272720182, text: "alderspensjon", type: "VARIABLE", fontType: "PLAIN" },
              {
                id: -711242333,
                parentId: 272720182,
                text: " from the Norwegian National Insurance Scheme on ",
                type: "LITERAL",
                fontType: "PLAIN",
                editedFontType: null,
                tags: [],
              },
              { id: -694080035, parentId: 272720182, text: "24 July 2024", type: "VARIABLE", fontType: "PLAIN" },
              {
                id: -1114690237,
                parentId: 272720182,
                text: ".",
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
            id: 822540105,
            parentId: null,
            editable: true,
            content: [
              {
                id: -1114690238,
                parentId: 822540105,
                text: "Our processing time for this type of application is usually ",
                type: "LITERAL",
                fontType: "PLAIN",
                editedFontType: null,
                tags: [],
              },
              { id: 1834595758, parentId: 822540105, text: "10", type: "VARIABLE", fontType: "PLAIN" },
              {
                id: 1838606639,
                parentId: 822540105,
                text: " weeks.",
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
          hilsenTekst: "Sincerely",
          saksbehandlerRolleTekst: "Caseworker",
          saksbehandlerNavn: "Sak S. Behandler",
          attesterendeSaksbehandlerNavn: "Attest S. Behandler",
          navAvsenderEnhet: "Nav Arbeid og ytelser Sørlandet",
        },
        deletedBlocks: [],
      };
      const brevResponse = makeBrevResponse(redigertBrev);
      await setupBrevRoute(page, brevResponse);
      await navigateToEditor(page);

      const fullText = "Our processing time for this type of application is usually 10 weeks.";
      await expect(page.getByText(fullText)).toBeVisible();
      await page.getByText(fullText).click();

      // Move left 10 times to position caret
      for (let i = 0; i < 10; i++) {
        await page.keyboard.press("ArrowLeft");
      }

      await dataE2E(page, "editor-bullet-list").click();
      await dataE2E(page, "fonttype-bold").click();
      await expect(page.getByText(fullText)).toBeVisible();
    });
  });
});
