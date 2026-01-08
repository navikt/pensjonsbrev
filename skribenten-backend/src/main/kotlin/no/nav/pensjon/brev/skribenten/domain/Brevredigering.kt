package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.skribenten.db.BrevredigeringTable
import no.nav.pensjon.brev.skribenten.db.Document
import no.nav.pensjon.brev.skribenten.db.DocumentTable
import no.nav.pensjon.brev.skribenten.db.Mottaker
import no.nav.pensjon.brev.skribenten.db.MottakerTable
import no.nav.pensjon.brev.skribenten.db.P1Data
import no.nav.pensjon.brev.skribenten.db.P1DataTable
import no.nav.pensjon.brev.skribenten.db.ValgteVedlegg
import no.nav.pensjon.brev.skribenten.db.ValgteVedleggTable
import no.nav.pensjon.brev.skribenten.db.wrap
import no.nav.pensjon.brev.skribenten.db.writeHashTo
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.usecase.Result
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import java.time.Instant
import java.time.temporal.ChronoUnit

// TODO: Intensjonen er at de fleste vars her skal ha private setters
class Brevredigering(id: EntityID<Long>) : LongEntity(id) {
    var saksId by BrevredigeringTable.saksId

    // Det er forventet at vedtaksId kun har verdi om brevet er i vedtakskontekst
    var vedtaksId by BrevredigeringTable.vedtaksId
    var brevkode by BrevredigeringTable.brevkode
    var spraak by BrevredigeringTable.spraak
    var avsenderEnhetId by BrevredigeringTable.avsenderEnhetId
    var saksbehandlerValg by BrevredigeringTable.saksbehandlerValg
    var redigertBrev by BrevredigeringTable.redigertBrevKryptert.writeHashTo(BrevredigeringTable.redigertBrevKryptertHash)
    val redigertBrevHash by BrevredigeringTable.redigertBrevKryptertHash
    var laastForRedigering by BrevredigeringTable.laastForRedigering
    var distribusjonstype by BrevredigeringTable.distribusjonstype
    var redigeresAv by BrevredigeringTable.redigeresAvNavIdent
    var sistRedigertAv by BrevredigeringTable.sistRedigertAvNavIdent
    var opprettetAvNavIdent by BrevredigeringTable.opprettetAvNavIdent
    var opprettet by BrevredigeringTable.opprettet
    var sistredigert by BrevredigeringTable.sistredigert
    var sistReservert by BrevredigeringTable.sistReservert
    var journalpostId by BrevredigeringTable.journalpostId
    val document by Document.Companion referrersOn DocumentTable.brevredigering orderBy (DocumentTable.id to SortOrder.DESC)
    val mottaker by Mottaker.Companion optionalBackReferencedOn MottakerTable.id
    val p1Data by P1Data.Companion optionalBackReferencedOn P1DataTable.id
    val valgteVedlegg by ValgteVedlegg.Companion optionalBackReferencedOn ValgteVedleggTable.id
    var attestertAvNavIdent by BrevredigeringTable.attestertAvNavIdent
    var brevtype by BrevredigeringTable.brevtype

    companion object : LongEntityClass<Brevredigering>(BrevredigeringTable) {
        fun findByIdAndSaksId(id: Long, saksId: Long?) =
            if (saksId == null) {
                findById(id)
            } else {
                find { (BrevredigeringTable.id eq id) and (BrevredigeringTable.saksId eq saksId) }.firstOrNull()
            }
    }

    val isVedtaksbrev get() = brevtype == LetterMetadata.Brevtype.VEDTAKSBREV
    val reservasjon: Reservasjon?
        get() {
            val reservertAv = this.redigeresAv ?: return null
            val sistReservert = this.sistReservert ?: return null

            return Reservasjon(
                vellykket = true,
                reservertAv = reservertAv,
                timestamp = sistReservert,
                expiresIn = BrevreservasjonPolicy.timeout,
                redigertBrevHash = this.redigertBrevHash,
            )
        }

    fun reserver(
        fra: Instant,
        saksbehandler: NavIdent,
        policy: BrevreservasjonPolicy
    ): Result<Reservasjon, BrevreservasjonPolicy.ReservertAvAnnen> =
        policy.kanReservere(this, fra, saksbehandler).then {
            redigeresAv = saksbehandler
            sistReservert = fra.truncatedTo(ChronoUnit.MILLIS)
            return@then reservasjon!!
        }

    fun oppdaterRedigertBev(nyttRedigertbrev: Edit.Letter, av: NavIdent) {
        redigertBrev = nyttRedigertbrev
        sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
        sistRedigertAv = av
    }

    fun mergeRendretBrev(rendretBrev: LetterMarkup) {
        redigertBrev = redigertBrev.updateEditedLetter(rendretBrev)
    }
}