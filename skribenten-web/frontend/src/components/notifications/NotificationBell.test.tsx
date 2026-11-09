import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { type ReactNode, StrictMode, useState } from "react";
import { beforeEach, describe, expect, test, vi } from "vitest";

import { FeatureAnnouncement } from "~/components/featureAnnouncement/FeatureAnnouncement";

import { NotificationBell } from "./NotificationBell";
import { DIFF_TOGGLES_ANNOUNCEMENT, listedUntil, type ProductAnnouncement } from "./productAnnouncements";

const { fetchAcknowledgedMock, acknowledgeMock, fetchSeenMock, markSeenMock } = vi.hoisted(() => ({
  fetchAcknowledgedMock: vi.fn(),
  acknowledgeMock: vi.fn(),
  fetchSeenMock: vi.fn(),
  markSeenMock: vi.fn(),
}));

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
  publishedAt: "2026-11-10",
  title: "Ny funksjon",
  description: "Beskrivelse av den nye funksjonen.",
  documentationUrl: "https://dokumentasjon.example/ny-funksjon",
};

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
        showUntil={listedUntil(NYHET)}
        steps={target ? [{ target, title: "Gjennomgang av funksjonen", content: "Forklaring" }] : []}
        when={target !== null}
      />
    </>
  );
};

describe("<NotificationBell />", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    fetchAcknowledgedMock.mockResolvedValue([]);
    acknowledgeMock.mockResolvedValue(undefined);
    fetchSeenMock.mockResolvedValue([]);
    markSeenMock.mockResolvedValue(undefined);
    vi.useFakeTimers({ toFake: ["Date"] });
    vi.setSystemTime(new Date("2026-11-15T12:00:00"));
    return () => vi.useRealTimers();
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

  test("viser nyheten til og med tre måneder etter publisering, men ikke dagen etter", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[{ ...NYHET, publishedAt: "2026-08-15" }]} />);
    await user.click(bell());
    expect(screen.getByRole("heading", { name: NYHET.title })).not.toBeNull();
  });

  test("viser ikke nyheter etter siste visningsdag", async () => {
    const user = userEvent.setup();
    const utloept = { ...NYHET, publishedAt: "2026-08-14" };
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

  test("viser nyheten om endringer i brevet med lenke til dokumentasjonen", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell />);
    await waitFor(() => expect(dot()).not.toBeNull());

    await user.click(bell());

    expect(screen.getByRole("heading", { name: DIFF_TOGGLES_ANNOUNCEMENT.title })).not.toBeNull();
    expect(screen.queryByRole("button", { name: "Se hvordan" })).toBeNull();
    expect(screen.getByRole("link", { name: /^Se dokumentasjon/ })).not.toBeNull();
  });

  test("markerer nyheten som sett når klokken åpnes før statusen er hentet", async () => {
    let answerSeen: (ids: string[]) => void = () => {};
    fetchSeenMock.mockReturnValue(new Promise<string[]>((resolve) => (answerSeen = resolve)));
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[NYHET]} />);

    await user.click(bell());
    expect(screen.getByRole("heading", { name: NYHET.title })).not.toBeNull();
    await act(async () => answerSeen([]));
    await settle();

    expect(markSeenMock).toHaveBeenCalledTimes(1);
    expect(markSeenMock).toHaveBeenCalledWith(NYHET.id);
    expect(dot()).toBeNull();
  });

  test("viser ikke nyheter før publiseringsdatoen, og markerer dem ikke som sett", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[{ ...NYHET, publishedAt: "2026-11-16" }]} />);
    await settle();

    expect(dot()).toBeNull();
    await user.click(bell());
    expect(screen.queryByRole("heading", { name: NYHET.title })).toBeNull();
    expect(markSeenMock).not.toHaveBeenCalled();
  });

  test("viser nyheten fra og med publiseringsdatoen", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<NotificationBell announcements={[{ ...NYHET, publishedAt: "2026-11-15" }]} />);

    await user.click(bell());

    expect(screen.getByRole("heading", { name: NYHET.title })).not.toBeNull();
  });

  test("«Se hvordan» venter på bekreftelsesstatusen, og gjennomgangen åpnes ikke igjen etter at den er lukket", async () => {
    let answerAcknowledged: (ids: string[]) => void = () => {};
    fetchAcknowledgedMock.mockReturnValue(new Promise<string[]>((resolve) => (answerAcknowledged = resolve)));
    const user = userEvent.setup();
    renderWithQueryClient(
      <>
        <NotificationBell announcements={[NYHET]} />
        <TourOnPage />
      </>,
    );
    await user.click(bell());
    await user.click(screen.getByRole("button", { name: "Se hvordan" }));
    await settle();
    expect(screen.queryByRole("alertdialog")).toBeNull();

    await act(async () => answerAcknowledged([]));
    await user.click(await screen.findByRole("button", { name: "Skjønner!" }));
    await waitFor(() => expect(screen.queryByRole("alertdialog")).toBeNull());
    await settle();

    expect(screen.queryByRole("alertdialog")).toBeNull();
    expect(acknowledgeMock).toHaveBeenCalledTimes(1);
  });

  test("«Se hvordan» virker selv om bekreftelsesstatusen ikke kan hentes", async () => {
    fetchAcknowledgedMock.mockRejectedValue(new Error("Nettverksfeil"));
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
    await user.click(screen.getByRole("button", { name: "Se hvordan" }));

    expect(await screen.findByRole("alertdialog", { name: "Gjennomgang av funksjonen" })).not.toBeNull();
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
