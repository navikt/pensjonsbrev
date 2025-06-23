package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.quoted
import no.nav.pensjon.brev.template.dsl.textExpr

object TBU1288_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //[TBU1288NN, TBU1288, TBU1288EN]

        paragraph {
            textExpr(
                Bokmal to "Du kan lese mer om beregningen av barnetillegg i vedlegget ".expr() + quoted("Opplysninger om beregningen") +".",
                Nynorsk to "Du kan lese meir om berekninga av barnetillegg i vedlegget ".expr() + quoted("Opplysningar om berekninga") +".",
                English to "Read more about how child supplements are calculated in the attachment called ".expr() + quoted("Information about calculations") + ".",
            )
        }
    }
}
