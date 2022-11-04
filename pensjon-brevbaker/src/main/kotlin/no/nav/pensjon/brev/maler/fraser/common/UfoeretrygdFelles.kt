package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.maler.fraser.Constants
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object UfoeretrygdFelles {

    object TBU2223 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned.",
                    Language.Nynorsk to "Uføretrygda blir framleis utbetalt seinast den 20. i kvar månad.",
                    Language.English to "Your disability benefit will still be paid no later than the 20th of every month."
                )
            }
        }
    }

    object TBU1128 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet.",
                    Language.Nynorsk to "I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet.",
                    Language.English to "In this letter we will explain your rights and obligations. Therefore, it is important that you read the whole letter."
                )
            }
        }
    }

    object TBU2364 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Du må melde fra om eventuell inntekt",
                    Language.Nynorsk to "Du må melde frå om eventuell inntekt",
                    Language.English to "Report any income"
                )
            }
        }
    }

    object TBU2365 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på nav.no. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd ved siden av inntekten din.",
                    Language.Nynorsk to "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd. Dette kan du gjere under menyvalet «uføretrygd» når du logger deg inn på nav.no. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd ved sida av inntekta di.",
                    Language.English to "If you are working or are planning to work, you must report any changes in your income. It is important that you report this as soon as possible, so that you receive the correct disability benefit payments. You can register your change in income under the option “uføretrygd” at nav.no. You can register how much you expect to earn in the calendar year. You will then be able to see how much disability benefit you will receive in addition to your income."
                )
            }
        }
    }

    object TBU2212 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Du må melde fra om endringer",
                    Language.Nynorsk to "Du må melde frå om endringar",
                    Language.English to "You must notify any changes"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget «Orientering om rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                    Language.Nynorsk to "Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget «Orientering om rettar og plikter» ser du kva endringar du må seie frå om.",
                    Language.English to "You must notify us immediately of any changes in your situation. In the attachment “Information about rights and obligations” you will see which changes you must report."
                )
            }
        }
    }

    object TBU2213 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Du har rett til å klage",
                    Language.Nynorsk to "Du har rett til å klage",
                    Language.English to "You have the right of appeal"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget «Orientering om rettigheter og plikter» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Language.Nynorsk to "Viss du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. I vedlegget «Orientering om rettar og plikter» får du vite meir om korleis du går fram. Du finn skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Language.English to "If you believe the decision is wrong, you may appeal. The deadline for appeal is six weeks from the date you received the decision. In the attachment “Information about rights and obligations”, you can find out more about how to proceed. You will find forms and information at ${Constants.KLAGE_URL}."

                )
            }
        }
    }

    object TBU2242 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg «Orientering om rettigheter og plikter» for informasjon om hvordan du går fram.",
                    Language.Nynorsk to "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «Orientering om rettar og plikter» for informasjon om korleis du går fram.",
                    Language.English to "You are entitled to see your case documents. Refer to the attachment “Rights and obligations” for information about how to proceed."
                )
            }
        }
    }

    object TBU1228 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Skattekort",
                    Language.Nynorsk to "Skattekort",
                    Language.English to "Tax card"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til NAV fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. Du bør likevel sjekke at du har riktig skattekort. Skattekortet kan du endre på skatteetaten.no. Under menyvalget «uføretrygd» når du logger deg inn på nav.no, kan du se hvilket skattetrekk som er registrert hos NAV.",
                    Language.Nynorsk to "Uføretrygd blir skattlagd som lønsinntekt. Du treng ikkje levere skattekortet ditt til NAV, fordi skatteopplysningane dine blir sende elektronisk frå Skatteetaten. Du bør likevel sjekke at du har rett skattekort. Skattekortet kan du endre på skatteetaten.no. Under menyvalet «uføretrygd» når du logger deg inn på nav.no, kan du sjå kva skattetrekk som er registrert hos NAV.",
                    Language.English to "You do not need to submit your tax card to NAV because your tax details are sent electronically from the Norwegian Tax Administration. However, you should check that you have the correct tax card. You may change your tax card under skatteetaten.no. You may see your registered income tax rate under the option “uføretrygd” at nav.no."
                )
            }
        }
    }

    data class TBU3730(
        val brukerBorInorge: Expression<Boolean>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(brukerBorInorge) {
                title1 {
                    text(
                        Language.Bokmal to "Skatt for deg som bor i utlandet",
                        Language.Nynorsk to "Skatt for deg som bur i utlandet",
                        Language.English to "Tax for people who live abroad"
                    )
                }
                paragraph {
                    text(
                        Language.Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.",
                        Language.Nynorsk to "Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på skatteetaten.no. Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur.",
                        Language.English to "You can find more information about withholding tax to Norway at skatteetaten.no. For information about taxation from your country of residence, you can contact the locale tax authorities."
                    )
                }
            }
        }
    }
}
