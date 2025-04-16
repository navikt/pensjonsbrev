package no.nav.pensjon.brev.maler.alder.avslag.gradsendring

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

@TemplateModelHelpers
object AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAuto : AutobrevTemplate<AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER_ETT_AAR_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag endring av uttaksgrad - før ett år",
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
                AvslagGradsendringFoerNormertPensjonsalderFoerEttAarFelles(
                    regelverkType = regelverkType,
                )
            )
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
    }
}
