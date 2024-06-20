package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.db.Brevredigering
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.principal
import org.jetbrains.exposed.sql.transactions.transaction
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

    suspend fun <T : Any> opprettBrev(
        call: ApplicationCall,
        sak: Pen.SakSelection,
        brevkode: Brevkode.Redigerbar,
        saksbehandlerValg: BrevbakerBrevdata,
        mapper: Brevredigering.() -> T,
    ): ServiceResult<T> {
        val pesysData = hentPesysData(call = call, brevkode = brevkode, saksId = sak.saksId)

        return brevbakerService.renderLetter(
            call = call,
            brevkode = brevkode,
            brevdata = GeneriskRedigerbarBrevdata(
                pesysData = pesysData.brevdata,
                saksbehandlerValg = saksbehandlerValg
            ),
            felles = pesysData.felles
        ).map { letter ->
            transaction {
                Brevredigering.new {
                    saksId = sak.saksId
                    opprettetAvNavIdent = call.principal().navIdent
                    this.brevkode = brevkode.name
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
    }

    suspend fun <T : Any> oppdaterBrev(
        call: ApplicationCall,
        brevId: Long,
        brevkode: Brevkode.Redigerbar,
        saksbehandlerValg: BrevbakerBrevdata,
        redigertBrev: Edit.Letter,
        mapper: Brevredigering.() -> T,
    ): ServiceResult<T?> = coroutineScope {
        val eksisterende = transaction { Brevredigering.findById(brevId) }

        return@coroutineScope if (eksisterende != null) {
            val pesysData = hentPesysData(call = call, brevkode = brevkode, saksId = eksisterende.saksId)

            brevbakerService.renderLetter(
                call = call,
                brevkode = brevkode,
                brevdata = GeneriskRedigerbarBrevdata(
                    pesysData = pesysData.brevdata,
                    saksbehandlerValg = saksbehandlerValg,
                ),
                felles = pesysData.felles
            ).map { redigertBrev.updateEditedLetter(it) }
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

    private suspend fun hentPesysData(call: ApplicationCall, brevkode: Brevkode.Redigerbar, saksId: Long): BrevdataResponse.Data =
        coroutineScope {
            when (val response = penService.hentPesysBrevdata(call, saksId, brevkode.name)) {
                is ServiceResult.Ok -> {
                    if (response.result.data == null) {
                        throw BrevbakerServiceException("Brevdata fra PEN var tom")
                    }
                    BrevdataResponse.Data(
                        felles = response.result.data.felles,
                        brevdata = EmptyBrevdata,
                    )
                }

                is ServiceResult.Error -> {
                    throw BrevbakerServiceException(response.error)
                }
            }
        }

    fun <T : Any> hentBrev(brevId: Long, mapper: Brevredigering.() -> T): T? {
        return transaction {
            Brevredigering.findById(brevId)?.mapper()
        }
    }

    fun <T : Any> hentSaksbehandlersBrev(navIdent: String, mapper: Brevredigering.() -> T): List<T?> {
        return transaction {
            Brevredigering.find { BrevredigeringTable.opprettetAvNavIdent eq navIdent }.map(mapper)
        }
    }
}
