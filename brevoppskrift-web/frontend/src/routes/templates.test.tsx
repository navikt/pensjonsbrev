import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createRootRouteWithContext, createRouter, RouterProvider } from "@tanstack/react-router";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { afterEach, describe, expect, it, vi } from "vitest";

import { type SearchableContent } from "~/api/brevbaker-api-endpoints";
import { type TemplateDescription } from "~/api/brevbakerTypes";
import { Route as TemplatesRouteImport } from "~/routes/templates";

const { getAllTemplateDocumentation, getBrevkoderMedMetadata } = vi.hoisted(() => ({
  getAllTemplateDocumentation: {
    queryKey: (malType: string) => ["TEMPLATE_DOCUMENTATION", malType, "BATCH"] as const,
    queryFn: vi.fn(),
  },
  getBrevkoderMedMetadata: {
    queryKey: (malType: string) => ["BREVKODER", malType, "METADATA"] as const,
    queryFn: vi.fn(),
  },
}));

vi.mock("~/api/brevbaker-api-endpoints", async () => {
  const actual = await vi.importActual<typeof import("~/api/brevbaker-api-endpoints")>("~/api/brevbaker-api-endpoints");
  return { ...actual, getAllTemplateDocumentation, getBrevkoderMedMetadata };
});

const autobrevDescriptions: TemplateDescription[] = [
  { name: "A1", letterDataClass: "AutobrevDto", languages: ["BOKMAL"] },
];
const redigerbarDescriptions: TemplateDescription[] = [
  { name: "R1", letterDataClass: "RedigerbarDto", languages: ["BOKMAL"] },
];
const autobrevContent: SearchableContent[] = [
  { brevkode: "A1", language: "BOKMAL", lines: [{ index: 0, segments: [{ type: "text", value: "Hei" }] }] },
];

/** Mounts the real `/templates` route standalone. `createFileRoute` leaves the
 *  exported `Route` unbound (no id/path/parent) — the app only wires those via
 *  the codegen'd `routeTree.gen.ts` (`TemplatesRouteImport.update({ id, path,
 *  getParentRoute })`). We mirror that exact wiring here instead of using
 *  `Route` as-is, otherwise the router throws
 *  "Duplicate routes found with id: __root__". */
async function renderTemplatesRoute() {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  const rootRoute = createRootRouteWithContext<{ queryClient: QueryClient }>()({});
  const templatesRoute = TemplatesRouteImport.update({
    id: "/templates",
    path: "/templates",
    getParentRoute: () => rootRoute,
    // biome-ignore lint/suspicious/noExplicitAny: mirrors the `as any` cast routeTree.gen.ts uses for this same `.update(...)` call.
  } as any);
  const router = createRouter({
    routeTree: rootRoute.addChildren([templatesRoute]),
    context: { queryClient },
    history: undefined,
  });
  await router.navigate({ to: "/templates" });
  await router.load();

  const result = render(
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>,
  );

  await waitFor(() => {
    expect(result.container.innerHTML).not.toBe("<div></div>");
  });

  return { user: userEvent.setup(), ...result };
}

describe("<AllTemplates /> (route: /templates)", () => {
  afterEach(() => {
    getAllTemplateDocumentation.queryFn.mockReset();
    getBrevkoderMedMetadata.queryFn.mockReset();
  });

  it("shows a warning alert with a retry action when a template corpus fails to load", async () => {
    getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevDescriptions : redigerbarDescriptions),
    );
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      malType === "redigerbar" ? Promise.reject(new Error("boom")) : Promise.resolve(autobrevContent),
    );

    await renderTemplatesRoute();

    expect(await screen.findByText(/Kunne ikke hente innhold for redigerbare brev/i)).toBeTruthy();
    expect(screen.getByRole("button", { name: "Prøv igjen" })).toBeTruthy();
  });

  it("does not show the warning alert when every template corpus loads successfully", async () => {
    getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevDescriptions : redigerbarDescriptions),
    );
    getAllTemplateDocumentation.queryFn.mockResolvedValue(autobrevContent);

    await renderTemplatesRoute();

    expect(screen.queryByText(/Kunne ikke hente innhold for/i)).toBeNull();
  });

  it("clicking 'Prøv igjen' retries only the failing corpus fetch", async () => {
    getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevDescriptions : redigerbarDescriptions),
    );
    getAllTemplateDocumentation.queryFn.mockImplementation((malType: string) =>
      malType === "redigerbar" ? Promise.reject(new Error("boom")) : Promise.resolve(autobrevContent),
    );

    const { user } = await renderTemplatesRoute();
    await screen.findByRole("button", { name: "Prøv igjen" });

    getAllTemplateDocumentation.queryFn.mockResolvedValue(autobrevContent);
    await user.click(screen.getByRole("button", { name: "Prøv igjen" }));

    await waitFor(() => {
      expect(screen.queryByText(/Kunne ikke hente innhold for/i)).toBeNull();
    });
  });

  // The status line is the only "search is running" signal now that the results
  // are no longer dimmed, so it has to appear for a search and hand its place
  // over to the hit summary once that search completes. An empty search box has
  // nothing in flight and nothing to summarise, so it gets no status at all.
  it("shows no status until a query is typed, then replaces the scope with the hit summary", async () => {
    getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevDescriptions : redigerbarDescriptions),
    );
    getAllTemplateDocumentation.queryFn.mockResolvedValue(autobrevContent);

    const { user } = await renderTemplatesRoute();
    const scope = /Søker i innholdet til \d+ maler på \d+ språk/;
    await waitFor(() => {
      expect(screen.queryByText(/Indekserer innhold/)).toBeNull();
    });
    expect(screen.queryByText(scope)).toBeNull();
    expect(screen.queryByTitle("Søket er fullført")).toBeNull();

    await user.type(screen.getByRole("searchbox"), "Hei");

    await waitFor(() => {
      expect(screen.getByText(/Frasen du søker på er brukt i/i)).toBeTruthy();
    });
    expect(screen.getByTitle("Søket er fullført")).toBeTruthy();
    expect(screen.queryByText(scope)).toBeNull();
  });

  // The min-length hint and the full-page loader are gone: the status line's
  // single spinner covers indexing, and an empty box simply shows nothing.
  it("does not render a min-length hint or a second loader", async () => {
    getBrevkoderMedMetadata.queryFn.mockImplementation((malType: string) =>
      Promise.resolve(malType === "autobrev" ? autobrevDescriptions : redigerbarDescriptions),
    );
    getAllTemplateDocumentation.queryFn.mockResolvedValue(autobrevContent);

    await renderTemplatesRoute();

    expect(screen.queryByText(/Skriv minst/i)).toBeNull();
    expect(screen.queryByTitle("Henter maler")).toBeNull();
  });
});
