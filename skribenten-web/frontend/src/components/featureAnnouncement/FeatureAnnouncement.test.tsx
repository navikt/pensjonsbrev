import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, fireEvent, render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { AxiosError, type AxiosResponse } from "axios";
import { addDays, format } from "date-fns";
import { type ReactNode, useState } from "react";
import { beforeEach, describe, expect, onTestFinished, test, vi } from "vitest";

import { FeatureAnnouncement, type FeatureAnnouncementStep } from "./FeatureAnnouncement";

const { fetchAcknowledgedMock, acknowledgeMock, logErrorMock } = vi.hoisted(() => ({
  fetchAcknowledgedMock: vi.fn(),
  acknowledgeMock: vi.fn(),
  logErrorMock: vi.fn(),
}));

vi.mock("~/utils/logger", () => ({ logError: logErrorMock }));

vi.mock("~/api/skribenten-api-endpoints", async (importOriginal) => {
  const original = await importOriginal<typeof import("~/api/skribenten-api-endpoints")>();
  return {
    ...original,
    getAcknowledgedAnnouncements: { ...original.getAcknowledgedAnnouncements, queryFn: fetchAcknowledgedMock },
    acknowledgeAnnouncement: acknowledgeMock,
  };
});

const today = () => format(new Date(), "yyyy-MM-dd");
const yesterday = () => format(addDays(new Date(), -1), "yyyy-MM-dd");

const Harness = ({
  when = true,
  multiStep = false,
  showTarget = true,
  spotlight,
  onDismiss,
  showUntil = "2999-12-31",
}: {
  when?: boolean;
  multiStep?: boolean;
  showTarget?: boolean;
  spotlight?: boolean;
  onDismiss?: () => void;
  showUntil?: string;
}) => {
  const [first, setFirst] = useState<HTMLElement | null>(null);
  const [second, setSecond] = useState<HTMLElement | null>(null);
  const steps: FeatureAnnouncementStep[] = [];
  if (first) steps.push({ target: first, title: "Første nyhet", content: "Første forklaring" });
  if (multiStep && second) steps.push({ target: second, title: "Andre nyhet", content: "Andre forklaring" });

  return (
    <>
      {showTarget && (
        <div ref={setFirst}>
          <button type="button">Bryter</button>
        </div>
      )}
      <div ref={setSecond}>Hjelp</div>
      <FeatureAnnouncement
        id="test-nyhet"
        onDismiss={onDismiss}
        showUntil={showUntil}
        spotlight={spotlight}
        steps={steps}
        when={when && first !== null}
      />
      <div contentEditable data-testid="editor" suppressContentEditableWarning>
        Brevtekst
      </div>
    </>
  );
};

const renderWithQueryClient = (ui: ReactNode, queryClient = createQueryClient()) =>
  render(ui, {
    wrapper: ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>,
  });

const createQueryClient = () =>
  new QueryClient({ defaultOptions: { queries: { retry: false }, mutations: { retryDelay: 0 } } });

const findAnnouncement = (name = "Første nyhet") => screen.findByRole("alertdialog", { name });
const queryAnnouncement = () => screen.queryByRole("alertdialog", { name: /nyhet$/ });
const settle = (ms = 50) => act(() => new Promise((resolve) => setTimeout(resolve, ms)));
const clickDimmedPage = () => {
  const dimmedArea = document.querySelector('[data-testid="spotlight"] path');
  if (!dimmedArea) throw new Error("Fant ikke den dimmede flaten");
  fireEvent.click(dimmedArea);
};

const appendToBody = (element: HTMLElement) => {
  document.body.appendChild(element);
  onTestFinished(() => element.remove());
  return element;
};

describe("<FeatureAnnouncement />", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    fetchAcknowledgedMock.mockResolvedValue([]);
    acknowledgeMock.mockResolvedValue(undefined);
    logErrorMock.mockResolvedValue(undefined);
  });

  test("prøver å lagre bekreftelsen på nytt og logger feilen hvis det ikke lykkes", async () => {
    const error = new AxiosError("Request failed", "ERR_BAD_RESPONSE", undefined, undefined, {
      status: 500,
    } as AxiosResponse);
    acknowledgeMock.mockRejectedValue(error);
    const user = userEvent.setup();
    renderWithQueryClient(<Harness />);
    await findAnnouncement();

    await user.click(screen.getByRole("button", { name: "Skjønner!" }));

    await waitFor(() => expect(logErrorMock).toHaveBeenCalledWith(error, 500));
    expect(acknowledgeMock).toHaveBeenCalledTimes(3);
    expect(logErrorMock).toHaveBeenCalledTimes(1);
    expect(queryAnnouncement()).toBeNull();
  });

  test("logger ikke når bekreftelsen lagres", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<Harness />);
    await findAnnouncement();

    await user.click(screen.getByRole("button", { name: "Skjønner!" }));
    await waitFor(() => expect(acknowledgeMock).toHaveBeenCalledTimes(1));
    await settle();

    expect(logErrorMock).not.toHaveBeenCalled();
  });

  test("vises som modal dialog når betingelsene er oppfylt", async () => {
    renderWithQueryClient(<Harness />);

    const announcement = await findAnnouncement();
    expect(announcement.getAttribute("aria-modal")).toBe("true");
    expect(announcement.getAttribute("data-feature-announcement")).toBe("test-nyhet");
    const descriptionId = announcement.getAttribute("aria-describedby") ?? "";
    expect(document.getElementById(descriptionId)?.textContent).toBe("Første forklaring");
  });

  test("venter til betingelsene er oppfylt og målet er rendret", async () => {
    const { rerender } = renderWithQueryClient(<Harness showTarget={false} />);
    await settle();
    expect(queryAnnouncement()).toBeNull();

    rerender(<Harness showTarget when={false} />);
    await settle();
    expect(queryAnnouncement()).toBeNull();

    rerender(<Harness showTarget when />);
    await findAnnouncement();
  });

  test("lagrer bekreftelsen én gang og åpnes ikke igjen når den er lukket", async () => {
    const user = userEvent.setup();
    const { rerender } = renderWithQueryClient(<Harness />);
    await findAnnouncement();

    await user.click(screen.getByRole("button", { name: "Skjønner!" }));
    await waitFor(() => expect(queryAnnouncement()).toBeNull());
    expect(acknowledgeMock).toHaveBeenCalledTimes(1);
    expect(acknowledgeMock).toHaveBeenCalledWith("test-nyhet");

    rerender(<Harness when={false} />);
    rerender(<Harness when />);
    await settle();
    expect(queryAnnouncement()).toBeNull();
    expect(acknowledgeMock).toHaveBeenCalledTimes(1);
  });

  test("vises ikke når brukeren allerede har bekreftet nyheten", async () => {
    fetchAcknowledgedMock.mockResolvedValue(["annen-nyhet", "test-nyhet"]);
    renderWithQueryClient(<Harness />);
    await settle();

    expect(fetchAcknowledgedMock).toHaveBeenCalled();
    expect(queryAnnouncement()).toBeNull();
  });

  test("vises ikke når bekreftelsene ikke kan hentes", async () => {
    fetchAcknowledgedMock.mockRejectedValue(new Error("Nettverksfeil"));
    renderWithQueryClient(<Harness />);
    await settle();

    expect(queryAnnouncement()).toBeNull();
  });

  test("vises til og med siste visningsdag", async () => {
    renderWithQueryClient(<Harness showUntil={today()} />);

    await findAnnouncement();
  });

  test("vises ikke etter siste visningsdag, og henter da ikke bekreftelser", async () => {
    renderWithQueryClient(<Harness showUntil={yesterday()} />);
    await settle();

    expect(queryAnnouncement()).toBeNull();
    expect(fetchAcknowledgedMock).not.toHaveBeenCalled();
  });

  test("henter ikke bekreftelser før betingelsene er oppfylt", async () => {
    renderWithQueryClient(<Harness when={false} />);
    await settle();

    expect(fetchAcknowledgedMock).not.toHaveBeenCalled();
  });

  test("vises ikke igjen etter ny montering i samme økt, selv om lagringen feiler", async () => {
    acknowledgeMock.mockRejectedValue(new Error("Nettverksfeil"));
    const user = userEvent.setup();
    const queryClient = createQueryClient();
    const firstRender = renderWithQueryClient(<Harness />, queryClient);
    await findAnnouncement();
    await user.click(screen.getByRole("button", { name: "Skjønner!" }));
    await waitFor(() => expect(queryAnnouncement()).toBeNull());
    firstRender.unmount();

    renderWithQueryClient(<Harness />, queryClient);
    await settle();

    expect(queryAnnouncement()).toBeNull();
  });

  test("lukkes med Escape", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<Harness />);
    await findAnnouncement();

    await user.keyboard("{Escape}");

    await waitFor(() => expect(queryAnnouncement()).toBeNull());
    expect(acknowledgeMock).toHaveBeenCalledTimes(1);
  });

  test("flytter fokus til nyheten og tilbake dit brukeren var når den lukkes", async () => {
    const user = userEvent.setup();
    const { rerender } = renderWithQueryClient(<Harness when={false} />);
    const editor = screen.getByTestId("editor");
    act(() => editor.focus());

    rerender(<Harness when />);
    await findAnnouncement();
    await waitFor(() => expect(document.activeElement).toBe(screen.getByRole("button", { name: "Skjønner!" })));

    await user.keyboard("{Escape}");

    await waitFor(() => expect(queryAnnouncement()).toBeNull());
    expect(document.activeElement).toBe(editor);
  });

  test("holder tastaturfokus i nyheten", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<Harness multiStep />);
    await findAnnouncement();
    await user.click(screen.getByRole("button", { name: "Neste" }));
    await findAnnouncement("Andre nyhet");
    const buttons = ["Lukk", "Forrige", "Skjønner!"].map((name) => screen.getByRole("button", { name }));
    act(() => buttons[2].focus());

    await user.tab();
    expect(document.activeElement).toBe(buttons[0]);
    await user.tab({ shift: true });
    expect(document.activeElement).toBe(buttons[2]);
  });

  test("støtter flere steg med neste, forrige og lukk", async () => {
    const user = userEvent.setup();
    renderWithQueryClient(<Harness multiStep />);

    await findAnnouncement();
    expect(screen.queryByText("Steg 1 av 2")).not.toBeNull();
    await user.click(screen.getByRole("button", { name: "Neste" }));

    await findAnnouncement("Andre nyhet");
    expect(acknowledgeMock).not.toHaveBeenCalled();
    await user.click(screen.getByRole("button", { name: "Forrige" }));

    await findAnnouncement();
    await user.click(screen.getByRole("button", { name: "Lukk" }));
    await waitFor(() => expect(queryAnnouncement()).toBeNull());
    expect(acknowledgeMock).toHaveBeenCalledTimes(1);
  });

  test("dimmer siden, skjult for skjermlesere, og lukkes ved klikk på den dimmede flaten", async () => {
    const onDismiss = vi.fn();
    renderWithQueryClient(<Harness onDismiss={onDismiss} />);
    await findAnnouncement();
    expect(document.querySelector('[data-testid="overlay"]')?.getAttribute("aria-hidden")).toBe("true");

    clickDimmedPage();

    await waitFor(() => expect(queryAnnouncement()).toBeNull());
    expect(onDismiss).toHaveBeenCalledTimes(1);
    expect(acknowledgeMock).toHaveBeenCalledTimes(1);
  });

  test("klikk på den dimmede flaten lukker hele gjennomgangen", async () => {
    renderWithQueryClient(<Harness multiStep />);
    await findAnnouncement();

    clickDimmedPage();
    await settle();

    expect(queryAnnouncement()).toBeNull();
  });

  test("spotlight kan slås av", async () => {
    renderWithQueryClient(<Harness spotlight={false} />);
    await findAnnouncement();

    expect(document.querySelector('[data-testid="overlay"]')).toBeNull();
  });

  test("vises ikke mens en modal dialog er åpen, og vises når den lukkes", async () => {
    const dialog = appendToBody(document.createElement("dialog"));
    dialog.setAttribute("open", "");

    renderWithQueryClient(<Harness />);
    await settle();
    expect(queryAnnouncement()).toBeNull();

    act(() => dialog.removeAttribute("open"));
    await findAnnouncement();
  });

  test("vises ikke mens en annen nyhet er aktiv", async () => {
    const otherAnnouncement = appendToBody(document.createElement("div"));
    otherAnnouncement.setAttribute("data-feature-announcement", "annen-nyhet");

    renderWithQueryClient(<Harness />);
    await settle();
    expect(queryAnnouncement()).toBeNull();

    act(() => otherAnnouncement.remove());
    await findAnnouncement();
  });

  test("skjules når målet forsvinner og rydder opp ved avmontering", async () => {
    const user = userEvent.setup();
    const { rerender, unmount } = renderWithQueryClient(<Harness />);
    await findAnnouncement();

    rerender(<Harness showTarget={false} />);
    await waitFor(() => expect(queryAnnouncement()).toBeNull());

    rerender(<Harness />);
    await findAnnouncement();
    unmount();

    expect(queryAnnouncement()).toBeNull();
    expect(document.querySelector("[data-feature-announcement]")).toBeNull();
    expect(document.querySelector('[data-testid="overlay"]')).toBeNull();
    await user.keyboard("{Escape}");
  });
});
