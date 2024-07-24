package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.toMarkup
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.Pen.SendRedigerbartBrevRequest
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.security.MessageDigest
import java.sql.Connection
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

data class GeneriskRedigerbarBrevdata(
    override val pesysData: BrevbakerBrevdata,
    override val saksbehandlerValg: BrevbakerBrevdata,
) : RedigerbarBrevdata<BrevbakerBrevdata, BrevbakerBrevdata>

class KanIkkeReservereBrevredigeringException(override val message: String) : RuntimeException(message)

// TODO: newSuspendedTransaction er blocking, så vi bør mest sannsynlig holde kall til andre tjenester utenfor transaksjoner.
class BrevredigeringService(
    private val brevbakerService: BrevbakerService,
    private val penService: PenService,
    private val navansattService: NavansattService,
) {
    companion object {
        val RESERVASJON_TIMEOUT = 10.minutes.toJavaDuration()
    }

    suspend fun opprettBrev(
        call: ApplicationCall,
        sak: Pen.SakSelection,
        brevkode: Brevkode.Redigerbar,
        spraak: LanguageCode,
        avsenderEnhetsId: String?,
        saksbehandlerValg: BrevbakerBrevdata,
        reserverForRedigering: Boolean = false,
    ): ServiceResult<Api.BrevResponse> =
        harTilgangTilEnhet(call, avsenderEnhetsId) {
            rendreBrev(call, brevkode, spraak, sak.saksId, saksbehandlerValg, avsenderEnhetsId).map { letter ->
                transaction {
                    Brevredigering.new {
                        saksId = sak.saksId
                        opprettetAvNavIdent = call.principal().navIdent
                        this.brevkode = brevkode
                        this.spraak = spraak
                        this.avsenderEnhetId = avsenderEnhetsId
                        this.saksbehandlerValg = saksbehandlerValg
                        laastForRedigering = false
                        redigeresAvNavIdent = if (reserverForRedigering) call.principal().navIdent else null
                        opprettet = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                        sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                        redigertBrev = letter.toEdit()
                        sistRedigertAvNavIdent = call.principal().navIdent
                    }.mapBrev()
                }
            }
        }

    suspend fun oppdaterBrev(
        call: ApplicationCall,
        brevId: Long,
        nyeSaksbehandlerValg: BrevbakerBrevdata,
        nyttRedigertbrev: Edit.Letter,
    ): ServiceResult<Api.BrevResponse>? =
        hentBrevMedReservasjon(call.principal(), brevId) { brev ->
            rendreBrev(call, brev.brevkode, brev.spraak, brev.saksId, nyeSaksbehandlerValg, brev.avsenderEnhetId)
                .map { nyttRedigertbrev.updateEditedLetter(it) }
                .map { oppdatertBrev ->
                    transaction {
                        brev.apply {
                            redigertBrev = oppdatertBrev
                            sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                            saksbehandlerValg = nyeSaksbehandlerValg
                            sistRedigertAvNavIdent = call.principal().navIdent
                        }
                    }.mapBrev()
                }
        }

    fun delvisOppdaterBrev(brevId: Long, patch: Api.DelvisOppdaterBrevRequest): Api.BrevResponse? =
        transaction {
            Brevredigering.findByIdAndUpdate(brevId) { brev ->
                patch.laastForRedigering?.also { brev.laastForRedigering = it }
            }
        }?.mapBrev()

    /**
     * Slett brev med id.
     * @return `true` om brevet ble slettet, false om brevet ikke eksisterer,
     */
    fun slettBrev(id: Long): Boolean {
        return transaction {
            val brev = Brevredigering.findById(id)
            if (brev != null) {
                brev.delete()
                true
            } else {
                false
            }
        }
    }

    suspend fun hentBrev(call: ApplicationCall, brevId: Long, reserverForRedigering: Boolean = false): ServiceResult<Api.BrevResponse>? =
        if (reserverForRedigering) {
            hentBrevMedReservasjon(call.principal(), brevId) { brev ->
                rendreBrev(call, brev.brevkode, brev.spraak, brev.saksId, brev.saksbehandlerValg, brev.avsenderEnhetId)
                    .map { brev.redigertBrev.updateEditedLetter(it) }
                    .map { transaction { brev.apply { redigertBrev = it }.mapBrev() } }
            }
        } else transaction { Brevredigering.findById(brevId)?.mapBrev() }?.let { ServiceResult.Ok(it) }

    fun hentBrevForSak(saksId: Long): List<Api.BrevInfo> {
        return transaction {
            Brevredigering.find { BrevredigeringTable.saksId eq saksId }.map(::mapBrevInfo)
        }
    }

    suspend fun fornyReservasjon(call: ApplicationCall, brevId: Long) {
        hentBrevMedReservasjon(call.principal(), brevId) { }
    }

    private suspend fun <T> hentBrevMedReservasjon(principal: UserPrincipal, brevId: Long, block: suspend (Brevredigering) -> T): T? {
        val brev = transaction(Connection.TRANSACTION_REPEATABLE_READ) {
            Brevredigering.findById(brevId)?.apply {
                if (redigeresAvNavIdent == null || erReservasjonUtloept()) {
                    redigeresAvNavIdent = principal.navIdent
                    sistReservert = Instant.now()
                }
            }
        }
        return brev?.let {
            if (brev.redigeresAvNavIdent == principal.navIdent) {
                block(brev)
            } else throw KanIkkeReservereBrevredigeringException("Brev er allerede reservert av: ${brev.redigeresAvNavIdent}")
        }
    }

    private suspend fun rendreBrev(
        call: ApplicationCall,
        brevkode: Brevkode.Redigerbar,
        spraak: LanguageCode,
        saksId: Long,
        saksbehandlerValg: BrevbakerBrevdata,
        avsenderEnhetsId: String?,
    ): ServiceResult<LetterMarkup> =
        penService.hentPesysBrevdata(call = call, saksId = saksId, brevkode = brevkode, avsenderEnhetsId = avsenderEnhetsId)
            .then { pesysData ->
                brevbakerService.renderMarkup(
                    call = call,
                    brevkode = brevkode,
                    spraak = spraak,
                    brevdata = GeneriskRedigerbarBrevdata(
                        pesysData = pesysData.brevdata,
                        saksbehandlerValg = saksbehandlerValg,
                    ),
                    felles = pesysData.felles
                )
            }

    private suspend fun <T> harTilgangTilEnhet(call: ApplicationCall, enhetsId: String?, then: suspend () -> ServiceResult<T>): ServiceResult<T> =
        (enhetsId?.let { navansattService.harTilgangTilEnhet(call, call.principal().navIdent, it) } ?: ServiceResult.Ok(true))
            .then { harTilgang ->
                if (harTilgang) {
                    then()
                } else ServiceResult.Error("Mangler tilgang til NavEnhet $enhetsId", HttpStatusCode.Forbidden)
            }

    private suspend fun opprettPdf(call: ApplicationCall, brevredigering: Brevredigering, redigertBrevHash: ByteArray): ServiceResult<ByteArray> {
        return penService.hentPesysBrevdata(
            call = call,
            saksId = brevredigering.saksId,
            brevkode = brevredigering.brevkode,
            avsenderEnhetsId = brevredigering.avsenderEnhetId
        ).then { pesysData ->
            brevbakerService.renderPdf(
                call = call,
                brevkode = brevredigering.brevkode,
                spraak = brevredigering.spraak,
                brevdata = GeneriskRedigerbarBrevdata(
                    pesysData = pesysData.brevdata,
                    saksbehandlerValg = brevredigering.saksbehandlerValg,
                ),
                felles = pesysData.felles,
                redigertBrev = brevredigering.redigertBrev.toMarkup()
            ).map {
                transaction {
                    val update: Document.() -> Unit = {
                        this.brevredigering = brevredigering
                        pdf = ExposedBlob(it.file)
                        dokumentDato = pesysData.felles.dokumentDato
                        this.redigertBrevHash = redigertBrevHash
                    }
                    Document.findSingleByAndUpdate(DocumentTable.brevredigering eq brevredigering.id, update)?.pdf?.bytes
                        ?: Document.new(update).pdf.bytes
                }
            }
        }
    }

    suspend fun hentEllerOpprettPdf(call: ApplicationCall, brevId: Long): ServiceResult<ByteArray>? {
        val (brevredigering, document) = transaction { Brevredigering.findById(brevId).let { it to it?.document?.firstOrNull() } }

        return brevredigering?.let {
            val currentHash = hashBrev(brevredigering.redigertBrev)

            if (document != null && document.redigertBrevHash.contentEquals(currentHash)) {
                ServiceResult.Ok(document.pdf.bytes)
            } else {
                opprettPdf(call, brevredigering, currentHash)
            }
        }
    }

    suspend fun sendBrev(call: ApplicationCall, brevId: Long): ServiceResult<Pen.BestillBrevResponse>? =
        newSuspendedTransaction {
            val brevredigering = Brevredigering.findById(brevId)
            val document = brevredigering?.document?.firstOrNull()

            if (document != null) {
                brevbakerService.getRedigerbarTemplate(call, brevredigering.brevkode).then {
                    penService.sendbrev(
                        call,
                        SendRedigerbartBrevRequest(
                            dokumentDato = document.dokumentDato,
                            saksId = brevredigering.saksId,
                            enhetId = brevredigering.avsenderEnhetId,
                            templateDescription = it,
                            brevkode = brevredigering.brevkode,
                            pdf = document.pdf.bytes,
                            eksternReferanseId = "skribenten:${brevredigering.id}",
                        )
                    )
                }.onOk {
                    if (it.journalpostId != null) {
                        document.delete()
                        brevredigering.delete()
                    }
                }
            } else null
        }

    private fun Brevredigering.mapBrev(): Api.BrevResponse =
        Api.BrevResponse(
            info = mapBrevInfo(this),
            redigertBrev = redigertBrev,
            saksbehandlerValg = saksbehandlerValg,
        )

    private fun mapBrevInfo(brev: Brevredigering): Api.BrevInfo = with(brev) {
        val redigeresAv = if (erReservasjonUtloept()) null else redigeresAvNavIdent
        Api.BrevInfo(
            id = id.value,
            opprettetAv = opprettetAvNavIdent,
            opprettet = opprettet,
            sistredigertAv = sistRedigertAvNavIdent,
            sistredigert = sistredigert,
            redigeresAv = redigeresAv,
            brevkode = brevkode,
            status = when {
                laastForRedigering -> Api.BrevStatus.Klar
                redigeresAv != null -> Api.BrevStatus.UnderRedigering(redigeresAv)
                else -> Api.BrevStatus.Kladd
            },
        )
    }

    private fun Brevredigering.erReservasjonUtloept(): Boolean =
        sistReservert?.plus(RESERVASJON_TIMEOUT)?.isBefore(Instant.now()) == true

    private val digest: MessageDigest = MessageDigest.getInstance("SHA3-256")
    private fun hashBrev(brev: Edit.Letter): ByteArray =
        digest.digest(databaseObjectMapper.writeValueAsBytes(brev))
            .also { assert(it.size == 32) { "SHA3-256 hash of redigertbrev was longer than 32 bytes: ${it.size}" } }
}
