import {
  createMemoryHistory,
  createRootRoute,
  createRoute,
  createRouter,
  RouterProvider,
} from "@tanstack/react-router";
import { act, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it } from "vitest";

import {
  ContentOrControlStructureTypeV2,
  type ContentOrControlStructureV2,
  ElementTypeV2,
  type Expr,
  type ExprFieldPath,
  ExprType,
  type ParagraphContentV2,
} from "~/api/brevbakerTypesV2";
import { DocumentV2 } from "~/components/TemplateDocumentationV2View";

const field: ExprFieldPath = {
  exprType: ExprType.FIELD_PATH,
  source: { dataSourceType: "SCOPE", name: "argument" },
  segments: ["fodselsnummer"],
  leafType: "kotlin.String",
  leafOwnerType: "no.nav.Person",
  exampleText: "12345678901",
};

type Item = ContentOrControlStructureV2<ParagraphContentV2>;

const text = (value: string): Item => ({
  controlStructureType: ContentOrControlStructureTypeV2.CONTENT,
  content: { elementType: ElementTypeV2.PARAGRAPH_TEXT_LITERAL, text: value } as never,
});

async function renderParagraph(paragraph: Item[]) {
  const root = createRootRoute();
  const route = createRoute({
    component: () => (
      <DocumentV2
        templateDocumentation={{
          title: [],
          outline: [
            {
              controlStructureType: ContentOrControlStructureTypeV2.CONTENT,
              content: { elementType: ElementTypeV2.PARAGRAPH, paragraph },
            },
          ],
          include: field,
          attachmentData: field,
        }}
      />
    ),
    getParentRoute: () => root,
    path: "/template/$malType/$templateId",
    validateSearch: (s: Record<string, unknown>) => s,
  });
  const router = createRouter({
    history: createMemoryHistory({ initialEntries: ["/template/autobrev/X"] }),
    routeTree: root.addChildren([route]),
  });
  await act(async () => {
    render(<RouterProvider router={router} />);
  });
  return router;
}

async function expectLinkReachableAndNavigable(
  trigger: HTMLElement,
  router: Awaited<ReturnType<typeof renderParagraph>>,
) {
  const user = userEvent.setup();
  await user.hover(trigger);
  const link = await screen.findByRole("link");
  // Pekeren forlater triggeren og går inn i popoveren - den skal ikke lukkes underveis.
  await user.unhover(trigger);
  await user.hover(link);
  await new Promise((resolve) => setTimeout(resolve, 350));
  expect(screen.getByRole("link")).toBeTruthy();
  await user.click(link);
  expect((router.state.location.search as Record<string, unknown>).highlightedDataField).toBe("fodselsnummer");
}

describe("ExprPopover", () => {
  it("lar feltlenken i eksempelverdi-popoveren nås og klikkes", async () => {
    const expression: Expr = {
      exprType: ExprType.NULL_COALESCE,
      value: field,
      fallback: { exprType: ExprType.LITERAL, value: "", kind: "STRING" },
    };
    const router = await renderParagraph([
      {
        controlStructureType: ContentOrControlStructureTypeV2.CONTENT,
        content: { elementType: ElementTypeV2.PARAGRAPH_TEXT_EXPRESSION, expression },
      },
    ]);
    await expectLinkReachableAndNavigable(screen.getByRole("button", { name: "Vis fullt uttrykk" }), router);
  });

  it("lar feltlenken i predikatet til en inline betingelse nås og klikkes", async () => {
    const router = await renderParagraph([
      text("før "),
      {
        controlStructureType: ContentOrControlStructureTypeV2.CONDITIONAL,
        predicate: field,
        showIf: [text("a")],
        elseIf: [],
        showElse: [text("b")],
      } as never,
      text(" etter"),
    ]);
    await expectLinkReachableAndNavigable(screen.getAllByRole("button", { name: "If" })[0], router);
  });
});
