import { act, fireEvent, render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { useState } from "react";
import { describe, expect, onTestFinished, test, vi } from "vitest";

import { FeatureAnnouncement, type FeatureAnnouncementStep } from "./FeatureAnnouncement";

const Harness = ({
  when = true,
  multiStep = false,
  showTarget = true,
  spotlight,
  onDismiss,
}: {
  when?: boolean;
  multiStep?: boolean;
  showTarget?: boolean;
  spotlight?: boolean;
  onDismiss?: () => void;
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
  test("vises som modal dialog når betingelsene er oppfylt", async () => {
    render(<Harness />);

    const announcement = await findAnnouncement();
    expect(announcement.getAttribute("aria-modal")).toBe("true");
    expect(announcement.getAttribute("data-feature-announcement")).toBe("test-nyhet");
    const descriptionId = announcement.getAttribute("aria-describedby") ?? "";
    expect(document.getElementById(descriptionId)?.textContent).toBe("Første forklaring");
  });

  test("venter til betingelsene er oppfylt og målet er rendret", async () => {
    const { rerender } = render(<Harness showTarget={false} />);
    await settle();
    expect(queryAnnouncement()).toBeNull();

    rerender(<Harness showTarget when={false} />);
    await settle();
    expect(queryAnnouncement()).toBeNull();

    rerender(<Harness showTarget when />);
    await findAnnouncement();
  });

  test("kan lukkes og åpnes ikke igjen mens komponenten er montert", async () => {
    const user = userEvent.setup();
    const { rerender } = render(<Harness />);
    await findAnnouncement();

    await user.click(screen.getByRole("button", { name: "Skjønner!" }));
    await waitFor(() => expect(queryAnnouncement()).toBeNull());

    rerender(<Harness when={false} />);
    rerender(<Harness when />);
    await settle();
    expect(queryAnnouncement()).toBeNull();
  });

  test("lukkes med Escape", async () => {
    const user = userEvent.setup();
    render(<Harness />);
    await findAnnouncement();

    await user.keyboard("{Escape}");

    await waitFor(() => expect(queryAnnouncement()).toBeNull());
  });

  test("flytter fokus til nyheten og tilbake dit brukeren var når den lukkes", async () => {
    const user = userEvent.setup();
    const { rerender } = render(<Harness when={false} />);
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
    render(<Harness multiStep />);
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
    render(<Harness multiStep />);

    await findAnnouncement();
    expect(screen.queryByText("Steg 1 av 2")).not.toBeNull();
    await user.click(screen.getByRole("button", { name: "Neste" }));

    await findAnnouncement("Andre nyhet");
    await user.click(screen.getByRole("button", { name: "Forrige" }));

    await findAnnouncement();
    await user.click(screen.getByRole("button", { name: "Lukk" }));
    await waitFor(() => expect(queryAnnouncement()).toBeNull());
  });

  test("dimmer siden, skjult for skjermlesere, og lukkes ved klikk på den dimmede flaten", async () => {
    const onDismiss = vi.fn();
    render(<Harness onDismiss={onDismiss} />);
    await findAnnouncement();
    expect(document.querySelector('[data-testid="overlay"]')?.getAttribute("aria-hidden")).toBe("true");

    clickDimmedPage();

    await waitFor(() => expect(queryAnnouncement()).toBeNull());
    expect(onDismiss).toHaveBeenCalledTimes(1);
  });

  test("klikk på den dimmede flaten lukker hele gjennomgangen", async () => {
    render(<Harness multiStep />);
    await findAnnouncement();

    clickDimmedPage();
    await settle();

    expect(queryAnnouncement()).toBeNull();
  });

  test("spotlight kan slås av", async () => {
    render(<Harness spotlight={false} />);
    await findAnnouncement();

    expect(document.querySelector('[data-testid="overlay"]')).toBeNull();
  });

  test("vises ikke mens en modal dialog er åpen, og vises når den lukkes", async () => {
    const dialog = appendToBody(document.createElement("dialog"));
    dialog.setAttribute("open", "");

    render(<Harness />);
    await settle();
    expect(queryAnnouncement()).toBeNull();

    act(() => dialog.removeAttribute("open"));
    await findAnnouncement();
  });

  test("vises ikke mens en annen nyhet er aktiv", async () => {
    const annenNyhet = appendToBody(document.createElement("div"));
    annenNyhet.setAttribute("data-feature-announcement", "annen-nyhet");

    render(<Harness />);
    await settle();
    expect(queryAnnouncement()).toBeNull();

    act(() => annenNyhet.remove());
    await findAnnouncement();
  });

  test("kontrollert lukking gjelder også etter at komponenten monteres på nytt", async () => {
    const user = userEvent.setup();
    const ControlledHarness = ({ mounted }: { mounted: boolean }) => {
      const [dismissed, setDismissed] = useState(false);
      const [target, setTarget] = useState<HTMLElement | null>(null);
      const steps = target ? [{ target, title: "Første nyhet", content: "Første forklaring" }] : [];
      return (
        <>
          <div ref={setTarget}>Mål</div>
          {mounted && (
            <FeatureAnnouncement
              dismissed={dismissed}
              id="test-nyhet"
              onDismiss={() => setDismissed(true)}
              steps={steps}
              when={target !== null}
            />
          )}
        </>
      );
    };
    const { rerender } = render(<ControlledHarness mounted />);
    await findAnnouncement();
    await user.click(screen.getByRole("button", { name: "Skjønner!" }));
    await waitFor(() => expect(queryAnnouncement()).toBeNull());

    rerender(<ControlledHarness mounted={false} />);
    rerender(<ControlledHarness mounted />);
    await settle();

    expect(queryAnnouncement()).toBeNull();
  });

  test("skjules når målet forsvinner og rydder opp ved avmontering", async () => {
    const user = userEvent.setup();
    const { rerender, unmount } = render(<Harness />);
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
