package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.maler.alder.AvslagUttakFoerNormertPensjonsalder.fritekst
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

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
    val avtalelandNavn: Expression<String>,
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
                    ) + " og reglene i trygdeavtalen med ".expr() + avtalelandNavn + ".",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §".expr() + ifElse(
                        regelverkType.isOneOf(AP2016),
                        ifTrue = "§ 19-2, 20-5, 20-8, 20-10",
                        ifFalse = " 19-2"
                    ) + " og reglane i trygdeavtalen med ".expr() + avtalelandNavn + ".",
                    English to "This decision was made pursuant to the provisions of §".expr() + ifElse(
                        regelverkType.isOneOf(
                            AP2016
                        ), ifTrue = "§ 19-2, 20-5, 20-8, 20-10", ifFalse = " 19-2"
                    ) + " of the National Insurance Act and to the rules of the Article of the Social Security Agreement with ".expr()
                            + avtalelandNavn + ".",
                )
            }
        }
    }
}


data class AvslagUnder3aarHjemmel(
    val avtalelandNavn: Expression<String>,
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