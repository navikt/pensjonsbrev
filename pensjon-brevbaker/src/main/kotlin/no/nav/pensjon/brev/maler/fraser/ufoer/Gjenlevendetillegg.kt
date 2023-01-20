package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.textExpr

object Gjenlevendetillegg {

    // TBU1214
    data class GjenlevendeOverskrift(
        val harGjenlevendetilleggInnvilget: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(harGjenlevendetilleggInnvilget) {
                title1 {
                    textExpr(
                        Bokmal to "For deg som mottar gjenlevendetillegg".expr(),
                        Nynorsk to "For deg som mottar gjenlevendetillegg".expr(),
                        English to "".expr()
                    )
                }
                paragraph {  }
            }
        }
    }
}