package no.nav.pensjon.etterlatte.maler.omstillingsstoenad

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTOSelectors.AvdoedEYOSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTOSelectors.AvdoedEYOSelectors.navn
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTOSelectors.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTOSelectors.UtbetalingsinfoSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTOSelectors.UtbetalingsinfoSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTOSelectors.UtbetalingsinfoSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.fraser.OMSInnvilgelse
import no.nav.pensjon.etterlatte.maler.fraser.common.OMSFelles
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

@TemplateModelHelpers
object OMSInnvilgelseAuto : EtterlatteTemplate<OMSInnvilgelseDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_INNVILGELSE_MANUELL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OMSInnvilgelseDTO::class,
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Manuelt brev for omstillingsstønad",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Language.Bokmal to "Vi har innvilget søknaden din om omstillingsstoenad",
            )
        }

        outline {
            includePhrase(Vedtak.Overskrift)

            includePhrase(OMSInnvilgelse.Vedtak(utbetalingsinfo.virkningsdato, avdoed.navn, avdoed.doedsdato))
            includePhrase(OMSInnvilgelse.BeregningOgUtbetaling(utbetalingsinfo.grunnbeloep, utbetalingsinfo.beregningsperioder))
            includePhrase(OMSInnvilgelse.Beregningsgrunnlag(utbetalingsinfo.inntekt))
            includePhrase(OMSInnvilgelse.Utbetaling)
            includePhrase(OMSInnvilgelse.EtterbetalingOgSkatt(utbetalingsinfo.virkningsdato))
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