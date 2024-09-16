import { BodyShort, Label, VStack } from "@navikt/ds-react";

import HentOgVisAdresse from "~/routes/saksnummer_/$saksId/brevvelger/$templateId/-components/endreMottaker/HentOgVisAdresse";
import type { Mottaker } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";

const OppsummeringAvMottaker = (props: { saksId: string; mottaker: Nullable<Mottaker>; withTitle: boolean }) => {
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
      <BodyShort>{props.mottaker.navn}</BodyShort>
      {props.mottaker.adresselinje1 && <BodyShort>{props.mottaker.adresselinje1}</BodyShort>}
      {props.mottaker.adresselinje2 && <BodyShort>{props.mottaker.adresselinje2}</BodyShort>}
      {props.mottaker.adresselinje3 && <BodyShort>{props.mottaker.adresselinje3}</BodyShort>}
      <BodyShort>
        {props.mottaker.postnummer} {props.mottaker.poststed}
      </BodyShort>
      {props.mottaker.type === "UtenlandskAdresse" && <BodyShort>{props.mottaker.landkode}</BodyShort>}
    </VStack>
  );
};

export default OppsummeringAvMottaker;
