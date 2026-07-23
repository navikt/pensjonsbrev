import { describe, expect, it } from "vitest";

import {
  extractRelevantSaksbehandlerValgFields,
  filterModelSpecificationByPropertyUsage,
} from "~/Brevredigering/ModelEditor/ModelEditor";
import { type FieldType, type PropertyUsage } from "~/types/brevbakerTypes";

describe("extractRelevantSaksbehandlerValgFields", () => {
  it("returns only properties matching the SaksbehandlerValg type", () => {
    const usage: PropertyUsage[] = [
      {
        typeName: "no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto.PesysData",
        propertyName: "sakstype",
      },
      {
        typeName:
          "no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto.SaksbehandlerValg",
        propertyName: "infoOmstillingsstoenad",
      },
      {
        typeName:
          "no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto.SaksbehandlerValg",
        propertyName: "gjenlevendeHarBarnUnder18MedAvdoed",
      },
      {
        typeName: "no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto",
        propertyName: "pesysData",
      },
    ];

    const result = extractRelevantSaksbehandlerValgFields(
      usage,
      "no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto.SaksbehandlerValg",
    );

    expect(Array.from(result)).toEqual(["infoOmstillingsstoenad", "gjenlevendeHarBarnUnder18MedAvdoed"]);
  });

  it("returns an empty set when the SaksbehandlerValg type is missing", () => {
    const usage: PropertyUsage[] = [
      {
        typeName: "no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto.PesysData",
        propertyName: "sakstype",
      },
    ];

    const result = extractRelevantSaksbehandlerValgFields(usage, undefined);

    expect(result.size).toBe(0);
  });

  it("returns an empty set when no properties match the SaksbehandlerValg type", () => {
    const usage: PropertyUsage[] = [
      {
        typeName: "no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto.PesysData",
        propertyName: "sakstype",
      },
    ];

    const result = extractRelevantSaksbehandlerValgFields(
      usage,
      "no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto.SaksbehandlerValg",
    );

    expect(result.size).toBe(0);
  });
});

describe("filterModelSpecificationByPropertyUsage", () => {
  const specification = {
    used: {} as FieldType,
    unused: {} as FieldType,
  };

  it("returns the complete specification when property usage is unavailable", () => {
    expect(filterModelSpecificationByPropertyUsage(specification, undefined, "SaksbehandlerValg")).toBe(specification);
  });

  it("returns no fields when property usage contains no matching fields", () => {
    expect(filterModelSpecificationByPropertyUsage(specification, [], "SaksbehandlerValg")).toEqual({});
  });

  it("returns only fields used by SaksbehandlerValg", () => {
    const propertyUsage: PropertyUsage[] = [
      { typeName: "SaksbehandlerValg", propertyName: "used" },
      { typeName: "PesysData", propertyName: "unused" },
    ];

    expect(filterModelSpecificationByPropertyUsage(specification, propertyUsage, "SaksbehandlerValg")).toEqual({
      used: specification.used,
    });
  });
});
