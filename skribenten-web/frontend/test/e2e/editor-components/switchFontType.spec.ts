import { expect, type Page, test } from "@playwright/test";

import { type EditedLetter } from "~/types/brevbakerTypes";
import { baseSakspart, baseSignatur, editorInfo } from "~test/e2e/support/editorFixtures";
import { setupSakStubs } from "~test/e2e/support/helpers";
import { brevResponse, editedLetter, signatur } from "~test/support/brevFixtures";
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
      node.parentElement?.closest<HTMLElement>('[contenteditable="true"]')?.focus();
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
    node.parentElement?.closest<HTMLElement>('[contenteditable="true"]')?.focus();
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
      node.parentElement?.closest<HTMLElement>('[contenteditable="true"]')?.focus();
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
        await selectTextInElement(page, "teste piltast", "teste piltast");
        await page.getByTestId("fonttype-bold-button").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "400");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");

        // With shortcut keys - bold on
        await selectTextInElement(page, "teste piltast", "teste piltast");
        await page.keyboard.press("Control+f");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");

        // With shortcut keys - bold off
        await selectTextInElement(page, "teste piltast", "teste piltast");
        await page.keyboard.press("Control+f");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "400");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");

        // Removing the formatting merges the text back into one literal
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
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
        await selectTextInElement(page, "teste piltast", "teste piltast");
        await page.getByTestId("fonttype-italic-button").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "normal");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Italic on with shortcut
        await selectTextInElement(page, "teste piltast", "teste piltast");
        await page.keyboard.press("Control+i");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "italic");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Italic off with shortcut
        await selectTextInElement(page, "teste piltast", "teste piltast");
        await page.keyboard.press("Control+i");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "normal");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Removing the formatting merges the text back into one literal
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
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

        // Removing the formatting merges the word back into its literal
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
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

        // Removing the formatting merges the word back into its literal
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
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

        // Removing the formatting merges the word back into its literal
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
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
        node.parentElement?.closest<HTMLElement>('[contenteditable="true"]')?.focus();
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

test.describe("Switch font type keeps the selection", () => {
  const sentence = "Noen stoler knirker i rom 4A.";

  test.beforeEach(async ({ page }) => {
    await setupBrevRoute(
      page,
      brevResponse({
        info: editorInfo(),
        redigertBrev: editedLetter({
          sakspart: baseSakspart(),
          blocks: [paragraph({ id: 1, content: [makeLiteral(11, 1, sentence)] })],
          signatur: baseSignatur(),
        }) as EditedLetter,
      }),
    );
    await navigateToEditor(page);
  });

  const selectedText = (page: Page) => page.evaluate(() => globalThis.getSelection()?.toString());
  const formatted = (page: Page, fontType: "BOLD" | "ITALIC") =>
    page.locator(`.editor [data-literal][data-font-type=${fontType}]`);
  const paragraphText = (page: Page) => page.locator(".editor-surface > .PARAGRAPH").first();

  async function expectOnly(page: Page, fontType: "BOLD" | "ITALIC" | "PLAIN") {
    for (const other of ["BOLD", "ITALIC"] as const) {
      if (other === fontType) await expect(formatted(page, other)).toHaveText(["knirker"]);
      else await expect(formatted(page, other)).toHaveCount(0);
    }
    await expect(paragraphText(page)).toHaveText(sentence);
    await expect.poll(() => selectedText(page)).toBe("knirker");
  }

  test("Bold then Italic from the toolbar, then undo and redo", async ({ page }) => {
    await selectTextInElement(page, sentence, "knirker");

    await page.getByTestId("fonttype-bold-button").click();
    await expectOnly(page, "BOLD");

    await page.getByTestId("fonttype-italic-button").click();
    await expectOnly(page, "ITALIC");

    await page.keyboard.press("ControlOrMeta+z");
    await expectOnly(page, "BOLD");
    await page.keyboard.press("ControlOrMeta+z");
    await expectOnly(page, "PLAIN");

    await page.keyboard.press("ControlOrMeta+Shift+z");
    await expectOnly(page, "BOLD");
    await page.keyboard.press("ControlOrMeta+Shift+z");
    await expectOnly(page, "ITALIC");
  });

  test("repeated shortcuts toggle the same text and keep it selected", async ({ page }) => {
    const bold = process.platform === "darwin" ? "Meta+b" : "Control+b";
    const italic = process.platform === "darwin" ? "Meta+i" : "Control+i";
    await selectTextInElement(page, sentence, "knirker");

    await page.keyboard.press(bold);
    await expectOnly(page, "BOLD");
    await page.keyboard.press(italic);
    await expectOnly(page, "ITALIC");
    await page.keyboard.press(italic);
    await expectOnly(page, "PLAIN");
    await page.keyboard.press(bold);
    await expectOnly(page, "BOLD");

    await page.keyboard.type("X");
    await expect(paragraphText(page)).toHaveText("Noen stoler X i rom 4A.");
  });
});

test.describe("Switch font type across differently formatted text", () => {
  const parts = ["Du får ", "alderspensjon", " fra 1. mai 2025 etter ", "folketrygdloven § 20-2"];
  const sentence = parts.join("");

  test.beforeEach(async ({ page }) => {
    await setupBrevRoute(
      page,
      brevResponse({
        info: editorInfo(),
        redigertBrev: editedLetter({
          sakspart: baseSakspart(),
          blocks: [
            paragraph({
              id: 1,
              content: [
                makeLiteral(11, 1, parts[0]),
                { ...makeLiteral(12, 1, parts[1]), fontType: "BOLD" },
                makeLiteral(13, 1, parts[2]),
                { ...makeLiteral(14, 1, parts[3]), fontType: "ITALIC" },
              ],
            }),
          ],
          signatur: baseSignatur(),
        }) as EditedLetter,
      }),
    );
    await navigateToEditor(page);
  });

  const fontTypes = (page: Page) =>
    page
      .locator(".editor-surface > .PARAGRAPH")
      .first()
      .locator("[data-literal]")
      .evaluateAll((literals) => literals.map((literal) => (literal as HTMLElement).dataset.fontType ?? "PLAIN"));
  const selectedText = (page: Page) => page.evaluate(() => globalThis.getSelection()?.toString());
  const boldButton = (page: Page) => page.getByTestId("fonttype-bold-button");
  const italicButton = (page: Page) => page.getByTestId("fonttype-italic-button");

  async function selectSentence(page: Page) {
    await page.evaluate((parts) => {
      const literals = [...document.querySelectorAll<HTMLElement>(".editor-surface > .PARAGRAPH [data-literal]")];
      const first = literals.find((literal) => literal.textContent === parts[0])!;
      const last = literals.find((literal) => literal.textContent === parts.at(-1))!;
      first.closest<HTMLElement>('[contenteditable="true"]')!.focus();
      const range = document.createRange();
      range.setStart(first.firstChild!, 0);
      range.setEnd(last.firstChild!, last.textContent!.length);
      getSelection()!.removeAllRanges();
      getSelection()!.addRange(range);
    }, parts);
    await expect.poll(() => selectedText(page)).toBe(sentence);
  }

  test("Bold and Italic apply to all of it and remove from all of it, from the toolbar and shortcuts", async ({
    page,
  }) => {
    const bold = process.platform === "darwin" ? "Meta+b" : "Control+b";
    const italic = process.platform === "darwin" ? "Meta+i" : "Control+i";
    await selectSentence(page);
    await expect(boldButton(page)).toHaveAttribute("aria-pressed", "mixed");
    await expect(italicButton(page)).toHaveAttribute("aria-pressed", "mixed");

    await boldButton(page).click();
    await expect.poll(() => fontTypes(page)).toEqual(["BOLD", "BOLD", "BOLD", "BOLD"]);
    await expect(boldButton(page)).toHaveAttribute("aria-pressed", "true");
    await expect(italicButton(page)).toHaveAttribute("aria-pressed", "false");
    await expect.poll(() => selectedText(page)).toBe(sentence);

    await page.keyboard.press(bold);
    await expect.poll(() => fontTypes(page)).toEqual(["PLAIN", "PLAIN", "PLAIN", "PLAIN"]);
    await page.keyboard.press(italic);
    await expect.poll(() => fontTypes(page)).toEqual(["ITALIC", "ITALIC", "ITALIC", "ITALIC"]);
    await italicButton(page).click();
    await expect.poll(() => fontTypes(page)).toEqual(["PLAIN", "PLAIN", "PLAIN", "PLAIN"]);
    await expect.poll(() => selectedText(page)).toBe(sentence);

    for (let step = 0; step < 4; step++) await page.keyboard.press("ControlOrMeta+z");
    await expect.poll(() => fontTypes(page)).toEqual(["PLAIN", "BOLD", "PLAIN", "ITALIC"]);
  });

  test("the toolbar follows the word at the caret", async ({ page }) => {
    await page.locator(".editor").getByText("alderspensjon", { exact: true }).click();
    await expect(boldButton(page)).toHaveAttribute("aria-pressed", "true");
    await expect(italicButton(page)).toHaveAttribute("aria-pressed", "false");

    await page.locator(".editor").getByText("folketrygdloven § 20-2", { exact: true }).click();
    await expect(boldButton(page)).toHaveAttribute("aria-pressed", "false");
    await expect(italicButton(page)).toHaveAttribute("aria-pressed", "true");
  });
});
