package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode.ANNEN_FORLD_RETT_BT
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode.ANNET
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode.BARN_FLYTTET_IKKE_AVT_LAND
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode.BARN_OPPH_IKKE_AVT_LAND
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode.BRK_FORSO_IKKE_BARN
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode.BRUKER_FLYTTET_IKKE_AVT_LAND
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode.BT_GITT_TIL_ANNEN
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode.BT_INNT_OVER_1G
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode.BT_OVER_18
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode.IKKE_MOTTATT_DOK
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BtBegrunnelseCode.MINDRE_ETT_AR_BT_FLT
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnetilleggUTDtoSelectors.fodselsdato
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnetilleggUTDtoSelectors.begrunnelse
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_URL
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
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
                        text(
                            bokmal { + "Du får " + kroner + " i uføretrygd per måned før skatt." },
                            nynorsk { + "Du får " + kroner + " i uføretrygd per månad før skatt." },
                            english { + "Your monthly disability benefit payment will be " + kroner + " before tax." }
                        )
                    }.orShowIf((fellesbarn or saerkullsbarn) and not(gjenlevende) and not(ektefelle)) {
                        text(
                            bokmal { + "Du får " + kroner + " i uføretrygd og barnetillegg per måned før skatt." },
                            nynorsk { + "Du får " + kroner + " i uføretrygd og barnetillegg per månad før skatt." },
                            english { + "Your monthly disability benefit and child supplement payment will be " + kroner + " before tax." }
                        )
                    }.orShowIf(not(fellesbarn) and not(saerkullsbarn) and not(ektefelle) and gjenlevende) {
                        text(
                            bokmal { + "Du får " + kroner + " i uføretrygd og gjenlevendetillegg per måned før skatt." },
                            nynorsk { + "Du får " + kroner + " i uføretrygd og attlevandetillegg per månad før skatt." },
                            english { + "Your monthly disability benefit and survivor's supplement payment will be " + kroner + " before tax." }
                        )
                    }.orShowIf((fellesbarn or saerkullsbarn) and ektefelle and not(gjenlevende)) {
                        text(
                            bokmal { + "Du får " + kroner + " i uføretrygd, barne- og ektefelletillegg per måned før skatt." },
                            nynorsk { + "Du får " + kroner + " i uføretrygd, barne- og ektefelletillegg per månad før skatt." },
                            english { + "Your monthly disability benefit, child supplement and survivor's supplement payment will be " + kroner + " before tax." }
                        )
                    }.orShowIf(not(fellesbarn) and not(saerkullsbarn) and ektefelle and not(gjenlevende)) {
                        text(
                            bokmal { + "Du får " + kroner + " i uføretrygd og ektefelletillegg per måned før skatt." },
                            nynorsk { + "Du får " + kroner + " i uføretrygd og ektefelletillegg per månad før skatt." },
                            english { + "Your monthly disability benefit and spouse supplement payment will be " + kroner + " before tax." }
                        )
                    }.orShowIf((fellesbarn or saerkullsbarn) and not(ektefelle) and gjenlevende) {
                        text(
                            bokmal { + "Du får " + kroner + " i uføretrygd, barne- og gjenlevendetillegg per måned før skatt." },
                            nynorsk { + "Du får " + kroner + " i uføretrygd, barne- og attlevandetillegg per månad før skatt." },
                            english { + "Your monthly disability benefit, child supplement and spouse supplement payment will be " + kroner + " before tax." }
                        )
                    }
                }.orShow {
                    showIf((fellesbarn or saerkullsbarn) and not(ektefelle) and not(gjenlevende)) {
                        // TBU4082
                        text(
                            bokmal { + "Du får " + kroner + " i barnetillegg per måned før skatt." },
                            nynorsk { + "Du får " + kroner + " i barnetillegg per månad før skatt." },
                            english { + "Your monthly child supplement payment will be " + kroner + " before tax." }
                        )
                    }.orShowIf((fellesbarn or saerkullsbarn) and ektefelle and not(gjenlevende)) {
                        // TBU4083
                        text(
                            bokmal { + "Du får " + kroner + " i barne- og ektefelletillegg per måned før skatt." },
                            nynorsk { + "Du får " + kroner + " i barne- og ektefelletillegg per månad før skatt." },
                            english { + "Your monthly child supplement and spouse supplement  payment will be " + kroner + " before tax." }
                        )
                    }.orShowIf(not(fellesbarn or saerkullsbarn) and not(gjenlevende) and (ektefelle)) {
                        // TBU4084
                        text(
                            bokmal { + "Du får " + kroner + " i ektefelletillegg per måned før skatt." },
                            nynorsk { + "Du får " + kroner + " i ektefelletillegg per månad før skatt." },
                            english { + "Your monthly spouse supplement payment will be " + kroner + " before tax." }
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
                    bokmal { + "Vedtaket er gjort etter folketrygdloven § 12-13 og § 22-12." },
                    nynorsk { + "Vedtaket har vi gjort etter folketrygdlova § 12-13 og § 22-12." },
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
                    bokmal { + "Dette er virkningstidspunktet ditt" },
                    nynorsk { + "Dette er verknadstidspunktet ditt" },
                    english { + "This is your effective date" },
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
                text(
                    bokmal { + "Uføretrygden din er endret fra " + dato + ". Dette kaller vi virkningstidspunktet. Du vil derfor få en ny utbetaling fra og med måneden vilkåret er oppfylt." },
                    nynorsk { + "Uføretrygda di er endra frå " + dato + ". Dette kallar vi verknadstidspunktet. Du vil derfor få ny utbetaling frå og med månaden vilkåret er oppfylt." },
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
                    bokmal { + "Sjekk utbetalingene dine" },
                    nynorsk { + "Sjekk utbetalingane dine" },
                    english { + "Information about your payments" },
                )
            }

            paragraph {
                text(
                    bokmal { + "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. " +
                            "Du kan se alle utbetalingene du har mottatt på ${Constants.DITT_NAV}. Her kan du også endre kontonummeret ditt." },

                    nynorsk { + "Du får uføretrygd utbetalt den 20. kvar månad, eller seinast siste yrkedag før denne datoen. " +
                            "Du kan sjå alle utbetalingar du har fått på ${Constants.DITT_NAV}. Her kan du også endre kontonummeret ditt." },

                    english { + "Your disability benefit will be paid on the 20th of each month or no later than the last business day before this date. " +
                            "To see all the payments you have received, go to: ${Constants.DITT_NAV}. You may also change your account number here." },
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
                        bokmal { + "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned." },
                        nynorsk { + "Uføretrygda blir framleis utbetalt seinast den 20. i kvar månad." },
                        english { + "Your disability benefit will still be paid no later than the 20th of every month." }
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
                    bokmal { + "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet." },
                    nynorsk { + "I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet." },
                    english { + "In this letter we will explain your rights and obligations. Therefore, it is important that you read the whole letter." }
                )
            }
        }
    }

    // TBU2364, MeldInntektUTOverskrift_001
    object MeldeFraOmEventuellInntektOverskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du må melde fra om eventuell inntekt" },
                    nynorsk { + "Du må melde frå om eventuell inntekt" },
                    english { + "Report any income" }
                )
            }
        }
    }

    // TBU2365, MeldInntektUT_001
    object MeldeFraOmEventuellInntekt : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { + "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget " + quoted("uføretrygd") + " når du logger deg inn på $NAV_URL. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd ved siden av inntekten din." },
                    nynorsk { + "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd. Dette kan du gjere under menyvalet " + quoted("uføretrygd") + " når du logger deg inn på $NAV_URL. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd ved sida av inntekta di." },
                    english { + "If you are working or are planning to work, you must report any changes in your income. It is important that you report this as soon as possible, so that you receive the correct disability benefit payments. You can register your change in income under the option " + quoted("uføretrygd") +" at $NAV_URL. You can register how much you expect to earn in the calendar year. You will then be able to see how much disability benefit you will receive in addition to your income." }
                )
            }
        }
    }

    // MeldInntektUTBT_001
    object MeldeFraOmEventuellInntektBarnetillegg : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    bokmal { + "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. " +
                            "Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd og barnetillegg. " +
                            "Dette kan du gjøre under menyvalget " + quoted("uføretrygd") +" når du logger deg inn på $NAV_URL. " +
                            "Her kan du legge inn hvor mye du forventer å tjene i løpet av året. " +
                            "Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd og barnetillegg." },

                    nynorsk { + "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. " +
                            "Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd og barnetillegg. " +
                            "Dette kan du gjere under menyvalet " + quoted("uføretrygd") +" når du logger deg inn på $NAV_URL. " +
                            "Her kan du leggje inn kor mykje du forventar å tene i løpet av året. " +
                            "Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd og barnetillegg." },

                    english { + "If you are working or are planning to work, you must report any changes in your income. " +
                            "It is important that you report this as soon as possible, so that you receive the correct disability benefit and child supplement payments. " +
                            "You can register your change in income under the option " + quoted("uføretrygd") +" at $NAV_URL. " +
                            "You can register how much you expect to earn in the calendar year. " +
                            "You will then be able to see how much disability benefit and child supplement you will receive." }
                )
            }
    }

    // TBU2212, TBU1223, TBU1224, MeldEndringerPesys_001
    object MeldeFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du må melde fra om endringer" },
                    nynorsk { + "Du må melde frå om endringar" },
                    english { + "You must notify any changes" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget " },
                    nynorsk { + "Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget " },
                    english { + "You must notify us immediately of any changes in your situation. In the attachment " }
                )
                namedReference(vedleggDineRettigheterOgPlikterUfoere)
                text(
                    bokmal { + " ser du hvilke endringer du må si fra om." },
                    nynorsk { + " ser du kva endringar du må seie frå om." },
                    english { + " you will see which changes you must report." }
                )
            }
        }
    }

    // TBU1228, SkattekortOverskrift_001, SkattekortUT_001
    object Skattekort : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Skattekort" },
                    nynorsk { + "Skattekort" },
                    english { + "Tax card" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til Nav fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. Du bør likevel sjekke at du har riktig skattekort. Skattekortet kan du endre på $SKATTEETATEN_URL. Under menyvalget " + quoted("uføretrygd") +" når du logger deg inn på $NAV_URL, kan du se hvilket skattetrekk som er registrert hos Nav." },
                    nynorsk { + "Uføretrygd blir skattlagd som lønsinntekt. Du treng ikkje levere skattekortet ditt til Nav, fordi skatteopplysningane dine blir sende elektronisk frå Skatteetaten. Du bør likevel sjekke at du har rett skattekort. Skattekortet kan du endre på $SKATTEETATEN_URL. Under menyvalet " + quoted("uføretrygd") +" når du logger deg inn på $NAV_URL, kan du sjå kva skattetrekk som er registrert hos Nav." },
                    english { + "You do not need to submit your tax card to Nav because your tax details are sent electronically from the Norwegian Tax Administration. However, you should check that you have the correct tax card. You may change your tax card under $SKATTEETATEN_URL. You may see your registered income tax rate under the option " + quoted("uføretrygd") +" at $NAV_URL." }
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
                        bokmal { + "Skatt for deg som bor i utlandet" },
                        nynorsk { + "Skatt for deg som bur i utlandet" },
                        english { + "Tax for people who live abroad" }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på $SKATTEETATEN_URL. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor." },
                        nynorsk { + "Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på $SKATTEETATEN_URL. Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur." },
                        english { + "You can find more information about withholding tax to Norway at $SKATTEETATEN_URL. For information about taxation from your country of residence, you can contact the locale tax authorities." }
                    )
                }
            }
        }
    }

    object BeregningenDinKanBliEndret : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Beregningen din kan bli endret" },
                    nynorsk { +"Berekninga di kan bli endra" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Når vi mottar vedtak fra utenlandske trygdemyndigheter, fatter vi et nytt vedtak der vi gjør en endelig beregning av uføretrygden din. Hvis det viser seg at du har fått utbetalt mer enn du skulle, kan vi kreve at du betaler tilbake det du skylder. Hvis du får etterbetalt penger fra en utenlandsk trygdemyndighet, kan vi trekke det du skylder fra etterbetalingen." },
                    nynorsk { + "Når vi får vedtak frå utanlandske trygdeorgan, fattar vi eit nytt vedtak der vi reknar ut uføretrygda di endeleg. Viss det viser seg at du har fått betalt ut meir enn du skulle ha, kan vi krevje at du betaler tilbake det du skuldar. Viss du får etterbetalt pengar frå eit utanlandsk trygdeorgan, kan vi trekkje det du skuldar frå etterbetalinga." },
                )
            }
        }
    }

    object RettTilNyVurdering : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text (
                    bokmal { + "Du har rett til ny vurdering" },
                    nynorsk { + "Du har rett til ny vurdering" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Hvis du mener uføretrygden din har blitt negativt påvirket av at flere lands trygdemyndigheter er involvert, kan du be om en ny vurdering. Du kan lese mer om dette i «P1 - Samlet melding om pensjonsvedtak» og «Informasjon om skjemaet P1 og hvordan det brukes»." },
                    nynorsk { + "Hvis du mener uføretrygden din har blitt negativt påvirket av at flere lands trygdemyndigheter er involvert, kan du be om en ny vurdering. Du kan lese mer om dette i «P1 - Samlet melding om pensjonsvedtak» og «Informasjon om skjemaet P1 og hvordan det brukes»." },
                )
            }
        }
    }

    object KombinereUforetrygdAldersPensjon : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text (
                    bokmal { + "For deg som kombinerer uføretrygd og alderspensjon" },
                    nynorsk { + "For deg som kombinerer uføretrygd og alderspensjon" },
                )
                text (
                    bokmal { + "Du mottar alderspensjon fra folketrygden. Hvis du kombinerer uføretrygd og alderspensjon kan disse til sammen ikke utgjøre mer enn 100 prosent." },
                    nynorsk { + "Du mottar alderspensjon frå folketrygda. Viss du kombinerer uføretrygd og alderspensjon, kan den totale prosenten ikkje vere høgare enn 100 prosent." },
                )
            }
        }
    }

    data class AvslagBarnetillegg(val barnetilleggAvslatt: Expression<List<no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.BarnetilleggUTDto>>) :
        OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            forEach(barnetilleggAvslatt) { barnetillegg ->
                title1 {
                    text(
                        bokmal { +"Nav har avslått søknaden din om barnetillegg" },
                        nynorsk { +"Nav har avslått søknaden din om barnetillegg" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Søknaden er avslått for barn født " + barnetillegg.fodselsdato.format() + "." },
                        nynorsk { +"Søknaden er avslått for barn fødd " + barnetillegg.fodselsdato.format() + "." },
                    )
                }
                title2 {
                    text(
                        bokmal { +"Begrunnelse for vedtaket" },
                        nynorsk { +"Begrunnelse for vedtaket" },
                    )
                }
                paragraph {
                    showIf(barnetillegg.begrunnelse.equalTo(ANNEN_FORLD_RETT_BT)) {
                        text(
                            bokmal { +"Når barnet blir forsørget av begge foreldrene og begge mottar uføretrygd, skal barnetillegget gis til den som får det høyeste tillegget. Barnets andre forelder har rett til et høyere barnetillegg enn det du vil få." },
                            nynorsk { +"Når barnet vert forsørgd av begge foreldra og begge mottar uføretrygd, skal barnetillegget givast til den som får det høgaste tillegget. Den andre forelderen til barnet har rett til eit høgare barnetillegg enn det du vil få." },
                        )
                    }.orShowIf(barnetillegg.begrunnelse.equalTo(BT_GITT_TIL_ANNEN)) {
                        text(
                            bokmal { +"Når barnet blir forsørget av foreldre som ikke bor sammen, blir barnetillegget gitt til den som har samme folkeregistrerte adresse som barnet. Du bor ikke på samme folkeregistrerte adresse som barnet." },
                            nynorsk { +"Når barnet vert forsørgd av foreldre som ikkje bur saman, blir barnetillegget gitt til den som har same folkeregistrerte adresse som barnet. Du bur ikkje på same folkeregistrerte adresse som barnet." },
                        )
                    }.orShowIf(barnetillegg.begrunnelse.equalTo(MINDRE_ETT_AR_BT_FLT)) {
                        text(
                            bokmal { +"Barnetillegg for fellesbarn kan flyttes mellom foreldrene når det har gått ett år siden tidligere overføring. Det er mindre enn ett år siden barnetillegget ble overført til den andre forelderen." },
                            nynorsk { +"Barnetillegg for fellesbarn kan flyttast mellom foreldra når det har gått eitt år sidan tidlegare overføring. Det er mindre enn eitt år sidan barnetillegget blei overført til den andre forelderen." },
                        )
                    }.orShowIf(barnetillegg.begrunnelse.equalTo(BT_OVER_18)) {
                        text(
                            bokmal { +"Barnet har fylt 18 år, og du kan derfor ikke få barnetillegg til uføretrygden. Barnetillegg gis bare for barn under 18 år." },
                            nynorsk { +"Barnet har fylt 18 år, og du kan derfor ikkje få barnetillegg til uføretrygda. Barnetillegg vert berre gjeve for barn under 18 år." },
                        )
                    }.orShowIf(barnetillegg.begrunnelse.equalTo(BT_INNT_OVER_1G)) {
                        text(
                            bokmal { +"Barnets inntekt er høyere enn 1G. Etter regelverket som gjaldt før 1. juli 2024, faller retten til barnetillegg bort hvis barnet har inntekt over 1G." },
                            nynorsk { +"Barnets inntekt er høgare enn 1G. Etter regelverket som gjaldt før 1. juli 2024, fell retten til barnetillegg bort hvis barnet har inntekt over 1G." },
                        )
                    }.orShowIf(barnetillegg.begrunnelse.equalTo(BRK_FORSO_IKKE_BARN).or(barnetillegg.begrunnelse.equalTo(IKKE_MOTTATT_DOK))) {
                        text(
                            bokmal { +"For å ha rett til barnetillegg må du forsørge barnet. Vi har ikke fått dokumentasjon som viser at du forsørger barnet." },
                            nynorsk { +"For å ha rett til barnetillegg må du forsørge barnet. Vi har ikkje fått dokumentasjon som viser at du forsørger barnet." },
                        )
                    }.orShowIf(barnetillegg.begrunnelse.equalTo(BRUKER_FLYTTET_IKKE_AVT_LAND)) {
                        text(
                            bokmal { +"For å ha rett til barnetillegg må du være medlem i folketrygden. Du bor i et land som Norge ikke har trygdeavtale med, og er derfor ikke medlem i folketrygden." },
                            nynorsk { +"For å ha rett til barnetillegg må du være medlem i folketrygden. Du bur i eit land som Noreg ikkje har trygdeavtale med, og er derfor ikkje medlem i folketrygden." },
                        )
                    }.orShowIf(barnetillegg.begrunnelse.equalTo(BARN_FLYTTET_IKKE_AVT_LAND)) {
                        text(
                            bokmal { +"For å ha rett til barnetillegg må barnet være medlem i folketrygden. Barnet bor i et land som Norge ikke har trygdeavtale med, og er derfor ikke medlem i folketrygden." },
                            nynorsk { +"For å ha rett til barnetillegg må barnet være medlem i folketrygden. Barnet bur i eit land som Noreg ikkje har trygdeavtale med, og er derfor ikkje medlem i folketrygden." },
                        )
                    }.orShowIf(barnetillegg.begrunnelse.equalTo(BARN_OPPH_IKKE_AVT_LAND)) {
                        text(
                            bokmal { +"For å ha rett til barnetillegg må barnet være medlem i folketrygden. Fordi barnet har oppholdt seg i mer enn 90 dager i et land som Norge ikke har trygdeavtale med, regnes barnet ikke lenger som medlem i folketrygden." },
                            nynorsk { +"For å ha rett til barnetillegg må barnet være medlem i folketrygden. Fordi barnet har opphalde seg i meir enn 90 dagar i eit land som Noreg ikkje har trygdeavtale med, reknast barnet ikkje lenger som medlem i folketrygden." },
                        )
                    }.orShowIf(barnetillegg.begrunnelse.equalTo(ANNET)) {
                        text(
                            bokmal { +Fritekst("Avslagstekst for perioden") },
                            nynorsk { +Fritekst("Avslagstekst for perioden") },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Du oppfyller derfor ikke vilkåret, og vi avslår søknaden din om barnetillegg i uføretrygden." },
                        nynorsk { +"Du oppfyller derfor ikkje vilkåret, og vi avslår søknaden din om barnetillegg i uføretrygda." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven § 12-15." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova § 12-15." },
                    )
                }
            }
        }
    }
}



