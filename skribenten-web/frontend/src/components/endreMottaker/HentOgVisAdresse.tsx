import { BodyShort, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { getKontaktAdresse, hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import type { AdresseVisningTag } from "~/components/AdresseVisning";
import AdresseVisning from "~/components/AdresseVisning";
import { ApiError } from "~/components/ApiError";
import { useSakGjelderNavnFormatert } from "~/hooks/useSakGjelderNavn";
import { type Adresse, type KontaktAdresseResponse } from "~/types/apiTypes";
import { humanizeName } from "~/utils/stringUtils";

import { erAdresseKontaktAdresse } from "./EndreMottakerUtils";

function mapKontaktAdresseTags(adresse: KontaktAdresseResponse): AdresseVisningTag[] {
  if (adresse.type === "VERGE_SAMHANDLER_POSTADRESSE" || adresse.type === "VERGE_PERSON_POSTADRESSE") {
    return [{ label: "Verge", color: "brand-magenta" }];
  }
  return [{ label: "Bruker", color: "info" }];
}

function mapSamhandlerAdresseTags(adresse: Adresse): AdresseVisningTag[] {
  const tags: AdresseVisningTag[] = [{ label: "Samhandler", color: "warning" }];
  if (adresse.manueltAdressertTil === "BRUKER") {
    tags.push({ label: "Bruker", color: "info" });
  } else if (adresse.manueltAdressertTil === "ANNEN") {
    tags.push({ label: "Verge", color: "brand-magenta" });
  }
  return tags;
}

function mapSamhandlerAdresseLinjer(adresse: Adresse): string[] {
  const postLinje = [adresse.postnr, adresse.poststed].filter(Boolean).join(" ");
  const landSuffix = adresse.land && adresse.land !== "NOR" ? `, ${adresse.land}` : "";
  return [adresse.linje1, `${postLinje}${landSuffix}`].filter((l): l is string => !!l);
}

/**
  En basic HentOgVis-komponent som henter og viser adresseinformasjon for en sak eller samhandler.
 */
const HentOgVisAdresse = (properties: { sakId: string; samhandlerId?: string; showMottakerTitle?: boolean }) => {
  const samhandlerAdresse = useQuery({
    ...hentSamhandlerAdresse(properties.samhandlerId as string),
    enabled: !!properties.samhandlerId,
  });

  const adresseQuery = useQuery({
    ...getKontaktAdresse(properties.sakId),
    enabled: !properties.samhandlerId,
  });

  return (
    <VStack>
      {!properties.samhandlerId && (
        <>
          {adresseQuery.isPending && <BodyShort size="small">Henter...</BodyShort>}
          {adresseQuery.error && <ApiError error={adresseQuery.error} title="Fant ikke adresse" />}
          {adresseQuery.isSuccess && (
            <ResolvedAdresse
              adresse={adresseQuery.data}
              erSamhandler={false}
              saksId={properties.sakId}
              withTitle={properties.showMottakerTitle}
            />
          )}
        </>
      )}
      {properties.samhandlerId && (
        <>
          {samhandlerAdresse.isPending && <BodyShort size="small">Henter...</BodyShort>}
          {samhandlerAdresse.error && <ApiError error={samhandlerAdresse.error} title="Fant ikke adresse" />}
          {samhandlerAdresse.isSuccess && (
            <ResolvedAdresse
              adresse={samhandlerAdresse.data}
              erSamhandler
              saksId={properties.sakId}
              withTitle={properties.showMottakerTitle}
            />
          )}
        </>
      )}
    </VStack>
  );
};

const ResolvedAdresse = (properties: {
  saksId: string;
  adresse: Adresse | KontaktAdresseResponse;
  erSamhandler: boolean;
  withTitle?: boolean;
}) => {
  const sakGjelderNavn = useSakGjelderNavnFormatert({ saksId: properties.saksId });

  if (erAdresseKontaktAdresse(properties.adresse)) {
    return (
      <AdresseVisning
        adresselinjer={properties.adresse.adresselinjer.map(humanizeName)}
        navn={sakGjelderNavn ?? ""}
        tags={mapKontaktAdresseTags(properties.adresse)}
        withTitle={properties.withTitle}
      />
    );
  }

  return (
    <AdresseVisning
      adresselinjer={mapSamhandlerAdresseLinjer(properties.adresse)}
      navn={properties.adresse.navn}
      tags={properties.erSamhandler ? mapSamhandlerAdresseTags(properties.adresse) : undefined}
      withTitle={properties.withTitle}
    />
  );
};

export default HentOgVisAdresse;
