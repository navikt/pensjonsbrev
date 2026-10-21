package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.Companion.pensjon
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.*
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.PE_KLAGE_BESLUTNING_OM_VEDTAKET_SKAL_IVERKSETTES
import no.nav.pensjon.brev.model.Brevkategori.*
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object KlageBeslutningOmVedtaketSkalIverksettesPE : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = PE_KLAGE_BESLUTNING_OM_VEDTAKET_SKAL_IVERKSETTES
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = pensjon

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - beslutning om vedtaket skal iverksettes",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title { text(bokmal { +"Beslutning om at vedtak " + fritekst("dato") + " skal iverksettes" }) }

        outline {

            paragraph {
                text(bokmal {
                    +"Nav har besluttet at vedtak "
                    +fritekst("dato") + " skal iverksettes. "
                    +"Vedtaket vil derfor bli gjennomført som planlagt."
                })
            }
        }

    }
}