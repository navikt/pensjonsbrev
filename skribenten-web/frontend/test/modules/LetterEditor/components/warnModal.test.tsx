import { render, screen } from "@testing-library/react";
import { describe, expect, test, vi } from "vitest";

import { WarnModal } from "~/Brevredigering/LetterEditor/components/warnModal";

describe("WarnModal", () => {
  test("renders nothing when closed", () => {
    const { container } = render(
      <WarnModal kind="fritekst" count={1} onClose={vi.fn()} onFortsett={vi.fn()} open={false} />,
    );
    expect(container.innerHTML).toBe("");
  });

  test("renders fritekst heading and body", () => {
    render(<WarnModal kind="fritekst" count={2} onClose={vi.fn()} onFortsett={vi.fn()} open />);

    expect(screen.queryByText("Du må fylle ut 2 fritekstfelt")).not.toBeNull();
    expect(
      screen.queryByText(
        "Du kan fortsette til brevbehandler, men brevet kan ikke sendes før alle fritekstfelter er fylt ut.",
      ),
    ).not.toBeNull();
  });

  test("renders tekstValg heading and body", () => {
    render(<WarnModal kind="tekstValg" onClose={vi.fn()} onFortsett={vi.fn()} open />);

    expect(screen.queryByText("Du må velge tekst")).not.toBeNull();
    expect(
      screen.queryByText(
        "Du kan fortsette til brevbehandler, men brevet kan ikke sendes før du har valgt et eller flere obligatoriske tekstvalg.",
      ),
    ).not.toBeNull();
  });

  test("renders fritekstOgTekstValg heading and body", () => {
    render(<WarnModal count={3} kind="fritekstOgTekstValg" onClose={vi.fn()} onFortsett={vi.fn()} open />);

    expect(screen.queryByText("Du må fylle ut 3 fritekstfelt og velge tekst")).not.toBeNull();
  });

  test("renders duplikatAvsnitt as its own heading and body, separate from fritekst/tekstValg", () => {
    render(<WarnModal count={2} kind="duplikatAvsnitt" onClose={vi.fn()} onFortsett={vi.fn()} open />);

    expect(screen.queryByText("Brevet inneholder 2 avsnitt som ikke lenger er del av malen")).not.toBeNull();
    expect(
      screen.queryByText(
        "Du kan fortsette til brevbehandler, men brevet kan ikke sendes før avsnittene som ikke lenger kan kobles til malen (markert i brevet) er fjernet eller redigert.",
      ),
    ).not.toBeNull();
  });
});

