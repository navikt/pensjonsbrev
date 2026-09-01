package no.nav.pensjon.brev.maler.ufoereBrev.regelendr26.auto

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.ReverseringLavereMinstesatsAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.reverseringLavereMinstesatsAutoDto.data
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.reverseringLavereMinstesatsDto.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.reverseringLavereMinstesatsDto.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.reverseringLavereMinstesatsDto.pe
import no.nav.pensjon.brev.maler.fraser.ufoer.ReverseringLavereMinstesats
import no.nav.pensjon.brev.maler.legacy.inkluderopplysningerbruktiberegningen
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object ReverseringLavereMinstesatsAuto : AutobrevTemplate<ReverseringLavereMinstesatsAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_REVERSERING_LAVERE_MINSTESATS_2026

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - omgjøring av reduksjon i minstesats",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {

        val data = this.data
        title {
            text(
                bokmal { +"Vedtaksbrev - Omgjøring av reduksjon i minstesats" },
                nynorsk { +"Vedtaksbrev - Omgjering av reduksjon i minstesats" },
            )
        }
        outline {
            includePhrase(
                ReverseringLavereMinstesats.Outline(data)
            )
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, data.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe, data.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, data.orienteringOmRettigheterUfoere)
    }
}