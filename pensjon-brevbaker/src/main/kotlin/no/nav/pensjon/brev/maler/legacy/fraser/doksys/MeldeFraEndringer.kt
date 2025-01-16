package no.nav.pensjon.brev.maler.legacy.fraser.doksys

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

// meldeFraEndringer_001
object MeldeFraEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Meld fra om endringer",
                Nynorsk to "Nynorsk fra om endringer",
                Language.English to "Duty to report changes"
            )
        }
        paragraph {
            text(
                Bokmal to "Du må melde fra til oss med en gang hvis det skjer endringer som kan ha betydning for saken din, "
                        + "for eksempel ved endring av sivilstand eller ved flytting.",
                Nynorsk to "Du må melde frå til oss med ein gong dersom det skjer endringar som kanha noko å seie for saka din, "
                        + "for eksempel ved endring av sivilstand ellerved flytting.",
                Language.English to "You must notify us immediately if there are any changes that may affectyour case, "
                        + "such as a change in your marital status or if you move."
            )
        }
    }
}
