package no.nav.pensjon.brev.alder.maler.afp.fraser

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

/**
 * Per-scenario forklaringer om hvilke nye inntektsopplysninger som er lagt fram
 * for AFP etteroppgjør (offentlig sektor / Statens pensjonskasse). Fem
 * gjensidig utelukkende scenarier som deles mellom
 * [no.nav.pensjon.brev.alder.maler.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAuto]
 * (PE_AF_04_103) — ikke-tilbakekreving — og PE_AF_04_104 — tilbakekreving.
 *
 * Hver scenario-frase består av to paragrafer: selve forklaringen og en
 * paret «Den faktiske arbeidsinntekten …»-paragraf som forteller hvilket
 * beløp som er lagt til grunn som inntekt i AFP-perioden.
 */
object AfpEtteroppgjoerForklaringer {

    /**
     * Bruker har lagt fram nye opplysninger, men dokumentasjonen gir ikke
     * tilstrekkelig grunnlag for å endre tidligere beregnet arbeidsinntekt
     * før uttak av AFP. Ingen paret «Den faktiske arbeidsinntekten …».
     *
     * Tilsvarer 102.INGEN_NYE_OPPLYSNINGER og 104-scenariet
     * `INGEN_OVERSTYRING_UTTAK_I_AARET`.
     */
    data class IngenNyeOpplysningerOmEndretInntektFoerUttak(
        val ifu: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du har lagt fram nye opplysninger om inntektsforholdene dine. " +
                            "Dokumentasjonen som foreligger gir ikke tilstrekkelig grunnlag for å " +
                            "endre tidligere beregnet arbeidsinntekt før uttak av AFP. " +
                            "Arbeidsinntekten din blir etter dette satt til " + ifu.format() +
                            " i samsvar med den tidligere beregningen. Dette beløpet skal holdes " +
                            "utenfor etteroppgjøret for " + oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Du har lagt fram nye opplysningar om inntektsforholda dine. " +
                            "Dokumentasjonen som ligg føre, gir ikkje tilstrekkeleg grunnlag for å " +
                            "endre tidlegare berekna arbeidsinntekt før uttak av AFP. " +
                            "Arbeidsinntekta di blir etter dette sett til " + ifu.format() +
                            " i samsvar med den tidlegare berekninga. Dette beløpet skal haldast " +
                            "utanfor etteroppgjeret for " + oppgjoersAar.format() + "."
                    },
                )
            }
        }
    }

    /**
     * Paret «Den faktiske arbeidsinntekten …»-paragraf for scenariene der
     * bare IFU er registrert/overstyrt. Differansen utgjør PGI − IFU.
     */
    data class DenFaktiskeArbeidsinntektenKunIfu(
        val iiap: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
        val pgi: Expression<Kroner>,
        val ifu: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP, er " +
                            iiap.format() + ". Dette beløpet utgjør differansen mellom din " +
                            "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " +
                            pgi.format() + " og arbeidsinntekten din før uttak av AFP på " +
                            ifu.format() + "."
                    },
                    nynorsk {
                        +"Den faktiske arbeidsinntekta i den perioden du har fått AFP, er " +
                            iiap.format() + ". Dette beløpet utgjer differansen mellom den " +
                            "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " +
                            pgi.format() + " og arbeidsinntekta di før uttak av AFP på " +
                            ifu.format() + "."
                    },
                )
            }
        }
    }

    /**
     * IFU oppjustert, uttak skjedde i etteroppgjørsåret. Selve forklaringen
     * (paret med [DenFaktiskeArbeidsinntektenKunIfu]).
     *
     * Tilsvarer 102.IFU_UTTAK_I_AARET og 104-scenariet
     * `IFU_OVERSTYRT_UTTAK_I_AARET`.
     */
    data class IfuOverstyrtUttakIAaret(
        val ifu: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                            "opplysninger har du hatt en høyere arbeidsinntekt før du tok ut AFP enn " +
                            "det som er beregnet tidligere. Arbeidsinntekten din er endret i samsvar " +
                            "med disse opplysningene til " + ifu.format() + ". Dette beløpet skal " +
                            "holdes utenfor etteroppgjøret for " + oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                            "opplysningar har du hatt ei høgare arbeidsinntekt før du tok ut AFP, enn " +
                            "det som er berekna tidlegare. Arbeidsinntekta di er endra i samsvar med " +
                            "desse opplysningane til " + ifu.format() + ". Dette beløpet skal haldast " +
                            "utanfor etteroppgjeret for " + oppgjoersAar.format() + "."
                    },
                )
            }
        }
    }

    /**
     * IFU oppjustert, uttak skjedde før etteroppgjørsåret — inntekten
     * stammer fra tidligere arbeid. Paret med [DenFaktiskeArbeidsinntektenKunIfu].
     *
     * Tilsvarer 102.IFU_UTTAK_FOER_AARET og 104-scenariet
     * `IFU_OVERSTYRT_HEL_AFP`.
     */
    data class IfuOverstyrtUttakFoerAaret(
        val ifu: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                            "opplysninger har du hatt pensjonsgivende inntekt som er opptjent før " +
                            "uttak av AFP. Arbeidsinntekten som stammer fra tidligere arbeid, er " +
                            "satt til " + ifu.format() + ". Dette beløpet skal holdes utenfor " +
                            "etteroppgjøret av AFP for " + oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                            "opplysningar har du hatt pensjonsgivande inntekt som er opptent før " +
                            "uttak av AFP. Arbeidsinntekta som stammar frå tidlegare arbeid, er " +
                            "sett til " + ifu.format() + ". Dette beløpet skal haldast utanfor " +
                            "etteroppgjeret av AFP for " + oppgjoersAar.format() + "."
                    },
                )
            }
        }
    }

    /**
     * Både IFU (før uttak) og IEO (etter opphør) er oppjustert. Selve
     * forklaringen + paret «Den faktiske arbeidsinntekten …» som her
     * regnes som PGI − IFU − IEO.
     *
     * Tilsvarer 102.IFU_OG_IEO_REGISTRERT og 104-scenariet
     * `IFU_OG_IEO_OVERSTYRT`.
     */
    data class IfuOgIeoOverstyrt(
        val ifu: Expression<Kroner>,
        val ieo: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                            "opplysninger har du hatt en høyere arbeidsinntekt enn tidligere beregnet " +
                            "i perioder hvor du ikke samtidig har hatt rett til AFP. " +
                            "Arbeidsinntekten din for denne perioden er endret i samsvar med disse " +
                            "opplysningene til henholdsvis " + ifu.format() + " for perioden før " +
                            "uttak av AFP og " + ieo.format() + " for perioden etter opphør av AFP. " +
                            "Disse beløpene skal holdes utenfor etteroppgjøret for " +
                            oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                            "opplysningar har du hatt ei høgare arbeidsinntekt enn tidlegare berekna " +
                            "i periodar der du ikkje samtidig har hatt rett til AFP. Arbeidsinntekta " +
                            "di for denne perioden er endra i samsvar med desse opplysningane til " +
                            "høvesvis " + ifu.format() + " for perioden før uttak av AFP og " +
                            ieo.format() + " for perioden etter opphør av AFP tok slutt. Desse " +
                            "beløpa skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() +
                            "."
                    },
                )
            }
        }
    }

    /** Paret «Den faktiske arbeidsinntekten …» for [IfuOgIeoOverstyrt]. */
    data class DenFaktiskeArbeidsinntektenIfuOgIeo(
        val iiap: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
        val pgi: Expression<Kroner>,
        val ifu: Expression<Kroner>,
        val ieo: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP, er " +
                            iiap.format() + ". Dette beløpet utgjør differansen mellom din " +
                            "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " +
                            pgi.format() + " og summen av arbeidsinntektene før uttak av AFP på " +
                            ifu.format() + " og etter opphør av AFP på " + ieo.format() + "."
                    },
                    nynorsk {
                        +"Den faktiske arbeidsinntekta i den perioden du har fått AFP, er " +
                            iiap.format() + ". Dette beløpet utgjer differansen mellom den " +
                            "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " +
                            pgi.format() + " og summen av arbeidsinntektene før uttak av AFP på " +
                            ifu.format() + " og etter at AFP tok slutt, på " + ieo.format() + "."
                    },
                )
            }
        }
    }

    /**
     * Bare IEO oppjustert — inntekten kom etter at AFP tok slutt. Selve
     * forklaringen + paret «Den faktiske arbeidsinntekten …» (PGI − IEO).
     *
     * Tilsvarer 102.KUN_IEO_REGISTRERT og 104-scenariet `KUN_IEO_OVERSTYRT`.
     */
    data class KunIeoOverstyrt(
        val ieo: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                            "opplysninger har du hatt en høyere arbeidsinntekt enn tidligere beregnet " +
                            "etter opphøret av AFP. Arbeidsinntekten din for denne perioden er " +
                            "endret i samsvar med disse opplysningene til " + ieo.format() + ". " +
                            "Dette beløpet skal holdes utenfor etteroppgjøret for " +
                            oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                            "opplysningar har du hatt ei høgare arbeidsinntekt enn tidlegare berekna " +
                            "etter at AFP tok slutt. Arbeidsinntekta di for denne perioden er endra " +
                            "i samsvar med desse opplysningane til " + ieo.format() + ". Dette " +
                            "beløpet skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() +
                            "."
                    },
                )
            }
        }
    }

    /** Paret «Den faktiske arbeidsinntekten …» for [KunIeoOverstyrt]. */
    data class DenFaktiskeArbeidsinntektenKunIeo(
        val iiap: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
        val pgi: Expression<Kroner>,
        val ieo: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP, er " +
                            iiap.format() + ". Dette beløpet utgjør differansen mellom din " +
                            "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " +
                            pgi.format() + " og arbeidsinntekten din etter opphøret av AFP på " +
                            ieo.format() + "."
                    },
                    nynorsk {
                        +"Den faktiske arbeidsinntekta i den perioden du har fått AFP, er " +
                            iiap.format() + ". Dette beløpet utgjer differansen mellom den " +
                            "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " +
                            pgi.format() + " og arbeidsinntekta di etter at AFP tok slutt, på " +
                            ieo.format() + "."
                    },
                )
            }
        }
    }
}
