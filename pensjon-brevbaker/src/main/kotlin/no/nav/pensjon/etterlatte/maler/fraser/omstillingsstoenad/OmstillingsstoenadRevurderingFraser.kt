package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

class OmstillingsstoenadRevurderingFraser {
    data class RevurderingVedtak(
        val erEndret: Expression<Boolean>,
        val beregning: Expression<OmstillingsstoenadBeregning>,
        val erEtterbetaling: Expression<Boolean>,
        val harFlereUtbetalingsperioder: Expression<Boolean>,
        val harUtbetaling: Expression<Boolean>
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                val formatertNyesteUtbetalingsperiodeDatoFom = beregning.sisteBeregningsperiode.datoFOM.format()
                val formatertVirkningsdato = beregning.virkningsdato.format()
                val formatertBeloep = beregning.sisteBeregningsperiode.utbetaltBeloep.format()

                showIf(erEndret) {
                    showIf(harUtbetaling) {
                        showIf(harFlereUtbetalingsperioder) {
                            paragraph {
                                textExpr(
                                    Language.Bokmal to "Omstillingsstønaden din er endret fra ".expr() +
                                            formatertVirkningsdato + ". Du får " + formatertBeloep + " kroner hver måned " +
                                            "før skatt fra " + formatertNyesteUtbetalingsperiodeDatoFom + ". Se beløp for " +
                                            "tidligere perioder og hvordan vi har beregnet omstillingsstønaden i " +
                                            "vedlegg «Beregning av omstillingsstønad».",
                                    Language.Nynorsk to "".expr(),
                                    Language.English to "".expr()
                                )
                            }
                        }.orShow {
                            paragraph {
                                textExpr(
                                    Language.Bokmal to "Omstillingsstønaden din er endret fra ".expr() +
                                            formatertVirkningsdato + ". Du får " + formatertBeloep + " kroner hver måned " +
                                            "før skatt. ",
                                    Language.Nynorsk to "".expr(),
                                    Language.English to "".expr()
                                )
                            }
                            paragraph {
                                text(
                                    Language.Bokmal to "Se hvordan vi har beregnet omstillingsstønaden din i vedlegget " +
                                            "«Beregning av omstillingsstønad».",
                                    Language.Nynorsk to "",
                                    Language.English to "",
                                )
                            }
                        }
                    }.orShow {
                        paragraph {
                            text(
                                Language.Bokmal to "Du får ikke utbetalt stønad. ",
                                Language.Nynorsk to "",
                                Language.English to "",
                            )
                        }
                        paragraph {
                            text(
                                Language.Bokmal to "Se hvordan vi har beregnet omstillingsstønaden din i vedlegget " +
                                        "«Beregning av omstillingsstønad».",
                                Language.Nynorsk to "",
                                Language.English to "",
                            )
                        }
                    }

                }.orShow {
                    paragraph {
                        text(
                            Language.Bokmal to "Omstillingsstønaden din er vurdert på nytt. ",
                            Language.Nynorsk to "",
                            Language.English to ""
                        )
                        showIf(harUtbetaling) {
                            textExpr(
                                Language.Bokmal to "Du får fortsatt ".expr() + formatertBeloep + " kroner per " +
                                        "måned før skatt.",
                                Language.Nynorsk to "".expr(),
                                Language.English to "".expr()
                            )
                        }.orShow {
                            text(
                                Language.Bokmal to "Du får fortsatt ikke utbetalt stønad.",
                                Language.Nynorsk to "",
                                Language.English to ""
                            )
                        }
                    }
                    paragraph {
                        text(
                            Language.Bokmal to "Se hvordan vi har beregnet omstillingsstønaden din i vedlegget " +
                                    "«Beregning av omstillingsstønad».",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }

        }
    }

    data class UtfallRedigerbart(val etterbetaling: Expression<Boolean>, val feilutbetaling: Expression<FeilutbetalingType?>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "(utfall jamfør tekstbibliotek)",
                    Language.Nynorsk to "(utfall jamfør tekstbibliotek)",
                    Language.English to "(utfall jamfør tekstbibliotek)",
                )
            }

            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_UTEN_VARSEL)) {
                  paragraph {
                      text(
                          Language.Bokmal to "Fordi stønaden din er redusert tilbake i tid, har du fått for " +
                                  "mye utbetalt. Beløpet er under den nedre grensen for tilbakekreving som fremgår " +
                                  "av folketrygdloven § 22-15 sjette ledd, og kreves derfor ikke tilbakebetalt. ",
                          Language.Nynorsk to "",
                          Language.English to "",
                      )
                  }
            }

            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven § <riktig paragrafhenvisning> ",
                    Language.Nynorsk to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven § <riktig paragrafhenvisning> ",
                    Language.English to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i folketrygdloven § <riktig paragrafhenvisning> ",
                )
                showIf(etterbetaling) {
                    text(
                        Language.Bokmal to ", § 22-12 og § 22-13.",
                        Language.Nynorsk to ", § 22-12 og § 22-13.",
                        Language.English to ", § 22-12 og § 22-13."
                    )
                }.orShow {
                    text(
                        Language.Bokmal to "og § 22-12.",
                        Language.Nynorsk to "og § 22-12.",
                        Language.English to "og § 22-12."
                    )
                }

            }
        }
    }

    data class Utbetaling(
        val erEtterbetaling: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Utbetaling av omstillingsstønad",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Omstillingsstønad blir utbetalt innen den 20. i hver måned. Du finner " +
                            "utbetalingsdatoer på ${Constants.UTBETALING_URL}. Utbetalingen kan bli forsinket hvis " +
                            "den skal samordnes med ytelser du mottar fra NAV eller andre, som for eksempel " +
                            "tjenestepensjonsordninger.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            showIf(erEtterbetaling) {
                paragraph {
                    text(
                        Language.Bokmal to "Du får etterbetalt stønad. Vanligvis vil du få denne i løpet av " +
                                "tre uker. Hvis Skatteetaten eller andre ordninger har krav i etterbetalingen kan " +
                                "denne bli forsinket. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Det trekkes vanligvis skatt av etterbetaling. Gjelder " +
                                "etterbetalingen tidligere år trekker NAV skatt etter Skatteetatens standardsatser. " +
                                "Du kan lese mer om satsene på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }
        }
    }

    object Feilutbetaling : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Feilutbetaling",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Fordi stønaden din er redusert tilbake i tid, har du fått for mye " +
                            "utbetalt. Se vedlegg «Forhåndsvarsel om eventuell tilbakekreving av feilutbetalt stønad».",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
    }

    object Aktivitetsplikt : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Du må være i aktivitet fra seks måneder etter dødsfallet",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Når det er gått seks måneder etter dødsfallet er du pliktig til å være " +
                            "i minst 50 prosent aktivitet for å motta omstillingsstønad. Les mer om aktivitetsplikt " +
                            "og hva denne innebærer i vedlegget «Informasjon til deg som mottar omstillingsstønad».",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
    }

}