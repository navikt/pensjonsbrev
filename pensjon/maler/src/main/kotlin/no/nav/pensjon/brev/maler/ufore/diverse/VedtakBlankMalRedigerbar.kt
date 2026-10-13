package no.nav.pensjon.brev.maler.ufore.diverse

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakBlankMalRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.vedtakBlankMalRedigerbarDto.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.vedtakBlankMalRedigerbarDto.pesysData.*
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.inkluderopplysningerbruktiberegningen
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.TemplateRootScope.RedigerbartVedlegg
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakBlankMalRedigerbar : RedigerbarTemplate<VedtakBlankMalRedigerbarDto> {

    override val featureToggle = FeatureToggles.brevmalUtVedtakBlankMal.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_VEDTAK_BLANK_MAL_RED
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"" + fritekst("Tittel") },
                nynorsk { +"" + fritekst("Tittel") }
            )
        }

        outline {
            includePhrase(Ufoeretrygd.RettTilAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        @OptIn(RedigerbartVedlegg::class)
        includeAttachmentRedigerbarIfNotNull(
            VedleggId("blankUTMaanedligUfoeretrygdFoerSkatt"),
            vedleggMaanedligUfoeretrygdFoerSkatt,
            pesysData.maanedligUfoeretrygdFoerSkatt,
        )
        @OptIn(RedigerbartVedlegg::class)
        includeAttachmentRedigerbar(
            VedleggId("blankUTBeregningVedlegg"),
            vedleggOpplysningerBruktIBeregningUTLegacy,
            pesysData.pe,
            pesysData.pe.inkluderopplysningerbruktiberegningen(),
        )
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, pesysData.orienteringOmRettigheterUfoere)
    }
}
