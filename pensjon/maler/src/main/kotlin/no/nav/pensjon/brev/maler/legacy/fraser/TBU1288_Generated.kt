package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference

object TBU1288_Generated : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        //[TBU1288NN, TBU1288, TBU1288EN]

        paragraph {
            text(
                bokmal { + "Du kan lese mer om beregningen av barnetillegg i vedlegget " },
                nynorsk { + "Du kan lese meir om berekninga av barnetillegg i vedlegget " },
            )
            namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
            text(bokmal { + "." }, nynorsk { + "." })
        }
    }
}
