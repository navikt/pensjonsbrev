package no.nav.pensjon.brev.maler.alder.avslag.gradsendring

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.afpBruktIBeregning
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.harEOSLand
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.minstePensjonssats
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.normertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.opplysningerBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.vedtakBegrunnelseLavOpptjening
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

@TemplateModelHelpers
object AvslagGradsendringFoerNormertPensjonsalderAuto : AutobrevTemplate<AvslagUttakFoerNormertPensjonsalderAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_AVSLAG_GRAD_FOER_NORMERT_PENSJONSALDER_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagUttakFoerNormertPensjonsalderAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag endring av uttaksgrad - AP2025",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Nav har avslått søknaden din om endring av alderspensjonen",
                Nynorsk to "Nav har avslått søknaden din om endring av alderspensjonen",
                English to "Your application to change your retirement pension has been declined",
            )
        }

        outline {
            includePhrase(
                AvslagGradsendringFoerNormertPensjonsalderFelles(
                    afpBruktIBeregning = afpBruktIBeregning,
                    normertPensjonsalder = normertPensjonsalder,
                    opplysningerBruktIBeregningen = opplysningerBruktIBeregningen,
                    virkFom = virkFom,
                    minstePensjonssats = minstePensjonssats,
                    totalPensjon = totalPensjon,
                    borINorge = borINorge,
                    harEOSLand = harEOSLand,
                    vedtakBegrunnelseLavOpptjening = vedtakBegrunnelseLavOpptjening,
                )
            )
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
        includeAttachment(
            template = opplysningerBruktIBeregningenAP,
            attachmentData = opplysningerBruktIBeregningen,
            predicate = vedtakBegrunnelseLavOpptjening
        )
    }
}
