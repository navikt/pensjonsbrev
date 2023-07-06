package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.etterlatte.maler.EndringIUtbetaling
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo

enum class BarnepensjonSoeskenjusteringGrunn(val endring: EndringIUtbetaling) {
    NYTT_SOESKEN(EndringIUtbetaling.REDUSERES),
    SOESKEN_DOER(EndringIUtbetaling.OEKES),
    SOESKEN_INN_INSTITUSJON_INGEN_ENDRING(EndringIUtbetaling.SAMME),
    SOESKEN_INN_INSTITUSJON_ENDRING(EndringIUtbetaling.OEKES),
    SOESKEN_UT_INSTITUSJON(EndringIUtbetaling.REDUSERES),
    FORPLEID_ETTER_BARNEVERNSLOVEN(EndringIUtbetaling.OEKES),
    SOESKEN_BLIR_ADOPTERT(EndringIUtbetaling.OEKES),
}

data class BarnepensjonRevurderingSoeskenjusteringDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val grunnForJustering: BarnepensjonSoeskenjusteringGrunn,
)
