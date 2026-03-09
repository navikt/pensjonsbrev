package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.*

data class TBU2212_Generated(
val vedlegg: AttachmentTemplate<LangBokmalNynorskEnglish, *>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Du må melde fra om endringer" },
                nynorsk { +"Du må melde frå om endringar" },
                english { +"You must notify any changes" },
            )
        }
        paragraph {
            text(
                bokmal { +"Skjer det endringer, må du melde fra til oss med en gang. I vedlegget " },
                nynorsk { +"Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget " },
                english { +"You must notify us immediately of any changes in your situation. In the attachment " },
            )
            namedReference(vedlegg)
            text(
                bokmal { +" ser du hvilke endringer du må si fra om." },
                nynorsk { + " ser du kva endringar du må seie frå om." },
                english { +" you will see which changes you must report." },
            )
        }
    }
}
