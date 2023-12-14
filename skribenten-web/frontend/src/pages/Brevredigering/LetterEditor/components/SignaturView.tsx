import type { Signatur } from "~/types/brevbakerTypes";

export type SignaturViewProperties = {
  signatur: Signatur;
};

const Saksbehandler = ({ rolleTekst, navn }: { rolleTekst: string; navn?: string }) => {
  return navn ? (
    <div>
      <div>{navn}</div>
      <div>{rolleTekst}</div>
    </div>
  ) : null;
};

export const SignaturView = ({ signatur }: SignaturViewProperties) => (
  <div>
    <div>{signatur.hilsenTekst}</div>
    <div>
      <Saksbehandler navn={signatur.saksbehandlerNavn} rolleTekst={signatur.saksbehandlerRolleTekst} />
      <Saksbehandler navn={signatur.attesterendeSaksbehandlerNavn} rolleTekst={signatur.saksbehandlerRolleTekst} />
    </div>
    <div>{signatur.navAvsenderEnhet}</div>
  </div>
);
