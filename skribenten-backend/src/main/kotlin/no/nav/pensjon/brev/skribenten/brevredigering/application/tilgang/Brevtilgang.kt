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

/**
 * Intensjonsbasert tilgang til et eksisterende brev.
 *
 * I stedet for at hver usecase selv må huske å hente brevet, reservere det og kjøre riktige policies,
 * deklarerer usecasen hva den har til hensikt å gjøre. [Brevtilgang] tar seg av oppslag, reservasjon
 * og policy-sjekk, og kaller blokken kun når alt er i orden.
 *
 * Blokken kalles med et [BrevScope] som gir tilgang til brevet og til DTO-mapping, og kjøres i en
 * [Transactional] som ruller tilbake om blokken feiler. Returnerer null når brevet ikke finnes.
 *
 * Usecases som ikke handler om ett bestemt, eksisterende brev — som å opprette et nytt brev eller
 * å liste brev på tvers av saker — hører ikke hjemme her, og bruker [Transactional] direkte.
 */
class Brevtilgang(
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val attesterBrevPolicy: AttesterBrevPolicy,
    private val sendBrevPolicy: SendBrevPolicy,
    private val brevreservasjonPolicy: BrevreservasjonPolicy,
    private val transactional: Transactional,
) {

    /**
     * Les brevet uten å reservere det. Ingen policy kreves, og brevet skal ikke endres.
     */
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

    /**
     * Rediger innholdet i brevet. Reserverer brevet og krever at [RedigerBrevPolicy] er oppfylt.
     *
     * @param frigiReservasjon om saksbehandler er ferdig med brevet etter denne endringen, eller
     *  fortsatt skal ha det reservert. Uten svar er det lett å bli sittende på en reservasjon.
     */
    suspend fun <R> forRedigering(
        brevId: BrevId,
        saksId: SaksId,
        frigiReservasjon: Boolean,
        block: suspend BrevScope.() -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? =
        reservertOgSjekket(brevId, saksId, frigiReservasjon, sjekk = { brev, principal ->
            kanRedigere(brev, principal, tillatKlarmarkertBrev = false)
        }, block = block)

    /**
     * Endre brevets status — klarmarkering, distribusjonstype og liknende — uten å røre brevteksten.
     * Derfor er et klarmarkert brev greit, i motsetning til [forRedigering].
     *
     * Endringen er idempotent: er [trengerEndring] usann, er dette en no-op som verken krever
     * policy eller rører reservasjonen. Ellers frigis reservasjonen, siden en statusendring
     * avslutter saksbehandlers arbeid med brevet.
     */
    suspend fun forStatusendring(
        brevId: BrevId,
        saksId: SaksId,
        trengerEndring: (Brevredigering) -> Boolean,
        endre: suspend BrevScope.() -> Outcome<Unit, BrevredigeringError>,
    ): Outcome<Dto.BrevInfo, BrevredigeringError>? {
        reserver(brevId, saksId)?.onError { return failure(it) } ?: return null

        return transactional.rollbackOnFailure {
            val brev = BrevredigeringEntity.findByIdAndSaksId(brevId, saksId) ?: return@rollbackOnFailure null
            val scope = BrevScope(brev, brevreservasjonPolicy)

            if (trengerEndring(brev)) {
                kanRedigere(brev, PrincipalInContext.require(), tillatKlarmarkertBrev = true)
                    .onError { return@rollbackOnFailure failure(it) }

                brev.frigiReservasjon()
                scope.endre().onError { return@rollbackOnFailure failure(it) }
            }

            with(scope) { success(brev.tilBrevInfo()) }
        }
    }

    /**
     * Attester brevet. Krever både [AttesterBrevPolicy] og [RedigerBrevPolicy].
     */
    suspend fun <R> forAttestering(
        brevId: BrevId,
        saksId: SaksId,
        frigiReservasjon: Boolean,
        block: suspend BrevScope.() -> Outcome<R, BrevredigeringError>?,
    ): Outcome<R, BrevredigeringError>? =
        reservertOgSjekket(brevId, saksId, frigiReservasjon, sjekk = { brev, principal ->
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

        return transactional.rollbackOnFailure(Connection.TRANSACTION_REPEATABLE_READ) {
            val brev = BrevredigeringEntity.findByIdAndSaksId(brevId, saksId) ?: return@rollbackOnFailure null
            val document = brev.document ?: return@rollbackOnFailure null

            sendBrevPolicy.kanSende(brev, document).onError { return@rollbackOnFailure failure(it) }

            BrevScope(brev, brevreservasjonPolicy).block(document)
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
        reservertOgSjekket(brevId, saksId, frigiReservasjon = false, sjekk = { _, _ -> success(Unit) }, block = { block() })

    /**
     * Reserver brevet uten å gjøre noe mer. Kjører i egen transaksjon.
     */
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
