package no.nav.pensjon.brev.alder.maler.afp.fraser

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

/**
 * Per-scenario forklaringer om hvilke nye inntektsopplysninger som er lagt fram
 * for AFP etteroppgjør (offentlig sektor / Statens pensjonskasse).
 */
object AfpEtteroppgjoerForklaringer {

    data class IngenNyeOpplysningerOmEndretInntektFoerUttak(
        val inntektFoerUttak: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du har lagt fram nye opplysninger om inntekten din. Dokumentasjonen gir ikke grunnlag for å endre tidligere beregnet arbeidsinntekt før uttak av AFP. Arbeidsinntekten din er derfor satt til " + inntektFoerUttak.format() + " i samsvar med den tidligere beregningen. Dette beløpet skal holdes utenfor etteroppgjøret for " + oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Du har lagt fram nye opplysningar om inntekta di. Dokumentasjonen gir ikkje grunnlag for å endre tidlegare berekna arbeidsinntekt før uttak av AFP. Arbeidsinntekta di er derfor sett til " + inntektFoerUttak.format() + " i samsvar med den tidlegare berekninga. Dette beløpet skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() + "."
                    },
                )
            }
        }
    }

    data class DenFaktiskeArbeidsinntektenKunIfu(
        val inntektIAfpPerioden: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
        val pensjonsgivendeInntekt: Expression<Kroner>,
        val inntektFoerUttak: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP er satt til " + inntektIAfpPerioden.format() + ". Dette beløpet er forskjellen mellom din pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " + pensjonsgivendeInntekt.format() + " og arbeidsinntekten din før uttak av AFP på " + inntektFoerUttak.format() + "."
                    },
                    nynorsk {
                        +"Den faktiske arbeidsinntekta i den perioden du har fått AFP er sett til " + inntektIAfpPerioden.format() + ". Dette beløpet er forskjellen mellom den pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " + pensjonsgivendeInntekt.format() + " og arbeidsinntekta di før uttak av AFP på " + inntektFoerUttak.format() + "."
                    },
                )
            }
        }
    }

    data class IfuOverstyrtUttakIAaret(
        val inntektFoerUttak: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du har lagt fram nye opplysninger om inntekten din. Ifølge nye opplysninger har du hatt en høyere arbeidsinntekt før du tok ut AFP enn det som er beregnet tidligere. Arbeidsinntekten din er endret i samsvar med disse opplysningene til " + inntektFoerUttak.format() + ". Dette beløpet skal holdes utenfor etteroppgjøret for " + oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Du har lagt fram nye opplysningar om inntekta di. Ifølgje nye opplysningar har du hatt ei høgare arbeidsinntekt før du tok ut AFP enn det som er berekna tidlegare. Arbeidsinntekta di er endra i samsvar med desse opplysningane til " + inntektFoerUttak.format() + ". Dette beløpet skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() + "."
                    },
                )
            }
        }
    }

    data class IfuOverstyrtUttakFoerAaret(
        val inntektFoerUttak: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye opplysninger har du hatt pensjonsgivende inntekt som er opptjent før uttak av AFP. Arbeidsinntekten som kommer fra tidligere arbeid, er satt til " + inntektFoerUttak.format() + ". Dette beløpet skal holdes utenfor etteroppgjøret av AFP for " + oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye opplysningar har du hatt pensjonsgivande inntekt som er opptent før uttak av AFP. Arbeidsinntekta som kjem frå tidlegare arbeid, er sett til " + inntektFoerUttak.format() + ". Dette beløpet skal haldast utanfor etteroppgjeret av AFP for " + oppgjoersAar.format() + "."
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
        val inntektFoerUttak: Expression<Kroner>,
        val inntektEtterOpphoer: Expression<Kroner>,
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
                            "opplysningene til henholdsvis " + inntektFoerUttak.format() + " for perioden før " +
                            "uttak av AFP og " + inntektEtterOpphoer.format() + " for perioden etter opphør av AFP. " +
                            "Disse beløpene skal holdes utenfor etteroppgjøret for " +
                            oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                            "opplysningar har du hatt ei høgare arbeidsinntekt enn tidlegare berekna " +
                            "i periodar der du ikkje samtidig har hatt rett til AFP. Arbeidsinntekta " +
                            "di for denne perioden er endra i samsvar med desse opplysningane til " +
                            "høvesvis " + inntektFoerUttak.format() + " for perioden før uttak av AFP og " +
                            inntektEtterOpphoer.format() + " for perioden etter at AFP tok slutt. Desse " +
                            "beløpa skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() +
                            "."
                    },
                )
            }
        }
    }

    data class DenFaktiskeArbeidsinntektenIfuOgIeo(
        val inntektIAfpPerioden: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
        val pensjonsgivendeInntekt: Expression<Kroner>,
        val inntektFoerUttak: Expression<Kroner>,
        val inntektEtterOpphoer: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP, er " + inntektIAfpPerioden.format() + ". Dette beløpet er differansen mellom din pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " + pensjonsgivendeInntekt.format() + " og summen av arbeidsinntektene før uttak av AFP på " + inntektFoerUttak.format() + " og etter opphør av AFP på " + inntektEtterOpphoer.format() + "."
                    },
                    nynorsk {
                        +"Den faktiske arbeidsinntekta i den perioden du har fått AFP, er " + inntektIAfpPerioden.format() + ". Dette beløpet er differansen mellom den pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " + pensjonsgivendeInntekt.format() + " og summen av arbeidsinntektene før uttak av AFP på " + inntektFoerUttak.format() + " og etter at AFP tok slutt, på " + inntektEtterOpphoer.format() + "."
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
        val inntektEtterOpphoer: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye opplysninger har du hatt en høyere arbeidsinntekt enn tidligere beregnet etter opphør av AFP. Arbeidsinntekten din for denne perioden er endret i samsvar med disse opplysningene til " + inntektEtterOpphoer.format() + ". Dette beløpet skal holdes utenfor etteroppgjøret for " + oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye opplysningar har du hatt ei høgare arbeidsinntekt enn tidlegare berekna etter at AFP tok slutt. Arbeidsinntekta di for denne perioden er endra i samsvar med desse opplysningane til " + inntektEtterOpphoer.format() + ". Dette beløpet skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() + "."
                    },
                )
            }
        }
    }

    data class DenFaktiskeArbeidsinntektenKunIeo(
        val inntektIAfpPerioden: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
        val pensjonsgivendeInntekt: Expression<Kroner>,
        val inntektEtterOpphoer: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP er " + inntektIAfpPerioden.format() + ". Dette beløpet er forskjellen mellom din pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " + pensjonsgivendeInntekt.format() + " og arbeidsinntekten din etter opphør av AFP på " + inntektEtterOpphoer.format() + "."
                    },
                    nynorsk {
                        +"Den faktiske arbeidsinntekta i den perioden du har fått AFP er " + inntektIAfpPerioden.format() + ". Dette beløpet er forskjellen mellom den pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " + pensjonsgivendeInntekt.format() + " og arbeidsinntekta di etter at AFP tok slutt, på " + inntektEtterOpphoer.format() + "."
                    },
                )
            }
        }
    }
}
