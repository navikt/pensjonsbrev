package no.nav.pensjon.brev.maler.ufore.endring.bunnfradragNesteAr

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.OktBunnfradragNesteArAutoDto
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArAutoDto.data
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.pe
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.vedtakVirkFom
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.year

@TemplateModelHelpers
object OktBunnfradragNesteArAuto : AutobrevTemplate<OktBunnfradragNesteArAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.UT_VEDTAK_OKT_BUNNFRADRAG_NESTE_AR_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Du kan ha høyere inntekt før vi reduserer uføretrygden din",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"I " + data.vedtakVirkFom.year.format() + " kan du ha høyere inntekt før vi reduserer uføretrygden din" },
                nynorsk { +"I " + data.vedtakVirkFom.year.format() + " kan du ha høgare inntekt før vi reduserer uføretrygda di" },
            )
        }
        outline {
            includePhrase(OktBunnfradragNesteAr.Outline(data))
        }
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, data.orienteringOmRettigheterUfoere)

    }
}
