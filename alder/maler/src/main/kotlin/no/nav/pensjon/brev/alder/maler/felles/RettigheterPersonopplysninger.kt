package no.nav.pensjon.brev.alder.maler.felles

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object RettigheterPersonopplysninger : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                bokmal { +"Du har rettigheter knyttet til personopplysningene dine" },
                nynorsk { +"Du har rettar knytt til personopplysningane dine" },
                english { +"" }
            )
        }
        paragraph {
            text(
                bokmal { +"Du finner informasjon om hvordan Nav behandler personopplysningene dine, og hvilke rettigheter du har, på ${Constants.PERSONVERNERKLAERING_URL}." },
                nynorsk { +"Du finn informasjon om korleis Nav behandlar personopplysningane dine og kva rettar du har på ${Constants.PERSONVERNERKLAERING_URL}." },
                english { +"" }
            )
        }
    }
}


