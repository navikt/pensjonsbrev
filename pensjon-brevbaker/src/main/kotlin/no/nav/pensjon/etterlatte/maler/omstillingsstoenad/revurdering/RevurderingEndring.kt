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
import no.nav.pensjon.etterlatte.maler.Beregningsinfo
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetaling
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OMSRevurderingEndringDTOSelectors.erEndret
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OMSRevurderingEndringDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.informasjonOmOmstillingsstoenad
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.informasjonOmYrkesskade

data class OMSRevurderingEndringDTO(
    override val innhold: List<Element>,
    val omstillingsstoenadBeregning: OmstillingsstoenadBeregning,
    val etterbetalinginfo: OmstillingsstoenadEtterbetaling? = null,
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

            includePhrase(OmstillingsstoenadInnvilgelseFraser.Inntektsendring)
            includePhrase(OmstillingsstoenadInnvilgelseFraser.Etteroppgjoer)
            includePhrase(OmstillingsstoenadFellesFraser.MeldFraOmEndringer)
            includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlage)
            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }

        //includeAttachment(beregningAvOmstillingsstoenad, beregningsinfo)
        includeAttachment(informasjonOmOmstillingsstoenad, innhold)
        includeAttachment(dineRettigheterOgPlikter, innhold)
        includeAttachment(informasjonOmYrkesskade, innhold)
        //includeAttachmentIfNotNull(etterbetalingVedlegg, etterbetalinginfo)

    }
}