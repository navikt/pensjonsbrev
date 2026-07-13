package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmIFUReduksjonsprosentAutoDto.*
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmIFUReduksjonsprosentData.*
import no.nav.pensjon.brev.maler.fraser.OktMinsteIFUReduksjonsprosent
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
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
            displayTitle = "Vedtaksbrev - økt minste IFU",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val data = vedtakData

        title {
            showIf(data.etterbetalingJuli.greaterThan(0)) {
                text(
                    bokmal { +"Vedtaksbrev - Du får en etterbetaling av uføretrygd " },
                    nynorsk { +"Vedtaksbrev - Du får ein etterbetaling av uføretrygd " },
                )
            }.orShowIf(data.etterbetalingJuli.equalTo(0) and not(data.endringNettoUforetrygdUtenTillegg or data.endringNettoBarnetillegg or data.endringNettoGjenlevendetillegg)) {
                text(
                    bokmal { +"Vedtaksbrev - Ingen endring av utbetalt uføretrygd" },
                    nynorsk { +"Vedtaksbrev - Ingen endring av utbetalt uføretrygd" },
                )
            }.orShow {
                text(
                    bokmal { +"Vedtaksbrev - Endring av uføretrygd" },
                    nynorsk { +"Vedtaksbrev - Endring av uføretrygd" },
                )
            }
        }
        outline {
            includePhrase(
                OktMinsteIFUReduksjonsprosent.Outline(
                    OktMinsteIFUReduksjonsprosent.Brevdata(
                        redigerbar = false.expr(),
                        beregningFomDato = data.beregningFomDato,
                        totalbelop = data.totalbelop,
                        nettoUforetrygdUtenTillegg = data.nettoUforetrygdUtenTillegg,
                        nettoBarnetillegg = data.nettoBarnetillegg,
                        nettoGjenlevendetillegg = data.nettoGjenlevendetillegg,
                        etterbetalingJuli = data.etterbetalingJuli,
                        uforegrad = data.uforegrad,
                        reduksjonsprosent = data.reduksjonsprosent,
                        inntektstak = data.inntektstak,
                        ifu = data.ifu,
                        endringNettoUforetrygdUtenTillegg = data.endringNettoUforetrygdUtenTillegg,
                        endringNettoBarnetillegg = data.endringNettoBarnetillegg,
                        endringNettoGjenlevendetillegg = data.endringNettoGjenlevendetillegg,
                        endringInntektstak = data.endringInntektstak,
                        harBelopsendring = data.erInntektsavkortet,
                        tillegg = data.tillegg,
                        hjemler = data.hjemler,
                        visOktMinsteIFU = true.expr(),
                        visReduksjonsprosent = false.expr(),
                        inntektsgrense = data.inntektsgrense,
                        endringInntektsgrense = data.endringInntektsgrense,
                        endringUforegrad = data.endringUforegrad
                    )
                )
            )
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, data.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe, data.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, data.dineRettigheterOgPlikterUfore)
    }
}