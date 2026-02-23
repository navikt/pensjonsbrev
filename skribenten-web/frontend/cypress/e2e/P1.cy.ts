import p1Data from "../fixtures/p1Data.json";
import { nyBrevResponse } from "../utils/brevredigeringTestUtils";

const { p1BrevInfo, p1BrevData, p1BrevDataWithMissingFields, countriesSubset } = p1Data;

// Hjelpere
const openP1Modal = () => {
  cy.contains(p1BrevInfo.brevtittel).click();
  cy.get('[data-cy="p1-edit-button"]').should("be.visible").click();
  cy.get('[data-cy="p1-edit-modal"]').should("be.visible");
  cy.contains("3. Innvilget pensjon").should("be.visible");
};

const getInnvilgetFelt = (index: number, felt: string) => cy.get(`[data-cy="innvilget-${index}-${felt}"]`);

describe("P1 med forsidebrev", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.fixture("helloWorldPdf.txt", "base64").then((pdfBase64) => {
      cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/*/pdf", (request) => {
        request.reply({
          body: {
            pdf: pdfBase64,
            rendretBrevErEndret: false,
          },
        });
      });
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (request) => {
      // @ts-expect-error: JSON fixture data might miss optional complex types but works for runtime
      request.reply(nyBrevResponse({ info: p1BrevInfo }));
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=false", (request) => {
      // @ts-expect-error: JSON fixture data might miss optional complex types but works for runtime
      request.reply(nyBrevResponse({ info: p1BrevInfo }));
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([p1BrevInfo]);
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/p1", (request) => {
      request.reply(p1BrevData);
    }).as("getP1Data");
    cy.intercept("GET", "/bff/skribenten-backend/landForP1", (request) => {
      request.reply(countriesSubset);
    }).as("getLand");
    // Intercept for Vedlegg component - returns empty array since P1 vedlegg is handled separately
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/*/alltidValgbareVedlegg", (request) => {
      request.reply([]);
    }).as("getVedlegg");
    cy.visit("/saksnummer/123456/brevbehandler");
  });

  it("viser og lagrer data i henholdsvis visningformat og api format", () => {
    openP1Modal();
    cy.wait("@getP1Data");
    cy.wait("@getLand");
    cy.contains("3. Innvilget pensjon").should("be.visible");

    // verifiser at land vises med fullt norsk navn, mens to-bokstavskode brukes fra/til backend
    // verifiser at dato vises som dd.MM.yyyy, mens formatet yyyy-MM-dd brukes fra/til backend
    cy.contains("Bulgaria").should("be.visible");
    cy.get(`[data-cy="land-0"]`).type("{selectall}{backspace}Frankrike{enter}");
    getInnvilgetFelt(0, "datoForVedtak").should("have.value", "23.09.2022");
    getInnvilgetFelt(0, "datoForVedtak").type("{selectall}{backspace}01.01.2025");
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/p1", (request) => {
      expect(request.body.innvilgedePensjoner[0].institusjon.land).to.eq("FR");
      expect(request.body.innvilgedePensjoner[0].institusjon.datoForVedtak).to.eq("2025-01-01");
      request.reply("200");
    });
    cy.contains("Lagre").click();
  });

  it("viser valideringsfeil når påkrevde felt mangler", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/p1", (request) => {
      request.reply(p1BrevDataWithMissingFields);
    });
    openP1Modal();
    cy.contains("3. Innvilget pensjon").should("be.visible");
    // Gjør skjemaet dirty ved å endre et felt
    getInnvilgetFelt(0, "datoForVedtak").type("{selectall}{backspace}01.01.2025");
    // Forsøk å lagre uten å fylle ut påkrevde felt
    cy.contains("Lagre").click();

    // Verifiser at individuelle feilmeldinger vises
    cy.contains("Institusjonsnavn er obligatorisk").should("be.visible");
    cy.contains("Pensjonstype må velges").should("be.visible");
  });

  it("viser valideringsfeil for ugyldig datoformat", () => {
    openP1Modal();
    cy.contains("3. Innvilget pensjon").should("be.visible");
    // Skriv inn ugyldig dato
    getInnvilgetFelt(0, "datoForVedtak").type("{selectall}{backspace}32.13.2022");
    cy.contains("Lagre").click();
    cy.contains("Skjemaet har feil som må rettes").should("exist");
    cy.contains("Ugyldig dato").should("be.visible");
  });

  it("viser valideringsfeil når tekstfelt overskrider makslengde", () => {
    openP1Modal();
    cy.contains("3. Innvilget pensjon").should("be.visible");
    const veryLongText = "A".repeat(300);
    // Bruker data-cy for å finne PIN feltet
    getInnvilgetFelt(0, "pin")
      .type(`{selectall}{backspace}${veryLongText}`, { delay: 0 })
      .press(Cypress.Keyboard.Keys.TAB);

    cy.contains("PIN kan ikke være lengre enn").should("be.visible");
    cy.contains("Skjemaet har feil som må rettes").should("not.exist");
    cy.contains("Lagre").click();
    cy.contains("Skjemaet har feil som må rettes").should("exist");
  });

  it("navigerer til fane med første feil ved validering", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/p1", (request) => {
      request.reply(p1BrevDataWithMissingFields);
    });
    openP1Modal();
    cy.contains("3. Innvilget pensjon").should("be.visible");
    getInnvilgetFelt(0, "datoForVedtak").type("01.01.2025");
    cy.contains("4. Avslag på pensjon").click();
    cy.contains("Institusjon som har avslått").should("be.visible");
    // Forsøk å lagre med feil i fane 3 (Innvilget)
    cy.contains("Lagre").click();
    // Verifiser at vi blir navigert tilbake til fane 3
    cy.contains("Institusjon som gir pensjonen").should("be.visible");
    cy.contains("Institusjonsnavn er obligatorisk").should("be.visible");
  });

  it("viser suksessmelding ved vellykket lagring", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/p1", (request) => {
      request.reply("200");
    });
    openP1Modal();
    cy.contains("3. Innvilget pensjon").should("be.visible");
    getInnvilgetFelt(0, "datoForVedtak").type("{selectall}{backspace}01.01.2025");
    cy.contains("Lagre").should("not.be.disabled").click();
    cy.contains("Endringene ble lagret").should("exist");
  });

  it("håndterer sakstype UFOREP (uføretrygd) korrekt ved lagring", () => {
    // This test explicitly verifies the fix for the reported bug
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/p1", (request) => {
      const uforepData = structuredClone(p1BrevData);
      uforepData.sakstype = "UFOREP";
      request.reply(uforepData);
    });

    openP1Modal();
    cy.contains("3. Innvilget pensjon").should("be.visible");

    // Modify field to enable save
    getInnvilgetFelt(0, "datoForVedtak").type("{selectall}{backspace}01.01.2025");

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/p1", (request) => {
      expect(request.body.sakstype).to.eq("UFOREP");
      request.reply("200");
    }).as("saveP1Uforep");

    cy.contains("Lagre").click();

    cy.wait("@saveP1Uforep");
    cy.contains("Endringene ble lagret").should("be.visible");
  });
});
