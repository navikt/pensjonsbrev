import { type BrevStatus } from "~/types/brev";

export const brevStatusTypeToTextAndTagVariant = (status: BrevStatus) => {
  switch (status.type) {
    case "Kladd": {
      return { variant: "warning" as const, text: "Kladd" };
    }
    case "Klar": {
      return { variant: "success" as const, text: "Klar til sending" };
    }

    case "UnderRedigering": {
      return { variant: "neutral" as const, text: `Redigeres av ${status.redigeresAv.navn}` };
    }
  }
};
