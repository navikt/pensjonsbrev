package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.db.Brevredigering
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.db.Document
import no.nav.pensjon.brev.skribenten.db.DocumentTable
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.toMarkup
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.Pen.SendRedigerbartBrevRequest
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.temporal.ChronoUnit

data class GeneriskRedigerbarBrevdata(
    override val pesysData: BrevbakerBrevdata,
    override val saksbehandlerValg: BrevbakerBrevdata,
) : RedigerbarBrevdata<BrevbakerBrevdata, BrevbakerBrevdata>


class BrevredigeringService(
    private val brevbakerService: BrevbakerService,
    private val penService: PenService,
    private val navansattService: NavansattService,
) {

    val logger: Logger = LoggerFactory.getLogger(BrevredigeringService::class.java)

    suspend fun <T : Any> opprettBrev(
        call: ApplicationCall,
        sak: Pen.SakSelection,
        brevkode: Brevkode.Redigerbar,
        spraak: LanguageCode,
        avsenderEnhetsId: String?,
        saksbehandlerValg: BrevbakerBrevdata,
        mapper: Brevredigering.() -> T,
    ): ServiceResult<T> =
        harTilgangTilEnhet(call, avsenderEnhetsId) {
            rendreBrev(call, brevkode, spraak, sak, saksbehandlerValg, avsenderEnhetsId).map { letter ->
                transaction {
                    Brevredigering.new {
                        saksId = sak.saksId
                        opprettetAvNavIdent = call.principal().navIdent
                        this.brevkode = brevkode
                        this.spraak = spraak
                        this.avsenderEnhetId = avsenderEnhetsId
                        this.saksbehandlerValg = saksbehandlerValg
                        laastForRedigering = false
                        redigeresAvNavIdent = null
                        opprettet = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                        sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                        redigertBrev = letter.toEdit()
                        sistRedigertAvNavIdent = call.principal().navIdent
                    }.mapper()
                }
            }
        }

    suspend fun <T : Any> oppdaterBrev(
        call: ApplicationCall,
        sak: Pen.SakSelection,
        brevId: Long,
        nyeSaksbehandlerValg: BrevbakerBrevdata,
        nyttRedigertbrev: Edit.Letter,
        mapper: Brevredigering.() -> T,
    ): ServiceResult<T>? =
        newSuspendedTransaction {
            val brevredigering = Brevredigering.findById(brevId)

            if (brevredigering != null) {
                rendreBrev(call, brevredigering.brevkode, brevredigering.spraak, sak, nyeSaksbehandlerValg, brevredigering.avsenderEnhetId)
                    .map { nyttRedigertbrev.updateEditedLetter(it) }
                    .map { oppdatertBrev ->
                        brevredigering.apply {
                            redigertBrev = oppdatertBrev
                            sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
                            saksbehandlerValg = nyeSaksbehandlerValg
                            sistRedigertAvNavIdent = call.principal().navIdent
                        }.mapper()
                    }
            } else null
        }

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

    suspend fun <T : Any> hentBrev(call: ApplicationCall, sak: Pen.SakSelection, brevId: Long, mapper: Brevredigering.() -> T): ServiceResult<T>? =
        newSuspendedTransaction {
            val brev = Brevredigering.findById(brevId)

            if (brev != null) {
                rendreBrev(call, brev.brevkode, brev.spraak, sak, brev.saksbehandlerValg, brev.avsenderEnhetId)
                    .map { brev.redigertBrev.updateEditedLetter(it) }
                    .map { brev.apply { redigertBrev = it }.mapper() }
            } else null
        }

    fun <T : Any> hentBrevForSak(saksId: Long, mapper: Brevredigering.() -> T): List<T> {
        return transaction {
            Brevredigering.find { BrevredigeringTable.saksId eq saksId }.map(mapper)
        }
    }

    private suspend fun rendreBrev(
        call: ApplicationCall,
        brevkode: Brevkode.Redigerbar,
        spraak: LanguageCode,
        sak: Pen.SakSelection,
        saksbehandlerValg: BrevbakerBrevdata,
        avsenderEnhetsId: String?,
    ): ServiceResult<LetterMarkup> =
        penService.hentPesysBrevdata(call = call, saksId = sak.saksId, brevkode = brevkode, avsenderEnhetsId = avsenderEnhetsId)
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

    suspend fun opprettPdf(call: ApplicationCall, brevId: Long): ServiceResult<ByteArray>? {
        val brevredigering = transaction { Brevredigering.findById(brevId) }

        return if (brevredigering != null) {
            penService.hentPesysBrevdata(
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
                        }
                        Document.findSingleByAndUpdate(DocumentTable.brevredigering eq brevId, update)?.pdf?.bytes
                            ?: Document.new(update).pdf.bytes
                    }
                }
            }
        } else null
    }

    fun hentPdf(brevId: Long): ByteArray? {
        return transaction {
            val brevredigering = Brevredigering.findById(brevId)
            brevredigering?.document?.firstOrNull()?.pdf?.bytes
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

}
