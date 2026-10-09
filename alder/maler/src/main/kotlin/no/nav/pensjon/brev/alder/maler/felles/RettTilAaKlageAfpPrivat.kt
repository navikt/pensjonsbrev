package no.nav.pensjon.brev.alder.maler.felles

import no.nav.pensjon.brev.alder.maler.felles.Constants.KLAGE_URL
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object RettTilAaKlageAfpPrivat: OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Dine rettigheter" },
                nynorsk { +"Dine rettar " },
                english { +"Your rights " },
            )
        }
        paragraph {
            text(
                bokmal { +"Hvis du mener vedtaket er feil, kan du klage innen 6 uker fra den datoen vedtaket har kommet fram til deg. " +
                        "Dette følger av AFP-tilskottsloven § 17 andre ledd andre punktum, sammenholdt med folketrygdloven § 21-12. " +
                        "Du finner skjema og informasjon på $KLAGE_URL." },
                nynorsk { +"Om du meiner vedtaket er feil, kan du klage innan 6 veker frå den datoen vedtaket har komme fram til deg. " +
                        "Dette følgjer av AFP-tilskottslova § 17 andre ledd andre punktum, samanheldt med folketrygdlova § 21-12. " +
                        "Du finn skjema og informasjon på $KLAGE_URL." },
                english { +"If you believe that the decision is incorrect, you may appeal within 6 weeks from when you receive the decision. " +
                        "This is pursuant to the National Insurance Act § 21-12. You will find the form and further information at $KLAGE_URL." },
            )
        }
    }
}