package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmOktFribelopRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.vedtakOmOktFribelopRedigerbarDto.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.vedtakOmOktFribelopRedigerbarDto.pesysData.vedtakData
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktFribelopData.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktFribelopData.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktFribelopData.pe
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.OktFribelop
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
object VedtakOmOktFribelopRedigerbar : RedigerbarTemplate<VedtakOmOktFribelopRedigerbarDto> {

    override val featureToggle = FeatureToggles.vedtakOmOktBunnfradrag.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_VEDTAK_OKT_FRIBELOP_2026_RED
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - økt fribeløp",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val data = pesysData.vedtakData

        title {
            text(
                bokmal { +"Du kan nå ha høyere inntekt før vi reduserer uføretrygden din" },
                nynorsk { +"Du kan no ha høgare inntekt før vi reduserer uføretrygda di" },
            )
        }
        outline {
            includePhrase(OktFribelop.Outline(data))
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, data.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe, data.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, data.orienteringOmRettigheterUfoere)
    }
}
