package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentAutoDtoSelectors.vedtakData
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.endringInntektstak
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.endringNettoBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.endringNettoGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.endringNettoUforetrygdUtenTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.erInntektsavkortet
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.etterbetalingJuli
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.hjemler
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.ifu
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.nettoBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.nettoGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.nettoUforetrygdUtenTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.reduksjonsprosent
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.tillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.totalbelop
import no.nav.pensjon.brev.maler.fraser.OktMinsteIFUReduksjonsprosent
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmOktMinsteIFUAuto : AutobrevTemplate<VedtakOmIFUReduksjonsprosentAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_VEDTAK_MINSTE_IFU_2026_AUTO

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - økt minste IFU fom 1. januar 2026",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val data = vedtakData

        title {
            text(
                bokmal { +"Vedtaksbrev - Nav endrer uføretrygden din" },
                nynorsk { +"Vedtaksbrev - Nav endrar uføretrygda di" },
            )
        }
        outline {
            includePhrase(
                OktMinsteIFUReduksjonsprosent.Outline(
                    OktMinsteIFUReduksjonsprosent.Brevdata(
                        totalbelop = data.totalbelop,
                        nettoUforetrygdUtenTillegg = data.nettoUforetrygdUtenTillegg,
                        nettoBarnetillegg = data.nettoBarnetillegg,
                        nettoGjenlevendetillegg = data.nettoGjenlevendetillegg,
                        etterbetalingJuli = data.etterbetalingJuli,
                        reduksjonsprosent = data.reduksjonsprosent,
                        inntektstak = data.inntektstak,
                        ifu = data.ifu,
                        endringNettoUforetrygdUtenTillegg = data.endringNettoUforetrygdUtenTillegg,
                        endringNettoBarnetillegg = data.endringNettoBarnetillegg,
                        endringNettoGjenlevendetillegg = data.endringNettoGjenlevendetillegg,
                        endringInntektstak = data.endringInntektstak,
                        erInntektsavkortet = data.erInntektsavkortet,
                        tillegg = data.tillegg,
                        hjemler = data.hjemler,
                        visOktMinsteIFU = true.expr(),
                        visReduksjonsprosent = false.expr(),
                    )
                )
            )
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, data.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe, data.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, data.orienteringOmRettigheterUfoere)
    }
}