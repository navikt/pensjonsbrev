import { css } from "@emotion/react";
import { BodyShort, Label, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";

import { getKontaktAdresse, getNavn, hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { Adresse, KontaktAdresseResponse } from "~/types/apiTypes";
import { getAdresseTypeName } from "~/types/nameMappings";
import { humanizeName } from "~/utils/stringUtils";

import { erAdresseKontaktAdresse } from "./EndreMottakerUtils";

/**
  En basic HentOgVis-komponent som henter og viser adresseinformasjon for en sak eller samhandler.
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
      {properties.showMottakerTitle && <Label size="small">Mottaker</Label>}
      {!properties.samhandlerId && (
        <div>
          {adresseQuery.isSuccess && (
            <MottakerAdresseOppsummering adresse={adresseQuery.data} erSamhandler={false} saksId={properties.sakId} />
          )}
          {adresseQuery.isPending && <BodyShort size="small">Henter...</BodyShort>}
          {adresseQuery.error && <ApiError error={adresseQuery.error} title="Fant ikke adresse" />}
        </div>
      )}
      {properties.samhandlerId && (
        <div>
          {hentSamhandlerAdresseQuery.isPending && <BodyShort size="small">Henter...</BodyShort>}
          {hentSamhandlerAdresseQuery.isSuccess && (
            <MottakerAdresseOppsummering
              adresse={hentSamhandlerAdresseQuery.data}
              erSamhandler
              saksId={properties.sakId}
            />
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
  saksId: string;
  adresse: Adresse | KontaktAdresseResponse;
  erSamhandler?: boolean;
}) => {
  return (
    <div>
      {erAdresseKontaktAdresse(properties.adresse) ? (
        <ValgtKontaktAdresseOppsummering adresse={properties.adresse} saksId={properties.saksId} />
      ) : (
        <ValgtAdresseOppsummering adresse={properties.adresse} erSamhandler={properties.erSamhandler ?? false} />
      )}
    </div>
  );
};

const ValgtKontaktAdresseOppsummering = (properties: { saksId: string; adresse: KontaktAdresseResponse }) => {
  const { data: navn } = useQuery({
    queryKey: getNavn.queryKey(properties.saksId),
    queryFn: () => getNavn.queryFn(properties.saksId),
  });

  return (
    <div>
      <BodyShort size="small">
        {navn ? humanizeName(navn) : undefined} ({getAdresseTypeName(properties.adresse.type)})
      </BodyShort>
      <VStack gap="0">
        {properties.adresse.adresselinjer.map((linje) => (
          <BodyShort key={linje} size="small">
            {humanizeName(linje)}
          </BodyShort>
        ))}
      </VStack>
    </div>
  );
};

const ValgtAdresseOppsummering = (properties: { adresse: Adresse; erSamhandler: boolean }) => {
  return (
    <div>
      <BodyShort size="small">
        {properties.adresse.navn} {properties.erSamhandler && "(Samhandler)"}
      </BodyShort>
      <VStack gap="0">
        <BodyShort size="small">{properties.adresse.linje1}</BodyShort>
        <BodyShort size="small">
          {properties.adresse.postnr} {properties.adresse.poststed}{" "}
          {properties.adresse.land === "NOR" ? "" : `, ${properties.adresse.land}`}
        </BodyShort>
      </VStack>
    </div>
  );
};

export default HentOgVisAdresse;
