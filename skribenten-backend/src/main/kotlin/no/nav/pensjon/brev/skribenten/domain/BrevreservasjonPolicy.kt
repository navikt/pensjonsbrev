package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success
import java.time.Duration
import java.time.Instant
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

class BrevreservasjonPolicy {
    val timeout: Duration = 10.minutes.toJavaDuration()

    fun erGyldig(reservasjon: Reservasjon, fra: Instant): Boolean =
        reservasjon.timestamp.plus(timeout).isAfter(fra)

    fun kanReservere(brev: Brevredigering, fra: Instant, saksbehandler: NavIdent): Outcome<Boolean, ReservertAvAnnen> {
        val eksisterende = brev.gjeldendeReservasjon(this)

        return if (eksisterende == null || eksisterende.reservertAv == saksbehandler || !erGyldig(eksisterende, fra)) {
            success(true)
        } else {
            failure(ReservertAvAnnen(eksisterende.copy(vellykket = false)))
        }
    }

    data class ReservertAvAnnen(val eksisterende: Reservasjon) : BrevredigeringError
}