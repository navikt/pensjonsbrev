import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import { SearchActivityOutline } from "~/search/components/SearchActivityOutline";

describe("SearchActivityOutline", () => {
  function renderOutline(active: boolean) {
    render(
      <SearchActivityOutline active={active}>
        <input aria-label="søk" />
      </SearchActivityOutline>,
    );
  }

  it("draws a decorative outline, hidden from assistive technology, while active", () => {
    renderOutline(true);

    const outline = screen.getByTestId("search-activity");
    expect(outline.getAttribute("aria-hidden")).toBe("true");
    expect(outline.querySelectorAll("rect.segment").length).toBeGreaterThan(1);
  });

  // Unmounting rather than hiding means an idle page runs no animation at all.
  it("renders no outline while inactive", () => {
    renderOutline(false);

    expect(screen.queryByTestId("search-activity")).toBeNull();
  });

  it.each([true, false])("renders the wrapped field when active is %s", (active) => {
    renderOutline(active);
    expect(screen.getByRole("textbox", { name: "søk" })).toBeTruthy();
  });
});
