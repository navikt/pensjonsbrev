import { act, cleanup, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { useState } from "react";
import { afterEach, describe, expect, it, vi } from "vitest";

import { LanguageToggle } from "./LanguageToggle";

function ControlledToggle({ available, onChange }: { available: string[]; onChange: (language: string) => void }) {
  const [value, setValue] = useState(available[0]);
  return (
    <LanguageToggle
      available={available}
      onChange={(language) => {
        onChange(language);
        setValue(language);
      }}
      value={value}
    />
  );
}

describe("LanguageToggle", () => {
  afterEach(cleanup);

  it("shows only the template's languages, in fixed order, by short name and named in full", () => {
    render(<LanguageToggle available={["ENGLISH", "BOKMAL"]} onChange={vi.fn()} value="BOKMAL" />);

    expect(screen.getAllByRole("radio").map((radio) => radio.textContent)).toEqual(["NB", "EN"]);
    expect(screen.getByRole("radio", { name: "Bokmål" }).getAttribute("aria-checked")).toBe("true");
    expect(screen.getByRole("radio", { name: "Engelsk" }).getAttribute("aria-checked")).toBe("false");
  });

  it("switches language on the arrow keys alone", async () => {
    const user = userEvent.setup();
    const onChange = vi.fn();
    render(<ControlledToggle available={["BOKMAL", "ENGLISH"]} onChange={onChange} />);

    await user.tab();
    expect(onChange).not.toHaveBeenCalled();

    await user.keyboard("{ArrowRight}");
    expect(onChange).toHaveBeenLastCalledWith("ENGLISH");
    expect(screen.getByRole("radio", { name: "Engelsk" }).getAttribute("aria-checked")).toBe("true");

    await user.keyboard("{ArrowLeft}");
    expect(onChange).toHaveBeenLastCalledWith("BOKMAL");
  });

  it("wraps around at the ends, so the other languages are one key press away", async () => {
    const user = userEvent.setup();
    const onChange = vi.fn();
    render(<ControlledToggle available={["BOKMAL", "NYNORSK", "ENGLISH"]} onChange={onChange} />);
    await user.tab();

    await user.keyboard("{ArrowLeft}");
    expect(onChange).toHaveBeenLastCalledWith("ENGLISH");
    expect(document.activeElement).toBe(screen.getByRole("radio", { name: "Engelsk" }));
    expect(screen.getByRole("radio", { name: "Engelsk" }).getAttribute("aria-checked")).toBe("true");

    await user.keyboard("{ArrowRight}");
    expect(onChange).toHaveBeenLastCalledWith("BOKMAL");
    expect(document.activeElement).toBe(screen.getByRole("radio", { name: "Bokmål" }));
    expect(onChange).toHaveBeenCalledTimes(2);
  });

  describe("while the chosen language loads", () => {
    const checked = (name: string) => screen.getByRole("radio", { name }).getAttribute("aria-checked");
    const spinners = () => document.querySelectorAll("svg title");

    it("selects it right away and shows a spinner on it", async () => {
      const user = userEvent.setup();
      render(<LanguageToggle available={["BOKMAL", "ENGLISH"]} onChange={vi.fn()} value="BOKMAL" />);
      await user.tab();

      await user.keyboard("{ArrowRight}");

      expect(checked("Engelsk")).toBe("true");
      expect(checked("Bokmål")).toBe("false");
      expect(screen.getByRole("radio", { name: "Engelsk" }).querySelector("svg")).not.toBeNull();
      expect(spinners()).toHaveLength(1);
      expect(screen.getByRole("radio", { name: "Engelsk" }).textContent).not.toContain("EN");
    });

    it("switches back to the shown language when focus returns to it", async () => {
      const user = userEvent.setup();
      const onChange = vi.fn();
      render(<LanguageToggle available={["BOKMAL", "ENGLISH"]} onChange={onChange} value="BOKMAL" />);
      await user.tab();

      await user.keyboard("{ArrowLeft}");
      await user.keyboard("{ArrowRight}");

      expect(onChange.mock.calls).toEqual([["ENGLISH"], ["BOKMAL"]]);
      expect(checked("Bokmål")).toBe("true");
      expect(spinners()).toHaveLength(0);
    });

    it("drops the spinner once the language has loaded, and follows a language changed elsewhere", async () => {
      const user = userEvent.setup();
      const { rerender } = render(
        <LanguageToggle available={["BOKMAL", "NYNORSK", "ENGLISH"]} onChange={vi.fn()} value="BOKMAL" />,
      );
      await user.tab();
      await user.keyboard("{ArrowRight}");

      rerender(<LanguageToggle available={["BOKMAL", "NYNORSK", "ENGLISH"]} onChange={vi.fn()} value="NYNORSK" />);
      expect(checked("Nynorsk")).toBe("true");
      expect(spinners()).toHaveLength(0);

      rerender(<LanguageToggle available={["BOKMAL", "NYNORSK", "ENGLISH"]} onChange={vi.fn()} value="ENGLISH" />);
      expect(checked("Engelsk")).toBe("true");
      expect(spinners()).toHaveLength(0);
    });

    it("falls back to the shown language when its latest navigation settles without it, e.g. redirected", async () => {
      const user = userEvent.setup();
      const settle: (() => void)[] = [];
      const onChange = () => new Promise<void>((resolve) => settle.push(resolve));
      render(<LanguageToggle available={["BOKMAL", "NYNORSK", "ENGLISH"]} onChange={onChange} value="BOKMAL" />);
      await user.tab();
      await user.keyboard("{ArrowRight}");
      await user.keyboard("{ArrowRight}");

      await act(async () => settle[0]?.());
      expect(checked("Engelsk")).toBe("true");
      expect(spinners()).toHaveLength(1);

      await act(async () => settle[1]?.());
      expect(checked("Bokmål")).toBe("true");
      expect(spinners()).toHaveLength(0);
    });
  });
});
