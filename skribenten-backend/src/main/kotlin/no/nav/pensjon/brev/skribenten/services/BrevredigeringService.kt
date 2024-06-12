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
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class GeneriskRedigerbarBrevdata(
    override val pesysData: BrevbakerBrevdata,
    override val saksbehandlerValg: BrevbakerBrevdata
) : RedigerbarBrevdata<BrevbakerBrevdata, BrevbakerBrevdata>


class BrevredigeringService(private val brevbakerService: BrevbakerService) {

    suspend fun <T : Any> opprettBrev(
        call: ApplicationCall,
        sak: Pen.SakSelection,
        brevkode: Brevkode.Redigerbar,
        saksbehandlerValg: BrevbakerBrevdata,
        mapper: Brevredigering.() -> T,
    ): ServiceResult<T> {
        val pesysData = hentPesysData(brevkode = brevkode, saksId = sak.saksId)

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
            val pesysData = hentPesysData(brevkode = brevkode, saksId = eksisterende.saksId)

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

    private fun hentPesysData(brevkode: Brevkode.Redigerbar, saksId: Long): PesysBrevdata = PesysBrevdata(
        //TODO faktisk hent pesys data

        felles = Felles(
            dokumentDato = LocalDate.now(),
            saksnummer = saksId.toString(),
            avsenderEnhet = NAVEnhet("nav.no", "NAV Familie- og pensjonsytelser Porsgrunn", Telefonnummer("22225555")),
            bruker = Bruker(Foedselsnummer("12345678910"), "Test", null, "Testeson"),
            vergeNavn = null,
            signerendeSaksbehandlere = SignerendeSaksbehandlere("Ole Saksbehandler")
        ),
        brevdata = EmptyBrevdata,
    )

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

    data class PesysBrevdata(val felles: Felles, val brevdata: BrevbakerBrevdata)
}
