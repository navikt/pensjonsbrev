import { expect, type Page, test } from "@playwright/test";

import { type EditedLetter } from "~/types/brevbakerTypes";
import { baseSakspart, baseSignatur, editorInfo } from "~test/e2e/support/editorFixtures";
import { setupSakStubs } from "~test/e2e/support/helpers";
import { brevResponse, editedLetter, signatur } from "~test/support/brevFixtures";
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
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av variable.",
        ),
      ],
    }),
    paragraph({
      id: 2,
      content: [
        makeLiteral(21, 2, "[CP2-1]Her vil vi teste om piltast opp/ned"),
        makeVariable(22, 2, "ØVRE-VARIABLE"),
        makeLiteral(23, 2, "fungerer mellom egne avsnitt"),
      ],
    }),
    paragraph({
      id: 3,
      content: [
        makeLiteral(31, 3, "[CP2-2]Her vil vi"),
        makeVariable(32, 3, "NEDRE-VARIABLE"),
        makeLiteral(33, 3, " teste om pil opp/ned fungerer mellom avsnitt [CP2-3]"),
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
                  content: [makeLiteral(-5131, 513, "Punkt 3. Øk budjsettet[CP3-3]")],
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
        await page.getByTestId("fonttype-bold-button").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");

        // Re-select and unbold
        await selectFullElement(page, "teste piltast");
        await page.getByTestId("fonttype-bold-button").click();
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
        await page.getByTestId("fonttype-italic-button").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "italic");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Italic off with toolbar
        await selectFullElement(page, "teste piltast");
        await page.getByTestId("fonttype-italic-button").click();
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
        await page.getByTestId("fonttype-bold-button").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "normal");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Switch to italic (removes bold, applies italic)
        await selectFullElement(page, "teste piltast");
        await page.getByTestId("fonttype-italic-button").click();
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
        const redigertBrev = editedLetter({
          title: {
            text: [makeLiteral(1, null, "Title")],
            deletedContent: [],
          },
          sakspart: {
            gjelderNavn: "Test",
            gjelderFoedselsnummer: "12345678910",
            saksnummer: "1234",
            dokumentDato: "2024-03-15",
          },
          blocks: [
            paragraph({
              id: 1,
              content: [
                makeLiteral(11, 1, "Jeg er en literal,"),
                makeLiteral(12, 1, " som blir fulgt opp av en annen literal,"),
                makeLiteral(13, 1, " og til slutt, en siste literal"),
              ],
            }),
          ],
          signatur: signatur({
            hilsenTekst: "Med vennlig hilsen",
            saksbehandlerNavn: "Ole Saksbehandler",
            attesterendeSaksbehandlerNavn: "",
            navAvsenderEnhet: "Nav",
          }),
        });
        const brevResponse = makeBrevResponse(redigertBrev);
        await setupBrevRoute(page, brevResponse);
        await navigateToEditor(page);

        const fullText = "Jeg er en literal, som blir fulgt opp av en annen literal, og til slutt, en siste literal";
        await expect(page.getByText(fullText)).toBeVisible();
        await page.getByText(fullText).click();
        await page.getByTestId("bullet-list-button").click();
        await page.getByTestId("fonttype-bold-button").click();
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
        await page.getByTestId("fonttype-bold-button").click();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(4);

        const mainText =
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av";
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "700");

        // Unbold by clicking on bold element and toggling
        await page.getByText("variable.").click();
        await page.getByTestId("fonttype-bold-button").click();
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

        await page.getByTestId("fonttype-italic-button").click();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(4);

        const mainText =
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av";
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "italic");

        await page.getByText("variable.").click();
        await page.getByTestId("fonttype-italic-button").click();
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

        await page.getByTestId("fonttype-bold-button").click();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(4);

        const mainText =
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av";
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "normal");

        // Switch to italic
        await page.getByText("variable.").click();
        await page.getByTestId("fonttype-italic-button").click();
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
      await page.getByTestId("fonttype-bold-button").click();

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");
      await page.getByTestId("fonttype-bold-button").click();
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
      await page.getByTestId("fonttype-italic-button").click();

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-style", "normal");
      await page.getByTestId("fonttype-italic-button").click();
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
      await page.getByTestId("fonttype-bold-button").click();

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-style", "normal");
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");

      await page.getByTestId("fonttype-italic-button").click();
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

      await page.getByTestId("fonttype-bold-button").click();

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");
    });

    test("endring av fonttype av en variable inne i et punkt skal bevare all content i punkten", async ({ page }) => {
      const redigertBrev = editedLetter();
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

      await page.getByTestId("bullet-list-button").click();
      await page.getByTestId("fonttype-bold-button").click();
      await expect(page.getByText(fullText)).toBeVisible();
    });
  });
});

test.describe("Switch font type i tabell", () => {
  const headerText = "Overskrift med kursiv";
  const bodyText = "Celle med fet tekst";
  const otherParagraphText = "Avsnitt etter tabellen";

  const tableLetter = editedLetter({
    title: { text: [makeLiteral(1, null, "Tabellbrev")], deletedContent: [] },
    sakspart: baseSakspart(),
    blocks: [
      paragraph({
        id: 1,
        content: [
          table([cell(makeLiteral(100, null, headerText))], [row(cell(makeLiteral(101, null, bodyText)))]),
          makeLiteral(102, 1, otherParagraphText),
        ],
      }),
    ],
    signatur: baseSignatur(),
  });

  test.beforeEach(async ({ page }) => {
    await setupBrevRoute(page, makeBrevResponse(tableLetter));
    await navigateToEditor(page);
    await expect(page.getByTestId("letter-table")).toBeVisible();
  });

  test("markert tekst i en celle gjøres fet uten å endre resten av cellen", async ({ page }) => {
    await selectTextInElement(page, bodyText, "fet");
    await page.getByTestId("fonttype-bold-button").click();

    const tableCell = page.getByTestId("table-cell-0-0");
    await expect(tableCell.getByText("Celle med", { exact: true })).toHaveCSS("font-weight", "400");
    await expect(tableCell.getByText("fet", { exact: true })).toHaveCSS("font-weight", "700");
    await expect(tableCell.getByText("tekst", { exact: true })).toHaveCSS("font-weight", "400");

    // Markeringen beholdes, slik at fonttypen kan endres på nytt uten å markere teksten igjen
    await expect.poll(() => page.evaluate(() => document.getSelection()?.toString())).toBe("fet");
    await page.getByTestId("fonttype-italic-button").click();
    await expect(tableCell.getByText("fet", { exact: true })).toHaveCSS("font-style", "italic");
    await expect(tableCell.getByText("fet", { exact: true })).toHaveCSS("font-weight", "400");
    await expect.poll(() => page.evaluate(() => document.getSelection()?.toString())).toBe("fet");
  });

  test("uten markering gjøres kun ordet under markøren fet", async ({ page }) => {
    await placeCaretBefore(page, bodyText, "et tekst");
    await page.keyboard.press("Control+f");

    const tableCell = page.getByTestId("table-cell-0-0");
    await expect(tableCell.getByText("fet", { exact: true })).toHaveCSS("font-weight", "700");
    await expect(tableCell.getByText("Celle med", { exact: true })).toHaveCSS("font-weight", "400");
  });

  test("i tabelloverskrift er fet deaktivert og kursiv gjelder kun markert tekst", async ({ page }) => {
    await selectTextInElement(page, headerText, "kursiv");
    await expect(page.getByTestId("fonttype-bold-button")).toBeDisabled();
    await page.getByTestId("fonttype-italic-button").click();

    const header = page.getByTestId("table-header-0");
    await expect(header.getByText("kursiv", { exact: true })).toHaveCSS("font-style", "italic");
    await expect(header.getByText("Overskrift med", { exact: true })).toHaveCSS("font-style", "normal");

    await selectTextInElement(page, "Overskrift med", "med");
    await page.keyboard.press("Control+f");
    await expect(header.getByText("Overskrift med", { exact: true })).toHaveCSS("font-style", "normal");
    await expect(page.getByTestId("fonttype-bold-button")).toBeDisabled();
  });

  test("listeknapper er deaktivert i tabellceller", async ({ page }) => {
    await selectTextInElement(page, headerText, "Overskrift");
    await expect(page.getByTestId("bullet-list-button")).toBeDisabled();
    await expect(page.getByTestId("number-list-button")).toBeDisabled();

    await placeCaretBefore(page, bodyText, "Celle");
    await expect(page.getByTestId("bullet-list-button")).toBeDisabled();
    await expect(page.getByTestId("number-list-button")).toBeDisabled();
    await expect(page.getByTestId("fonttype-bold-button")).toBeEnabled();

    await placeCaretBefore(page, otherParagraphText, "Avsnitt");
    await expect(page.getByTestId("bullet-list-button")).toBeEnabled();
    await expect(page.getByTestId("number-list-button")).toBeEnabled();
  });
});

test.describe("Markering etter endring av fonttype", () => {
  test("markert tekst i et avsnitt forblir markert og kan endres videre", async ({ page }) => {
    await setupBrevRoute(page, makeBrevResponse(exampleLetter1));
    await navigateToEditor(page);

    await selectTextInElement(page, longLiteralText, "teste piltast");
    await page.getByTestId("fonttype-bold-button").click();
    await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "700");
    await expect.poll(() => page.evaluate(() => document.getSelection()?.toString())).toBe("teste piltast");

    await page.keyboard.press("Control+i");
    await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "italic");
    await expect.poll(() => page.evaluate(() => document.getSelection()?.toString())).toBe("teste piltast");

    // Skriving erstatter markeringen som vanlig
    await page.keyboard.type("X");
    await expect(page.getByText("X", { exact: true })).toBeVisible();
    await expect(page.getByText("teste piltast")).toHaveCount(0);
  });
});

test.describe("Markør og fokus etter endring av fonttype", () => {
  const caretState = (page: Page) =>
    page.evaluate(() => {
      const selection = document.getSelection();
      const active = document.activeElement as HTMLElement | null;
      return {
        editorFocused: active?.isContentEditable === true && active.contains(selection?.anchorNode ?? null),
        collapsed: selection?.isCollapsed,
        text: selection?.anchorNode?.textContent,
        offset: selection?.anchorOffset,
      };
    });

  test.beforeEach(async ({ page }) => {
    await setupBrevRoute(page, makeBrevResponse(exampleLetter1));
    await navigateToEditor(page);
  });

  test("markøren blir stående og editoren beholder fokus ved gjentatte klikk", async ({ page }) => {
    await placeCaretBefore(page, longLiteralText, "ste piltast");

    await page.getByTestId("fonttype-bold-button").click();
    await expect(page.getByText("teste", { exact: true })).toHaveCSS("font-weight", "700");
    await expect
      .poll(() => caretState(page))
      .toEqual({ editorFocused: true, collapsed: true, text: "teste", offset: 2 });

    // Ordet er nå en egen literal – fokus skal likevel tilbake til editoren
    await page.getByTestId("fonttype-italic-button").click();
    await expect(page.getByText("teste", { exact: true })).toHaveCSS("font-style", "italic");
    await expect
      .poll(() => caretState(page))
      .toEqual({ editorFocused: true, collapsed: true, text: "teste", offset: 2 });

    await page.keyboard.type("X");
    await expect(page.getByText("teXste", { exact: true })).toBeVisible();
  });

  test("markør på mellomrom gir ingen endring, men editoren beholder fokus", async ({ page }) => {
    await placeCaretBefore(page, longLiteralText, " teste piltast");

    await page.getByTestId("fonttype-bold-button").click();
    await expect
      .poll(() => caretState(page))
      .toEqual({ editorFocused: true, collapsed: true, text: expect.stringContaining("Er laget for å"), offset: 14 });
  });

  test("tastaturaktivering av knappen gir fokus tilbake til editoren", async ({ page }) => {
    await placeCaretBefore(page, longLiteralText, "ste piltast");
    await page.getByTestId("fonttype-bold-button").focus();

    await page.keyboard.press("Enter");
    await expect(page.getByText("teste", { exact: true })).toHaveCSS("font-weight", "700");
    await expect
      .poll(() => caretState(page))
      .toEqual({ editorFocused: true, collapsed: true, text: "teste", offset: 2 });
  });

  test("markering beholdes ved klikk på knappene", async ({ page }) => {
    await selectTextInElement(page, longLiteralText, "teste piltast");

    for (const button of ["fonttype-bold-button", "fonttype-italic-button", "fonttype-bold-button"]) {
      await page.getByTestId(button).click();
      await expect.poll(() => page.evaluate(() => document.getSelection()?.toString())).toBe("teste piltast");
    }
    await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "normal");
    await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "700");
  });
});

test.describe("Verktøylinje i overskrifter", () => {
  test("fet, kursiv, liste- og tabellknapper er deaktivert i overskrifter", async ({ page }) => {
    await setupBrevRoute(page, makeBrevResponse(exampleLetter1));
    await navigateToEditor(page);

    for (const title of ["Tittel over punktliste", "Tittel under punktliste"]) {
      await placeCaretBefore(page, title, "punktliste");
      await expect(page.getByTestId("fonttype-bold-button")).toBeDisabled();
      await expect(page.getByTestId("fonttype-italic-button")).toBeDisabled();
      await expect(page.getByTestId("bullet-list-button")).toBeDisabled();
      await expect(page.getByTestId("number-list-button")).toBeDisabled();
      await expect(page.getByTestId("toolbar-table-button")).toBeDisabled();
    }

    await placeCaretBefore(page, longLiteralText, "piltast");
    await expect(page.getByTestId("fonttype-bold-button")).toBeEnabled();
    await expect(page.getByTestId("fonttype-italic-button")).toBeEnabled();
    await expect(page.getByTestId("bullet-list-button")).toBeEnabled();
    await expect(page.getByTestId("number-list-button")).toBeEnabled();
    await expect(page.getByTestId("toolbar-table-button")).toBeEnabled();
  });
});

/** Finner skjermkoordinatene til tegnet ved `offset` i tekstnoden som har nøyaktig teksten `nodeText`. */
async function charPosition(page: Page, nodeText: string, offset: number) {
  return page.evaluate(
    ({ nodeText, offset }) => {
      const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null);
      while (walker.nextNode()) {
        const node = walker.currentNode as Text;
        if (node.data === nodeText) {
          const range = document.createRange();
          range.setStart(node, Math.min(offset, node.length - 1));
          range.setEnd(node, Math.min(offset, node.length - 1) + 1);
          const rect = range.getBoundingClientRect();
          const x = offset >= node.length ? rect.right : rect.left;
          return { x: x + (offset >= node.length ? -1 : 1), y: rect.top + rect.height / 2 };
        }
      }
      throw new Error(`Could not find text node "${nodeText}"`);
    },
    { nodeText, offset },
  );
}

/** Markerer med musa fra `from` til `to`, slik at markeringen kan krysse flere literals. */
async function dragSelect(page: Page, from: { text: string; offset: number }, to: { text: string; offset: number }) {
  const start = await charPosition(page, from.text, from.offset);
  const end = await charPosition(page, to.text, to.offset);
  await page.mouse.move(start.x, start.y);
  await page.mouse.down();
  await page.mouse.move(end.x, end.y, { steps: 10 });
  await page.mouse.up();
}

const selectedText = (page: Page) => page.evaluate(() => document.getSelection()?.toString());

test.describe("Markering over flere literals", () => {
  const cellFirst = "Første del ";
  const cellSecond = "andre del";
  const otherCell = "Annen celle";
  const paragraphFirst = "Avsnitt start ";
  const paragraphSecond = "avsnitt slutt";

  const multiLiteralLetter = editedLetter({
    title: { text: [makeLiteral(1, null, "Brev med flere literals")], deletedContent: [] },
    sakspart: baseSakspart(),
    blocks: [
      paragraph({
        id: 1,
        content: [
          table(
            [cell(makeLiteral(100, null, "Overskrift"))],
            [
              row(cell(makeLiteral(101, null, cellFirst), makeLiteral(102, null, cellSecond))),
              row(cell(makeLiteral(103, null, otherCell))),
            ],
          ),
          makeLiteral(104, 1, "Mellom"),
        ],
      }),
      paragraph({
        id: 2,
        content: [makeLiteral(201, 2, paragraphFirst), makeLiteral(202, 2, paragraphSecond)],
      }),
    ],
    signatur: baseSignatur(),
  });

  test.beforeEach(async ({ page }) => {
    await setupBrevRoute(page, makeBrevResponse(multiLiteralLetter));
    await navigateToEditor(page);
    await expect(page.getByTestId("letter-table")).toBeVisible();
  });

  test("markering over to literals i en celle gjøres fet og forblir markert", async ({ page }) => {
    await dragSelect(page, { text: cellFirst, offset: 7 }, { text: cellSecond, offset: 5 });
    await expect.poll(() => selectedText(page)).toBe("del andre");

    await page.getByTestId("fonttype-bold-button").click();

    const tableCell = page.getByTestId("table-cell-0-0");
    await expect(tableCell.getByText("Første", { exact: true })).toHaveCSS("font-weight", "400");
    await expect(tableCell.getByText("del", { exact: true }).first()).toHaveCSS("font-weight", "700");
    await expect(tableCell.getByText("andre", { exact: true })).toHaveCSS("font-weight", "700");
    await expect(tableCell.getByText("del", { exact: true }).last()).toHaveCSS("font-weight", "400");
    await expect.poll(() => selectedText(page)).toBe("del andre");

    // Samme knapp igjen gjør hele markeringen normal
    await page.getByTestId("fonttype-bold-button").click();
    await expect(tableCell.getByText("andre", { exact: true })).toHaveCSS("font-weight", "400");
    await expect(tableCell.getByText("del", { exact: true }).first()).toHaveCSS("font-weight", "400");
    await expect.poll(() => selectedText(page)).toBe("del andre");
  });

  test("markering over flere avsnitt gir ingen endring, og markeringen beholdes", async ({ page }) => {
    await dragSelect(page, { text: "Mellom", offset: 2 }, { text: paragraphFirst, offset: 7 });
    await expect.poll(() => selectedText(page)).toMatch(/^llom\s+Avsnitt$/);
    const before = await selectedText(page);

    await page.getByTestId("fonttype-bold-button").click();

    await expect(page.getByText("Mellom", { exact: true })).toHaveCSS("font-weight", "400");
    await expect(page.getByText(paragraphFirst, { exact: true })).toHaveCSS("font-weight", "400");
    expect(await selectedText(page)).toBe(before);
  });

  test("markering over to literals i et avsnitt gjøres kursiv og forblir markert", async ({ page }) => {
    await dragSelect(page, { text: paragraphFirst, offset: 8 }, { text: paragraphSecond, offset: 7 });
    await expect.poll(() => selectedText(page)).toBe("start avsnitt");

    await page.keyboard.press("Control+i");

    await expect(page.getByText("start", { exact: true })).toHaveCSS("font-style", "italic");
    await expect(page.getByText("avsnitt", { exact: true })).toHaveCSS("font-style", "italic");
    await expect(page.getByText("Avsnitt", { exact: true })).toHaveCSS("font-style", "normal");
    await expect(page.getByText("slutt", { exact: true })).toHaveCSS("font-style", "normal");
    await expect.poll(() => selectedText(page)).toBe("start avsnitt");
  });

  test("skriving over markering i flere literals i en celle erstatter hele markeringen", async ({ page }) => {
    await dragSelect(page, { text: cellFirst, offset: 7 }, { text: cellSecond, offset: 5 });
    await expect.poll(() => selectedText(page)).toBe("del andre");

    await page.keyboard.type("ny");

    await expect(page.getByTestId("table-cell-0-0")).toHaveText("Første ny del");
  });

  for (const key of ["Space", "Enter", "Backspace"]) {
    test(`${key} på fokusert fet-knapp sletter ikke markering over flere literals`, async ({ page }) => {
      await dragSelect(page, { text: cellFirst, offset: 7 }, { text: cellSecond, offset: 5 });
      await expect.poll(() => selectedText(page)).toBe("del andre");

      await page.getByTestId("fonttype-bold-button").focus();
      await page.keyboard.press(key);

      const tableCell = page.getByTestId("table-cell-0-0");
      await expect(tableCell).toHaveText("Første del andre del");
      if (key !== "Backspace") {
        await expect(tableCell.getByText("andre", { exact: true })).toHaveCSS("font-weight", "700");
      }
    });
  }

  test("Enter over markering i flere literals sletter markeringen og deler avsnittet", async ({ page }) => {
    await dragSelect(page, { text: paragraphFirst, offset: 8 }, { text: paragraphSecond, offset: 7 });
    await expect.poll(() => selectedText(page)).toBe("start avsnitt");

    await page.keyboard.press("Enter");

    await expect(page.locator(".PARAGRAPH").nth(1)).toHaveText(/^Avsnitt\s*$/);
    await expect(page.locator(".PARAGRAPH").nth(2)).toHaveText(/^\s*slutt$/);
  });

  test("etter markering over flere literals kan man skrive videre og klikke andre steder", async ({ page }) => {
    await dragSelect(page, { text: paragraphFirst, offset: 8 }, { text: paragraphSecond, offset: 7 });
    await expect.poll(() => selectedText(page)).toBe("start avsnitt");
    await page.getByTestId("fonttype-bold-button").click();
    await expect(page.getByText("avsnitt", { exact: true })).toHaveCSS("font-weight", "700");

    // Skriving erstatter hele markeringen og gjør teksten redigerbar igjen
    await page.keyboard.type("XY");
    await expect(page.locator("[data-editor-root][data-unified]")).toHaveCount(0);
    // Som ved Delete fulgt av skriving kan ett av de to mellomrommene forsvinne (eksisterende whitespace-oppførsel)
    await expect(page.locator(".PARAGRAPH").nth(1)).toHaveText(/^Avsnitt XY ?slutt$/);

    // Klikk et annet sted gir vanlig markør som man kan skrive ved
    await dragSelect(page, { text: cellFirst, offset: 0 }, { text: cellSecond, offset: 3 });
    await expect.poll(() => selectedText(page)).toBe("Første del and");
    await page.getByText("Mellom", { exact: true }).click();
    await page.keyboard.press("End");
    await page.keyboard.type("Y");
    await expect(page.getByText("MellomY", { exact: true })).toBeVisible();
  });
});
