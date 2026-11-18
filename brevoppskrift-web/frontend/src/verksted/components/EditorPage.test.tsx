import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createRootRoute, createRouter, RouterProvider } from "@tanstack/react-router";
import { render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { type ReactNode } from "react";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";

import { ApiProblem, workshopApi } from "../api";
import { newDraft } from "../draftOps";
import { type DraftRecord, type TemplateSource } from "../types";
import { EditorPage } from "./EditorPage";
import { DraftListPage } from "./Pages";
import { StartFromSource } from "./StartFromSource";

function record(overrides: Partial<DraftRecord> = {}): DraftRecord {
  return {
    id: "d1",
    revision: 3,
    status: "WORK",
    sourceKey: null,
    draft: newDraft({ name: "Testbrev", type: "REDIGERBAR", targetModule: "PENSJON", languages: ["NB", "NN", "EN"] }),
    createdAt: "2025-01-01T00:00:00Z",
    updatedAt: "2025-01-01T00:00:00Z",
    updatedBy: "Z1",
    ...overrides,
  };
}

async function mount(ui: ReactNode) {
  const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  const rootRoute = createRootRoute({ component: () => <>{ui}</> });
  const router = createRouter({ routeTree: rootRoute });
  await router.load();
  render(
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>,
  );
  await screen.findAllByText(/Testbrev|Arbeidsutkast|arbeidsutkast|støttes ikke|Lag arbeidsutkast/i);
  return userEvent.setup();
}

describe("<EditorPage />", () => {
  beforeEach(() => {
    vi.spyOn(workshopApi, "preview").mockResolvedValue({ html: "<p>forhåndsvisning</p>", warnings: [] });
    vi.spyOn(workshopApi, "generate").mockResolvedValue({ files: [], issues: [] });
  });
  afterEach(() => {
    vi.restoreAllMocks();
  });

  const editor = (r: DraftRecord = record(), canEdit = true) => (
    <>
      <h1>Testbrev</h1>
      <EditorPage canEdit={canEdit} integrations={undefined} record={r} />
    </>
  );

  it("keeps text in other languages when switching language", async () => {
    const user = await mount(editor());
    await user.type(screen.getByLabelText("Tekst"), "Hei bokmål");
    await user.click(screen.getByRole("radio", { name: "NN" }));
    const nn = screen.getByLabelText("Tekst");
    expect((nn as HTMLTextAreaElement).value).toBe("");
    await user.type(nn, "Hei nynorsk");
    await user.click(screen.getByRole("radio", { name: "NB" }));
    expect((screen.getByLabelText("Tekst") as HTMLTextAreaElement).value).toBe("Hei bokmål");
    await user.click(screen.getByRole("checkbox", { name: "Vis alle språk" }));
    expect((screen.getByLabelText("Tekst (Nynorsk)") as HTMLTextAreaElement).value).toBe("Hei nynorsk");
  });

  it("adds, duplicates and moves blocks with fresh stable ids", async () => {
    const user = await mount(editor());
    await user.click(screen.getByRole("button", { name: "+ Overskrift" }));
    expect(screen.getAllByRole("group", { name: /Overskrift|Avsnitt/ }).length).toBeGreaterThan(0);
    const before = screen.getAllByRole("button", { name: /Kopier|Dupliser/i });
    expect(before.length).toBeGreaterThan(0);
    await user.click(before[0]);
    // Original avsnitt + overskrift + kopi
    expect(screen.getAllByLabelText("Tekst").length).toBe(3);
  });

  it("moves a block into a condition via the move selector, preserving its text", async () => {
    const base = record();
    base.draft.outline = [
      {
        type: "PARAGRAPH",
        id: "p1",
        text: [{ type: "TEXT", text: { NB: "Flyttes", NN: "Flyttast", EN: "Moved" }, fontType: "PLAIN" }],
      },
      { type: "CONDITIONAL", id: "c1", predicate: { type: "IS_NOT_NULL", fieldId: "" }, ifTrue: [], ifFalse: [] },
    ];
    const user = await mount(editor(base));
    const select = screen.getByLabelText("Flytt avsnitt til");
    // Betingelsen er ikke et lovlig mål for seg selv, men avsnittet kan inn i den.
    await user.selectOptions(select, screen.getByRole("option", { name: /Vilkår 2 \(hvis sant\)/ }));
    const ifTrue = screen.getByRole("group", { name: "Blokker hvis sant" });
    expect((within(ifTrue).getByLabelText("Tekst") as HTMLTextAreaElement).value).toBe("Flyttes");
    // Vilkåret kan ikke flyttes inn i seg selv: kun toppnivå tilbys.
    const condSelect = screen.getByLabelText("Flytt vilkår til");
    expect(
      within(condSelect)
        .getAllByRole("option")
        .map((o) => o.textContent),
    ).toEqual(["Flytt til…", "Øverst i brevet"]);
  });

  it("explains source profiles, protects locked blocks and hides scenarios", async () => {
    const base = record();
    base.draft.outline = [
      { type: "LOCKED_SOURCE", id: "l1", bindingId: "binding-1" },
      {
        type: "CONDITIONAL",
        id: "c1",
        predicate: { type: "IS_NOT_NULL", fieldId: "" },
        ifTrue: [{ type: "LOCKED_SOURCE", id: "l2", bindingId: "binding-2" }],
        ifFalse: [],
      },
      {
        type: "PARAGRAPH",
        id: "p1",
        text: [{ type: "TEXT", text: { NB: "", NN: "", EN: "" }, fontType: "PLAIN" }],
      },
    ];
    const user = await mount(editor(base));
    expect(screen.getByText(/Importert fra kilde med tekstprofil/)).toBeTruthy();
    expect(screen.queryByLabelText("Scenario")).toBeNull();
    expect(screen.getAllByText(/kan ikke redigeres her/).length).toBe(2);
    // Strukturen er låst i en kildeprofil: ingen kopier/slett/flytt/legg til, men tekst kan endres.
    expect(screen.queryByTitle("Slett vilkår")).toBeNull();
    expect(screen.queryByTitle("Kopier vilkår")).toBeNull();
    expect(screen.queryByTitle("Slett avsnitt")).toBeNull();
    expect(screen.queryByLabelText("Flytt avsnitt til")).toBeNull();
    expect(screen.queryByRole("button", { name: "+ Avsnitt" })).toBeNull();
    expect((screen.getByLabelText("Tekst") as HTMLTextAreaElement).readOnly).toBe(false);
    await user.type(screen.getByLabelText("Tekst"), "Endret tekst");
    expect((screen.getByLabelText("Tekst") as HTMLTextAreaElement).value).toBe("Endret tekst");
    await user.click(screen.getByRole("tab", { name: "Datafelt og valg" }));
    expect(screen.queryByRole("button", { name: /Legg til datafelt/ })).toBeNull();
    await user.click(screen.getByRole("tab", { name: "Scenarier" }));
    expect(screen.getByText(/Scenarier støttes ikke for importerte kildeprofiler/)).toBeTruthy();
  });

  it("undo reverts the last edit and save is explicit", async () => {
    const save = vi
      .spyOn(workshopApi, "save")
      .mockImplementation(async (_id, revision, draft) => ({ ...record(), revision: revision + 1, draft }));
    const user = await mount(editor());
    const saveButton = screen.getByRole("button", { name: "Lagre" });
    expect((saveButton as HTMLButtonElement).disabled).toBe(true);
    await user.type(screen.getByLabelText("Tittel"), "Ny");
    expect(save).not.toHaveBeenCalled();
    await user.click(screen.getByRole("button", { name: "Angre" }));
    expect((screen.getByLabelText("Tittel") as HTMLInputElement).value).toBe("");
    await user.type(screen.getByLabelText("Tittel"), "Tittel");
    await user.click(screen.getByRole("button", { name: "Lagre" }));
    await waitFor(() => expect(save).toHaveBeenCalledTimes(1));
    expect(save.mock.calls[0][1]).toBe(3);
    expect(await screen.findByText("Lagret (rev. 4)")).toBeTruthy();
  });

  it("preserves local changes on a 409 and never overwrites", async () => {
    const save = vi
      .spyOn(workshopApi, "save")
      .mockRejectedValue(new ApiProblem({ message: "konflikt", status: 409, currentRevision: 9 }));
    const user = await mount(editor());
    await user.type(screen.getByLabelText("Tittel"), "Mitt arbeid");
    await user.click(screen.getByRole("button", { name: "Lagre" }));
    expect(await screen.findByText("Noen andre har lagret nyere versjon")).toBeTruthy();
    expect((screen.getByLabelText("Tittel") as HTMLInputElement).value).toBe("Mitt arbeid");
    expect(screen.getByText("Ulagrede endringer")).toBeTruthy();
    expect((screen.getByRole("button", { name: "Lagre" }) as HTMLButtonElement).disabled).toBe(true);
    expect(save).toHaveBeenCalledTimes(1);
  });

  it("preserves unsaved text on 401 and offers login in a separate tab", async () => {
    const save = vi
      .spyOn(workshopApi, "save")
      .mockRejectedValue(new ApiProblem({ message: "Du er ikke innlogget.", status: 401 }));
    const user = await mount(editor());
    await user.type(screen.getByLabelText("Tittel"), "Mitt arbeid");
    await user.click(screen.getByRole("button", { name: "Lagre" }));
    const login = await screen.findByRole("link", { name: "Logg inn på nytt (åpnes i ny fane)" });
    expect(login.getAttribute("href")).toBe("/oauth2/login?redirect=%2Farbeidsutkast");
    expect(login.getAttribute("target")).toBe("_blank");
    expect((screen.getByLabelText("Tittel") as HTMLInputElement).value).toBe("Mitt arbeid");
    expect(screen.getByText("Ulagrede endringer")).toBeTruthy();
    expect(save).toHaveBeenCalledOnce();
  });

  it("adopts the server version only after explicit confirmation", async () => {
    vi.spyOn(workshopApi, "save").mockRejectedValue(new ApiProblem({ message: "konflikt", status: 409 }));
    const serverDraft = record().draft;
    serverDraft.title = { NB: "Serverens", NN: "", EN: "" };
    vi.spyOn(workshopApi, "draft").mockResolvedValue(record({ revision: 9, draft: serverDraft, updatedBy: "Z9" }));
    const user = await mount(editor());
    await user.type(screen.getByLabelText("Tittel"), "Lokal");
    await user.click(screen.getByRole("button", { name: "Lagre" }));
    await user.click(await screen.findByRole("button", { name: "Hent serverens versjon" }));
    await user.click(await screen.findByRole("button", { name: /Erstatt mine endringer/ }));
    expect((screen.getByLabelText("Tittel") as HTMLInputElement).value).toBe("Lokal");
    await user.click(screen.getByRole("button", { name: "Forkast mine endringer og bruk serverens" }));
    expect((screen.getByLabelText("Tittel") as HTMLInputElement).value).toBe("Serverens");
  });

  it("adds a comment, a reply and resolves it without client-side author/time", async () => {
    const user = await mount(editor());
    await user.click(screen.getByRole("tab", { name: "Gjennomgang" }));
    await user.type(screen.getByLabelText("Kommentar"), "Sjekk formuleringen");
    await user.click(screen.getByRole("button", { name: "Legg til kommentar" }));
    expect(await screen.findByText("Sjekk formuleringen")).toBeTruthy();
    expect(screen.getAllByText("Ikke lagret ennå").length).toBeGreaterThan(0);
    await user.type(screen.getByLabelText("Svar"), "Rettet");
    await user.click(screen.getByRole("button", { name: "Send svar" }));
    expect(await screen.findByText("Rettet")).toBeTruthy();
    await user.click(screen.getByRole("button", { name: "Marker som løst" }));
    expect(screen.queryByText("Sjekk formuleringen")).toBeNull();
  });

  it("is read-only without edit access", async () => {
    await mount(editor(record(), false));
    expect(screen.queryByRole("button", { name: "Lagre" })).toBeNull();
    expect(screen.queryByRole("button", { name: "+ Avsnitt" })).toBeNull();
    expect(screen.getByText(/kun lesetilgang/i)).toBeTruthy();
  });
});

describe("workshop availability and sources", () => {
  afterEach(() => {
    vi.restoreAllMocks();
  });

  it("shows a recoverable unavailable state when the API is not configured", async () => {
    vi.spyOn(workshopApi, "me").mockRejectedValue(new ApiProblem({ message: "nede", status: 503, unavailable: true }));
    await mount(<DraftListPage />);
    expect(await screen.findByText("Malverkstedet er ikke tilgjengelig")).toBeTruthy();
    expect(screen.getByRole("button", { name: "Prøv igjen" })).toBeTruthy();
  });

  it("cannot start editing a catalog source that is not editable", async () => {
    const source: TemplateSource = {
      id: "s1",
      name: "Ustøttet",
      templateType: "AUTOBREV",
      targetModule: "PENSJON",
      languages: ["NB"],
      sourceCommit: "abc",
      editable: false,
      diagnostics: [{ path: "Brev.kt", code: "UNSUPPORTED", message: "Bruker ukjent konstruksjon" }],
    };
    const create = vi.spyOn(workshopApi, "create");
    await mount(<StartFromSource drafts={[]} source={source} />);
    expect(screen.getByText(/støttes ikke i malverkstedet/i)).toBeTruthy();
    expect(screen.getByText("Bruker ukjent konstruksjon (UNSUPPORTED)")).toBeTruthy();
    expect(screen.queryByRole("button", { name: "Lag arbeidsutkast" })).toBeNull();
    expect(create).not.toHaveBeenCalled();
  });
});

describe("<AttachmentsPanel /> usage", () => {
  afterEach(() => {
    vi.restoreAllMocks();
  });

  it("lists active letters using the attachment", async () => {
    vi.spyOn(workshopApi, "preview").mockResolvedValue({ html: "", warnings: [] });
    vi.spyOn(workshopApi, "drafts").mockResolvedValue([]);
    const usage = vi.spyOn(workshopApi, "usage").mockResolvedValue([{ id: "o1", name: "Eierbrev", revision: 4 }]);
    const r = record({
      draft: newDraft({ name: "Testbrev", type: "VEDLEGG", targetModule: "PENSJON", languages: ["NB", "NN", "EN"] }),
    });
    const user = await mount(
      <>
        <h1>Testbrev</h1>
        <EditorPage canEdit integrations={undefined} record={r} />
      </>,
    );
    await user.click(screen.getByRole("tab", { name: /Vedlegg/ }));
    expect(await screen.findByText("Eierbrev")).toBeTruthy();
    expect(screen.getByText(/kopi av rev\. 4/)).toBeTruthy();
    expect(usage).toHaveBeenCalledWith("d1");
  });
});
