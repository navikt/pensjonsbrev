import { type UserInfo } from "~/api/bff-endpoints";
import { type BrevInfo, type BrevStatus, type NavAnsatt } from "~/types/brev";

export const brevStatusTypeToTextAndTagVariant = (status: BrevStatus, gjeldendeBruker?: UserInfo) => {
  switch (status.type) {
    case "Kladd": {
      return { color: "warning" as const, text: "Kladd" };
    }

    case "UnderRedigering": {
      return {
        color: "neutral" as const,
        text: `Redigeres av ${forkortetSaksbehandlernavn(status.redigeresAv, gjeldendeBruker)}`,
      };
    }

    case "Attestering": {
      return { color: "meta-lime" as const, text: "Klar til Attestering" };
    }

    case "Klar": {
      return { color: "success" as const, text: "Klar til sending" };
    }

    case "Arkivert": {
      return { color: "danger" as const, text: "Kunne ikke sende brev" };
    }
  }
};

export const forkortetSaksbehandlernavn = (navAnsatt: NavAnsatt, gjeldendeBruker?: UserInfo) => {
  return navAnsatt.id === gjeldendeBruker?.navident ? "deg" : (navAnsatt.navn ?? navAnsatt.id);
};

const sorteringsRekkefølge = {
  Arkivert: 1,
  Kladd: 2,
  UnderRedigering: 3,
  Klar: 4,
  Attestering: 5,
};

export const sortBrev = (brev: BrevInfo[]): BrevInfo[] =>
  brev.toSorted((a, b) => {
    if (sorteringsRekkefølge[a.status.type] !== sorteringsRekkefølge[b.status.type]) {
      return sorteringsRekkefølge[a.status.type] - sorteringsRekkefølge[b.status.type];
    }

    return new Date(a.sistredigert).getTime() - new Date(b.sistredigert).getTime();
  });
