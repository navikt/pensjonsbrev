package no.nav.pensjon.brev.maler.klageOgAnke

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.*
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.*
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KLAGEINSTANS
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.model.Brevkategori.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker.foedselsnummer
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.navEnhet.navn

@TemplateModelHelpers

// Erstatte PE_IY_03_157
object KlageOrienteringOmOversendelseTilKlageinstans :
    RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.brevmalKlageOrienteringOmOversendelseTilKlageinstans.toggle

    override val kode = PE_KLAGE_ORIENTERING_OM_OVERSENDELSE_KLAGEINSTANS
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = ALLE
    override val sakstyper = Sakstype.all

    override val template = createTemplate(
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - orientering om oversendelse til Nav klageinstans",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title { text(bokmal { +"Klage - " + fritekst("ytelse") }, english { +"Appeal - " + fritekst("ytelse") }) }

        outline {
            paragraph {
                text(bokmal { +"Klageren: " }, english { +"Appellant: " }, BOLD)
                text(bokmal { +felles.bruker.fulltNavn() + " " }, english { +felles.bruker.fulltNavn() + " " })
                text(bokmal { +felles.bruker.foedselsnummer.format() }, english { +felles.bruker.foedselsnummer.format() })
            }
            paragraph {
                text(bokmal { +"Klagemotpart: " }, english { +"Other party: " }, BOLD)
                text(bokmal { +felles.avsenderEnhet.navn }, english { +felles.avsenderEnhet.navn })
            }
            paragraph {
                text(
                    bokmal { +"Klagesaken er nå sendt til $NAV_KLAGEINSTANS for avgjørelse." },
                    english { +"The appeal has now been sent to $NAV_KLAGEINSTANS for a decision." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Når saken er avgjort, vil du få melding direkte fra $NAV_KLAGEINSTANS." },
                    english { +"When the case has been decided, you will receive notification directly from $NAV_KLAGEINSTANS." }
                )
            }
            paragraph { text(bokmal { +"Vedlegg: Kopi av brev til $NAV_KLAGEINSTANS" }, english { +"Attachment: Copy of letter to $NAV_KLAGEINSTANS." }) }
        }
    }
}