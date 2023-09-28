package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.EndringIUtbetaling
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.antallBarn
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beloep
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.soeskenjustering
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingSoeskenjusteringDTOSelectors.grunnForJustering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingSoeskenjusteringDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Lover
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Soeskenjustering
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak


enum class BarnepensjonSoeskenjusteringGrunn(val endring: EndringIUtbetaling) {
    NYTT_SOESKEN(EndringIUtbetaling.REDUSERES),
    SOESKEN_DOER(EndringIUtbetaling.OEKES),
    SOESKEN_INN_INSTITUSJON_INGEN_ENDRING(EndringIUtbetaling.SAMME),
    SOESKEN_INN_INSTITUSJON_ENDRING(EndringIUtbetaling.OEKES),
    SOESKEN_UT_INSTITUSJON(EndringIUtbetaling.REDUSERES),
    FORPLEID_ETTER_BARNEVERNSLOVEN(EndringIUtbetaling.OEKES),
    SOESKEN_BLIR_ADOPTERT(EndringIUtbetaling.OEKES)
}

data class BarnepensjonRevurderingSoeskenjusteringDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val grunnForJustering: BarnepensjonSoeskenjusteringGrunn
)

@TemplateModelHelpers
object SoeskenjusteringRevurdering : EtterlatteTemplate<BarnepensjonRevurderingSoeskenjusteringDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_SOESKENJUSTERING

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonRevurderingSoeskenjusteringDTO::class,
        languages = languages(Bokmal),
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
                    Bokmal to "Vi har endret barnepensjonen din",
                )
            }
            showIf(grunnForJustering.isOneOf(*kunVurdert.toTypedArray())) {
                text(Bokmal to "Vi har vurdert barnepensjonen din")
            }
        }

        outline {
            includePhrase(Vedtak.Overskrift)
            includePhrase(
                Soeskenjustering.RevurderingSoeskenjusteringBehandlingsvedtak(
                    virkningsdato = utbetalingsinfo.virkningsdato,
                    soeskenjusteringType = grunnForJustering,
                    beloep = utbetalingsinfo.beloep
                )
            )
            includePhrase(Barnepensjon.TilOgMedKalendermaaneden18Aar)
            includePhrase(Lover.Folketrygdloven18og22)


            includePhrase(Barnepensjon.BeregningOgUtbetalingOverskrift)
            includePhrase(
                Barnepensjon.SlikHarViBeregnetPensjonenDin(
                    beregningsperioder = utbetalingsinfo.beregningsperioder,
                    soeskenjustering = utbetalingsinfo.soeskenjustering,
                    antallBarn = utbetalingsinfo.antallBarn
                )
            )
            includePhrase(Barnepensjon.BeregnetPensjonTabell(utbetalingsinfo.beregningsperioder))

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