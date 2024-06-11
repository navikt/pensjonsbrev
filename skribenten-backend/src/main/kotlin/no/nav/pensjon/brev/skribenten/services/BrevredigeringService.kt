package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.skribenten.db.Brevredigering
import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.routes.GeneriskRedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class BrevredigeringService(private val brevbakerService: BrevbakerService) {

    suspend fun <T : Any> opprettBrev(
        call: ApplicationCall,
        sak: PenService.SakSelection,
        brevkode: Brevkode.Redigerbar,
        saksbehandlerValg: BrevbakerBrevdata,
        mapper: Brevredigering.() -> T,
    ): ServiceResult<T> {
        //TODO hent pesys data
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

    fun slettBrev(id: Long): ServiceResult<*> {
        return transaction {
            val brev = Brevredigering.findById(id)
            if (brev != null) {
                brev.delete()
                return@transaction ServiceResult.Ok("Brev med id: $id slettet")
            } else {
                return@transaction ServiceResult.Error("Kunne ikke finnne brev med id: $id", HttpStatusCode.NotFound)
            }
        }
    }

    //TODO implementer felter
    private fun hentPesysData(brevkode: Brevkode.Redigerbar, saksId: Long): PesysBrevdata = PesysBrevdata(
        Felles(
            dokumentDato = LocalDate.now(),
            saksnummer = "1234",
            avsenderEnhet = NAVEnhet("nav.no", "NAV Familie- og pensjonsytelser Porsgrunn", Telefonnummer("22225555")),
            bruker = Bruker(Foedselsnummer("12345678910"), "Test", null, "Testeson"),
            vergeNavn = null,
            signerendeSaksbehandlere = SignerendeSaksbehandlere("Ole Saksbehandler")
        ), EmptyBrevdata
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