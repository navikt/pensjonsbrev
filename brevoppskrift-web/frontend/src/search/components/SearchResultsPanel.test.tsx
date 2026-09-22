import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import { SearchResultsPanel } from "~/search/components/SearchResultsPanel";

describe("SearchResultsPanel", () => {
  function renderPanel(isPending: boolean) {
    render(
      <SearchResultsPanel isPending={isPending} page={1} pageCount={1} setPage={() => undefined}>
        <div>treff</div>
      </SearchResultsPanel>,
    );
    return screen.getByText("treff").parentElement;
  }

  // `aria-busy` is now the only staleness marker the panel carries, so these
  // assertions keep it wired to the DOM through Aksel's VStack.
  it("marks the results as busy while a newer query is in flight", () => {
    expect(renderPanel(true)?.getAttribute("aria-busy")).toBe("true");
  });

  it("clears the busy marker once the results are up to date", () => {
    expect(renderPanel(false)?.getAttribute("aria-busy")).toBe("false");
  });

  // The retained results must stay fully readable while a newer search runs:
  // compositing Aksel's text colours at reduced opacity drops them below the
  // WCAG 1.4.3 4.5:1 floor (text-subtle lands at ~2.4:1 at 50%). The visual
  // "still searching" signal belongs in the status line, not here.
  it("does not dim the retained results while pending", () => {
    const list = renderPanel(true);
    expect(list?.getAttribute("data-pending")).toBeNull();
    const opacity = list ? getComputedStyle(list).opacity : "";
    expect(opacity === "" || opacity === "1").toBe(true);
  });
});
