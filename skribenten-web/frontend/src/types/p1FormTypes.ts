export type P1PersonForm = {
  fornavn: string;
  etternavn: string;
  etternavnVedFoedsel: string;
  foedselsdato: string;
  adresselinje: string;
  poststed: string;
  postnummer: string;
  landkode: string;
};

export type P1InstitusjonForm = {
  institusjonsnavn: string;
  pin: string;
  saksnummer: string;
  vedtaksdato: string;
  land: string;
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

export type P1InnvilgetPensjonForm = {
  institusjon: P1InstitusjonForm;
  pensjonstype: Pensjonstype | null;
  datoFoersteUtbetaling: string;
  utbetalt: string;
  grunnlagInnvilget: GrunnlagInnvilget | null;
  reduksjonsgrunnlag: Reduksjonsgrunnlag | null;
  vurderingsperiode: string;
  adresseNyVurdering: string;
};

export type P1AvslaattPensjonForm = {
  institusjon: P1InstitusjonForm;
  pensjonstype: Pensjonstype | null;
  avslagsbegrunnelse: Avslagsbegrunnelse | null;
  vurderingsperiode: string;
  adresseNyVurdering: string;
};
export type P1UtfyllendeInstitusjonForm = {
  navn: string;
  adresselinje: string;
  poststed: string;
  postnummer: string;
  landkode: string;
  institusjonsID: string;
  faksnummer: string;
  telefonnummer: string;
  epost: string;
  dato: string;
};

export type P1RedigerbarForm = {
  innehaver: P1PersonForm;
  forsikrede: P1PersonForm;
  sakstype: "ALDER" | "UFORE" | "ETTERLATTE";
  innvilgedePensjoner: P1InnvilgetPensjonForm[];
  avslaattePensjoner: P1AvslaattPensjonForm[];
  utfyllendeInstitusjon: P1UtfyllendeInstitusjonForm;
};
