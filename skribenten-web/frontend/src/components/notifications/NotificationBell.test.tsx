import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { addDays, format } from "date-fns";
import { type ReactNode, StrictMode, useState } from "react";
import { beforeEach, describe, expect, test, vi } from "vitest";

import { FeatureAnnouncement } from "~/components/featureAnnouncement/FeatureAnnouncement";

import { DOCUMENTATION_URL, NotificationBell } from "./NotificationBell";
import { type ProductAnnouncement } from "./productAnnouncements";

const { useUserInfoMock, fetchAcknowledgedMock, acknowledgeMock } = vi.hoisted(() => ({
  useUserInfoMock: vi.fn(),
  fetchAcknowledgedMock: vi.fn(),
  acknowledgeMock: vi.fn(),
}));

vi.mock("~/hooks/useUserInfo", () => ({ useUserInfo: useUserInfoMock }));
vi.mock("~/api/skribenten-api-endpoints", async (importOriginal) => {
  const original = await importOriginal<typeof import("~/api/skribenten-api-endpoints")>();
  return {
    ...original,
    getAcknowledgedAnnouncements: { ...original.getAcknowledgedAnnouncements, queryFn: fetchAcknowledgedMock },
    acknowledgeAnnouncement: acknowledgeMock,
  };
});

const NYHET: ProductAnnouncement = {
  id: "test-nyhet",
  replayableUntil: "2999-12-31",
  title: "Ny funksjon",
  description: "Beskrivelse av den nye funksjonen.",
  unavailable: { heading: "Du må åpne redigeringsvisningen", body: "Funksjonen finnes bare i redigeringsvisningen." },
};
const ATTESTANTNYHET: ProductAnnouncement = {
  ...NYHET,
  id: "attestant-nyhet",
  title: "Kun for attestanter",
  isRelevantFor: (user) => user.erAttestant,
};

const givenUser = (erAttestant: boolean) =>
  useUserInfoMock.mockReturnValue({ navident: "Z990297", name: "F_Z990297 E_Z990297", erAttestant });

const renderWithQueryClient = (ui: ReactNode) => {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  return render(ui, {
    wrapper: ({ children }) => (
      <StrictMode>
        <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
      </StrictMode>
    ),
  });
};

const settle = () => act(() => new Promise((resolve) => setTimeout(resolve, 50)));
const bell = () => screen.getByRole("button", { name: /^Nytt i Skribenten og nyttige lenker/ });
const dot = () => screen.queryByTestId("unseen-announcements-dot");

const TourOnPage = () => {
  const [target, setTarget] = useState<HTMLElement | null>(null);
  return (
    <>
      <div ref={setTarget}>Målet</div>
      <FeatureAnnouncement
        id={NYHET.id}
        showUntil={NYHET.replayableUntil}
        steps={target ? [{ target, title: "Gjennomgang av funksjonen", content: "Forklaring" }] : []}
        when={target !== null}
      />
    </>
  );
};

describe("<NotificationBell />", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    givenUser(false);
    fetchAcknowledgedMock.mockResolvedValue([]);
    acknowledgeMock.mockResolvedValue(undefined);
  });

  test("viser rød prikk og antall i navnet når brukeren har nyheter som ikke er bekreftet", async () => {
    renderWithQueryClient(<NotificationBell announcements={[NYHET]} />);

    expect(await screen.findByRole("button", { name: /1 ny nyhet$/ })).not.toBeNull();
    expect(dot()).not.toBeNull();
  });

  test("viser ikke prikk for bekreftede nyheter, men de vises fortsatt i listen", async () => {
    fetchAcknowledgedMock.mockResolvedValue([NYHET.id]);
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[NYHET]} />);
    await settle();

    expect(dot()).toBeNull();
    expect(bell().getAttribute("aria-expanded")).toBe("false");

    await user.click(bell());

    expect(bell().getAttribute("aria-expanded")).toBe("true");
    expect(screen.getByRole("heading", { name: NYHET.title })).not.toBeNull();
    expect(screen.getByText(NYHET.description)).not.toBeNull();
  });

  test("viser ikke prikk når bekreftelsene ikke kan hentes", async () => {
    fetchAcknowledgedMock.mockRejectedValue(new Error("Nettverksfeil"));
    renderWithQueryClient(<NotificationBell announcements={[NYHET]} />);
    await settle();

    expect(dot()).toBeNull();
  });

  test("viser bare nyheter som er relevante for brukeren", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[ATTESTANTNYHET]} />);
    await settle();

    expect(dot()).toBeNull();
    await user.click(bell());
    expect(screen.queryByRole("heading", { name: ATTESTANTNYHET.title })).toBeNull();
    expect(screen.getByText("Ingen nyheter akkurat nå.")).not.toBeNull();
    expect(fetchAcknowledgedMock).not.toHaveBeenCalled();
  });

  test("viser nyheter for attestanter til attestanter", async () => {
    givenUser(true);
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[ATTESTANTNYHET]} />);

    await waitFor(() => expect(dot()).not.toBeNull());
    await user.click(bell());
    expect(screen.getByRole("heading", { name: ATTESTANTNYHET.title })).not.toBeNull();
  });

  test("viser ikke nyheter etter siste visningsdag", async () => {
    const user = userEvent.setup();
    const utloept = { ...NYHET, replayableUntil: format(addDays(new Date(), -1), "yyyy-MM-dd") };
    renderWithQueryClient(<NotificationBell announcements={[utloept]} />);
    await settle();

    expect(dot()).toBeNull();
    await user.click(bell());
    expect(screen.queryByRole("heading", { name: NYHET.title })).toBeNull();
  });

  test("har seksjoner for nyheter og for dokumentasjon, med lenke som åpnes i ny fane", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[NYHET]} />);

    await user.click(bell());

    expect(screen.getByRole("region", { name: "Nytt i Skribenten" })).not.toBeNull();
    const links = screen.getByRole("region", { name: "Dokumentasjon og nyttige lenker" });
    const link = within(links).getByRole("link", { name: /Dokumentasjon og veiledning/ });
    expect(link.getAttribute("href")).toBe(DOCUMENTATION_URL);
    expect(link.getAttribute("target")).toBe("_blank");
  });

  test("«Vis meg» starter gjennomgangen på nytt når den er tilgjengelig på siden", async () => {
    fetchAcknowledgedMock.mockResolvedValue([NYHET.id]);
    const user = userEvent.setup();
    renderWithQueryClient(
      <>
        <NotificationBell announcements={[NYHET]} />
        <TourOnPage />
      </>,
    );
    await settle();
    expect(screen.queryByRole("alertdialog")).toBeNull();

    await user.click(bell());
    await user.click(screen.getByRole("button", { name: "Vis meg", description: NYHET.title }));

    expect(await screen.findByRole("alertdialog", { name: "Gjennomgang av funksjonen" })).not.toBeNull();
    expect(bell().getAttribute("aria-expanded")).toBe("false");

    await user.keyboard("{Escape}");
    await waitFor(() => expect(screen.queryByRole("alertdialog")).toBeNull());
    expect(document.activeElement).toBe(bell());
  });

  test("«Vis meg» forklarer hvor funksjonen finnes når gjennomgangen ikke er tilgjengelig på siden", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[NYHET]} />);

    await user.click(bell());
    await user.click(screen.getByRole("button", { name: "Vis meg" }));

    const dialog = await screen.findByRole("dialog", { name: NYHET.unavailable.heading });
    expect(within(dialog).getByText(NYHET.unavailable.body)).not.toBeNull();

    const closeButtons = within(dialog).getAllByRole("button", { name: "Lukk" });
    await user.click(closeButtons[closeButtons.length - 1]);
    await waitFor(() => expect(screen.queryByRole("dialog", { name: NYHET.unavailable.heading })).toBeNull());
    await waitFor(() => expect(document.activeElement).toBe(bell()));
    expect(acknowledgeMock).not.toHaveBeenCalled();
  });
});
