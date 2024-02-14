package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.gjenoppstaatt

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

object ForhaandsvarselGjenoppstaattFraser {

    object ReglerForBarnepensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Regler for barnepensjon før 1. januar 2024",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Language.Bokmal to "Barnepensjon ble utbetalt til den måneden du fylte 18 år",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Barnepensjon ble utbetalt etter 18 år hvis du var under utdanning eller var lærling/praktikant, og dødsfallet skyldtes yrkesskade/yrkessykdom eller du var foreldreløs",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Barnepensjon ble beregnet samlet når flere barn med barnepensjon ble oppdratt sammen, og beløpet ble utbetalt likt til hvert barn.",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }
            }
            title2 {
                text(
                    Language.Bokmal to "Regler for barnepensjon fra 1. januar 2024",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Language.Bokmal to "Satsen for barnepensjon er økt",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Barnepensjon utbetales til du fyller 20 år",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Det er ikke lenger et krav om at du er under utdanning, er lærling eller praktikant for å få barnepensjon etter fylte 18 år",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Satsen for barnepensjon er den samme, uavhengig av antall søsken.",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }
            }
        }
    }

    data class KontonummerOgSkatt(val erBosattUtlandet: Expression<Boolean>) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(erBosattUtlandet.not()) {
                paragraph {
                    text(
                        Language.Bokmal to "Du kan sjekke og endre kontonummeret som er registrert på deg ved å logge inn på nav.no. Hvis du ikke kan melde fra digitalt, kan du melde om endringer per post. Du må da legge ved kopi av gyldig legitimasjon.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Du finner mer informasjon og lenke til riktig skjema på ${Constants.KONTONUMMER_URL}.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten at du har gitt beskjed om det. Du må kontakte Skatteetaten for å avklare om du bør endre skattekortet eller sende inn frivillig skattetrekk til NAV.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Language.Bokmal to "Hvis du logger på nav.no med BankID, Buypass eller Comfides, kan du endre kontonummer i \"Personopplysninger\" på www.${Constants.NAV_URL}. Hvis du ikke kan melde fra digitalt, kan du melde om endringer via post.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Du finner skjema på ${Constants.Utland.ENDRE_KONTONUMMER_SKJEMA_URL}. Husk underskrift på skjemaet og legg ved kopi av gyldig legitimasjon.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten at du har gitt beskjed om det. Skatteetaten svarer på spørsmål om skatt på pensjon for deg som ikke er skattemessig bosatt i Norge. Les mer om skatt på ${Constants.SKATTETREKK_KILDESKATT_URL}.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }
        }

    }
}