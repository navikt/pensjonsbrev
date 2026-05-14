package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentAutoDtoSelectors.vedtakData
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.avkortetPgaRedusertTrygdetid
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.egenopptjentUforetrygd
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.endringNettoBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.endringNettoGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.endringNettoUforetrygdUtenTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.endringReduksjonsprosent
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.harGradertUfoeretrygd
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.harMinstesats
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.hjemmeltekst
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.nettoBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.nettoGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.nettoUforetrygdUtenTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.nyMinstesats
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.reduksjonsprosent
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.tidligereMinstesats
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentDataSelectors.tillegg
import no.nav.pensjon.brev.maler.fraser.LavereMinstesats
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmOktMinsteIFUAuto : AutobrevTemplate<VedtakOmIFUReduksjonsprosentAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_VEDTAK_LAVERE_MINSTESATS_2026

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - økt minste IFU fom 1. juli 2026",
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
                LavereMinstesats.Outline(
                    LavereMinstesats.Brevdata(
                        nettoUforetrygdUtenTillegg = data.nettoUforetrygdUtenTillegg,
                        nettoBarnetillegg = data.nettoBarnetillegg,
                        nettoGjenlevendetillegg = data.nettoGjenlevendetillegg,
                        endringNettoUforetrygdUtenTillegg = data.endringNettoUforetrygdUtenTillegg,
                        endringNettoBarnetillegg = data.endringNettoBarnetillegg,
                        endringNettoGjenlevendetillegg = data.endringNettoGjenlevendetillegg,
                        endringReduksjonsprosent = data.endringReduksjonsprosent,
                        reduksjonsprosent = data.reduksjonsprosent,
                        harMinstesats = data.harMinstesats,
                        tidligereMinstesats = data.tidligereMinstesats,
                        nyMinstesats = data.nyMinstesats,
                        tillegg = data.tillegg,
                        egenopptjentUforetrygd = data.egenopptjentUforetrygd,
                        avkortetPgaRedusertTrygdetid = data.avkortetPgaRedusertTrygdetid,
                        harGradertUfoeretrygd = data.harGradertUfoeretrygd,
                        hjemmeltekst = data.hjemmeltekst,
                    )
                )
            )
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, data.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, data.pe, data.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, data.orienteringOmRettigheterUfoere)
    }
}