import { expect, type Page, test } from "@playwright/test";

import { SpraakKode } from "~/types/apiTypes";
import { type EditedLetter } from "~/types/brevbakerTypes";

import {
  nyBrevInfo,
  nyBrevResponse,
  nyItem,
  nyItemList,
  nyLiteral,
  nyParagraphBlock,
  nyRedigertBrev,
  nySignatur,
  nyTitle1Block,
  nyTitle2Block,
  nyVariable,
} from "../../utils/brevredigeringTestUtils";
import { setupSakStubs } from "../utils/helpers";

const baseSakspart = {
  gjelderNavn: "Test Testeson",
  gjelderFoedselsnummer: "12345678910",
  saksnummer: "1234",
  dokumentDato: "2024-03-15",
};

const baseSignatur = nySignatur({
  hilsenTekst: "Med vennlig hilsen",
  saksbehandlerRolleTekst: "Saksbehandler",
  saksbehandlerNavn: "Ole Saksbehandler",
  attesterendeSaksbehandlerNavn: "",
  navAvsenderEnhet: "Nav Familie- og pensjonsytelser Porsgrunn",
});

const editorInfo = nyBrevInfo({
  brevkode: "BREV1",
  brevtittel: "Brev 1",
  opprettet: "2024-01-01",
  sistredigert: "2024-01-01",
  sistredigertAv: { id: "Z123", navn: "Z entotre" },
  opprettetAv: { id: "Z123", navn: "Z entotre" },
  status: { type: "UnderRedigering", redigeresAv: { id: "Z123", navn: "Z entotre" } },
  avsenderEnhet: { enhetNr: "0001", navn: "NAV Familie- og pensjonsytelser" },
  spraak: SpraakKode.Bokmaal,
  saksId: 22981081,
});

function literal(id: number, parentId: number | null, text: string) {
  return { ...nyLiteral({ id, text }), parentId };
}

function variable(id: number, parentId: number | null, text: string) {
  return { ...nyVariable({ id, text }), parentId };
}

const exampleLetter1 = nyRedigertBrev({
  title: {
    text: [literal(1, null, "Informasjon om saksbehandlingstiden vår")],
    deletedContent: [],
  },
  sakspart: baseSakspart,
  blocks: [
    nyParagraphBlock({
      id: 1,
      content: [
        literal(11, 1, "Denne blokken[CP1-1] "),
        variable(12, 1, "VARIABLE-MED-LITT-LENGDE"),
        literal(
          13,
          1,
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av variable.",
        ),
      ],
    }),
    nyParagraphBlock({
      id: 2,
      content: [
        literal(21, 2, "[CP2-1]Her vil vi teste om piltast opp/ned"),
        variable(22, 2, "ØVRE-VARIABLE"),
        literal(23, 2, "fungerer mellom egne avsnitt"),
      ],
    }),
    nyParagraphBlock({
      id: 3,
      content: [
        literal(31, 3, "[CP2-2]Her vil vi"),
        variable(32, 3, "NEDRE-VARIABLE"),
        literal(33, 3, " teste om pil opp/ned fungerer mellom avsnitt [CP2-3]"),
      ],
    }),
    nyTitle1Block({
      id: 4,
      content: [literal(41, 4, "Tittel over punktliste")],
    }),
    nyParagraphBlock({
      id: 5,
      content: [
        {
          ...nyItemList({
            id: 51,
            items: [
              {
                ...nyItem({
                  id: 511,
                  content: [
                    literal(
                      5111,
                      511,
                      "Punkt 1. Dette er et veldig langt punkt kun av den grunn at vi ønsker helst, mest sannsynlig, at denne skal brekke over to linjer[CP3-1]",
                    ),
                  ],
                }),
                parentId: 51,
              },
              {
                ...nyItem({
                  id: 512,
                  content: [literal(5121, 512, "Punkt 2. Migrere brev[CP3-2]")],
                }),
                parentId: 51,
              },
              {
                ...nyItem({
                  id: 513,
                  content: [literal(-5131, 513, "Punkt 3. Øk budjsettet[CP3-3]")],
                }),
                parentId: 51,
              },
            ],
          }),
          parentId: 5,
        },
      ],
    }),
    nyTitle2Block({
      id: 6,
      content: [literal(61, 6, "Tittel under punktliste")],
    }),
    nyParagraphBlock({
      id: 7,
      content: [literal(71, 7, "Siste avsnitt for å teste at pil ned tar oss til end of line.[CP4-1]")],
    }),
  ],
  signatur: baseSignatur,
}) as EditedLetter;

function makeBrevResponse(redigertBrev: EditedLetter) {
  return nyBrevResponse({ info: editorInfo, redigertBrev });
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
        await page.getByTestId("fonttype-bold").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");

        // Re-select and unbold
        await selectFullElement(page, "teste piltast");
        await page.getByTestId("fonttype-bold").click();
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
        await page.getByTestId("fonttype-italic").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "italic");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Italic off with toolbar
        await selectFullElement(page, "teste piltast");
        await page.getByTestId("fonttype-italic").click();
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
        await page.getByTestId("fonttype-bold").click();
        await expect(page.getByText("Er laget for")).toHaveCSS("font-weight", "400");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("Er laget for")).toHaveCSS("font-style", "normal");
        await expect(page.getByText("teste piltast")).toHaveCSS("font-style", "normal");
        await expect(page.getByText(afterSelectionText)).toHaveCSS("font-style", "normal");

        // Switch to italic (removes bold, applies italic)
        await selectFullElement(page, "teste piltast");
        await page.getByTestId("fonttype-italic").click();
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
        const redigertBrev = nyRedigertBrev({
          title: {
            text: [literal(1, null, "Title")],
            deletedContent: [],
          },
          sakspart: {
            gjelderNavn: "Test",
            gjelderFoedselsnummer: "12345678910",
            saksnummer: "1234",
            dokumentDato: "2024-03-15",
          },
          blocks: [
            nyParagraphBlock({
              id: 1,
              content: [
                literal(11, 1, "Jeg er en literal,"),
                literal(12, 1, " som blir fulgt opp av en annen literal,"),
                literal(13, 1, " og til slutt, en siste literal"),
              ],
            }),
          ],
          signatur: nySignatur({
            hilsenTekst: "Med vennlig hilsen",
            saksbehandlerRolleTekst: "Saksbehandler",
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
        await page.getByTestId("editor-bullet-list").click();
        await page.getByTestId("fonttype-bold").click();
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
        await page.getByTestId("fonttype-bold").click();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(4);

        const mainText =
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av";
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "700");

        // Unbold by clicking on bold element and toggling
        await page.getByText("variable.").click();
        await page.getByTestId("fonttype-bold").click();
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

        await page.getByTestId("fonttype-italic").click();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(4);

        const mainText =
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av";
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "italic");

        await page.getByText("variable.").click();
        await page.getByTestId("fonttype-italic").click();
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

        await page.getByTestId("fonttype-bold").click();
        expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(4);

        const mainText =
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Vi vil teste [CP1-3] at caret går til nærmeste side av";
        await expect(page.getByText(mainText)).toHaveCSS("font-weight", "400");
        await expect(page.getByText("variable.")).toHaveCSS("font-weight", "700");
        await expect(page.getByText(mainText)).toHaveCSS("font-style", "normal");
        await expect(page.getByText("variable.")).toHaveCSS("font-style", "normal");

        // Switch to italic
        await page.getByText("variable.").click();
        await page.getByTestId("fonttype-italic").click();
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
      await page.getByTestId("fonttype-bold").click();

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");
      await page.getByTestId("fonttype-bold").click();
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
      await page.getByTestId("fonttype-italic").click();

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-style", "normal");
      await page.getByTestId("fonttype-italic").click();
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
      await page.getByTestId("fonttype-bold").click();

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-style", "normal");
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");

      await page.getByTestId("fonttype-italic").click();
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

      await page.getByTestId("fonttype-bold").click();

      expect(await firstParagraph.evaluate((el) => el.childNodes.length)).toBe(3);
      await expect(page.getByText("VARIABLE-MED-LITT-LENGDE")).toHaveCSS("font-weight", "400");
    });

    test("endring av fonttype av en variable inne i et punkt skal bevare all content i punkten", async ({ page }) => {
      const redigertBrev = nyRedigertBrev({});
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

      await page.getByTestId("editor-bullet-list").click();
      await page.getByTestId("fonttype-bold").click();
      await expect(page.getByText(fullText)).toBeVisible();
    });
  });
});
