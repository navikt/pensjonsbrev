package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.usecase.Result
import no.nav.pensjon.brev.skribenten.model.NavIdent
import java.time.Duration
import java.time.Instant
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

class BrevreservasjonPolicy(val timeout: Duration = 10.minutes.toJavaDuration()) {

    fun kanReservere(brev: Brevredigering, fra: Instant, saksbehandler: NavIdent): Result<Boolean, ReservertAvAnnen> {
        val eksisterende = brev.reservasjon

        return if (eksisterende == null || eksisterende.reservertAv == saksbehandler || eksisterende.timestamp.plus(timeout).isBefore(fra)) {
            Result.Companion.success(true)
        } else {
            Result.Companion.failure(ReservertAvAnnen(eksisterende))
        }
    }

    data class ReservertAvAnnen(val eksisterende: Reservasjon) : BrevedigeringError
}