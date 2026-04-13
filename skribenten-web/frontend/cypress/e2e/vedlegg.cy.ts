import { SpraakKode } from "~/types/apiTypes";
import type { AlltidValgbartVedleggV2 } from "~/types/brev";

import { nyBrevInfo, nyBrevResponse } from "../utils/brevredigeringTestUtils";

const bokmaalBrev = nyBrevInfo({
  spraak: SpraakKode.Bokmaal,
  status: { type: "Kladd" },
});

const alleTilgjengelige: AlltidValgbartVedleggV2[] = [
  {
    kode: "V1",
    visningstekst: "Skjema for bankopplysninger",
    spraak: ["BOKMAL", "NYNORSK"],
    tilgjengeligForSpraak: true,
  },
  { kode: "V2", visningstekst: "Uttaksskjema", spraak: ["BOKMAL", "ENGLISH"], tilgjengeligForSpraak: true },
];

const noenUtilgjengelige: AlltidValgbartVedleggV2[] = [
  {
    kode: "V1",
    visningstekst: "Skjema for bankopplysninger",
    spraak: ["BOKMAL", "NYNORSK"],
    tilgjengeligForSpraak: true,
  },
  { kode: "V2", visningstekst: "Uttaksskjema", spraak: ["ENGLISH"], tilgjengeligForSpraak: false },
];

const ingenTilgjengelige: AlltidValgbartVedleggV2[] = [
  { kode: "V1", visningstekst: "Skjema for bankopplysninger", spraak: ["ENGLISH"], tilgjengeligForSpraak: false },
  { kode: "V2", visningstekst: "Uttaksskjema", spraak: ["ENGLISH", "NYNORSK"], tilgjengeligForSpraak: false },
];

const setupBrevbehandler = (vedlegg: AlltidValgbartVedleggV2[]) => {
  cy.setupSakStubs();
  cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", { body: [bokmaalBrev] });
  cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", {
    body: nyBrevResponse({ info: bokmaalBrev }),
  });
  cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=false", {
    body: nyBrevResponse({ info: bokmaalBrev }),
  });
  cy.intercept("GET", /\/brev\/\d+\/v2\/alltidValgbareVedlegg/, { body: vedlegg }).as("getVedlegg");
  cy.visit("/saksnummer/123456/brevbehandler");
  cy.get(`[aria-label="${bokmaalBrev.brevtittel}"] button[aria-expanded]`).click();
  cy.wait("@getVedlegg");
};

const openVedleggModal = () => {
  cy.get('button[title="Legg til vedlegg"]').click();
};

describe("Vedlegg modal - viser tilgjengelighet basert på brevets språk", () => {
  it("alle vedlegg er tilgjengelige", () => {
    setupBrevbehandler(alleTilgjengelige);
    openVedleggModal();

    cy.contains("Skjema for bankopplysninger").should("be.visible");
    cy.contains("Uttaksskjema").should("be.visible");
    cy.contains("Bokmål, Nynorsk").should("be.visible");
    cy.contains("Bokmål, Engelsk").should("be.visible");
  });

  it("noen vedlegg er utilgjengelige", () => {
    setupBrevbehandler(noenUtilgjengelige);
    openVedleggModal();

    cy.contains("Noen skjemaer er ikke tilgjengelig på språket i brevet.").should("be.visible");
    cy.contains("Skjemaer som ikke er tilgjengelig på Bokmål").should("be.visible");
    cy.contains("Skjema for bankopplysninger").should("be.visible");
    cy.contains("Uttaksskjema").should("be.visible");
  });

  it("ingen vedlegg er tilgjengelige", () => {
    setupBrevbehandler(ingenTilgjengelige);
    openVedleggModal();

    cy.contains("Ingen skjemaer er tilgjengelig basert på språket i brevet.").should("be.visible");
    cy.contains("Hvorfor kan jeg ikke legge til skjema på et annet språk/målform?").should("be.visible");
    cy.contains("Skjema for bankopplysninger").should("be.visible");
    cy.contains("Uttaksskjema").should("be.visible");
  });
});
