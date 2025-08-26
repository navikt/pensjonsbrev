import { format, formatISO } from "date-fns";

import brev from "../../fixtures/brevResponse.json";

describe("autolagring", () => {
  const hurtiglagreTidspunkt = formatISO(new Date());
  beforeEach(() => {
    cy.setupSakStubs();
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", brev).as("brev");
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    }).as("modelSpecification");

    cy.viewport(1200, 1400);
  });

  it("lagrer endring av dato-felt automatisk", () => {
    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
      expect(req.body).contains({
        mottattSoeknad: "2024-09-10",
        ytelse: "alderspensjon",
        land: "Spania",
        svartidUker: "10",
      });
      req.reply({ ...brev, info: { ...brev.info, sistredigert: hurtiglagreTidspunkt }, saksbehandlerValg: req.body });
    }).as("autoLagring");

    cy.visit("/saksnummer/123456/brev/1");
    cy.wait("@brev");
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.contains("Overstyring").click();
    cy.getDataCy("datepicker-editor").should("have.value", "24.07.2024");
    cy.getDataCy("datepicker-editor").click().clear().type("10.09.2024");

    cy.wait("@autoLagring");
    cy.contains("Lagret kl " + format(hurtiglagreTidspunkt, "HH:mm")).should("exist");
  });

  it("lagrer endring av tekst-felt automatisk", () => {
    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
      expect(req.body).contains({
        mottattSoeknad: "2024-07-24",
        ytelse: "Supplerende stønad",
        land: "Spania",
        svartidUker: "10",
      });
      req.reply({ ...brev, saksbehandlerValg: req.body, info: { ...brev.info, sistredigert: hurtiglagreTidspunkt } });
    }).as("autoLagring");

    cy.visit("/saksnummer/123456/brev/1");
    cy.wait("@brev");
    cy.wait("@modelSpecification");
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.contains("Overstyring").click();
    cy.contains("Ytelse").click().type("{selectall}{backspace}Supplerende stønad", { delay: 20 });
    cy.wait("@autoLagring");
    cy.contains("Lagret kl " + format(hurtiglagreTidspunkt, "HH:mm")).should("exist");
  });

  it("autolagrer når nullable tekst felter tømmes", () => {
    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
      expect(req.body).contains({
        mottattSoeknad: "2024-07-24",
        ytelse: "alderspensjon",
        land: null,
        svartidUker: "10",
      });
      req.reply({ ...brev, saksbehandlerValg: req.body, info: { ...brev.info, sistredigert: hurtiglagreTidspunkt } });
    }).as("autoLagring");

    cy.visit("/saksnummer/123456/brev/1");
    cy.get("span:contains('Spania')").should("exist");
    cy.contains("Land").click().type("{selectall}{backspace}{selectall}{backspace}");
    cy.wait("@autoLagring");
  });

  it("autolagrer ikke før alle avhengige felter er utfylt", () => {
    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
      expect(req.body).deep.equal({
        mottattSoeknad: "2024-07-24",
        ytelse: "alderspensjon",
        land: "Spania",
        svartidUker: "10",
        inkluderVenterSvarAFP: {
          uttakAlderspensjonProsent: "55",
          uttaksDato: "2024-09-10",
        },
      });
      req.reply({ ...brev, saksbehandlerValg: req.body, info: { ...brev.info, sistredigert: hurtiglagreTidspunkt } });
    }).as("autoLagringSaksbehandlerValg");

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.contains("Saksbehandlingstiden vår er vanligvis 10 uker.").should("exist");
    cy.contains("Inkluder venter svar AFP").click();
    cy.getDataCy("datepicker-editor").eq(0).should("have.value", "");
    cy.getDataCy("datepicker-editor").eq(0).click().clear().type("10.09.2024");
    //verifiserer at autolagring ikke har skjedd enda
    cy.get("@autoLagringSaksbehandlerValg.all").should("have.length", 0);
    cy.contains("Uttak alderspensjon prosent").click().type("55");
    cy.get("@autoLagringSaksbehandlerValg.all").should("have.length", 1);
  });

  it("autolagrer signatur", () => {
    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", (req) => {
      expect(req.body.signatur.saksbehandlerNavn).contains("Min nye underskrift");
      req.reply({ ...brev, redigertBrev: req.body, info: { ...brev.info, sistredigert: hurtiglagreTidspunkt } });
    }).as("autoLagring");

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.getDataCy("brev-editor-saksbehandler").should("have.text", "Sak S. Behandler");
    cy.contains("Underskrift").click().type("{selectall}{backspace}").type("Min nye underskrift");
    cy.getDataCy("brev-editor-saksbehandler").should("have.text", "Min nye underskrift");
    cy.wait("@autoLagring");
    cy.get("@autoLagring.all").then((interceptions) => {
      expect(interceptions).to.have.length(1);
    });
    cy.getDataCy("brev-editor-saksbehandler").should("have.text", "Min nye underskrift");
  });

  it("autolagring av boolean felter", () => {
    cy.fixture("modelSpecificationOrienteringOmSaksbehandlingstid.json").then((modelSpecification) => {
      cy.intercept(
        "GET",
        "/bff/skribenten-backend/brevmal/UT_ORIENTERING_OM_SAKSBEHANDLINGSTID/modelSpecification",
        (req) => req.reply(modelSpecification),
      );
    });

    cy.fixture("orienteringOmSaksbehandlingstidResponse.json").then((response) => {
      cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (req) => req.reply(response));
    });

    cy.fixture("orienteringOmSaksbehandlingstidResponseMedSoknadOversendesTilUtlandet.json").then((response) => {
      cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
        req.reply(response);
      }).as("autoLagring");
    });
    cy.fixture("orienteringOmSaksbehandlingstidResponseMedSoknadOversendesTilUtlandet.json").then((response) => {
      cy.intercept("PUT", "/bff/skribenten-backend/brev/1/signatur", (req) => {
        expect(req.body).contains("F_Z990297 E_Z990297");
        req.reply(response);
      }).as("signatur");
    });

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Søknaden din vil også bli oversendt utlandet fordi").should("not.exist");
    cy.contains("Søknad oversendes til utlandet").click();
    cy.wait("@autoLagring");
    cy.contains("Søknaden din vil også bli oversendt utlandet fordi").should("exist");
  });
});
