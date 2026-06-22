package no.nav.pensjon.brev.maler.klageOgAnke

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.Companion.pensjon
import no.nav.pensjon.brev.api.model.TemplateDescription.*
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmOversendelseTilKlageinstansDto
import no.nav.pensjon.brev.model.Brevkategori.*
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
object KlageOrienteringOmOversendelseTilKageinstans : RedigerbarTemplate<KlageOrienteringOmOversendelseTilKlageinstansDto> {

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
                text(bokmal { +"Klageren: " }, english { +"Appellant: " })
                text(bokmal { +pesysData.navn + " " }, english { +pesysData.navn + " " })
                text(bokmal { +pesysData.foedselsnummer.format() }, english { +pesysData.foedselsnummer.format() })
            }
            paragraph {
                text(bokmal { +"Klagemotpart: " }, english { +"Other party: " })
                text(bokmal { +pesysData.navnAvsenderEnhet }, english { +pesysData.navnAvsenderEnhet })
            }
        }
    }
}