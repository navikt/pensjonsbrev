import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";

import { TemplateSearchField } from "~/templateSearch/TemplateSearchField";

describe("TemplateSearchField", () => {
  it("writes the latest input even when it returns to the current URL query", async () => {
    const onSearch = vi.fn();
    const { rerender } = render(<TemplateSearchField hits={undefined} onSearch={onSearch} q="pensjon" />);
    const input = screen.getByRole("searchbox", { name: "Søk etter innhold" });

    fireEvent.change(input, { target: { value: "pen" } });
    await waitFor(() => expect(onSearch).toHaveBeenLastCalledWith("pen"));

    fireEvent.change(input, { target: { value: "pensjon" } });
    await waitFor(() => expect(onSearch).toHaveBeenLastCalledWith("pensjon"));

    rerender(<TemplateSearchField hits={undefined} onSearch={onSearch} q="pen" />);
    expect((input as HTMLInputElement).value).toBe("pensjon");
  });

  it("follows a query changed outside the input", async () => {
    const onSearch = vi.fn();
    const { rerender } = render(<TemplateSearchField hits={undefined} onSearch={onSearch} q="pensjon" />);
    const input = screen.getByRole("searchbox", { name: "Søk etter innhold" });

    rerender(<TemplateSearchField hits={undefined} onSearch={onSearch} q="uføretrygd" />);

    await waitFor(() => expect((input as HTMLInputElement).value).toBe("uføretrygd"));
  });
});
