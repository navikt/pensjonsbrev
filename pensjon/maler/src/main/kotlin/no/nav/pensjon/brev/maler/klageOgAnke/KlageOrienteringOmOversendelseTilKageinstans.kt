package no.nav.pensjon.brev.maler.klageOgAnke

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.Companion.pensjon
import no.nav.pensjon.brev.api.model.TemplateDescription.*
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmOversendelseTilKlageinstansDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmOversendelseTilKlageinstansDtoSelectors.PesysDataSelectors.foedselsnummer
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmOversendelseTilKlageinstansDtoSelectors.PesysDataSelectors.navn
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmOversendelseTilKlageinstansDtoSelectors.PesysDataSelectors.navnAvsenderEnhet
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmOversendelseTilKlageinstansDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KLAGEINSTANS
import no.nav.pensjon.brev.model.Brevkategori.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers

// Erstatte PE_IY_03_157
object KlageOrienteringOmOversendelseTilKageinstans :
    RedigerbarTemplate<KlageOrienteringOmOversendelseTilKlageinstansDto> {

    override val kode = PE_KLAGE_ORIENTERING_OM_OVERSENDELSE_KLAGEINSTANS
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = pensjon

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
                text(bokmal { +pesysData.navn + " " }, english { +pesysData.navn + " " })
                text(bokmal { +pesysData.foedselsnummer.format() }, english { +pesysData.foedselsnummer.format() })
            }
            paragraph {
                text(bokmal { +"Klagemotpart: " }, english { +"Other party: " }, BOLD)
                text(bokmal { +pesysData.navnAvsenderEnhet }, english { +pesysData.navnAvsenderEnhet })
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