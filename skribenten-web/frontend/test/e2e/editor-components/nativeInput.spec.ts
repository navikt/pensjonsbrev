import { type CDPSession, expect, type Page, test } from "@playwright/test";

import { type EditedLetter, FontType, type LiteralValue } from "~/types/brevbakerTypes";
import { editorInfo } from "~test/e2e/support/editorFixtures";
import { setupSakStubs } from "~test/e2e/support/helpers";
import { brevResponse, editedLetter } from "~test/support/brevFixtures";
import { literal as _literal, cell, item, itemList, paragraph, row, table } from "~test/support/letterEditorTestUtils";

/**
 * Input that the browser applies itself instead of through a cancelable beforeinput: dead keys and IME composition,
 * and spellcheck/autocorrect replacements. Every test ends by typing through the model, which re-renders the literal
 * from the model, so a DOM edit that never reached the model shows up as wrong text.
 */

const bold = (text: string): LiteralValue => ({ ..._literal(text), fontType: FontType.BOLD });

const letter = editedLetter({
  title: { text: [_literal("Vedtak om kafe")], deletedContent: [] },
  blocks: [
    paragraph([_literal("Vi drar til kaf i morgen.")]),
    paragraph([_literal("Vi har motatt "), bold("soknaden"), _literal(" din.")]),
    paragraph([_literal("")]),
    paragraph([itemList({ items: [item(_literal("Punkt med kaf"))] })]),
    paragraph([
      table([cell(_literal("Sted")), cell(_literal("Tid"))], [row(cell(_literal("Kaf")), cell(_literal("Mandag")))]),
    ]),
  ],
}) as EditedLetter;

async function openEditor(page: Page) {
  const errors: string[] = [];
  page.on("pageerror", (error) => errors.push(error.message));
  page.on("console", (message) => {
    if (message.type() === "error") errors.push(message.text());
  });

  await setupSakStubs(page);
  const response = brevResponse({ info: editorInfo(), redigertBrev: letter });
  await page.route("**/bff/skribenten-backend/sak/123456/brev/1**", (route) =>
    route.fulfill({ json: response, contentType: "application/json" }),
  );
  await page.goto("/saksnummer/123456/brev/1");
  await page.locator(".editor").waitFor({ state: "visible", timeout: 15_000 });
  return errors;
}

const literalSelector = ".editor [data-literal]";

/** Places the DOM caret, or a selection when `to` is given, in literals found by their text. */
async function select(page: Page, from: [text: string, offset: number], to?: [text: string, offset: number]) {
  await page.evaluate(
    ({ selector, from, to }) => {
      const find = (text: string) => {
        const literal = [...document.querySelectorAll<HTMLElement>(selector)].find((el) => el.textContent === text);
        if (!literal?.firstChild) throw new Error(`No literal with text "${text}"`);
        return literal;
      };
      const start = find(from[0]);
      start.closest<HTMLElement>('[contenteditable="true"]')!.focus();
      const range = document.createRange();
      range.setStart(start.firstChild!, from[1]);
      if (to) range.setEnd(find(to[0]).firstChild!, to[1]);
      else range.collapse(true);
      getSelection()!.removeAllRanges();
      getSelection()!.addRange(range);
    },
    { selector: literalSelector, from, to },
  );
  // Selection changes reach the model through the asynchronous selectionchange event.
  await page.waitForTimeout(50);
}

/** A dead key or IME: one or more composition updates, then the committed text. */
async function compose(cdp: CDPSession, updates: string[], committed: string) {
  for (const text of updates) {
    await cdp.send("Input.imeSetComposition", { text, selectionStart: text.length, selectionEnd: text.length });
  }
  await cdp.send("Input.insertText", { text: committed });
}

/** Fires the beforeinput event Chrome sends when a spellcheck suggestion or autocorrection is chosen. */
async function chooseSpellingSuggestion(
  page: Page,
  word: [text: string, start: number, end: number],
  replacement: string,
) {
  return page.evaluate(
    ({ selector, word, replacement }) => {
      const literal = [...document.querySelectorAll<HTMLElement>(selector)].find((el) => el.textContent === word[0])!;
      const dataTransfer = new DataTransfer();
      dataTransfer.setData("text/plain", replacement);
      const target = new StaticRange({
        startContainer: literal.firstChild!,
        startOffset: word[1],
        endContainer: literal.firstChild!,
        endOffset: word[2],
      });
      const event = new InputEvent("beforeinput", {
        inputType: "insertReplacementText",
        dataTransfer,
        targetRanges: [target],
        bubbles: true,
        cancelable: true,
      });
      literal.dispatchEvent(event);
      return event.defaultPrevented;
    },
    { selector: literalSelector, word, replacement },
  );
}

const blockTexts = (page: Page) =>
  page.evaluate(() =>
    [...document.querySelectorAll(".editor-surface > div")].map((block) =>
      (block.textContent ?? "").replaceAll(/[\u200B\u2060]/g, ""),
    ),
  );

test.describe("native input reaches the letter model", () => {
  test("a dead key in the middle of a word", async ({ page }) => {
    const errors = await openEditor(page);
    const cdp = await page.context().newCDPSession(page);

    await select(page, ["Vi drar til kaf i morgen.", 15]);
    await compose(cdp, ["´"], "é");
    await page.keyboard.type("Z");

    expect((await blockTexts(page))[0]).toBe("Vi drar til kaféZ i morgen.");
    expect(errors).toEqual([]);
  });

  test("a dead key at the start of a literal after another literal", async ({ page }) => {
    const errors = await openEditor(page);
    const cdp = await page.context().newCDPSession(page);

    await select(page, [" din.", 0]);
    await compose(cdp, ["´"], "é");
    await page.keyboard.type("Z");

    expect((await blockTexts(page))[1]).toBe("Vi har motatt soknadenéZ din.");
    expect(errors).toEqual([]);
  });

  test("a dead key in an empty paragraph", async ({ page }) => {
    const errors = await openEditor(page);
    const cdp = await page.context().newCDPSession(page);

    await page.locator(".editor-surface > .PARAGRAPH").nth(2).locator("[data-literal]").click();
    await compose(cdp, ["´"], "é");
    await page.keyboard.type("Z");

    expect((await blockTexts(page))[2]).toBe("éZ");
    expect(errors).toEqual([]);
  });

  test("a dead key replacing a selection within one literal", async ({ page }) => {
    const errors = await openEditor(page);
    const cdp = await page.context().newCDPSession(page);

    await select(page, ["Vi drar til kaf i morgen.", 3], ["Vi drar til kaf i morgen.", 7]);
    await compose(cdp, ["¨"], "ü");
    await page.keyboard.type("Z");

    expect((await blockTexts(page))[0]).toBe("Vi üZ til kaf i morgen.");
    expect(errors).toEqual([]);
  });

  test("a dead key replacing a selection across literals", async ({ page }) => {
    const errors = await openEditor(page);
    const cdp = await page.context().newCDPSession(page);

    await select(page, ["Vi har motatt ", 7], [" din.", 1]);
    await compose(cdp, ["´"], "é");
    await page.keyboard.type("Z");

    expect((await blockTexts(page))[1]).toBe("Vi har éZdin.");
    expect(errors).toEqual([]);
  });

  test("a dead key in a list item and in a table cell", async ({ page }) => {
    const errors = await openEditor(page);
    const cdp = await page.context().newCDPSession(page);

    await select(page, ["Punkt med kaf", 13]);
    await compose(cdp, ["´"], "é");
    await page.keyboard.type("Z");
    await select(page, ["Kaf", 3]);
    await compose(cdp, ["´"], "é");
    await page.keyboard.type("Z");

    const texts = await blockTexts(page);
    expect(texts[3]).toBe("Punkt med kaféZ");
    expect(texts[4]).toContain("KaféZ");
    expect(errors).toEqual([]);
  });

  test("a dead key in the letter title", async ({ page }) => {
    const errors = await openEditor(page);
    const cdp = await page.context().newCDPSession(page);

    await select(page, ["Vedtak om kafe", 13]);
    await compose(cdp, ["´"], "é");
    await page.keyboard.type("Z");

    await expect(page.locator(".letter-title")).toHaveText("Vedtak om kaféZe");
    expect(errors).toEqual([]);
  });

  test("a multi-step IME composition", async ({ page }) => {
    const errors = await openEditor(page);
    const cdp = await page.context().newCDPSession(page);

    await select(page, ["Vi drar til kaf i morgen.", 3]);
    await compose(cdp, ["k", "か", "かf", "かふ"], "カフェ");
    await page.keyboard.type("Z");

    expect((await blockTexts(page))[0]).toBe("Vi カフェZdrar til kaf i morgen.");
    expect(errors).toEqual([]);
  });

  test("the text being composed stays visible until the composition ends", async ({ page }) => {
    const errors = await openEditor(page);
    const cdp = await page.context().newCDPSession(page);

    await select(page, ["Vi drar til kaf i morgen.", 3]);
    for (const text of ["k", "か", "かf", "かふ"]) {
      await cdp.send("Input.imeSetComposition", { text, selectionStart: text.length, selectionEnd: text.length });
      // Let selection changes and any re-render run, as they would between key presses.
      await page.waitForTimeout(100);
      expect((await blockTexts(page))[0]).toBe(`Vi ${text}drar til kaf i morgen.`);
    }
    await cdp.send("Input.insertText", { text: "カフェ" });

    expect((await blockTexts(page))[0]).toBe("Vi カフェdrar til kaf i morgen.");
    expect(errors).toEqual([]);
  });

  test("an autosave during a composition doesn't move the caret or change the text being composed", async ({
    page,
  }) => {
    const errors = await openEditor(page);
    await page.route("**/bff/skribenten-backend/brev/1/redigertBrev**", (route) =>
      route.fulfill({
        json: brevResponse({ info: editorInfo(), redigertBrev: route.request().postDataJSON() }),
        contentType: "application/json",
      }),
    );
    const cdp = await page.context().newCDPSession(page);

    await select(page, ["Vi drar til kaf i morgen.", 3]);
    await page.keyboard.type("x");
    const autosave = page.waitForResponse(
      (response) => response.url().includes("/redigertBrev") && response.request().method() === "PUT",
      { timeout: 15_000 },
    );
    await cdp.send("Input.imeSetComposition", { text: "かふ", selectionStart: 2, selectionEnd: 2 });
    await autosave;
    await page.waitForTimeout(300);
    expect((await blockTexts(page))[0]).toBe("Vi xかふdrar til kaf i morgen.");

    await cdp.send("Input.insertText", { text: "カフェ" });
    await page.keyboard.type("Z");

    expect((await blockTexts(page))[0]).toBe("Vi xカフェZdrar til kaf i morgen.");
    expect(errors).toEqual([]);
  });

  test("the model caret stays where the composition started until it is committed", async ({ page }) => {
    const errors = await openEditor(page);
    await page.goto("/saksnummer/123456/brev/1?debug=true");
    await page.locator(".editor").waitFor({ state: "visible", timeout: 15_000 });
    const modelFocus = page.locator("[data-debug-panel-stats] > div").filter({ hasText: "FOCUS:" });
    const cdp = await page.context().newCDPSession(page);

    await select(page, ["Vi drar til kaf i morgen.", 3]);
    for (const text of ["k", "か", "かふ"]) {
      await cdp.send("Input.imeSetComposition", { text, selectionStart: text.length, selectionEnd: text.length });
      await page.waitForTimeout(100);
      await expect(modelFocus).toContainText("cursorPosition: 3");
    }
    await cdp.send("Input.insertText", { text: "カフェ" });

    await expect(modelFocus).toContainText("cursorPosition: 6");
    expect(errors).toEqual([]);
  });

  test("Enter that commits a composition doesn't split the paragraph", async ({ page }) => {
    const errors = await openEditor(page);
    const cdp = await page.context().newCDPSession(page);

    await select(page, ["Vi drar til kaf i morgen.", 3]);
    await cdp.send("Input.imeSetComposition", { text: "かふ", selectionStart: 2, selectionEnd: 2 });
    await page.waitForTimeout(100);
    // An IME sends the Enter that commits the composition as a composing key event.
    await page.evaluate(() => {
      const target = getSelection()!.anchorNode!.parentElement!;
      const init = { key: "Enter", isComposing: true, keyCode: 229, bubbles: true, cancelable: true };
      target.dispatchEvent(new KeyboardEvent("keydown", init));
    });
    await cdp.send("Input.insertText", { text: "カフェ" });
    await page.keyboard.type("Z");

    const texts = await blockTexts(page);
    expect(texts[0]).toBe("Vi カフェZdrar til kaf i morgen.");
    expect(texts[1]).toBe("Vi har motatt soknaden din.");
    expect(errors).toEqual([]);
  });

  test("a spellcheck suggestion replaces the misspelled word in the model", async ({ page }) => {
    const errors = await openEditor(page);

    // The caret is elsewhere: the correction applies to the word the browser targets, not to the caret.
    await select(page, [" din.", 5]);
    expect(await chooseSpellingSuggestion(page, ["Vi har motatt ", 7, 13], "mottatt")).toBe(true);
    await expect.poll(() => blockTexts(page).then((texts) => texts[1])).toBe("Vi har mottatt soknaden din.");

    await page.keyboard.type("Z");
    expect((await blockTexts(page))[1]).toBe("Vi har mottattZ soknaden din.");
    expect(errors).toEqual([]);
  });

  test("a spellcheck suggestion in bold text keeps the bold", async ({ page }) => {
    const errors = await openEditor(page);

    await select(page, ["soknaden", 8]);
    expect(await chooseSpellingSuggestion(page, ["soknaden", 0, 8], "søknaden")).toBe(true);
    await expect(page.locator(".editor [data-font-type=BOLD]")).toHaveText("søknaden");

    await page.keyboard.type("Z");
    await expect(page.locator(".editor [data-font-type=BOLD]")).toHaveText("søknadenZ");
    expect(errors).toEqual([]);
  });
});
