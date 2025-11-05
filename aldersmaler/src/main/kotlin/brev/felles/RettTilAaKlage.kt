package brev.felles

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object RettTilAAKlage : OutlinePhrase<LangBokmalNynorskEnglish>() {
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
                bokmal { +"Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen vedtaket har kommet fram til deg. Du finner skjema og informasjon på " +
                        "${Constants.KLAGE_URL}." },
                nynorsk { +"Om du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen vedtaket har komme fram til deg. Du finn skjema og informasjon på " +
                        "${Constants.KLAGE_URL}." },
                english { +"If you think the decision is wrong, you may appeal the decision within six weeks from the date the decision was delivered to you. You can find the form and more information at " +
                        "${Constants.KLAGE_URL}." },
            )
        }

        paragraph {
            text(
                bokmal { +"Du får vite mer om hvordan du klager i vedlegget om rettigheter." },
                nynorsk { +"Du får vite meir om korleis du klagar i vedlegget om rettar." },
                english { +"You will find more information on how to appeal in the appendix about your rights." },
            )
        }
    }
}