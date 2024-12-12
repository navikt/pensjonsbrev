import { Distribusjonstype } from "~/types/brev";

export const distribusjonstypeTilText = (d: Distribusjonstype) => {
  switch (d) {
    case Distribusjonstype.SENTRALPRINT: {
      return "Sentral print";
    }
    case Distribusjonstype.LOKALPRINT: {
      return "Lokal print";
    }
  }
};
