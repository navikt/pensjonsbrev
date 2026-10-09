package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.felles.RettTilAaKlageAfpPrivat
import no.nav.pensjon.brev.alder.maler.felles.RettigheterPersonopplysninger
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggDinAfpPrivatBeregning
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDto
import no.nav.pensjon.brev.alder.model.afpprivat.selectors.innvilgelseAvAfpAutoDto.afpBeregning.*
import no.nav.pensjon.brev.alder.model.afpprivat.selectors.innvilgelseAvAfpAutoDto.*
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — innvilgelse av AFP i privat sektor (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_115`. Brevkroppen er felles med
 * den redigerbare malen `InnvilgelseAvAfp` (PE_AF_04_111) og ligger i
 * [InnvilgelseAvAfpInnhold].
 */
@TemplateModelHelpers
object InnvilgelseAvAfpAuto : AutobrevTemplate<InnvilgelseAvAfpAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_INNVILGELSE_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av AFP i privat sektor",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Nav har innvilget søknaden din om avtalefestet pensjon (AFP) i privat sektor" },
                nynorsk { +"Nav har innvilga søknaden din om avtalefesta pensjon (AFP) i privat sektor" },
                english { +"Nav has granted your application for contractual pension (AFP) in the private sector" }
            )
        }

        outline {
            includePhrase(
                InnvilgelseAvAfpInnhold(
                    virkningFom = virkningFom,
                    totalPensjon = afpBeregning.totalPensjon,
                    kronetilleggBrutto = afpBeregning.kronetilleggBrutto,
                    kompensasjonstilleggBrutto = afpBeregning.kompensasjonstilleggBrutto,
                    brukerUnder70Aar = brukerUnder70Aar,
                    bosattINorge = bosattINorge,
                    harEtterbetaling = harEtterbetaling,
                ),
            )
            includePhrase(RettigheterPersonopplysninger)
            includePhrase(RettTilAaKlageAfpPrivat)
            includePhrase(HarDuSpoersmaal.alder)
        }

        includeAttachmentIfNotNull(vedleggDinAfpPrivatBeregning, dinAfpPrivatBeregning)
    }
}
