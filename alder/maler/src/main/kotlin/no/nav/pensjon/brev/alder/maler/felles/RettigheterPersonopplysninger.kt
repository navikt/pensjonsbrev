package no.nav.pensjon.brev.alder.maler.felles

import no.nav.pensjon.brev.alder.maler.felles.Constants.PERSONVERNERKLAERING_URL
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
                english { +"Your rights regarding your personal data" }
            )
        }
        paragraph {
            text(
                bokmal { +"Du finner informasjon om hvordan Nav behandler personopplysningene dine, og hvilke rettigheter du har, på $PERSONVERNERKLAERING_URL." },
                nynorsk { +"Du finn informasjon om korleis Nav behandlar personopplysningane dine og kva rettar du har på $PERSONVERNERKLAERING_URL." },
                english { +"You can find information about how Nav handles your personal data, and what rights you have, at $PERSONVERNERKLAERING_URL." }
            )
        }
    }
}


