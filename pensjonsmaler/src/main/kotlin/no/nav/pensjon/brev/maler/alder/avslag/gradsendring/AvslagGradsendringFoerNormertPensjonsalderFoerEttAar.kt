package no.nav.pensjon.brev.maler.alder.avslag.gradsendring

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AvslagGradsendringFoerNormertPensjonsalderFoerEttAar : RedigerbarTemplate<AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_GRAD_FOER_NORM_PEN_ALDER_ETT_AAR

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag endring av uttaksgrad - før ett år",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
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
                    regelverkType = pesysData.regelverkType,
                )
            )
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
    }

    override val kategori: TemplateDescription.Brevkategori =
        TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
}
