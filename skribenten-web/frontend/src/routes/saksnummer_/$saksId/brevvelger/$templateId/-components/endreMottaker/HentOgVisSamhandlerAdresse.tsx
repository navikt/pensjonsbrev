import { Skeleton, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";

import { hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { SamhandlerTypeCode } from "~/types/apiTypes";
import { SAMHANDLER_ENUM_TO_TEXT } from "~/types/nameMappings";

import type { TypeMottaker } from "./EndreMottakerUtils";
import OppsummeringAvValgtMottaker from "./OppsummeringAvValgtMottaker";

const HentOgVisSamhandlerAdresse = (properties: {
  id: string;
  typeMottaker: SamhandlerTypeCode | TypeMottaker;
  onTilbakeTilSøk: () => void;
  onBekreftNyMottaker: () => void;
  onCloseIntent: () => void;
}) => {
  const hentSamhandlerAdresseQuery = useQuery({
    queryKey: hentSamhandlerAdresse.queryKey(properties.id),
    queryFn: () => hentSamhandlerAdresse.queryFn({ idTSSEkstern: properties.id }),
  });

  if (hentSamhandlerAdresseQuery.isPending) {
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

  if (hentSamhandlerAdresseQuery.isError) {
    return <ApiError error={hentSamhandlerAdresseQuery.error} title="Fant ikke samhandleradresse" />;
  }

  return (
    <VStack gap="4">
      <OppsummeringAvValgtMottaker
        adresse={{
          navn: hentSamhandlerAdresseQuery.data?.navn,
          adresselinje1: hentSamhandlerAdresseQuery.data?.linje1,
          adresselinje2: hentSamhandlerAdresseQuery.data?.linje2,
          adresselinje3: hentSamhandlerAdresseQuery.data?.linje3,
          postnummer: hentSamhandlerAdresseQuery.data?.postnr,
          poststed: hentSamhandlerAdresseQuery.data?.poststed,
          land: hentSamhandlerAdresseQuery.data?.land,
        }}
        onAvbryt={properties.onCloseIntent}
        onBekreft={properties.onBekreftNyMottaker}
        onTilbake={{
          fn: properties.onTilbakeTilSøk,
          plassering: "bottom",
        }}
        type={SAMHANDLER_ENUM_TO_TEXT[properties.typeMottaker as SamhandlerTypeCode]}
      />
    </VStack>
  );
};

export default HentOgVisSamhandlerAdresse;
