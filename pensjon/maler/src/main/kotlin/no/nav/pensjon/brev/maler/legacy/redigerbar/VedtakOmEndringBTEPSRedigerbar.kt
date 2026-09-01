package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmEndringBarnetilleggEPSRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.vedtakOmEndringBarnetilleggEPSRedigerbarDto.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.vedtakOmEndringBarnetilleggEPSRedigerbarDto.pesysData.vedtakData
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmEndringBarnetilleggEPSData.*
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.ufoer.EndringBTEPS
import no.nav.pensjon.brev.maler.legacy.inkluderopplysningerbruktiberegningen
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmEndringBTEPSRedigerbar : RedigerbarTemplate<VedtakOmEndringBarnetilleggEPSRedigerbarDto> {

    override val featureToggle = FeatureToggles.vedtakOmOktBunnfradrag.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_VEDTAK_ENDRING_BT_EPS_2026_RED
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Endring barnetillegg pga lovendring annen forelder",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val data = pesysData.vedtakData

        title {
            text(
                bokmal { +"Nav har endret barnetillegg i uføretrygden din" },
                nynorsk { +"Nav har endra barnetillegg i uføretrygda di" },
            )
        }
        outline {
            includePhrase(EndringBTEPS.OutlineRedigerbar(
                EndringBTEPS.Brevdata(
                    nettoUforetrygdUtenTillegg = data.nettoUforetrygdUtenTillegg.ifNull(Kroner(0)),
                    nettoBarnetilleggFB = data.nettoBarnetilleggFB.ifNull(Kroner(0)),
                    nettoBarnetilleggSB = data.nettoBarnetilleggSB.ifNull(Kroner(0)),
                    totalbelop = data.totalbelop.ifNull(Kroner(0)),
                    barnetilleggSB = data.barnetilleggSB,
                )
            ))
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, data.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe, data.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, data.dineRettigheterOgPlikterUfore)
    }
}
