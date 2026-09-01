package no.nav.pensjon.brev.maler.ufoereBrev.regelendr26.auto

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmEndringBarnetilleggEPSAutoDto.vedtakData
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmEndringBarnetilleggEPSData.*
import no.nav.pensjon.brev.maler.fraser.ufoer.EndringBTEPS
import no.nav.pensjon.brev.maler.legacy.inkluderopplysningerbruktiberegningen
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmEndringBTEPSOktoberAuto : AutobrevTemplate<VedtakOmEndringBarnetilleggEPSAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_VEDTAK_ENDRING_BT_EPS_2026_OKT_AUTO

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Endring barnetillegg pga lovendring annen forelder",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val data = vedtakData

        title {
            text(
                bokmal { +"Nav har endret barnetillegg i uføretrygden din" },
                nynorsk { +"Nav har endra barnetillegg i uføretrygda di" },
            )
        }
        outline {
            includePhrase(
                EndringBTEPS.Outline(
                    EndringBTEPS.Brevdata(
                        nettoUforetrygdUtenTillegg = data.nettoUforetrygdUtenTillegg.ifNull(Kroner(0)),
                        nettoBarnetilleggFB = data.nettoBarnetilleggFB.ifNull(Kroner(0)),
                        nettoBarnetilleggSB = data.nettoBarnetilleggSB.ifNull(Kroner(0)),
                        totalbelop = data.totalbelop.ifNull(Kroner(0)),
                        barnetilleggSB = data.barnetilleggSB,
                    )
                )
            )
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, data.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe, data.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, data.dineRettigheterOgPlikterUfore)
    }
}