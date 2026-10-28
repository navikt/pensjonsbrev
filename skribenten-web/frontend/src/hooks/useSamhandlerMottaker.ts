import { useQuery } from "@tanstack/react-query";

import { hentSamhandler, hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import { type HentSamhandlerResponseDtoSuccess, type SamhandlerPostadresse } from "~/types/apiTypes";

type SamhandlerMottaker =
  | {
      samhandler: HentSamhandlerResponseDtoSuccess & {
        organisasjonsnummer: NonNullable<HentSamhandlerResponseDtoSuccess["organisasjonsnummer"]>;
      };
      adresse?: never;
    }
  | {
      samhandler: HentSamhandlerResponseDtoSuccess & { organisasjonsnummer?: null };
      adresse: SamhandlerPostadresse;
    };

export function useSamhandlerMottaker(idTSSEkstern?: string) {
  return useQuery({
    queryKey: ["SAMHANDLER_MOTTAKER", idTSSEkstern],
    enabled: !!idTSSEkstern,
    queryFn: async (): Promise<SamhandlerMottaker> => {
      const id = idTSSEkstern!;
      const samhandler = await hentSamhandler.queryFn({ idTSSEkstern: id, hentDetaljert: false });
      if (!samhandler) {
        throw new Error("Fant ikke samhandler");
      }
      if (samhandler.organisasjonsnummer) {
        return {
          samhandler: { ...samhandler, organisasjonsnummer: samhandler.organisasjonsnummer },
        };
      }

      const adresse = await hentSamhandlerAdresse(id).queryFn();
      return { samhandler: { ...samhandler, organisasjonsnummer: null }, adresse };
    },
  });
}
