package brev.felles

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

class DuHarRettTilInnsyn : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Du har rett til innsyn" },
                nynorsk { +"Du har rett til innsyn" },
                english { +"You have the right to access your file" },
            )
        }
        paragraph {
            text(
                bokmal { +"Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram." },
                nynorsk { +"Du har rett til å sjå dokumenta i saka di. I vedlegget får du vite korleis du går fram." },
                english { +"You have the right to access all documents pertaining to your case. The attachment includes information on how to proceed." },
            )
        }
    }
}