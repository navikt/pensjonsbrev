import type { Sakspart } from "~/types/brevbakerTypes";

export type SakspartViewProperties = {
  sakspart: Sakspart;
};
export const SakspartView = ({ sakspart }: SakspartViewProperties) => (
  <div>
    <div>
      <div>Saken gjelder:</div>
      <div>{sakspart.gjelderNavn}</div>
      <div>Fødselsnummer:</div>
      <div>{sakspart.gjelderFoedselsnummer}</div>
      <div>Saksnummer:</div>
      <div>{sakspart.saksnummer}</div>
    </div>
    <div>
      <div>{sakspart.dokumentDato}</div>
    </div>
  </div>
);
