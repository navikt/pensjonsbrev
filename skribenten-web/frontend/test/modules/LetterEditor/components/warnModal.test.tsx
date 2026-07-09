import { render, screen } from "@testing-library/react";
import { describe, expect, test, vi } from "vitest";

import { WarnModal } from "~/Brevredigering/LetterEditor/components/warnModal";

describe("WarnModal", () => {
  test("renders nothing when closed", () => {
    const { container } = render(
      <WarnModal count={1} kind="fritekst" onClose={vi.fn()} onFortsett={vi.fn()} open={false} />,
    );
    expect(container.innerHTML).toBe("");
  });

  test("renders fritekst heading and body", () => {
    render(<WarnModal count={2} kind="fritekst" onClose={vi.fn()} onFortsett={vi.fn()} open />);

    expect(screen.queryByText("Du må fylle ut 2 fritekstfelt")).not.toBeNull();
    expect(
      screen.queryByText("Du kan fortsette, men brevet kan ikke sendes før alle fritekstfelter er fylt ut."),
    ).not.toBeNull();
  });

  test("renders tekstValg heading and body", () => {
    render(<WarnModal kind="tekstValg" onClose={vi.fn()} onFortsett={vi.fn()} open />);

    expect(screen.queryByText("Du må velge tekst")).not.toBeNull();
    expect(
      screen.queryByText(
        "Du kan fortsette, men brevet kan ikke sendes før du har valgt et eller flere obligatoriske tekstvalg.",
      ),
    ).not.toBeNull();
  });

  test("renders fritekstOgTekstValg heading and body", () => {
    render(<WarnModal count={3} kind="fritekstOgTekstValg" onClose={vi.fn()} onFortsett={vi.fn()} open />);

    expect(screen.queryByText("Du må fylle ut 3 fritekstfelt og velge tekst")).not.toBeNull();
  });

  test("renders avsnittIkkeIMal as its own heading and body, separate from fritekst/tekstValg", () => {
    render(<WarnModal count={2} kind="avsnittIkkeIMal" onClose={vi.fn()} onFortsett={vi.fn()} open />);

    expect(screen.queryByText("Du må velge om du vil beholde eller slette 2 avsnitt")).not.toBeNull();
    expect(
      screen.queryByText(
        "Disse avsnittene er markert i brevet. Velg «Behold» eller «Slett» for hvert av dem. Du kan fortsette, men brevet kan ikke sendes før dette er gjort.",
      ),
    ).not.toBeNull();
  });

  test("renders avsnittIkkeIMal body in singular when count is 1", () => {
    render(<WarnModal count={1} kind="avsnittIkkeIMal" onClose={vi.fn()} onFortsett={vi.fn()} open />);

    expect(screen.queryByText("Du må velge om du vil beholde eller slette 1 avsnitt")).not.toBeNull();
    expect(
      screen.queryByText(
        "Dette avsnittet er markert i brevet. Velg «Behold» eller «Slett». Du kan fortsette, men brevet kan ikke sendes før dette er gjort.",
      ),
    ).not.toBeNull();
  });

  test("uses the default fortsett label when none is provided", () => {
    render(<WarnModal kind="tekstValg" onClose={vi.fn()} onFortsett={vi.fn()} open />);

    expect(screen.queryByText("Fortsett til brevbehandler")).not.toBeNull();
  });

  test("renders a configurable fortsett label", () => {
    render(
      <WarnModal
        fortsettLabel="Fortsett til forhåndsvisning"
        kind="tekstValg"
        onClose={vi.fn()}
        onFortsett={vi.fn()}
        open
      />,
    );

    expect(screen.queryByText("Fortsett til forhåndsvisning")).not.toBeNull();
    expect(screen.queryByText("Fortsett til brevbehandler")).toBeNull();
  });
});
