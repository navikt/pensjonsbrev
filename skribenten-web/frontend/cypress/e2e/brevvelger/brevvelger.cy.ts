import sak from "../../fixtures/sak.json";

describe("Brevvelger spec", () => {
  beforeEach(() => {
    cy.setupSakStubs();
  });

  it("Søk med saksnummer", () => {
    cy.visit("/");
    cy.contains("Brevvelger").should("not.exist");
    cy.contains("Saksnummer").click();
    cy.focused().type("123{enter}");
    cy.contains("Finner ikke saksnummer").should("exist");
    cy.focused().type("456{enter}");
    cy.contains("Brevvelger");
  });

  it("Søk etter brevmal", () => {
    cy.visit("/saksnummer/123456/brevvelger");

    cy.getDataCy("brevmal-search").click().type("Varsel tilbakekreving");
    cy.getDataCy("category-item").should("contain.text", "Feilutbetaling");
    cy.getDataCy("brevmal-button").contains("Varsel - tilbakekreving").should("be.visible");

    cy.focused().clear().type("!");
    cy.getDataCy("category-item").should("have.length", 0);
    cy.getDataCy("brevmal-button").should("have.length", 0);
    cy.getDataCy("ingen-treff-alert");

    cy.focused().type("{esc}");
    cy.getDataCy("category-item").should("have.length", 9).and("not.have.class", "aksel-accordion__item--open");
  });

  it("Favoritter", () => {
    cy.intercept("GET", "/bff/skribenten-backend/me/favourites", []).as("ingen favoritter");
    cy.visit("/saksnummer/123456/brevvelger");
    cy.get(".aksel-accordion__item").contains("Favoritter").should("not.exist");

    cy.contains("Informasjonsbrev").click();
    cy.get("p:contains('Informasjon om saksbehandlingstid')").should("have.length", 1);
    cy.contains("Informasjon om saksbehandlingstid").click();

    cy.intercept("POST", "/bff/skribenten-backend/me/favourites", (request) => {
      expect(request.body).to.eq("INFORMASJON_OM_SAKSBEHANDLINGSTID");
      request.reply({});
    }).as("Legg til favoritt");
    cy.intercept("GET", "/bff/skribenten-backend/me/favourites", ["INFORMASJON_OM_SAKSBEHANDLINGSTID"]).as(
      "1 favoritt",
    );

    cy.getDataCy("add-favorite-button").click();
    cy.get(".aksel-accordion__item").contains("Favoritter").should("exist").and("have.length", 1);
    //skal finnes 2 elementer i DOM'en
    cy.get("p:contains('Informasjon om saksbehandlingstid')").should("have.length", 2);
    cy.get("p:contains('Informasjon om saksbehandlingstid')").filter(":visible").should("have.length", 1);

    cy.intercept("DELETE", "/bff/skribenten-backend/me/favourites", (request) => {
      expect(request.body).to.eq("INFORMASJON_OM_SAKSBEHANDLINGSTID");
      request.reply({});
    }).as("Fjern favoritt");
    cy.intercept("GET", "/bff/skribenten-backend/me/favourites", []).as("ingen favoritter");
    cy.getDataCy("remove-favorite-button").click();

    cy.get(".aksel-accordion__item").contains("Favoritter").should("not.exist");
  });

  it("Bestill Exstream brev", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream", (request) => {
      expect(request.body).contains({
        brevkode: "PE_IY_05_027",
        idTSSEkstern: null,
        spraak: "NN",
        brevtittel: "",
        enhetsId: "4405",
      });
      request.reply({ fixture: "bestillBrevExstream.json" });
    }).as("bestill exstream");

    cy.visit("/saksnummer/123456/brevvelger", {
      onBeforeLoad(window) {
        cy.stub(window, "open").as("window-open");
      },
    });

    cy.getDataCy("category-item").contains("Feilutbetaling").click();
    cy.getDataCy("brevmal-button").contains("Varsel - tilbakekreving").click();

    cy.get("select[name=spraak]").select("Nynorsk");
    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");

    cy.getDataCy("order-letter").click();
    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
    cy.getDataCy("order-letter-success-message");
  });

  it("Skriv notat", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream", (request) => {
      expect(request.body).contains({
        brevkode: "PE_IY_03_156",
        spraak: "NB",
        brevtittel: "GGMU",
        enhetsId: "4405",
      });
      request.reply({ fixture: "bestillBrevNotat.json" });
    }).as("bestill notat");

    cy.visit("/saksnummer/123456/brevvelger", {
      onBeforeLoad(window) {
        cy.stub(window, "open").as("window-open");
      },
    });

    cy.getDataCy("brevmal-search").click().type("notat");
    cy.getDataCy("brevmal-button").click();

    cy.getDataCy("språk-velger-select").should("not.exist");

    cy.getDataCy("brev-title-textfield").click().type("GGMU");
    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");
    cy.getDataCy("order-letter").click();

    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mbdok://PE2@brevklient/dokument/453864212?token=1711023327721&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
    cy.getDataCy("order-letter-success-message");
  });

  it("Bestill E-blankett", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/bestillBrev/exstream/eblankett", (request) => {
      expect(request.body).contains({
        brevkode: "E001",
        landkode: "GBR",
        mottakerText: "Haaland",
        enhetsId: "4405",
      });
      request.reply({ fixture: "bestillBrevEblankett.json" });
    }).as("bestill e-blankett");

    cy.visit("/saksnummer/123456/brevvelger", {
      onBeforeLoad(window) {
        cy.stub(window, "open").as("window-open");
      },
    });

    cy.getDataCy("brevmal-search").click().type("E 001");
    cy.getDataCy("brevmal-button").contains("E 001").click();

    cy.getDataCy("order-letter").click();
    cy.get("select[name=enhetsId]").select("Nav Arbeid og ytelser Innlandet");

    cy.get("label").contains("Land").parent().find(".aksel-error-message");
    cy.get("select[name=landkode]").select("Storbritannia");
    cy.get("label").contains("Land").parent().find(".aksel-error-message").should("not.exist");

    cy.getDataCy("mottaker-text-textfield").parent().find(".aksel-error-message");
    cy.getDataCy("mottaker-text-textfield").type("Haaland");
    cy.getDataCy("mottaker-text-textfield").parent().find(".aksel-error-message").should("not.exist");

    cy.getDataCy("order-letter").click();
    cy.get("@window-open").should(
      "have.been.calledOnceWithExactly",
      "mbdok://PE2@brevklient/dokument/453864284?token=1711101230605&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
    cy.getDataCy("order-letter-success-message");
  });

  it("enhetsId som url param gjenspeiles i form og inputs", () => {
    cy.visit('/saksnummer/123456/brevvelger?templateId=PE_IY_03_163&enhetsId="4815"', {
      onBeforeLoad(window) {
        cy.stub(window, "open").as("window-open");
      },
    });
    cy.wait("@enheter");
    cy.getDataCy("avsenderenhet-select").should("have.value", "4815");
  });

  it("Saksinformasjon i subheader", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456", (request) => {
      request.reply({
        ...sak,
        sak: { ...sak.sak, ...{ foedselsdato: "1999-12-31" } },
      });
    });

    cy.visit("/saksnummer/123456/brevvelger");
    cy.contains("Født: 31.12.1999").should("exist");
    cy.contains("Død: 31.12.2014").should("not.exist");
    cy.contains("Egen ansatt").should("not.exist");
    cy.contains("Vergemål").should("not.exist");
    cy.contains("Diskresjon").should("not.exist");

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456", (request) => {
      request.reply({
        ...sak,
        sak: { ...sak.sak, ...{ foedselsdato: "1999-12-31" } },
      });
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brukerstatus", (request) => {
      request.reply({
        doedsfall: "2014-12-31",
        erSkjermet: true,
        vergemaal: true,
        adressebeskyttelse: true,
      });
    });

    cy.visit("/saksnummer/123456/brevvelger");
    cy.contains("Født: 31.12.1999").should("exist");
    cy.contains("Død: 31.12.2014").should("exist");
    cy.contains("Egen ansatt").should("exist");
    cy.contains("Vergemål").should("exist");
    cy.contains("Diskresjon").should("exist");
  });
});
