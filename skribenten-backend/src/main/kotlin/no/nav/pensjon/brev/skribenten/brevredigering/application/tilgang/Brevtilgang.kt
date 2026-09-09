package no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy.KanIkkeRedigere.LaastBrev
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.brevredigering.domain.SendBrevPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.db.Transactional
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import java.sql.Connection
import java.time.Instant

class Brevtilgang(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val attesterBrevPolicy: AttesterBrevPolicy,
    private val sendBrevPolicy: SendBrevPolicy,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val transactional: Transactional,
) {


    suspend fun <R, E> forLesing(
        brevId: BrevId,
        saksId: SaksId?,
        isolationLevel: Int? = null,
        block: suspend BrevScope.() -> Outcome<R, E>?,
    ): Outcome<R, E>? =
        transactional.rollbackOnFailure(isolationLevel) {
            val brev = BrevredigeringEntity.findByIdAndSaksId(brevId, saksId) ?: return@rollbackOnFailure null
            BrevScope(brev, brevreservasjonPolicy).block()
        }

    suspend fun <R> forRedigering(
        brevId: BrevId,
        saksId: SaksId,
        frigiReservasjon: Boolean,
        block: suspend BrevScope.() -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? =
        reservertOgSjekket(brevId, saksId, frigiReservasjon, sjekk = { brev, principal ->
            kanRedigere(brev, principal, tillatKlarmarkertBrev = false)
        }, block = block)

    suspend fun forStatusendring(
        brevId: BrevId,
        saksId: SaksId,
        endre: suspend BrevScope.() -> Outcome<Unit, BrevredigeringError>,
    ): Outcome<Dto.BrevInfo, BrevredigeringError>? {
        reserver(brevId, saksId)?.onError { return failure(it) } ?: return null

        return transactional.rollbackOnFailure {
            val brev = BrevredigeringEntity.findByIdAndSaksId(brevId, saksId) ?: return@rollbackOnFailure null
            val scope = BrevScope(brev, brevreservasjonPolicy)

            kanRedigere(brev, PrincipalInContext.require(), tillatKlarmarkertBrev = true)
                .onError { return@rollbackOnFailure failure(it) }

            brev.frigiReservasjon()
            scope.endre().onError { return@rollbackOnFailure failure(it) }

            with(scope) { success(brev.tilBrevInfo()) }
        }
    }

    suspend fun <R> forAttestering(
        brevId: BrevId,
        saksId: SaksId,
        frigiReservasjon: Boolean,
        block: suspend BrevScope.() -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? =
        reservertOgSjekket(brevId, saksId, frigiReservasjon, sjekk = { brev, principal ->
            kanAttestere(brev, principal)
        }, block = block)

    suspend fun <R> forSending(
        brevId: BrevId,
        saksId: SaksId,
        block: suspend BrevScope.(Dto.Document) -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? {
        reserver(brevId, saksId)?.onError { return failure(it) } ?: return null

        return transactional.rollbackOnFailure(Connection.TRANSACTION_REPEATABLE_READ) {
            val brev = BrevredigeringEntity.findByIdAndSaksId(brevId, saksId) ?: return@rollbackOnFailure null
            val document = brev.document ?: return@rollbackOnFailure null

            sendBrevPolicy.kanSende(brev, document).onError { return@rollbackOnFailure failure(it) }

            BrevScope(brev, brevreservasjonPolicy).block(document)
        }
    }

    suspend fun <R> forSletting(
        brevId: BrevId,
        saksId: SaksId,
        block: suspend BrevScope.() -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? =
        reservertOgSjekket(brevId, saksId, frigiReservasjon = false, sjekk = { _, _ -> success(Unit) }, block = { block() })

    suspend fun reserver(brevId: BrevId, saksId: SaksId): Outcome<Reservasjon, BrevredigeringError>? =
        transactional.rollbackOnFailure(Connection.TRANSACTION_REPEATABLE_READ) {
            BrevredigeringEntity.findByIdAndSaksId(brevId, saksId)
                ?.reserver(Instant.now(), PrincipalInContext.require().navIdent, brevreservasjonPolicy)
        }

    private suspend fun <R> reservertOgSjekket(
        brevId: BrevId,
        saksId: SaksId,
        frigiReservasjon: Boolean,
        sjekk: suspend (BrevredigeringEntity, UserPrincipal) -> Outcome<Unit, BrevredigeringError>,
        block: suspend BrevScope.() -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? {
        // Forsøk å reservere brevet før vi kjører blokken, om reservasjonen feiler returner feilen eller om brevet ikke finnes returner null.
        reserver(brevId, saksId)?.onError { return failure(it) } ?: return null

        return transactional.rollbackOnFailure {
            val brev = BrevredigeringEntity.findByIdAndSaksId(brevId, saksId) ?: return@rollbackOnFailure null

            sjekk(brev, PrincipalInContext.require()).onError { return@rollbackOnFailure failure(it) }

            // Frigis før blokken, slik at brev-DTO-er blokken bygger viser riktig reservasjonstilstand.
            if (frigiReservasjon) brev.frigiReservasjon()

            BrevScope(brev, brevreservasjonPolicy).block()
        }
    }

    private fun kanRedigere(
        brev: Brevredigering,
        principal: UserPrincipal,
        tillatKlarmarkertBrev: Boolean,
    ): Outcome<Unit, BrevredigeringError> {
        redigerBrevPolicy.kanRedigere(brev, principal)
            .onError(ignore = { tillatKlarmarkertBrev && it is LaastBrev }) { return failure(it) }

        return success(Unit)
    }

    private fun kanAttestere(brev: Brevredigering, principal: UserPrincipal): Outcome<Unit, BrevredigeringError> {
        attesterBrevPolicy.kanAttestere(brev, principal).onError { return failure(it) }
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        return success(Unit)
    }
}

/**
 * Gir tilgang til brevet og til DTO-mapping, uten at hver usecase må injisere [BrevreservasjonPolicy].
 */
class BrevScope(val brev: BrevredigeringEntity, private val brevreservasjonPolicy: BrevreservasjonPolicy) {
    fun BrevredigeringEntity.tilDto(coverage: Set<LetterMarkupWithDataUsage.Property>? = null): Dto.Brevredigering =
        toDto(brevreservasjonPolicy, coverage)

    fun BrevredigeringEntity.tilBrevInfo(): Dto.BrevInfo =
        toBrevInfo(brevreservasjonPolicy)
}
