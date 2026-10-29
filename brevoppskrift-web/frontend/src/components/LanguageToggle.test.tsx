import { cleanup, render, screen } from "@testing-library/react";
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
});
