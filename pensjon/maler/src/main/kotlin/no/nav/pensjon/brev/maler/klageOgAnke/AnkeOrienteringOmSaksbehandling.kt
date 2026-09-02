package no.nav.pensjon.brev.maler.klageOgAnke

import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AnkeOrienteringOmSaksbehandling : RedigerbarTemplate<EmptyRedigerbarBrevdataMedSaksbehandlerValg> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_ANKE_ORIENTERING_OM_SAKSBEHANDLING
    override val kategori = no.nav.pensjon.brev.model.Brevkategori.KLAGE_OG_ANKE
    override val brevkontekst = no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = no.nav.pensjon.brev.api.model.Sakstype.all

    override val template = createTemplate(
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Anke - orientering om saksbehandling ved Nav klageinstans",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title { text(bokmal { +"Anke - orientering om saksbehandling" }, english { +"Appeal - information about processing time" }) }
        outline {
            paragraph {
                text(
                    bokmal { +"Dette er en automatisk generert melding som informerer om at anken er mottatt og under behandling." },
                    english { +"This is an automatically generated message informing you that the appeal has been received and is being processed." })
            }
        }
    }
}