import { readFileSync } from "node:fs";

import { expect, type Page, test } from "@playwright/test";

import { type BrevInfo, Distribusjonstype } from "~/types/brev";

import { nyBrevInfo } from "../utils/brevredigeringTestUtils";
import { setupSakStubs } from "./utils/helpers";

test.describe("Brevbehandler", () => {
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

  const openBrevCard = async (page: Page, title: string) => {
    const card = page.locator(`[aria-label="${title}"]`).first();
    await expect(card).toBeVisible();
    await card.getByRole("button").click();
  };

  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);
  });

  test("saken inneholder ingen brev", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [] });
      }
      return route.fallback();
    });
    await page.goto("/saksnummer/123456/brevbehandler");
    await expect(page.getByText("Fant ingen brev som er under behandling")).toBeVisible();
  });

  test("kan ferdigstille og sende brev med sentralprint", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (route) => {
      if (route.request().method() === "POST") {
        return route.fulfill({ json: { journalpostId: 80_912, error: null } });
      }
      return route.fallback();
    });

    let requestNo = 0;
    const brevResponse = [kladdBrev, klarBrev];
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [brevResponse[requestNo++]] });
      }
      return route.fallback();
    });

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/status", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        expect(body).toEqual(expect.objectContaining({ klar: true }));
        return route.fulfill({ json: klarBrev });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevbehandler");

    // åpner brevet
    await openBrevCard(page, kladdBrev.brevtittel);
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler\?brevId=1/);

    // verifiserer innhold før / etter lås
    await expect(page.getByText("Fortsett redigering")).toBeVisible();
    await page.getByText("Brevet er klart for sending").click();
    await expect(page.getByText("Fortsett redigering")).not.toBeVisible();
    await expect(
      page.getByTestId("brevbehandler-distribusjonstype").locator('input[type="radio"][value="SENTRALPRINT"]'),
    ).toBeChecked();

    // ---- ferdigstiller brevet
    await page.getByText("Send 1 brev").click();
    await expect(page.getByText("Vil du ferdigstille, og sende disse brevene?")).toBeVisible();
    await expect(
      page.getByTestId("ferdigstillbrev-valgte-brev").locator('input[type="checkbox"][value="1"]'),
    ).toBeChecked();
    await page.getByText("Ja, send valgte brev").click();

    // verifisering av kvittering
    await expect(page).toHaveURL(/\/saksnummer\/123456\/kvittering$/);
    await expect(page.getByText("Sendt til mottaker")).toBeVisible();
    await openBrevCard(page, kladdBrev.brevtittel);
    await expect(page.getByText("Distribusjon")).toBeVisible();
    await expect(page.getByText("Sentral print")).toBeVisible();
    await expect(page.getByText("Journalpost")).toBeVisible();
    await expect(page.getByText("80912")).toBeVisible();
  });

  test("kan ferdigstille og sende brev med lokalprint", async ({ page }) => {
    let hentBrevRequestNr = 0;
    const lokalprintBrev = {
      ...kladdBrev,
      distribusjonstype: "LOKALPRINT",
      status: { type: "Klar" },
    };
    const brevResponse = [kladdBrev, lokalprintBrev];

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (route) => {
      if (route.request().method() === "POST") {
        return route.fulfill({ json: { journalpostId: 80_912, error: null } });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/pdf/80912", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({
          path: "test/e2e/fixtures/helloWorldPdf.txt",
          contentType: "application/pdf",
        });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [brevResponse[hentBrevRequestNr++]] });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/status", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        expect(body).toEqual(expect.objectContaining({ klar: true }));
        return route.fulfill({ json: klarBrev });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/distribusjon", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        expect(body).toEqual(expect.objectContaining({ distribusjon: "LOKALPRINT" }));
        return route.fulfill({ json: lokalprintBrev });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevbehandler");

    // åpner brevet
    await openBrevCard(page, kladdBrev.brevtittel);
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler\?brevId=1/);

    // verifiserer innhold før / etter lås
    await expect(page.getByText("Fortsett redigering")).toBeVisible();
    await page.getByText("Brevet er klart for sending").click();
    await expect(page.getByText("Fortsett redigering")).not.toBeVisible();
    await page.getByText("Lokalprint").click();

    // ---- ferdigstiller brevet
    await page.getByText("Send 1 brev").click();
    await expect(page.getByText("Vil du ferdigstille, og sende disse brevene?")).toBeVisible();
    await expect(
      page.getByTestId("ferdigstillbrev-valgte-brev").locator('input[type="checkbox"][value="1"]'),
    ).toBeChecked();

    await page.getByText("Ja, send valgte brev").click();

    // verifisering av kvittering
    await expect(page).toHaveURL(/\/saksnummer\/123456\/kvittering/);
    await expect(page.getByText("Lokalprint – arkivert")).toBeVisible();
    await expect(page.getByText(kladdBrev.brevtittel)).toBeVisible();
    await expect(page.getByText("Distribusjon")).toBeVisible();
    await expect(page.getByText("Lokal print")).toBeVisible();
    await expect(page.getByText("Journalpost")).toBeVisible();
    await expect(page.getByText("80912")).toBeVisible();
    await expect(page.getByText("Åpne PDF")).toBeVisible();
  });

  test("kan ferdigstille og sende brev med lokalprint selv om henting av pdf feiler", async ({ page }) => {
    let hentBrevRequestNr = 0;
    const lokalprintBrev = {
      ...kladdBrev,
      distribusjonstype: "LOKALPRINT",
      status: { type: "Klar" },
    };
    const brevResponse = [kladdBrev, lokalprintBrev];

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (route) => {
      if (route.request().method() === "POST") {
        return route.fulfill({ json: { journalpostId: 80_912, error: null } });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [brevResponse[hentBrevRequestNr++]] });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/pdf/80912", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({
          status: 500,
          body: "simulerer en feil ved henting av pdf for journalpostId'en",
        });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/status", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        expect(body).toEqual(expect.objectContaining({ klar: true }));
        return route.fulfill({ json: klarBrev });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/distribusjon", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        expect(body).toEqual(expect.objectContaining({ distribusjon: "LOKALPRINT" }));
        return route.fulfill({ json: lokalprintBrev });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevbehandler");

    // åpner brevet
    await openBrevCard(page, kladdBrev.brevtittel);
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler\?brevId=1/);

    // verifiserer innhold før / etter lås
    await expect(page.getByText("Fortsett redigering")).toBeVisible();
    await page.getByText("Brevet er klart for sending").click();
    await expect(page.getByText("Fortsett redigering")).not.toBeVisible();
    await page.getByText("Lokalprint").click();

    // ---- ferdigstiller brevet
    await page.getByText("Send 1 brev").click();
    await expect(page.getByText("Vil du ferdigstille, og sende disse brevene?")).toBeVisible();
    await expect(
      page.getByTestId("ferdigstillbrev-valgte-brev").locator('input[type="checkbox"][value="1"]'),
    ).toBeChecked();

    await page.getByText("Ja, send valgte brev").click();

    // verifisering av kvittering
    await expect(page).toHaveURL(/\/saksnummer\/123456\/kvittering$/);
    await expect(page.getByText("Lokalprint – arkivert")).toBeVisible();
    await expect(page.getByText(kladdBrev.brevtittel)).toBeVisible();
    await expect(page.getByText("Distribusjon")).toBeVisible();
    await expect(page.getByText("Lokal print")).toBeVisible();
    await expect(page.getByText("Journalpost")).toBeVisible();
    await expect(page.getByText("80912")).toBeVisible();
    await expect(page.getByText("Åpne PDF")).toBeVisible();
  });

  test("kan sende flere ferdigstilte brev samtidig", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (route) => {
      if (route.request().method() === "POST") {
        return route.fulfill({ json: { journalpostId: 80_912, error: null } });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev/2/pdf/send", (route) => {
      if (route.request().method() === "POST") {
        return route.fulfill({ json: { journalpostId: 80_913, error: null } });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/pdf/80913", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({
          path: "test/e2e/fixtures/helloWorldPdf.txt",
          contentType: "application/pdf",
        });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [klarBrev, brevSomSendesSomLokalPrint] });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevbehandler");

    await page.getByText("Send 2 ferdigstilte brev").click();
    await expect(page.getByText("Vil du ferdigstille, og sende disse brevene?")).toBeVisible();
    await expect(
      page.getByTestId("ferdigstillbrev-valgte-brev").locator('input[type="checkbox"][value="1"]'),
    ).toBeChecked();
    await expect(
      page.getByTestId("ferdigstillbrev-valgte-brev").locator('input[type="checkbox"][value="2"]'),
    ).toBeChecked();
    await page.getByText("Ja, send valgte brev").click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/kvittering$/);

    await expect(page.getByText("Sendt til mottaker")).toBeVisible();
    await openBrevCard(page, kladdBrev.brevtittel);
    await expect(page.getByTestId("journalpostId-80912").getByText("Distribusjon")).toBeVisible();
    await expect(page.getByTestId("journalpostId-80912").getByText("Sentral print")).toBeVisible();
    await expect(page.getByTestId("journalpostId-80912").getByText("Journalpost")).toBeVisible();
    await expect(page.getByTestId("journalpostId-80912").getByText("80912")).toBeVisible();
    await openBrevCard(page, kladdBrev.brevtittel);

    await expect(page.getByText("Lokalprint – arkivert")).toBeVisible();
    await expect(page.getByText(brevSomSendesSomLokalPrint.brevtittel)).toBeVisible();
    await expect(page.getByTestId("journalpostId-80913").getByText("Distribusjon")).toBeVisible();
    await expect(page.getByTestId("journalpostId-80913").getByText("Lokal print")).toBeVisible();
    await expect(page.getByTestId("journalpostId-80913").getByText("Journalpost")).toBeVisible();
    await expect(page.getByTestId("journalpostId-80913").getByText("80913")).toBeVisible();
    await expect(page.getByText("Åpne PDF")).toBeVisible();
  });

  test("velger hvilke brev som skal sendes", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (route) => {
      if (route.request().method() === "POST") {
        return route.fulfill({ json: { journalpostId: 80_912, error: null } });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [klarBrev, brevSomSendesSomLokalPrint] });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevbehandler");

    await page.getByText("Send 2 ferdigstilte brev").click();
    await expect(page.getByText("Vil du ferdigstille, og sende disse brevene?")).toBeVisible();
    await expect(
      page.getByTestId("ferdigstillbrev-valgte-brev").locator('input[type="checkbox"][value="1"]'),
    ).toBeChecked();
    await page.getByTestId("ferdigstillbrev-valgte-brev").locator('input[type="checkbox"][value="2"]').click();
    await page.getByText("Ja, send valgte brev").click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/kvittering$/);

    await expect(page.getByText("Sendt til mottaker")).toBeVisible();
    await openBrevCard(page, kladdBrev.brevtittel);
    await expect(page.getByText("Distribusjon")).toBeVisible();
    await expect(page.getByText("Sentral print")).toBeVisible();
    await expect(page.getByText("Journalpost")).toBeVisible();
    await expect(page.getByText("80912")).toBeVisible();

    await expect(page.getByText("Sendt til lokalprint")).not.toBeVisible();
    await expect(page.getByText(brevSomSendesSomLokalPrint.brevtittel)).not.toBeVisible();
  });

  test("viser pdf når er brev er valgt", async ({ page }) => {
    const pdfBase64 = readFileSync("test/e2e/fixtures/helloWorldPdf.txt", "base64");
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/pdf", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({
          json: { pdf: pdfBase64, rendretBrevErEndret: false },
        });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [kladdBrev] });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevbehandler");

    await openBrevCard(page, kladdBrev.brevtittel);
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler\?brevId=1/);
    await expect(page.getByText("Hello World")).toBeVisible();
  });

  test("sletter et brev", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [kladdBrev] });
      }
      return route.fallback();
    });
    const pdfBase64 = readFileSync("test/e2e/fixtures/helloWorldPdf.txt", "base64");
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/pdf", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({
          json: { pdf: pdfBase64, rendretBrevErEndret: false },
        });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: kladdBrev });
      }
      if (route.request().method() === "DELETE") {
        return route.fulfill({ status: 204 });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevbehandler");

    await openBrevCard(page, kladdBrev.brevtittel);
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler\?brevId=1/);
    await page.getByRole("button", { name: /Slett/ }).click();
    await expect(page.getByText("Vil du slette brevet?")).toBeVisible();
    const slettResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/sak/123456/brev/1") && r.request().method() === "DELETE",
    );
    await page.getByText("Ja, slett brevet").click();
    const slettResponse = await slettResponsePromise;
    expect(slettResponse.status()).toBe(204);

    await page.getByRole("button", { name: "Gå til brevbehandler" }).click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler$/);
  });

  test("får error i responsen for ferdigstill & prøver igjen", async ({ page }) => {
    let requestNo = 0;
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (route) => {
      if (route.request().method() === "POST") {
        if (requestNo === 0) {
          requestNo++;
          return route.fulfill({
            json: {
              journalpostId: null,
              error: {
                brevIkkeStoettet: "Dette brevet er ikke støttet",
                tekniskgrunn: "Her er det en teknisk grunn",
                beskrivelse:
                  "Her kommer det også en veldig god, og begrunnende beskrivelse. Denne skal kanskje være litt lenger, siden den forklarer mer om hva som har skjedd?",
              },
            },
          });
        } else {
          return route.fulfill({ json: { journalpostId: 80_912, error: null } });
        }
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [klarBrev] });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevbehandler");

    await page.getByText("Send 1 ferdigstilt brev").click();
    await expect(page.getByText("Vil du ferdigstille, og sende disse brevene?")).toBeVisible();
    await expect(
      page.getByTestId("ferdigstillbrev-valgte-brev").locator('input[type="checkbox"][value="1"]'),
    ).toBeChecked();
    await page.getByText("Ja, send valgte brev").click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/kvittering$/);

    await expect(page.getByText("Kunne ikke sende brev")).toBeVisible();
    await expect(page.getByText(kladdBrev.brevtittel)).toBeVisible();

    await expect(page.getByText("Skribenten klarte ikke å sende brevet.")).toBeVisible();
    await expect(page.getByText("Brevet ligger lagret i brevbehandler til brevet er sendt.")).toBeVisible();
    await expect(page.getByText("Prøv å sende igjen")).toBeVisible();
    await page.getByRole("button", { name: "Prøv å sende igjen" }).click();

    await expect(page.getByText("Sendt til mottaker")).toBeVisible();
    await expect(page.getByText("Distribusjon")).toBeVisible();
    await expect(page.getByText("Sentral print")).toBeVisible();
    await expect(page.getByText("Journalpost")).toBeVisible();
    await expect(page.getByText("80912")).toBeVisible();
  });

  test("brev som blir sendt blir fjernet fra brevbehandler", async ({ page }) => {
    const brevSomIkkeSkalSendes = { ...klarBrev, id: 2 };

    await page.route("**/bff/skribenten-backend/sak/123456/brev/2/pdf/send", (route) => {
      if (route.request().method() === "POST") {
        return route.fulfill({ json: { journalpostId: 80_912, error: null } });
      }
      return route.fallback();
    });
    let requestNo = 0;
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        if (requestNo === 0 || requestNo === 1) {
          requestNo++;
          return route.fulfill({ json: [klarBrev, brevSomIkkeSkalSendes] });
        } else {
          return route.fulfill({ json: [brevSomIkkeSkalSendes] });
        }
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevbehandler");

    await page.getByText("Send 2 ferdigstilte brev").click();
    await page.getByTestId("ferdigstillbrev-valgte-brev").locator('input[type="checkbox"][value="1"]').click();
    await page.getByText("Ja, send valgte brev").click();
    await page.getByText("Gå til brevbehandler").click();
    await expect(page.locator("[aria-label='Informasjon om saksbehandlingstid']")).toHaveCount(1);
  });

  test("et arkivert brev kan ikke endre på noe informasjon, og kan kun sendes på nytt", async ({ page }) => {
    const arkivertBrev: BrevInfo = {
      ...klarBrev,
      status: { type: "Arkivert" },
      journalpostId: 123_456,
    };

    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [arkivertBrev] });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevbehandler");

    await openBrevCard(page, "Informasjon om saksbehandlingstid");

    await expect(page.getByText("Brevet er klart for sending")).not.toBeVisible();
    await expect(page.getByText("Fortsett redigering")).not.toBeVisible();
    await expect(page.getByText("Distribusjon")).not.toBeVisible();

    await expect(page.getByText("Brevet er journalført med id 123456. Brevet kan ikke endres")).toBeVisible();
    await expect(page.getByText("Brevet har ikke blitt sendt. Du kan prøve å sende brevet på nytt.")).toBeVisible();
    await page.getByText("Send 1 brev").click();
    await page.getByText("Ja, send valgte brev").click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/kvittering$/);
  });

  test("brev som har uendret fritekstfelter kan ikke gjøres klar for sending", async ({ page }) => {
    const nyBrevInfo2 = nyBrevInfo({});

    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [nyBrevInfo2] });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/status", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        expect(body).toEqual({ klar: true });
        return route.fulfill({
          status: 400,
          json: { message: "dette er en feil" },
        });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevbehandler");

    await openBrevCard(page, "Informasjon om saksbehandlingstid");
    await page.getByText("Brevet er klart for sending").click();

    //TODO - Her skal vi egentlig på en faktisk feilmelding om at brevet inneholder uendret fritekst
    await expect(page.getByText("Noe gikk galt")).toBeVisible();
  });
});
