import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, render as renderWithoutClient, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { type ReactNode, useState } from "react";
import { beforeEach, describe, expect, test, vi } from "vitest";

import { AttestantDiffAnnouncement } from "./AttestantDiffAnnouncement";

const { useUserInfoMock, useMatchMock, fetchAcknowledgedMock, acknowledgeMock } = vi.hoisted(() => ({
  useUserInfoMock: vi.fn(),
  useMatchMock: vi.fn(),
  fetchAcknowledgedMock: vi.fn(),
  acknowledgeMock: vi.fn(),
}));

vi.mock("~/hooks/useUserInfo", () => ({ useUserInfo: useUserInfoMock }));
vi.mock("@tanstack/react-router", async (importOriginal) => ({
  ...(await importOriginal<typeof import("@tanstack/react-router")>()),
  useMatch: useMatchMock,
}));
vi.mock("~/api/skribenten-api-endpoints", async (importOriginal) => {
  const original = await importOriginal<typeof import("~/api/skribenten-api-endpoints")>();
  return {
    ...original,
    getAcknowledgedAnnouncements: { ...original.getAcknowledgedAnnouncements, queryFn: fetchAcknowledgedMock },
    acknowledgeAnnouncement: acknowledgeMock,
  };
});

const ATTESTANT_REDIGERING = "/saksnummer_/$saksId/attester/$brevId/redigering";
const TITTEL = "Se manuelle endringer i brevet";

const Harness = ({ showMenu = true }: { showMenu?: boolean }) => {
  const [menu, setMenu] = useState<HTMLDivElement | null>(null);
  const [toggles, setToggles] = useState<HTMLDivElement | null>(null);
  return (
    <>
      {showMenu && (
        <div ref={setMenu}>
          <div ref={setToggles}>Diff-brytere</div>
        </div>
      )}
      <AttestantDiffAnnouncement menu={menu} toggles={toggles} />
    </>
  );
};

const queryClient = () => new QueryClient({ defaultOptions: { queries: { retry: false } } });
const render = (ui: ReactNode) => {
  const client = queryClient();
  return renderWithoutClient(ui, {
    wrapper: ({ children }) => <QueryClientProvider client={client}>{children}</QueryClientProvider>,
  });
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
    fetchAcknowledgedMock.mockResolvedValue([]);
    acknowledgeMock.mockResolvedValue(undefined);
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

  test("kan lukkes, og bekreftelsen lagres", async () => {
    const user = userEvent.setup();
    render(<Harness />);
    await screen.findByRole("alertdialog", { name: TITTEL });

    await user.click(screen.getByRole("button", { name: "Skjønner!" }));
    await settle();

    expect(queryAnnouncement()).toBeNull();
    expect(acknowledgeMock).toHaveBeenCalledWith("attestant-diff");
  });

  test("vises ikke når attestanten allerede har bekreftet nyheten", async () => {
    fetchAcknowledgedMock.mockResolvedValue(["attestant-diff"]);
    render(<Harness />);
    await settle();

    expect(queryAnnouncement()).toBeNull();
  });
});
