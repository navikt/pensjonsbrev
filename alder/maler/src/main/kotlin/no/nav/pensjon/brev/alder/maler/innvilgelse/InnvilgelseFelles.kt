package no.nav.pensjon.brev.alder.maler.innvilgelse

import no.nav.pensjon.brev.alder.maler.felles.Constants.UTBETALINGER_URL
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

object InnvilgelseFelles {
    // hvisFlyttetBosattEØS / hvisFlyttetBosattAvtaleland
    data class HvisFlytetFaktiskBostedsland(
        val eksportTrygdeavtaleAvtaleland: Expression<Boolean>,
        val eksportTrygdeavtaleEOS: Expression<Boolean>,
        val faktiskBostedsland: Expression<String>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(eksportTrygdeavtaleEOS or eksportTrygdeavtaleAvtaleland) {
                // hvisFlyttetBosattEØS / hvisFlyttetBosattAvtaleland
                paragraph {
                    text(
                        bokmal {
                            +"Vi forutsetter at du bor i " + faktiskBostedsland + ". Hvis du skal flytte til et " + ifElse(
                                eksportTrygdeavtaleEOS,
                                ifTrue = "land utenfor EØS-området",
                                ifFalse = "annet land"
                            ) + ", må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon."
                        },
                        nynorsk {
                            +"Vi føreset at du bur i " + faktiskBostedsland + ". Dersom du skal flytte til eit " + ifElse(
                                eksportTrygdeavtaleEOS,
                                ifTrue = "land utanfor EØS-området",
                                ifFalse = "anna land"
                            ) + ", må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon."
                        },
                        english {
                            +"We presume that you live in " + faktiskBostedsland + ". If you are moving to " + ifElse(
                                eksportTrygdeavtaleEOS,
                                ifTrue = "a country outside the EEA region",
                                ifFalse = "another country"
                            ) + ", it is important that you contact Nav. We will then reassess your eligibility for retirement pension."
                        }
                    )
                }
            }
        }
    }


    // TODO: bruken av dette bør kanskje erstattes med den i AlderspensjonFelles
    // Er gjort sånn for å ikke endre noe i den direkte flyttinga fra pensjonsmodul til aldersmodul
    // utbetalingsInfoMndUtbet_001
    object Utbetalingsinformasjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { +
                    "Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på $UTBETALINGER_URL."+
                            "Her kan du også endre kontonummeret ditt. " +
                            "Hvis du har andre pensjonsutbetalinger gjennom Nav, blir de utbetalt i tillegg til alderspensjonen."
                    },
                    nynorsk { +
                    "Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på $UTBETALINGER_URL." +
                            "Her kan du også endre kontonummeret ditt. "+
                            "Om du har andre pensjonsutbetalingar gjennom Nav, blir dei utbetalte i tillegg til alderspensjonen"
                    },
                    english { +
                    "Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at $UTBETALINGER_URL."+
                            "Here you can also change your bank account number. "+
                            "If you receive other pension payments through Nav, these will be paid in addition to your retirement pension."
                    },
                )
            }
        }
    }

    // TODO: bruken av dette bør kanskje erstattes med den i AlderspensjonFelles
    // pensjonFraAndreInfoAP_001
    object InfoPensjonFraAndreAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Andre pensjonsordninger" },
                    nynorsk { + "Andre pensjonsordningar" },
                    english { + "Other pension schemes" },
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Mange er tilknyttet en eller flere offentlige eller private pensjonsordninger som de har pensjonsrettigheter fra. " +
                            "Du bør kontakte dem du har slike ordninger med for å undersøke hvilke rettigheter du kan ha. Du kan også undersøke med siste arbeidsgiver. " },
                    nynorsk { +
                    "Mange er knytte til ei eller fleire offentlege eller private pensjonsordningar som de har pensjonsrettar frå. " +
                            "Du bør kontakte dei du har slike ordningar med for å undersøke kva for rettar du har. Du kan også undersøkje med siste arbeidsgivar." },
                    english { +
                    "Many people are also members of one or more public or private pension schemes where they also have pension rights. " +
                            "You must contact the company/ies you have pension arrangements with, if you have any questions about this. You can also contact your most recent employer." },
                )
            }
        }
    }
}