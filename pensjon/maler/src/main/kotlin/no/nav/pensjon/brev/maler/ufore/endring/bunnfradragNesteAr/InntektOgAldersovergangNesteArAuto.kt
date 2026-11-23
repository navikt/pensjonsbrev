package no.nav.pensjon.brev.maler.ufore.endring.bunnfradragNesteAr

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.InntektOgAldersovergangNesteArAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.inntektOgAldersovergangNesteArAutoDto.data
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.inntektOgAldersovergangNesteArDto.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.inntektOgAldersovergangNesteArDto.pe
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.inntektOgAldersovergangNesteArDto.vedtakVirkFom
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.legacy.vedlegg.OpplysningerBruktIBeregningUTProfil
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.year
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object InntektOgAldersovergangNesteArAuto : AutobrevTemplate<InntektOgAldersovergangNesteArAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.UT_INNTEKT_ALDERSOVERGANG_NESTE_AR_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - inntekt og alderspensjon neste år",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Så mye kan du tjene i " + data.vedtakVirkFom.year.format() },
                nynorsk { +"Så mykje kan du tene i " + data.vedtakVirkFom.year.format() },
            )
        }
        outline {
            includePhrase(InntektOgAldersovergangNesteAr.Outline(data))
        }
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy(OpplysningerBruktIBeregningUTProfil.ENDRING), data.pe)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, data.orienteringOmRettigheterUfoere)
    }
}