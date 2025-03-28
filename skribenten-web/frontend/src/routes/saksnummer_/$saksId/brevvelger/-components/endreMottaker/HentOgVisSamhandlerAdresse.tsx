import { Skeleton, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import type { AxiosError } from "axios";

import { hentSamhandlerAdresseQuery } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { SamhandlerTypeCode } from "~/types/apiTypes";
import { SAMHANDLER_ENUM_TO_TEXT } from "~/types/nameMappings";
import type { Nullable } from "~/types/Nullable";

import OppsummeringAvValgtMottaker from "./OppsummeringAvValgtMottaker";

/**
    En special case hentOgVis-komponent av samhandler som henter og viser i contexten av endre mottaker
 */
const HentOgVisSamhandlerAdresse = (properties: {
  id: string;
  samhandlerType: SamhandlerTypeCode;
  onTilbakeTilSøk: () => void;
  onBekreftNyMottaker: () => void;
  error: Nullable<AxiosError>;
  isPending: Nullable<boolean>;
  onCloseIntent: () => void;
}) => {
  const samhandlerAdresse = useQuery(hentSamhandlerAdresseQuery(properties.id));

  if (samhandlerAdresse.isPending) {
    return (
      <VStack gap="4">
        <Skeleton height={30} variant="rectangle" width="100%" />
        <Skeleton height={30} variant="rectangle" width="100%" />
        <Skeleton height={30} variant="rectangle" width="100%" />
        <Skeleton height={30} variant="rectangle" width="100%" />
        <Skeleton height={30} variant="rectangle" width="100%" />
      </VStack>
    );
  }

  if (samhandlerAdresse.isError) {
    return <ApiError error={samhandlerAdresse.error} title="Fant ikke samhandleradresse" />;
  }

  return (
    <VStack gap="4">
      <OppsummeringAvValgtMottaker
        adresse={{
          navn: samhandlerAdresse.data?.navn,
          linje1: samhandlerAdresse.data?.linje1,
          linje2: samhandlerAdresse.data?.linje2,
          linje3: samhandlerAdresse.data?.linje3,
          postnr: samhandlerAdresse.data?.postnr,
          poststed: samhandlerAdresse.data?.poststed,
          land: samhandlerAdresse.data?.land,
        }}
        error={properties.error}
        isPending={properties.isPending}
        onAvbryt={properties.onCloseIntent}
        onBekreft={properties.onBekreftNyMottaker}
        onTilbake={{
          fn: properties.onTilbakeTilSøk,
          plassering: "bottom",
        }}
        samhandlerType={SAMHANDLER_ENUM_TO_TEXT[properties.samhandlerType]}
      />
    </VStack>
  );
};

export default HentOgVisSamhandlerAdresse;
