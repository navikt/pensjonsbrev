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
});
