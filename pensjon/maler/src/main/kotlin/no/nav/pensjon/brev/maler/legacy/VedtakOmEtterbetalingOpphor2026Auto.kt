package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEtterbetalingOpphor2026AutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEtterbetalingOpphor2026AutoDtoSelectors.dineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEtterbetalingOpphor2026AutoDtoSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEtterbetalingOpphor2026AutoDtoSelectors.hjemler
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEtterbetalingOpphor2026AutoDtoSelectors.reduksjonsprosent
import no.nav.pensjon.brev.maler.fraser.VedtakOmEtterbetalingOpphor2026
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmEtterbetalingOpphor2026Auto : AutobrevTemplate<VedtakOmEtterbetalingOpphor2026AutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_VEDTAK_ETTERBETALING_OPPHOR_2026

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtaksbrev opphørt sak - økt minste IFU og lavere reduksjonsprosent",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            showIf(etterbetaling.greaterThan(0)) {
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
            includePhrase(VedtakOmEtterbetalingOpphor2026.Outline(etterbetaling = etterbetaling, hjemler = hjemler, reduksjonsprosent = reduksjonsprosent))
            includePhrase(VedtakOmEtterbetalingOpphor2026.RettTilAAKlage)
            includePhrase(Ufoeretrygd.RettTilInnsyn)
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, dineRettigheterOgPlikterUfore)
    }
}