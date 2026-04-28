package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsAutoDtoSelectors.vedtakData
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.avkortetPgaRedusertTrygdetid
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.egenopptjentUforetrygd
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.endringNettoBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.endringNettoGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.endringNettoUforetrygdUtenTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.endringReduksjonsprosent
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.harGradertUfoeretrygd
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.harMinstesats
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.hjemmeltekst
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.nettoBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.nettoGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.nettoUforetrygdUtenTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.nyMinstesats
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.maler.legacy.vedlegg.opplysningerBruktIBeregningUTLegacySelector
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.reduksjonsprosent
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.tidligereMinstesats
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDataSelectors.tillegg
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
object VedtakOmLavereMinstesatsAuto : AutobrevTemplate<VedtakOmLavereMinstesatsAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_VEDTAK_LAVERE_MINSTESATS_2026

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av minstesats fom 1. juli 2026",
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
        includeAttachment(
            vedleggOpplysningerBruktIBeregningUTLegacy,
            argument.select(opplysningerBruktIBeregningUTLegacySelector<VedtakOmLavereMinstesatsAutoDto> { vedtakData.pe }),
            data.pe.inkluderopplysningerbruktiberegningen(),
        )
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, data.orienteringOmRettigheterUfoere)
    }
}