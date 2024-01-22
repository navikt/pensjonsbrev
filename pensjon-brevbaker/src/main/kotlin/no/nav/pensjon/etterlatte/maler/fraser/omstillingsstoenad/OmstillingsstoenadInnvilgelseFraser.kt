package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.size
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetaling
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

object OmstillingsstoenadInnvilgelseFraser {

    data class Vedtak(
        val avdoed: Expression<Avdoed>,
        val omstillingsstoenadBeregning: Expression<OmstillingsstoenadBeregning>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val utbetaltBeloep = omstillingsstoenadBeregning.sisteBeregningsperiode.utbetaltBeloep
            val harFlerePerioder = omstillingsstoenadBeregning.beregningsperioder.size().greaterThan(1)

            paragraph {
                val formatertVirkningsdato = omstillingsstoenadBeregning.virkningsdato.format()
                val formatertDoedsdato = avdoed.doedsdato.format()
                textExpr(
                    Bokmal to "Du er innvilget omstillingsstønad fra ".expr() + formatertVirkningsdato +
                            " fordi " + avdoed.navn + " døde " + formatertDoedsdato + ".",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }

            showIf(utbetaltBeloep.greaterThan(0)) {
                showIf(harFlerePerioder) {
                    val datoFomSisteBeregningsperiode = omstillingsstoenadBeregning.sisteBeregningsperiode.datoFOM

                    paragraph {
                        textExpr(
                            Bokmal to "".expr() + "Du får " +
                                    utbetaltBeloep.format() +
                                    " kroner hver måned før skatt fra " + datoFomSisteBeregningsperiode.format() +
                                    ". Se utbetalingsbeløp for tidligere perioder i vedlegget «Etterbetaling av " +
                                    "omstillingsstønad».",
                            Nynorsk to "".expr(),
                            English to "".expr(),
                        )
                    }
                }.orShow {
                    paragraph {
                        textExpr(
                            Bokmal to "".expr() + "Du får " +
                                    utbetaltBeloep.format() +
                                    " kroner i stønad hver måned før skatt.",
                            Nynorsk to "".expr(),
                            English to "".expr(),
                        )
                    }
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Du får ikke utbetalt omstillingsstønad fordi du har inntekt som er høyere " +
                                "enn grensen for å få utbetalt stønaden.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Se hvordan vi har beregnet omstillingsstønaden din i vedlegget «Beregning av " +
                            "omstillingsstønad».",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class BegrunnelseForVedtaketRedigerbart(
        val etterbetaling: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Begrunnelse for vedtaket",
                    Nynorsk to "",
                    English to "",
                )
            }

            paragraph {
                text(
                    Bokmal to "Omstillingsstønad gis på bakgrunn av at",
                    Nynorsk to "",
                    English to "",
                )
                list {
                    item {
                        text(
                            Bokmal to "du som gjenlevende ektefelle på dødsfallstidspunktet hadde vært gift " +
                                    "med avdøde i minst fem år, har eller har hatt barn med avdøde, eller har " +
                                    "omsorgen for barn under 18 år med minst halvparten av full tid",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du er medlem i folketrygden",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                    item {
                        text(
                            Bokmal to "avdøde i de siste fem årene før dødsfallet var medlem i folketrygden, " +
                                    "eller fikk pensjon eller uføretrygd fra folketrygden.",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Samboere med felles barn og samboere som tidligere har vært gift likestilles " +
                            "med ektefeller.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                showIf(etterbetaling) {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven " +
                                "§ 17-2, § 17-3, § 17-4, § 17-5, § 17-6, § 17-9, § 22-12 og § 22-13.",
                        Nynorsk to "",
                        English to "",
                    )
                } orShow {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven " +
                                "§ 17-2, § 17-3, § 17-4, § 17-5, § 17-6, § 17-9 og § 22-12.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
    }

    data class Utbetaling(
        val etterbetaling: Expression<OmstillingsstoenadEtterbetaling?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Utbetaling av omstillingsstønad",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Omstillingsstønad blir utbetalt innen den 20. i hver måned. " +
                            "Du finner utbetalingsdatoer på ${Constants.UTBETALING_URL}.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Utbetalingen kan bli forsinket hvis den skal samordnes med ytelser du mottar " +
                            "fra NAV eller andre, som for eksempel tjenestepensjonsordninger.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Har du rett til etterbetaling, vil du vanligvis få dette i løpet av tre uker. ",
                    Nynorsk to "",
                    English to "",
                )
                ifNotNull(etterbetaling) {
                    text(
                        Bokmal to "Du finner mer informasjon om etterbetaling i vedlegget " +
                                "«Etterbetaling av omstillingsstønad».",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
    }

    object HvaErOmstillingsstoenad : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Hva er omstillingsstønad",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Formålet med omstillingsstønaden er å sikre den gjenlevende inntekt og gi hjelp " +
                            "til selvhjelp, slik at den gjenlevende etter en omstillingsperiode etter dødsfallet " +
                            "kan bli i stand til å forsørge seg selv ved eget arbeid.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class HvorLengerKanDuFaaOmstillingsstoenad(
        val beregning: Expression<OmstillingsstoenadBeregning>,
        val lavEllerIngenInntekt: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Hvor lenge kan du få omstillingsstønaden?",
                    Nynorsk to "",
                    English to "",
                )
            }
            showIf(beregning.sisteBeregningsperiode.utbetaltBeloep.greaterThan(0)) {
                paragraph {
                    text(
                        Bokmal to "Du får omstillingsstønad frem til det er gått tre år fra datoen for dødsfallet, så " +
                                "lenge du oppfyller vilkårene.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Stønaden kan forlenges med inntil to år hvis du tar utdanning som er nødvendig " +
                                "og hensiktsmessig.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Du er innvilget omstillingsstønad frem til det er gått tre år fra datoen for " +
                                "dødsfallet, så lenge du oppfyller vilkårene. Om det skjer endringer " +
                                "i inntekten din kan dette gjør at du likevel vil få utbetalt stønad i denne perioden. ",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }

            showIf(lavEllerIngenInntekt) {
                paragraph {
                    text(
                        Bokmal to "Du kommer inn under unntaksreglene for varighet av stønaden, fordi du har hatt " +
                                "lav eller ingen inntekt de siste fem årene før dødsfallstidspunktet. Du får " +
                                "omstillingsstønad frem til du fyller 67 år, så lenge du oppfyller vilkårene.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Les mer om hvor lenge du kan få på " + Constants.OMS_HVORLENGE_URL + ".",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    object Inntektsendring : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du må melde fra hvis inntekten din endrer seg",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "For at du skal motta korrekt omstillingsstønad, er det viktig at du informerer " +
                            "oss hvis inntekten din endrer seg. Vi justerer omstillingsstønaden fra måneden etter " +
                            "at du har gitt beskjed. Du kan lese mer om inntektsendring i vedlegget " +
                            "«Informasjon til deg som mottar omstillingsstønad».",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class Aktivitetsplikt(
        val innvilgetMindreEnnFireMndEtterDoedsfall: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du må være i aktivitet fra seks måneder etter dødsfallet",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Det første halvåret etter dødsfallet stilles det ikke krav til at du er i " +
                            "arbeid eller arbeidsrettet aktivitet. Etter seks måneder er det et vilkår for å " +
                            "fortsatt ha rett til omstillingsstønad at du er i minst 50 prosent aktivitet. " +
                            "Dette kalles aktivitetsplikt. Les mer om aktivitetsplikt og hva denne innebærer i vedlegget " +
                            "«Informasjon til deg som mottar omstillingsstønad».",
                    Nynorsk to "",
                    English to "",
                )
            }

            showIf(innvilgetMindreEnnFireMndEtterDoedsfall) {
                paragraph {
                    text(
                        Bokmal to "Vi sender deg et eget informasjonsbrev om at du må være i aktivitet når det " +
                                "nærmer seg seks måneder etter dødsfallet.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
    }

    object Etteroppgjoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Etteroppgjør",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hver høst sjekker NAV inntektsopplysningene i skatteoppgjøret ditt for å se " +
                            "om du har fått utbetalt riktig beløp i omstillingsstønad året før. Hvis du har fått " +
                            "for lite utbetalt, får du en etterbetaling. Har du fått for mye utbetalt, må du betale " +
                            "tilbake. Du kan finne mer informasjon om etteroppgjør på " +
                            "${Constants.OMS_ETTEROPPGJOER_URL}.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}
