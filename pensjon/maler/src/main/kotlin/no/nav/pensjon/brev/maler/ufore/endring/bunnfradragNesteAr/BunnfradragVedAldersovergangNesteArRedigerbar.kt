package no.nav.pensjon.brev.maler.ufore.endring.bunnfradragNesteAr

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.BunnfradragVedAldersovergangNesteArRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArRedigerbarDto.pesysData
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArRedigerbarDto.pesysData.data
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.pe
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object BunnfradragVedAldersovergangNesteArRedigerbar : RedigerbarTemplate<BunnfradragVedAldersovergangNesteArRedigerbarDto> {
    override val kode = Pesysbrevkoder.Redigerbar.UT_BUNNFRADRAG_ALDERSOVERGANG_NESTE_AR_RED

    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Inntekt og bunnfradrag ved overgang til alderspensjon neste år",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Så mye kan du tjene det året du går over til alderspensjon" },
                nynorsk { +"Så mykje kan du tene det året du går over til alderspensjon" },
            )
        }
        outline {
            includePhrase(BunnfradragVedAldersovergangNesteAr.Outline(pesysData.data))
        }
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.data.pe)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, pesysData.data.orienteringOmRettigheterUfoere)
    }
}
