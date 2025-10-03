package brev.felles

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference

data class RettTilInnsyn(
    val vedlegg: AttachmentTemplate<LangBokmalNynorskEnglish, *>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Du har rett til innsyn" },
                nynorsk { + "Du har rett til innsyn" },
                english { + "You have the right to access your file" },
            )
        }

        paragraph {
            text(
                bokmal { + "Du har rett til å se dokumentene i saken din. Se vedlegg " },
                nynorsk { + "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg " },
                english { + "You are entitled to see your case documents. Refer to the attachment " },
            )
            namedReference(vedlegg)
            text(
                bokmal { + " for informasjon om hvordan du går fram." },
                nynorsk { + " for informasjon om korleis du går fram." },
                english { + " for information about how to proceed." },
            )
        }
    }
}
