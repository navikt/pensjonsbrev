package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success
import java.time.Duration
import java.time.Instant
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

// TODO: Det er noe som lukter med de statiske feltene her, skal se på det i handler for fornyReservasjon
class BrevreservasjonPolicy {

    companion object {
        val timeout: Duration = 10.minutes.toJavaDuration()

        fun isValid(reservasjon: Reservasjon, fra: Instant): Boolean {
            return reservasjon.timestamp.plus(timeout).isAfter(fra)
        }
    }

    fun kanReservere(brev: Brevredigering, fra: Instant, saksbehandler: NavIdent): Outcome<Boolean, ReservertAvAnnen> {
        val eksisterende = brev.reservasjon

        return if (eksisterende == null || eksisterende.reservertAv == saksbehandler || !isValid(eksisterende, fra)) {
            success(true)
        } else {
            failure(ReservertAvAnnen(eksisterende))
        }
    }

    data class ReservertAvAnnen(val eksisterende: Reservasjon) : BrevredigeringError
}