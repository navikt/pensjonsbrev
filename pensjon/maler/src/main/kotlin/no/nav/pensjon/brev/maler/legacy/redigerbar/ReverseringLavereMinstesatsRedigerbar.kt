package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.ReverseringLavereMinstesatsRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.reverseringLavereMinstesatsRedigerbarDto.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.reverseringLavereMinstesatsRedigerbarDto.pesysData.data
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.reverseringLavereMinstesatsDto.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.reverseringLavereMinstesatsDto.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.reverseringLavereMinstesatsDto.pe
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.ufoer.ReverseringLavereMinstesats
import no.nav.pensjon.brev.maler.legacy.inkluderopplysningerbruktiberegningen
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object ReverseringLavereMinstesatsRedigerbar : RedigerbarTemplate<ReverseringLavereMinstesatsRedigerbarDto> {

    override val featureToggle = FeatureToggles.reverseringLavereMinstesats.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_REVERSERING_LAVERE_MINSTESATS_2026_RED
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - omgjøring av reduksjon i minstesats",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val data = pesysData.data

        title {
            text(
                bokmal { +"Vedtaksbrev - Omgjøring av reduksjon i minstesats" },
                nynorsk { +"Vedtaksbrev - Omgjering av reduksjon i minstesats" },
            )
        }
        outline {
            includePhrase(ReverseringLavereMinstesats.Outline(data))
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, data.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe, data.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, data.orienteringOmRettigheterUfoere)
    }
}
