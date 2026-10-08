import { useQuery } from "@tanstack/react-query";

import { getFeatureToggle, hentSamhandler, hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import { Identtype } from "~/components/endreMottaker/EndreMottakerUtils";

export function useSamhandlerMottaker(idTSSEkstern?: string) {
  const feature = useQuery({
    ...getFeatureToggle("samhandlerOrgnummer"),
    enabled: !!idTSSEkstern,
  });
  const visOrgnummer = feature.data?.enabled === true;

  return useQuery({
    queryKey: ["SAMHANDLER_MOTTAKER", idTSSEkstern, visOrgnummer],
    enabled: !!idTSSEkstern && !feature.isPending,
    queryFn: async () => {
      const id = idTSSEkstern!;
      if (visOrgnummer) {
        const samhandler = await hentSamhandler.queryFn({ idTSSEkstern: id, hentDetaljert: false });
        if (!samhandler) {
          throw new Error("Fant ikke samhandler");
        }
        if (samhandler.idType === Identtype.NORSK_ORGNR) {
          return { type: "organisasjon" as const, samhandler };
        }
      }

      const adresse = await hentSamhandlerAdresse(id).queryFn();
      return { type: "adresse" as const, adresse };
    },
  });
}
