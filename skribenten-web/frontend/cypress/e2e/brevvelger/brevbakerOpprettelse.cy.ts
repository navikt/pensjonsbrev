describe("Oppretter brevbakerbrev", () => {
  beforeEach(() => {
    cy.setupSakStubs();
  });

  it("kun obligatoriske felter vises ved opprettelse av brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    });

    cy.visit("saksnummer/123456/brevvelger?templateId=INFORMASJON_OM_SAKSBEHANDLINGSTID");
    cy.contains("Mottatt søknad").should("exist");
    cy.contains("Ytelse").should("exist");
    cy.contains("Land").should("not.exist");
    cy.contains("inkluder venter svar afp").should("not.exist");
    cy.contains("Svartid uker").should("exist");
  });

  // it("boolean felter vises ikke ved opprettelse av brev", () => {
  //   cy.intercept("GET", "/bff/skribenten-backend/brevmal/UT_ORIENTERING_OM_SAKSBEHANDLINGSTID/modelSpecification", {
  //     fixture: "modelSpecificationOrienteringOmSaksbehandlingstid.json",
  //   });
  //   cy.visit("/saksnummer/123456/brevvelger?templateId=UT_ORIENTERING_OM_SAKSBEHANDLINGSTID&spraak=NB&enhetsId=null");
  //   cy.contains("Mottatt søknad").should("exist");
  //   cy.contains("Soknad oversendes til utlandet").should("not.exist");
  // });

  it("validering trigges", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    }).as("getBrevmal");
    cy.visit("/saksnummer/123456/brevvelger?templateId=INFORMASJON_OM_SAKSBEHANDLINGSTID");
    cy.wait("@getBrevmal");
    cy.contains("Åpne brev").click();
    cy.get(".aksel-error-message").should("have.length", 4);
  });

  it("oppretter brev", () => {
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
        vedtaksId: null,
      });

      req.reply({ fixture: "brevResponse.json" });
    });

    cy.visit("/saksnummer/123456/brevvelger?templateId=INFORMASJON_OM_SAKSBEHANDLINGSTID");

    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");
    cy.get("select[name=spraak]").should("have.value", "NB");

    cy.contains("Mottatt søknad").click().type("09.10.2024");
    cy.contains("Ytelse").click().type("Alderspensjon");
    cy.contains("Svartid uker").click().type("4");
    cy.contains("Åpne brev").click();
    cy.location("pathname").should("eq", "/saksnummer/123456/brev/1");
    cy.location("search").should("include", "enhetsId");
  });

  it("oppretter brev som har non-nullable boolean felt", () => {
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/UT_ORIENTERING_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecificationOrienteringOmSaksbehandlingstid.json",
    });

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      expect(req.body).deep.equal({
        brevkode: "UT_ORIENTERING_OM_SAKSBEHANDLINGSTID",
        spraak: "NB",
        avsenderEnhetsId: "4405",
        mottaker: null,
        saksbehandlerValg: {
          mottattSøknad: "2024-10-09",
          søknadOversendesTilUtlandet: false,
        },
        vedtaksId: null,
      });
      req.reply({ fixture: "brevResponse.json" });
    });

    cy.visit("/saksnummer/123456/brevvelger?templateId=UT_ORIENTERING_OM_SAKSBEHANDLINGSTID");

    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");
    cy.get("select[name=spraak]").should("have.value", "NB");

    cy.contains("Mottatt søknad").click().type("09.10.2024");
    cy.contains("Åpne brev").click();
    cy.location("pathname").should("eq", "/saksnummer/123456/brev/1");
    cy.location("search").should("include", "enhetsId");
  });

  it("kan opprette brev som har tom saksbehandlerValg", () => {
    cy.intercept(
      "GET",
      "/bff/skribenten-backend/brevmal/PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS/modelSpecification",
      (req) => {
        req.reply({ types: {}, letterModelTypeName: null });
      },
    );

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev", (req) => {
      expect(req.body).deep.equal({
        brevkode: "PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS",
        spraak: "NB",
        avsenderEnhetsId: "4405",
        mottaker: null,
        saksbehandlerValg: {},
        vedtaksId: null,
      });
      req.reply({ fixture: "brevResponse.json" });
    });

    cy.visit("saksnummer/123456/brevvelger?templateId=PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS");
    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");
    cy.contains("Åpne brev").click();
    cy.location("pathname").should("eq", "/saksnummer/123456/brev/1");
    cy.location("search").should("include", "enhetsId");
  });

  it("oppretter brev som har tomme nullable enum felt", () => {
    cy.fixture("modelSpecificationBrukertestBrevPensjon2025").then((spec) => {
      spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].utsiktenFraKontoret.nullable = true;
      spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].denBesteKaken.nullable = true;

      cy.intercept("GET", "/bff/skribenten-backend/brevmal/BRUKERTEST_BREV_PENSJON_2025/modelSpecification", spec).as(
        "modelSpecification",
      );
    });
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev", {
      fixture: "brevResponseTestBrev.json",
    });

    cy.visit("/saksnummer/123456/brevvelger?templateId=BRUKERTEST_BREV_PENSJON_2025");
    cy.wait("@modelSpecification");

    cy.contains("Brevvelger").should("exist");

    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");

    cy.contains("Mot trær og natur").should("not.exist");

    cy.contains("Åpne brev").click();

    cy.contains("Obligatorisk: du må velge et alternativ").should("not.exist");

    cy.location("pathname").should("eq", "/saksnummer/123456/brev/1");
    cy.location("search").should("include", "enhetsId");
  });

  it("oppretter ikke brev som har tomme non-nullable enum felt", () => {
    cy.fixture("modelSpecificationBrukertestBrevPensjon2025").then((spec) => {
      spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].utsiktenFraKontoret.nullable = false;
      spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].denBesteKaken.nullable = false;
      cy.intercept("GET", "/bff/skribenten-backend/brevmal/BRUKERTEST_BREV_PENSJON_2025/modelSpecification", spec).as(
        "modelSpecification",
      );
    });
    cy.fixture("brevResponseTestBrev.json").then((brev) => {
      cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev", {
        ...brev,
        ...{
          saksbehandlerValg: {
            utsiktenFraKontoret: "MOT_TRAER_OG_NATUR",
            denBesteKaken: "OSTEKAKE",
          },
        },
      });
    });

    cy.visit("/saksnummer/123456/brevvelger?templateId=BRUKERTEST_BREV_PENSJON_2025");
    cy.wait("@modelSpecification");
    cy.contains("Brevvelger").should("exist");

    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");
    cy.get("select[name=spraak]").should("have.value", "NB");

    cy.contains("Åpne brev").click();

    cy.location("pathname").should("not.eq", "/saksnummer/123456/brev/1");
    cy.contains("Obligatorisk: du må velge et alternativ").should("exist");

    cy.contains("Mot trær og natur").click();
    cy.contains("Ostekake").click();
    cy.contains("@errorMessage").should("not.exist");

    cy.contains("Åpne brev").click();
    cy.location("pathname").should("eq", "/saksnummer/123456/brev/1");
    cy.location("search").should("include", "enhetsId");
  });
});
