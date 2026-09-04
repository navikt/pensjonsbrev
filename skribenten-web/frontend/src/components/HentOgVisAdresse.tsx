import { HStack, Skeleton, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";

import { getKontaktAdresse, hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import AdresseVisning, { type AdresseVisningTag } from "~/components/AdresseVisning";
import { ApiError } from "~/components/ApiError";
import { useSakGjelderNavnFormatert } from "~/hooks/useSakGjelderNavn";
import { type KontaktAdresseResponse, type SamhandlerPostadresse } from "~/types/apiTypes";
import {
  erAdresseKontaktAdresse,
  MOTTAKER_TAG_BRUKER,
  MOTTAKER_TAG_SAMHANDLER,
  MOTTAKER_TAG_VERGE,
} from "~/utils/AdresseUtils";
import { humanizeName } from "~/utils/stringUtils";

function mapKontaktAdresseTags(adresse: KontaktAdresseResponse): AdresseVisningTag[] {
  if (adresse.type === "VERGE_SAMHANDLER_POSTADRESSE" || adresse.type === "VERGE_PERSON_POSTADRESSE") {
    return [MOTTAKER_TAG_VERGE];
  }
  return [MOTTAKER_TAG_BRUKER];
}

function mapSamhandlerAdresseLinjer(adresse: SamhandlerPostadresse): string[] {
  const postLinje = [adresse.postnr, adresse.poststed].filter(Boolean).join(" ");
  const landSuffix = adresse.land && adresse.land !== "NOR" ? `, ${adresse.land}` : "";
  return [adresse.linje1, `${postLinje}${landSuffix}`].filter((l): l is string => !!l);
}

// Speiler formen til AdresseVisning, slik at innholdet ikke kollapser til én linje mens adressen
// hentes og deretter spretter ut igjen når den er på plass.
const AdresseSkeleton = ({ withTitle }: { withTitle?: boolean }) => (
  <VStack>
    {withTitle && <Skeleton variant="text" width="30%" />}
    <Skeleton variant="text" width="60%" />
    <Skeleton variant="text" width="75%" />
    <Skeleton variant="text" width="45%" />
    <HStack marginBlock="space-4 space-0">
      <Skeleton height={20} variant="rounded" width={72} />
    </HStack>
  </VStack>
);

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
          {adresseQuery.isPending && <AdresseSkeleton withTitle={properties.showMottakerTitle} />}
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
          {samhandlerAdresse.isPending && <AdresseSkeleton withTitle={properties.showMottakerTitle} />}
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

const ResolvedKontaktAdresse = (properties: {
  saksId: string;
  adresse: KontaktAdresseResponse;
  withTitle?: boolean;
}) => {
  const sakGjelderNavn = useSakGjelderNavnFormatert({ saksId: properties.saksId });

  return (
    <AdresseVisning
      adresselinjer={properties.adresse.adresselinjer.map(humanizeName)}
      navn={sakGjelderNavn ?? ""}
      tags={mapKontaktAdresseTags(properties.adresse)}
      withTitle={properties.withTitle}
    />
  );
};

const ResolvedAdresse = (properties: {
  saksId: string;
  adresse: SamhandlerPostadresse | KontaktAdresseResponse;
  erSamhandler: boolean;
  withTitle?: boolean;
}) => {
  if (erAdresseKontaktAdresse(properties.adresse)) {
    return (
      <ResolvedKontaktAdresse
        adresse={properties.adresse}
        saksId={properties.saksId}
        withTitle={properties.withTitle}
      />
    );
  }

  return (
    <AdresseVisning
      adresselinjer={mapSamhandlerAdresseLinjer(properties.adresse)}
      navn={properties.adresse.navn}
      tags={properties.erSamhandler ? [MOTTAKER_TAG_SAMHANDLER] : undefined}
      withTitle={properties.withTitle}
    />
  );
};

export default HentOgVisAdresse;
