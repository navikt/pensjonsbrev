import { act, cleanup, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { afterEach, describe, expect, it, vi } from "vitest";

import { type TemplateDocumentation } from "~/api/templateDocumentationTypes";
import { createTemplateWordDocument } from "~/word/exportTemplate";

import { ExportTemplateButton } from "./ExportTemplateButton";

vi.mock("~/word/exportTemplate", () => ({ createTemplateWordDocument: vi.fn() }));

const documentation: TemplateDocumentation = {
  title: [],
  outline: [],
  attachments: [],
  templateModelSpecification: { types: {}, letterModelTypeName: "Dto" },
};

afterEach(() => {
  cleanup();
  vi.restoreAllMocks();
  vi.unstubAllGlobals();
  vi.useRealTimers();
});

describe("Last ned brevoppskrift som Word", () => {
  it("laster ned valgt mal og språk og frigjør nedlastingsadressen", async () => {
    const blob = new Blob(["word"]);
    vi.mocked(createTemplateWordDocument).mockResolvedValue(blob);
    const createObjectURL = vi.fn(() => "blob:word");
    const revokeObjectURL = vi.fn();
    vi.stubGlobal("URL", { createObjectURL, revokeObjectURL });
    const links: HTMLAnchorElement[] = [];
    vi.spyOn(HTMLAnchorElement.prototype, "click").mockImplementation(function (this: HTMLAnchorElement) {
      links.push(this);
    });
    render(<ExportTemplateButton documentation={documentation} language="NYNORSK" templateId="MITT_BREV" />);
    vi.useFakeTimers();
    await act(async () => screen.getByRole("button", { name: "Last ned brevoppskrift som Word" }).click());
    expect(links).toHaveLength(1);

    expect(createTemplateWordDocument).toHaveBeenCalledWith({
      documentation,
      language: "NYNORSK",
      templateId: "MITT_BREV",
    });
    const link = links[0];
    expect(link.download).toBe("MITT_BREV-NYNORSK.docx");
    expect(link.href).toBe("blob:word");
    expect(link.isConnected).toBe(false);
    expect(createObjectURL).toHaveBeenCalledWith(blob);
    expect(revokeObjectURL).not.toHaveBeenCalled();

    await act(async () => vi.runAllTimersAsync());
    expect(revokeObjectURL).toHaveBeenCalledWith("blob:word");
  });

  it("viser feil og tillater nytt forsøk hvis eksporten feiler", async () => {
    vi.mocked(createTemplateWordDocument).mockRejectedValue(new Error("Eksportfeil"));
    render(<ExportTemplateButton documentation={documentation} language="BOKMAL" templateId="BREV" />);

    await userEvent.click(screen.getByRole("button"));
    expect(await screen.findByRole("alert")).toBeTruthy();
    expect(screen.getByText("Kunne ikke eksportere brevoppskriften til Word. Prøv igjen.")).toBeTruthy();
    expect(screen.getByRole("button", { name: "Last ned brevoppskrift som Word" }).getAttribute("disabled")).toBeNull();
  });
});
