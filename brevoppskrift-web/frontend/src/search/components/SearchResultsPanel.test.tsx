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
});
