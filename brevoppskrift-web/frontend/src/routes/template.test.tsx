import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createMemoryHistory, createRootRouteWithContext, createRouter, RouterProvider } from "@tanstack/react-router";
import { cleanup, render, screen, waitFor } from "@testing-library/react";
import { AxiosError, AxiosHeaders } from "axios";
import { afterEach, describe, expect, it, vi } from "vitest";

import { type TemplateDocumentation } from "~/api/templateDocumentationTypes";
import { Route as TemplateRouteImport } from "~/routes/template.$malType.$templateId";

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

/** Mounts the real template route's loader and search validation, with a stub
 *  page in place of the (heavy) documentation view. */
async function renderTemplateRoute(path: string, search: Record<string, unknown> = {}) {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  const rootRoute = createRootRouteWithContext<{ queryClient: QueryClient }>()({});
  const templateRoute = TemplateRouteImport.update({
    id: "/template/$malType/$templateId",
    path: "/template/$malType/$templateId",
    getParentRoute: () => rootRoute,
    component: () => <p>Malsiden</p>,
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
