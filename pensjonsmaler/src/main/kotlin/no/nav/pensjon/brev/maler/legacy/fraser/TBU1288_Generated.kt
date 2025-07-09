package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference

object TBU1288_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //[TBU1288NN, TBU1288, TBU1288EN]

        paragraph {
            text(
                Bokmal to "Du kan lese mer om beregningen av barnetillegg i vedlegget ",
                Nynorsk to "Du kan lese meir om berekninga av barnetillegg i vedlegget ",
                English to "Read more about how child supplements are calculated in the attachment called ",
            )
            namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
            text(Bokmal to ".", Nynorsk to ".", English to ".")
        }
    }
}
