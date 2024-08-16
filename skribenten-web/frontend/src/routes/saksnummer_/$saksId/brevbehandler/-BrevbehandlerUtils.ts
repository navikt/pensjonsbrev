import { type BrevStatus } from "~/types/brev";

export const brevStatusTypeToTextAndTagVariant = (status: BrevStatus) => {
  switch (status.type) {
    case "Kladd": {
      return { variant: "warning" as const, text: "Kladd", description: null };
    }
    case "Klar": {
      return { variant: "success" as const, text: "Klar", description: null };
    }

    case "UnderRedigering": {
      return { variant: "alt1" as const, text: "Under redigering", description: `Redigeres av ${status.redigeresAv}` };
    }
  }
};
