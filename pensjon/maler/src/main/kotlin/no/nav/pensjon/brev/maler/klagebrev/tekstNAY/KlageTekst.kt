package no.nav.pensjon.brev.maler.klagebrev.tekstNAY

import no.nav.pensjon.brev.maler.fraser.common.Constants.INNSYNSKRAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

// Med ankemulighet
object DuKanKlagePaaVedtaketKlage : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        title1 { text(bokmal { +"Du kan klage på vedtaket" }) }
        paragraph {
            text(bokmal {
                +"Hvis du mener vedtaket er feil, har du rett til å klage. "
                +"Du må sende klagen innen 6 uker fra den datoen vedtaket har kommet fram til deg. "
                +"Dette følger av forvaltningsloven § 21-12. "
                +"Du finner mer informasjon om hvordan du kan klage på $KLAGE_URL."
            })
        }
        paragraph {
            text(bokmal {
                +"Nav kan veilede deg på telefon hvis du trenger hjelp til å sende en klage. "
                +"Du kan også få hjelp av Nav-kontoret ditt til å skrive en klage. "
                +"Hvis du velger å sende klagen på papir, må du huske å signere den."

            })
        }
        paragraph {
            text(bokmal {
                +"Når du klager, vil den Nav-enheten som har skrevet vedtaket, vurdere saken din på nytt. Hvis du får medhold i klagen din, mottar du et nytt vedtak. "
                +"Hvis du ikke får medhold, sender vi klagen videre til Nav klageinstans. "
                +"Du får brev når saken er sendt dit. "
                +"I brevet står det hva som skjer videre. "
                +"Før en sak kan bringes inn for domstolene, må du ha brukt muligheten til å klage og anke. "
                +"Dette følger av folketrygdloven § 21-12."
            })
        }
    }
}

object DuHarRettTilInnsynKlage : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        title1 { text(bokmal { +"Du har rett til innsyn i saken din" }) }
        paragraph {
            text(bokmal {
                +"Du har rett til å se dokumentene i saken din. Dette følger av forvaltningsloven § 18. "
                +"Du har tilgang til mange av dokumentene dine digitalt på $NAV_URL. "
                +"Hvis det er dokumenter du ikke finner, kan du be oss om innsyn i disse. "
                +"Du kan lese mer om retten til innsyn på $INNSYNSKRAV_URL."
            })
        }
    }
}

