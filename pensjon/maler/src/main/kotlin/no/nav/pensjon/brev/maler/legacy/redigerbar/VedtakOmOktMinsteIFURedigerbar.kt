package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.beregningFomDato
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.dineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmIFUReduksjonsprosentRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmIFUReduksjonsprosentRedigerbarDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmIFUReduksjonsprosentRedigerbarDtoSelectors.PesysDataSelectors.vedtakData
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
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.reduksjonsprosent
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.tillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.totalbelop
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.OktMinsteIFUReduksjonsprosent
import no.nav.pensjon.brev.maler.legacy.inkluderopplysningerbruktiberegningen
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmOktMinsteIFURedigerbar : RedigerbarTemplate<VedtakOmIFUReduksjonsprosentRedigerbarDto> {

    override val featureToggle = FeatureToggles.vedtakOmOktMinsteIFUOgReduksjonsprosent.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_VEDTAK_MINSTE_IFU_2026_RED
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtaksbrev - økt minste IFU",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val data = pesysData.vedtakData

        title {
            showIf(data.etterbetalingJuli.greaterThan(0)) {
                text(
                    bokmal { +"Vedtaksbrev - Du får en etterbetaling av uføretrygd " },
                    nynorsk { +"Vedtaksbrev - Du får ein etterbetaling av uføretrygd " },
                )
            }.orShowIf(data.endringNettoUforetrygdUtenTillegg or data.endringNettoBarnetillegg or data.endringNettoGjenlevendetillegg) {
                text(
                    bokmal { +"Vedtaksbrev - Endring av uføretrygd" },
                    nynorsk { +"Vedtaksbrev - Endring av uføretrygd" },
                )
            }.orShow {
                text(
                    bokmal { +"Vedtaksbrev - Ingen endring av utbetalt uføretrygd" },
                    nynorsk { +"Vedtaksbrev - Ingen endring av utbetalt uføretrygd" },
                )
            }
        }
        outline {
            includePhrase(
                OktMinsteIFUReduksjonsprosent.Outline(
                    OktMinsteIFUReduksjonsprosent.Brevdata(
                        redigerbar = true.expr(),
                        beregningFomDato = data.beregningFomDato,
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
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, data.dineRettigheterOgPlikterUfore)
    }
}
