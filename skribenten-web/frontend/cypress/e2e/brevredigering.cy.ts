import { format, formatISO } from "date-fns";

import type { BrevResponse } from "~/types/brev";

describe("Brevredigering", () => {
  const hurtiglagreTidspunkt = formatISO(new Date());
  beforeEach(() => {
    cy.setupSakStubs();
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1?reserver=true", { fixture: "brevResponse.json" }).as(
      "brev",
    );
    cy.fixture("brevResponseEtterLagring.json").then((brev: BrevResponse) => {
      brev.info.sistredigert = hurtiglagreTidspunkt;
      cy.intercept("put", "/bff/skribenten-backend/brev/1/redigertBrev", brev).as("hurtiglagreRedigertBrev");
    });
    cy.intercept("GET", "/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification", {
      fixture: "modelSpecification.json",
    }).as("modelSpecification");
    cy.intercept("GET", "/bff/skribenten-backend/brev/1/reservasjon", {
      fixture: "brevreservasjon.json",
    }).as("reservasjon");

    cy.viewport(1200, 1400);
  });

  it("Åpne brevredigering", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Saksbehandlingstiden vår er vanligvis 10 uker.").should("exist");
  });

  it("Autolagrer etter redigering", () => {
    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.contains("Dersom vi trenger flere opplysninger").click();
    cy.focused().type(" hello!");
    cy.wait("@hurtiglagreRedigertBrev");
    cy.contains("Lagret kl " + format(hurtiglagreTidspunkt, "HH:mm")).should("exist");
    cy.contains("hello!").should("exist");
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
    cy.intercept("PUT", "/bff/skribenten-backend/brev/1/redigertBrev", {
      fixture: "brevResponse.json",
    });

    cy.visit("/saksnummer/123456/brev/1");
    cy.contains("Lagret 26.07.2024 ").should("exist");
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
    cy.contains("Lagret 26.07.2024 ").should("exist");
    cy.contains("Dersom vi trenger flere opplysninger").click();
    cy.focused().type(" hello!");

    cy.contains("Tilbakestill malen").click();
    cy.contains("Nei, behold brevet").click();
    cy.contains(" hello!").should("exist");
  });

  describe("autolagring", () => {
    it("lagrer endring av dato-felt automatisk", () => {
      cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
        req.reply({
          info: {
            id: 1,
            opprettetAv: "Z990297",
            opprettet: "2024-09-10T11:16:53.128Z",
            sistredigertAv: "Z990297",
            sistredigert: "2024-09-12T13:13:43.667Z",
            brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
            status: {
              type: "UnderRedigering",
              redigeresAv: "Z990297",
            },
            distribusjonstype: "SENTRALPRINT",
            mottaker: null,
          },
          redigertBrev: {
            title: "Information about application processing time",
            sakspart: {
              gjelderNavn: "TRYGG ANBEFALING",
              gjelderFoedselsnummer: "21418744917",
              saksnummer: "22981081",
              dokumentDato: "12/09/2024",
            },
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
                originalType: null,
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
                originalType: null,
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
                originalType: null,
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
                originalType: null,
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
                originalType: null,
                type: "PARAGRAPH",
              },
            ],
            signatur: {
              hilsenTekst: "Yours sincerely",
              saksbehandlerRolleTekst: "Caseworker",
              saksbehandlerNavn: "TODO",
              attesterendeSaksbehandlerNavn: "",
              navAvsenderEnhet: "NAV Arbeid og ytelser Sørlandet",
            },
            deletedBlocks: [],
          },
          redigertBrevHash: "a213163062cea478ac9c56192fb2d2038ad0e9595d858f22237884a84130ff14",
          saksbehandlerValg: {
            mottattSoeknad: "2024-09-10",
            ytelse: "Supplerende stønad",
            land: "Spania",
            svartidUker: "3",
          },
        });
      }).as("autoLagring");

      cy.visit("/saksnummer/123456/brev/1");
      cy.contains("Lagret 26.07.2024 ").should("exist");
      cy.getDataCy("datepicker-editor").should("have.value", "24.07.2024");
      cy.getDataCy("datepicker-editor").click().clear().type("10.09.2024");
      cy.wait("@autoLagring");
      cy.contains("10 September 2024").should("exist");
      cy.contains("Lagret 12.09.2024 15:13").should("exist");
    });

    it("lagrer endring av tekst-felt automatisk", () => {
      cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", (req) => {
        req.reply({
          info: {
            id: 1,
            opprettetAv: "Z990297",
            opprettet: "2024-09-10T11:16:53.128Z",
            sistredigertAv: "Z990297",
            sistredigert: "2024-09-12T13:13:43.667Z",
            brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
            status: {
              type: "UnderRedigering",
              redigeresAv: "Z990297",
            },
            distribusjonstype: "SENTRALPRINT",
            mottaker: null,
          },
          redigertBrev: {
            title: "Information about application processing time",
            sakspart: {
              gjelderNavn: "TRYGG ANBEFALING",
              gjelderFoedselsnummer: "21418744917",
              saksnummer: "22981081",
              dokumentDato: "12/09/2024",
            },
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
                originalType: null,
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
                originalType: null,
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
                originalType: null,
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
                originalType: null,
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
                originalType: null,
                type: "PARAGRAPH",
              },
            ],
            signatur: {
              hilsenTekst: "Yours sincerely",
              saksbehandlerRolleTekst: "Caseworker",
              saksbehandlerNavn: "TODO",
              attesterendeSaksbehandlerNavn: "",
              navAvsenderEnhet: "NAV Arbeid og ytelser Sørlandet",
            },
            deletedBlocks: [],
          },
          redigertBrevHash: "a213163062cea478ac9c56192fb2d2038ad0e9595d858f22237884a84130ff14",
          saksbehandlerValg: {
            mottattSoeknad: "2024-09-10",
            ytelse: "Supplerende stønad",
            land: "Spania",
            svartidUker: "3",
          },
        });
      }).as("autoLagring");

      cy.visit("/saksnummer/123456/brev/1");
      cy.contains("Lagret 26.07.2024 ").should("exist");
      //TODO - se om vi kan få .clear() til å fungere
      cy.contains("Ytelse").click().type("{selectall}{backspace}{selectall}{backspace}").type("Supplerende stønad");
      cy.wait("@autoLagring");
      cy.contains("Lagret 12.09.2024 15:13").should("exist");
      cy.contains(" Supplerende stønad").should("exist");
    });

    it("autolagrer ikke før alle avhengige felter er utfylt", () => {
      cy.intercept("PUT", "/bff/skribenten-backend/brev/1/saksbehandlerValg", {
        fixture: "saksbehandlerValgResponse.json",
      }).as("autoLagring");

      cy.visit("/saksnummer/123456/brev/1");
      cy.contains("Lagret 26.07.2024 ").should("exist");
      cy.contains("Saksbehandlingstiden vår er vanligvis 10 uker.").should("exist");
      cy.contains("Inkluder venter svar afp").click();
      cy.getDataCy("datepicker-editor").eq(1).should("have.value", "");
      cy.getDataCy("datepicker-editor").eq(1).click().clear().type("10.09.2024");
      cy.wait(4000);
      //verifiserer at autolagring ikke har skjedd enda
      cy.get("@autoLagring.all").then((interceptions) => {
        expect(interceptions).to.have.length(0);
      });
      cy.contains("Uttak alderspensjon prosent").click().type("55");
      cy.contains("Your accumulated pension capital").should("exist");
    });
  });
});
