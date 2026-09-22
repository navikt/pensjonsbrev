import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import { SearchStatus } from "~/search/components/SearchStatus";

describe("SearchStatus", () => {
  /** True when `first` precedes `second` in document order. */
  function precedes(first: Element, second: Element): boolean {
    return (first.compareDocumentPosition(second) & Node.DOCUMENT_POSITION_FOLLOWING) !== 0;
  }

  // The icon trails the message so the eye lands on the text first and the
  // status reads as one sentence rather than as a decorated row.
  it("renders the spinner after the message while a search runs", () => {
    render(
      <SearchStatus isDone={false} isWorking={true}>
        Søker i innholdet
      </SearchStatus>,
    );
    expect(precedes(screen.getByText("Søker i innholdet"), screen.getByTitle("Søker"))).toBe(true);
  });

  it("renders the checkmark after the message once the search is done", () => {
    render(
      <SearchStatus isDone={true} isWorking={false}>
        Frasen du søker på er brukt i 2 maler
      </SearchStatus>,
    );
    expect(precedes(screen.getByText(/Frasen du søker på/), screen.getByTitle("Søket er fullført"))).toBe(true);
  });

  // While a newer search is in flight the spinner wins: showing a checkmark for
  // the previous search next to an outdated message would claim the results on
  // screen are current.
  it("shows only the spinner when a new search starts after a completed one", () => {
    render(
      <SearchStatus isDone={true} isWorking={true}>
        Søker i innholdet
      </SearchStatus>,
    );
    expect(screen.getByTitle("Søker")).toBeTruthy();
    expect(screen.queryByTitle("Søket er fullført")).toBeNull();
  });

  it("shows no icon before the first search", () => {
    render(
      <SearchStatus isDone={false} isWorking={false}>
        Søker i innholdet
      </SearchStatus>,
    );
    expect(screen.queryByTitle("Søker")).toBeNull();
    expect(screen.queryByTitle("Søket er fullført")).toBeNull();
  });
});
