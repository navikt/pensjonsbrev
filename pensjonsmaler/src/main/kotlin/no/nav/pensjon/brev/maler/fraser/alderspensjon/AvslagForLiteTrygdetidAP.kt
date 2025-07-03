package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse.*
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.normertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.trygdeperioderNorge
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.trygdeperioderUtland
import no.nav.pensjon.brev.api.model.maler.alderApi.TrygdeperiodeNorgeSelectors.fom
import no.nav.pensjon.brev.api.model.maler.alderApi.TrygdeperiodeNorgeSelectors.tom
import no.nav.pensjon.brev.api.model.maler.alderApi.TrygdeperiodeUtlandSelectors.fom
import no.nav.pensjon.brev.api.model.maler.alderApi.TrygdeperiodeUtlandSelectors.land
import no.nav.pensjon.brev.api.model.maler.alderApi.TrygdeperiodeUtlandSelectors.tom
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.TrygdetidSelectors.fom
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.TrygdetidSelectors.fom_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.TrygdetidSelectors.land
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.TrygdetidSelectors.tom
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.TrygdetidSelectors.tom_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.maler.alder.AvslagUttakFoerNormertPensjonsalder.fritekst
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.SUPPLERENDE_STOENAD
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.isNotEmpty
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object AvslagUnder1aarTT : OutlinePhrase<LangBokmalNynorskEnglish>() {
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


data class AvslagUnder1aarHjemmel(
    val avtaleland: Expression<String>,
    val erEOSland: Expression<Boolean>,
    val regelverkType: Expression<AlderspensjonRegelverkType>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(erEOSland) {
            paragraph {
                textExpr(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §".expr() + ifElse(
                        regelverkType.isOneOf(
                            AP2016
                        ), ifTrue = "§ 19-2, 20-5, 20-8, 20-10", ifFalse = " 19-2"
                    ) + " og EØS-avtalens forordning 883/2004 artikkel 57.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §".expr() + ifElse(
                        regelverkType.isOneOf(AP2016),
                        ifTrue = "§ 19-2, 20-5, 20-8, 20-10", ifFalse = " 19-2"
                    ) + " og EØS-avtalens forordning 883/2004 artikkel 57.",
                    English to "This decision was made pursuant to the provisions of §".expr() + ifElse(
                        regelverkType.isOneOf(
                            AP2016
                        ), ifTrue = "§ 19-2, 20-5, 20-8, 20-10", ifFalse = " 19-2"
                    ) + " of the National Insurance Act and Article 57 of Regulation (EC) 883/2004."
                )
            }
        } orShow {
            paragraph {
                textExpr(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §".expr() + ifElse(
                        regelverkType.isOneOf(AP2016),
                        ifTrue = "§ 19-2, 20-5, 20-8, 20-10",
                        ifFalse = " 19-2"
                    ) + " og reglene i trygdeavtalen med ".expr() + avtaleland + ".",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §".expr() + ifElse(
                        regelverkType.isOneOf(AP2016),
                        ifTrue = "§ 19-2, 20-5, 20-8, 20-10",
                        ifFalse = " 19-2"
                    ) + " og reglane i trygdeavtalen med ".expr() + avtaleland + ".",
                    English to "This decision was made pursuant to the provisions of §".expr() + ifElse(
                        regelverkType.isOneOf(
                            AP2016
                        ), ifTrue = "§ 19-2, 20-5, 20-8, 20-10", ifFalse = " 19-2"
                    ) + " of the National Insurance Act and to the rules of the Article of the Social Security Agreement with ".expr()
                            + avtaleland + ".",
                )
            }
        }
    }
}

object AvslagUnder3aarTT : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Våre opplysninger viser at du har bodd eller arbeidet i Norge i ".expr() + fritekst(
                    "angi trygdetid"
                ) + ". Våre opplysninger viser at du ikke har bodd eller arbeidet i Norge.",
                Nynorsk to "Våre opplysningar viser at du har budd eller arbeidd i Noreg i ".expr() + fritekst(
                    "angi trygdetid"
                ) + ". I følgje våre opplysningar har du ikkje budd eller arbeidd i Noreg.",
                English to "We have registered that you have been living or working in Norway for ".expr() + fritekst(
                    "angi trygdetid"
                ) + ". We have no record of you living or working in Norway.",
            )
        }
    }
}

data class OpptjeningstidEOSAvtaleland(
    val erEOSland: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " måneder opptjeningstid i ".expr()
                        + ifElse(
                    erEOSland,
                    ifTrue = "annet EØS-land",
                    ifFalse = "annet avtaleland"
                ) + ". Den samlede trygdetiden din i Norge og ".expr()
                        + ifElse(erEOSland, ifTrue = "annet EØS-land", ifFalse = "annet avtaleland") + " er ".expr()
                        + fritekst("angi samlet trygdetid i Norge og EØS/avtale-land") + " år.",
                Nynorsk to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " månader oppteningstid i ".expr()
                        + ifElse(
                    erEOSland,
                    ifTrue = "anna EØS-land",
                    ifFalse = "anna avtaleland"
                ) + ". Den samla trygdetida din i Noreg og ".expr()
                        + ifElse(erEOSland, ifTrue = "anna EØS-land", ifFalse = "anna avtaleland") + ". er ".expr()
                        + fritekst("angi samlet trygdetid i Norge og EØS/Avtale-land") + " år.",
                English to "We have been informed that you have ".expr() + fritekst("angi antall") + " months of national insurance coverage in ".expr()
                        + ifElse(
                    erEOSland,
                    ifTrue = "an other EEA",
                    ifFalse = "an other signatory"
                ) + " country. Your total national insurance coverage in Norway and ".expr()
                        + ifElse(
                    erEOSland,
                    ifTrue = "an other EEA",
                    ifFalse = "an other signatory"
                ) + " country is ".expr()
                        + fritekst("angi samlet trygdetid i Norge og EEA/signatory country") + " years."
            )
        }
    }
}

// avslagAP2011Under3aar, avslagAP2011Under5aar
data class RettTilAPFolketrygdsak(
    val avslagsbegrunnelse: Expression<VedtaksBegrunnelse>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "For å ha rett til alderspensjon må du ha minst ".expr()
                        + ifElse(avslagsbegrunnelse.isOneOf(UNDER_3_AR_TT,), ifTrue = "tre", ifFalse = "fem")
                        + " års trygdetid. Det har du ikke, og derfor har vi avslått søknaden din.",
                Nynorsk to "For å ha rett til alderspensjon må du ha minst ".expr()
                        + ifElse(avslagsbegrunnelse.isOneOf(UNDER_3_AR_TT,), ifTrue = "tre", ifFalse = "fem")
                        + " års trygdetid. Det har du ikkje, og derfor har vi avslått søknaden din.",
                English to "To be eligible for retirement pension, you must have at least ".expr()
                        + ifElse(avslagsbegrunnelse.isOneOf(UNDER_3_AR_TT,), ifTrue = "three", ifFalse = "five")
                        + " years of national insurance coverage. You do not meet this requirement, therefore we have declined your application.",
            )
        }
    }
}

data class RettTilAPMedEOSAvtalelandOg3aar5aarTT(
    val avslagsbegrunnelse: Expression<VedtaksBegrunnelse>,
    val erEOSland: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "For å ha rett til alderspensjon må du til sammen ha minst ".expr()
                        + ifElse(
                    avslagsbegrunnelse.isOneOf(UNDER_3_AR_TT),
                    ifTrue = "tre",
                    ifFalse = "fem"
                ) + " års trygdetid i Norge og annet ".expr()
                        + ifElse(
                    erEOSland,
                    ifTrue = "EØS-land",
                    ifFalse = "avtaleland"
                ) + ". Det har du ikke, og derfor har vi avslått søknaden din.",
                Nynorsk to "For å ha rett til alderspensjon må du til saman ha minst ".expr()
                        + ifElse(
                    avslagsbegrunnelse.isOneOf(UNDER_3_AR_TT),
                    ifTrue = "tre",
                    ifFalse = "fem"
                ) + " års trygdetid i Noreg og anna ".expr()
                        + ifElse(
                    erEOSland,
                    ifTrue = "EØS-land",
                    ifFalse = "avtaleland"
                ) + ". Det har du ikkje, og derfor har vi avslått søknaden din.",
                English to "To be eligible for retirement pension, you must have in total at least ".expr()
                        + ifElse(
                    avslagsbegrunnelse.isOneOf(UNDER_3_AR_TT),
                    ifTrue = "three",
                    ifFalse = "five"
                ) + " years of national insurance coverage in Norway and an other ".expr()
                        + ifElse(
                    erEOSland,
                    ifTrue = "EEA",
                    ifFalse = "signatory"
                ) + " country. You do not meet this requirement, therefore we have declined your application.",
            )
        }
    }
}

object AvslagUnder3aar5aarTTEOS : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " måneder opptjeningstid i annet EØS-land. Den samlede trygdetiden din i Norge og annet EØS-land er ".expr()
                        + fritekst("angi samlet trygdetid i Norge og EØS-land") + ".",
                Nynorsk to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " månader oppteningstid i anna avtaleland. Den samla trygdetida din i Noreg og anna avtaleland er ".expr()
                        + fritekst("angi samlet trygdetid i Norge og EØS-land") + ".",
                English to "We have been informed that you have <FRITEKST: angi antall> months of national insurance coverage in an other EEA country. Your total national insurance coverage in Norway and an other EEA country is ".expr()
                        + fritekst("angi samlet trygdetid i Norge og EEA-country") + "."
            )
        }
    }
}

object AvslagUnder3aar5aarTTAvtale : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " måneder opptjeningstid i annet avtaleland. Den samlede trygdetiden din i Norge og annet avtaleland er ".expr() + fritekst(
                    "angi samlet trygdetid i Norge og avtaleland"
                ) + ".",
                Nynorsk to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " månader oppteningstid i anna avtaleland. Den samla trygdetida din i Noreg og anna avtaleland er ".expr() + fritekst(
                    "angi samlet trygdetid i Norge og avtaleland"
                ) + ".",
                English to "We have been informed that you have ".expr() + fritekst("angi antall") + " months of national insurance coverage in an other signatory country. Your total national insurance coverage in Norway and an other signatory country is".expr() + fritekst(
                    "angi samlet trygdetid i Norge og avtaleland"
                ) + "."
            )
        }
    }
}

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

data class AvslagUnder3aarHjemmel(
    val avtaleland: Expression<String>,
    val erEOSland: Expression<Boolean>,
    val regelverkType: Expression<AlderspensjonRegelverkType>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(erEOSland) {
            paragraph {
                textExpr(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §".expr() + ifElse(
                        regelverkType.isOneOf(AP2016),
                        ifTrue = "§ 19-2, 20-5 til 20-8 og 20-10",
                        ifFalse = " 19-2"
                    ) + " og EØS-avtalens forordning 883/2004 artikkel 6.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §".expr() + ifElse(
                        regelverkType.isOneOf(AP2016),
                        ifTrue = "§ 19-2, 20-5 til 20-8 og 20-10",
                        ifFalse = " 19-2"
                    ) + " og EØS-avtalens forordning 883/2004 artikkel 6.",
                    English to "This decision was made pursuant to the provisions of §".expr() + ifElse(
                        regelverkType.isOneOf(
                            AP2016
                        ), ifTrue = "§ 19-2, 20-5 to 20-8 and 20-10", ifFalse = " 19-2"
                    ) + " of the National Insurance Act and Article 6 of Regulation (EC) 883/2004",
                )
            }
        }.orShow {
            paragraph {
                textExpr(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §".expr() + ifElse(
                        regelverkType.isOneOf(AP2016),
                        ifTrue = "§ 19-2, 20-5 til 20-8, og 20-10.",
                        ifFalse = " 19-2."
                    ),
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §".expr() + ifElse(
                        regelverkType.isOneOf(AP2016),
                        ifTrue = "§ 19-2, 20-5 til 20-8 og 20-10.",
                        ifFalse = " 19-2."
                    ),
                    English to "This decision was made pursuant to the provisions of §".expr() + ifElse(
                        regelverkType.isOneOf(
                            AP2016
                        ), ifTrue = "§ 19-2, 20-5 to 20-8 and 20-10", ifFalse = " 19-2"
                    ) + " of the National Insurance Act.",
                )
            }
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
    val trygdeperioderNorge: Expression<List<AvslagForLiteTrygdetidAPDto.Trygdetid>>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        showIf(trygdeperioderNorge.isNotEmpty()) {
            paragraph {
                table(header = {
                    column {
                        text(
                            Bokmal to "Fra og med",
                            Nynorsk to "Frå og med",
                            English to "Start date",
                        )
                    }
                    column(alignment = RIGHT) {
                        text(
                            Bokmal to "Til og med",
                            Nynorsk to "Til og med",
                            English to "End date"
                        )
                    }
                }) {
                    forEach(trygdeperioderNorge) { periode ->
                        row {
                            cell {
                                textExpr(
                                    Bokmal to periode.fom.format(),
                                    Nynorsk to periode.fom.format(),
                                    English to periode.fom.format()
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to periode.tom.format(),
                                    Nynorsk to periode.tom.format(),
                                    English to periode.tom.format()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class TrygdeperioderEOSland(
    val trygdeperioderEOSland: Expression<List<AvslagForLiteTrygdetidAPDto.Trygdetid>>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        showIf(trygdeperioderEOSland.isNotEmpty()) {
            paragraph {
                text(
                    Bokmal to "Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i EØS-land. Disse periodene er brukt i vurderingen av retten til alderspensjon.",
                    Nynorsk to "Tabellen nedanfor viser periodar du har budd og/eller arbeidd i EØS-land. Desse periodane er brukt i vurderinga av retten til alderspensjon.",
                    English to "The table below shows your national insurance coverage in EEA countries. These periods have been used to assess whether you are eligible for retirement pension."
                )
            }

            paragraph {
                table(header = {
                    column {
                        text(
                            Bokmal to "Land",
                            Nynorsk to "Land",
                            English to "Country"
                        )
                    }
                    column(alignment = RIGHT) {
                        text(
                            Bokmal to "Fra og med",
                            Nynorsk to "Frå og med",
                            English to "Start date",
                        )
                    }
                    column(alignment = RIGHT) {
                        text(
                            Bokmal to "Til og med",
                            Nynorsk to "Til og med",
                            English to "End date"
                        )
                    }
                }) {
                    forEach(trygdeperioderEOSland) { utlandPeriode ->
                        row {
                            cell {
                                textExpr(
                                    Bokmal to utlandPeriode.land,
                                    Nynorsk to utlandPeriode.land,
                                    English to utlandPeriode.land
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to utlandPeriode.fom.format(),
                                    Nynorsk to utlandPeriode.fom.format(),
                                    English to utlandPeriode.fom.format()
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to utlandPeriode.tom.format(),
                                    Nynorsk to utlandPeriode.tom.format(),
                                    English to utlandPeriode.tom.format()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class TrygdeperioderAvtaleland(
    val trygdeperioderAvtaleland: Expression<List<AvslagForLiteTrygdetidAPDto.Trygdetid>>,
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

            paragraph {
                table(header = {
                    column {
                        text(
                            Bokmal to "Land",
                            Nynorsk to "Land",
                            English to "Country"
                        )
                    }
                    column(alignment = RIGHT) {
                        text(
                            Bokmal to "Fra og med",
                            Nynorsk to "Frå og med",
                            English to "Start date",
                        )
                    }
                    column(alignment = RIGHT) {
                        text(
                            Bokmal to "Til og med",
                            Nynorsk to "Til og med",
                            English to "End date"
                        )
                    }
                }) {
                    forEach(trygdeperioderAvtaleland) { utlandPeriode ->
                        row {
                            cell {
                                textExpr(
                                    Bokmal to utlandPeriode.land,
                                    Nynorsk to utlandPeriode.land,
                                    English to utlandPeriode.land
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to utlandPeriode.fom.format(),
                                    Nynorsk to utlandPeriode.fom.format(),
                                    English to utlandPeriode.fom.format()
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to utlandPeriode.tom.format(),
                                    Nynorsk to utlandPeriode.tom.format(),
                                    English to utlandPeriode.tom.format()
                                )
                            }
                        }
                    }
                }
            }
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
