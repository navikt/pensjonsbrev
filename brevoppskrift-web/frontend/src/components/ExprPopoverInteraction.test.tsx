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
  type ContentOrControlStructure,
  ContentOrControlStructureType,
  ElementType,
  type Expr,
  type ExprFieldPath,
  ExprType,
  type ParagraphContent,
} from "~/api/templateDocumentationTypes";
import { Document } from "~/components/TemplateDocumentationView";

const field: ExprFieldPath = {
  exprType: ExprType.FIELD_PATH,
  source: { dataSourceType: "SCOPE", name: "argument" },
  segments: ["fodselsnummer"],
  leafType: "kotlin.String",
  leafOwnerType: "no.nav.Person",
  exampleText: "12345678901",
};

type Item = ContentOrControlStructure<ParagraphContent>;

const text = (value: string): Item => ({
  controlStructureType: ContentOrControlStructureType.CONTENT,
  content: { elementType: ElementType.PARAGRAPH_TEXT_LITERAL, text: value } as never,
});

async function renderParagraph(paragraph: Item[]) {
  const root = createRootRoute();
  const route = createRoute({
    component: () => (
      <Document
        templateDocumentation={{
          title: [],
          outline: [
            {
              controlStructureType: ContentOrControlStructureType.CONTENT,
              content: { elementType: ElementType.PARAGRAPH, index: 0, paragraph },
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
        controlStructureType: ContentOrControlStructureType.CONTENT,
        content: { elementType: ElementType.PARAGRAPH_TEXT_EXPRESSION, expression },
      },
    ]);
    await expectLinkReachableAndNavigable(screen.getByRole("button", { name: "Vis fullt uttrykk" }), router);
  });

  it("lar feltlenken i predikatet til en inline betingelse nås og klikkes", async () => {
    const router = await renderParagraph([
      text("før "),
      {
        controlStructureType: ContentOrControlStructureType.CONDITIONAL,
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
