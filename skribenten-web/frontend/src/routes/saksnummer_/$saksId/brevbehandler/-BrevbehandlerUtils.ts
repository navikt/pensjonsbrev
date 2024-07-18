import type { BrevInfoStatus } from "~/types/brev";
import { BrevInfoStatusType } from "~/types/brev";

export enum DistribusjonsMetode {
  Sentralprint = "Sentralprint",
  Lokaltprint = "Lokaltprint",
}

export const brevInfoStatusTypeToTextAndTagVariant = (status: BrevInfoStatus) => {
  switch (status.type) {
    case BrevInfoStatusType.KLADD: {
      return { variant: "warning" as const, text: "Kladd", description: null };
    }
    case BrevInfoStatusType.KLAR: {
      return { variant: "success" as const, text: "Klar", description: null };
    }

    case BrevInfoStatusType.UNDER_REDIGERING: {
      return { variant: "alt1" as const, text: "Under redigering", description: `Redigeres av ${status.redigeresAv}` };
    }
  }
};
