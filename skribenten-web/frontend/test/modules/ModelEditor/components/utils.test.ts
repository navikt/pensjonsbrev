import { describe, expect } from "vitest";

import { convertFieldToReadableLabel, getDefaultValueForField } from "~/Brevredigering/ModelEditor/components/utils";
import { type FieldType, type ObjectTypeSpecifications } from "~/types/brevbakerTypes";

describe("convertFieldToReadableLabel", () => {
  it("should split words by capital letters", () => {
    expect(convertFieldToReadableLabel("mottattFraUtland").toLowerCase()).toEqual("mottatt fra utland");
  });

  it("should capitalize first letter", () => {
    expect(convertFieldToReadableLabel("denGangDa")).toEqual("Den gang da");
  });

  it("should keep casing of acronyms", () => {
    expect(convertFieldToReadableLabel("harAFPOgUFO")).toEqual("Har AFP og UFO");
  });

  it("should replace æ", () => {
    expect(convertFieldToReadableLabel("aerekjaerFaerrest")).toEqual("Ærekjær færrest");
  });

  it("should replace ø", () => {
    expect(convertFieldToReadableLabel("oevrigeFoelelser")).toEqual("Øvrige følelser");
  });

  it("should replace å", () => {
    expect(convertFieldToReadableLabel("aaremaalPaaGang")).toEqual("Åremål på gang");
  });

  it("should accept norwegian characters", () => {
    expect(convertFieldToReadableLabel("søknadOversendesTilUtlandet")).toEqual("Søknad oversendes til utlandet");
  });

  it("should build defaults for nested display-only structures", () => {
    const types: ObjectTypeSpecifications = {
      SaksbehandlerValg: {
        alderspensjonListe: {
          displayText: "Alderspensjon",
          items: {
            displayText: null,
            nullable: false,
            type: "object",
            typeName: "Alderspensjon",
          },
          nullable: false,
          type: "array",
        },
        vilkaarsproevingsresultat: {
          displayText: "Vilkårsprøving",
          nullable: false,
          type: "object",
          typeName: "Vilkaarsproevingsresultat",
        },
      },
      Vilkaarsproevingsresultat: {
        alternativ: {
          displayText: "Alternativ",
          nullable: true,
          type: "object",
          typeName: "Uttaksparametre",
        },
        erInnvilget: {
          displayText: "Innvilget",
          kind: "BOOLEAN",
          nullable: false,
          type: "scalar",
        },
      },
    };

    const field: FieldType = {
      displayText: null,
      nullable: false,
      type: "object",
      typeName: "SaksbehandlerValg",
    };

    expect(getDefaultValueForField(field, types)).toEqual({
      alderspensjonListe: [],
      vilkaarsproevingsresultat: {
        alternativ: null,
        erInnvilget: false,
      },
    });
  });
});
