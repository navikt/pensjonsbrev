import { BodyShort, HStack, Label, Tag, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";

import { getKontaktAdresse, hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { useSakGjelderNavnFormatert } from "~/hooks/useSakGjelderNavn";
import type { Adresse, KontaktAdresseResponse } from "~/types/apiTypes";
import { humanizeName } from "~/utils/stringUtils";

import { erAdresseKontaktAdresse } from "./EndreMottakerUtils";

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
      {properties.showMottakerTitle && <Label size="small">Mottaker</Label>}
      {!properties.samhandlerId && (
        <>
          {adresseQuery.isPending && <BodyShort size="small">Henter...</BodyShort>}
          {adresseQuery.error && <ApiError error={adresseQuery.error} title="Fant ikke adresse" />}
          {adresseQuery.isSuccess && (
            <MottakerAdresseOppsummering adresse={adresseQuery.data} erSamhandler={false} saksId={properties.sakId} />
          )}
        </>
      )}
      {properties.samhandlerId && (
        <>
          {samhandlerAdresse.isPending && <BodyShort size="small">Henter...</BodyShort>}
          {samhandlerAdresse.error && <ApiError error={samhandlerAdresse.error} title="Fant ikke adresse" />}
          {samhandlerAdresse.isSuccess && (
            <MottakerAdresseOppsummering adresse={samhandlerAdresse.data} erSamhandler saksId={properties.sakId} />
          )}
        </>
      )}
    </VStack>
  );
};

/**
 *
 * @param properties - erSamhandler burde settes dersom adressen er en Adresse, og ikke en KontaktAdresseResponse
 */
const MottakerAdresseOppsummering = (properties: {
  saksId: string;
  adresse: Adresse | KontaktAdresseResponse;
  erSamhandler?: boolean;
}) => {
  return (
    <>
      {erAdresseKontaktAdresse(properties.adresse) ? (
        <ValgtKontaktAdresseOppsummering adresse={properties.adresse} saksId={properties.saksId} />
      ) : (
        <ValgtAdresseOppsummering adresse={properties.adresse} erSamhandler={properties.erSamhandler ?? false} />
      )}
    </>
  );
};

const ValgtKontaktAdresseOppsummering = (properties: { saksId: string; adresse: KontaktAdresseResponse }) => {
  const navn = useSakGjelderNavnFormatert(properties);

  return (
    <>
      {properties.adresse &&
      (properties.adresse.type === "VERGE_SAMHANDLER_POSTADRESSE" ||
        properties.adresse.type === "VERGE_PERSON_POSTADRESSE") ? (
        <HStack>
          <Tag data-color="brand-magenta" size="xsmall" variant="strong">
            Verge
          </Tag>
        </HStack>
      ) : (
        <HStack>
          <Tag data-color="info" size="xsmall" variant="strong">
            Bruker
          </Tag>
        </HStack>
      )}
      <BodyShort size="small">{navn}</BodyShort>
      <VStack>
        {properties.adresse.adresselinjer.map((linje) => (
          <BodyShort key={linje} size="small">
            {humanizeName(linje)}
          </BodyShort>
        ))}
      </VStack>
    </>
  );
};

const ValgtAdresseOppsummering = (properties: { adresse: Adresse; erSamhandler: boolean }) => {
  return (
    <>
      {properties.erSamhandler && (
        <HStack>
          <Tag data-color="warning" size="xsmall" variant="strong">
            Samhandler
          </Tag>
          {properties.adresse.manueltAdressertTil === "BRUKER" && (
            <Tag data-color="info" size="xsmall" variant="strong">
              Bruker
            </Tag>
          )}
          {properties.adresse.manueltAdressertTil === "ANNEN" && (
            <Tag data-color="brand-magenta" size="xsmall" variant="strong">
              Verge
            </Tag>
          )}
        </HStack>
      )}
      <BodyShort size="small">{properties.adresse.navn}</BodyShort>
      <VStack>
        <BodyShort size="small">{properties.adresse.linje1}</BodyShort>
        <BodyShort size="small">
          {properties.adresse.postnr} {properties.adresse.poststed}{" "}
          {properties.adresse.land === "NOR" ? "" : `, ${properties.adresse.land}`}
        </BodyShort>
      </VStack>
    </>
  );
};

export default HentOgVisAdresse;
