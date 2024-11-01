import { nyBrevInfo, nyBrevResponse } from "../../utils/brevredigeringTestUtils";

describe("Kladd", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.viewport(1200, 1400);

    cy.visit("/saksnummer/123456/brevvelger");
  });

  it("Viser kladder i brevvelgeren", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      req.reply([
        {
          id: 1,
          opprettetAv: { id: "Z990297", navn: "F_Z990297 E_Z990297" },
          opprettet: "2024-09-17T08:36:09.785Z",
          sistredigertAv: { id: "Z990297", navn: "F_Z990297 E_Z990297" },
          sistredigert: "2024-09-17T08:38:48.503Z",
          brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
          brevtittel: "Informasjon om saksbehandlingstid",
          status: { type: "Kladd" },
          distribusjonstype: "SENTRALPRINT",
          mottaker: null,
          avsenderEnhet: null,
          spraak: "EN",
        },
      ]);
    }).as("getAlleBrevForSak");

    cy.wait("@getAlleBrevForSak");
    cy.contains("Informasjon om saksbehandlingstid").click();
    cy.contains("Mottaker").should("be.visible");
    cy.contains("Avsenderenhet").should("be.visible");
    cy.contains("Språk").should("be.visible");
    cy.contains("Gå til brevbehandler").should("be.visible");
    cy.contains("Åpne brev").should("be.visible");
  });

  it("Kan slette kladd", () => {
    let requestnr = 0;
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      if (requestnr !== 2) {
        requestnr++;

        req.reply([
          {
            id: 1,
            opprettetAv: { id: "Z990297", navn: "F_Z990297 E_Z990297" },
            opprettet: "2024-09-17T08:36:09.785Z",
            sistredigertAv: { id: "Z990297", navn: "F_Z990297 E_Z990297" },
            sistredigert: "2024-09-17T08:38:48.503Z",
            brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
            brevtittel: "Informasjon om saksbehandlingstid",
            status: { type: "Kladd" },
            distribusjonstype: "SENTRALPRINT",
            mottaker: null,
            avsenderEnhet: null,
            spraak: "EN",
          },
        ]);
        //Det skjer visst en ekstra rerendering slik at det er det tredje kallet til endepunktet som er det riktige
      } else if (requestnr === 2) {
        req.reply([]);
      }
    }).as("getAlleBrevForSak");

    cy.intercept("DELETE", "/bff/skribenten-backend/sak/123456/brev/1", {
      statusCode: 204,
      body: null,
    }).as("deleteKladd");

    cy.wait("@getAlleBrevForSak");

    cy.contains("Informasjon om saksbehandlingstid").click();
    cy.contains("Slett kladd").click();
    cy.contains("Vil du slette kladden?").should("be.visible");
    cy.contains("Kladden vil bli slettet, og du kan ikke angre denne handlingen.").should("be.visible");
    cy.contains("Nei, behold kladden").should("be.visible");
    cy.contains("Ja, slett kladden").should("be.visible");
    cy.contains("Ja, slett kladden").click();
    cy.wait("@deleteKladd");
    cy.wait("@getAlleBrevForSak");
    cy.contains("Informasjon om saksbehandlingstid").should("not.be.visible");
  });

  it("bruker eksisterende kladd", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      req.reply([
        nyBrevInfo({ id: 1, opprettet: "2024-09-17T08:36:09.785Z" }),
        nyBrevInfo({ id: 2, opprettet: "2024-10-17T08:36:09.785Z" }),
      ]);
    }).as("getAlleBrevForSak");
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    });

    cy.wait("@getAlleBrevForSak");
    cy.contains("Informasjonsbrev").click();
    cy.get("p:contains('Informasjon om saksbehandlingstid')").eq(3).click();
    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");
    cy.get("select[name=spraak]").should("have.value", "NB");

    cy.contains("Mottatt soeknad").click().type("09.10.2024");
    cy.contains("Ytelse").click().type("Alderspensjon");
    cy.contains("Svartid uker").click().type("4");
    cy.contains("Åpne brev").click("left");

    cy.contains("Vil du bruke eksisterende kladd?").should("be.visible");
    cy.contains("Du har en eksisterende kladd basert på samme brevmal.").should("be.visible");
    cy.contains("Lag nytt brev").should("be.visible");
    cy.contains("Ja, bruk eksisterende kladd").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brev/2");
  });

  it("lager nytt brev selv om saken har eksisterende kladd", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      req.reply([nyBrevInfo({ id: 1, opprettet: "2024-09-17T08:36:09.785Z" })]);
    }).as("getAlleBrevForSak");
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    });
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      expect(req.body).deep.equal({
        brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
        spraak: "NB",
        avsenderEnhetsId: "4405",
        mottaker: null,
        saksbehandlerValg: {
          mottattSoeknad: "2024-10-09",
          ytelse: "Alderspensjon",
          svartidUker: "4",
        },
      });
      req.reply(nyBrevResponse({ info: nyBrevInfo({ id: 2 }) }));
    }).as("createBrev");

    cy.wait("@getAlleBrevForSak");
    cy.contains("Informasjonsbrev").click();
    cy.get("p:contains('Informasjon om saksbehandlingstid')").eq(2).click();
    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");
    cy.get("select[name=spraak]").should("have.value", "NB");

    cy.contains("Mottatt soeknad").click().type("09.10.2024");
    cy.contains("Ytelse").click().type("Alderspensjon");
    cy.contains("Svartid uker").click().type("4");
    cy.contains("Åpne brev").click("left");

    cy.contains("Vil du bruke eksisterende kladd?").should("be.visible");
    cy.contains("Du har en eksisterende kladd basert på samme brevmal.").should("be.visible");
    cy.contains("Lag nytt brev").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brev/2");
  });

  it("arkiverte brev i brevvelger skal ikke kunne gjøre endringer på brev, og kun navigere videre til brevbehandler", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      req.reply([
        nyBrevInfo({ id: 1, opprettet: "2024-09-17T08:36:09.785Z", status: { type: "Klar" }, journalpostId: 123_456 }),
      ]);
    }).as("getAlleBrevForSak");

    cy.visit('/saksnummer/123456/brevvelger?brevId="1"');
    cy.contains("Endre mottaker").should("not.exist");
    cy.contains("Slett brev").should("not.exist");
    cy.contains("Åpne brev").click("left");
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler?brevId=1");
  });
});
