import fs from "node:fs";
import path from "node:path";

import { expect, test } from "@playwright/test";

import { setupSakStubs } from "~test/e2e/support/helpers";

const brevResponse = JSON.parse(fs.readFileSync(path.resolve("test/e2e/fixtures/brevResponse.json"), "utf-8"));

const VEDLEGG_ID = "vedlegg-om-alderspensjon";
const VEDLEGG_TITTEL = "Opplysninger om alderspensjon";
const VEDLEGG_BROEDTEKST = "Vedleggsteksten som saksbehandler kan redigere.";
const ANNET_VEDLEGG_ID = "vedlegg-om-bankopplysninger";
const ANNET_VEDLEGG_TITTEL = "Bankopplysninger";
const ANNET_VEDLEGG_BROEDTEKST = "Bankopplysningene vi har om deg.";
/** Speiler AUTOSAVE_TIMER i src, slik at ventetidene her dekker en hel debounce-runde. */
const AUTOSAVE_DEBOUNCE_MS = 5000;

const VEDLEGGLISTE_URL = "**/bff/skribenten-backend/sak/123456/brev/1/redigerbareVedlegg";
const vedleggUrl = (vedleggId: string) => `${VEDLEGGLISTE_URL}/${vedleggId}`;

const literal = (text: string, id: number) => ({
  type: "LITERAL",
  id: id,
  text: text,
  editedText: null,
  tags: [],
});

const lagVedlegg = (tittel: string, broedtekst: string, idBase: number) => ({
  includeSakspart: false,
  title: { text: [literal(tittel, idBase)], deletedContent: [] },
  deletedBlocks: [],
  blocks: [
    {
      id: idBase + 1,
      type: "PARAGRAPH",
      editable: true,
      deletedContent: [],
      content: [literal(broedtekst, idBase + 2)],
    },
  ],
});

const vedlegg = lagVedlegg(VEDLEGG_TITTEL, VEDLEGG_BROEDTEKST, 900);
const annetVedlegg = lagVedlegg(ANNET_VEDLEGG_TITTEL, ANNET_VEDLEGG_BROEDTEKST, 920);

/** Åpner vedlegget og gjør en endring i brødteksten, uten å vente på autolagringen. */
const endreVedlegg = async (page: import("@playwright/test").Page, tekst: string) => {
  await page.getByText(VEDLEGG_BROEDTEKST).click();
  await page.locator(":focus").pressSequentially(tekst);
};

test.describe("Redigerbare vedlegg", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevResponse.json", contentType: "application/json" }),
    );
    await page.route(
      "**/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" }),
    );
    await page.route("**/bff/skribenten-backend/brev/1/reservasjon", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevreservasjon.json", contentType: "application/json" }),
    );

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/redigerbareVedlegg", (route) =>
      route.fulfill({ json: [{ vedleggId: VEDLEGG_ID, tittel: VEDLEGG_TITTEL }] }),
    );
    await page.route(`**/bff/skribenten-backend/sak/123456/brev/1/redigerbareVedlegg/${VEDLEGG_ID}`, (route) =>
      route.fulfill({ json: vedlegg }),
    );
  });

  test("åpner brevet på brevmal-fanen", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");

    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();
    await expect(page.getByRole("tab", { name: "Brevmal" })).toHaveAttribute("aria-selected", "true");
    await expect(page.getByText(VEDLEGG_BROEDTEKST)).toBeHidden();
  });

  test("viser ingen faner når brevet ikke har redigerbare vedlegg", async ({ page }) => {
    await page.route(VEDLEGGLISTE_URL, (route) => route.fulfill({ json: [] }));

    await page.goto("/saksnummer/123456/brev/1");

    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();
    await expect(page.getByRole("tab", { name: "Brevmal" })).toBeHidden();
    await expect(page.getByRole("tab", { name: "Vedlegg" })).toBeHidden();
  });

  test("åpner det første vedlegget i redigeringsfeltet når vedlegg-fanen velges", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();

    await page.getByRole("tab", { name: "Vedlegg" }).click();

    await expect(
      page.getByRole("region", { name: VEDLEGG_TITTEL }).getByRole("button", { name: "Vis mer" }),
    ).toHaveAttribute("aria-expanded", "true");
    await expect(page.getByText(VEDLEGG_BROEDTEKST)).toBeVisible();
    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeHidden();
    await expect(page).toHaveURL(new RegExp(`vedlegg=${VEDLEGG_ID}`));
  });

  test("dyplenke til et vedlegg åpner vedlegg-fanen", async ({ page }) => {
    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);

    await expect(page.getByRole("tab", { name: "Vedlegg" })).toHaveAttribute("aria-selected", "true");
    await expect(page.getByText(VEDLEGG_BROEDTEKST)).toBeVisible();
  });

  test("ukjent vedlegg i dyplenke går tilbake til brevet og brevmal-fanen", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1?vedlegg=finnes-ikke");

    await expect(page).not.toHaveURL(/vedlegg=/);
    await expect(page.getByRole("tab", { name: "Brevmal" })).toHaveAttribute("aria-selected", "true");
    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();
    await expect(page.getByText(VEDLEGG_BROEDTEKST)).toBeHidden();
  });

  test("går tilbake til brevet når vedlegget lukkes", async ({ page }) => {
    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await expect(page.getByText(VEDLEGG_BROEDTEKST)).toBeVisible();

    await page.getByRole("region", { name: VEDLEGG_TITTEL }).getByRole("button", { name: "Vis mer" }).click();

    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();
  });

  test("autolagrer en endring i vedlegget én gang", async ({ page }) => {
    const lagringer: string[] = [];
    await page.route(`**/bff/skribenten-backend/sak/123456/brev/1/redigerbareVedlegg/${VEDLEGG_ID}`, (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      const lagret = route.request().postDataJSON().redigertVedlegg;
      lagringer.push(JSON.stringify(lagret));
      return route.fulfill({ json: lagret });
    });

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await page.getByText(VEDLEGG_BROEDTEKST).click();
    await page.locator(":focus").pressSequentially(" endret!");

    await expect(page.getByText("Lagret")).toBeVisible({ timeout: 15_000 });
    await expect(page.getByText("Klarte ikke lagre")).toBeHidden();
    expect(lagringer).toHaveLength(1);
    expect(lagringer[0]).toContain("endret!");

    await page.waitForTimeout(AUTOSAVE_DEBOUNCE_MS + 2000);
    expect(lagringer).toHaveLength(1);
  });

  test("viser innhold som backend merger inn ved lagring", async ({ page }) => {
    const NY_TEKST_FRA_MAL = "Nytt innhold fra oppdatert vedleggsmal.";
    await page.route(vedleggUrl(VEDLEGG_ID), (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      const lagret = route.request().postDataJSON().redigertVedlegg;
      return route.fulfill({
        json: {
          ...lagret,
          blocks: [
            ...lagret.blocks,
            {
              id: 950,
              type: "PARAGRAPH",
              editable: true,
              deletedContent: [],
              content: [literal(NY_TEKST_FRA_MAL, 951)],
            },
          ],
        },
      });
    });

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " endret!");

    await expect(page.getByText("Lagret")).toBeVisible({ timeout: 15_000 });
    await expect(page.getByText(NY_TEKST_FRA_MAL)).toBeVisible();
  });

  test("bevarer angrehistorikk når backend returnerer null for utelatte felter", async ({ page }) => {
    await page.route(vedleggUrl(VEDLEGG_ID), (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      const lagret = route.request().postDataJSON().redigertVedlegg;
      return route.fulfill({ json: { ...lagret, parentId: null } });
    });

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " endret!");

    await expect(page.getByText("Lagret")).toBeVisible({ timeout: 15_000 });
    await expect(page.getByRole("button", { name: "Angre (Undo)" })).toBeEnabled();
    await page.getByRole("button", { name: "Angre (Undo)" }).click();
    await expect(page.getByText(VEDLEGG_BROEDTEKST, { exact: true })).toBeVisible();
  });

  test("prøver ikke samme lagring på nytt i det uendelige når lagring feiler", async ({ page }) => {
    let forsoek = 0;
    await page.route(`**/bff/skribenten-backend/sak/123456/brev/1/redigerbareVedlegg/${VEDLEGG_ID}`, (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      forsoek += 1;
      return route.fulfill({ status: 500, json: "Uff" });
    });

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await page.getByText(VEDLEGG_BROEDTEKST).click();
    await page.locator(":focus").pressSequentially(" endret!");

    await expect(page.getByText("Klarte ikke lagre")).toBeVisible({ timeout: 15_000 });
    await page.waitForTimeout(AUTOSAVE_DEBOUNCE_MS + 2000);
    expect(forsoek).toBe(1);
  });

  test("lagrer nyere endringer selv om en tidligere lagring feilet", async ({ page }) => {
    const forsoek: string[] = [];
    await page.route(vedleggUrl(VEDLEGG_ID), async (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      const redigertVedlegg = route.request().postDataJSON().redigertVedlegg;
      forsoek.push(JSON.stringify(redigertVedlegg));
      if (forsoek.length === 1) {
        // Hold den første lagringen åpen slik at saksbehandler rekker å skrive mer, og la den feile.
        await new Promise((resolve) => setTimeout(resolve, 3000));
        return route.fulfill({ status: 500, json: "Uff" });
      }
      return route.fulfill({ json: redigertVedlegg });
    });

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " AAA");

    await expect.poll(() => forsoek.length, { timeout: 15_000 }).toBe(1);
    await page.locator(":focus").pressSequentially("BBB");

    await expect.poll(() => forsoek.length, { timeout: 20_000 }).toBe(2);
    expect(forsoek[1]).toContain("AAABBB");
  });

  test("lagrer vedlegget før Fortsett lagrer brevet og frigir reservasjonen", async ({ page }) => {
    const hendelser: string[] = [];

    await page.route(vedleggUrl(VEDLEGG_ID), async (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      hendelser.push("vedlegg-lagring-start");
      await new Promise((resolve) => setTimeout(resolve, 1000));
      hendelser.push("vedlegg-lagring-slutt");
      return route.fulfill({ json: route.request().postDataJSON().redigertVedlegg });
    });

    await page.route(
      (url) => url.pathname.endsWith("/sak/123456/brev/1") && !url.search.includes("reserver"),
      (route) => {
        if (route.request().method() !== "PUT") {
          return route.fallback();
        }
        hendelser.push("brev-lagring");
        return route.fulfill({ json: brevResponse });
      },
    );

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " endret!");

    // Klikk Fortsett med én gang, altså før autolagringens debounce har løpt ut.
    await page.getByText("Fortsett", { exact: true }).click();
    // Brevet kan kreve en bekreftelse i varselmodalen først, avhengig av innholdet i fixturen.
    const bekreftIModal = page.getByText("Fortsett til brevbehandler");
    await bekreftIModal
      .waitFor({ state: "visible", timeout: 1000 })
      .then(() => bekreftIModal.click())
      .catch(() => undefined);

    await expect(page).toHaveURL(/brevbehandler/, { timeout: 20_000 });
    expect(hendelser).toEqual(["vedlegg-lagring-start", "vedlegg-lagring-slutt", "brev-lagring"]);
  });

  test("lagrer endringer skrevet mens Fortsett-lagringen pågår før brevet lagres", async ({ page }) => {
    const lagringer: string[] = [];
    const hendelser: string[] = [];

    await page.route(vedleggUrl(VEDLEGG_ID), async (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      const redigertVedlegg = route.request().postDataJSON().redigertVedlegg;
      lagringer.push(JSON.stringify(redigertVedlegg));
      hendelser.push(`vedlegg-${lagringer.length}`);
      if (lagringer.length === 1) {
        await new Promise((resolve) => setTimeout(resolve, 1500));
      }
      return route.fulfill({ json: redigertVedlegg });
    });

    await page.route(
      (url) => url.pathname.endsWith("/sak/123456/brev/1") && !url.search.includes("reserver"),
      (route) => {
        if (route.request().method() !== "PUT") {
          return route.fallback();
        }
        hendelser.push("brev-lagring");
        return route.fulfill({ json: brevResponse });
      },
    );

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " AAA");
    await page.getByText("Fortsett", { exact: true }).click();
    const bekreftIModal = page.getByText("Fortsett til brevbehandler");
    await bekreftIModal
      .waitFor({ state: "visible", timeout: 1000 })
      .then(() => bekreftIModal.click())
      .catch(() => undefined);

    await expect.poll(() => lagringer.length, { timeout: 10_000 }).toBe(1);
    const redigerbarTekst = page.locator("span[contenteditable='true']", { hasText: "AAA" });
    await redigerbarTekst.focus();
    await redigerbarTekst.press("End");
    await redigerbarTekst.pressSequentially("BBB");

    await expect(page).toHaveURL(/brevbehandler/, { timeout: 20_000 });
    expect(lagringer).toHaveLength(2);
    expect(lagringer[0]).toContain("AAA");
    expect(lagringer[0]).not.toContain("BBB");
    expect(lagringer[1]).toContain("AAA");
    expect(lagringer[1]).toContain("BBB");
    expect(hendelser).toEqual(["vedlegg-1", "vedlegg-2", "brev-lagring"]);
  });

  test("Fortsett lagrer ikke brevet når vedlegget ikke lot seg lagre", async ({ page }) => {
    let brevLagret = false;
    let lagringsforsoek = 0;

    await page.route(vedleggUrl(VEDLEGG_ID), (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      lagringsforsoek += 1;
      return route.fulfill({ status: 500, json: "Uff" });
    });
    await page.route(
      (url) => url.pathname.endsWith("/sak/123456/brev/1") && !url.search.includes("reserver"),
      (route) => {
        if (route.request().method() !== "PUT") {
          return route.fallback();
        }
        brevLagret = true;
        return route.fulfill({ json: brevResponse });
      },
    );

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " endret!");

    // La autolagringen feile først: da må Fortsett prøve på nytt i stedet for å tro at alt er lagret.
    await expect(page.getByText("Klarte ikke lagre")).toBeVisible({ timeout: 15_000 });
    expect(lagringsforsoek).toBe(1);

    await page.getByText("Fortsett", { exact: true }).click();
    const bekreftIModal = page.getByText("Fortsett til brevbehandler");
    await bekreftIModal
      .waitFor({ state: "visible", timeout: 1000 })
      .then(() => bekreftIModal.click())
      .catch(() => undefined);

    await expect.poll(() => lagringsforsoek, { timeout: 10_000 }).toBe(2);
    await expect(page).toHaveURL(new RegExp(`vedlegg=${VEDLEGG_ID}`));
    expect(brevLagret).toBe(false);
  });

  test("lagrer vedlegget før saksbehandler går tilbake til brevvelger", async ({ page }) => {
    const hendelser: string[] = [];
    await page.route(vedleggUrl(VEDLEGG_ID), async (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      hendelser.push("lagring-start");
      await new Promise((resolve) => setTimeout(resolve, 1000));
      hendelser.push("lagring-slutt");
      return route.fulfill({ json: route.request().postDataJSON().redigertVedlegg });
    });

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " endret!");
    await page.getByRole("button", { name: "Tilbake til brevvelger" }).click();

    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevvelger/, { timeout: 15_000 });
    expect(hendelser).toEqual(["lagring-start", "lagring-slutt"]);
  });

  test("blir i vedlegget når lagring før tilbake til brevvelger feiler", async ({ page }) => {
    let lagringsforsoek = 0;
    await page.route(vedleggUrl(VEDLEGG_ID), (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      lagringsforsoek += 1;
      return route.fulfill({ status: 500, json: "Uff" });
    });

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " endret!");
    await page.getByRole("button", { name: "Tilbake til brevvelger" }).click();

    await expect(page.getByText("Klarte ikke lagre")).toBeVisible();
    await expect(page).toHaveURL(new RegExp(`vedlegg=${VEDLEGG_ID}`));
    expect(lagringsforsoek).toBe(1);
  });

  test("lagrer vedlegget nøyaktig én gang når saksbehandler bytter til et annet vedlegg", async ({ page }) => {
    const lagringer: string[] = [];
    await page.route(VEDLEGGLISTE_URL, (route) =>
      route.fulfill({
        json: [
          { vedleggId: VEDLEGG_ID, tittel: VEDLEGG_TITTEL },
          { vedleggId: ANNET_VEDLEGG_ID, tittel: ANNET_VEDLEGG_TITTEL },
        ],
      }),
    );
    await page.route(vedleggUrl(ANNET_VEDLEGG_ID), (route) => route.fulfill({ json: annetVedlegg }));
    await page.route(vedleggUrl(VEDLEGG_ID), (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      const redigertVedlegg = route.request().postDataJSON().redigertVedlegg;
      lagringer.push(JSON.stringify(redigertVedlegg));
      return route.fulfill({ json: redigertVedlegg });
    });

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " endret!");

    // Bytt vedlegg før debouncen har løpt ut.
    await page.getByRole("region", { name: ANNET_VEDLEGG_TITTEL }).getByRole("button", { name: "Vis mer" }).click();
    await expect(page.getByText(ANNET_VEDLEGG_BROEDTEKST)).toBeVisible();

    await expect.poll(() => lagringer.length, { timeout: 15_000 }).toBe(1);
    expect(lagringer[0]).toContain("endret!");

    await page.waitForTimeout(AUTOSAVE_DEBOUNCE_MS + 2000);
    expect(lagringer).toHaveLength(1);
  });

  test("blir i vedlegget når lagring før dokumentbytte feiler", async ({ page }) => {
    let lagringsforsoek = 0;
    await page.route(VEDLEGGLISTE_URL, (route) =>
      route.fulfill({
        json: [
          { vedleggId: VEDLEGG_ID, tittel: VEDLEGG_TITTEL },
          { vedleggId: ANNET_VEDLEGG_ID, tittel: ANNET_VEDLEGG_TITTEL },
        ],
      }),
    );
    await page.route(vedleggUrl(ANNET_VEDLEGG_ID), (route) => route.fulfill({ json: annetVedlegg }));
    await page.route(vedleggUrl(VEDLEGG_ID), (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      lagringsforsoek += 1;
      return route.fulfill({ status: 500, json: "Uff" });
    });

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " endret!");
    await page.getByRole("region", { name: ANNET_VEDLEGG_TITTEL }).getByRole("button", { name: "Vis mer" }).click();

    await expect(page.getByText("Klarte ikke lagre")).toBeVisible();
    await expect(
      page.getByRole("region", { name: VEDLEGG_TITTEL }).getByRole("button", { name: "Vis mer" }),
    ).toHaveAttribute("aria-expanded", "true");
    await expect(page.getByText(ANNET_VEDLEGG_BROEDTEKST)).toBeHidden();
    await expect(page).toHaveURL(new RegExp(`vedlegg=${VEDLEGG_ID}`));
    expect(lagringsforsoek).toBe(1);
  });

  test("tilbakestilling setter tittelen i vedleggslisten tilbake til malens", async ({ page }) => {
    const ENDRET_TITTEL = "Mine opplysninger";

    await page.route(VEDLEGGLISTE_URL, (route) =>
      route.fulfill({ json: [{ vedleggId: VEDLEGG_ID, tittel: VEDLEGG_TITTEL }] }),
    );
    await page.route(vedleggUrl(VEDLEGG_ID), (route) => {
      const metode = route.request().method();
      // Tittelen i listen skal følge svaret fra backend, ikke en ny henting av listen.
      if (metode === "PUT") {
        const lagret = route.request().postDataJSON().redigertVedlegg;
        return route.fulfill({ json: { ...lagret, title: { ...lagret.title, text: [literal(ENDRET_TITTEL, 901)] } } });
      }
      if (metode === "DELETE") {
        return route.fulfill({ json: vedlegg });
      }
      return route.fulfill({ json: vedlegg });
    });

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " endret!");
    await expect(page.getByRole("region", { name: ENDRET_TITTEL })).toBeVisible({ timeout: 15_000 });
    await expect(page.getByTestId("tilbakestill-mal-button")).toBeHidden();

    await page
      .getByRole("region", { name: ENDRET_TITTEL })
      .getByRole("button", { name: "Tilbakestill vedlegg" })
      .click();
    await page.getByText("Ja, tilbakestill vedlegget").click();

    await expect(page.getByRole("region", { name: VEDLEGG_TITTEL })).toBeVisible();
  });

  test("tilbakestilling venter på en pågående lagring", async ({ page }) => {
    const hendelser: string[] = [];
    await page.route(vedleggUrl(VEDLEGG_ID), async (route) => {
      const metode = route.request().method();
      if (metode === "PUT") {
        hendelser.push("lagring-start");
        await new Promise((resolve) => setTimeout(resolve, 2000));
        hendelser.push("lagring-slutt");
        return route.fulfill({ json: route.request().postDataJSON().redigertVedlegg });
      }
      if (metode === "DELETE") {
        hendelser.push("tilbakestilling");
        return route.fulfill({ json: vedlegg });
      }
      return route.fulfill({ json: vedlegg });
    });

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " endret!");

    await expect.poll(() => hendelser.includes("lagring-start"), { timeout: 15_000 }).toBe(true);
    await page
      .getByRole("region", { name: VEDLEGG_TITTEL })
      .getByRole("button", { name: "Tilbakestill vedlegg" })
      .click();
    await page.getByText("Ja, tilbakestill vedlegget").click();

    await expect.poll(() => hendelser.includes("tilbakestilling"), { timeout: 15_000 }).toBe(true);
    expect(hendelser).toEqual(["lagring-start", "lagring-slutt", "tilbakestilling"]);
  });

  test("starter ikke en ny lagring mens en lagring fortsatt pågår", async ({ page }) => {
    test.setTimeout(90_000);
    // Lengre enn debouncen, slik at neste autolagring blir klar mens den forrige fortsatt pågår.
    const TREG_LAGRING_MS = AUTOSAVE_DEBOUNCE_MS + 1500;
    const lagringer: string[] = [];
    let samtidige = 0;
    let maksSamtidige = 0;

    await page.route(vedleggUrl(VEDLEGG_ID), async (route) => {
      if (route.request().method() !== "PUT") {
        return route.fulfill({ json: vedlegg });
      }
      const redigertVedlegg = route.request().postDataJSON().redigertVedlegg;
      lagringer.push(JSON.stringify(redigertVedlegg));
      samtidige += 1;
      maksSamtidige = Math.max(maksSamtidige, samtidige);
      await new Promise((resolve) => setTimeout(resolve, TREG_LAGRING_MS));
      samtidige -= 1;
      return route.fulfill({ json: redigertVedlegg });
    });

    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await endreVedlegg(page, " AAA");

    // Skriv videre så snart den trege lagringen er i gang.
    await expect.poll(() => lagringer.length, { timeout: 15_000 }).toBe(1);
    await page.locator(":focus").pressSequentially("BBB");

    await expect.poll(() => lagringer.length, { timeout: 30_000 }).toBe(2);
    await expect(page.getByText("Lagret")).toBeVisible({ timeout: 30_000 });

    expect(maksSamtidige).toBe(1);
    expect(lagringer[1]).toContain("AAABBB");
  });
});
