import { type BrevInfo, Distribusjonstype } from "../../src/types/brev";
import { nyBrevInfo } from "../utils/brevredigeringTestUtils";

describe("Brevbehandler", () => {
  const kladdBrev = nyBrevInfo({
    id: 1,
    opprettetAv: { id: "Z990297", navn: "Ola Nordmann" },
    opprettet: "2021-09-01T12:00:00",
    sistredigertAv: { id: "Z990297", navn: "Ola Nordmann" },
    sistredigert: "2021-09-01T12:00:00",
    brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
    brevtittel: "Informasjon om saksbehandlingstid",
    status: { type: "Kladd" },
    distribusjonstype: Distribusjonstype.SENTRALPRINT,
  });

  const klarBrev: BrevInfo = { ...kladdBrev, status: { type: "Klar" } };

  const brevSomSendesSomLokalPrint = {
    ...klarBrev,
    brevkode: "BREV_SOM_SENDES_SOM_LOKALPRINT",
    brevtittel: "Brev som sendes som lokalprint",
    id: 2,
    distribusjonstype: "LOKALPRINT",
    status: { type: "Klar" },
  };

  beforeEach(() => {
    cy.setupSakStubs();
    cy.viewport(1200, 1400);
    cy.visit("/saksnummer/123456/brevbehandler");
  });

  it("saken inneholder ingen brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", { body: [] });
    cy.contains("Fant ingen brev som er under behandling").should("be.visible");
  });

  it("kan ferdigstille og sende brev med sentralprint", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (request) => {
      request.reply({ journalpostId: 80_912, error: null });
    });

    const brevResponse = [kladdBrev, klarBrev];

    let requestNo = 0;
    //endepunktet blir kallt ved sidelast av brevbehandler, men også ved ferdigstill modalen som finner alle ferdigstilte brev
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([brevResponse[requestNo++]]);
    });

    cy.intercept("PATCH", "/bff/skribenten-backend/sak/123456/brev/1", (request) => {
      expect(request.body).contains({ saksId: "123456", brevId: 1, laastForRedigering: true });
      //er ikke interesert i innholdet i redigert brev + saksbehandlerValg
      request.reply({ info: klarBrev, redigertBrev: {}, saksbehandlerValg: {} });
    });

    //åpner brevet
    cy.contains(kladdBrev.brevtittel).click();
    //brev med id 1
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler?brevId=1");

    //verifiserer innhold før / etter lås
    cy.contains("Fortsett redigering").should("be.visible");
    cy.contains("Brevet er klart for sending").click();
    cy.contains("Fortsett redigering").should("not.exist");
    cy.get('[data-cy="brevbehandler-distribusjonstype"] input[type="radio"][value="SENTRALPRINT"]').should(
      "be.checked",
    );

    //---- ferdigstiller brevet
    //tanstack knappen hovrer over ferdigstill knappen - vå i klikker på vestre side av knappen som er synlig. Se om vi kan fikse dette
    cy.contains("Ferdigstill 1 brev").click("left");
    cy.contains("Vil du ferdigstille, og sende disse brevene?").should("be.visible");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="1"]`).should("be.checked");
    cy.contains("Ja, send valgte brev").click();

    //verifisering av kvittering
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/kvittering");
    cy.contains("Sendt til mottaker").should("be.visible");
    cy.contains(kladdBrev.brevtittel).click();
    cy.contains("Distribueres via").should("be.visible");
    cy.contains("Sentral print").should("be.visible");
    cy.contains("Journalpost ID").should("be.visible");
    cy.contains("80912").should("be.visible");
  });

  it("kan ferdigstille og sende brev med lokalprint", () => {
    //ser ikke ut til å være en god måte å gi ulik respons på hvert kall, så vi må ha en teller
    let hentBrevRequestNr = 0;
    let patchBrevRequestNr = 0;
    const lokalprintBrev = { ...kladdBrev, distribusjonstype: "LOKALPRINT", status: { type: "Klar" } };

    const brevResponse = [kladdBrev, lokalprintBrev];
    const patchResponse = [klarBrev, lokalprintBrev];

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (request) => {
      request.reply({ journalpostId: 80_912, error: null });
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/pdf/80912", (request) => {
      request.reply({ fixture: "helloWorldPdf.txt", headers: { "content-type": "application/pdf" } });
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([brevResponse[hentBrevRequestNr++]]);
    });
    cy.intercept("PATCH", "/bff/skribenten-backend/sak/123456/brev/1", (request) => {
      if (patchBrevRequestNr === 0) {
        expect(request.body).contains({ saksId: "123456", brevId: 1, laastForRedigering: true });
      } else {
        expect(request.body).contains({ saksId: "123456", brevId: 1, distribusjonstype: "LOKALPRINT" });
      }
      //er ikke interesert i innholdet i redigert brev + saksbehandlerValg
      request.reply({ info: patchResponse[patchBrevRequestNr++], redigertBrev: {}, saksbehandlerValg: {} });
    });

    //åpner brevet
    cy.contains(kladdBrev.brevtittel).click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler?brevId=1");

    //verifiserer innhold før / etter lås
    cy.contains("Fortsett redigering").should("be.visible");
    cy.contains("Brevet er klart for sending").click();
    cy.contains("Fortsett redigering").should("not.exist");
    cy.contains("Lokalprint").click();

    //---- ferdigstiller brevet
    //tanstack knappen hovrer over ferdigstill knappen - vå i klikker på vestre side av knappen som er synlig. Se om vi kan fikse dette
    cy.contains("Ferdigstill 1 brev").click("left");
    cy.contains("Vil du ferdigstille, og sende disse brevene?").should("be.visible");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="1"]`).should("be.checked");

    cy.contains("Ja, send valgte brev").click();

    //verifisering av kvittering
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/kvittering");
    cy.contains("Lokalprint - sendt til joark").should("be.visible");
    cy.contains(kladdBrev.brevtittel).click();
    cy.contains("Distribueres via").should("be.visible");
    cy.contains("Lokal print").should("be.visible");
    cy.contains("Journalpost ID").should("be.visible");
    cy.contains("80912").should("be.visible");
    cy.contains("Åpne utskrivbar fil i ny fane").should("be.visible");
  });

  it("kan ferdigstille og sende brev med lokalprint selv om henting av pdf feiler", () => {
    //ser ikke ut til å være en god måte å gi ulik respons på hvert kall, så vi må ha en teller
    let hentBrevRequestNr = 0;
    let patchBrevRequestNr = 0;
    const lokalprintBrev = { ...kladdBrev, distribusjonstype: "LOKALPRINT", status: { type: "Klar" } };

    const brevResponse = [kladdBrev, lokalprintBrev];
    const patchResponse = [klarBrev, lokalprintBrev];

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (request) => {
      request.reply({ journalpostId: 80_912, error: null });
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([brevResponse[hentBrevRequestNr++]]);
    });

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/pdf/80912", (request) => {
      request.reply({ body: "simulerer en feil ved henting av pdf for journalpostId'en", statusCode: 500 });
    });

    cy.intercept("PATCH", "/bff/skribenten-backend/sak/123456/brev/1", (request) => {
      if (patchBrevRequestNr === 0) {
        expect(request.body).contains({ saksId: "123456", brevId: 1, laastForRedigering: true });
      } else {
        expect(request.body).contains({ saksId: "123456", brevId: 1, distribusjonstype: "LOKALPRINT" });
      }
      //er ikke interesert i innholdet i redigert brev + saksbehandlerValg
      request.reply({ info: patchResponse[patchBrevRequestNr++], redigertBrev: {}, saksbehandlerValg: {} });
    });

    //åpner brevet
    cy.contains(kladdBrev.brevtittel).click();
    //brev med id 1
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler?brevId=1");

    //verifiserer innhold før / etter lås
    cy.contains("Fortsett redigering").should("be.visible");
    cy.contains("Brevet er klart for sending").click();
    cy.contains("Fortsett redigering").should("not.exist");
    cy.contains("Lokalprint").click();

    //---- ferdigstiller brevet
    //tanstack knappen hovrer over ferdigstill knappen - vå i klikker på vestre side av knappen som er synlig. Se om vi kan fikse dette
    cy.contains("Ferdigstill 1 brev").click("left");
    cy.contains("Vil du ferdigstille, og sende disse brevene?").should("be.visible");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="1"]`).should("be.checked");

    cy.contains("Ja, send valgte brev").click();

    //verifisering av kvittering
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/kvittering");
    cy.contains("Lokalprint - sendt til joark").should("be.visible");
    cy.contains(kladdBrev.brevtittel).click();
    cy.contains("Distribueres via").should("be.visible");
    cy.contains("Lokal print").should("be.visible");
    cy.contains("Journalpost ID").should("be.visible");
    cy.contains("80912").should("be.visible");
    cy.contains("Åpne utskrivbar fil i ny fane").should("be.visible");
  });

  it("kan sende flere ferdigstilte brev samtidig", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (request) => {
      request.reply({ journalpostId: 80_912, error: null });
    });
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/2/pdf/send", (request) => {
      request.reply({ journalpostId: 80_913, error: null });
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/pdf/80913", (request) => {
      request.reply({ fixture: "helloWorldPdf.txt", headers: { "content-type": "application/pdf" } });
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([klarBrev, brevSomSendesSomLokalPrint]);
    });

    cy.contains("Send 2 ferdigstilte brev").click("left");
    cy.contains("Vil du ferdigstille, og sende disse brevene?").should("be.visible");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="1"]`).should("be.checked");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="2"]`).should("be.checked");
    cy.contains("Ja, send valgte brev").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/kvittering");

    cy.contains("Sendt til mottaker").should("be.visible");
    cy.contains(kladdBrev.brevtittel).click();
    cy.contains("Distribueres via").should("be.visible");
    cy.contains("Sentral print").should("be.visible");
    cy.contains("Journalpost ID").should("be.visible");
    cy.contains("80912").should("be.visible");
    cy.contains(kladdBrev.brevtittel).click();

    cy.contains("Lokalprint - sendt til joark").should("be.visible");
    cy.contains(brevSomSendesSomLokalPrint.brevtittel).click();
    cy.get('p:contains("Distribueres via")').eq(1).should("be.visible");
    cy.contains("Lokal print").should("be.visible");
    cy.get('p:contains("Journalpost ID")').eq(1).should("be.visible");
    cy.contains("80913").should("be.visible");
    cy.contains("Åpne utskrivbar fil i ny fane").should("be.visible");
  });

  it("velger hvilke brev som skal sendes", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (request) => {
      request.reply({ journalpostId: 80_912, error: null });
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([klarBrev, brevSomSendesSomLokalPrint]);
    });

    cy.contains("Send 2 ferdigstilte brev").click("left");
    cy.contains("Vil du ferdigstille, og sende disse brevene?").should("be.visible");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="1"]`).should("be.checked");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="2"]`).click();
    cy.contains("Ja, send valgte brev").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/kvittering");

    cy.contains("Sendt til mottaker").should("be.visible");
    cy.contains(kladdBrev.brevtittel).click();
    cy.contains("Distribueres via").should("be.visible");
    cy.contains("Sentral print").should("be.visible");
    cy.contains("Journalpost ID").should("be.visible");
    cy.contains("80912").should("be.visible");

    cy.contains("Sendt til lokalprint").should("not.exist");
    cy.contains(brevSomSendesSomLokalPrint.brevtittel).should("not.exist");
  });

  it("viser pdf når er brev er valgt", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/pdf", (request) => {
      request.reply({ fixture: "helloWorldPdf.txt" });
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([kladdBrev]);
    });

    cy.contains(kladdBrev.brevtittel).click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler?brevId=1");
    cy.contains("Hello World").should("be.visible");
  });

  it("sletter et brev", () => {
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([kladdBrev]);
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/pdf", (request) => {
      request.reply({ fixture: "helloWorldPdf.txt" });
    });
    cy.intercept("DELETE", "/bff/skribenten-backend/sak/123456/brev/1", (request) => {
      request.reply({});
    });

    cy.contains(kladdBrev.brevtittel).click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler?brevId=1");
    cy.contains("Slett").click();
    cy.contains("Vil du slette brevet?").should("be.visible");
    cy.contains("Ja, slett brevet").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler");
  });

  it("får error i responsen for ferdigstill & prøver igjen", () => {
    let requestNo = 0;
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (request) => {
      if (requestNo === 0) {
        requestNo++;
        request.reply({
          journalpostId: null,
          error: {
            brevIkkeStoettet: "Dette brevet er ikke støttet",
            tekniskgrunn: "Her er det en teknisk grunn",
            beskrivelse:
              "Her kommer det også en veldig god, og begrunnende beskrivelse. Denne skal kanskje være litt lenger, siden den forklarer mer om hva som har skjedd?",
          },
        });
      } else {
        request.reply({ journalpostId: 80_912, error: null });
      }
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([klarBrev]);
    });

    cy.contains("Send 1 ferdigstilt brev").click("left");
    cy.contains("Vil du ferdigstille, og sende disse brevene?").should("be.visible");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="1"]`).should("be.checked");
    cy.contains("Ja, send valgte brev").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/kvittering");

    cy.contains("Kunne ikke sende brev").should("be.visible");
    cy.contains(kladdBrev.brevtittel).click();

    cy.contains("Brevet ble ikke sendt pga Her er det en teknisk grunn. Prøv igjen.").should("be.visible");
    cy.contains(
      "Her kommer det også en veldig god, og begrunnende beskrivelse. Denne skal kanskje være litt lenger, siden den forklarer mer om hva som har skjedd?",
    ).should("be.visible");
    cy.contains("Prøv igjen").should("be.visible");
    cy.get("button").contains("Prøv igjen").click();

    cy.contains("Sendt til mottaker").should("be.visible");
    cy.contains("Distribueres via").should("be.visible");
    cy.contains("Sentral print").should("be.visible");
    cy.contains("Journalpost ID").should("be.visible");
    cy.contains("80912").should("be.visible");
  });

  it("brev som blir sendt blir fjernet fra brevbehandler", () => {
    const brevSomIkkeSkalSendes = { ...klarBrev, id: 2 };

    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/2/pdf/send", (request) => {
      request.reply({ journalpostId: 80_912, error: null });
    });
    let requestNo = 0;
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      //vi henter brevene ved sidelast, men også når i åpner modalen
      if (requestNo === 0 || requestNo === 1) {
        requestNo++;
        request.reply([klarBrev, brevSomIkkeSkalSendes]);
      } else {
        request.reply([brevSomIkkeSkalSendes]);
      }
    });
    cy.contains("Send 2 ferdigstilte brev").click("left");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="1"]`).click();
    cy.contains("Ja, send valgte brev").click();
    cy.contains("Gå til brevbehandler").click();
    cy.get("label:contains('Informasjon om saksbehandlingstid')").should("have.length", 1);
  });

  it("et arkivert brev kan ikke endre på noe informasjon, og kan kun sendes på nytt", () => {
    const arkivertBrev: BrevInfo = { ...klarBrev, status: { type: "Arkivert" }, journalpostId: 123_456 };

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([arkivertBrev]);
    });

    cy.contains("Informasjon om saksbehandlingstid").click();

    cy.contains("Brevet er klart for sending").should("not.exist");
    cy.contains("Fortsett redigering").should("not.exist");
    cy.contains("Distribusjon").should("not.exist");

    cy.contains("Brevet er journalført med id 123456. Brevet kan ikke endres").should("be.visible");
    cy.contains("Brevet har ikke blitt sendt. Du kan prøve å sende brevet på nytt.").should("be.visible");
    cy.contains("Ferdigstill 1 brev").click("left");
    cy.contains("Ja, send valgte brev").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/kvittering");
  });
});
