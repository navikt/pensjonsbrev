package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.Barnepensjon
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.AvdoedEYBSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedEYBSelectors.navn
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTO
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.antallBarn
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beloep
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.soeskenjustering
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.virkningsdato

@TemplateModelHelpers
object BarnepensjonVedtak : EtterlatteTemplate<BarnepensjonVedtakDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_VEDTAK

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonVedtakDTO::class,
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd fordi du fyller 20 år",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Language.Bokmal to "Vi har innvilget søknaded din om barnepensjon",
            )
        }

        outline {

            includePhrase(Vedtak.Overskrift)
            includePhrase(
                Barnepensjon.Vedtak(
                    utbetalingsinfo.virkningsdato,
                    avdoed.navn,
                    avdoed.doedsdato,
                    utbetalingsinfo.beloep
                )
            )

            includePhrase(Barnepensjon.UtbetalingOverskrift)
            includePhrase(
                Barnepensjon.SlikHarViBeregnetPensjonenDin(
                    utbetalingsinfo.beregningsperioder,
                    utbetalingsinfo.soeskenjustering,
                    utbetalingsinfo.antallBarn
                )
            )
            includePhrase(Barnepensjon.MeldFraOmEndringer)
            includePhrase(Barnepensjon.EndringAvKontonummer)
            includePhrase(Barnepensjon.SkattetrekkPaaBarnepensjon)
            includePhrase(Barnepensjon.DuHarRettTilAaKlage)
            includePhrase(Barnepensjon.DuHarRettTilInnsyn)
            includePhrase(Barnepensjon.HarDuSpoersmaal)
        }

    }
}