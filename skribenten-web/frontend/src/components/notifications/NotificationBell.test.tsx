import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { addDays, format } from "date-fns";
import { type ReactNode, StrictMode, useState } from "react";
import { beforeEach, describe, expect, test, vi } from "vitest";

import { FeatureAnnouncement } from "~/components/featureAnnouncement/FeatureAnnouncement";

import { NotificationBell } from "./NotificationBell";
import { type ProductAnnouncement } from "./productAnnouncements";

const { useUserInfoMock, fetchAcknowledgedMock, acknowledgeMock, fetchSeenMock, markSeenMock } = vi.hoisted(() => ({
  useUserInfoMock: vi.fn(),
  fetchAcknowledgedMock: vi.fn(),
  acknowledgeMock: vi.fn(),
  fetchSeenMock: vi.fn(),
  markSeenMock: vi.fn(),
}));

vi.mock("~/hooks/useUserInfo", () => ({ useUserInfo: useUserInfoMock }));
vi.mock("~/api/skribenten-api-endpoints", async (importOriginal) => {
  const original = await importOriginal<typeof import("~/api/skribenten-api-endpoints")>();
  return {
    ...original,
    getAcknowledgedAnnouncements: { ...original.getAcknowledgedAnnouncements, queryFn: fetchAcknowledgedMock },
    acknowledgeAnnouncement: acknowledgeMock,
    getSeenAnnouncements: { ...original.getSeenAnnouncements, queryFn: fetchSeenMock },
    markAnnouncementSeen: markSeenMock,
  };
});

const NYHET: ProductAnnouncement = {
  id: "test-nyhet",
  replayableUntil: "2999-12-31",
  publishedAt: "2026-11-10",
  title: "Ny funksjon",
  description: "Beskrivelse av den nye funksjonen.",
  documentationUrl: "https://dokumentasjon.example/ny-funksjon",
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
const bell = () => screen.getByRole("button", { name: /^Nytt i Skribenten/ });
const dot = () => screen.queryByTestId("unseen-announcements-dot");
const announcementsDialog = () => screen.queryByRole("dialog", { name: "Nytt i Skribenten" });
const newsItem = (title: string) => {
  const item = screen.getByRole("heading", { name: title }).closest("li");
  if (!item) throw new Error(`Fant ikke nyheten ${title}`);
  return item;
};

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
    fetchSeenMock.mockResolvedValue([]);
    markSeenMock.mockResolvedValue(undefined);
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
    expect(bell().getAttribute("aria-haspopup")).toBe("dialog");
    expect(announcementsDialog()).toBeNull();

    await user.click(bell());

    expect(announcementsDialog()).not.toBeNull();
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

  test("åpning av klokken markerer nyhetene som sett, fjerner prikken og beholder dem i listen", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(
      <NotificationBell announcements={[NYHET, { ...NYHET, id: "annen-nyhet", title: "Annen" }]} />,
    );
    await waitFor(() => expect(dot()).not.toBeNull());

    await user.click(bell());

    expect(dot()).toBeNull();
    expect(markSeenMock).toHaveBeenCalledWith(NYHET.id);
    expect(markSeenMock).toHaveBeenCalledWith("annen-nyhet");
    expect(acknowledgeMock).not.toHaveBeenCalled();
    expect(screen.getByRole("heading", { name: NYHET.title })).not.toBeNull();

    await user.click(bell());
    await user.click(bell());
    expect(markSeenMock).toHaveBeenCalledTimes(2);
  });

  test("viser ikke prikk for nyheter som er sett, og markerer dem ikke på nytt", async () => {
    fetchSeenMock.mockResolvedValue([NYHET.id]);
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[NYHET]} />);
    await settle();

    expect(dot()).toBeNull();
    await user.click(bell());
    expect(markSeenMock).not.toHaveBeenCalled();
  });

  test("markerer ikke bekreftede nyheter som sett", async () => {
    fetchAcknowledgedMock.mockResolvedValue([NYHET.id]);
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[NYHET]} />);
    await settle();

    await user.click(bell());

    expect(markSeenMock).not.toHaveBeenCalled();
  });

  test("viser ikke prikk når det ikke kan hentes hvilke nyheter som er sett", async () => {
    fetchSeenMock.mockRejectedValue(new Error("Nettverksfeil"));
    renderWithQueryClient(<NotificationBell announcements={[NYHET]} />);
    await settle();

    expect(dot()).toBeNull();
  });

  test("gjennomgangen starter fortsatt automatisk etter at nyheten er sett i klokken", async () => {
    const user = userEvent.setup();
    const { rerender } = renderWithQueryClient(<NotificationBell announcements={[NYHET]} />);
    await waitFor(() => expect(dot()).not.toBeNull());
    await user.click(bell());
    await user.click(screen.getByRole("button", { name: "Lukk" }));
    await waitFor(() => expect(announcementsDialog()).toBeNull());

    rerender(
      <>
        <NotificationBell announcements={[NYHET]} />
        <TourOnPage />
      </>,
    );

    expect(await screen.findByRole("alertdialog", { name: "Gjennomgang av funksjonen" })).not.toBeNull();
  });

  test("«Se hvordan» starter gjennomgangen på nytt når den er tilgjengelig på siden", async () => {
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
    await user.click(screen.getByRole("button", { name: "Se hvordan", description: NYHET.title }));

    expect(await screen.findByRole("alertdialog", { name: "Gjennomgang av funksjonen" })).not.toBeNull();
    expect(announcementsDialog()).toBeNull();

    await user.keyboard("{Escape}");
    await waitFor(() => expect(screen.queryByRole("alertdialog")).toBeNull());
    expect(document.activeElement).toBe(bell());
  });

  test("viser dato, tittel og beskrivelse", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[NYHET]} />);

    await user.click(bell());

    const item = newsItem(NYHET.title);
    const date = within(item).getByText("10.11.2026");
    expect(date.tagName).toBe("TIME");
    expect(date.getAttribute("dateTime")).toBe("2026-11-10");
    expect(within(item).getByText(NYHET.description)).not.toBeNull();
  });

  test("viser bare «Se dokumentasjon» når gjennomgangen ikke er tilgjengelig på siden", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[NYHET]} />);

    await user.click(bell());

    expect(screen.queryByRole("button", { name: "Se hvordan" })).toBeNull();
    const link = screen.getByRole("link", {
      name: /^Se dokumentasjon\s*\(åpnes i ny fane\)$/,
      description: NYHET.title,
    });
    expect(link.getAttribute("href")).toBe(NYHET.documentationUrl);
    expect(link.getAttribute("target")).toBe("_blank");
  });

  test("viser både «Se hvordan» og «Se dokumentasjon» når gjennomgangen er tilgjengelig på siden", async () => {
    fetchAcknowledgedMock.mockResolvedValue([NYHET.id]);
    const user = userEvent.setup();
    renderWithQueryClient(
      <>
        <NotificationBell announcements={[NYHET]} />
        <TourOnPage />
      </>,
    );
    await settle();

    await user.click(bell());

    expect(screen.getByRole("button", { name: "Se hvordan", description: NYHET.title })).not.toBeNull();
    expect(screen.getByRole("link", { name: /^Se dokumentasjon\s*\(åpnes i ny fane\)$/ })).not.toBeNull();
  });
});
