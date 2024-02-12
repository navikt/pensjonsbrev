package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiode
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetaling
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import java.time.LocalDate

object BarnepensjonForeldreloesFraser {

    data class Vedtak(
        val virkningstidspunkt: Expression<LocalDate>,
        val sistePeriodeFom: Expression<LocalDate>,
        val sistePeriodeBeloep: Expression<Kroner>,
        val bareEnPeriode: Expression<Boolean>,
        val flerePerioder: Expression<Boolean>,
        val ingenUtbetaling: Expression<Boolean>,
        val vedtattIPesys: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertVirkningsdato = virkningstidspunkt.format()
            val formatertBeloep = sistePeriodeBeloep.format()
            val formatertFom = sistePeriodeFom.format()

            paragraph {
                textExpr(
                    Language.Bokmal to
                            ifElse(
                                vedtattIPesys,
                                "Vi viser til at du er innvilget barnepensjon. Stortinget har vedtatt nye regler for barnepensjon. Pensjonen din er derfor endret fra 1. januar 2024.".expr(),
                                "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato + "fordi begge foreldrene dine er registrert død.".expr()
                            ),
                    Language.Nynorsk to "".expr(),
                    Language.English to "".expr(),
                )
            }
            showIf(flerePerioder) {
                paragraph {
                    textExpr(
                        Language.Bokmal to (
                                "Du får ".expr() + formatertBeloep + "kroner hver måned før skatt fra " + formatertFom +
                                        ". Se beløp for tidligere perioder og hvordan vi har beregnet pensjonen i vedlegg “Beregning av barnepensjon”."
                                ),
                        Language.Nynorsk to "".expr(),
                        Language.English to "".expr(),
                    )
                }
            }.orShow {
                paragraph {
                    textExpr(
                        Language.Bokmal to "Du får ".expr() + formatertBeloep + " kroner hver måned før skatt.".expr(),
                        Language.Nynorsk to "".expr(),
                        Language.English to "".expr(),
                    )
                }
            }
            showIf(ingenUtbetaling) {
                paragraph {
                    text(
                        Language.Bokmal to "Du får ikke utbetalt barnepensjon fordi den er redusert utfra det du" +
                                " mottar i uføretrygd fra NAV.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Se hvordan vi har beregnet pensjonen din i vedlegget “Beregning av barnepensjon”.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
    }

    data class BegrunnelseForVedtaketRedigerbart(
        val etterbetaling: Expression<Boolean>,
        val vedtattIPesys: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(vedtattIPesys) {
                paragraph {
                    text(
                        Language.Bokmal to "De nye reglene for barnepensjon har ingen søskenjustering, og du vil få pensjon til du er 20 år selv om du er under utdanning.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Barnepensjon gis på bakgrunn av at",
                    Language.Nynorsk to "Det blir gitt barnepensjon på bakgrunn av at",
                    Language.English to "",
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "du har mistet begge foreldrene dine",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "du er under 20 år",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "du er medlem i folketrygden",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "minst en av foreldrene dine i de siste fem årene før dødsfallet " +
                                    "var medlem i folketrygden, eller fikk pensjon eller uføretrygd fra folketrygden",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }
            }
            paragraph {
                showIf(etterbetaling) {
                    text(
                        Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven " +
                                "§ 17-2, § 17-3, § 17-4, § 17-5, § 17-6, § 17-9, § 22-12 og § 22-13.",
                        Language.Nynorsk to "Vedtaket er fatta etter føresegnene om barnepensjon i " +
                                "folketrygdlova §§ 17-2, 17-3, 17-4, 17-5, 17-6, 17-9 og 22-12 og § 22-13.",
                        Language.English to "",
                    )
                } orShow {
                    text(
                        Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven " +
                                "§ 17-2, § 17-3, § 17-4, § 17-5, § 17-6, § 17-9 og § 22-12.",
                        Language.Nynorsk to "Vedtaket er fatta etter føresegnene om barnepensjon i folketrygdlova " +
                                "§§ 17-2, 17-3, 17-4, 17-5, 17-6, 17-9 og 22-12.",
                        Language.English to "",
                    )
                }
            }
        }
    }

    // TODO Er tilsvarende BarnepensjonInnvilgelseFraser.UtbetalingAvBarnepensjon med unntak av del for etterbetaling
    // Vanlig innvilgelsebrev skal bli endres til å bli lik som denne og da bør vi sammenslå disse.
    data class UtbetalingAvBarnepensjon(
        val beregningsperioder: Expression<List<BarnepensjonBeregningsperiode>>,
        val etterbetaling: Expression<BarnepensjonEtterbetaling?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Utbetaling av barnepensjon",
                    Language.Nynorsk to "Utbetaling av barnepensjon",
                    Language.English to "Payment of the children's pension",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Pensjonen blir utbetalt innen den 20. i hver måned. Du finner utbetalingsdatoer på ${Constants.UTBETALINGSDATOER_URL}.",
                    Language.Nynorsk to "Pensjonen blir utbetalt innan den 20. kvar månad. Du finn utbetalingsdatoane på ${Constants.UTBETALINGSDATOER_URL}.",
                    Language.English to "The pension is paid by the 20th of each month. You can find payout dates online: ${Constants.Engelsk.UTBETALINGSDATOER_URL}.",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Har du rett til etterbetaling, vil du vanligvis få dette i løpet av tre uker.",
                    Language.Nynorsk to "Dersom du har rett på etterbetaling, vil du vanlegvis få denne i løpet av tre veker.",
                    Language.English to "If you are entitled to a back payment, you will normally receive this within three weeks.",
                )
            }
            ifNotNull(etterbetaling) {
                paragraph {
                    text(
                        Language.Bokmal to "Det kan bli beregnet fradrag i etterbetalingen for skatt, eller ytelser du har mottatt fra NAV eller andre. " +
                                "Hvis Skatteetaten eller andre ordninger har krav i etterbetalingen kan denne bli forsinket. " +
                                "Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Det trekkes vanligvis skatt av etterbetaling. " +
                                "Gjelder etterbetalingen tidligere år trekker NAV skatt etter Skatteetatens standardsatser. " +
                                "Du kan lese mer om satsene på ${Constants.SKATTETREKK_ETTERBETALING_URL}.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }
        }
    }

    object HvorLengeKanDuFaaBarnepensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Hvor lenge kan du få barnepensjon?",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du er innvilget barnepensjon til og med den kalendermåneden du fyller 20 år, så lenge du oppfyller vilkårene.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
    }

}
