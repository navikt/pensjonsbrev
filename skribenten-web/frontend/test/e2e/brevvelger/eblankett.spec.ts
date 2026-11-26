import { expect, type Page, test } from "@playwright/test";

import { type InnblikkEventData } from "~/utils/innblikk";
import { setupSakStubs } from "~test/e2e/support/helpers";

import avtaleland from "../fixtures/avtaleland.json" with { type: "json" };

declare global {
  var eblankettEvents: { name: string; data?: InnblikkEventData }[];
}

const orderUrl = "**/bff/skribenten-backend/sak/123456/bestillBrev/exstream/eblankett";

const recipients = {
  almannaverkid: "Almannaverkið\nStaravegur 18\nPostrúm 3096\n110 Tórshavn\nFÆRØYENE",
  "sociale-anliggender":
    "Departementet for Sociale Anliggender, Arbejdsmarked, Familier og Indenrigsanliggender\nSociale Anliggender\nImaneq 1 A\nPostboks 970\n3900 Nuuk\nGRØNLAND",
  socialeomraadet:
    "Departementet for Sociale Anliggender, Arbejdsmarked, Familier og Indenrigsanliggender\nSocialeområdet\nImaneq 1 A\nPostboks 970\n3900 Nuuk\nGRØNLAND",
  "isle-of-man": "The Treasury\nSocial Security Division\nMarkwell House\nMarket Street\nDouglas\nISLE OF MAN",
  jersey:
    "Government of Jersey\nEmployment, Social Security and Housing Department\nUnion Street\nSt Helier\nJERSEY, CHANNEL ISLANDS",
};

const creationEvents = (page: Page) =>
  page.evaluate(() => globalThis.eblankettEvents.filter((event) => event.name === "brev opprettet"));

test.beforeEach(async ({ page }) => {
  await setupSakStubs(page);
  await page.addInitScript(() => {
    globalThis.open = () => null;
    globalThis.eblankettEvents = [];
    globalThis.sporing = {
      identify: () => {},
      track: (name, data) => {
        globalThis.eblankettEvents.push({ name, data });
      },
    };
  });
});

for (const landkode of ["NOR", "GBR"]) {
  test(`E-blankett bestiller og sporer valgt land ${landkode}`, async ({ page }) => {
    let releaseOrder: () => void = () => {};
    const orderReady = new Promise<void>((resolve) => {
      releaseOrder = resolve;
    });
    await page.route(orderUrl, async (route) => {
      await orderReady;
      await route.fulfill({ path: "test/e2e/fixtures/bestillBrevEblankett.json", contentType: "application/json" });
    });

    await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
    const eblankettTag = page.getByText("E-blankett", { exact: true });
    await expect(eblankettTag).toHaveAttribute("data-color", "neutral");
    await expect(eblankettTag.locator("..").locator(".aksel-tag")).toHaveText(["Exstream", "E-blankett"]);
    const country = page.getByRole("combobox", { name: "Land", exact: true });
    await expect(country).toHaveValue("NOR");
    await expect(country.locator("option")).toHaveText([
      "Velg land",
      "Norge",
      "Færøyene",
      "Grønland",
      "Storbritannia",
      ...avtaleland
        .filter((land) => !["NOR", "FRO", "GRL", "GBR"].includes(land.kode))
        .map((land) => land.navn)
        .toSorted((a, b) => a.localeCompare(b, "nb")),
    ]);
    await expect(page.getByRole("combobox", { name: "Avsenderenhet" })).toHaveValue("4815");
    if (landkode !== "NOR") {
      await country.selectOption(landkode);
    }
    const mottakerText = landkode === "NOR" ? "Testmottaker\nPostboks 123\n0123 Oslo" : "Testmottaker";
    await page.getByRole("textbox", { name: "Mottaker" }).fill(mottakerText);
    expect(await creationEvents(page)).toEqual([]);

    const orderRequest = page.waitForRequest(
      (request) => request.url().endsWith("/bestillBrev/exstream/eblankett") && request.method() === "POST",
    );
    await page.getByTestId("order-letter").click();
    expect((await orderRequest).postDataJSON()).toEqual({
      brevkode: "E001",
      landkode,
      mottakerText,
      enhetsId: "4815",
    });
    expect(await creationEvents(page)).toEqual([]);
    releaseOrder();
    await expect(page.getByTestId("order-letter-success-message")).toBeVisible();
    expect(await creationEvents(page)).toEqual([
      {
        name: "brev opprettet",
        data: {
          brevkode: "E001",
          brevtittel: "E 001 - Anmodning om opplysninger",
          brevsystem: "exstream",
          dokumentkategori: "E_BLANKETT",
          landkode,
        },
      },
    ]);
  });
}

test("Mottakeretiketten er knyttet til tekstfeltet i alle land", async ({ page }) => {
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
  const country = page.getByRole("combobox", { name: "Land", exact: true });
  const textarea = page.getByRole("textbox", { name: "Mottaker", exact: true });
  const visibleLabel = page.getByText("Mottaker", { exact: true }).first();
  const recipient = page.getByRole("combobox", { name: "Velg mottaker", exact: true });
  const radios = page.getByRole("radiogroup", { name: "Velg mottaker", exact: true });

  for (const code of ["NOR", "GRL", "GBR", "FRO", "BEL", ""]) {
    await country.selectOption(code);
    await expect(visibleLabel).toHaveAttribute("for", (await textarea.getAttribute("id")) ?? "");
    await expect(textarea).toHaveAccessibleName("Mottaker");
    await visibleLabel.click();
    await expect(textarea).toBeFocused();
    if (code === "NOR") {
      await expect(recipient).toHaveAccessibleName("Velg mottaker");
    } else if (code === "GRL" || code === "GBR") {
      await expect(radios).toHaveAccessibleName("Velg mottaker");
    } else {
      await expect(recipient).not.toBeVisible();
      await expect(radios).not.toBeVisible();
    }
  }
});

test("Mottaker har 8 px avstand etter etiketten og 12 px etter mottakervalg", async ({ page }) => {
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
  const country = page.getByRole("combobox", { name: "Land", exact: true });
  const recipient = page.getByRole("combobox", { name: "Velg mottaker", exact: true });
  const textarea = page.getByRole("textbox", { name: "Mottaker", exact: true });
  const label = page.getByText("Mottaker", { exact: true }).first();

  for (const code of ["NOR", "GRL", "GBR", "FRO", "BEL", "", "NOR"]) {
    await country.selectOption(code);
    const labelBox = await label.boundingBox();
    const textareaBox = await textarea.boundingBox();
    if (!labelBox || !textareaBox) {
      throw new Error("Mottaker-feltet må være synlig");
    }

    if (["NOR", "GRL", "GBR"].includes(code)) {
      const control = code === "NOR" ? recipient : page.getByRole("radiogroup", { name: "Velg mottaker" });
      const recipientBox = await control.boundingBox();
      if (!recipientBox) {
        throw new Error("Mottakerlisten må være synlig");
      }
      expect(recipientBox.y - labelBox.y - labelBox.height).toBe(8);
      expect(textareaBox.y - recipientBox.y - recipientBox.height).toBe(12);
    } else {
      await expect(recipient).not.toBeVisible();
      expect(textareaBox.y - labelBox.y - labelBox.height).toBe(8);
    }
  }
});

test("Norge tilbyr alle mottakere og lar saksbehandler redigere eller tømme adressen", async ({ page }) => {
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
  const country = page.getByRole("combobox", { name: "Land", exact: true });
  const recipient = page.getByRole("combobox", { name: "Velg mottaker", exact: true });
  const textarea = page.getByRole("textbox", { name: "Mottaker", exact: true });
  const counter = page.locator(".aksel-textarea__counter");
  await expect(country).toHaveValue("NOR");
  await expect(recipient).toHaveValue("");
  await expect(textarea).toHaveValue("");
  await expect(counter).toHaveText("200 tegn igjen");
  await expect(recipient.locator("option")).toHaveText([
    "Velg mottaker",
    "Almannaverkið (Færøyene)",
    "Sociale Anliggender (udsatte voksne, ældre) (Grønland)",
    "Socialeområdet (udsatte voksne, ældre, familie) (Grønland)",
    "Social Security Division (Isle of Man)",
    "Employment, Social Sec. & Housing (Jersey)",
  ]);
  await expect(page.getByRole("radiogroup", { name: "Velg mottaker" })).not.toBeVisible();
  await page.getByTestId("order-letter").click();
  await expect(textarea).toHaveAttribute("aria-invalid", "true");

  for (const [id, address] of Object.entries(recipients)) {
    await recipient.selectOption(id);
    await expect(textarea).toHaveValue(address);
    await expect(counter).toHaveText(`${200 - address.length} tegn igjen`);
    await expect(textarea).not.toHaveAttribute("aria-invalid", "true");
    await expect(country).toHaveValue("NOR");
  }

  await textarea.fill("Manuelt endret mottaker");
  await expect(counter).toHaveText("177 tegn igjen");
  await expect(recipient).toHaveValue("");
  await expect(textarea).toHaveValue("Manuelt endret mottaker");
  await recipient.selectOption("almannaverkid");
  await recipient.selectOption("");
  await expect(textarea).toHaveValue("");
  await expect(counter).toHaveText("200 tegn igjen");
  await expect(textarea).not.toHaveAttribute("aria-invalid", "true");
  await page.getByTestId("order-letter").click();
  await expect(textarea).toHaveAttribute("aria-invalid", "true");
});

for (const { landkode, ids } of [
  { landkode: "FRO", ids: ["almannaverkid"] },
  { landkode: "GRL", ids: ["sociale-anliggender", "socialeomraadet"] },
  { landkode: "GBR", ids: ["isle-of-man", "jersey"] },
] as const) {
  test(`Land ${landkode} velger standardmottaker og tilbyr bare landets adresser`, async ({ page }) => {
    await page.route(orderUrl, (route) =>
      route.fulfill({ path: "test/e2e/fixtures/bestillBrevEblankett.json", contentType: "application/json" }),
    );
    await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
    const country = page.getByRole("combobox", { name: "Land", exact: true });
    const recipient = page.getByRole("combobox", { name: "Velg mottaker", exact: true });
    const recipientRadios = page.getByRole("radiogroup", { name: "Velg mottaker", exact: true });
    const textarea = page.getByRole("textbox", { name: "Mottaker", exact: true });
    await textarea.fill("Manuelt skrevet mottaker");
    await country.selectOption(landkode);
    await expect(textarea).toHaveValue(recipients[ids[0]]);
    await expect(recipient).not.toBeVisible();
    if (ids.length === 2) {
      const radios = recipientRadios.getByRole("radio");
      const customRecipient = recipientRadios.getByRole("radio", { name: "Egendefinert", exact: true });
      await expect(radios).toHaveCount(3);
      await expect(radios.nth(0)).toBeChecked();
      expect(await radios.evaluateAll((inputs) => inputs.map((input) => input.getAttribute("value")))).toEqual([
        ...ids,
        "",
      ]);
      const expectedNames =
        landkode === "GRL"
          ? ["Sociale Anliggender (udsatte voksne, ældre)", "Socialeområdet (udsatte voksne, ældre, familie)"]
          : ["Social Security Division (Isle of Man)", "Employment, Social Sec. & Housing (Jersey)"];
      for (const [index, id] of ids.entries()) {
        await expect(radios.nth(index)).toHaveAccessibleName(expectedNames[index]);
        await radios.nth(index).check();
        await expect(textarea).toHaveValue(recipients[id]);
      }

      await customRecipient.check();
      await expect(customRecipient).toBeChecked();
      await expect(textarea).toHaveValue("");
      await expect(textarea).not.toHaveAttribute("aria-invalid", "true");
      await page.getByTestId("order-letter").click();
      await expect(textarea).toHaveAttribute("aria-invalid", "true");
      await expect(page.getByTestId("order-letter-success-message")).not.toBeVisible();

      await radios.nth(1).check();
      await textarea.fill("Mottaker endret manuelt");
      await expect(customRecipient).toBeChecked();
      await expect(textarea).toHaveValue("Mottaker endret manuelt");
      await expect(textarea).not.toHaveAttribute("aria-invalid", "true");
      await country.selectOption("NOR");
      await expect(recipient).toHaveValue("");
      await expect(textarea).toHaveValue("Mottaker endret manuelt");
      await country.selectOption(landkode);
      await expect(radios.nth(0)).toBeChecked();
      await expect(textarea).toHaveValue(recipients[ids[0]]);
      await radios.nth(0).check();
      await radios.nth(0).press("ArrowDown");
      await expect(radios.nth(1)).toBeChecked();
      await expect(textarea).toHaveValue(recipients[ids[1]]);

      await country.selectOption("NOR");
      await expect(recipientRadios).not.toBeVisible();
      await expect(recipient).toHaveValue(ids[1]);
      await expect(textarea).toHaveValue(recipients[ids[1]]);
      await country.selectOption(landkode);
      await expect(radios.nth(0)).toBeChecked();
      await expect(textarea).toHaveValue(recipients[ids[0]]);
      await radios.nth(1).check();
    } else {
      await expect(recipientRadios).not.toBeVisible();
    }

    const request = page.waitForRequest(
      (request) => request.url().endsWith("/bestillBrev/exstream/eblankett") && request.method() === "POST",
    );
    await page.getByTestId("order-letter").click();
    expect((await request).postDataJSON()).toEqual({
      brevkode: "E001",
      landkode,
      mottakerText: recipients[ids[ids.length - 1]],
      enhetsId: "4815",
    });
    await expect(page.getByTestId("order-letter-success-message")).toBeVisible();
  });
}

test("Mottaker kan ikke være lengre enn 200 tegn", async ({ page }) => {
  let orders = 0;
  await page.route(orderUrl, async (route) => {
    orders++;
    await route.fulfill({ path: "test/e2e/fixtures/bestillBrevEblankett.json", contentType: "application/json" });
  });
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
  const textarea = page.getByRole("textbox", { name: "Mottaker", exact: true });
  const counter = page.locator(".aksel-textarea__counter");
  const error = page.getByText("Mottaker kan ikke være lengre enn 200 tegn", { exact: true });
  const proceed = page.getByTestId("order-letter");
  const validRecipient = `${"a".repeat(198)}\nø`;
  const tooLongRecipient = `${validRecipient}b`;

  await textarea.fill(tooLongRecipient);
  await expect(counter).toHaveText("1 tegn for mye");
  await expect(error).not.toBeVisible();
  await proceed.click();
  await expect(error).toBeVisible();
  await expect(textarea).toHaveAttribute("aria-invalid", "true");
  expect(orders).toBe(0);

  await textarea.fill(`${tooLongRecipient}c`);
  await expect(error).not.toBeVisible();
  await proceed.click();
  await expect(error).toBeVisible();
  expect(orders).toBe(0);

  await textarea.fill(validRecipient);
  await expect(counter).toHaveText("0 tegn igjen");
  await expect(error).not.toBeVisible();
  const request = page.waitForRequest(
    (request) => request.url().endsWith("/bestillBrev/exstream/eblankett") && request.method() === "POST",
  );
  await proceed.click();
  expect((await request).postDataJSON().mottakerText).toBe(validRecipient);
  await expect(page.getByTestId("order-letter-success-message")).toBeVisible();
  expect(orders).toBe(1);
});

test("Mottaker valideres bare når Fortsett trykkes etter redigering og valg", async ({ page }) => {
  let orders = 0;
  await page.route(orderUrl, async (route) => {
    orders++;
    await route.fulfill({ path: "test/e2e/fixtures/bestillBrevEblankett.json", contentType: "application/json" });
  });
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
  const country = page.getByRole("combobox", { name: "Land", exact: true });
  const recipient = page.getByRole("combobox", { name: "Velg mottaker", exact: true });
  const textarea = page.getByRole("textbox", { name: "Mottaker", exact: true });
  const error = page.getByText("Vennligst fyll inn mottaker", { exact: true });
  const proceed = page.getByTestId("order-letter");

  await proceed.click();
  await expect(error).toBeVisible();
  await textarea.fill("Mottaker");
  await expect(error).not.toBeVisible();
  await textarea.clear();
  await expect(error).not.toBeVisible();
  await proceed.click();
  await expect(error).toBeVisible();
  expect(orders).toBe(0);

  await recipient.selectOption("almannaverkid");
  await expect(error).not.toBeVisible();
  await recipient.selectOption("");
  await expect(textarea).toHaveValue("");
  await expect(error).not.toBeVisible();
  await proceed.click();
  await expect(error).toBeVisible();
  expect(orders).toBe(0);

  for (const code of ["GRL", "GBR"]) {
    await country.selectOption(code);
    await expect(error).not.toBeVisible();
    await page.getByRole("radio", { name: "Egendefinert", exact: true }).check();
    await expect(textarea).toHaveValue("");
    await expect(error).not.toBeVisible();
    await proceed.click();
    await expect(error).toBeVisible();
    expect(orders).toBe(0);

    await textarea.fill("Egen mottaker");
    await expect(error).not.toBeVisible();
    await textarea.clear();
    await expect(error).not.toBeVisible();
    await proceed.click();
    await expect(error).toBeVisible();
    expect(orders).toBe(0);
  }
});

test("Landbytte tømmer mottaker uten feilmelding før Fortsett trykkes", async ({ page }) => {
  let orders = 0;
  await page.route(orderUrl, async (route) => {
    orders++;
    await route.fulfill({ path: "test/e2e/fixtures/bestillBrevEblankett.json", contentType: "application/json" });
  });
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
  const country = page.getByRole("combobox", { name: "Land", exact: true });
  const textarea = page.getByRole("textbox", { name: "Mottaker", exact: true });
  const error = page.getByText("Vennligst fyll inn mottaker", { exact: true });

  for (const code of ["BEL", "", "SWE"]) {
    await country.selectOption("FRO");
    await expect(textarea).toHaveValue(recipients.almannaverkid);
    await country.selectOption(code);
    await expect(textarea).toHaveValue("");
    await expect(error).not.toBeVisible();
    await expect(textarea).not.toHaveAttribute("aria-invalid", "true");
    await page.getByTestId("order-letter").click();
    await expect(error).toBeVisible();
    await expect(textarea).toHaveAttribute("aria-invalid", "true");
    expect(orders).toBe(0);
  }

  await country.selectOption("BEL");
  await expect(error).not.toBeVisible();
  await page.getByTestId("order-letter").click();
  await expect(error).toBeVisible();
  expect(orders).toBe(0);
});

test("Land uten mottakere tømmer adressen og lar saksbehandler skrive en ny mottaker", async ({ page }) => {
  await page.route(orderUrl, (route) =>
    route.fulfill({ path: "test/e2e/fixtures/bestillBrevEblankett.json", contentType: "application/json" }),
  );
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
  const country = page.getByRole("combobox", { name: "Land", exact: true });
  const recipient = page.getByRole("combobox", { name: "Velg mottaker", exact: true });
  const textarea = page.getByRole("textbox", { name: "Mottaker", exact: true });
  for (const code of ["BEL", ""]) {
    await country.selectOption(code);
    await expect(recipient).not.toBeVisible();
    await expect(textarea).toBeEditable();
    await expect(textarea).toHaveValue("");
  }
  for (const code of ["BEL", "", "SWE"]) {
    await country.selectOption("FRO");
    await country.selectOption("NOR");
    await expect(recipient).toHaveValue("almannaverkid");
    await expect(textarea).toHaveValue(recipients.almannaverkid);
    await country.selectOption(code);
    await expect(textarea).toHaveValue("");
    await expect(textarea).toBeEditable();
    await expect(recipient).not.toBeVisible();
    await country.selectOption("NOR");
    await expect(recipient).toHaveValue("");
    await expect(textarea).toHaveValue("");
  }

  await country.selectOption("BEL");
  const manualRecipient = "Egen mottaker\nAdresse i Belgia";
  await textarea.fill(manualRecipient);
  await expect(recipient).not.toBeVisible();
  await expect(textarea).toHaveValue(manualRecipient);
  await country.selectOption("NOR");
  await expect(recipient).toHaveValue("");
  await expect(textarea).toHaveValue(manualRecipient);
  await country.selectOption("BEL");
  await expect(textarea).toHaveValue("");
  await page.getByTestId("order-letter").click();
  await expect(textarea).toHaveAttribute("aria-invalid", "true");
  await expect(page.getByTestId("order-letter-success-message")).not.toBeVisible();
  await textarea.fill(manualRecipient);
  await expect(textarea).not.toHaveAttribute("aria-invalid", "true");
  const request = page.waitForRequest(
    (request) => request.url().endsWith("/bestillBrev/exstream/eblankett") && request.method() === "POST",
  );
  await page.getByTestId("order-letter").click();
  expect((await request).postDataJSON()).toMatchObject({ landkode: "BEL", mottakerText: manualRecipient });
  await expect(page.getByTestId("order-letter-success-message")).toBeVisible();
});

test("E-blankett validerer tomt land og enhet og sporer ikke feil", async ({ page }) => {
  let orders = 0;
  await page.route(orderUrl, async (route) => {
    orders++;
    await route.fulfill({ status: 500, json: { message: "Bestilling feilet" } });
  });
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001");
  const country = page.getByRole("combobox", { name: "Land", exact: true });
  const enhet = page.getByRole("combobox", { name: "Avsenderenhet" });
  await expect(country).toHaveValue("NOR");
  await expect(enhet).toHaveValue("");
  await country.selectOption("");
  await page.getByTestId("order-letter").click();
  await expect(country).toHaveAttribute("aria-invalid", "true");
  await expect(enhet).toHaveAttribute("aria-invalid", "true");
  await expect(page.getByRole("textbox", { name: "Mottaker" })).toHaveAttribute("aria-invalid", "true");
  expect(orders).toBe(0);
  expect(await creationEvents(page)).toEqual([]);

  await country.selectOption("GBR");
  await enhet.selectOption("4405");
  await page.getByRole("textbox", { name: "Mottaker" }).fill("Testmottaker");
  await page.getByTestId("order-letter").click();
  await expect(page.getByRole("heading", { name: "Bestilling feilet" })).toBeVisible();
  expect(orders).toBe(1);
  expect(await creationEvents(page)).toEqual([]);
});

test("E-blankett synkroniserer enhet uten å nullstille andre felt", async ({ page }) => {
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
  const enhet = page.getByRole("combobox", { name: "Avsenderenhet" });
  const country = page.getByRole("combobox", { name: "Land", exact: true });
  const mottaker = page.getByRole("textbox", { name: "Mottaker" });
  await expect(enhet).toHaveValue("4815");
  await country.selectOption("GBR");
  await mottaker.fill("Testmottaker");
  await enhet.selectOption("4405");
  await expect(page).toHaveURL((url) => JSON.parse(url.searchParams.get("enhetsId") ?? "null") === "4405");
  await expect(country).toHaveValue("GBR");
  await expect(mottaker).toHaveValue("Testmottaker");

  for (const enhetsId of ["4808", ""]) {
    await page.evaluate((id) => {
      const url = new URL(location.href);
      if (id) {
        url.searchParams.set("enhetsId", id);
      } else {
        url.searchParams.delete("enhetsId");
      }
      history.pushState({}, "", url);
      dispatchEvent(new PopStateEvent("popstate"));
    }, enhetsId);
    await expect(enhet).toHaveValue(enhetsId);
    await expect(country).toHaveValue("GBR");
    await expect(mottaker).toHaveValue("Testmottaker");
    expect(new URL(page.url()).searchParams.get("enhetsId")).toBe(enhetsId || null);
  }

  await enhet.selectOption("4815");
  await page.getByTestId("brevmal-search").fill("E 204");
  await page.getByTestId("brevmal-button").filter({ hasText: "E 204" }).click();
  await expect(country).toHaveValue("NOR");
  await expect(mottaker).toHaveValue("");
  await expect(enhet).toHaveValue("4815");
});

test("E-blankett blokkerer bestilling til landene er lastet inn", async ({ page }) => {
  let orders = 0;
  await page.route(orderUrl, async (route) => {
    orders++;
    await route.fulfill({ path: "test/e2e/fixtures/bestillBrevEblankett.json", contentType: "application/json" });
  });
  let releaseOptions: () => void = () => {};
  const optionsReady = new Promise<void>((resolve) => {
    releaseOptions = resolve;
  });
  await page.route("**/bff/skribenten-backend/kodeverk/avtaleland", async (route) => {
    await optionsReady;
    await route.fallback();
  });
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
  await page.getByRole("textbox", { name: "Mottaker" }).fill("Testmottaker");
  await expect(page.getByRole("combobox", { name: "Avsenderenhet" })).toHaveValue("4815");
  const country = page.getByRole("combobox", { name: "Land", exact: true });
  await expect(country.locator("option")).toHaveText(["Laster land..."]);
  await page.getByTestId("order-letter").click();
  await expect(country).toHaveAttribute("aria-invalid", "true");
  expect(orders).toBe(0);
  expect(await creationEvents(page)).toEqual([]);

  releaseOptions();
  await expect(country).toHaveValue("NOR");
  await expect(page.getByRole("combobox", { name: "Avsenderenhet" })).toHaveValue("4815");
  await page.getByTestId("order-letter").click();
  await expect(page.getByTestId("order-letter-success-message")).toBeVisible();
  expect(orders).toBe(1);
});

for (const response of ["feil", "tom liste", "mangler Norge"]) {
  test(`E-blankett blokkerer ugyldig land ved ${response}`, async ({ page }) => {
    let orders = 0;
    await page.route(orderUrl, async (route) => {
      orders++;
      await route.fulfill({ path: "test/e2e/fixtures/bestillBrevEblankett.json", contentType: "application/json" });
    });
    await page.route("**/bff/skribenten-backend/kodeverk/avtaleland", async (route) => {
      await route.fulfill({
        status: response === "feil" ? 500 : 200,
        json: response === "mangler Norge" ? avtaleland.filter((land) => land.kode !== "NOR") : [],
      });
    });
    await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
    const country = page.getByRole("combobox", { name: "Land", exact: true });
    await expect(country.locator("option").first()).toHaveText("Velg land");
    if (response === "feil") {
      await expect(page.getByRole("heading", { name: "Klarte ikke å hente land", exact: true })).toBeVisible();
    }
    await expect(page.getByRole("combobox", { name: "Avsenderenhet" })).toHaveValue("4815");
    await page.getByRole("textbox", { name: "Mottaker" }).fill("Testmottaker");
    await page.getByTestId("order-letter").click();
    await expect(country).toHaveAttribute("aria-invalid", "true");
    expect(orders).toBe(0);
    expect(await creationEvents(page)).toEqual([]);

    if (response === "mangler Norge") {
      await country.selectOption("GBR");
      await page.getByTestId("order-letter").click();
      await expect(page.getByTestId("order-letter-success-message")).toBeVisible();
      expect(orders).toBe(1);
    }
  });
}
