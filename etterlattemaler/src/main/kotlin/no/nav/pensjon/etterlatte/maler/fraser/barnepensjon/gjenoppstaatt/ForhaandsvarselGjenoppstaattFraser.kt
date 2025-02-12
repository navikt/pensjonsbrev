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
                    Language.Nynorsk to "Reglar for barnepensjon før 1. januar 2024",
                    Language.English to "Rules for children’s pension before 1 January 2024",
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Language.Bokmal to "Barnepensjon ble utbetalt til den måneden du fylte 18 år.",
                            Language.Nynorsk to "Barnepensjon blei utbetalt fram til månaden du fylte 18 år.",
                            Language.English to "Children’s pension was paid out until the month you reached the age of 18.",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Barnepensjon ble utbetalt etter 18 år hvis du var under utdanning eller var lærling/praktikant, og dødsfallet skyldtes yrkesskade/yrkessykdom eller du var foreldreløs.",
                            Language.Nynorsk to "Barnepensjon blei utbetalt etter fylte 18 år dersom du var lærling/praktikant eller under utdanning, og dødsfallet skuldast yrkesskade/yrkessjukdom eller du var foreldrelaus.",
                            Language.English to "Children’s pension was paid out after the age of 18 if you were in education or an apprentice/trainee and the death was due to an occupational injury/occupational illness or you were an orphan.",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Barnepensjon ble beregnet samlet når flere barn med barnepensjon ble oppdratt sammen, og beløpet ble utbetalt likt til hvert barn.",
                            Language.Nynorsk to "Barnepensjon blei rekna ut samla når fleire barn med barnepensjon blei oppdregne saman, og beløpet blei utbetalt likt til kvart barn.",
                            Language.English to "Children’s pension was calculated as a whole when several children with children’s pension were brought up together, and the amount was paid out equally to each child.",
                        )
                    }
                }
            }
            title2 {
                text(
                    Language.Bokmal to "Regler for barnepensjon fra 1. januar 2024.",
                    Language.Nynorsk to "Reglar for barnepensjon frå 1. januar 2024.",
                    Language.English to "Rules for children’s pension after 1 January 2024",
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Language.Bokmal to "Satsen for barnepensjon er økt",
                            Language.Nynorsk to "Satsen for barnepensjon er auka.",
                            Language.English to "The rate for children’s pension has increased.",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Barnepensjon utbetales til du fyller 20 år.",
                            Language.Nynorsk to "Barnepensjon blir utbetalt til du fyller 20 år.",
                            Language.English to "Children’s pension is paid until you reach the age of 20.",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Det er ikke lenger et krav om at du er under utdanning, er lærling eller praktikant for å få barnepensjon etter fylte 18 år.",
                            Language.Nynorsk to "Det blir ikkje lenger stilt krav om at du må vere lærling/praktikant eller under utdanning for å få barnepensjon etter fylte 18 år.",
                            Language.English to "The requirement that you had to be in education, an apprentice or trainee to receive children’s pension after the age of 18 no longer applies.",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Satsen for barnepensjon er den samme, uavhengig av antall søsken.",
                            Language.Nynorsk to "Satsen for barnepensjon er den same, uavhengig av talet på søsken.",
                            Language.English to "The rate for children’s pension is the same, regardless of the number of siblings.",
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
                        Language.Nynorsk to "Du kan sjekke og endre kontonummeret du er registrert med, ved å logge inn på nav.no. Dersom du ikkje kan melde frå digitalt, melder du i staden frå om endringane via post. Legg i så fall ved kopi av gyldig legitimasjon.",
                        Language.English to "You can check and change your registered account number by logging in to nav.no. If you cannot notify us digitally, you can notify of any changes by post. In such case, you must enclose a copy of valid ID.",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Du finner mer informasjon og lenke til riktig skjema på ${Constants.KONTONUMMER_URL}.",
                        Language.Nynorsk to "Du finn meir informasjon og lenkje til rett skjema på ${Constants.KONTONUMMER_URL}.",
                        Language.English to "You can find additional information and a link to the correct form at: ${Constants.Engelsk.KONTONUMMER_URL}.",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten at du har gitt beskjed om det. Du må kontakte Skatteetaten for å avklare om du bør endre skattekortet eller sende inn frivillig skattetrekk til Nav.",
                        Language.Nynorsk to "Barnepensjon er skattepliktig, men vi trekkjer ikkje skatt utan at du har gitt beskjed om det. Kontakt Skatteetaten for å avklare om du bør endre skattekortet eller sende inn frivillig skattetrekk til Nav.",
                        Language.English to "Children’s pension is taxable; however, we do not deduct tax if you do not notify us to do so. You must contact the Tax Administration to clarify whether you need to change your tax card or submit voluntary tax deductions to Nav.",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Language.Bokmal to "Hvis du logger på nav.no med BankID, Buypass eller Comfides, kan du endre kontonummer i «Personopplysninger» på www.${Constants.NAV_URL}. Hvis du ikke kan melde fra digitalt, kan du melde om endringer via post.",
                        Language.Nynorsk to "Dersom du loggar på nav.no med BankID, Buypass eller Comfides, kan du endre kontonummer under «Personopplysningar» på www.${Constants.NAV_URL}. Dersom du ikkje kan melde frå digitalt, melder du i staden frå om endringane via post.",
                        Language.English to "If you log on to nav.no using BankID, Buypass or Commfides, you can change the account number in «Personal information» at www.${Constants.NAV_URL}. If you cannot notify us digitally, you can notify of any changes by post.",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Du finner skjema på ${Constants.Utland.ENDRE_KONTONUMMER_SKJEMA_URL}. Husk underskrift på skjemaet og legg ved kopi av gyldig legitimasjon.",
                        Language.Nynorsk to "Du finn skjema på ${Constants.Utland.ENDRE_KONTONUMMER_SKJEMA_URL}. Hugs å skrive under på skjemaet og leggje ved kopi av gyldig legitimasjon.",
                        Language.English to "You will find the correct form at: ${Constants.Utland.ENDRE_KONTONUMMER_SKJEMA_URL}. Remember to sign the form and to enclose a copy of valid ID.",
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten at du har gitt beskjed om det. Skatteetaten svarer på spørsmål om skatt på pensjon for deg som ikke er skattemessig bosatt i Norge. Les mer om skatt på ${Constants.SKATTETREKK_KILDESKATT_URL}.",
                        Language.Nynorsk to "Barnepensjon er skattepliktig, men vi trekkjer ikkje skatt utan at du har gitt beskjed om det. Skatteetaten svarer på spørsmål om skatt på pensjon for deg som ikkje er skattemessig busett i Noreg. Les meir om skatt på ${Constants.SKATTETREKK_KILDESKATT_URL}.",
                        Language.English to "Children’s pension is taxable; however, we do not deduct tax if you do not notify us to do so. The Tax Administration will respond to any queries regarding tax on pensions for those who are resident in Norway for tax purposes. Read more about tax at:  ${Constants.SKATTETREKK_KILDESKATT_URL}.",
                    )
                }
            }
        }
    }
}
