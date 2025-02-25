import { BodyShort, Label, VStack } from "@navikt/ds-react";

import { useLandDataContext } from "~/context/LandDataContext";
import HentOgVisAdresse from "~/routes/saksnummer_/$saksId/brevvelger/-components/endreMottaker/HentOgVisAdresse";
import type { Mottaker } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { getCountryNameByKode } from "~/utils/countryUtils";

const OppsummeringAvMottaker = (props: { saksId: string; mottaker: Nullable<Mottaker>; withTitle: boolean }) => {
  const { data: landData } = useLandDataContext();

  if (props.mottaker === null || props.mottaker.type === "Samhandler") {
    return (
      <HentOgVisAdresse
        sakId={props.saksId}
        samhandlerId={props.mottaker?.type === "Samhandler" ? props.mottaker.tssId : undefined}
        showMottakerTitle={props.withTitle}
      />
    );
  }

  return (
    <VStack>
      {props.withTitle && <Label size="small">Mottaker</Label>}
      <BodyShort size="small">{props.mottaker.navn}</BodyShort>
      {props.mottaker.adresselinje1 && <BodyShort size="small">{props.mottaker.adresselinje1}</BodyShort>}
      {props.mottaker.adresselinje2 && <BodyShort size="small">{props.mottaker.adresselinje2}</BodyShort>}
      {props.mottaker.adresselinje3 && <BodyShort size="small">{props.mottaker.adresselinje3}</BodyShort>}
      {(props.mottaker.postnummer || props.mottaker.poststed) && (
        <BodyShort size="small">
          {props.mottaker.postnummer ?? ""} {props.mottaker.poststed ?? ""}
        </BodyShort>
      )}
      {props.mottaker.type === "UtenlandskAdresse" && (
        <BodyShort size="small">{getCountryNameByKode(props.mottaker.landkode, landData || [])}</BodyShort>
      )}
    </VStack>
  );
};

export default OppsummeringAvMottaker;
