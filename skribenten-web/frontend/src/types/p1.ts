export type P1Person = {
  fornavn: string | null;
  etternavn: string | null;
  etternavnVedFoedsel: string | null;
  foedselsdato: string | null;
  adresselinje: string | null;
  poststed: string | null;
  postnummer: string | null;
  landkode: string | null;
};

export type P1Institusjon = {
  institusjonsnavn: string | null;
  pin: string | null;
  saksnummer: string | null;
  datoForVedtak: string | null;
  land: string | null;
};

export type Pensjonstype = "Alder" | "Ufoere" | "Etterlatte";
export type GrunnlagInnvilget = "IHenholdTilNasjonalLovgivning" | "ProRata" | "MindreEnnEttAar";
export type Reduksjonsgrunnlag = "PaaGrunnAvAndreYtelserEllerAnnenInntekt" | "PaaGrunnAvOverlappendeGodskrevnePerioder";

export type Avslagsbegrunnelse =
  | "IngenOpptjeningsperioder"
  | "OpptjeningsperiodePaaMindreEnnEttAar"
  | "KravTilKvalifiseringsperiodeEllerAndreKvalifiseringskravErIkkeOppfylt"
  | "VilkaarOmUfoerhetErIkkeOppfylt"
  | "InntektstakErOverskredet"
  | "PensjonsalderErIkkeNaadd"
  | "AndreAarsaker";

export type P1InnvilgetPensjon = {
  institusjon: P1Institusjon | null;
  pensjonstype: Pensjonstype | null;
  datoFoersteUtbetaling: string | null;
  utbetalt: string | null;
  grunnlagInnvilget: GrunnlagInnvilget | null;
  reduksjonsgrunnlag: Reduksjonsgrunnlag | null;
  vurderingsperiode: string | null;
  adresseNyVurdering: string | null;
};

export type P1AvslaattPensjon = {
  institusjon: P1Institusjon | null;
  pensjonstype: Pensjonstype | null;
  avslagsbegrunnelse: Avslagsbegrunnelse | null;
  vurderingsperiode: string | null;
  adresseNyVurdering: string | null;
};

export type P1UtfyllendeInstitusjon = {
  navn: string;
  adresselinje: string;
  poststed: string;
  postnummer: string;
  landkode: string;
  institusjonsID: string | null;
  faksnummer: string | null;
  telefonnummer: string | null;
  epost: string | null;
  dato: string | null;
};

export type P1Redigerbar = {
  innehaver: P1Person;
  forsikrede: P1Person;
  sakstype: string;
  innvilgedePensjoner: P1InnvilgetPensjon[];
  avslaattePensjoner: P1AvslaattPensjon[];
  utfyllendeInstitusjon: P1UtfyllendeInstitusjon;
};
