package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmEndringBarnetilleggEPSAutoDto.*
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmEndringBarnetilleggEPSData.*
import no.nav.pensjon.brev.maler.fraser.EndringBTEPSVedMinsteIFUReduksjonsprosentRev
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
object VedtakOmEndringBarnetilleggEPSRevAuto : AutobrevTemplate<VedtakOmEndringBarnetilleggEPSAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_VEDTAK_ENDRING_BT_EPS_2026_REV_AUTO

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring barnetillegg berørt sak ved regelendringer 1. oktober 2026",
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
                EndringBTEPSVedMinsteIFUReduksjonsprosentRev.Outline(
                    EndringBTEPSVedMinsteIFUReduksjonsprosentRev.Brevdata(
                        nettoUforetrygdUtenTillegg = data.nettoUforetrygdUtenTillegg.ifNull(Kroner(0)),
                        nettoBarnetilleggFB = data.nettoBarnetilleggFB.ifNull(Kroner(0)),
                        nettoBarnetilleggSB = data.nettoBarnetilleggSB.ifNull(Kroner(0)),
                        totalbelop = data.totalbelop.ifNull(Kroner(0)),
                        samletInntektsgrenseBarnetillegg = data.samletInntektsgrenseBarnetillegg.ifNull(Kroner(0)),
                        fribelop = data.fribelop.ifNull(Kroner(0)),
                        barnetilleggSB = data.barnetilleggSB,
                        opphortUforetrygdEllerBTFB = data.opphortUforetrygdEllerBTFB,
                    )
                )
            )
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, data.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe, data.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, data.dineRettigheterOgPlikterUfore)
    }
}