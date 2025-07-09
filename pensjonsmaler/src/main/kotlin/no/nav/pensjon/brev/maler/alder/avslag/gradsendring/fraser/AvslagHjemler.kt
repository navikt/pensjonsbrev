package no.nav.pensjon.brev.maler.alder.avslag.gradsendring.fraser

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class AvslagHjemler(
    val regelverkType: Expression<AlderspensjonRegelverkType>,
    val harEOSLand: Expression<Boolean>,
    val prorataBruktIBeregningen: Expression<Boolean>,
    val avtaleland: Expression<String?>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2025)) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-15 og 22-13.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-15 og 22-13.",
                    English to "This decision was made pursuant to the provisions of §§ 20-15 and 22-13 of the National Insurance Act."
                )
            }
        }.orShowIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2016)) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-11, 19-15, 20-15, 20-19 og 22-13.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-11, 19-15, 20-15, 20-19 og 22-13.",
                    English to "This decision was made pursuant to the provisions of §§ 19-11, 19-15, 20-15, 20-19 and 22-13 of the National Insurance Act."
                )
            }
        }

        showIf(harEOSLand and prorataBruktIBeregningen) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004, artikkel 6.",
                    Nynorsk to "Vedtaket er også gjort etter reglane i EØS-avtalen i forordning 883/2004, artikkel 6.",
                    English to "This decision was also made pursuant to the provisions of Regulation (EC) 883/2004, article 6.",
                )
            }
        }

        showIf(harEOSLand.not() and prorataBruktIBeregningen) {
            ifNotNull(avtaleland)  { avtaleland ->
                paragraph {
                    textExpr(
                        Bokmal to "Vedtaket er også gjort etter reglene i trygdeavtalen med ".expr() + avtaleland + ".",
                        Nynorsk to "Vedtaket er også gjort etter reglane i trygdeavtalen med ".expr() + avtaleland + ".",
                        English to "This decision was also made pursuant to the provisions of the social security agreement with ".expr() + avtaleland + ".",
                    )
                }
            }
        }
    }
}
