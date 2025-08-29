package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
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

            showIf(erSluttbehandling) {
                includePhrase(BarnepensjonFellesFraser.DuHarTidligereAvslagViHarFaattNyeOplysninger)
            }

            paragraph {
                showIf(vedtattIPesys) {
                    text(
                        bokmal { +"Vi viser til at du er innvilget barnepensjon. Stortinget har vedtatt nye regler for barnepensjon. Pensjonen din er derfor endret fra 1. januar 2024." },
                        nynorsk { +"Vi viser til at du er innvilga barnepensjon. Stortinget har vedteke nye reglar for barnepensjon. Pensjonen din er difor endra frå og med 1. januar 2024." },
                        english { +"You are currently receiving a children’s pension. The Norwegian Parliament (Stortinget) has adopted new rules regarding children's pensions. Your pension has therefore been changed from 1 January 2024." },
                    )
                }.orShowIf(erGjenoppretting) {
                    text(
                        bokmal { +"Vi viser til forhåndsvarsel om ny barnepensjon. Du er innvilget barnepensjon på nytt fra " +
                                formatertVirkningsdato + " fordi begge foreldrene dine er registrert død." },
                        nynorsk { +"Vi viser til førehandsvarselet om ny barnepensjon. Du er innvilga ny barnepensjon frå og med " +
                                formatertVirkningsdato + " fordi begge foreldra dine er registrert død." },
                        english { +"We refer to the advance notice about a new children’s pension scheme. You have been granted a children's pension again from " +
                                formatertVirkningsdato + " both your parents are registered as deceased." }
                    )
                }.orIfNotNull(forskjelligAvdoedPeriode) { forskjelligAvdoed ->
                    ifNotNull(forskjelligAvdoed.foersteAvdoed, forskjelligAvdoed.senereAvdoed) { foersteAvdoed, senereAvdoed ->
                        val senereVirkningsdato = forskjelligAvdoed.senereVirkningsdato.format()
                        val avdoedNavn = foersteAvdoed.navn
                        val formatertDoedsdato = foersteAvdoed.doedsdato.format()
                        val formatertSenereDoedsdato = senereAvdoed.doedsdato.format()

                        text(
                            bokmal { +"Du er innvilget barnepensjon fra " + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". Barnepensjon endres fra " + senereVirkningsdato + " fordi den andre forelderen din er registrert død " + formatertSenereDoedsdato + ". " },
                            nynorsk { +"Du er innvilga barnepensjon frå og med " + formatertVirkningsdato + " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". Barnepensjonen din er endra frå " + senereVirkningsdato + " fordi begge foreldra dine er registrert som døde. " },
                            english { +"You have been granted a children's pension " + formatertVirkningsdato + " because " + avdoedNavn + " is registered as deceased on "+ formatertDoedsdato + ". Your children's pension will change on " + senereVirkningsdato + " because both your parents are registered as deceased. " },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Du er innvilget barnepensjon fra " +
                                    formatertVirkningsdato + " fordi begge foreldrene dine er registrert død." },
                            nynorsk { +"Du er innvilga barnepensjon frå " +
                                    formatertVirkningsdato + " fordi begge foreldra dine er registrert som døde." },
                            english { +"You have been granted a children's pension starting " +
                                    formatertVirkningsdato + " because both your parents are registered as deceased." }
                        )
                    }

                }.orShow {
                    text(
                        bokmal { +"Du er innvilget barnepensjon fra " +
                                formatertVirkningsdato + " fordi begge foreldrene dine er registrert død." },
                        nynorsk { +"Du er innvilga barnepensjon frå " +
                                formatertVirkningsdato + " fordi begge foreldra dine er registrert som døde." },
                        english { +"You have been granted a children's pension starting " +
                                formatertVirkningsdato + " because both your parents are registered as deceased." }
                    )
                }

                showIf(harUtbetaling) {
                    showIf(flerePerioder) {
                        text(
                            bokmal { +" Du får " + formatertBeloep +
                                    " hver måned før skatt fra " + formatertFom + "." },
                            nynorsk { +" Du får " + formatertBeloep +
                                    " per månad før skatt frå " + formatertFom + "." },
                            english { +" You will receive " + formatertBeloep +
                                    " each month before tax, starting on " + formatertFom + "." }
                        )
                    }.orShow {
                        text(
                            bokmal { +" Du får " + formatertBeloep + " hver måned før skatt." },
                            nynorsk { +" Du får " + formatertBeloep + " per månad før skatt." },
                            english { +" You will receive " + formatertBeloep + " each month before tax." },
                        )
                    }
                }.orShow {
                    text(
                        bokmal { +" Du får ikke utbetalt barnepensjon fordi den er redusert utfra det du" +
                                " mottar i uføretrygd fra Nav." },
                        nynorsk { +" Du får ikkje utbetalt barnepensjon, då denne har blitt redusert med" +
                                " utgangspunkt i uføretrygda du får frå Nav." },
                        english { +" You will not receive payments from the children’s pension because they" +
                                " have been reduced according to what you already receive in disability benefits from Nav." },
                    )
                }
            }

            paragraph {
                showIf(harUtbetaling and flerePerioder) {
                    text(
                        bokmal { +"Se beløp for tidligere perioder og hvordan vi har beregnet pensjonen din " +
                                "i vedlegget “Beregning av barnepensjon”." },
                        nynorsk { +"I vedlegget «Utrekning av barnepensjon» kan du sjå beløp for tidlegare periodar " +
                                "og korleis vi har rekna ut pensjonen." },
                        english { +"You can see amounts for previous periods and how we calculated your pension " +
                                "in the attachment, Calculation of Children’s Pension." }
                    )
                }.orShow {
                    text(
                        bokmal { +"Se hvordan vi har beregnet pensjonen din i vedlegget “Beregning av barnepensjon”." },
                        nynorsk { +"I vedlegget «Utrekning av barnepensjon» kan du sjå korleis vi har rekna ut pensjonen din." },
                        english { +"You can find more information about how we have calculated your" +
                                " children's pension in the attachment, Calculating the Children's Pension." },
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
                        bokmal { +"De nye reglene for barnepensjon har ingen søskenjustering, og du vil få pensjon til du er 20 år selv om du ikke er under utdanning." },
                        nynorsk { +"Dei nye reglane for barnepensjon har inga søskenjustering, og du vil få pensjon til du er 20 år sjølv om du ikkje er under utdanning." },
                        english { +"The new rules for children’s pensions have no sibling adjustment, and you will receive pension until you are 20 years old even if you are not in education." },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"Barnepensjon gis på bakgrunn av at" },
                    nynorsk { +"Du kan få barnepensjon når" },
                    english { +"The children’s pension has been granted because" },
                )
                list {
                    item {
                        text(
                            bokmal { +"du har mistet begge foreldrene dine" },
                            nynorsk { +"du har mista begge foreldre" },
                            english { +"you have lost both your parents" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du er under 20 år" },
                            nynorsk { +"du er under 20 år" },
                            english { +"you are under the age of 20" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du er medlem i folketrygden" },
                            nynorsk { +"du er medlem i folketrygda" },
                            english { +"you are a member of the national insurance scheme" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"minst en av foreldrene dine i de siste fem årene før dødsfallet " +
                                    "var medlem i folketrygden, eller fikk pensjon eller uføretrygd fra folketrygden." },
                            nynorsk { +"eine eller begge foreldra dine var medlem i folketrygda eller " +
                                    "fekk pensjon/uføretrygd frå folketrygda dei siste fem åra før sin død" },
                            english { +"at least one of your parents was a member of the national " +
                                    "insurance scheme in the last five years before his or her death, or received a " +
                                    "pension or disability pension from the scheme" },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven " +
                            "§§ 18-2, 18-3, 18-4, 18-5, 22-12 og 22-13." },
                    nynorsk { +"Vedtaket er fatta etter føresegnene om barnepensjon i folketrygdlova " +
                            "§§ 18-2, 18-3, 18-4, 18-5, 22-12 og 22-13." },
                    english { +"This decision has been made pursuant to the provisions regarding " +
                            "children's pensions in the National Insurance Act – " +
                            "sections 18-2, 18-3, 18-4, 18-5, 22-12 and 22-13." },
                )
            }
        }
    }
}
