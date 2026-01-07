import { Distribusjonstype } from "~/types/brev";

import { nyBrevInfo } from "../utils/brevredigeringTestUtils";

describe("P1 med forsidebrev", () => {
  beforeEach(() => {
    cy.setupSakStubs();
    cy.viewport(1200, 1400);
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
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([p1BrevInfo]);
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/p1", (request) => {
      request.reply(p1BrevData);
    });
    cy.intercept("GET", "/bff/skribenten-backend/land", (request) => {
      request.reply(countriesSubset);
    });
    cy.visit("/saksnummer/123456/brevbehandler");
  });

  it("viser vedlegg og redigeringsknapp kun for P1", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([p1BrevInfo, annetBrev]);
    });

    cy.contains(annetBrev.brevtittel).click();
    cy.contains("Vedlegg").should("not.be.visible");
    cy.get('[data-cy="p1-edit-button"]').should("not.be.visible");
    cy.contains(p1BrevInfo.brevtittel).click();
    cy.contains("Overstyring av vedlegg - P1").should("not.exist");
    cy.contains("Vedlegg").should("be.visible").get('[data-cy="p1-edit-button"]').should("be.visible").click();
    cy.contains("Overstyring av vedlegg - P1").should("be.visible");
  });

  it("viser og lagrer data i henholdsvis visningformat og api format", () => {
    cy.contains(p1BrevInfo.brevtittel).click().get('[data-cy="p1-edit-button"]').click();
    cy.contains("3. Innvilget pensjon").should("be.visible");

    // verifiser at land vises som norsk navn på land selv om payload fra backend er string ala "NO"
    cy.contains("Land").parent().find("input").should("have.value", "Bulgaria");
    // verifiser at datoformat vises som dd.MM.yyyy selv om payload fra backend er string på formatet yyyy-MM-dd
    cy.contains("Vedtaksdato").parent().find("input").should("have.value", "23.09.2022");

    // verifiser at land satt til blank sender null til backend
    // verifiser at dato satt til 31.12.2021 sender 2021-12-31 til backend
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/p1", (request) => {
      expect(request.body.innvilgedePensjoner[0].institusjon.vedtaksdato).to.eq("2021-12-31");
      expect(request.body.innvilgedePensjoner[0].institusjon.land).to.eq(null);
      request.reply("200");
    });
    cy.contains("Land").parent().find("input").type("{selectAll}{backspace}{downArrow}{enter}");
    cy.contains("Vedtaksdato").parent().find("input").type("{selectall}{backspace}31.12.2021");
    cy.contains("Lagre").click();
  });

  it("viser valideringsfeil når påkrevde felt mangler", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/p1", (request) => {
      request.reply(p1BrevDataWithMissingFields);
    });

    cy.contains(p1BrevInfo.brevtittel).click().get('[data-cy="p1-edit-button"]').click();
    cy.contains("3. Innvilget pensjon").should("be.visible");

    // Forsøk å lagre uten å fylle ut påkrevde felt
    cy.contains("Lagre").click();

    // Verifiser at valideringsfeil vises i alert
    cy.contains("Skjemaet har feil som må rettes").should("be.visible");

    // Verifiser at individuelle feilmeldinger vises
    cy.contains("Institusjonsnavn er obligatorisk").should("be.visible");
    cy.contains("Pensjonstype må velges").should("be.visible");
  });

  it("viser valideringsfeil for ugyldig datoformat", () => {
    cy.contains(p1BrevInfo.brevtittel).click().get('[data-cy="p1-edit-button"]').click();
    cy.contains("3. Innvilget pensjon").should("be.visible");

    // Skriv inn ugyldig dato
    cy.contains("Vedtaksdato").parent().find("input").type("{selectall}{backspace}32.13.2022");
    cy.contains("Lagre").click();
    cy.contains("Skjemaet har feil som må rettes").should("exist");
    cy.contains("Ugyldig dato").should("be.visible");
  });

  it("viser valideringsfeil når tekstfelt overskrider makslengde", () => {
    cy.contains(p1BrevInfo.brevtittel).click().get('[data-cy="p1-edit-button"]').click();
    cy.contains("3. Innvilget pensjon").should("be.visible");

    const veryLongText = "A".repeat(300);
    cy.contains("Land")
      .closest("td")
      .contains("PIN")
      .parent()
      .find("input")
      .type("{selectall}{backspace}" + veryLongText, { delay: 0 })
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

    cy.contains(p1BrevInfo.brevtittel).click().get('[data-cy="p1-edit-button"]').click();
    cy.contains("3. Innvilget pensjon").should("be.visible");

    cy.contains("4. Avslag på pensjon").click();
    cy.contains("Institusjon som har avslått").should("be.visible");

    // Forsøk å lagre med feil i fane 3 (Innvilget)
    cy.contains("Lagre").click();

    // Verifiser at vi blir navigert tilbake til fane 3
    cy.contains("Institusjon som gir pensjonen").should("be.visible");
    cy.contains("Skjemaet har feil som må rettes").should("be.visible");
    cy.contains("Institusjonsnavn er obligatorisk").should("be.visible");
  });

  it("viser suksessmelding ved vellykket lagring", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/p1", (request) => {
      request.reply("200");
    });

    cy.contains(p1BrevInfo.brevtittel).click().get('[data-cy="p1-edit-button"]').click();
    cy.contains("3. Innvilget pensjon").should("be.visible");
    cy.contains("Lagre").click();
    cy.contains("Endringene ble lagret").should("be.visible");
  });
});

const annetBrev = nyBrevInfo({
  id: 2,
  opprettetAv: { id: "Z990297", navn: "Ola Nordmann" },
  opprettet: "2021-09-01T12:00:00",
  sistredigertAv: { id: "Z990297", navn: "Ola Nordmann" },
  sistredigert: "2021-09-01T12:00:00",
  brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
  brevtittel: "Informasjon om saksbehandlingstid",
  status: { type: "Kladd" },
  distribusjonstype: Distribusjonstype.SENTRALPRINT,
});

const p1BrevInfo = nyBrevInfo({
  id: 1,
  opprettetAv: { id: "Z990297", navn: "Ola Nordmann" },
  opprettet: "2021-09-01T12:00:00",
  sistredigertAv: { id: "Z990297", navn: "Ola Nordmann" },
  sistredigert: "2021-09-01T12:00:00",
  brevkode: "P1_SAMLET_MELDING_OM_PENSJONSVEDTAK_V2",
  brevtittel: "P1 med forsidebrev",
  status: { type: "Kladd" },
  distribusjonstype: Distribusjonstype.SENTRALPRINT,
});

const p1BrevData = {
  innehaver: {
    fornavn: "Penny",
    etternavn: "Pernsjon",
    etternavnVedFoedsel: null,
    foedselsdato: "1955-02-23",
    adresselinje: "Towarowa 3",
    poststed: null,
    postnummer: null,
    landkode: "BG",
  },
  forsikrede: {
    fornavn: "Penny",
    etternavn: "Pernsjon",
    etternavnVedFoedsel: null,
    foedselsdato: "1955-02-23",
    adresselinje: "Towarova 3",
    poststed: null,
    postnummer: null,
    landkode: "BG",
  },
  sakstype: "ALDER",
  innvilgedePensjoner: [
    {
      institusjon: {
        institusjonsnavn: "BUL99AFL",
        pin: "99999999",
        saksnummer: "88888888",
        vedtaksdato: "2022-09-23",
        land: "BG",
      },
      pensjonstype: "Alder",
      datoFoersteUtbetaling: "2022-03-01",
      utbetalt: "3972 NOK\nMånedlig (12 per år)",
      grunnlagInnvilget: "ProRata",
      reduksjonsgrunnlag: "PaaGrunnAvAndreYtelserEllerAnnenInntekt",
      vurderingsperiode: "6 uker fra samlet melding om pensjons-vedtak er mottatt.",
      adresseNyVurdering: "Nav familie- og pensjonsytelser\nPostboks 6600 Etterstad\n0607 Oslo\nNorge",
    },
    {
      institusjon: {
        institusjonsnavn: "BUL99AFL",
        pin: "99999999",
        saksnummer: "99999999",
        vedtaksdato: "2022-08-26",
        land: "BG",
      },
      pensjonstype: "Ufoere",
      datoFoersteUtbetaling: "2024-01-01",
      utbetalt: "1500 NOK\nMånedlig (12 per år)",
      grunnlagInnvilget: "IHenholdTilNasjonalLovgivning",
      reduksjonsgrunnlag: "PaaGrunnAvOverlappendeGodskrevnePerioder",
      vurderingsperiode: "6 uker fra samlet melding om pensjons-vedtak er mottatt.",
      adresseNyVurdering: "Nav familie- og pensjonsytelser\nPostboks 6600 Etterstad\n0607 Oslo\nNorge",
    },
  ],
  avslaattePensjoner: [
    {
      institusjon: {
        institusjonsnavn: "BUL99AFL",
        pin: "99999999",
        saksnummer: "88888888",
        vedtaksdato: "2022-08-26",
        land: "BG",
      },
      pensjonstype: "Ufoere",
      avslagsbegrunnelse: "OpptjeningsperiodePaaMindreEnnEttAar",
      vurderingsperiode: "6 uker fra samlet melding om pensjons-vedtak er mottatt.",
      adresseNyVurdering: "Nav familie- og pensjonsytelser\nPostboks 6600 Etterstad\n0607 Oslo\nNorge",
    },
  ],
  utfyllendeInstitusjon: {
    navn: "Nav Familie- og pensjonsytelser",
    adresselinje: "Postboks 6600 Etterstad",
    poststed: "Oslo",
    postnummer: "0607",
    landkode: "NO",
    institusjonsID: null,
    faksnummer: null,
    telefonnummer: "55 55 33 34",
    epost: null,
    dato: "2026-01-06",
  },
};

const countriesSubset = [
  { kode: "GB", navn: "Storbritannia" },
  { kode: "FR", navn: "Frankrike" },
  { kode: "DK", navn: "Danmark" },
  { kode: "BG", navn: "Bulgaria" },
  { kode: "NO", navn: "Norge" },
];

const p1BrevDataWithMissingFields = {
  ...p1BrevData,
  ...{
    innvilgedePensjoner: [
      {
        institusjon: {
          institusjonsnavn: "", // Mangler påkrevd felt
          pin: "123456",
          saksnummer: "",
          vedtaksdato: null,
          land: null,
        },
        pensjonstype: null,
        datoFoersteUtbetaling: null,
        utbetalt: "",
        grunnlagInnvilget: null,
        reduksjonsgrunnlag: null,
        vurderingsperiode: "",
        adresseNyVurdering: "",
      },
    ],
    avslaattePensjoner: [],
  },
};
