package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.vedlegg.*
import java.time.LocalDate

@Suppress("unused")
data class BarnepensjonVedtakDTO(
    val saksnummer: Int,
    val utbetalingsinfo: Utbetalingsinfo,
    val barn: Barn,
    val avdoed: AvdoedEYB,
    val spraak: String,
    val avsender: Avsender,
    val mottaker: Mottaker,
    val utsendingsDato: LocalDate,
    val attestant: Attestant? = null,
) {

    data class Utbetalingsinfo(
        val antallBarn: Int? = null,
        val beloep: Kroner,
        val soeskenjustering: Boolean,
        val virkningsdato: LocalDate,
        val beregningsperioder: List<Beregningsperiode>,
    )

    data class Beregningsperiode(
        val datoFOM: LocalDate,
        val datoTOM: LocalDate?,
        val grunnbeloep: Kroner,
        val antallBarn: Int,
        var utbetaltBeloep: Kroner,
    )

    data class Barn(
        val navn: String,
        val fnr: Foedselsnummer,
    )

    data class AvdoedEYB(
        val navn: String,
        val doedsdato: LocalDate,
    )

    data class Avsender(
        val kontor: String,
        val adresse: String,
        val postnummer: String,
        val telefonnummer: Telefonnummer,
        val saksbehandler: String,
    )

    data class Mottaker(
        val navn: String,
        val adresse: String,
        val postnummer: String,
        val poststed: String,
        val land: String,
    )

    data class Attestant(
        val navn: String,
        val kontor: String,
    )

}