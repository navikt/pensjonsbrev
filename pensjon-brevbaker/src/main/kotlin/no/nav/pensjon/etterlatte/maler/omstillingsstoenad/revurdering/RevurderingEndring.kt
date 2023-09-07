package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.fraser.OMSInnvilgelse
import no.nav.pensjon.etterlatte.maler.fraser.common.OMSFelles
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.*
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OMSRevurderingDTOSelectors.avkortingsinfo
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OMSRevurderingDTOSelectors.beregningsinfo
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OMSRevurderingDTOSelectors.erEndret
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OMSRevurderingDTOSelectors.etterbetalinginfo
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OMSRevurderingDTOSelectors.innhold

data class OMSRevurderingEndringDTO(
    override val innhold: List<Element>,
    val avkortingsinfo: Avkortingsinfo,
    val etterbetalinginfo: EtterbetalingDTO? = null,
    val beregningsinfo: Beregningsinfo,
    val erEndret: Boolean
): BrevDTO

@TemplateModelHelpers
object RevurderingEndring : EtterlatteTemplate<OMSRevurderingEndringDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_REVURDERING_ENDRING

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OMSRevurderingEndringDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Revurdering av omstillingsstønad",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vi har ",
                Nynorsk to "",
                English to "",
            )
            showIf(erEndret) {
                text(
                    Bokmal to "endret",
                    Nynorsk to "",
                    English to "",
                )
            } orShow {
                text(
                    Bokmal to "vurdert",
                    Nynorsk to "",
                    English to "",
                )
            }
            text(
                Bokmal to " omstillingsstønaden din",
                Nynorsk to "",
                English to "",
            )
        }

        outline {
            includePhrase(Vedtak.BegrunnelseForVedtaket)

            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(OMSInnvilgelse.Inntektsendring)
            includePhrase(OMSInnvilgelse.Etteroppgjoer)
            includePhrase(OMSFelles.MeldFraOmEndringer)
            includePhrase(OMSFelles.DuHarRettTilAaKlage)
            includePhrase(OMSFelles.HarDuSpoersmaal)
        }

        includeAttachment(beregningAvOmstillingsstoenad, beregningsinfo)
        includeAttachment(informasjonOmOvergangsstoenad, avkortingsinfo)
        includeAttachment(dineRettigheterOgPlikterOMS, avkortingsinfo)
        includeAttachment(informasjonOmYrkesskade, avkortingsinfo)
        includeAttachmentIfNotNull(etterbetaling, etterbetalinginfo)

    }
}