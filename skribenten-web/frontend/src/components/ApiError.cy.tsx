import { AxiosError, AxiosHeaders } from "axios";

import { ApiError } from "./ApiError";

function makeFunctionalAxiosError(tittel: string, melding: string): AxiosError {
  return new AxiosError("Functional error", "ERR_BAD_RESPONSE", undefined, undefined, {
    status: 422,
    data: { tittel, melding },
    headers: new AxiosHeaders(),
    config: { headers: new AxiosHeaders() },
    statusText: "Unprocessable Entity",
  });
}

function makeGenericAxiosError(title: string, correlationId?: string): AxiosError {
  const headers = new AxiosHeaders();
  if (correlationId) {
    headers.set("x-request-id", correlationId);
  }
  return new AxiosError(title, "ERR_BAD_RESPONSE", undefined, undefined, {
    status: 500,
    data: {},
    headers,
    config: { headers: new AxiosHeaders() },
    statusText: "Internal Server Error",
  });
}

// BrevmalPanel wraps ApiError in a VStack with width="385px" and padding="space-16" (16px each side),
// leaving ~353px of usable width — well below the Alert's maxWidth of 512px.
const BREVEMAL_PANEL_WIDTH = 353;

describe("ApiError - functional error overflow", () => {
  it("wraps long title text without overflow at brevvelger panel width", () => {
    const longTitle =
      "Forventa verdi av typen class no.nav.domain.pensjon.kjerne.beregning.SomeVeryLongClassName men den mangla og den var veldig lang og skulle teste om det skjer overflow i alerten";
    const longMelding =
      "Forventa verdi, men den mangla og den var veldig lang og skulle teste om det skjer overflow i alerten";

    cy.mount(
      <div style={{ width: `${BREVEMAL_PANEL_WIDTH}px` }}>
        <ApiError error={makeFunctionalAxiosError(longTitle, longMelding)} title="Fallback tittel" />
      </div>,
    );

    cy.get("[data-cy=functional-error-alert]").should("be.visible");

    cy.get("[data-cy=functional-error-alert]").then(($el) => {
      expect($el[0].scrollWidth).to.be.lte($el[0].clientWidth);
    });
  });
});

describe("ApiError - generic axios error overflow", () => {
  it("wraps long title without overflow at brevvelger panel width", () => {
    const longTitle =
      "Klarte ikke hente no.nav.domain.pensjon.kjerne.SomeVeryLongServiceClassName for saksnummer 1234567890";

    cy.mount(
      <div style={{ width: `${BREVEMAL_PANEL_WIDTH}px` }}>
        <ApiError error={makeGenericAxiosError(longTitle, "abc-123-correlation-id")} title={longTitle} />
      </div>,
    );

    cy.get("[data-cy=generic-error-alert]").should("be.visible");

    cy.get("[data-cy=generic-error-alert]").then(($el) => {
      expect($el[0].scrollWidth).to.be.lte($el[0].clientWidth);
    });
  });
});
