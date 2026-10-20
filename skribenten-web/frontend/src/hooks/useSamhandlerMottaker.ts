import { useQuery } from "@tanstack/react-query";

import { hentSamhandler, hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import { Identtype } from "~/components/endreMottaker/EndreMottakerUtils";

export function useSamhandlerMottaker(idTSSEkstern?: string) {
  return useQuery({
    queryKey: ["SAMHANDLER_MOTTAKER", idTSSEkstern],
    enabled: !!idTSSEkstern,
    queryFn: async () => {
      const id = idTSSEkstern!;
      const samhandler = await hentSamhandler.queryFn({ idTSSEkstern: id, hentDetaljert: false });
      if (!samhandler) {
        throw new Error("Fant ikke samhandler");
      }
      if (samhandler.idType === Identtype.NORSK_ORGNR && samhandler.offentligId.trim() !== "") {
        return { type: "organisasjon" as const, samhandler };
      }

      const adresse = await hentSamhandlerAdresse(id).queryFn();
      return { type: "adresse" as const, adresse };
    },
  });
}
