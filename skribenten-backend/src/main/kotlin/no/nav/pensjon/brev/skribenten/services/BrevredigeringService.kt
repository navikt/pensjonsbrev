package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.db.Brevredigering
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.db.Document
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.toMarkup
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class GeneriskRedigerbarBrevdata(
    override val pesysData: BrevbakerBrevdata,
    override val saksbehandlerValg: BrevbakerBrevdata
) : RedigerbarBrevdata<BrevbakerBrevdata, BrevbakerBrevdata>


class BrevredigeringService(
    private val brevbakerService: BrevbakerService,
    private val penService: PenService,
) {

    val logger: Logger = LoggerFactory.getLogger(BrevredigeringService::class.java)

    suspend fun <T : Any> opprettBrev(
        call: ApplicationCall,
        sak: Pen.SakSelection,
        brevkode: Brevkode.Redigerbar,
        spraak: LanguageCode,
        saksbehandlerValg: BrevbakerBrevdata,
        mapper: Brevredigering.() -> T,
    ): ServiceResult<T> =
        rendreBrev(call, brevkode, spraak, sak, saksbehandlerValg).map { letter ->
            transaction {
                Brevredigering.new {
                    saksId = sak.saksId
                    opprettetAvNavIdent = call.principal().navIdent
                    this.brevkode = brevkode
                    this.spraak = spraak
                    this.saksbehandlerValg = saksbehandlerValg
                    laastForRedigering = false
                    redigeresAvNavIdent = null
                    opprettet = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
                    sistredigert = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
                    redigertBrev = letter.toEdit()
                    sistRedigertAvNavIdent = call.principal().navIdent
                }.mapper()
            }
        }

    suspend fun <T : Any> oppdaterBrev(
        call: ApplicationCall,
        sak: Pen.SakSelection,
        brevId: Long,
        saksbehandlerValg: BrevbakerBrevdata,
        redigertBrev: Edit.Letter,
        mapper: Brevredigering.() -> T,
    ): ServiceResult<T?> {
        val eksisterende = transaction { Brevredigering.findById(brevId) }

        return if (eksisterende != null) {
            rendreBrev(call, eksisterende.brevkode, eksisterende.spraak, sak, saksbehandlerValg)
                .map { redigertBrev.updateEditedLetter(it) }
                .map { brev ->
                    transaction {
                        Brevredigering.findByIdAndUpdate(brevId) {
                            it.redigertBrev = brev
                            it.sistredigert = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
                            it.saksbehandlerValg = saksbehandlerValg
                            it.sistRedigertAvNavIdent = call.principal().navIdent
                        }?.mapper()
                    }
                }
        } else {
            ServiceResult.Error("Fant ikke brev med id: $brevId", HttpStatusCode.NotFound)
        }
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

    suspend fun <T : Any> hentBrev(call: ApplicationCall, sak: Pen.SakSelection, brevId: Long, mapper: Brevredigering.() -> T): ServiceResult<T?> =
        newSuspendedTransaction {
            val brev = Brevredigering.findById(brevId)

            if (brev != null) {
                rendreBrev(call, brev.brevkode, brev.spraak, sak, brev.saksbehandlerValg)
                    .map { brev.redigertBrev.updateEditedLetter(it) }
                    .map { brev.apply { redigertBrev = it }.mapper() }
            } else {
                ServiceResult.Ok(null)
            }
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
        saksbehandlerValg: BrevbakerBrevdata
    ): ServiceResult<LetterMarkup> {
        val pesysData = hentPesysData(call = call, brevkode = brevkode, saksId = sak.saksId)

        return brevbakerService.renderMarkup(
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

    private suspend fun hentPesysData(call: ApplicationCall, brevkode: Brevkode.Redigerbar, saksId: Long): BrevdataResponse.Data =
        when (val response = penService.hentPesysBrevdata(call, saksId, brevkode)) {
            is ServiceResult.Ok -> {
                response.result.data ?: throw BrevbakerServiceException("Brevdata fra PEN var tom. error: ${response.result.error}")
            }

            is ServiceResult.Error -> {
                throw BrevbakerServiceException(response.error)
            }
        }

    suspend fun ferdigstill(call: ApplicationCall, brevId: Long) {
        val brevredigering = transaction { Brevredigering[brevId] }

        val pesysData = hentPesysData(
            call = call,
            brevkode = brevredigering.brevkode,
            saksId = brevredigering.saksId
        )


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
        ).onOk {
            transaction {
                Document.new {
                    this.brevredigering = brevredigering
                    pdf = ExposedBlob(it.file)
                }
            }
        }.onError { error, statusCode ->
            logger.error("En feil oppstod under rendering av PDF: $error", statusCode)
            throw BrevbakerServiceException(error)
        }
    }

    fun hentPdf(brevId: Long): ByteArray? {
        return transaction {
            val brevredigering = Brevredigering.findById(brevId)
            brevredigering?.document?.firstOrNull()?.pdf?.bytes
        }
    }
}
