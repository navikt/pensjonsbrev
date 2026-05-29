package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmEtterbetalingOpphor2026RedigerbarDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmEtterbetalingOpphor2026RedigerbarDtoSelectors.PesysDataSelectors.dineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmEtterbetalingOpphor2026RedigerbarDtoSelectors.PesysDataSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmEtterbetalingOpphor2026RedigerbarDtoSelectors.PesysDataSelectors.hjemler
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmEtterbetalingOpphor2026RedigerbarDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.VedtakOmEtterbetalingOpphor2026
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
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
            displayTitle = "Vedtaksbrev - Du får en etterbetaling fra Nav",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Vedtaksbrev - Du får en etterbetaling fra Nav" },
                nynorsk { +"Vedtaksbrev - Du får ei etterbetaling frå Nav" },
            )
        }
        outline {
            includePhrase(VedtakOmEtterbetalingOpphor2026.Outline(etterbetaling = pesysData.etterbetaling, hjemler = pesysData.hjemler, erRedigerbar = true.expr()))
            includePhrase(VedtakOmEtterbetalingOpphor2026.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsynRedigerbarebrev)
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, pesysData.dineRettigheterOgPlikterUfore)
    }
}
