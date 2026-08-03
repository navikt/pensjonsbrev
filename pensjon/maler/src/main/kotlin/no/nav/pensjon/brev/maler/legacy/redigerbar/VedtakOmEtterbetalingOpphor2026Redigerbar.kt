package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmEtterbetalingOpphor2026RedigerbarDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.vedtakOmEtterbetalingOpphor2026RedigerbarDto.pesysData.*
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.vedtakOmEtterbetalingOpphor2026RedigerbarDto.*
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.VedtakOmEtterbetalingOpphor2026
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
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
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmEtterbetalingOpphor2026Redigerbar : RedigerbarTemplate<VedtakOmEtterbetalingOpphor2026RedigerbarDto> {
    override val featureToggle = FeatureToggles.vedtakOmOktMinsteIFUOgReduksjonsprosent.toggle
    override val kode = Pesysbrevkoder.Redigerbar.UT_VEDTAK_ETTERBETALING_OPPHOR_2026_RED
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtaksbrev opphørt sak - økt minste IFU og lavere reduksjonsprosent",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            showIf(pesysData.etterbetaling.greaterThan(0)) {
                text(
                    bokmal { +"Vedtaksbrev - Du får en etterbetaling av uføretrygd " },
                    nynorsk { +"Vedtaksbrev - Du får ein etterbetaling av uføretrygd " },
                )
            }.orShow {
                text(
                    bokmal { +"Vedtaksbrev - Ingen endring av utbetalt uføretrygd" },
                    nynorsk { +"Vedtaksbrev - Ingen endring av utbetalt uføretrygd" },
                )
            }
        }
        outline {
            includePhrase(VedtakOmEtterbetalingOpphor2026.Outline(etterbetaling = pesysData.etterbetaling, hjemler = pesysData.hjemler, reduksjonsprosent = pesysData.reduksjonsprosent, uforegrad = pesysData.uforegrad, ifu = pesysData.ifu, endringUforegrad = pesysData.endringUforegrad, endringIfu = pesysData.endringIfu, erRedigerbar = true.expr()))
            includePhrase(VedtakOmEtterbetalingOpphor2026.RettTilAAKlage)
            includePhrase(Ufoeretrygd.RettTilInnsyn)
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, pesysData.dineRettigheterOgPlikterUfore)
    }
}
