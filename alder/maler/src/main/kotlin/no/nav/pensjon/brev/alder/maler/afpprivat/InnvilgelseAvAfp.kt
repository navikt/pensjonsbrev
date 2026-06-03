package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afpprivat.InnvilgelseAvAfpInnhold
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggOversiktOverPensjonenAfpPrivat
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.AfpBeregningSelectors.kompensasjonstilleggBrutto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.AfpBeregningSelectors.kronetilleggBrutto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.AfpBeregningSelectors.livsvarigBrutto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.AfpBeregningSelectors.totalPensjon
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.afpBeregning
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.bosattINorge
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.brukerUnder70Aar
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.kravMottattDato
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.oversiktOverPensjonen
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDtoSelectors.virkningFom
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpDto
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — innvilgelse av AFP i privat sektor (redigerbar).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_111`. Brevkroppen er felles med
 * autobrev-varianten [InnvilgelseAvAfpAuto] (`PE_AF_04_115`) og ligger i
 * [InnvilgelseAvAfpInnhold]. Saksbehandler kan redigere selve teksten i
 * Skribenten ved behov.
 */
@TemplateModelHelpers
object InnvilgelseAvAfp : RedigerbarTemplate<InnvilgelseAvAfpDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_INNVILGELSE

    override val featureToggle = FeatureToggles.innvilgelseAvAfp.toggle

    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP_PRIVAT)

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
                bokmal { +"Søknaden din om avtalefestet pensjon (AFP) i privat sektor er innvilget – melding om vedtak" },
                nynorsk { +"Søknaden din om avtalefesta pensjon (AFP) i privat sektor er innvilga – melding om vedtak" },
                english { +"Your application for contractual pension (AFP) in the private sector has been granted - notification of decision" },
            )
        }

        outline {
            includePhrase(
                InnvilgelseAvAfpInnhold(
                    kravMottattDato = pesysData.kravMottattDato,
                    virkningFom = pesysData.virkningFom,
                    totalPensjon = pesysData.afpBeregning.totalPensjon,
                    livsvarigBrutto = pesysData.afpBeregning.livsvarigBrutto,
                    kronetilleggBrutto = pesysData.afpBeregning.kronetilleggBrutto,
                    kompensasjonstilleggBrutto = pesysData.afpBeregning.kompensasjonstilleggBrutto,
                    brukerUnder70Aar = pesysData.brukerUnder70Aar,
                    bosattINorge = pesysData.bosattINorge,
                ),
            )
            includePhrase(HarDuSpoersmaal.alder)
        }

        // PE_AF_oversikt_over_pensjonen_RTF — inkluderes når vedtaket har flere
        // beregningsperioder (i Exstream: BeregningAntallPerioder > 1).
        includeAttachmentIfNotNull(
            vedleggOversiktOverPensjonenAfpPrivat,
            pesysData.oversiktOverPensjonen,
        )
    }
}
