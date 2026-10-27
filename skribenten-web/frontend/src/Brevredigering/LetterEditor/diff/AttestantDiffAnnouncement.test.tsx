import { act, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { useState } from "react";
import { beforeEach, describe, expect, test, vi } from "vitest";

import { AttestantDiffAnnouncement } from "./AttestantDiffAnnouncement";

const { useUserInfoMock, useMatchMock } = vi.hoisted(() => ({
  useUserInfoMock: vi.fn(),
  useMatchMock: vi.fn(),
}));

vi.mock("~/hooks/useUserInfo", () => ({ useUserInfo: useUserInfoMock }));
vi.mock("@tanstack/react-router", async (importOriginal) => ({
  ...(await importOriginal<typeof import("@tanstack/react-router")>()),
  useMatch: useMatchMock,
}));

const ATTESTANT_REDIGERING = "/saksnummer_/$saksId/attester/$brevId/redigering";
const TITTEL = "Se manuelle endringer i brevet";

const Harness = ({ showMenu = true }: { showMenu?: boolean }) => {
  const [menu, setMenu] = useState<HTMLDivElement | null>(null);
  const [toggles, setToggles] = useState<HTMLDivElement | null>(null);
  const [dismissed, setDismissed] = useState(false);
  return (
    <>
      {showMenu && (
        <div ref={setMenu}>
          <div ref={setToggles}>Diff-brytere</div>
        </div>
      )}
      <AttestantDiffAnnouncement
        dismissed={dismissed}
        menu={menu}
        onDismiss={() => setDismissed(true)}
        toggles={toggles}
      />
    </>
  );
};

const settle = () => act(() => new Promise((resolve) => setTimeout(resolve, 50)));
const queryAnnouncement = () => screen.queryByRole("alertdialog", { name: TITTEL });

const givenUser = (erAttestant: boolean) =>
  useUserInfoMock.mockReturnValue({ navident: "Z990297", name: "F_Z990297 E_Z990297", erAttestant });
const givenRoute = (routeId: string | undefined) =>
  useMatchMock.mockImplementation(({ from }: { from: string }) => (from === routeId ? { routeId: from } : undefined));

describe("<AttestantDiffAnnouncement />", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    givenUser(true);
    givenRoute(ATTESTANT_REDIGERING);
  });

  test("vises for attestant i attestantens redigeringsvisning", async () => {
    render(<Harness />);

    expect(await screen.findByRole("alertdialog", { name: TITTEL })).not.toBeNull();
    expect(useMatchMock).toHaveBeenCalledWith({ from: ATTESTANT_REDIGERING, shouldThrow: false });
  });

  test("vises ikke for saksbehandler uten attestantrolle", async () => {
    givenUser(false);
    render(<Harness />);
    await settle();

    expect(queryAnnouncement()).toBeNull();
  });

  test("vises ikke før brukerinformasjonen er hentet", async () => {
    useUserInfoMock.mockReturnValue(undefined);
    render(<Harness />);
    await settle();

    expect(queryAnnouncement()).toBeNull();
  });

  test("vises ikke utenfor attestantens redigeringsvisning", async () => {
    givenRoute("/saksnummer_/$saksId/brev/$brevId");
    render(<Harness />);
    await settle();

    expect(queryAnnouncement()).toBeNull();
  });

  test("venter til diff-menyen er rendret", async () => {
    const { rerender } = render(<Harness showMenu={false} />);
    await settle();
    expect(queryAnnouncement()).toBeNull();

    rerender(<Harness showMenu />);
    expect(await screen.findByRole("alertdialog", { name: TITTEL })).not.toBeNull();
  });

  test("lukkes når attestanten ikke lenger er på ruten", async () => {
    const { rerender } = render(<Harness />);
    await screen.findByRole("alertdialog", { name: TITTEL });

    givenRoute(undefined);
    rerender(<Harness />);
    await settle();

    expect(queryAnnouncement()).toBeNull();
  });

  test("kan lukkes", async () => {
    const user = userEvent.setup();
    render(<Harness />);
    await screen.findByRole("alertdialog", { name: TITTEL });

    await user.click(screen.getByRole("button", { name: "Skjønner!" }));
    await settle();

    expect(queryAnnouncement()).toBeNull();
  });
});
