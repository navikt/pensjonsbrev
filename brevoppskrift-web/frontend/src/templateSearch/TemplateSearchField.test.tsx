import { act, fireEvent, render, screen, waitFor } from "@testing-library/react";
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

  it("does not search again for a query changed outside the input while its own write is pending", async () => {
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

  it("retires coalesced writes when navigation completes without changing the URL query", async () => {
    const resolveNavigations: Array<() => void> = [];
    const onSearch = vi.fn(
      () =>
        new Promise<void>((resolve) => {
          resolveNavigations.push(resolve);
        }),
    );
    const { rerender } = render(<TemplateSearchField hits={undefined} onSearch={onSearch} q="alder" />);
    const input = screen.getByRole("searchbox", { name: "Søk etter innhold" });

    fireEvent.change(input, { target: { value: "pensjon" } });
    await waitFor(() => expect(onSearch).toHaveBeenCalledTimes(1));

    fireEvent.change(input, { target: { value: "alder" } });
    await waitFor(() => expect(onSearch).toHaveBeenCalledTimes(2));

    await act(async () => {
      for (const resolve of resolveNavigations) {
        resolve();
      }
    });

    rerender(<TemplateSearchField hits={undefined} onSearch={onSearch} q="pensjon" />);
    await waitFor(() => expect((input as HTMLInputElement).value).toBe("pensjon"));
  });
});
