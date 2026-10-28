package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntekt

import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object Grunnbeloep : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Folketrygdens grunnbeløp endres hvert år, og uføretrygden din blir justert ut fra dette." },
                nynorsk { + "Grunnbeløpet i folketrygda blir endra kvart år, og uføretrygda di blir justert ut frå dette." },
            )
        }
    }
}