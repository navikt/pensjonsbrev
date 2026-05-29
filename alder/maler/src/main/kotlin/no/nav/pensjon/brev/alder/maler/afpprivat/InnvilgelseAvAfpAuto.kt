package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.afpprivat.InnvilgelseAvAfpInnhold
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.AfpBeregningSelectors.kompensasjonstilleggBrutto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.AfpBeregningSelectors.kronetilleggBrutto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.AfpBeregningSelectors.livsvarigBrutto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.AfpBeregningSelectors.totalPensjon
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.afpBeregning
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.bosattINorge
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.brukerUnder70Aar
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.kravMottattDato
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.virkningFom
import no.nav.pensjon.brev.template.AutobrevTemplate
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
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av AFP i privat sektor",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Søknaden din om avtalefestet pensjon (AFP) i privat sektor er innvilget – melding om vedtak" },
                nynorsk { +"Søknaden din om avtalefesta pensjon (AFP) i privat sektor er innvilga – melding om vedtak" },
            )
        }

        outline {
            includePhrase(
                InnvilgelseAvAfpInnhold(
                    kravMottattDato = kravMottattDato,
                    virkningFom = virkningFom,
                    totalPensjon = afpBeregning.totalPensjon,
                    livsvarigBrutto = afpBeregning.livsvarigBrutto,
                    kronetilleggBrutto = afpBeregning.kronetilleggBrutto,
                    kompensasjonstilleggBrutto = afpBeregning.kompensasjonstilleggBrutto,
                    brukerUnder70Aar = brukerUnder70Aar,
                    bosattINorge = bosattINorge,
                ),
            )
            includePhrase(HarDuSpoersmaal.alder)
        }
    }
}
