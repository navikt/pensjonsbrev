import { formatISO } from "date-fns";

import type { BrevResponse } from "~/types/brev";

import { nyBrevResponse } from "../../utils/brevredigeringTestUtils";

describe("Brevredigering", () => {
  const hurtiglagreTidspunkt = formatISO(new Date());
  beforeEach(() => {
    cy.setupSakStubs();
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", { fixture: "brevResponse.json" }).as(
      "brev",
    );
    cy.fixture("brevResponseEtterLagring.json").then((brev: BrevResponse) => {
      brev.info.sistredigert = hurtiglagreTidspunkt;
      cy.intercept("put", "/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", brev).as(
        "hurtiglagreRedigertBrev",
      );
    });
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    }).as("modelSpecification");
    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", {
      fixture: "brevreservasjon.json",
    }).as("reservasjon");
  });

  it("Åpne brevredigering", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Saksbehandlingstiden vår er vanligvis 10 uker.").should("exist");
  });

  it("Autolagrer etter redigering", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret").should("exist");
    cy.contains("Dersom vi trenger flere opplysninger").click();
    cy.focused().type(" hello!");
    cy.wait("@hurtiglagreRedigertBrev", { timeout: 20000 });
    cy.contains("Lagret").should("exist");
    cy.contains("hello!").should("exist");
  });

  it("lagrer signatur og saksbehandlerValg ved fortsett klikk", () => {
    const brev = nyBrevResponse({});

    cy.intercept({ method: "put", pathname: "/bff/skribenten-backend/sak/123456/brev/1" }, (req) => {
      expect(req.body.saksbehandlerValg).deep.equal({
        mottattSoeknad: "2024-07-24",
        ytelse: "alderspensjon",
        land: "Mars",
        svartidUker: "10",
      });
      expect(req.body.redigertBrev.signatur.saksbehandlerNavn).to.equal("Det nye saksbehandlernavnet");
      req.reply(brev);
    }).as("lagreBrev");

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Land").click().type("{selectall}{backspace}").type("Mars");
    cy.contains("Underskrift").click().type("{selectall}{backspace}").type("Det nye saksbehandlernavnet");
    cy.contains("Fortsett").click();
    cy.contains("Fortsett til brevbehandler").should("be.visible").click();

    cy.wait("@lagreBrev", { timeout: 20000 }).should((req) => {
      expect(req.response?.statusCode).to.equal(200);
    });
    cy.location("pathname")
      .should("eq", "/saksnummer/123456/brevbehandler")
      .location("search")
      .should("eq", "?brevId=1");

    cy.get("@lagreBrev.all").should("have.length", 1);
  });
  it("Blokkerer redigering om brev er reservert av noen andre", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", { fixture: "brevreservasjon_opptatt.json" }).as(
      "brevreservasjon_opptatt",
    );
    cy.wait("@brevreservasjon_opptatt");
    cy.contains("Brevet er utilgjengelig for deg fordi Hugo Weaving har brevet åpent.").should("exist");
  });

  it("Gjenoppta redigering", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", { fixture: "brevreservasjon_opptatt.json" }).as(
      "brevreservasjon_opptatt",
    );
    cy.wait("@brevreservasjon_opptatt");
    cy.contains("Brevet er utilgjengelig for deg fordi Hugo Weaving har brevet åpent.").should("exist");

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", {
      fixture: "brevResponse_ny_hash.json",
    }).as("brev_ny_hash");
    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", { fixture: "brevreservasjon_ny_hash.json" }).as(
      "brevreservasjon_ny_hash",
    );
    cy.contains("Ja, åpne på nytt").click();
    cy.wait("@brev_ny_hash");

    cy.contains("Informasjon om saksbehandlingstiden vår med ny hash").should("exist");
  });

  it("Åpne brev som er reservert av noen andre", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", {
      statusCode: 423,
      fixture: "brevreservasjon_opptatt.json",
    }).as("brevreservasjon_opptatt");
    cy.visit("/saksnummer/123456/brev/1");

    cy.contains("Saksbehandlingstiden vår er vanligvis 10 uker.").should("not.exist");
    cy.contains("Brevet redigeres av noen andre").should("exist");
  });

  it("kan tilbakestille malen", () => {
    cy.intercept("POST", "/bff/skribenten-backend/brev/1/tilbakestill", {
      fixture: "brevResponse.json",
    });

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret").should("exist");
    cy.contains("Dersom vi trenger flere opplysninger").click();
    cy.focused().type(" hello!");

    cy.contains("Tilbakestill malen").click();
    cy.contains("Vil du tilbakestille brevmalen?").should("exist");
    cy.contains("Innholdet du har endret eller lagt til i brevet vil bli slettet.").should("exist");
    cy.contains("Du kan ikke angre denne handlingen.").should("exist");
    cy.contains("Ja, tilbakestill malen").click();
    cy.contains(" hello!").should("not.exist");
  });

  it("beholder brevet etter å ville tilbakestille", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret").should("exist");
    cy.contains("Dersom vi trenger flere opplysninger").click();
    cy.focused().type(" hello!");

    cy.contains("Tilbakestill malen").click();
    cy.contains("Nei, behold brevet").click();
    cy.contains(" hello!").should("exist");
  });

  it("kan ikke redigere et arkivert brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", {
      statusCode: 409,
    }).as("brev");
    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Brevet er arkivert, og kan derfor ikke redigeres.").should("exist");
    cy.contains("Gå til brevbehandler").click();
    cy.location("pathname").should("eq", "/saksnummer/123456/brevbehandler").location("search").should("eq", "");
  });

  it("kan toggle punktliste", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Du må melde").click();
    cy.getDataCy("editor-bullet-list").click();
    cy.get("ul li span:last").should("contain.text", "Du må melde");
    cy.getDataCy("editor-bullet-list").click();
    cy.get("ul li span:last").should("not.contain.text", "Du må melde");
  });

  it("lagrer ikke brev som har tomme non-nullable enum felt", () => {
    cy.fixture("modelSpecificationBrukertestBrevPensjon2025").then((spec) => {
      spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].utsiktenFraKontoret.nullable = false;
      spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].denBesteKaken.nullable = false;
      cy.intercept("GET", "/bff/skribenten-backend/brevmal/BRUKERTEST_BREV_PENSJON_2025/modelSpecification", spec);
    });
    cy.fixture("brevResponseTestBrev.json").then((brev) => {
      cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", {
        ...brev,
        ...{
          saksbehandlerValg: {
            // utsiktenFraKontoret: "MOT_TRAER_OG_NATUR",
            // denBesteKaken: "OSTEKAKE",
          },
        },
      });
    });

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Overstyring").click();
    cy.contains("Utsikten fra kontoret")
      .parent()
      .find('[type="radio"]')
      .first()
      .as("radioGroup1Option1")
      .should("not.be.checked");
    cy.get("@radioGroup1Option1").click().should("be.checked");
    cy.contains("Den beste kaken er")
      .parent()
      .find('[type="radio"]')
      .first()
      .as("radioGroup2Option1")
      .should("not.be.checked");

    cy.contains("Fortsett")
      .click()
      .location("pathname")
      .should("eq", "/saksnummer/123456/brev/1")
      .location("search")
      .should("eq", "");
    cy.contains("Obligatorisk: du må velge et alternativ").should("exist");

    cy.get("@radioGroup2Option1").click().should("be.checked");
    cy.contains("Obligatorisk: du må velge et alternativ").should("not.exist");
  });

  it("lagrer brev som har tomme nullable enum felt", () => {
    cy.fixture("modelSpecificationBrukertestBrevPensjon2025").then((spec) => {
      spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].utsiktenFraKontoret.nullable = true;
      spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].denBesteKaken.nullable = true;
      cy.intercept("GET", "/bff/skribenten-backend/brevmal/BRUKERTEST_BREV_PENSJON_2025/modelSpecification", spec);
    });
    cy.fixture("brevResponseTestBrev.json").then((brev) => {
      cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", {
        ...brev,
        ...{
          saksbehandlerValg: {
            // utsiktenFraKontoret: "MOT_TRAER_OG_NATUR",
            // denBesteKaken: "OSTEKAKE",
          },
        },
      });
    });

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Overstyring").should("not.exist");
    cy.contains("Utsikten fra kontoret")
      .parent()
      .find('[type="radio"]')
      .each((elm) => expect(elm).not.to.be.checked);
    cy.contains("Den beste kaken er")
      .parent()
      .find('[type="radio"]')
      .each((elm) => expect(elm).not.to.be.checked);

    cy.contains("Fortsett")
      .click()
      .location("pathname")
      .should("eq", "/saksnummer/123456/brev/1")
      .location("search")
      .should("eq", "");
    cy.contains("Obligatorisk: du må velge et alternativ").should("not.exist");
  });
});
