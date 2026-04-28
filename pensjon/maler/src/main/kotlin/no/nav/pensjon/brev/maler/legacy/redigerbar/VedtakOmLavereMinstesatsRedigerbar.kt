package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmLavereMinstesatsRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmLavereMinstesatsRedigerbarDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmLavereMinstesatsRedigerbarDtoSelectors.PesysDataSelectors.vedtakData
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
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.LavereMinstesats
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
object VedtakOmLavereMinstesatsRedigerbar : RedigerbarTemplate<VedtakOmLavereMinstesatsRedigerbarDto> {

    override val featureToggle = FeatureToggles.vedtakOmLavereMinstesats.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_VEDTAK_OM_LAVERE_MINSTESATS_2026
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av minstesats fom 1. juli 2026",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val data = pesysData.vedtakData

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
            argument.select(opplysningerBruktIBeregningUTLegacySelector<VedtakOmLavereMinstesatsRedigerbarDto> { pesysData.vedtakData.pe }),
            data.pe.inkluderopplysningerbruktiberegningen(),
        )
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, data.orienteringOmRettigheterUfoere)
    }
}
