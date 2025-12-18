import type { P1RedigerbarForm } from "~/types/p1FormTypes";

export const emptyInnvilgetRow = (): P1RedigerbarForm["innvilgedePensjoner"][number] => ({
  institusjon: {
    institusjonsnavn: "",
    pin: "",
    saksnummer: "",
    vedtaksdato: undefined,
    land: "",
  },
  pensjonstype: null,
  datoFoersteUtbetaling: undefined,
  utbetalt: "",
  grunnlagInnvilget: null,
  reduksjonsgrunnlag: null,
  vurderingsperiode: "",
  adresseNyVurdering: "",
});

export const emptyAvslaattRow = (): P1RedigerbarForm["avslaattePensjoner"][number] => ({
  institusjon: {
    institusjonsnavn: "",
    pin: "",
    saksnummer: "",
    vedtaksdato: undefined,
    land: "",
  },
  pensjonstype: null,
  avslagsbegrunnelse: null,
  vurderingsperiode: "",
  adresseNyVurdering: "",
});
