package no.nav.pensjon.brev.maler.ufore.endring

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.OktBunnfradragNesteArRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.pe
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArRedigerbarDto.pesysData
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArRedigerbarDto.pesysData.data
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.vedtakVirkFom
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.endring.bunnfradragNesteAr.OktBunnfradragNesteAr
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.year
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object OktBunnfradragNesteArRedigerbar : RedigerbarTemplate<OktBunnfradragNesteArRedigerbarDto> {
    override val featureToggle = FeatureToggles.brevmalUtDesemberkjoring.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_VEDTAK_OKT_BUNNFRADRAG_NESTE_AR_RED
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Du kan ha høyere inntekt før vi reduserer uføretrygden din",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"I " + pesysData.data.vedtakVirkFom.year.format() + " kan du ha høyere inntekt før vi reduserer uføretrygden din" },
                nynorsk { +"I " + pesysData.data.vedtakVirkFom.year.format() + " kan du ha høgare inntekt før vi reduserer uføretrygda di" },
            )
        }
        outline {
            includePhrase(OktBunnfradragNesteAr.Outline(pesysData.data))
        }
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.data.pe)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, pesysData.data.orienteringOmRettigheterUfoere)
    }
}
