import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { useState } from "react";
import { describe, expect, it, vi } from "vitest";

import { TemplateSearchField } from "~/templateSearch/TemplateSearchField";

// Like the router, which knows the query of a navigation as soon as it starts.
function Routed({ initial, onSearch }: { initial: string; onSearch: (q: string | undefined) => void }) {
  const [q, setQ] = useState<string | undefined>(initial);
  const search = (newQ: string | undefined) => {
    onSearch(newQ);
    setQ(newQ);
  };
  return <TemplateSearchField hits={undefined} onSearch={search} q={q} />;
}

describe("TemplateSearchField", () => {
  it("writes the latest input, also when it returns to an earlier query", async () => {
    const onSearch = vi.fn();
    render(<Routed initial="pensjon" onSearch={onSearch} />);
    const input = screen.getByRole("searchbox", { name: "Søk etter innhold" });

    fireEvent.change(input, { target: { value: "pen" } });
    await waitFor(() => expect(onSearch).toHaveBeenLastCalledWith("pen"));
    fireEvent.change(input, { target: { value: "pensjon" } });
    await waitFor(() => expect(onSearch).toHaveBeenLastCalledWith("pensjon"));

    expect(onSearch).toHaveBeenCalledTimes(2);
    expect((input as HTMLInputElement).value).toBe("pensjon");
  });

  it("follows a query changed outside the input", async () => {
    const onSearch = vi.fn();
    const { rerender } = render(<TemplateSearchField hits={undefined} onSearch={onSearch} q="pensjon" />);
    const input = screen.getByRole("searchbox", { name: "Søk etter innhold" });

    rerender(<TemplateSearchField hits={undefined} onSearch={onSearch} q="uføretrygd" />);

    await waitFor(() => expect((input as HTMLInputElement).value).toBe("uføretrygd"));
  });

  it("follows a query changed outside the input when its own write was dropped, without searching again", async () => {
    const onSearch = vi.fn();
    const { rerender } = render(<TemplateSearchField hits={undefined} onSearch={onSearch} q="pensjon" />);
    const input = screen.getByRole("searchbox", { name: "Søk etter innhold" });

    fireEvent.change(input, { target: { value: "alder" } });
    await waitFor(() => expect(onSearch).toHaveBeenLastCalledWith("alder"));

    rerender(<TemplateSearchField hits={undefined} onSearch={onSearch} q="uføretrygd" />);
    await waitFor(() => expect((input as HTMLInputElement).value).toBe("uføretrygd"));
    await new Promise((resolve) => setTimeout(resolve, 0));
    expect(onSearch).toHaveBeenCalledTimes(1);
  });

  it("follows a query changed outside the input after a repeated query was written", async () => {
    const onSearch = vi.fn();
    const { rerender } = render(<TemplateSearchField hits={undefined} onSearch={onSearch} q="" />);
    const input = screen.getByRole("searchbox", { name: "Søk etter innhold" });

    for (const value of ["alder", "pensjon", "alder"]) {
      fireEvent.change(input, { target: { value } });
      await waitFor(() => expect(onSearch).toHaveBeenLastCalledWith(value));
    }
    expect(onSearch).toHaveBeenCalledTimes(3);

    rerender(<TemplateSearchField hits={undefined} onSearch={onSearch} q="alder" />);
    rerender(<TemplateSearchField hits={undefined} onSearch={onSearch} q="pensjon" />);
    await waitFor(() => expect((input as HTMLInputElement).value).toBe("pensjon"));
  });
});
