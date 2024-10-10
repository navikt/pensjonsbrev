describe("Autobrev", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.viewport(1200, 1400);
  });

  it("Bestill ikke-redigerbar exstream brev", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream", (request) => {
      expect(request.body).contains({
        brevkode: "PE_IY_03_051",
        idTSSEkstern: null,
        spraak: "NB",
        isSensitive: false,
        brevtittel: "",
        enhetsId: "4405",
      });
      request.reply({ fixture: "bestillAutobrevExstream.json" });
    }).as("bestill exstream");

    cy.visit("/saksnummer/123456/brevvelger", {
      onBeforeLoad(window) {
        cy.stub(window, "open").as("window-open");
      },
    });

    cy.get("label:contains('Varsel')").click();
    cy.getDataCy("brevmal-button").contains("Varsel om mulig avslag/opphør p.g.a. manglende opplysninger").click();

    cy.get("select[name=enhetsId]").select("NAV Arbeid og ytelser Innlandet");
    cy.getDataCy("is-sensitive").contains("Nei").click({ force: true });

    cy.contains("Åpne brev").click();

    cy.contains("Vil du bestille og sende brevet?").should("be.visible");
    cy.contains(
      "Brevet blir sendt til angitt mottaker, og lagt til i dokumentoversikten. Du kan ikke angre denne handlingen.",
    ).should("be.visible");

    cy.contains("Ja, bestill og send brevet").click();
    cy.contains("Brevet er bestilt og sendt til mottaker med journalpostId 453864183").should("be.visible");
  });

  it("Ingen api kall gjøres når man lukker modalen", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream").as("bestillAutobrev");

    cy.visit("/saksnummer/123456/brevvelger");

    cy.get("label:contains('Varsel')").click();
    cy.getDataCy("brevmal-button").contains("Varsel om mulig avslag/opphør p.g.a. manglende opplysninger").click();

    cy.get("select[name=enhetsId]").select("NAV Arbeid og ytelser Innlandet");
    cy.getDataCy("is-sensitive").contains("Nei").click({ force: true });

    cy.contains("Åpne brev").click();
    cy.contains("Vil du bestille og sende brevet?").should("be.visible");
    cy.getDataCy("autobrev-modal-nei").click();
    cy.contains("Vil du bestille og sende brevet?").should("not.exist");

    cy.get("@bestillAutobrev.all").then((interceptions) => {
      expect(interceptions).to.have.length(0);
    });
  });
});
