import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createMemoryHistory, createRootRouteWithContext, createRouter, RouterProvider } from "@tanstack/react-router";
import { cleanup, render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { AxiosError, AxiosHeaders } from "axios";
import { afterEach, describe, expect, it, vi } from "vitest";

import { type TemplateDocumentation } from "~/api/templateDocumentationTypes";
import { Route as TemplateRouteImport } from "~/routes/template.$malType.$templateId";
import { type SearchCriteria } from "~/search/searchContext";
import { type TemplateHits } from "~/templateSearch/useTemplateHits";

const { useTemplateHits } = vi.hoisted(() => ({ useTemplateHits: vi.fn() }));
vi.mock("~/templateSearch/useTemplateHits", async () => {
  const actual = await vi.importActual<typeof import("~/templateSearch/useTemplateHits")>(
    "~/templateSearch/useTemplateHits",
  );
  return { ...actual, useTemplateHits };
});

const { getTemplateDescription, getTemplateDocumentation } = vi.hoisted(() => ({
  getTemplateDescription: {
    queryKey: (malType: string, templateId: string) => ["TEMPLATE_DESCRIPTION", malType, templateId] as const,
    queryFn: vi.fn(),
  },
  getTemplateDocumentation: {
    queryKey: (malType: string, templateId: string, language: string) =>
      ["TEMPLATE_DOCUMENTATION", malType, templateId, language] as const,
    queryFn: vi.fn(),
  },
}));

vi.mock("~/api/brevbaker-api-endpoints", async () => {
  const actual = await vi.importActual<typeof import("~/api/brevbaker-api-endpoints")>("~/api/brevbaker-api-endpoints");
  return { ...actual, getTemplateDescription, getTemplateDocumentation };
});

// `Route.update` changes the shared route in place, so the real page is kept
// before any test swaps in the stub.
const realComponent = TemplateRouteImport.options.component;

const documentation = { title: [], outline: [], attachments: [] } as unknown as TemplateDocumentation;

function notFoundError() {
  return new AxiosError("Not Found", "ERR_BAD_REQUEST", undefined, undefined, {
    status: 404,
    statusText: "Not Found",
    data: undefined,
    headers: {},
    config: { headers: new AxiosHeaders() },
  });
}

/** Mounts the real template route's loader and search validation. By default
 *  with a stub page in place of the (heavy) documentation view. */
async function renderTemplateRoute(path: string, search: Record<string, unknown> = {}, { realPage = false } = {}) {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  const rootRoute = createRootRouteWithContext<{ queryClient: QueryClient }>()({});
  const templateRoute = TemplateRouteImport.update({
    id: "/template/$malType/$templateId",
    path: "/template/$malType/$templateId",
    getParentRoute: () => rootRoute,
    component: realPage ? realComponent : () => <p>Malsiden</p>,
    // biome-ignore lint/suspicious/noExplicitAny: mirrors the `as any` cast routeTree.gen.ts uses for this same `.update(...)` call.
  } as any);
  const router = createRouter({
    routeTree: rootRoute.addChildren([templateRoute]),
    context: { queryClient },
    history: createMemoryHistory({ initialEntries: ["/"] }),
    defaultNotFoundComponent: () => <p>Fant ikke siden</p>,
  });
  render(
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>,
  );
  // biome-ignore lint/suspicious/noExplicitAny: the test router is untyped, like the route it mounts.
  await router.navigate({ to: path, search } as any);
  return router;
}

describe("template route with incorrect URL values", () => {
  afterEach(() => {
    cleanup();
    vi.resetAllMocks();
  });

  it("opens the default language, keeping the search, when the language is unknown", async () => {
    getTemplateDescription.queryFn.mockResolvedValue({ languages: ["NYNORSK", "BOKMAL"] });
    getTemplateDocumentation.queryFn.mockResolvedValue(documentation);

    const router = await renderTemplateRoute("/template/autobrev/A", { language: "SAMISK", q: "pensjon" });

    await screen.findByText("Malsiden");
    expect(router.state.location.search).toMatchObject({ language: "NYNORSK", q: "pensjon" });
    expect(getTemplateDocumentation.queryFn).not.toHaveBeenCalledWith("autobrev", "A", "SAMISK");
  });

  it("shows the not-found page for an unknown template type", async () => {
    await renderTemplateRoute("/template/ukjent/A", { language: "BOKMAL" });

    await screen.findByText("Fant ikke siden");
    expect(getTemplateDescription.queryFn).not.toHaveBeenCalled();
  });

  it("shows the not-found page for a template that does not exist", async () => {
    getTemplateDescription.queryFn.mockRejectedValue(notFoundError());

    await renderTemplateRoute("/template/autobrev/FINNES_IKKE", { language: "BOKMAL" });

    await waitFor(() => expect(screen.getByText("Fant ikke siden")).toBeTruthy());
  });
});

describe("turning on 'Vis søketreff'", () => {
  function paragraph(index: number, text: string) {
    return {
      controlStructureType: "CONTENT",
      content: {
        elementType: "PARAGRAPH",
        index,
        paragraph: [{ controlStructureType: "CONTENT", content: { elementType: "PARAGRAPH_TEXT_LITERAL", text } }],
      },
    };
  }
  const page = {
    title: [],
    outline: [paragraph(10, "Innledning"), paragraph(20, "Første treff"), paragraph(30, "Andre treff")],
    attachments: [],
    templateModelSpecification: { types: {}, letterModelTypeName: "" },
  } as unknown as TemplateDocumentation;

  /** Hits in blocks 20 and 30, with the requested block active as the real hook does. */
  function readyHits(criteria: SearchCriteria | undefined, _current: unknown, requestedBlock?: number) {
    if (!criteria) {
      return undefined;
    }
    return {
      status: "ready",
      highlight: { needle: "treff", exactOnly: false },
      matchingBlocks: [20, 30],
      activeBlock: requestedBlock ?? (criteria.tab === "innhold" ? 20 : undefined),
      result: { position: 1, total: 1 },
      previousResult: undefined,
      nextResult: undefined,
    } satisfies TemplateHits;
  }

  const scrolledTo: (string | null)[] = [];
  function trackScrolling() {
    scrolledTo.length = 0;
    Element.prototype.scrollIntoView = function (this: Element) {
      scrolledTo.push(this.getAttribute("data-block-index"));
    };
  }

  afterEach(() => {
    cleanup();
    vi.resetAllMocks();
  });

  it("goes to the first hit when none is active, e.g. opened from the Brev tab", async () => {
    getTemplateDescription.queryFn.mockResolvedValue({ languages: ["BOKMAL"] });
    getTemplateDocumentation.queryFn.mockResolvedValue(page);
    useTemplateHits.mockImplementation(readyHits);
    trackScrolling();
    const router = await renderTemplateRoute(
      "/template/autobrev/A",
      { language: "BOKMAL", q: "treff" },
      { realPage: true },
    );
    const toggle = await screen.findByLabelText<HTMLInputElement>("Vis søketreff");
    expect(toggle.checked).toBe(false);

    await userEvent.click(toggle);

    await waitFor(() => expect(router.state.location.search).toMatchObject({ index: 20 }));
    await waitFor(() => expect(scrolledTo).toContain("20"));
    expect(screen.getByText("Avsnitt 1 av 2")).toBeTruthy();
  });

  it("goes back to the active hit when turned off and on again", async () => {
    getTemplateDescription.queryFn.mockResolvedValue({ languages: ["BOKMAL"] });
    getTemplateDocumentation.queryFn.mockResolvedValue(page);
    useTemplateHits.mockImplementation(readyHits);
    trackScrolling();
    await renderTemplateRoute(
      "/template/autobrev/A",
      { language: "BOKMAL", q: "treff", tab: "innhold", index: 30 },
      { realPage: true },
    );
    await waitFor(() => expect(scrolledTo).toEqual(["30"]));
    const toggle = screen.getByLabelText<HTMLInputElement>("Vis søketreff");

    await userEvent.click(toggle);
    await userEvent.click(toggle);

    await waitFor(() => expect(scrolledTo).toEqual(["30", "30"]));
  });
});
