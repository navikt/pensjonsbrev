package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU5007_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU5007_NN, TBU5007, TBU5007_EN]

        paragraph {
            text(
                Bokmal to "Hvis du planlegger å flytte eller oppholde deg i et annet land, må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til barnetillegg. Dette gjelder også hvis barnet du forsørger skal oppholde seg i et annet land.",
                Nynorsk to "Om du planlegg å flytte eller opphalde deg i eit anna land må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til barnetillegg. Dette gjeld også om barnet du forsørgjer skal opphalde seg i eit anna land. ",
                English to "If you plan to move or stay in another country, you must contact us so we can evaluate if you still can claim child supplement. This applies also if the child you support is to stay in another country.",
            )
        }
    }
}
