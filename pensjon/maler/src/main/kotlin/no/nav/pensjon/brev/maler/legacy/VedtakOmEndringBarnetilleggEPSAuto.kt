package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSAutoDtoSelectors.vedtakData
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.arligUtbetalingBarnetilleggFB
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.dineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.fribelop
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.gInntil
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.samletInntektsgrenseBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.inntektBruker
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.inntektEPS
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.nettoBarnetilleggFB
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.nettoUforetrygdUtenTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.nyttBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.samletInntekt
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.totalbelop
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.utbetalingBarnetilleggResten
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSDataSelectors.utbetaltBarnetilleggHittilIAr
import no.nav.pensjon.brev.maler.fraser.EndringBTEPSVedMinsteIFUReduksjonsprosent
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
object VedtakOmEndringBarnetilleggEPSAuto : AutobrevTemplate<VedtakOmEndringBarnetilleggEPSAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_VEDTAK_ENDRING_BT_EPS_2026_AUTO

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring barnetillegg EPS ved regelendringer 1. juli 2026",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val data = vedtakData

        title {
            text(
                bokmal { +"Nav har endret utbetalingen av barnetillegg i uføretrygden din" },
                nynorsk { +"Nav har endra utbetalinga av barnetillegg i uføretrygda di" },
            )
        }
        outline {
            includePhrase(
                EndringBTEPSVedMinsteIFUReduksjonsprosent.Outline(
                    EndringBTEPSVedMinsteIFUReduksjonsprosent.Brevdata(
                        nettoUforetrygdUtenTillegg = data.nettoUforetrygdUtenTillegg,
                        nettoBarnetilleggFB = data.nettoBarnetilleggFB.ifNull(Kroner(0)),
                        totalbelop = data.totalbelop,
                        inntektBruker = data.inntektBruker,
                        inntektEPS = data.inntektEPS,
                        gInntil = data.gInntil,
                        samletInntekt = data.samletInntekt,
                        samletInntektsgrenseBarnetillegg = data.samletInntektsgrenseBarnetillegg,
                        nyttBarnetillegg = data.nyttBarnetillegg,
                        fribelop = data.fribelop,
                        arligUtbetalingBarnetilleggFB = data.arligUtbetalingBarnetilleggFB,
                        utbetaltBarnetilleggHittilIAr = data.utbetaltBarnetilleggHittilIAr,
                        utbetalingBarnetilleggResten = data.utbetalingBarnetilleggResten,
                    )
                )
            )
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, data.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe, data.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, data.dineRettigheterOgPlikterUfore)
    }
}