package no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.FerdigRedigertPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy.KanIkkeRedigere.LaastBrev
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.brevredigering.domain.SendBrevPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import java.sql.Connection
import java.time.Instant

/**
 * Intensjonsbasert tilgang til et brev under redigering.
 *
 * I stedet for at hver usecase selv må huske å hente brevet, reservere det og kjøre riktige policies,
 * deklarerer usecasen hva den har til hensikt å gjøre. [Brevtilgang] tar seg av reservasjon, transaksjon,
 * oppslag og policy-sjekk, og kaller blokken kun når alt er i orden.
 *
 * Blokken kalles med et [BrevScope] som gir tilgang til brevet og til DTO-mapping.
 * Returnerer null når brevet ikke finnes, og ruller tilbake transaksjonen når blokken feiler.
 */
class Brevtilgang(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val attesterBrevPolicy: AttesterBrevPolicy,
    private val ferdigRedigertPolicy: FerdigRedigertPolicy,
    private val sendBrevPolicy: SendBrevPolicy,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val database: Database,
) {

    /**
     * For usecases som ikke handler om ett bestemt, eksisterende brev.
     */
    suspend fun <R, E> iTransaksjon(
        isolasjonsnivaa: Int? = null,
        block: suspend BrevMapping.() -> Outcome<R, E>,
    ): Outcome<R, E> =
        transaksjon(isolasjonsnivaa) {
            BrevMapping(brevreservasjonPolicy).block().onError { rollback() }
        }

    /**
     * Les brevet uten å reservere det. Ingen policy kreves, og brevet skal ikke endres.
     */
    suspend fun <R, E> forLesing(
        brevId: BrevId,
        saksId: SaksId?,
        isolasjonsnivaa: Int? = null,
        block: suspend BrevScope.() -> Outcome<R, E>?,
    ): Outcome<R, E>? =
        transaksjon(isolasjonsnivaa) {
            val brev = BrevredigeringEntity.findByIdAndSaksId(brevId, saksId) ?: return@transaksjon null
            BrevScope(brev, brevreservasjonPolicy).block()?.onError { rollback() }
        }

    /**
     * Rediger innholdet i brevet. Reserverer brevet og krever at [RedigerBrevPolicy] er oppfylt.
     *
     * @param tillatKlarmarkertBrev lar endringen gå gjennom selv om brevet er låst for redigering.
     *  Brukes for endringer som ikke berører selve brevteksten.
     * @param sjekkPolicyNaar gjør policy-sjekken betinget, for usecases som er en no-op når brevet
     *  allerede har ønsket tilstand.
     */
    suspend fun <R> forRedigering(
        brevId: BrevId,
        saksId: SaksId,
        tillatKlarmarkertBrev: Boolean = false,
        sjekkPolicyNaar: (Brevredigering) -> Boolean = { true },
        block: suspend BrevScope.() -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? =
        reservertOgSjekket(brevId, saksId, sjekk = { brev, principal ->
            kanRedigere(brev, principal, tillatKlarmarkertBrev, sjekkPolicyNaar)
        }, block = block)

    /**
     * Marker brevet som klar til attestering/sending. Krever i tillegg at brevet er ferdig redigert.
     */
    suspend fun <R> forKlarmarkering(
        brevId: BrevId,
        saksId: SaksId,
        sjekkPolicyNaar: (Brevredigering) -> Boolean = { true },
        block: suspend BrevScope.() -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? =
        reservertOgSjekket(brevId, saksId, sjekk = { brev, principal ->
            kanKlarmarkere(brev, principal, sjekkPolicyNaar)
        }, block = block)

    /**
     * Attester brevet. Krever både [AttesterBrevPolicy] og [RedigerBrevPolicy].
     */
    suspend fun <R> forAttestering(
        brevId: BrevId,
        saksId: SaksId,
        block: suspend BrevScope.() -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? =
        reservertOgSjekket(brevId, saksId, sjekk = { brev, principal ->
            kanAttestere(brev, principal)
        }, block = block)

    /**
     * Send brevet til fagsystemet. Krever at brevet har et gjeldende dokument og at [SendBrevPolicy] er oppfylt.
     */
    suspend fun <R> forSending(
        brevId: BrevId,
        saksId: SaksId,
        block: suspend BrevScope.(Dto.Document) -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? {
        reserver(brevId, saksId)?.onError { return failure(it) } ?: return null

        return transaksjon(Connection.TRANSACTION_REPEATABLE_READ) {
            val brev = BrevredigeringEntity.findByIdAndSaksId(brevId, saksId) ?: return@transaksjon null
            val document = brev.document ?: return@transaksjon null

            sendBrevPolicy.kanSende(brev, document).onError {
                rollback()
                return@transaksjon failure(it)
            }

            BrevScope(brev, brevreservasjonPolicy).block(document)?.onError { rollback() }
        }
    }

    /**
     * Slett brevet. Reserverer brevet, men har ingen ytterligere policy.
     */
    suspend fun <R> forSletting(
        brevId: BrevId,
        saksId: SaksId,
        block: suspend BrevScope.() -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? =
        reservertOgSjekket(brevId, saksId, sjekk = { _, _ -> success(Unit) }, block = block)

    /**
     * Reserver brevet uten å gjøre noe mer. Kjører i egen transaksjon.
     */
    suspend fun reserver(brevId: BrevId, saksId: SaksId): Outcome<Reservasjon, BrevredigeringError>? =
        transaksjon(Connection.TRANSACTION_REPEATABLE_READ) {
            BrevredigeringEntity.findByIdAndSaksId(brevId, saksId)
                ?.reserver(Instant.now(), PrincipalInContext.require().navIdent, brevreservasjonPolicy)
                ?.onError { rollback() }
        }

    private suspend fun <R> reservertOgSjekket(
        brevId: BrevId,
        saksId: SaksId,
        sjekk: suspend (BrevredigeringEntity, UserPrincipal) -> Outcome<Unit, BrevredigeringError>,
        block: suspend BrevScope.() -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? {
        // Forsøk å reservere brevet før vi kjører blokken, om reservasjonen feiler returner feilen eller om brevet ikke finnes returner null.
        reserver(brevId, saksId)?.onError { return failure(it) } ?: return null

        return transaksjon(null) {
            val brev = BrevredigeringEntity.findByIdAndSaksId(brevId, saksId) ?: return@transaksjon null

            sjekk(brev, PrincipalInContext.require()).onError {
                rollback()
                return@transaksjon failure(it)
            }

            BrevScope(brev, brevreservasjonPolicy).block()
                ?.onError { rollback() }
        }
    }

    private fun kanRedigere(
        brev: Brevredigering,
        principal: UserPrincipal,
        tillatKlarmarkertBrev: Boolean,
        sjekkPolicyNaar: (Brevredigering) -> Boolean,
    ): Outcome<Unit, BrevredigeringError> {
        if (!sjekkPolicyNaar(brev)) {
            return success(Unit)
        }
        redigerBrevPolicy.kanRedigere(brev, principal)
            .onError(ignore = { tillatKlarmarkertBrev && it is LaastBrev }) { return failure(it) }

        return success(Unit)
    }

    private fun kanAttestere(brev: Brevredigering, principal: UserPrincipal): Outcome<Unit, BrevredigeringError> {
        attesterBrevPolicy.kanAttestere(brev, principal).onError { return failure(it) }
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        return success(Unit)
    }

    private suspend fun kanKlarmarkere(
        brev: Brevredigering,
        principal: UserPrincipal,
        sjekkPolicyNaar: (Brevredigering) -> Boolean,
    ): Outcome<Unit, BrevredigeringError> {
        if (!sjekkPolicyNaar(brev)) {
            return success(Unit)
        }
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }
        ferdigRedigertPolicy.erFerdigRedigert(brev).onError { return failure(it) }

        return success(Unit)
    }

    private suspend fun <T> transaksjon(isolasjonsnivaa: Int?, block: suspend JdbcTransaction.() -> T): T =
        if (isolasjonsnivaa != null) {
            suspendTransaction(db = database, transactionIsolation = isolasjonsnivaa, statement = block)
        } else {
            suspendTransaction(db = database, statement = block)
        }
}

/**
 * Gir tilgang til DTO-mapping uten at hver usecase må injisere [BrevreservasjonPolicy].
 */
open class BrevMapping(private val brevreservasjonPolicy: BrevreservasjonPolicy) {
    fun BrevredigeringEntity.tilDto(coverage: Set<LetterMarkupWithDataUsage.Property>? = null): Dto.Brevredigering =
        toDto(brevreservasjonPolicy, coverage)

    fun BrevredigeringEntity.tilBrevInfo(): Dto.BrevInfo =
        toBrevInfo(brevreservasjonPolicy)

    fun BrevredigeringEntity.reserverFor(navIdent: NavIdent): Outcome<Reservasjon, BrevreservasjonPolicy.ReservertAvAnnen> =
        reserver(Instant.now(), navIdent, brevreservasjonPolicy)
}

class BrevScope(val brev: BrevredigeringEntity, brevreservasjonPolicy: BrevreservasjonPolicy) : BrevMapping(brevreservasjonPolicy)
