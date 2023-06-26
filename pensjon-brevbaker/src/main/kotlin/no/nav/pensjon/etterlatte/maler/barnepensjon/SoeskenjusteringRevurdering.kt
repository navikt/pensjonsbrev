package no.nav.pensjon.etterlatte.maler.barnepensjon

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BarnepensjonRevurderingSoeskenjusteringDTO
import no.nav.pensjon.etterlatte.maler.BarnepensjonRevurderingSoeskenjusteringDTOSelectors.grunnForJustering
import no.nav.pensjon.etterlatte.maler.BarnepensjonRevurderingSoeskenjusteringDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.BarnepensjonSoeskenjusteringGrunn
import no.nav.pensjon.etterlatte.maler.EndringIUtbetaling
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.antallBarn
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beloep
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.soeskenjustering
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

@TemplateModelHelpers
object SoeskenjusteringRevurdering : EtterlatteTemplate<BarnepensjonRevurderingSoeskenjusteringDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_SOESKENJUSTERING

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonRevurderingSoeskenjusteringDTO::class,
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring i barnepensjon",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val (endringIBarnepensjon, kunVurdert) = BarnepensjonSoeskenjusteringGrunn.values()
            .partition { it.endring != EndringIUtbetaling.SAMME }
        title {
            showIf(grunnForJustering.isOneOf(*endringIBarnepensjon.toTypedArray())) {
                text(
                    Language.Bokmal to "Vi har endret barnepensjonen din",
                )
            }
            showIf(grunnForJustering.isOneOf(*kunVurdert.toTypedArray())) {
                text(Language.Bokmal to "Vi har vurdert barnepensjonen din")
            }
        }

        outline {
            includePhrase(Vedtak.Overskrift)
            includePhrase(
                Barnepensjon.RevurderingSoeskenjusteringBehandlingsvedtak(
                    virkningsdato = utbetalingsinfo.virkningsdato,
                    soeskenjusteringType = grunnForJustering,
                    beloep = utbetalingsinfo.beloep
                )
            )


            includePhrase(Barnepensjon.BeregningOgUtbetalingOverskrift)
            includePhrase(
                Barnepensjon.SlikHarViBeregnetPensjonenDin(
                    beregningsperioder = utbetalingsinfo.beregningsperioder,
                    soeskenjustering = utbetalingsinfo.soeskenjustering,
                    antallBarn = utbetalingsinfo.antallBarn
                )
            )

            includePhrase(Barnepensjon.Utbetaling)
            includePhrase(Barnepensjon.Regulering)

            includePhrase(Barnepensjon.InformasjonTilDegOverskrift)
            includePhrase(Barnepensjon.MeldFraOmEndringer)
            includePhrase(Barnepensjon.EndringAvKontonummer)
            includePhrase(Barnepensjon.SkattetrekkPaaBarnepensjonRevurdering)
            includePhrase(Barnepensjon.DuHarRettTilAaKlage)
            includePhrase(Barnepensjon.DuHarRettTilInnsyn)
            includePhrase(Barnepensjon.HarDuSpoersmaal)
        }

    }
}