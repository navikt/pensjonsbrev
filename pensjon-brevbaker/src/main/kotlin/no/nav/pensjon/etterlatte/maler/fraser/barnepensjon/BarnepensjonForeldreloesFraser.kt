package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import no.nav.pensjon.etterlatte.maler.ForskjelligAvdoedPeriode
import no.nav.pensjon.etterlatte.maler.ForskjelligAvdoedPeriodeSelectors.foersteAvdoed
import no.nav.pensjon.etterlatte.maler.ForskjelligAvdoedPeriodeSelectors.senereAvdoed
import no.nav.pensjon.etterlatte.maler.ForskjelligAvdoedPeriodeSelectors.senereVirkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak.BegrunnelseForVedtaket
import java.time.LocalDate

object BarnepensjonForeldreloesFraser {

    data class Vedtak(
        val virkningstidspunkt: Expression<LocalDate>,
        val sistePeriodeFom: Expression<LocalDate>,
        val sistePeriodeBeloep: Expression<Kroner>,
        val flerePerioder: Expression<Boolean>,
        val harUtbetaling: Expression<Boolean>,
        val vedtattIPesys: Expression<Boolean>,
        val erGjenoppretting: Expression<Boolean>,
        val forskjelligAvdoedPeriode: Expression<ForskjelligAvdoedPeriode?>,
        val erSluttbehandling: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertVirkningsdato = virkningstidspunkt.format()
            val formatertBeloep = sistePeriodeBeloep.format()
            val formatertFom = sistePeriodeFom.format()

            // TODO: add BP sluttbehandling

            paragraph {
                showIf(vedtattIPesys) {
                    text(
                        Language.Bokmal to "Vi viser til at du er innvilget barnepensjon. Stortinget har vedtatt nye regler for barnepensjon. Pensjonen din er derfor endret fra 1. januar 2024.",
                        Language.Nynorsk to "Vi viser til at du er innvilga barnepensjon. Stortinget har vedteke nye reglar for barnepensjon. Pensjonen din er difor endra frå og med 1. januar 2024.",
                        Language.English to "You are currently receiving a children’s pension. The Norwegian Parliament (Stortinget) has adopted new rules regarding children's pensions. Your pension has therefore been changed from 1 January 2024.",
                    )
                }.orShowIf(erGjenoppretting) {
                    textExpr(
                        Language.Bokmal to "Vi viser til forhåndsvarsel om ny barnepensjon. Du er innvilget barnepensjon på nytt fra ".expr() +
                                formatertVirkningsdato + " fordi begge foreldrene dine er registrert død.",
                        Language.Nynorsk to "Vi viser til førehandsvarselet om ny barnepensjon. Du er innvilga ny barnepensjon frå og med ".expr() +
                                formatertVirkningsdato + " fordi begge foreldra dine er registrert død.",
                        Language.English to "We refer to the advance notice about a new children’s pension scheme. You have been granted a children's pension again from ".expr() +
                                formatertVirkningsdato + " both your parents are registered as deceased."
                    )
                }.orIfNotNull(forskjelligAvdoedPeriode) { forskjelligAvdoed ->
                    val avdoedNavn = forskjelligAvdoed.foersteAvdoed.navn
                    val formatertDoedsdato = forskjelligAvdoed.foersteAvdoed.doedsdato.format()
                    val senereDoedsdato = forskjelligAvdoed.senereAvdoed.doedsdato.format()
                    val senereVirkningsdato = forskjelligAvdoed.senereVirkningsdato.format()
                    textExpr(
                        Language.Bokmal to "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". Barnepensjon endres fra " + senereVirkningsdato + " fordi den andre forelderen din er registrert død " + senereDoedsdato + ". ",
                        Language.Nynorsk to "Du er innvilga barnepensjon frå og med ".expr() + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". Barnepensjonen din er endra frå " + senereVirkningsdato + " fordi begge foreldra dine er registrert som døde. ",
                        Language.English to "You have been granted a children's pension ".expr() + formatertVirkningsdato + " because " + avdoedNavn + " is registered as deceased on "+ formatertDoedsdato + ". Your children's pension will change on " + senereVirkningsdato + " because both your parents are registered as deceased. ",
                    )
                }.orShow {
                    textExpr(
                        Language.Bokmal to "Du er innvilget barnepensjon fra ".expr() +
                                formatertVirkningsdato + " fordi begge foreldrene dine er registrert død.".expr(),
                        Language.Nynorsk to "Du er innvilga barnepensjon frå ".expr() +
                                formatertVirkningsdato + " fordi begge foreldra dine er registrert som døde.".expr(),
                        Language.English to "You have been granted a children's pension starting ".expr() +
                                formatertVirkningsdato + " because both your parents are registered as deceased.".expr()
                    )
                }

                showIf(harUtbetaling) {
                    showIf(flerePerioder) {
                        textExpr(
                            Language.Bokmal to " Du får ".expr() + formatertBeloep +
                                    " kroner hver måned før skatt fra " + formatertFom + ".",
                            Language.Nynorsk to " Du får ".expr() + formatertBeloep +
                                    " kroner per månad før skatt frå " + formatertFom + ".",
                            Language.English to " You will receive NOK ".expr() + formatertBeloep +
                                    " each month before tax, starting on " + formatertFom + "."
                        )
                    }.orShow {
                        textExpr(
                            Language.Bokmal to " Du får ".expr() + formatertBeloep + " kroner hver måned før skatt.".expr(),
                            Language.Nynorsk to " Du får ".expr() + formatertBeloep + " kroner per månad før skatt.".expr(),
                            Language.English to " You will receive NOK ".expr() + formatertBeloep + " each month before tax.".expr(),
                        )
                    }
                }.orShow {
                    text(
                        Language.Bokmal to " Du får ikke utbetalt barnepensjon fordi den er redusert utfra det du" +
                                " mottar i uføretrygd fra NAV.",
                        Language.Nynorsk to " Du får ikkje utbetalt barnepensjon, då denne har blitt redusert med" +
                                " utgangspunkt i uføretrygda du får frå NAV.",
                        Language.English to " You will not receive payments from the children’s pension because they" +
                                " have been reduced according to what you already receive in disability benefits from NAV.",
                    )
                }
            }

            paragraph {
                showIf(harUtbetaling and flerePerioder) {
                    text(
                        Language.Bokmal to "Se beløp for tidligere perioder og hvordan vi har beregnet pensjonen din " +
                                "i vedlegget “Beregning av barnepensjon”.",
                        Language.Nynorsk to "I vedlegget «Utrekning av barnepensjon» kan du sjå beløp for tidlegare periodar " +
                                "og korleis vi har rekna ut pensjonen.",
                        Language.English to "You can see amounts for previous periods and how we calculated your pension " +
                                "in the attachment, Calculation of Children’s Pension."
                    )
                }.orShow {
                    text(
                        Language.Bokmal to "Se hvordan vi har beregnet pensjonen din i vedlegget “Beregning av barnepensjon”.",
                        Language.Nynorsk to "I vedlegget «Utrekning av barnepensjon» kan du sjå korleis vi har rekna ut pensjonen din.",
                        Language.English to "You can find more information about how we have calculated your" +
                                " children's pension in the attachment, Calculating the Children's Pension.",
                    )
                }
            }
        }
    }

    data class BegrunnelseForVedtaketRedigerbart(
        val etterbetaling: Expression<Boolean>,
        val vedtattIPesys: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            includePhrase(BegrunnelseForVedtaket)
            showIf(vedtattIPesys) {
                paragraph {
                    text(
                        Language.Bokmal to "De nye reglene for barnepensjon har ingen søskenjustering, og du vil få pensjon til du er 20 år selv om du ikke er under utdanning.",
                        Language.Nynorsk to "Dei nye reglane for barnepensjon har inga søskenjustering, og du vil få pensjon til du er 20 år sjølv om du ikkje er under utdanning.",
                        Language.English to "The new rules for children’s pensions have no sibling adjustment, and you will receive pension until you are 20 years old even if you are not in education.",
                    )
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Barnepensjon gis på bakgrunn av at",
                    Language.Nynorsk to "Du kan få barnepensjon når",
                    Language.English to "The children’s pension has been granted because",
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "du har mistet begge foreldrene dine",
                            Language.Nynorsk to "du har mista begge foreldre",
                            Language.English to "you have lost both your parents",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "du er under 20 år",
                            Language.Nynorsk to "du er under 20 år",
                            Language.English to "you are under the age of 20",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "du er medlem i folketrygden",
                            Language.Nynorsk to "du er medlem i folketrygda",
                            Language.English to "you are a member of the national insurance scheme",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "minst en av foreldrene dine i de siste fem årene før dødsfallet " +
                                    "var medlem i folketrygden, eller fikk pensjon eller uføretrygd fra folketrygden.",
                            Language.Nynorsk to "eine eller begge foreldra dine var medlem i folketrygda eller " +
                                    "fekk pensjon/uføretrygd frå folketrygda dei siste fem åra før sin død",
                            Language.English to "at least one of your parents was a member of the national " +
                                    "insurance scheme in the last five years before his or her death, or received a " +
                                    "pension or disability pension from the scheme",
                        )
                    }
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven " +
                            "§§ 18-2, 18-3, 18-4, 18-5, 22-12 og 22-13.",
                    Language.Nynorsk to "Vedtaket er fatta etter føresegnene om barnepensjon i folketrygdlova " +
                            "§§ 18-2, 18-3, 18-4, 18-5, 22-12 og 22-13.",
                    Language.English to "This decision has been made pursuant to the provisions regarding " +
                            "children's pensions in the National Insurance Act – " +
                            "sections 18-2, 18-3, 18-4, 18-5, 22-12 and 22-13.",
                )
            }
        }
    }
}
