package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_URL
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

object Ufoeretrygd {

    /**
     * TBU1120, TBU1121, TBU1122, TBU1254, TBU1253, TBU1123
     */
    data class Beloep(
        val perMaaned: Expression<Kroner>,
        val ektefelle: Expression<Boolean>,
        val ufoeretrygd: Expression<Boolean>,
        val gjenlevende: Expression<Boolean>,
        val fellesbarn: Expression<Boolean>,
        val saerkullsbarn: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                val kroner = perMaaned.format()
                showIf(ufoeretrygd) {
                    showIf(not(fellesbarn) and not(saerkullsbarn) and not(ektefelle) and not(gjenlevende)) {
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd per månad før skatt.".expr(),
                            English to "Your monthly disability benefit payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf((fellesbarn or saerkullsbarn) and not(gjenlevende) and not(ektefelle)) {
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd og barnetillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd og barnetillegg per månad før skatt.".expr(),
                            English to "Your monthly disability benefit and child supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf(not(fellesbarn) and not(saerkullsbarn) and not(ektefelle) and gjenlevende) {
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd og gjenlevendetillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd og attlevandetillegg per månad før skatt.".expr(),
                            English to "Your monthly disability benefit and survivor's supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf((fellesbarn or saerkullsbarn) and ektefelle and not(gjenlevende)) {
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd, barne- og ektefelletillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd, barne- og ektefelletillegg per månad før skatt.".expr(),
                            English to "Your monthly disability benefit, child supplement and survivor's supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf(not(fellesbarn) and not(saerkullsbarn) and ektefelle and not(gjenlevende)) {
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd og ektefelletillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd og ektefelletillegg per månad før skatt.".expr(),
                            English to "Your monthly disability benefit and spouse supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf((fellesbarn or saerkullsbarn) and not(ektefelle) and gjenlevende) {
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt.".expr(),
                            English to "Your monthly disability benefit, child supplement and spouse supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }
                }.orShow {
                    showIf((fellesbarn or saerkullsbarn) and not(ektefelle) and not(gjenlevende)) {
                        // TBU4082
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i barnetillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i barnetillegg per månad før skatt.".expr(),
                            English to "Your monthly child supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf((fellesbarn or saerkullsbarn) and ektefelle and not(gjenlevende)) {
                        // TBU4083
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i barne- og ektefelletillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i barne- og ektefelletillegg per månad før skatt.".expr(),
                            English to "Your monthly child supplement and spouse supplement  payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf(not(fellesbarn or saerkullsbarn) and not(gjenlevende) and (ektefelle)) {
                        // TBU4084
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i ektefelletillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i ektefelletillegg per månad før skatt.".expr(),
                            English to "Your monthly spouse supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }
                }
            }
    }

    /**
     * TBU3011
     */
    object HjemmelSivilstand : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-13 og § 22-12.",
                    Nynorsk to "Vedtaket har vi gjort etter folketrygdlova § 12-13 og § 22-12.",
                )
            }
    }

    /**
     * TBU1174
     */
    object VirkningFomOverskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            title1 {
                text(
                    Bokmal to "Dette er virkningstidspunktet ditt",
                    Nynorsk to "Dette er verknadstidspunktet ditt",
                    English to "This is your effective date",
                )
            }
    }

    /**
     * TBU2529x
     */
    data class VirkningFraOgMed(val kravVirkningFraOgMed: Expression<LocalDate>) :
        OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
            paragraph {
                val dato = kravVirkningFraOgMed.format()
                textExpr(
                    Bokmal to "Uføretrygden din er endret fra ".expr() + dato + ". Dette kaller vi virkningstidspunktet. Du vil derfor få en ny utbetaling fra og med måneden vilkåret er oppfylt.".expr(),
                    Nynorsk to "Uføretrygda di er endra frå ".expr() + dato + ". Dette kallar vi verknadstidspunktet. Du vil derfor få ny utbetaling frå og med månaden vilkåret er oppfylt.".expr(),
                )
            }
    }

    /**
     * TBU1227, sjekkUtbetalingeneOverskrift_001, sjekkUtbetalingeneUT_001
     */
    object SjekkUtbetalingene : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Sjekk utbetalingene dine",
                    Nynorsk to "Sjekk utbetalingane dine",
                    English to "Information about your payments",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. " +
                            "Du kan se alle utbetalingene du har mottatt på ${Constants.DITT_NAV}. Her kan du også endre kontonummeret ditt.",

                    Nynorsk to "Du får uføretrygd utbetalt den 20. kvar månad, eller seinast siste yrkedag før denne datoen. " +
                            "Du kan sjå alle utbetalingar du har fått på ${Constants.DITT_NAV}. Her kan du også endre kontonummeret ditt.",

                    English to "Your disability benefit will be paid on the 20th of each month or no later than the last business day before this date. " +
                            "To see all the payments you have received, go to: ${Constants.DITT_NAV}. You may also change your account number here.",
                )
            }
        }
    }

    // TBU2223
    data class UtbetalingsdatoUfoeretrygd(val faarUtbetaltUfoeretrygd: Expression<Boolean>) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(faarUtbetaltUfoeretrygd) {
                paragraph {
                    text(
                        Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned.",
                        Nynorsk to "Uføretrygda blir framleis utbetalt seinast den 20. i kvar månad.",
                        English to "Your disability benefit will still be paid no later than the 20th of every month."
                    )
                }
            }
        }
    }

    // TBU1128
    object ViktigAALeseHeleBrevet : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet.",
                    Nynorsk to "I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet.",
                    English to "In this letter we will explain your rights and obligations. Therefore, it is important that you read the whole letter."
                )
            }
        }
    }

    // TBU2364, MeldInntektUTOverskrift_001
    object MeldeFraOmEventuellInntektOverskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Du må melde fra om eventuell inntekt",
                    Nynorsk to "Du må melde frå om eventuell inntekt",
                    English to "Report any income"
                )
            }
        }
    }

    // TBU2365, MeldInntektUT_001
    object MeldeFraOmEventuellInntekt : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd ved siden av inntekten din.",
                    Nynorsk to "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd. Dette kan du gjere under menyvalet «uføretrygd» når du logger deg inn på $NAV_URL. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd ved sida av inntekta di.",
                    English to "If you are working or are planning to work, you must report any changes in your income. It is important that you report this as soon as possible, so that you receive the correct disability benefit payments. You can register your change in income under the option “uføretrygd” at $NAV_URL. You can register how much you expect to earn in the calendar year. You will then be able to see how much disability benefit you will receive in addition to your income."
                )
            }
        }
    }

    // MeldInntektUTBT_001
    object MeldeFraOmEventuellInntektBarnetillegg : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. " +
                            "Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd og barnetillegg. " +
                            "Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL. " +
                            "Her kan du legge inn hvor mye du forventer å tjene i løpet av året. " +
                            "Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd og barnetillegg.",

                    Nynorsk to "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. " +
                            "Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd og barnetillegg. " +
                            "Dette kan du gjere under menyvalet «uføretrygd» når du logger deg inn på $NAV_URL. " +
                            "Her kan du leggje inn kor mykje du forventar å tene i løpet av året. " +
                            "Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd og barnetillegg.",

                    English to "If you are working or are planning to work, you must report any changes in your income. " +
                            "It is important that you report this as soon as possible, so that you receive the correct disability benefit and child supplement payments. " +
                            "You can register your change in income under the option “uføretrygd” at $NAV_URL. " +
                            "You can register how much you expect to earn in the calendar year. " +
                            "You will then be able to see how much disability benefit and child supplement you will receive."
                )
            }
    }

    // TBU2212, TBU1223, TBU1224, MeldEndringerPesys_001
    object MeldeFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "You must notify any changes"
                )
            }
            paragraph {
                text(
                    Bokmal to "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget «Dine rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                    Nynorsk to "Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget «Dine rettar og plikter» ser du kva endringar du må seie frå om.",
                    English to "You must notify us immediately of any changes in your situation. In the attachment «Your rights and obligations» you will see which changes you must report."
                )
            }
        }
    }

    // TBU1228, SkattekortOverskrift_001, SkattekortUT_001
    object Skattekort : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Skattekort",
                    Nynorsk to "Skattekort",
                    English to "Tax card"
                )
            }
            paragraph {
                text(
                    Bokmal to "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til NAV fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. Du bør likevel sjekke at du har riktig skattekort. Skattekortet kan du endre på $SKATTEETATEN_URL. Under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL, kan du se hvilket skattetrekk som er registrert hos NAV.",
                    Nynorsk to "Uføretrygd blir skattlagd som lønsinntekt. Du treng ikkje levere skattekortet ditt til NAV, fordi skatteopplysningane dine blir sende elektronisk frå Skatteetaten. Du bør likevel sjekke at du har rett skattekort. Skattekortet kan du endre på $SKATTEETATEN_URL. Under menyvalet «uføretrygd» når du logger deg inn på $NAV_URL, kan du sjå kva skattetrekk som er registrert hos NAV.",
                    English to "You do not need to submit your tax card to NAV because your tax details are sent electronically from the Norwegian Tax Administration. However, you should check that you have the correct tax card. You may change your tax card under $SKATTEETATEN_URL. You may see your registered income tax rate under the option “uføretrygd” at $NAV_URL."
                )
            }
        }
    }

    // TBU3730, SkattBorIUtlandPesys_001
    data class SkattForDegSomBorIUtlandet(val brukerBorInorge: Expression<Boolean>) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(not(brukerBorInorge)) {
                title1 {
                    text(
                        Bokmal to "Skatt for deg som bor i utlandet",
                        Nynorsk to "Skatt for deg som bur i utlandet",
                        English to "Tax for people who live abroad"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på $SKATTEETATEN_URL. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.",
                        Nynorsk to "Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på $SKATTEETATEN_URL. Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur.",
                        English to "You can find more information about withholding tax to Norway at $SKATTEETATEN_URL. For information about taxation from your country of residence, you can contact the locale tax authorities."
                    )
                }
            }
        }
    }
}



