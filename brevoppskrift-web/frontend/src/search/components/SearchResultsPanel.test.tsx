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

  // The dimming is driven by a static Emotion rule keyed off `data-pending`
  // rather than an interpolated opacity, so these two assertions are what keep
  // that rule connected to the DOM: if the underlying component ever stopped
  // forwarding `data-*`, the results would silently never dim again.
  it("marks the results as busy and pending while a newer query is in flight", () => {
    const list = renderPanel(true);
    expect(list?.getAttribute("data-pending")).toBe("true");
    expect(list?.getAttribute("aria-busy")).toBe("true");
  });

  it("clears both markers once the results are up to date", () => {
    const list = renderPanel(false);
    expect(list?.getAttribute("data-pending")).toBe("false");
    expect(list?.getAttribute("aria-busy")).toBe("false");
  });
});
