import { css } from "@emotion/react";
import { BodyShort, Heading, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";

import { getKontaktAdresse, getNavn, hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { Adresse, KontaktAdresseResponse } from "~/types/apiTypes";
import { getAdresseTypeName } from "~/types/nameMappings";

import { Route } from "../../route";
import { erAdresseKontaktAdresse } from "./EndreMottakerUtils";

/*
 * Henter og viser adresse for mottaker. Dersom samhandlerId er satt, hentes samhandleradressen. Ellers hentes kontaktadressen.
 */
const HentOgVisAdresse = (properties: { sakId: string; samhandlerId?: string; showMottakerTitle?: boolean }) => {
  const hentSamhandlerAdresseQuery = useQuery({
    queryKey: hentSamhandlerAdresse.queryKey(properties.samhandlerId as string),
    queryFn: () => hentSamhandlerAdresse.queryFn({ idTSSEkstern: properties.samhandlerId as string }),
    enabled: !!properties.samhandlerId,
  });

  const adresseQuery = useQuery({
    queryKey: getKontaktAdresse.queryKey(properties.sakId),
    queryFn: () => getKontaktAdresse.queryFn(properties.sakId),
    enabled: !properties.samhandlerId,
  });

  return (
    <div
      css={css`
        h3 {
          margin-bottom: var(--a-spacing-1);
        }
      `}
    >
      {properties.showMottakerTitle && (
        <Heading level="3" size="xsmall">
          Mottaker
        </Heading>
      )}
      {!properties.samhandlerId && (
        <div>
          {adresseQuery.isSuccess && <MottakerAdresseOppsummering adresse={adresseQuery.data} erSamhandler={false} />}
          {adresseQuery.isPending && <BodyShort>Henter...</BodyShort>}
          {adresseQuery.error && <ApiError error={adresseQuery.error} title="Fant ikke adresse" />}
        </div>
      )}
      {properties.samhandlerId && (
        <div>
          {hentSamhandlerAdresseQuery.isPending && <BodyShort>Henter...</BodyShort>}
          {hentSamhandlerAdresseQuery.data && (
            <MottakerAdresseOppsummering adresse={hentSamhandlerAdresseQuery.data} erSamhandler />
          )}
          {hentSamhandlerAdresseQuery.error && (
            <ApiError error={hentSamhandlerAdresseQuery.error} title="Fant ikke adresse" />
          )}
        </div>
      )}
    </div>
  );
};

/**
 *
 * @param erSamhandler - burde settes dersom adressen er en Adresse, og ikke en KontaktAdresseResponse
 */
const MottakerAdresseOppsummering = (properties: {
  adresse: Adresse | KontaktAdresseResponse;
  erSamhandler?: boolean;
}) => {
  return (
    <div>
      {erAdresseKontaktAdresse(properties.adresse) ? (
        <ValgtKontaktAdresseOppsummering adresse={properties.adresse} />
      ) : (
        <ValgtAdresseOppsummering adresse={properties.adresse} erSamhandler={properties.erSamhandler ?? false} />
      )}
    </div>
  );
};

const ValgtKontaktAdresseOppsummering = (properties: { adresse: KontaktAdresseResponse }) => {
  const { saksId } = Route.useParams();
  const { data: navn } = useQuery({
    queryKey: getNavn.queryKey(saksId),
    queryFn: () => getNavn.queryFn(saksId),
  });

  return (
    <div>
      <div>
        {navn} ({getAdresseTypeName(properties.adresse.type)})
      </div>
      <VStack gap="0">
        {properties.adresse.adresselinjer.map((linje) => (
          <span key={linje}>{linje}</span>
        ))}
      </VStack>
    </div>
  );
};

const ValgtAdresseOppsummering = (properties: { adresse: Adresse; erSamhandler: boolean }) => {
  return (
    <div>
      <BodyShort>
        {properties.adresse.navn} {properties.erSamhandler && "(Samhandler)"}
      </BodyShort>
      <VStack gap="0">
        <BodyShort>{properties.adresse.linje1}</BodyShort>
        <BodyShort>
          {properties.adresse.postnr} {properties.adresse.poststed}{" "}
          {properties.adresse.land === "NOR" ? "" : `, ${properties.adresse.land}`}
        </BodyShort>
      </VStack>
    </div>
  );
};

export default HentOgVisAdresse;
