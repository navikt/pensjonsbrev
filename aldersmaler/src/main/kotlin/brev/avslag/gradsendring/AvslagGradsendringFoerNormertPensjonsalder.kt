package brev.avslag.gradsendring

import dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.afpBruktIBeregning
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.avtaleland
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.borINorge
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.harEOSLand
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.minstePensjonssats
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.normertPensjonsalder
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.opplysningerBruktIBeregningen
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.totalPensjon
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderDto
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderDtoSelectors.pesysData
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.prorataBruktIBeregningen
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.uttaksgrad
import no.nav.pensjon.brev.brev.FeatureToggles
import no.nav.pensjon.brev.maler.alder.avslag.gradsendring.AvslagGradsendringFoerNormertPensjonsalderFelles
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP2025Vedlegg
import no.nav.pensjon.brev.model.alder.Aldersbrevkoder
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AvslagGradsendringFoerNormertPensjonsalder : RedigerbarTemplate<AvslagUttakFoerNormertPensjonsalderDto> {

    override val featureToggle = FeatureToggles.apAvslagGradsendringNormertPensjonsalder.toggle

    override val kode = Aldersbrevkoder.Redigerbar.PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag endring av uttaksgrad",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { + "Nav har avslått søknaden din om endring av alderspensjonen" },
                nynorsk { + "Nav har avslått søknaden din om endring av alderspensjonen" },
                english { + "Your application to change your retirement pension has been declined" },
            )
        }

        outline {
            includePhrase(
                AvslagGradsendringFoerNormertPensjonsalderFelles(
                    afpBruktIBeregning = pesysData.afpBruktIBeregning,
                    normertPensjonsalder = pesysData.normertPensjonsalder,
                    uttaksgrad = pesysData.opplysningerBruktIBeregningen.uttaksgrad,
                    prorataBruktIBeregningen = pesysData.opplysningerBruktIBeregningen.prorataBruktIBeregningen,
                    virkFom = pesysData.virkFom,
                    minstePensjonssats = pesysData.minstePensjonssats,
                    totalPensjon = pesysData.totalPensjon,
                    borINorge = pesysData.borINorge,
                    harEOSLand = pesysData.harEOSLand,
                    regelverkType = pesysData.regelverkType,
                    avtaleland = pesysData.avtaleland,
                )
            )
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
        includeAttachment(
            template = opplysningerBruktIBeregningenAP2025Vedlegg,
            attachmentData = pesysData.opplysningerBruktIBeregningen
        )
    }

    override val kategori: TemplateDescription.Brevkategori =
        TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
}