import { describe, expect, test } from "vitest";

import { literal, paragraph, stateV2 } from "~test/support/editorV2TestUtils";

import { run } from "../state/editorState";
import { updateSignatur } from "./updateSignatur";

describe("updateSignatur", () => {
  test("updates the saksbehandler signature", () => {
    const result = run(stateV2(paragraph(literal("tekst"))), updateSignatur, "Saksbehandler", "Ny Behandler");

    expect(result.redigertBrev.signatur.saksbehandlerNavn).toBe("Ny Behandler");
    expect(result.saveStatus).toBe("DIRTY");
  });

  test("updates the attestant signature", () => {
    const result = run(stateV2(paragraph(literal("tekst"))), updateSignatur, "Attestant", "Atte Stant");

    expect(result.redigertBrev.signatur.attesterendeSaksbehandlerNavn).toBe("Atte Stant");
    expect(result.redigertBrev.signatur.saksbehandlerNavn).toBe("Sak S. Behandler");
  });
});
