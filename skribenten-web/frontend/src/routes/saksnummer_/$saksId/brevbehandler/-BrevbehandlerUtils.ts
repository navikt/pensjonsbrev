import type { UserInfo } from "~/api/bff-endpoints";
import { type BrevStatus, type NavAnsatt } from "~/types/brev";

export const brevStatusTypeToTextAndTagVariant = (status: BrevStatus, gjeldendeBruker?: UserInfo) => {
  switch (status.type) {
    case "Kladd": {
      return { variant: "warning" as const, text: "Kladd" };
    }
    case "Klar": {
      return { variant: "success" as const, text: "Klar til sending" };
    }

    case "UnderRedigering": {
      return {
        variant: "neutral" as const,
        text: `Redigeres av ${forkortetSaksbehandlernavn(status.redigeresAv, gjeldendeBruker)}`,
      };
    }
  }
};

export const forkortetSaksbehandlernavn = (navAnsatt: NavAnsatt, gjeldendeBruker?: UserInfo) => {
  return navAnsatt.id === gjeldendeBruker?.navident ? "deg" : navAnsatt.navn ?? navAnsatt.id;
};
