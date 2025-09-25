package brev.felles

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference

data class RettTilAAKlage(
    val vedlegg: AttachmentTemplate<LangBokmalNynorskEnglish, *>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Du har rett til å klage" },
                nynorsk { +"Du har rett til å klage" },
                english { +"You have the right of appeal" },
            )
        }
        paragraph {
            text(
                bokmal { +"Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget " },
                nynorsk { +"Viss du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. I vedlegget " },
                english { +"If you believe the decision is wrong, you may appeal. The deadline for appeal is six weeks from the date you received the decision. In the attachment " },
            )
            namedReference(vedlegg)
            text(
                bokmal { +" får du vite mer om hvordan du går fram. Du finner skjema og informasjon på ${Constants.KLAGE_URL}." },
                nynorsk { +" får du vite meir om korleis du går fram. Du finn skjema og informasjon på ${Constants.KLAGE_URL}." },
                english { +", you can find out more about how to proceed. You will find forms and information at $${Constants.KLAGE_URL}." },
            )
        }
    }
}