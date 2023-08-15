package no.nav.pensjon.etterlatte.maler.omstillingsstoenad

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import no.nav.pensjon.etterlatte.maler.AvkortingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.AvkortingsinfoSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.AvkortingsinfoSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.AvkortingsinfoSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.OMSInnvilgelse
import no.nav.pensjon.etterlatte.maler.fraser.common.OMSFelles
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseDTOSelectors.avkortingsinfo
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseDTOSelectors.etterbetalingsinfo

data class OMSInnvilgelseDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val avkortingsinfo: Avkortingsinfo,
    val avdoed: Avdoed,
    val etterbetalingsinfo: EtterbetalingDTO
)

@TemplateModelHelpers
object OMSInnvilgelseAuto : EtterlatteTemplate<OMSInnvilgelseDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_INNVILGELSE_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OMSInnvilgelseDTO::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Manuelt brev for omstillingsstønad",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vi har innvilget søknaden din om omstillingsstønad",
            )
        }

        outline {
            includePhrase(Vedtak.Overskrift)

            includePhrase(OMSInnvilgelse.Vedtak(avkortingsinfo.virkningsdato, avdoed.navn, avdoed.doedsdato, etterbetalingsinfo))
            includePhrase(OMSInnvilgelse.BeregningOgUtbetaling(avkortingsinfo.grunnbeloep, avkortingsinfo.beregningsperioder))
            includePhrase(OMSInnvilgelse.Beregningsgrunnlag(avkortingsinfo.inntekt))
            includePhrase(OMSInnvilgelse.Utbetaling)
            includePhrase(OMSInnvilgelse.EtterbetalingOgSkatt(avkortingsinfo.virkningsdato))
            includePhrase(OMSInnvilgelse.Regulering)
            includePhrase(OMSInnvilgelse.Aktivitetsplikt)
            includePhrase(OMSInnvilgelse.Inntektsendring)
            includePhrase(OMSInnvilgelse.Etteroppgjoer)
            includePhrase(OMSFelles.MeldFraOmEndringer)
            includePhrase(OMSFelles.DuHarRettTilAaKlage)
            includePhrase(OMSFelles.DuHarRettTilInnsyn)
            includePhrase(OMSFelles.HarDuSpoersmaal)
        }

    }
}