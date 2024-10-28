import { format, formatISO } from "date-fns";

import type { BrevResponse } from "~/types/brev";

import { nyBrevInfo, nyBrevResponse, nyRedigertBrev } from "../../utils/brevredigeringTestUtils";

describe("autolagring", () => {
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
      req.reply(
        nyBrevResponse({
          info: nyBrevInfo({
            sistredigert: hurtiglagreTidspunkt,
          }),
          saksbehandlerValg: {
            mottattSoeknad: "2024-09-10",
            ytelse: "Supplerende stønad",
            land: "Spania",
            svartidUker: "3",
          },
          redigertBrev: nyRedigertBrev({
            blocks: [
              {
                id: -2_030_215_963,
                editable: true,
                content: [
                  {
                    id: 1_507_865_607,
                    text: "We received your application for ",
                    editedText: "Wae received your application for ",
                    type: "LITERAL",
                  },
                  {
                    id: -726_051_414,
                    text: "Supplerende stønad",
                    type: "VARIABLE",
                  },
                  {
                    id: -142_236_915,
                    text: " from the National Insurance authorities in ",
                    editedText: null,
                    type: "LITERAL",
                  },
                  {
                    id: -1_660_311_050,
                    text: "Spania",
                    type: "VARIABLE",
                  },
                  {
                    id: 1_063_425,
                    text: " on ",
                    editedText: null,
                    type: "LITERAL",
                  },
                  {
                    id: -694_080_035,
                    text: "10 September 2024",
                    type: "VARIABLE",
                  },
                  {
                    id: 46,
                    text: ".",
                    editedText: null,
                    type: "LITERAL",
                  },
                  {
                    id: -1_894_607_013,
                    items: [
                      {
                        id: -503_413_477,
                        content: [
                          {
                            id: -503_413_508,
                            text: "item 1 - TODO remove itemlist",
                            editedText: "item 1 - TODO remove itemlist hei hei",
                            type: "LITERAL",
                          },
                        ],
                      },
                      {
                        id: 886_663_488,
                        content: [
                          {
                            id: 886_663_457,
                            text: "item 2 - TODO remove itemlist",
                            editedText: null,
                            type: "LITERAL",
                          },
                        ],
                      },
                      {
                        id: -867_386_911,
                        content: [
                          {
                            id: -867_386_942,
                            text: "item 3 - TODO remove itemlist",
                            editedText: null,
                            type: "LITERAL",
                          },
                        ],
                      },
                    ],
                    deletedItems: [],
                    type: "ITEM_LIST",
                  },
                ],
                deletedContent: [],
                type: "PARAGRAPH",
              },
              {
                id: 822_540_105,
                editable: true,
                content: [
                  {
                    id: -1_114_690_237,
                    text: "Our processing time for this type of application is usually ",
                    editedText: null,
                    type: "LITERAL",
                  },
                  {
                    id: 1_834_595_758,
                    text: "3",
                    type: "VARIABLE",
                  },
                  {
                    id: 1_838_606_639,
                    text: " weeks.",
                    editedText: null,
                    type: "LITERAL",
                  },
                ],
                deletedContent: [],
                type: "PARAGRAPH",
              },
              {
                id: -1_153_661_566,
                editable: true,
                content: [
                  {
                    id: -1_153_661_597,
                    text: "We will contact you if we need you to provide more information.",
                    editedText: null,
                    type: "LITERAL",
                  },
                ],
                deletedContent: [],
                type: "PARAGRAPH",
              },
              {
                id: 1_767_516_329,
                editable: true,
                content: [
                  {
                    id: 1_767_516_298,
                    text: "Duty to report changes",
                    editedText: null,
                    type: "LITERAL",
                  },
                ],
                deletedContent: [],
                type: "TITLE1",
              },
              {
                id: 173_660_319,
                editable: true,
                content: [
                  {
                    id: 173_660_288,
                    text: "You must notify us immediately if there are any changes that may affect your case, such as a change in your marital status or if you move.",
                    editedText: null,
                    type: "LITERAL",
                  },
                ],
                deletedContent: [],
                type: "PARAGRAPH",
              },
            ],
          }),
        }),
      );
    }).as("autoLagring");

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.contains("Overstyring").click();
    cy.getDataCy("datepicker-editor").should("have.value", "24.07.2024");
    cy.getDataCy("datepicker-editor").click().clear().type("10.09.2024");
    cy.wait("@autoLagring");
    cy.contains("10 September 2024").should("exist");
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
      req.reply(
        nyBrevResponse({
          info: nyBrevInfo({
            sistredigert: hurtiglagreTidspunkt,
          }),
          redigertBrev: nyRedigertBrev({
            blocks: [
              {
                id: -2_030_215_963,
                editable: true,
                content: [
                  {
                    id: 1_507_865_607,
                    text: "We received your application for ",
                    editedText: "Wae received your application for ",
                    type: "LITERAL",
                  },
                  {
                    id: -726_051_414,
                    text: "Supplerende stønad",
                    type: "VARIABLE",
                  },
                  {
                    id: -142_236_915,
                    text: " from the National Insurance authorities in ",
                    editedText: null,
                    type: "LITERAL",
                  },
                  {
                    id: -1_660_311_050,
                    text: "Spania",
                    type: "VARIABLE",
                  },
                  {
                    id: 1_063_425,
                    text: " on ",
                    editedText: null,
                    type: "LITERAL",
                  },
                  {
                    id: -694_080_035,
                    text: "10 September 2024",
                    type: "VARIABLE",
                  },
                  {
                    id: 46,
                    text: ".",
                    editedText: null,
                    type: "LITERAL",
                  },
                  {
                    id: -1_894_607_013,
                    items: [
                      {
                        id: -503_413_477,
                        content: [
                          {
                            id: -503_413_508,
                            text: "item 1 - TODO remove itemlist",
                            editedText: "item 1 - TODO remove itemlist hei hei",
                            type: "LITERAL",
                          },
                        ],
                      },
                      {
                        id: 886_663_488,
                        content: [
                          {
                            id: 886_663_457,
                            text: "item 2 - TODO remove itemlist",
                            editedText: null,
                            type: "LITERAL",
                          },
                        ],
                      },
                      {
                        id: -867_386_911,
                        content: [
                          {
                            id: -867_386_942,
                            text: "item 3 - TODO remove itemlist",
                            editedText: null,
                            type: "LITERAL",
                          },
                        ],
                      },
                    ],
                    deletedItems: [],
                    type: "ITEM_LIST",
                  },
                ],
                deletedContent: [],

                type: "PARAGRAPH",
              },
              {
                id: 822_540_105,
                editable: true,
                content: [
                  {
                    id: -1_114_690_237,
                    text: "Our processing time for this type of application is usually ",
                    editedText: null,
                    type: "LITERAL",
                  },
                  {
                    id: 1_834_595_758,
                    text: "3",
                    type: "VARIABLE",
                  },
                  {
                    id: 1_838_606_639,
                    text: " weeks.",
                    editedText: null,
                    type: "LITERAL",
                  },
                ],
                deletedContent: [],

                type: "PARAGRAPH",
              },
              {
                id: -1_153_661_566,
                editable: true,
                content: [
                  {
                    id: -1_153_661_597,
                    text: "We will contact you if we need you to provide more information.",
                    editedText: null,
                    type: "LITERAL",
                  },
                ],
                deletedContent: [],

                type: "PARAGRAPH",
              },
              {
                id: 1_767_516_329,
                editable: true,
                content: [
                  {
                    id: 1_767_516_298,
                    text: "Duty to report changes",
                    editedText: null,
                    type: "LITERAL",
                  },
                ],
                deletedContent: [],

                type: "TITLE1",
              },
              {
                id: 173_660_319,
                editable: true,
                content: [
                  {
                    id: 173_660_288,
                    text: "You must notify us immediately if there are any changes that may affect your case, such as a change in your marital status or if you move.",
                    editedText: null,
                    type: "LITERAL",
                  },
                ],
                deletedContent: [],

                type: "PARAGRAPH",
              },
            ],
          }),
          saksbehandlerValg: {
            mottattSoeknad: "2024-09-10",
            ytelse: "Supplerende stønad",
            land: "Spania",
            svartidUker: "3",
          },
        }),
      );
    }).as("autoLagring");

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.contains("Overstyring").click();
    //TODO - se om vi kan få .clear() til å fungere
    cy.contains("Ytelse").click().type("{selectall}{backspace}{selectall}{backspace}").type("Supplerende stønad");
    cy.wait("@autoLagring");
    cy.contains("Lagret kl " + format(hurtiglagreTidspunkt, "HH:mm")).should("exist");
    cy.contains(" Supplerende stønad").should("exist");
  });

  it("autolagrer når nullable tekst felter tømmes", () => {
    const brevResponse = nyBrevResponse({});

    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
      expect(req.body).contains({
        mottattSoeknad: "2024-07-24",
        ytelse: "alderspensjon",
        land: null,
        svartidUker: "10",
      });
      req.reply(brevResponse);
    }).as("autoLagring");

    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/signatur", (req) => {
      expect(req.body).contains("Sak S. Behandler");
      req.reply(brevResponse);
    }).as("autoLagring");

    cy.visit("/saksnummer/123456/brev/1");
    cy.get("span:contains('Spania')").should("exist");
    cy.contains("Land").click().type("{selectall}{backspace}{selectall}{backspace}");
    cy.wait("@autoLagring");
    cy.get("span:contains('Spania')").should("not.exist");
  });

  it("autolagrer ikke før alle avhengige felter er utfylt", () => {
    cy.fixture("saksbehandlerValgResponse.json").then((saksbehandlerValgResponse) => {
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
        req.reply(saksbehandlerValgResponse);
      }).as("autoLagring");
    });

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.contains("Saksbehandlingstiden vår er vanligvis 10 uker.").should("exist");
    cy.contains("Inkluder venter svar afp").click();
    cy.getDataCy("datepicker-editor").eq(0).should("have.value", "");
    cy.getDataCy("datepicker-editor").eq(0).click().clear().type("10.09.2024");
    cy.wait(4000);
    //verifiserer at autolagring ikke har skjedd enda
    cy.get("@autoLagring.all").then((interceptions) => {
      expect(interceptions).to.have.length(0);
    });
    cy.contains("Uttak alderspensjon prosent").click().type("55");
    cy.contains("Your accumulated pension capital").should("exist");
  });

  it("autolagrer signatur", () => {
    const brevResponse = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        signatur: {
          hilsenTekst: "Yours sincerely",
          saksbehandlerRolleTekst: "Caseworker",
          saksbehandlerNavn: "Saksbehandler navn",
          attesterendeSaksbehandlerNavn: "",
          navAvsenderEnhet: "Nav Arbeid og ytelser Sørlandet",
        },
      }),
    });
    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
      expect(req.body).contains({
        mottattSoeknad: "2024-07-24",
        ytelse: "alderspensjon",
        land: "Spania",
        svartidUker: "10",
      });
      req.reply(brevResponse);
    }).as("saksbehandlerValg");
    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/signatur", (req) => {
      expect(req.body).contains("Saksbehandler navn");
      req.reply(brevResponse);
    }).as("autoLagring");

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.getDataCy("brev-editor-saksbehandler").should("have.text", "Sak S. Behandler");
    cy.contains("Signatur").click().type("{selectall}{backspace}").type("Saksbehandler navn");
    cy.getDataCy("brev-editor-saksbehandler").should("have.text", "Sak S. Behandler");
    cy.wait(4000);
    cy.get("@autoLagring.all").then((interceptions) => {
      expect(interceptions).to.have.length(1);
    });
    cy.getDataCy("brev-editor-saksbehandler").should("have.text", "Saksbehandler navn");
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

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Søknaden din vil også bli oversendt utlandet fordi").should("not.exist");
    cy.contains("Soknad oversendes til utlandet").click();
    cy.wait("@autoLagring");
    cy.contains("Søknaden din vil også bli oversendt utlandet fordi").should("exist");
  });
});
