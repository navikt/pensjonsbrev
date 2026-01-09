export const PENSJONSTYPE_OPTIONS = [
  { value: "Alder", label: "Alder" },
  { value: "Ufoere", label: "Uføre" },
  { value: "Etterlatte", label: "Etterlatte" },
] as const;

export const GRUNNLAG_INNVILGET_OPTIONS = [
  { value: "IHenholdTilNasjonalLovgivning", label: "I henhold til nasjonal lovgivning" },
  { value: "ProRata", label: "Pro rata" },
  { value: "MindreEnnEttAar", label: "Mindre enn ett år" },
  { value: "IKKE_RELEVANT", label: "Ikke relevant" },
] as const;

export const REDUKSJONSGRUNNLAG_OPTIONS = [
  { value: "PaaGrunnAvAndreYtelserEllerAnnenInntekt", label: "Andre ytelser/inntekt" },
  { value: "PaaGrunnAvOverlappendeGodskrevnePerioder", label: "Overlappende perioder" },
  { value: "IKKE_REDUSERT", label: "Ikke redusert" },
] as const;

export const AVSLAGSBEGRUNNELSE_OPTIONS = [
  { value: "IngenOpptjeningsperioder", label: "Ingen opptjeningsperioder" },
  { value: "OpptjeningsperiodePaaMindreEnnEttAar", label: "Opptjeningsperiode < 1 år" },
  {
    value: "KravTilKvalifiseringsperiodeEllerAndreKvalifiseringskravErIkkeOppfylt",
    label: "Kvalifiseringskrav ikke oppfylt",
  },
  { value: "VilkaarOmUfoerhetErIkkeOppfylt", label: "Vilkår om uførhet ikke oppfylt" },
  { value: "InntektstakErOverskredet", label: "Inntektstak overskredet" },
  { value: "PensjonsalderErIkkeNaadd", label: "Pensjonsalder ikke nådd" },
  { value: "AndreAarsaker", label: "Andre årsaker" },
] as const;
