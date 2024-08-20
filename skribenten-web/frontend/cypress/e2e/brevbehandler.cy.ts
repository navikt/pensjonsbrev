describe("Brevbehandler", () => {
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

    const kladdBrev = {
      id: 1,
      opprettetAv: "Ola Nordmann",
      opprettet: "2021-09-01T12:00:00",
      sistredigertAv: "Ola Nordmann",
      sistredigert: "2021-09-01T12:00:00",
      brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
      status: { type: "Kladd" },
      distribusjonstype: "SENTRALPRINT",
    };

    const klarBrev = { ...kladdBrev, status: { type: "Klar" } };

    const brevResponse = [kladdBrev, klarBrev];

    let requestNo = 0;
    //endepunktet blir kallt ved sidelast av brevbehandler, men også ved ferdigstill modalen som finner alle ferdigstilte brev
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([brevResponse[requestNo++]]);
    });

    cy.intercept("PATCH", "/bff/skribenten-backend/sak/123456/brev/1", (request) => {
      expect(request.body).contains({ sakId: "123456", brevId: 1, laastForRedigering: true });

      //er ikke interesert i innholdet i redigert brev + saksbehandlerValg
      request.reply({ info: klarBrev, redigertBrev: {}, saksbehandlerValg: {} });
    });

    //åpner brevet
    cy.contains("INFORMASJON_OM_SAKSBEHANDLINGSTID").click();
    //brev med id 1
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler?brevId=%221%22");

    //verifiserer innhold før / etter lås
    cy.contains("Fortsett redigering").should("be.visible");
    cy.contains("Lås for redigering").click();
    cy.contains("Fortsett redigering").should("not.exist");
    cy.get('[data-cy="brevbehandler-distribusjonstype"] input[type="radio"][value="SENTRALPRINT"]').should(
      "be.checked",
    );

    //---- ferdigstiller brevet
    //tanstack knappen hovrer over ferdigstill knappen - vå i klikker på vestre side av knappen som er synlig. Se om vi kan fikse dette
    cy.contains("Ferdigstill brev").click("left");
    cy.contains("Vil du ferdigstille, og sende disse brevene?").should("be.visible");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="1"]`).should("be.checked");
    cy.contains("Ja, send valgte brev").click();

    //verifisering av kvittering
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/kvittering");
    cy.contains("Sendt til mottaker").should("be.visible");
    cy.contains("INFORMASJON_OM_SAKSBEHANDLINGSTID").click();
    cy.contains("Distribueres via").should("be.visible");
    cy.contains("Sentral print").should("be.visible");
    cy.contains("Journalpost ID").should("be.visible");
    cy.contains("80912").should("be.visible");
  });

  it("kan ferdigstille og sende brev med lokalprint", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (request) => {
      request.reply({ journalpostId: 80_912, error: null });
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/pdf", (request) => {
      request.reply(new Blob(["%PDF-1.4\n%..."], { type: "application/pdf" }));
    });
    const kladdBrev = {
      id: 1,
      opprettetAv: "Ola Nordmann",
      opprettet: "2021-09-01T12:00:00",
      sistredigertAv: "Ola Nordmann",
      sistredigert: "2021-09-01T12:00:00",
      brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
      status: { type: "Kladd" },
      distribusjonstype: "SENTRALPRINT",
    };

    const klarBrev = { ...kladdBrev, status: { type: "Klar" } };
    const lokalprintBrev = { ...kladdBrev, distribusjonstype: "LOKALPRINT", status: { type: "Klar" } };

    const brevResponse = [kladdBrev, lokalprintBrev];
    const patchResponse = [klarBrev, lokalprintBrev];

    let hentBrevRequestNr = 0;
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([brevResponse[hentBrevRequestNr++]]);
    });

    let patchBrevRequestNr = 0;
    cy.intercept("PATCH", "/bff/skribenten-backend/sak/123456/brev/1", (request) => {
      if (patchBrevRequestNr === 0) {
        expect(request.body).contains({ sakId: "123456", brevId: 1, laastForRedigering: true });
      } else {
        expect(request.body).contains({ sakId: "123456", brevId: 1, distribusjonstype: "LOKALPRINT" });
      }
      request.reply({ info: patchResponse[patchBrevRequestNr++], redigertBrev: {}, saksbehandlerValg: {} });
    });

    //åpner brevet
    cy.contains("INFORMASJON_OM_SAKSBEHANDLINGSTID").click();
    //brev med id 1
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler?brevId=%221%22");

    //verifiserer innhold før / etter lås
    cy.contains("Fortsett redigering").should("be.visible");
    cy.contains("Lås for redigering").click();
    cy.contains("Fortsett redigering").should("not.exist");
    cy.contains("Lokalprint").click();

    //---- ferdigstiller brevet
    //tanstack knappen hovrer over ferdigstill knappen - vå i klikker på vestre side av knappen som er synlig. Se om vi kan fikse dette
    cy.contains("Ferdigstill brev").click("left");
    cy.contains("Vil du ferdigstille, og sende disse brevene?").should("be.visible");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="1"]`).should("be.checked");

    cy.contains("Ja, send valgte brev").click();

    //verifisering av kvittering
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/kvittering");
    cy.contains("Sendt til lokalprint").should("be.visible");
    cy.contains("INFORMASJON_OM_SAKSBEHANDLINGSTID").click();
    cy.contains("Distribueres via").should("be.visible");
    cy.contains("Lokal print").should("be.visible");
    cy.contains("Journalpost ID").should("be.visible");
    cy.contains("80912").should("be.visible");
    cy.contains("Åpne brevet i ny fane").should("be.visible");
  });
  it("kan sende flere ferdigstilte brev samtidig", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (request) => {
      request.reply({ journalpostId: 80_912, error: null });
    });
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/2/pdf/send", (request) => {
      request.reply({ journalpostId: 80_913, error: null });
    });
    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/2/pdf", (request) => {
      request.reply(new Blob(["%PDF-1.4\n%..."], { type: "application/pdf" }));
    });

    const klarBrev = {
      id: 1,
      opprettetAv: "Ola Nordmann",
      opprettet: "2021-09-01T12:00:00",
      sistredigertAv: "Ola Nordmann",
      sistredigert: "2021-09-01T12:00:00",
      brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
      distribusjonstype: "SENTRALPRINT",
      status: { type: "Klar" },
    };
    const lokalprintBrev = {
      ...klarBrev,
      brevkode: "BREV_SOM_SENDES_SOM_LOKALPRINT",
      id: 2,
      distribusjonstype: "LOKALPRINT",
      status: { type: "Klar" },
    };

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([klarBrev, lokalprintBrev]);
    });

    cy.contains("Send ferdigstilte brev").click("left");
    cy.contains("Vil du ferdigstille, og sende disse brevene?").should("be.visible");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="1"]`).should("be.checked");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="2"]`).should("be.checked");
    cy.contains("Ja, send valgte brev").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/kvittering");

    cy.contains("Sendt til mottaker").should("be.visible");
    cy.contains("INFORMASJON_OM_SAKSBEHANDLINGSTID").click();
    cy.contains("Distribueres via").should("be.visible");
    cy.contains("Sentral print").should("be.visible");
    cy.contains("Journalpost ID").should("be.visible");
    cy.contains("80912").should("be.visible");
    cy.contains("INFORMASJON_OM_SAKSBEHANDLINGSTID").click();

    cy.contains("Sendt til lokalprint").should("be.visible");
    cy.contains("BREV_SOM_SENDES_SOM_LOKALPRINT").click();

    cy.get(".css-16pei6b-Oppsummeringspar").eq(2).contains("Distribueres via").should("be.visible");
    cy.contains("Lokal print").should("be.visible");
    cy.get(".css-16pei6b-Oppsummeringspar").eq(3).contains("Journalpost ID").should("be.visible");
    cy.contains("80913").should("be.visible");
    cy.contains("Åpne brevet i ny fane").should("be.visible");
  });
  it("velger hvilke brev som skal sendes", () => {
    cy.intercept("POST", "/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (request) => {
      request.reply({ journalpostId: 80_912, error: null });
    });

    const klarBrev = {
      id: 1,
      opprettetAv: "Ola Nordmann",
      opprettet: "2021-09-01T12:00:00",
      sistredigertAv: "Ola Nordmann",
      sistredigert: "2021-09-01T12:00:00",
      brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
      distribusjonstype: "SENTRALPRINT",
      status: { type: "Klar" },
    };
    const lokalprintBrev = {
      ...klarBrev,
      brevkode: "BREV_SOM_SENDES_SOM_LOKALPRINT",
      id: 2,
      distribusjonstype: "LOKALPRINT",
      status: { type: "Klar" },
    };

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([klarBrev, lokalprintBrev]);
    });

    cy.contains("Send ferdigstilte brev").click("left");
    cy.contains("Vil du ferdigstille, og sende disse brevene?").should("be.visible");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="1"]`).should("be.checked");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="2"]`).click();
    cy.contains("Ja, send valgte brev").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/kvittering");

    cy.contains("Sendt til mottaker").should("be.visible");
    cy.contains("INFORMASJON_OM_SAKSBEHANDLINGSTID").click();
    cy.contains("Distribueres via").should("be.visible");
    cy.contains("Sentral print").should("be.visible");
    cy.contains("Journalpost ID").should("be.visible");
    cy.contains("80912").should("be.visible");

    cy.contains("Sendt til lokalprint").should("not.exist");
    cy.contains("BREV_SOM_SENDES_SOM_LOKALPRINT").should("not.exist");
  });

  it("viser pdf når er brev er valgt", () => {
    const kladdBrev = {
      id: 1,
      opprettetAv: "Ola Nordmann",
      opprettet: "2021-09-01T12:00:00",
      sistredigertAv: "Ola Nordmann",
      sistredigert: "2021-09-01T12:00:00",
      brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
      status: { type: "Kladd" },
      distribusjonstype: "SENTRALPRINT",
    };

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/pdf", (request) => {
      const pdfContent = `
%PDF-1.1
%¥±ë

1 0 obj
  << /Type /Catalog
     /Pages 2 0 R
  >>
endobj

2 0 obj
  << /Type /Pages
     /Kids [3 0 R]
     /Count 1
     /MediaBox [0 0 300 144]
  >>
endobj

3 0 obj
  <<  /Type /Page
      /Parent 2 0 R
      /Resources
       << /Font
           << /F1
               << /Type /Font
                  /Subtype /Type1
                  /BaseFont /Times-Roman
               >>
           >>
       >>
      /Contents 4 0 R
  >>
endobj

4 0 obj
  << /Length 55 >>
stream
  BT
    /F1 18 Tf
    0 0 Td
    (Hello World) Tj
  ET
endstream
endobj

xref
0 5
0000000000 65535 f 
0000000018 00000 n 
0000000077 00000 n 
0000000178 00000 n 
0000000457 00000 n 
trailer
  <<  /Root 1 0 R
      /Size 5
  >>
startxref
565
%%EOF`;

      request.reply(pdfContent);
    });

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([kladdBrev]);
    });

    //åpner brevet
    cy.contains("INFORMASJON_OM_SAKSBEHANDLINGSTID").click();
    //brev med id 1
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler?brevId=%221%22");

    cy.contains("Hello World").should("be.visible");
  });
  it("sletter et brev", () => {
    const kladdBrev = {
      id: 1,
      opprettetAv: "Ola Nordmann",
      opprettet: "2021-09-01T12:00:00",
      sistredigertAv: "Ola Nordmann",
      sistredigert: "2021-09-01T12:00:00",
      brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
      status: { type: "Kladd" },
      distribusjonstype: "SENTRALPRINT",
    };

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([kladdBrev]);
    });

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev/1/pdf", (request) => {
      const pdfContent = `
  %PDF-1.1
  %¥±ë
  
  1 0 obj
    << /Type /Catalog
       /Pages 2 0 R
    >>
  endobj
  
  2 0 obj
    << /Type /Pages
       /Kids [3 0 R]
       /Count 1
       /MediaBox [0 0 300 144]
    >>
  endobj
  
  3 0 obj
    <<  /Type /Page
        /Parent 2 0 R
        /Resources
         << /Font
             << /F1
                 << /Type /Font
                    /Subtype /Type1
                    /BaseFont /Times-Roman
                 >>
             >>
         >>
        /Contents 4 0 R
    >>
  endobj
  
  4 0 obj
    << /Length 55 >>
  stream
    BT
      /F1 18 Tf
      0 0 Td
      (Hello World) Tj
    ET
  endstream
  endobj
  
  xref
  0 5
  0000000000 65535 f 
  0000000018 00000 n 
  0000000077 00000 n 
  0000000178 00000 n 
  0000000457 00000 n 
  trailer
    <<  /Root 1 0 R
        /Size 5
    >>
  startxref
  565
  %%EOF`;

      request.reply(pdfContent);
    });

    cy.intercept("DELETE", "/bff/skribenten-backend/sak/123456/brev/1");
    cy.contains("INFORMASJON_OM_SAKSBEHANDLINGSTID").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/brevbehandler?brevId=%221%22");
    cy.contains("Slett").click();
    cy.contains("Vil du slette brevet?").should("be.visible");
    cy.contains("Ja, slett brevet").click();
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

    const klarBrev = {
      id: 1,
      opprettetAv: "Ola Nordmann",
      opprettet: "2021-09-01T12:00:00",
      sistredigertAv: "Ola Nordmann",
      sistredigert: "2021-09-01T12:00:00",
      brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
      distribusjonstype: "SENTRALPRINT",
      status: { type: "Klar" },
    };

    cy.intercept("GET", "/bff/skribenten-backend/sak/123456/brev", (request) => {
      request.reply([klarBrev]);
    });

    cy.contains("Send ferdigstilte brev").click("left");
    cy.contains("Vil du ferdigstille, og sende disse brevene?").should("be.visible");
    cy.get(`[data-cy="ferdigstillbrev-valgte-brev"] input[type="checkbox"][value="1"]`).should("be.checked");
    cy.contains("Ja, send valgte brev").click();
    cy.url().should("eq", "http://localhost:5173/saksnummer/123456/kvittering");

    cy.contains("Brev ble ikke sendt").should("be.visible");
    cy.contains("INFORMASJON_OM_SAKSBEHANDLINGSTID").click();

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
});
