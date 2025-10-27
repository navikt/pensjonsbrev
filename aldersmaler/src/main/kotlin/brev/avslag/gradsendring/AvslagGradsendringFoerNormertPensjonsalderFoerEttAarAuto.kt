package no.nav.pensjon.brev.maler.alder.avslag.gradsendring

import dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.model.alder.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto
import no.nav.pensjon.brev.model.alder.Aldersbrevkoder
import no.nav.pensjon.brev.model.alder.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

@TemplateModelHelpers
object AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAuto : AutobrevTemplate<AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER_ETT_AAR_AUTO

    override val template = createTemplate(
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
                bokmal { + "Nav har avslått søknaden din om endring av alderspensjonen" },
                nynorsk { + "Nav har avslått søknaden din om endring av alderspensjonen" },
                english { + "Your application to change your retirement pension has been declined" },
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
