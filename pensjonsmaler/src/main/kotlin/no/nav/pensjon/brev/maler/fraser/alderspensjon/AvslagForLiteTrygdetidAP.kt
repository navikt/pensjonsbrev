package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse.UNDER_3_AR_TT
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse.UNDER_5_AR_TT
import no.nav.pensjon.brev.api.model.vedlegg.Trygdetid
import no.nav.pensjon.brev.maler.alder.AvslagUttakFoerNormertPensjonsalder.fritekst
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SUPPLERENDE_STOENAD
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningenTrygdetidTabeller
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

object AvslagForLiteTrygdetidAP {
    // avslagAP2011Under1aar, avslagAP2016Under1aar, avslagUnder3aarTT, avslagUnder5aarTT
    object AvslagUnder1aar3aar5aarTT : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Våre opplysninger viser at du har bodd eller arbeidet i Norge i X antall dager/måneder. /Våre opplysninger viser at du ikke har bodd eller arbeidet i Norge.",
                    Nynorsk to "Våre opplysningar viser at du har budd eller arbeidd i Noreg i X antall dager/måneder. /Våre opplysningar viser at du ikkje har budd eller arbeidd i Noreg.",
                    English to "We have registered that you have been living or working in Norway X days/months. /We have no record of you living or working in Norway.",
                )
            }

        }
    }

    // avslagAP2011Under1aarHjemmelEOS, avslagAP2016Under1aarHjemmelEOS, avslagAP2011Under1aarHjemmelAvtale, avslagAP2016Under1aarHjemmelAvtale
    data class AvslagUnder1aarHjemmel(
        val avtaleland: Expression<String>,
        val erEOSland: Expression<Boolean>,
        val regelverkType: Expression<AlderspensjonRegelverkType>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(erEOSland) {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-2",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-2",
                        English to "This decision was made pursuant to the provisions of § 19-2",
                    )
                    showIf(regelverkType.equalTo(AP2016)) {
                        text(
                            Bokmal to ", 20-5, 20-8, 20-10",
                            Nynorsk to ", 20-5, 20-8, 20-10",
                            English to ", 20-5, 20-8, 20-10",
                        )
                    }
                    showIf(erEOSland) {
                        text(
                            Bokmal to " og EØS-avtalens forordning 883/2004 artikkel 57.",
                            Nynorsk to " og EØS-avtalens forordning 883/2004 artikkel 57.",
                            English to " of the National Insurance Act and Article 57 of Regulation (EC) 883/2004.",
                        )
                    }.orShow {
                        textExpr(
                            Bokmal to " og reglene i trygdeavtalen med ".expr() + avtaleland + ".",
                            Nynorsk to " og reglane i trygdeavtalen med ".expr() + avtaleland + ".",
                            English to " of the National Insurance Act and to the rules of the Article of the Social Security Agreement with ".expr()
                                    + avtaleland + ".",
                        )
                    }

                }
            }
        }
    }

    data class OpptjeningstidEOSAvtaleland(
        val erAvtaleland: Expression<Boolean>,
        val erEOSland: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "Vi har fått opplyst at du har ".expr() + fritekst("Angi antall") + " måneder opptjeningstid i annet ",
                    Nynorsk to "Vi har fått opplyst at du har ".expr() + fritekst("Angi antall") + " månader oppteningstid i anna ",
                    English to "We have been informed that you have ".expr() + fritekst("Angi antall") + " months of national insurance coverage in "
                )
                showIf(erEOSland and not(erAvtaleland)) {
                    text(Bokmal to "EØS-land. ", Nynorsk to "EØS-land. ", English to "an other EEA country. ")
                }
                showIf(erAvtaleland and not(erEOSland)) {
                    text(Bokmal to "avtaleland. ", Nynorsk to "avtaleland. ", English to "an other signatory country. ")
                }
                showIf(erEOSland and erAvtaleland) {
                    text(
                        Bokmal to "EØS- og avtaleland. ",
                        Nynorsk to "EØS- og avtaleland. ",
                        English to "other EEA and signatory countries. "
                    )
                }
                text(
                    Bokmal to "Den samlede trygdetiden din i Norge og ",
                    Nynorsk to "Den samla trygdetida din i Noreg og ",
                    English to "Your total national insurance coverage in Norway and "
                )
                showIf(erEOSland and not(erAvtaleland)) {
                    text(Bokmal to "EØS-land", Nynorsk to "EØS-land", English to "an other EEA country")
                }
                showIf(erAvtaleland and not(erEOSland)) {
                    text(Bokmal to "avtaleland", Nynorsk to "avtaleland", English to "an other signatory country")
                }
                showIf(erEOSland and erAvtaleland) {
                    text(
                        Bokmal to "EØS- og avtaleland",
                        Nynorsk to "EØS- og avtaleland",
                        English to "other EEA and signatory countries"
                    )
                }
                textExpr(
                    Bokmal to " er ".expr() + fritekst("Angi samlet trygdetid") + ".",
                    Nynorsk to " er ".expr() + fritekst("Angi samlet trygdetid") + ".",
                    English to " is ".expr() + fritekst("Angi samlet trygdetid") + " years/months of national insurance coverage."
                )
            }
        }
    }

    // avslagAP2011Under3aar, avslagAP2011Under5aar, avslagAP2016Under3aar, avslagAP2016Under5aar
    data class RettTilAPFolketrygdsak(
        val avslagsbegrunnelse: Expression<VedtaksBegrunnelse>,
        val regelverkType: Expression<AlderspensjonRegelverkType>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(avslagsbegrunnelse.isOneOf(UNDER_3_AR_TT, UNDER_5_AR_TT)) {
                paragraph {
                    text(
                        Bokmal to "For å ha rett til alderspensjon må du ha minst ",
                        Nynorsk to "For å ha rett til alderspensjon må du ha minst ",
                        English to "To be eligible for retirement pension, you must have at least ",
                    )

                    showIf(avslagsbegrunnelse.equalTo(UNDER_3_AR_TT)) {
                        text(Bokmal to "tre", Nynorsk to "tre", English to "three")
                    }.orShow {
                        text(Bokmal to "fem", Nynorsk to "fem", English to "five")
                    }

                    text(
                        Bokmal to " års trygdetid",
                        Nynorsk to " års trygdetid",
                        English to " years of national insurance coverage",
                    )
                    showIf(regelverkType.equalTo(AP2016)) {
                        text(
                            Bokmal to ", eller ha tjent opp inntektspensjon",
                            Nynorsk to ", eller ha tent opp inntektspensjon",
                            English to ", or have had a pensionable income",
                        )
                    }
                    text(
                        Bokmal to ". Det har du ikke, og derfor har vi avslått søknaden din.",
                        Nynorsk to ". Det har du ikkje, og derfor har vi avslått søknaden din.",
                        English to ". You do not meet this requirement, therefore we have declined your application.",
                    )
                }
            }
        }
    }

    // avslagAP2011Under3aarEOS, avslagAP2011Under3aarAvtale, avslagAP2016Under3aarEOS, avslagAP2016Under3aarAvtale,
// avslagAP2011Under5aarEOS, avslagAP2011Under5aarAvtale, avslagAP2016Under5aarEOS, avslagAP2016Under5aarAvtale
    data class RettTilAPMedEOSAvtalelandOg3aar5aarTT(
        val avslagsbegrunnelse: Expression<VedtaksBegrunnelse>,
        val erAvtaleland: Expression<Boolean>,
        val erEOSland: Expression<Boolean>,
        val regelverkType: Expression<AlderspensjonRegelverkType>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(avslagsbegrunnelse.isOneOf(UNDER_3_AR_TT, UNDER_5_AR_TT)) {

                paragraph {
                    text(
                        Bokmal to "For å ha rett til alderspensjon må du til sammen ha minst ",
                        Nynorsk to "For å ha rett til alderspensjon må du til saman ha minst ",
                        English to "To be eligible for retirement pension, you must have in total at least ",
                    )
                    showIf(avslagsbegrunnelse.equalTo(UNDER_3_AR_TT)) {
                        text(Bokmal to "tre", Nynorsk to "tre", English to "three")
                    }.orShowIf(avslagsbegrunnelse.equalTo(UNDER_5_AR_TT)){
                        text(Bokmal to "fem", Nynorsk to "fem", English to "five")
                    }
                    text(
                        Bokmal to " års trygdetid i Norge og annet ",
                        Nynorsk to " års trygdetid i Noreg og anna ",
                        English to " years of national insurance coverage in Norway and ",
                    )
                    showIf(erEOSland and not(erAvtaleland)) {
                        text(
                            Bokmal to "EØS-land",
                            Nynorsk to "EØS-land",
                            English to "an other EEA country",
                        )
                    }.orShowIf(erAvtaleland and not(erEOSland)) {
                        text(
                            Bokmal to "avtaleland",
                            Nynorsk to "avtaleland",
                            English to "an other signatory country",
                        )
                    }.orShowIf(erEOSland and erAvtaleland) {
                        text(
                            Bokmal to "EØS- og avtaleland",
                            Nynorsk to "EØS- og avtaleland",
                            English to "other EEA and signatory countries",
                        )
                    }
                    showIf(regelverkType.isOneOf(AP2016)) {
                        text(
                            Bokmal to ", eller ha tjent opp inntektspensjon",
                            Nynorsk to ", eller ha tent opp inntektspensjon",
                            English to ", or have had a pensionable income",
                        )
                    }

                    text(
                        Bokmal to ". Det har du ikke, og derfor har vi avslått søknaden din.",
                        Nynorsk to ". Det har du ikkje, og derfor har vi avslått søknaden din.",
                        English to ". You do not meet this requirement, therefore we have declined your application.",
                    )
                }
            }
        }
    }

    // avslagAP2011Under3aarHjemmel,
    object AvslagAP2011FolketrygdsakHjemmel : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-2.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-2.",
                    English to "This decision was made pursuant to the provisions of § 19-2 of the National Insurance Act."
                )
            }
        }
    }

    object AvslagAP2011Under3aar5aarHjemmel : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-2 og EØS-avtalens forordning 883/2004 artikkel 6.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-2 og EØS-avtalens forordning 883/2004 artikkel 6.",
                    English to "This decision was made pursuant to the provisions of § 19-2 of the National Insurance Act and Article 6 of Regulation (EC) 883/2004.",
                )
            }
        }
    }

// Trygdeperiodertabeller

    data class TrygdeperioderNorgeTabell(
        val trygdeperioderNorge: Expression<List<Trygdetid>>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            showIf(trygdeperioderNorge.isNotEmpty()) {
                //trygdetidOverskrift
                title1 {
                    text(
                        Bokmal to "Trygdetid",
                        Nynorsk to "Trygdetid",
                        English to "Period of national insurance coverage"
                    )
                }
                //norskTTInfoGenerell_001
                paragraph {
                    text(
                        Bokmal to "Trygdetid er perioder du har vært medlem i folketrygden. Som hovedregel er dette perioder du har bodd eller arbeidet i Norge. Trygdetid har betydning for beregning av pensjonen din. Full trygdetid er 40 år.",
                        Nynorsk to "Trygdetid er periodar du har vore medlem i folketrygda. Som hovudregel er dette periodar du har budd eller arbeidd i Noreg. Trygdetid har betydning for berekninga av pensjonen din. Full trygdetid er 40 år.",
                        English to "The period of national insurance coverage is the periods in which you have been a member of the Norwegian National Insurance Scheme. As a general rule, these are periods when you have been registered as living or working in Norway. The period of national insurance coverage affects the calculation of your pension. The full insurance period is 40 years.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Tabellen nedenfor viser perioder vi har brukt for å fastsette din norske trygdetid.",
                        Nynorsk to "Tabellen nedanfor viser periodar vi har brukt for å fastsetje den norske trygdetida di.",
                        English to "The table below shows the time periods used to establish your Norwegian national insurance coverage.",
                    )
                }

                includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetid(trygdeperioderNorge))
            }
        }
    }

    data class TrygdeperioderEOSlandTabell(
        val trygdetid: Expression<List<Trygdetid>>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            showIf(trygdetid.isNotEmpty()) {
                paragraph {
                    text(
                        Bokmal to "Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i EØS-land. Disse periodene er brukt i vurderingen av retten til alderspensjon.",
                        Nynorsk to "Tabellen nedanfor viser periodar du har budd og/eller arbeidd i EØS-land. Desse periodane er brukt i vurderinga av retten til alderspensjon.",
                        English to "The table below shows your national insurance coverage in EEA countries. These periods have been used to assess whether you are eligible for retirement pension."
                    )
                }

                includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.UtenlandskTrygdetid(trygdetid))

            }
        }
    }

    data class TrygdeperioderAvtalelandTabell(
        val trygdeperioderAvtaleland: Expression<List<Trygdetid>>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            showIf(trygdeperioderAvtaleland.isNotEmpty()) {
                paragraph {
                    text(
                        Bokmal to "Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i land som Norge har trygdeavtale med. Disse periodene er brukt i vurderingen av retten til alderspensjon.",
                        Nynorsk to "Tabellen nedanfor viser periodar du har budd og/eller arbeidd i land som Noreg har trygdeavtale med. Desse periodane er brukt i vurderinga av retten til alderspensjon.",
                        English to "The table below shows your national insurance coverage in countries Norway has a social security agreement. These periods have been used to assess whether you are eligible for retirement pension."
                    )
                }

                includePhrase(
                    OpplysningerBruktIBeregningenTrygdetidTabeller.UtenlandskTrygdetid(
                        trygdeperioderAvtaleland
                    )
                )
            }
        }
    }

    object AvslagAPUttakAlderU62Begrunn : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Du har søkt om å ta ut alderspensjon før du fyller 62 år. For å ha rett til alderspensjon må du være 62 år. Derfor har vi avslått søknaden din.",
                    Nynorsk to "For å ha rett til alderspensjon må du vere 62 år. Du har søkt om å ta ut alderspensjon før du fyller 62 år. Derfor har vi avslått søknaden din.",
                    English to "In order to be eligible for retirement pension you have to be 62 years. You have applied for retirement pension from a date prior to having turned 62. Therefore, we have declined your application.",
                )
            }
        }
    }

    object AvslagAPUttakAlderU62Info : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Du kan selv sjekke når du kan få alderspensjon",
                    Nynorsk to "Du kan sjølv sjekke når du kan få alderspensjon",
                    English to "You can find out when you are eligible for retirement pension",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan ta ut alderspensjon før du fyller 67 år hvis du har hatt høy nok pensjonsopptjening. I Din pensjon på $DIN_PENSJON_URL kan du se hva pensjonen din blir, avhengig av når og hvor mye du tar ut. Slik kan du sjekke når du tidligst kan ta ut alderspensjon.",
                    Nynorsk to "Du kan ta ut alderspensjon før du fyller 67 år dersom du har hatt høg nok pensjonsopptening. I Din pensjon på $DIN_PENSJON_URL kan du sjå kva pensjonen din blir, avhengig av når og kor mykje du tek ut. Slik kan du sjekke når du tidlegast kan ta ut alderspensjon.",
                    English to "You may be eligible for retirement pension before the age of 67, provided your accumulated pension capital is sufficiently high. Log on to Din pensjon at $DIN_PENSJON_URL to find out more about your pension payments. You can also see how your payments change depending on when you start drawing a retirement pension and what percentage of retirement pension you choose."
                )
            }
        }
    }

    object NysoknadAPInfo : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Du må sende oss en ny søknad når du ønsker å ta ut alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden.2",
                    Nynorsk to "Du må sende oss ein ny søknad når du ønskjer å ta ut alderspensjonen. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden.",
                    English to "You have to submit an application when you want to start drawing your retirement pension. Any change will be implemented at the earliest the month after we have received the application."
                )
            }
        }
    }

    object SupplerendeStoenad : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Supplerende stønad",
                    Nynorsk to "Supplerande stønad",
                    English to "Supplementary benefit"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du har kort botid i Norge når du fyller 67 år, kan du søke om supplerende stønad. Du kan lese mer om supplerende stønad på vår nettside $SUPPLERENDE_STOENAD.",
                    Nynorsk to "Dersom du har kort butid i Noreg når du fyller 67 år, kan du søke om supplerande stønad. Du kan lese meir om supplerande stønad på vår nettside $SUPPLERENDE_STOENAD.",
                    English to "If you have only lived a short period in Norway before reaching 67 years of age, you can apply for supplementary benefit. You can read more about supplementary benefit at our website $SUPPLERENDE_STOENAD.",
                )

            }
        }
    }

    data class AvslagUnder3aar5aarHjemmelAvtaleAuto(
        val avtaleland: Expression<String>,
        val erAvtaleland: Expression<Boolean>,
        val erEOSland: Expression<Boolean>,
        val regelverkType: Expression<AlderspensjonRegelverkType>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven ".expr()
                            + ifElse(
                        regelverkType.isOneOf(AP2016),
                        ifTrue = "§§ 19-2, 20-5 til 20-8 og 20-10,",
                        ifFalse = "§ 19-2"
                    ) + " og reglene i trygdeavtalen med ".expr()
                            + avtaleland,
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova ".expr()
                            + ifElse(
                        regelverkType.isOneOf(AP2016),
                        ifTrue = "§§ 19-2, 20-5 til 20-8 og 20-10,",
                        ifFalse = "§ 19-2"
                    ) + " og reglane i trygdeavtalen med ".expr()
                            + avtaleland,
                    English to "This decision was made pursuant to the provision of ".expr()
                            + ifElse(
                        regelverkType.isOneOf(AP2016),
                        ifTrue = "§§ 19-2, 20-5 til 20-8 og 20-10,",
                        ifFalse = "§ 19-2"
                    ) + " of the National Insurance Act and to the provisions of the social security agreement with ".expr()
                            + avtaleland
                )
                showIf(erAvtaleland and not(erEOSland)) {
                    text(Bokmal to ".", Nynorsk to ".", English to ".")
                }.orShowIf(erAvtaleland and erEOSland) {
                    text(
                        Bokmal to ", og EØS-avtalens forordning 883/2004 artikkel 6.",
                        Nynorsk to ", og EØS-avtalens forordning 883/2004 artikkel 6.",
                        English to ", and Article 6 of regulation (EC) 883/2004."
                    )
                }
            }
        }
    }

}
