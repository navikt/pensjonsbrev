import { expect, type Page, test } from "@playwright/test";

import { SpraakKode } from "~/types/apiTypes";
import { type Content, type EditedLetter, type Item, ListType } from "~/types/brevbakerTypes";

import {
  nyBrevInfo,
  nyBrevResponse,
  nyItem,
  nyItemList,
  nyLiteral,
  nyParagraphBlock,
  nyRedigertBrev,
  nySignatur,
} from "../../utils/brevredigeringTestUtils";
import { setupSakStubs } from "../utils/helpers";

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
  sadsId: "22981081",
});

function makeBrevResponse(redigertBrev: EditedLetter) {
  return nyBrevResponse({ info: editorInfo, redigertBrev });
}

function makeLiteral(id: number, text: string, parentId: number | null = null) {
  return { ...nyLiteral({ id, text }), parentId };
}

function makeItem(id: number, text: string): Item {
  return {
    ...nyItem({ id, content: [makeLiteral(id * 10 + 1, text, id)] }),
    parentId: null,
  };
}

function makeItemList(id: number, items: Item[], listType: ListType = ListType.PUNKTLISTE) {
  return {
    ...nyItemList({
      id,
      listType,
      items: items.map((item) => ({ ...item, parentId: id })),
    }),
    parentId: null,
  };
}

function makeBlock(id: number, content: Content[]) {
  return nyParagraphBlock({
    id,
    content: content.map((entry) => ({ ...entry, parentId: id })),
  });
}

function makeLetter(blocks: ReturnType<typeof makeBlock>[]) {
  return nyRedigertBrev({
    title: {
      text: [makeLiteral(1, "Test")],
      deletedContent: [],
    },
    sakspart: {
      gjelderNavn: "Test",
      gjelderFoedselsnummer: "12345678910",
      saksnummer: "1234",
      dokumentDato: "2024-03-15",
    },
    blocks,
    signatur: nySignatur({
      hilsenTekst: "Hilsen",
      saksbehandlerRolleTekst: "Saksbehandler",
      saksbehandlerNavn: "Ole",
      attesterendeSaksbehandlerNavn: "",
      navAvsenderEnhet: "Nav",
    }),
  });
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
}

test.describe("toggle number-list", () => {
  test("renders a number-list as an ol element", async ({ page }) => {
    const letter = makeLetter([
      makeBlock(10, [
        makeItemList(11, [makeItem(111, "første punkt"), makeItem(112, "andre punkt")], ListType.NUMMERERT_LISTE),
      ]),
    ]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ul")).toHaveCount(0);
    await expect(page.locator("ol li")).toHaveCount(2);
    await expect(page.locator("ol li").nth(0)).toContainText("første punkt");
    await expect(page.locator("ol li").nth(1)).toContainText("andre punkt");
  });

  test("renders a PUNKTLISTE as a ul element", async ({ page }) => {
    const letter = makeLetter([
      makeBlock(10, [makeItemList(11, [makeItem(111, "kulepunkt1"), makeItem(112, "kulepunkt2")])]),
    ]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await expect(page.locator("ul")).toHaveCount(1);
    await expect(page.locator("ol")).toHaveCount(0);
  });

  test("toggles a paragraph to a numbered list", async ({ page }) => {
    const letter = makeLetter([makeBlock(10, [makeLiteral(11, "Et avsnitt")])]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await expect(page.locator("ol")).toHaveCount(0);
    await page.getByText("Et avsnitt").click();
    await page.getByTestId("number-list-button").click();
    await expect(page.locator("ol li span").filter({ hasText: "Et avsnitt" })).toBeVisible();
    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ul")).toHaveCount(0);
  });

  test("number-list button is active when focus is on a numbered list", async ({ page }) => {
    const letter = makeLetter([makeBlock(10, [makeItemList(11, [makeItem(111, "punkt")], ListType.NUMMERERT_LISTE)])]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await page.locator("ol li span").filter({ hasText: "punkt" }).click();
    await expect(page.getByTestId("number-list-button")).toHaveAttribute("data-variant", "primary");
    await expect(page.getByTestId("bullet-list-button")).toHaveAttribute("data-variant", "tertiary");
  });

  test("bullet-list button is active when focus is on a bullet list", async ({ page }) => {
    const letter = makeLetter([makeBlock(10, [makeItemList(11, [makeItem(111, "punkt")])])]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await page.locator("ul li span").filter({ hasText: "punkt" }).click();
    await expect(page.getByTestId("bullet-list-button")).toHaveAttribute("data-variant", "primary");
    await expect(page.getByTestId("number-list-button")).toHaveAttribute("data-variant", "tertiary");
  });

  test("toggles off a numbered list with the number-list button", async ({ page }) => {
    const letter = makeLetter([
      makeBlock(10, [makeItemList(11, [makeItem(111, "punkt1"), makeItem(112, "punkt2")], ListType.NUMMERERT_LISTE)]),
    ]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await expect(page.locator("ol li")).toHaveCount(2);
    await page.locator("ol li span").filter({ hasText: "punkt1" }).click();
    await page.getByTestId("number-list-button").click();
    await expect(page.locator("ol li")).toHaveCount(1);
    await expect(page.getByText("punkt1")).toBeVisible();
    await expect(page.locator("ol").getByText("punkt1")).toHaveCount(0);
    await expect(page.locator("ol").getByText("punkt2")).toBeVisible();
  });

  test("converts a numbered list to a bullet list when clicking the bullet-list button", async ({ page }) => {
    const letter = makeLetter([
      makeBlock(10, [makeItemList(11, [makeItem(111, "punkt1"), makeItem(112, "punkt2")], ListType.NUMMERERT_LISTE)]),
    ]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await page.locator("ol li span").filter({ hasText: "punkt1" }).click();
    await page.getByTestId("bullet-list-button").click();
    await expect(page.locator("ol")).toHaveCount(0);
    await expect(page.locator("ul")).toHaveCount(1);
    await expect(page.locator("ul li")).toHaveCount(2);
    await expect(page.locator("ul li").nth(0)).toContainText("punkt1");
    await expect(page.locator("ul li").nth(1)).toContainText("punkt2");
  });

  test("converts a bullet list to a numbered list when clicking the number-list button", async ({ page }) => {
    const letter = makeLetter([makeBlock(10, [makeItemList(11, [makeItem(111, "punkt1"), makeItem(112, "punkt2")])])]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await page.locator("ul li span").filter({ hasText: "punkt1" }).click();
    await page.getByTestId("number-list-button").click();
    await expect(page.locator("ul")).toHaveCount(0);
    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ol li")).toHaveCount(2);
    await expect(page.locator("ol li").nth(0)).toContainText("punkt1");
    await expect(page.locator("ol li").nth(1)).toContainText("punkt2");
  });
});

test.describe("mixed list types", () => {
  test("item 2 of a 3-item bullet list is converted to numbered, then item 3 is also converted and merges with item 2", async ({
    page,
  }) => {
    const letter = makeLetter([
      makeBlock(10, [makeItemList(11, [makeItem(111, "item1"), makeItem(112, "item2"), makeItem(113, "item3")])]),
    ]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    // Step 1: toggle item2 off to regular text
    await page.locator("ul li span").filter({ hasText: "item2" }).click();
    await page.getByTestId("bullet-list-button").click();
    // Now: bulletList[item1] | text(item2) | bulletList[item3]
    await expect(page.locator("ul")).toHaveCount(2);
    await expect(page.locator("ul li")).toHaveCount(2);

    // Step 2: toggle item2 to number list
    await page.getByText("item2").click();
    await page.getByTestId("number-list-button").click();
    // Now: bulletList[item1] | numberList[item2] | bulletList[item3]
    await expect(page.locator("ul")).toHaveCount(2);
    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ol li")).toHaveCount(1);

    // Step 3: toggle item3 from bullet list to number list
    await page.locator("ul li span").filter({ hasText: "item3" }).click();
    await page.getByTestId("number-list-button").click();
    // item3 converts to number list and merges with item2's number list
    await expect(page.locator("ul")).toHaveCount(1);
    await expect(page.locator("ul li")).toHaveCount(1);
    await expect(page.locator("ul li").filter({ hasText: "item1" })).toBeVisible();
    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ol li")).toHaveCount(2);
    await expect(page.locator("ol li").nth(0)).toContainText("item2");
    await expect(page.locator("ol li").nth(1)).toContainText("item3");
  });

  test("toggling the middle item of a 5-item bullet list to a numbered list converts the whole list", async ({
    page,
  }) => {
    const letter = makeLetter([
      makeBlock(10, [
        makeItemList(11, [
          makeItem(111, "item1"),
          makeItem(112, "item2"),
          makeItem(113, "item3"),
          makeItem(114, "item4"),
          makeItem(115, "item5"),
        ]),
      ]),
    ]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await expect(page.locator("ul")).toHaveCount(1);
    await expect(page.locator("ul li")).toHaveCount(5);

    await page.locator("ul li span").filter({ hasText: "item3" }).click();
    await page.getByTestId("number-list-button").click();

    await expect(page.locator("ul")).toHaveCount(0);
    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ol li")).toHaveCount(5);
    for (const [i, text] of ["item1", "item2", "item3", "item4", "item5"].entries()) {
      await expect(page.locator("ol li").nth(i)).toContainText(text);
    }
  });

  test("toggling the first bullet item off and then on as a numbered list leaves the second item as a bullet list", async ({
    page,
  }) => {
    const letter = makeLetter([makeBlock(10, [makeItemList(11, [makeItem(111, "item1"), makeItem(112, "item2")])])]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    // toggle off the first bullet item
    await page.locator("ul li span").filter({ hasText: "item1" }).click();
    await page.getByTestId("bullet-list-button").click();

    // "item1" is now regular text; "item2" is still in the bullet list
    await expect(page.locator("ul li")).toHaveCount(1);
    await expect(page.locator("ul li").filter({ hasText: "item2" })).toBeVisible();

    // toggle "item1" as a number list
    await page.getByText("item1").click();
    await page.getByTestId("number-list-button").click();

    // item1 should be in an ordered list, item2 should still be in an unordered list
    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ul")).toHaveCount(1);
    await expect(page.locator("ol li")).toHaveCount(1);
    await expect(page.locator("ol li").filter({ hasText: "item1" })).toBeVisible();
    await expect(page.locator("ul li")).toHaveCount(1);
    await expect(page.locator("ul li").filter({ hasText: "item2" })).toBeVisible();
  });

  test("two numbered lists with a text paragraph in between are merged into one list of five items when the text is toggled to a numbered list", async ({
    page,
  }) => {
    const letter = makeLetter([
      makeBlock(10, [
        makeItemList(11, [makeItem(111, "item1"), makeItem(112, "item2")], ListType.NUMMERERT_LISTE),
        { ...makeLiteral(13, "middle"), parentId: 10 } as Content,
        makeItemList(14, [makeItem(141, "item3"), makeItem(142, "item4")], ListType.NUMMERERT_LISTE),
      ]),
    ]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await expect(page.locator("ol")).toHaveCount(2);
    await expect(page.locator("ol li")).toHaveCount(4);

    await page.getByText("middle").click();
    await page.getByTestId("number-list-button").click();

    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ol li")).toHaveCount(5);
    for (const [i, text] of ["item1", "item2", "middle", "item3", "item4"].entries()) {
      await expect(page.locator("ol li").nth(i)).toContainText(text);
    }
  });
});

test.describe("undo/redo of list type conversion", () => {
  const undoShortcut = "Control+z";
  const redoShortcut = "Control+y";

  test("undo reverts a bullet-to-numbered conversion", async ({ page }) => {
    const letter = makeLetter([makeBlock(10, [makeItemList(11, [makeItem(111, "punkt1"), makeItem(112, "punkt2")])])]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await page.locator("ul li span").filter({ hasText: "punkt1" }).click();
    await page.getByTestId("number-list-button").click();
    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ul")).toHaveCount(0);

    await page.keyboard.press(undoShortcut);
    await expect(page.locator("ul")).toHaveCount(1);
    await expect(page.locator("ol")).toHaveCount(0);
    await expect(page.locator("ul li")).toHaveCount(2);
  });

  test("redo re-applies a bullet-to-numbered conversion after undo", async ({ page }) => {
    const letter = makeLetter([makeBlock(10, [makeItemList(11, [makeItem(111, "punkt1"), makeItem(112, "punkt2")])])]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await page.locator("ul li span").filter({ hasText: "punkt1" }).click();
    await page.getByTestId("number-list-button").click();
    await expect(page.locator("ol")).toHaveCount(1);

    await page.keyboard.press(undoShortcut);
    await expect(page.locator("ul")).toHaveCount(1);

    await page.keyboard.press(redoShortcut);
    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ul")).toHaveCount(0);
    await expect(page.locator("ol li")).toHaveCount(2);
  });
});

test.describe("cross-block merge with numbered lists", () => {
  test("converting a single-list block between two blocks with numbered lists merges all into one block", async ({
    page,
  }) => {
    const letter = makeLetter([
      makeBlock(10, [makeItemList(11, [makeItem(111, "top1"), makeItem(112, "top2")], ListType.NUMMERERT_LISTE)]),
      makeBlock(20, [makeLiteral(21, "middle text")]),
      makeBlock(30, [makeItemList(31, [makeItem(311, "bot1"), makeItem(312, "bot2")], ListType.NUMMERERT_LISTE)]),
    ]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await expect(page.locator("ol")).toHaveCount(2);

    await page.getByText("middle text").click();
    await page.getByTestId("number-list-button").click();

    // All items should merge into one numbered list
    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ol li")).toHaveCount(5);
    for (const [i, text] of ["top1", "top2", "middle text", "bot1", "bot2"].entries()) {
      await expect(page.locator("ol li").nth(i)).toContainText(text);
    }
  });

  test("a block with only a numbered list merges with adjacent numbered list in previous block", async ({ page }) => {
    const letter = makeLetter([
      makeBlock(10, [
        makeLiteral(11, "text before"),
        makeItemList(12, [makeItem(121, "prev1"), makeItem(122, "prev2")], ListType.NUMMERERT_LISTE),
      ]),
      makeBlock(20, [makeLiteral(21, "standalone")]),
    ]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await page.getByText("standalone").click();
    await page.getByTestId("number-list-button").click();

    // The standalone block should merge with the numbered list in the previous block
    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ol li")).toHaveCount(3);
    await expect(page.locator("ol li").nth(2)).toContainText("standalone");
  });
});

test.describe("split (Enter) in numbered list", () => {
  test("pressing Enter in a numbered list creates a new item in the same numbered list", async ({ page }) => {
    const letter = makeLetter([
      makeBlock(10, [makeItemList(11, [makeItem(111, "first item")], ListType.NUMMERERT_LISTE)]),
    ]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await expect(page.locator("ol li")).toHaveCount(1);
    await page.locator("ol li span").filter({ hasText: "first item" }).click();
    // Move cursor to end and press Enter
    await page.keyboard.press("End");
    await page.keyboard.press("Enter");

    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ol li")).toHaveCount(2);
    await expect(page.locator("ol li").nth(0)).toContainText("first item");
  });

  test("pressing Enter in the middle of a numbered list item splits the text", async ({ page }) => {
    const letter = makeLetter([
      makeBlock(10, [makeItemList(11, [makeItem(111, "helloworld")], ListType.NUMMERERT_LISTE)]),
    ]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await page.locator("ol li span").filter({ hasText: "helloworld" }).click();
    // Place cursor after "hello" (5 chars from start)
    await page.keyboard.press("Home");
    for (let i = 0; i < 5; i++) {
      await page.keyboard.press("ArrowRight");
    }
    await page.keyboard.press("Enter");

    await expect(page.locator("ol")).toHaveCount(1);
    await expect(page.locator("ol li")).toHaveCount(2);
    await expect(page.locator("ol li").nth(0)).toContainText("hello");
    await expect(page.locator("ol li").nth(1)).toContainText("world");
  });
});

test.describe("deleteSelection across different list types", () => {
  test("deleting all content in a numbered list removes the list element", async ({ page }) => {
    const letter = makeLetter([
      makeBlock(10, [
        makeLiteral(11, "before"),
        makeItemList(12, [makeItem(121, "delete me")], ListType.NUMMERERT_LISTE),
        { ...makeLiteral(13, "after"), parentId: 10 } as Content,
      ]),
    ]);
    await setupBrevRoute(page, makeBrevResponse(letter));
    await navigateToEditor(page);

    await expect(page.locator("ol")).toHaveCount(1);

    // Select all text in the list item and delete
    await page.locator("ol li span").filter({ hasText: "delete me" }).click();
    await page.keyboard.press("Control+a");
    await page.keyboard.press("Backspace");

    // The list should be gone (or have empty item), and surrounding text preserved
    await expect(page.getByText("before")).toBeVisible();
    await expect(page.getByText("after")).toBeVisible();
  });
});
