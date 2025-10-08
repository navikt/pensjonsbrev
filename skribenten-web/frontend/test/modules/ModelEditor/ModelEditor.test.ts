import { describe, expect, it } from "vitest";

import { extractRelevantSaksbehandlerValgFields } from "~/Brevredigering/ModelEditor/ModelEditor";
import type { PropertyUsage } from "~/types/brevbakerTypes";

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
});
