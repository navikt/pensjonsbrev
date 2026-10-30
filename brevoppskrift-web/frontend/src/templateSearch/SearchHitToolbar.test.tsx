import { createRootRoute, createRoute, createRouter, RouterProvider } from "@tanstack/react-router";
import { cleanup, render, screen } from "@testing-library/react";
import { afterEach, describe, expect, it, vi } from "vitest";

import { type SearchCriteria } from "~/search/searchContext";
import { SearchHitToolbar } from "~/templateSearch/SearchHitToolbar";
import { type TemplateHits } from "~/templateSearch/useTemplateHits";

const criteria: SearchCriteria = { q: "tekst", exact: false, tab: "brev" };

const hits: TemplateHits = {
  status: "ready",
  highlight: { needle: "tekst", exactOnly: false },
  matchingBlocks: [20, 30],
  activeBlock: undefined,
  result: { position: 1, total: 2, next: { malType: "autobrev", id: "B", language: "BOKMAL" } },
  previousResult: undefined,
  nextResult: { ref: { malType: "autobrev", id: "B", language: "BOKMAL" } },
};

async function renderToolbar(marking: boolean) {
  const rootRoute = createRootRoute({
    component: () => (
      <SearchHitToolbar
        criteria={criteria}
        hits={hits}
        marking={marking}
        onMarkingChange={vi.fn()}
        onSelectBlock={vi.fn()}
      />
    ),
  });
  const templateRoute = createRoute({ getParentRoute: () => rootRoute, path: "/template/$malType/$templateId" });
  const router = createRouter({ routeTree: rootRoute.addChildren([templateRoute]) });
  render(<RouterProvider router={router} />);
  await screen.findByLabelText("Vis søketreff");
}

describe("SearchHitToolbar", () => {
  afterEach(cleanup);

  it("steps between hits and between results while marking is on", async () => {
    await renderToolbar(true);

    expect(screen.getByLabelText<HTMLInputElement>("Vis søketreff").checked).toBe(true);
    expect(screen.getByText("Avsnitt – av 2")).toBeTruthy();
    expect(screen.getByText("Mal 1 av 2")).toBeTruthy();
    // Marking was turned on, so the next result opens with it on as well.
    expect(screen.getByTitle("Neste resultat").closest("a")?.getAttribute("href")).toBe(
      "/template/autobrev/B?q=tekst&language=BOKMAL",
    );
  });

  it("still steps between results with marking off, but not between hits", async () => {
    await renderToolbar(false);

    expect(screen.getByLabelText<HTMLInputElement>("Vis søketreff").checked).toBe(false);
    expect(screen.queryByText(/Avsnitt/)).toBeNull();
    expect(screen.getByText("Mal 1 av 2")).toBeTruthy();
    expect(screen.getByTitle("Neste resultat").closest("a")?.getAttribute("href")).toBe(
      "/template/autobrev/B?q=tekst&language=BOKMAL&showHits=false",
    );
  });
});
