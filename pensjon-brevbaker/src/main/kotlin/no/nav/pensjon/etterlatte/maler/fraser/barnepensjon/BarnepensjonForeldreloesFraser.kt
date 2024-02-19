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
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
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
                                "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato + " fordi begge foreldrene dine er registrert død.".expr()
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
                        Language.Bokmal to "De nye reglene for barnepensjon har ingen søskenjustering, og du vil få pensjon til du er 20 år selv om du ikke er under utdanning.",
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
                                    "var medlem i folketrygden, eller fikk pensjon eller uføretrygd fra folketrygden.",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }
            }
            paragraph {
                textExpr(
                    Language.Bokmal to ("Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven " +
                            "§§ 18-2, 18-3, 18-4, 18-5").expr() +
                            ifElse(etterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                    Language.Nynorsk to ("Vedtaket er fatta etter føresegnene om barnepensjon i folketrygdlova " +
                            "§§ 18-2, 18-3, 18-4, 18-5").expr() +
                            ifElse(etterbetaling, ", 22-12 og 22-13.", " og 22-12."),
                    Language.English to ("This decision has been made pursuant to the provisions regarding " +
                            "children's pensions in the National Insurance Act – " +
                            "sections 18-2, 18-3, 18-4, 18-5").expr() +
                            ifElse(etterbetaling, ", 22-12 and 22-13.", " and 22-12."),
                )
            }
        }
    }
}
