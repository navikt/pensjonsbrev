import { describe, expect, test } from "vitest";

import { type Adresse } from "~/types/apiTypes";
import { ManueltAdressertTil } from "~/types/brev";
import { mapEndreMottakerValueTilMottaker } from "~/utils/AdresseUtils";

function adresse(overstyringer: Partial<Adresse> = {}): Adresse {
  return {
    navn: "Test Testesen",
    linje1: "Gateveien 1",
    linje2: null,
    linje3: null,
    postnr: "0001",
    poststed: "Oslo",
    land: "NO",
    manueltAdressertTil: ManueltAdressertTil.BRUKER,
    ...overstyringer,
  };
}

describe("mapEndreMottakerValueTilMottaker", () => {
  test("mapper en streng til samhandler", () => {
    expect(mapEndreMottakerValueTilMottaker("80000123456")).toEqual({
      type: "Samhandler",
      tssId: "80000123456",
      navn: null,
    });
  });

  test("mapper land NO til norsk adresse med postnummer og poststed", () => {
    expect(mapEndreMottakerValueTilMottaker(adresse())).toMatchObject({
      type: "NorskAdresse",
      postnummer: "0001",
      poststed: "Oslo",
      adresselinje1: "Gateveien 1",
    });
  });

  test("mapper andre land til utenlandsk adresse med landkode", () => {
    expect(mapEndreMottakerValueTilMottaker(adresse({ land: "SE", postnr: "", poststed: "" }))).toMatchObject({
      type: "UtenlandskAdresse",
      landkode: "SE",
      adresselinje1: "Gateveien 1",
    });
  });

  /*
  Land kan ikke bli null via skjemaet - valideringen stopper det. Testen låser likevel oppførselen,
  fordi Adresse.land er Nullable<string> og en framtidig kaller kan sende inn en adresse fra en
  annen kilde. Da skal vi feile høylytt framfor å sende landkode: null videre til PEN.
  */
  test("kaster dersom land mangler, i stedet for å sende en adresse uten landkode videre", () => {
    expect(() => mapEndreMottakerValueTilMottaker(adresse({ land: null }))).toThrow(
      "Teknisk feil - manuell adresse mangler land",
    );
  });
});
