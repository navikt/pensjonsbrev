import { expect, type Page, test } from "@playwright/test";

import { setupSakStubs } from "~test/e2e/support/helpers";

async function expectOrderLetterSuccess(page: Page, expectedUrl: string) {
  const successMessage = page.getByTestId("order-letter-success-message");
  await expect(successMessage).toBeVisible();
  await expect(successMessage.getByRole("link", { name: "Klikk her for å prøve igjen" })).toHaveAttribute(
    "href",
    expectedUrl,
  );
}

async function velgLand(page: Page, søketekst: string) {
  await page.getByTestId("land-combobox").click();
  await page.getByTestId("land-combobox").pressSequentially(søketekst);
  await page.keyboard.press("Enter");
}

/*
Aksel sin single-select combobox har ingen "fjern"-knapp: for å velge bort et land må man
navigere til det allerede valgte alternativet i listen og velge det på nytt, slik at
toggleOption kaller removeSelectedOption. Å skrive inn samme verdi og trykke Enter uten å
flytte fokus ned i listen tømmer bare søkefeltet, og fjerner ikke selve valget.
*/
async function fjernValgtLand(page: Page, landnavn: string) {
  await page.getByTestId("land-combobox").click();
  await page.getByTestId("land-combobox").pressSequentially(landnavn);
  await page.keyboard.press("ArrowDown");
  await page.keyboard.press("Enter");
}

test.describe("Endrer på mottaker", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);

    await page.route("**/bff/skribenten-backend/hentSamhandlerAdresse", async (route) => {
      return route.fulfill({ path: "test/e2e/fixtures/hentSamhandlerAdresse.json", contentType: "application/json" });
    });

    await page.route("**/bff/skribenten-backend/sak/123456/bestillBrev/exstream", async (route) => {
      const body = route.request().postDataJSON();
      expect(body).toEqual({
        brevkode: "PE_IY_05_300",
        idTSSEkstern: "80000781720",
        spraak: "NB",
        brevtittel: "Vedtak om bla bla",
        enhetsId: "4405",
        vedtaksId: null,
      });
      return route.fulfill({ path: "test/e2e/fixtures/bestillBrevExstream.json", contentType: "application/json" });
    });

    await page.route(
      "**/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => {
        return route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" });
      },
    );

    await page.route("**/bff/skribenten-backend/land", (route) => {
      return route.fulfill({ path: "test/e2e/fixtures/land.json", contentType: "application/json" });
    });

    await page.goto("/saksnummer/123456/brevvelger");
  });

  test("søk med direkte oppslag", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/finnSamhandler", async (route) => {
      const body = route.request().postDataJSON();
      expect(body).toEqual({
        samhandlerType: "ADVO",
        identtype: "ORG",
        id: "direkte-oppslag-id",
        type: "DIREKTE_OPPSLAG",
      });
      return route.fulfill({ path: "test/e2e/fixtures/finnSamhandler.json", contentType: "application/json" });
    });

    await page.route("**/bff/skribenten-backend/sak/123456/bestillBrev/exstream", async (route) => {
      const body = route.request().postDataJSON();
      expect(body).toEqual({
        brevkode: "PE_IY_05_300",
        idTSSEkstern: "80000781720",
        spraak: "NB",
        brevtittel: "Vedtak om bla bla",
        enhetsId: "4405",
        vedtaksId: null,
      });
      return route.fulfill({ path: "test/e2e/fixtures/bestillBrevExstream.json", contentType: "application/json" });
    });

    // Search and select the letter
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("brev fra nav");
    await page.getByTestId("brevmal-button").click();

    // Open change recipient modal, verify search button not visible
    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await expect(page.getByTestId("endre-mottaker-søk-button")).not.toBeVisible();

    // Select direkte oppslag and fill form
    await page.getByTestId("endre-mottaker-søketype-select").selectOption("Direkte oppslag");
    await expect(page.getByTestId("endre-mottaker-søk-button")).toBeVisible();
    await page.getByLabel("Samhandlertype").click();
    await page.locator(":focus").pressSequentially("adv");
    await page.keyboard.press("Enter");
    await page.getByTestId("endre-mottaker-identtype-select").selectOption("Norsk orgnr");
    await page.getByLabel("ID", { exact: true }).click();
    await page.locator(":focus").pressSequentially("direkte-oppslag-id");
    await page.getByTestId("endre-mottaker-søk-button").click();

    // Select first samhandler by clicking the name (selects and expands the row)
    await page.getByTestId("endre-mottaker-modal").getByText("Advokat 1 As").first().click();

    // Assert address details shown in expanded row
    await expect(page.getByText("Postboks 603 Sentrum")).toBeVisible();
    await expect(page.getByText("4003")).toBeVisible();
    await expect(page.getByText("Stavanger")).toBeVisible();
    await expect(page.getByRole("cell", { name: "Nor", exact: true })).toBeVisible();
    await page.getByTestId("lagre-samhandler").click();

    // Assert we have switched to samhandler
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevvelger/);
    expect(page.url()).toContain("templateId=PE_IY_05_300");
    expect(page.url()).toContain("idTSSEkstern=%2280000781720%22");

    await page.getByTestId("avsenderenhet-select").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });
    await page.getByTestId("brev-title-textfield").click();
    await page.getByTestId("brev-title-textfield").pressSequentially("Vedtak om bla bla");
    await expect(page.getByTestId("språk-velger-select")).toHaveValue("NB");

    // Order letter
    await page.getByTestId("order-letter").click();
    await expectOrderLetterSuccess(
      page,
      "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
  });

  test("søk med organisasjonsnavn", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/finnSamhandler", async (route) => {
      const body = route.request().postDataJSON();
      expect(body).toEqual({
        samhandlerType: "ADVO",
        innlandUtland: "ALLE",
        navn: "navnet på samhandler",
        type: "ORGANISASJONSNAVN",
      });
      return route.fulfill({ path: "test/e2e/fixtures/finnSamhandler.json", contentType: "application/json" });
    });

    // Search and select the letter
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("brev fra nav");
    await page.getByTestId("brevmal-button").click();

    // Open change recipient modal, verify search button not visible
    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await expect(page.getByTestId("endre-mottaker-søk-button")).not.toBeVisible();

    // Select organisasjonsnavn and fill form
    await page.getByTestId("endre-mottaker-søketype-select").selectOption("Organisasjonsnavn");
    await expect(page.getByTestId("endre-mottaker-søk-button")).toBeVisible();
    await expect(page.getByTestId("endre-mottaker-organisasjonsnavn-innOgUtland")).toHaveValue("ALLE");
    await page.getByLabel("Samhandlertype").click();
    await page.locator(":focus").pressSequentially("adv");
    await page.keyboard.press("Enter");
    await page.getByLabel("Navn", { exact: true }).click();
    await page.locator(":focus").pressSequentially("navnet på samhandler");

    await page.getByTestId("endre-mottaker-søk-button").click();

    // Select first samhandler by clicking the name (selects and expands the row)
    await page.getByTestId("endre-mottaker-modal").getByText("Advokat 1 As").first().click();

    // Assert address details shown in expanded row
    await expect(page.getByText("Postboks 603 Sentrum")).toBeVisible();
    await expect(page.getByText("4003")).toBeVisible();
    await expect(page.getByText("Stavanger")).toBeVisible();
    await expect(page.getByRole("cell", { name: "Nor", exact: true })).toBeVisible();
    await page.getByTestId("lagre-samhandler").click();

    // Assert we have switched to samhandler
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevvelger/);
    expect(page.url()).toContain("templateId=PE_IY_05_300");
    expect(page.url()).toContain("idTSSEkstern=%2280000781720%22");

    await page.getByTestId("avsenderenhet-select").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });
    await page.getByTestId("brev-title-textfield").click();
    await page.getByTestId("brev-title-textfield").pressSequentially("Vedtak om bla bla");
    await expect(page.getByTestId("språk-velger-select")).toHaveValue("NB");

    // Order letter
    await page.getByTestId("order-letter").click();
    await expectOrderLetterSuccess(
      page,
      "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
  });

  test("søk med personnavn", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/finnSamhandler", async (route) => {
      const body = route.request().postDataJSON();
      expect(body).toEqual({
        samhandlerType: "ADVO",
        fornavn: "Fornavnet",
        etternavn: "Etternavnet",
        type: "PERSONNAVN",
      });
      return route.fulfill({ path: "test/e2e/fixtures/finnSamhandler.json", contentType: "application/json" });
    });

    // Search and select the letter
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("brev fra nav");
    await page.getByTestId("brevmal-button").click();

    // Open change recipient modal, verify search button not visible
    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await expect(page.getByTestId("endre-mottaker-søk-button")).not.toBeVisible();

    // Select personnavn and fill form
    await page.getByTestId("endre-mottaker-søketype-select").selectOption("Personnavn");
    await expect(page.getByTestId("endre-mottaker-søk-button")).toBeVisible();
    await page.getByLabel("Samhandlertype").click();
    await page.locator(":focus").pressSequentially("adv");
    await page.keyboard.press("Enter");
    await page.getByLabel("Fornavn").click();
    await page.locator(":focus").pressSequentially("Fornavnet");
    await page.getByLabel("Etternavn").click();
    await page.locator(":focus").pressSequentially("Etternavnet");

    await page.getByTestId("endre-mottaker-søk-button").click();

    // Select first samhandler by clicking the name (selects and expands the row)
    await page.getByTestId("endre-mottaker-modal").getByText("Advokat 1 As").first().click();

    // Assert address details shown in expanded row
    await expect(page.getByText("Postboks 603 Sentrum")).toBeVisible();
    await expect(page.getByText("4003")).toBeVisible();
    await expect(page.getByText("Stavanger")).toBeVisible();
    await expect(page.getByRole("cell", { name: "Nor", exact: true })).toBeVisible();
    await page.getByTestId("lagre-samhandler").click();

    // Assert we have switched to samhandler
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevvelger/);
    expect(page.url()).toContain("templateId=PE_IY_05_300");
    expect(page.url()).toContain("idTSSEkstern=%2280000781720%22");

    await page.getByTestId("avsenderenhet-select").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });
    await page.getByTestId("brev-title-textfield").click();
    await page.getByTestId("brev-title-textfield").pressSequentially("Vedtak om bla bla");
    await expect(page.getByTestId("språk-velger-select")).toHaveValue("NB");

    // Order letter
    await page.getByTestId("order-letter").click();
    await expectOrderLetterSuccess(
      page,
      "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    );
  });

  test("viser valideringsfeil for direkte oppslag med tomme felter", async ({ page }) => {
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("brev fra nav");
    await page.getByTestId("brevmal-button").click();
    await page.getByTestId("toggle-endre-mottaker-modal").click();

    await page.getByTestId("endre-mottaker-søketype-select").selectOption("Direkte oppslag");
    await page.getByTestId("endre-mottaker-søk-button").click();

    // samhandlertype, identtype og id er påkrevd
    await expect(page.getByTestId("endre-mottaker-modal").locator(".aksel-error-message")).toHaveCount(3, {
      timeout: 5000,
    });
    await expect(page.getByTestId("endre-mottaker-modal").getByText("Feltet må fylles ut").first()).toBeVisible();
  });

  test("viser valideringsfeil for organisasjonsnavn med tomme felter", async ({ page }) => {
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("brev fra nav");
    await page.getByTestId("brevmal-button").click();
    await page.getByTestId("toggle-endre-mottaker-modal").click();

    await page.getByTestId("endre-mottaker-søketype-select").selectOption("Organisasjonsnavn");
    await page.getByTestId("endre-mottaker-søk-button").click();

    // samhandlertype og navn er påkrevd
    await expect(page.getByTestId("endre-mottaker-modal").locator(".aksel-error-message").first()).toBeVisible();
    await expect(page.getByTestId("endre-mottaker-modal").getByText("Feltet må fylles ut").first()).toBeVisible();
  });

  test("viser valideringsfeil for personnavn med tomme felter", async ({ page }) => {
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("brev fra nav");
    await page.getByTestId("brevmal-button").click();
    await page.getByTestId("toggle-endre-mottaker-modal").click();

    await page.getByTestId("endre-mottaker-søketype-select").selectOption("Personnavn");
    await page.getByTestId("endre-mottaker-søk-button").click();

    // samhandlertype, fornavn og etternavn er påkrevd
    await expect(page.getByTestId("endre-mottaker-modal").locator(".aksel-error-message")).toHaveCount(3, {
      timeout: 5000,
    });
    await expect(page.getByTestId("endre-mottaker-modal").getByText("Feltet må fylles ut").first()).toBeVisible();
  });

  test("land må velges før resten av adressen kan fylles ut", async ({ page }) => {
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("Informasjon om saksbehandlingstid");
    await page.getByText("Informasjon om saksbehandlingstid").click();
    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await page.getByText("Legg til manuelt").click();

    // Uten valgt land er alle adressefeltene låst
    await expect(page.getByText("Du må velge land før resten kan fylles ut.")).toBeVisible();
    for (const felt of ["Navn", "Adresselinje 1", "Adresselinje 2", "Adresselinje 3", "Postnummer", "Poststed"]) {
      await expect(page.getByLabel(felt, { exact: true })).toHaveAttribute("readonly", "");
    }

    // Lagring blokkeres, og vi klager kun på land - ikke på feltene saksbehandler ikke fikk fylle ut
    await page.getByTestId("endre-mottaker-modal").getByText("Lagre og lukk").click();
    await expect(page.getByTestId("endre-mottaker-modal").getByText("Du må velge land", { exact: true })).toBeVisible();
    await expect(page.getByTestId("endre-mottaker-modal").getByText("Obligatorisk")).not.toBeVisible();

    // Etter at land er valgt låses feltene opp
    await velgLand(page, "Norge");
    for (const felt of ["Navn", "Adresselinje 1", "Adresselinje 2", "Adresselinje 3", "Postnummer", "Poststed"]) {
      await expect(page.getByLabel(felt, { exact: true })).not.toHaveAttribute("readonly", "");
    }
  });

  test("Norge ligger øverst i landlisten, resten er alfabetisk", async ({ page }) => {
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("Informasjon om saksbehandlingstid");
    await page.getByText("Informasjon om saksbehandlingstid").click();
    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await page.getByText("Legg til manuelt").click();

    await page.getByTestId("land-combobox").click();

    // Alfabetisk ville rekkefølgen vært Danmark, Norge, Sverige - Norge løftes bevisst til toppen.
    // Merk: må scopes til combobox-listen, ellers plukker role=option også opp <option> i vanlige select-er på siden.
    await expect(page.locator(".aksel-combobox__list-options").getByRole("option")).toHaveText([
      "Norge",
      "Danmark",
      "Sverige",
    ]);
  });

  test("fjerning av land låser feltene igjen og tømmer det som er fylt ut", async ({ page }) => {
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("Informasjon om saksbehandlingstid");
    await page.getByText("Informasjon om saksbehandlingstid").click();
    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await page.getByText("Legg til manuelt").click();

    await velgLand(page, "Norge");
    await page.getByLabel("Navn", { exact: true }).click();
    await page.locator(":focus").pressSequentially("Test Testesen");

    // Fjern landet igjen
    await fjernValgtLand(page, "Norge");

    await expect(page.getByLabel("Navn", { exact: true })).toHaveValue("");
    await expect(page.getByLabel("Navn", { exact: true })).toHaveAttribute("readonly", "");
  });

  test("viser valideringsfeil for manuell adresse med norsk adresse", async ({ page }) => {
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("Informasjon om saksbehandlingstid");
    await page.getByText("Informasjon om saksbehandlingstid").click();
    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await page.getByText("Legg til manuelt").click();

    await velgLand(page, "Norge");

    // submit with empty fields — name is mandatory
    await page.getByTestId("endre-mottaker-modal").getByText("Lagre og lukk").click();
    await expect(page.getByTestId("endre-mottaker-modal").getByText("Obligatorisk")).toBeVisible();

    // fill name, set invalid postal code
    await page.getByLabel("Navn", { exact: true }).click();
    await page.locator(":focus").pressSequentially("Test Testesen");
    await page.getByLabel("Postnummer").click();
    await page.locator(":focus").pressSequentially("abc");
    await page.getByLabel("Poststed").click();
    await page.locator(":focus").pressSequentially("Stedet");
    await page.getByTestId("endre-mottaker-modal").getByText("Lagre og lukk").click();
    await expect(page.getByTestId("endre-mottaker-modal").getByText("Postnummer må være 4 siffer")).toBeVisible();
  });

  test("viser valideringsfeil for manuell adresse med utenlandsk adresse", async ({ page }) => {
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("Informasjon om saksbehandlingstid");
    await page.getByText("Informasjon om saksbehandlingstid").click();
    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await page.getByText("Legg til manuelt").click();

    // Switch to foreign address — postal code/city should be hidden, address lines remain
    await velgLand(page, "Sver");
    await expect(page.getByLabel("Postnummer")).not.toBeVisible();
    await expect(page.getByLabel("Poststed")).not.toBeVisible();
    await expect(page.getByLabel("Adresselinje 1")).toBeVisible();
    await expect(page.getByLabel("Adresselinje 2")).toBeVisible();
    await expect(page.getByLabel("Adresselinje 3")).toBeVisible();

    // Fill name but leave address line 1 empty
    await page.getByLabel("Navn", { exact: true }).click();
    await page.locator(":focus").pressSequentially("Test Testesen");
    await page.getByTestId("endre-mottaker-modal").getByText("Lagre og lukk").click();
    await expect(page.getByTestId("endre-mottaker-modal").getByText("Adresselinje 1 må fylles ut")).toBeVisible();
  });

  test("kan legge inn manuell adresse for brevbaker brev", async ({ page }) => {
    // Search and select the letter
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("Informasjon om saksbehandlingstid");
    await page.getByText("Informasjon om saksbehandlingstid").click();
    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await page.getByText("Legg til manuelt").click();
    await velgLand(page, "Norge");
    await page.getByLabel("Navn", { exact: true }).click();
    await page.locator(":focus").pressSequentially("Fornavn Etternavnsen");
    await page.getByLabel("Adresselinje 1").click();
    await page.locator(":focus").pressSequentially("Adresselinjen 1");
    await page.getByLabel("Postnummer").click();
    await page.locator(":focus").pressSequentially("0000");
    await page.getByLabel("Poststed").click();
    await page.locator(":focus").pressSequentially("Poststedet");
    // Bytter til utenlandsk adresse — postnummer og poststed skal ikke bli med videre
    await velgLand(page, "Sver");
    await page.getByTestId("endre-mottaker-modal").getByText("Lagre og lukk").click();

    await expect(page.getByText("Fornavn Etternavnsen")).toBeVisible();
    await expect(page.getByText("Adresselinjen")).toBeVisible();
    await expect(page.locator("body")).not.toContainText("0000");
    await expect(page.locator("body")).not.toContainText("Poststedet");
    await expect(page.getByText("Sverige")).toBeVisible();
    await expect(page.getByText("Tilbakestill")).toBeVisible();
  });

  test("kan ikke legge inn manuell adresse for exstream brev", async ({ page }) => {
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("brev fra nav");
    await page.getByTestId("brevmal-button").click();

    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await expect(page.getByText("Legg til manuelt")).not.toBeVisible();
  });

  test("kan avbryte uten bekreftelse dersom det ikke finnes endringer", async ({ page }) => {
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("brev fra nav");
    await page.getByTestId("brevmal-button").click();
    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await expect(page.getByTestId("endre-mottaker-søk-button")).not.toBeVisible();
    await page.getByText("Avbryt").click();
    await expect(page.getByTestId("endre-mottaker-modal")).not.toBeVisible();
  });

  test("må bekrefte avbrytelse dersom det finnes endringer for manuellAdresse", async ({ page }) => {
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("Informasjon om saksbehandlingstid");
    await page.getByText("Informasjon om saksbehandlingstid").click();
    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await page.getByText("Legg til manuelt").click();
    await velgLand(page, "Norge");
    await page.getByLabel("Navn", { exact: true }).click();
    await page.locator(":focus").pressSequentially("Fornavn Etternavnsen");
    await page.getByText("Avbryt").click();

    // Confirmation dialog appears
    await expect(page.getByText("Vil du avbryte endring av mottaker?")).toBeVisible();
    await expect(
      page.getByText("Infoen du har skrevet inn blir ikke lagret. Du kan ikke angre denne handlingen."),
    ).toBeVisible();
    // Can navigate back to form
    await page.getByText("Nei, ikke avbryt").click();
    // Choose to confirm cancel
    await page.getByText("Avbryt").click();
    await page.getByText("Ja, avbryt").click();
    await expect(page.getByTestId("endre-mottaker-modal")).not.toBeVisible();
  });

  test("ESC-tasten gir bekreftelsesdialog dersom det finnes endringer for manuellAdresse", async ({ page }) => {
    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("Informasjon om saksbehandlingstid");
    await page.getByText("Informasjon om saksbehandlingstid").click();
    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await page.getByText("Legg til manuelt").click();
    await velgLand(page, "Norge");
    await page.getByLabel("Navn", { exact: true }).click();
    await page.locator(":focus").pressSequentially("Fornavn Etternavnsen");

    // Press Escape to trigger cancel
    await page.keyboard.press("Escape");

    // Confirmation dialog appears, same as clicking Avbryt
    await expect(page.getByText("Vil du avbryte endring av mottaker?")).toBeVisible();
    await expect(
      page.getByText("Infoen du har skrevet inn blir ikke lagret. Du kan ikke angre denne handlingen."),
    ).toBeVisible();
    // Can navigate back to form
    await page.getByText("Nei, ikke avbryt").click();
    // Trigger ESC again and confirm
    await page.keyboard.press("Escape");
    await page.getByText("Ja, avbryt").click();
    await expect(page.getByTestId("endre-mottaker-modal")).not.toBeVisible();
  });

  /*
  Avkrysningsboksen «Overstyring av brukers adresse» har motsatt polaritet av enum-verdien den styrer:
  avkrysset betyr at bruker selv er mottaker (BRUKER), uavkrysset at adressen tilhører noen andre (ANNEN).
  Verdien avgjør om brevet får «Mottaker: <navn>» + «Saken gjelder: <bruker>» i sakspart-blokken, så
  disse testene låser koblingen mellom avkrysning og utgående verdi.
  */
  async function bestillBrevMedManuellAdresse(page: Page, krysseAvOverstyring: boolean) {
    let requestBody: Record<string, unknown> | undefined;
    await page.route("**/bff/skribenten-backend/sak/123456/brev", async (route) => {
      if (route.request().method() === "POST") {
        requestBody = route.request().postDataJSON();
        return route.fulfill({ path: "test/e2e/fixtures/brevResponse.json", contentType: "application/json" });
      }
      return route.fallback();
    });

    await page.getByTestId("brevmal-search").click();
    await page.getByTestId("brevmal-search").pressSequentially("Informasjon om saksbehandlingstid");
    await page.getByText("Informasjon om saksbehandlingstid").click();

    await page.getByTestId("toggle-endre-mottaker-modal").click();
    await page.getByText("Legg til manuelt").click();

    const overstyringsboks = page.getByRole("checkbox", { name: "Overstyring av brukers adresse" });
    await expect(overstyringsboks).not.toBeChecked();
    if (krysseAvOverstyring) {
      await overstyringsboks.check();
    }

    await velgLand(page, "Norge");
    await page.getByLabel("Navn", { exact: true }).click();
    await page.locator(":focus").pressSequentially("Fornavn Etternavnsen");
    await page.getByLabel("Adresselinje 1").click();
    await page.locator(":focus").pressSequentially("Adresselinjen 1");
    await page.getByLabel("Postnummer").click();
    await page.locator(":focus").pressSequentially("0000");
    await page.getByLabel("Poststed").click();
    await page.locator(":focus").pressSequentially("Poststedet");
    await page.getByTestId("endre-mottaker-modal").getByText("Lagre og lukk").click();
    await expect(page.getByTestId("endre-mottaker-modal")).not.toBeVisible();

    await page.locator("select[name=enhetsId]").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });
    await page.getByLabel("Mottatt søknad").click();
    await page.locator(":focus").pressSequentially("09.10.2024");
    await page.getByLabel("Ytelse").click();
    await page.locator(":focus").pressSequentially("Alderspensjon");
    await page.getByLabel("Svartid uker").click();
    await page.locator(":focus").pressSequentially("4");
    await page.getByRole("button", { name: "Fortsett" }).click();

    await expect(page).toHaveURL(/\/saksnummer\/123456\/brev\/1/);
    return requestBody;
  }

  test("uavkrysset overstyring sender manueltAdressertTil ANNEN", async ({ page }) => {
    const requestBody = await bestillBrevMedManuellAdresse(page, false);

    expect(requestBody?.mottaker).toEqual({
      type: "NorskAdresse",
      navn: "Fornavn Etternavnsen",
      manueltAdressertTil: "ANNEN",
      adresselinje1: "Adresselinjen 1",
      adresselinje2: "",
      adresselinje3: "",
      postnummer: "0000",
      poststed: "Poststedet",
    });
  });

  test("avkrysset overstyring sender manueltAdressertTil BRUKER", async ({ page }) => {
    const requestBody = await bestillBrevMedManuellAdresse(page, true);

    expect(requestBody?.mottaker).toMatchObject({
      navn: "Fornavn Etternavnsen",
      manueltAdressertTil: "BRUKER",
    });
  });
});
