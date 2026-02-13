package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import java.time.Instant
import java.time.temporal.ChronoUnit

interface Brevredigering {
    val id: EntityID<Long>
    val saksId: Long
    val vedtaksId: Long?
    val brevkode: Brevkode.Redigerbart
    val spraak: LanguageCode
    val avsenderEnhetId: EnhetId
    val saksbehandlerValg: SaksbehandlerValg
    val redigertBrev: Edit.Letter
    val redigertBrevHash: Hash<Edit.Letter>

    /**
     * Denne burde få et bedre navn.
     * Semantikken er at kladden er ferdig, og brevet er klart til sending eller attestring,
     * avhengig av om det er informasjonsbrev eller vedtaksbrev.
     */
    val laastForRedigering: Boolean
    val distribusjonstype: Distribusjonstype
    val redigeresAv: NavIdent?
    val sistRedigertAv: NavIdent
    val opprettetAv: NavIdent
    val opprettet: Instant
    val sistredigert: Instant
    val sistReservert: Instant?
    val journalpostId: Long?
    val document: Iterable<Document>
    val mottaker: Mottaker?
    val p1Data: P1Data?
    val valgteVedlegg: ValgteVedlegg?
    val attestertAvNavIdent: NavIdent?
    val brevtype: LetterMetadata.Brevtype
    val isVedtaksbrev: Boolean
    val reservasjon: Reservasjon?

    fun reserver(
        fra: Instant,
        saksbehandler: NavIdent,
        policy: BrevreservasjonPolicy
    ): Outcome<Reservasjon, BrevreservasjonPolicy.ReservertAvAnnen>

    fun oppdaterRedigertBev(nyttRedigertbrev: Edit.Letter, av: NavIdent)
    fun markerSomKlar()
    fun markerSomKladd()
    fun mergeRendretBrev(rendretBrev: LetterMarkup)
    fun settMottaker(mottakerDto: Dto.Mottaker?, annenMottakerNavn: String?): Mottaker?
    fun toDto(coverage: Set<LetterMarkupWithDataUsage.Property>?): Dto.Brevredigering
    fun toBrevInfo(): Dto.BrevInfo
}

class BrevredigeringEntity(id: EntityID<Long>) : LongEntity(id), Brevredigering {
    override var saksId by BrevredigeringTable.saksId
        private set
    // Det er forventet at vedtaksId kun har verdi om brevet er i vedtakskontekst
    override var vedtaksId by BrevredigeringTable.vedtaksId
        private set
    override var brevkode by BrevredigeringTable.brevkode
        private set
    override var spraak by BrevredigeringTable.spraak
        private set
    override var avsenderEnhetId by BrevredigeringTable.avsenderEnhetId
        private set
    override var saksbehandlerValg by BrevredigeringTable.saksbehandlerValg
    override var redigertBrev by BrevredigeringTable.redigertBrevKryptert.writeHashTo(BrevredigeringTable.redigertBrevKryptertHash)
    override val redigertBrevHash by BrevredigeringTable.redigertBrevKryptertHash
    override var laastForRedigering by BrevredigeringTable.laastForRedigering
        private set
    override var distribusjonstype by BrevredigeringTable.distribusjonstype
    override var redigeresAv by BrevredigeringTable.redigeresAvNavIdent
    override var sistRedigertAv by BrevredigeringTable.sistRedigertAvNavIdent
    override var opprettetAv by BrevredigeringTable.opprettetAvNavIdent
        private set
    override var opprettet by BrevredigeringTable.opprettet
        private set
    override var sistredigert by BrevredigeringTable.sistredigert
    override var sistReservert by BrevredigeringTable.sistReservert
    override var journalpostId by BrevredigeringTable.journalpostId
    override val document by Document referrersOn DocumentTable.brevredigering orderBy (DocumentTable.id to SortOrder.DESC)
    override val mottaker by Mottaker optionalBackReferencedOn MottakerTable.id
    override val p1Data by P1Data optionalBackReferencedOn P1DataTable.id
    override val valgteVedlegg by ValgteVedlegg optionalBackReferencedOn ValgteVedleggTable.id
    override var attestertAvNavIdent by BrevredigeringTable.attestertAvNavIdent
    override var brevtype by BrevredigeringTable.brevtype
        private set

    companion object : LongEntityClass<BrevredigeringEntity>(BrevredigeringTable) {
        fun findByIdAndSaksId(id: Long, saksId: Long?) =
            if (saksId == null) {
                findById(id)
            } else {
                find { (BrevredigeringTable.id eq id) and (BrevredigeringTable.saksId eq saksId) }.firstOrNull()
            }

        fun opprettBrev(
            saksId: Long,
            vedtaksId: Long?,
            opprettetAv: NavIdent,
            brevkode: Brevkode.Redigerbart,
            spraak: LanguageCode,
            avsenderEnhetId: EnhetId,
            saksbehandlerValg: SaksbehandlerValg,
            redigertBrev: Edit.Letter,
            brevtype: LetterMetadata.Brevtype,
            timestamp: Instant = Instant.now(),
            distribusjonstype: Distribusjonstype = Distribusjonstype.SENTRALPRINT,
        ): BrevredigeringEntity = new {
            this.saksId = saksId
            this.vedtaksId = vedtaksId
            this.opprettetAv = opprettetAv
            this.brevkode = brevkode
            this.spraak = spraak
            this.avsenderEnhetId = avsenderEnhetId
            this.saksbehandlerValg = saksbehandlerValg
            this.laastForRedigering = false
            this.distribusjonstype = distribusjonstype
            this.opprettet = timestamp
            this.sistredigert = timestamp
            this.sistRedigertAv = opprettetAv
            this.redigertBrev = redigertBrev
            this.brevtype = brevtype
        }
    }

    override val isVedtaksbrev get() = brevtype == LetterMetadata.Brevtype.VEDTAKSBREV

    // TODO: Vurder å ekstrahere dette som en egen entitet i egen tabell
    override val reservasjon: Reservasjon?
        get() {
            val reservertAv = this.redigeresAv ?: return null
            val sistReservert = this.sistReservert ?: return null

            return Reservasjon(
                vellykket = true,
                reservertAv = reservertAv,
                timestamp = sistReservert,
                expiresIn = BrevreservasjonPolicy.timeout,
                redigertBrevHash = this.redigertBrevHash,
            ).takeIf { BrevreservasjonPolicy.isValid(it, Instant.now()) }
        }

    override fun reserver(
        fra: Instant,
        saksbehandler: NavIdent,
        policy: BrevreservasjonPolicy
    ): Outcome<Reservasjon, BrevreservasjonPolicy.ReservertAvAnnen> =
        policy.kanReservere(this, fra, saksbehandler).then {
            redigeresAv = saksbehandler
            sistReservert = fra.truncatedTo(ChronoUnit.MILLIS)
            return@then reservasjon!!
        }

    override fun oppdaterRedigertBev(nyttRedigertbrev: Edit.Letter, av: NavIdent) {
        redigertBrev = nyttRedigertbrev
        sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
        sistRedigertAv = av
    }

    override fun markerSomKlar() {
        laastForRedigering = true
    }

    override fun markerSomKladd() {
        laastForRedigering = false
        attestertAvNavIdent = null
        redigertBrev = redigertBrev.withSignatur(attestant = null)
    }

    override fun mergeRendretBrev(rendretBrev: LetterMarkup) {
        redigertBrev = redigertBrev.updateEditedLetter(rendretBrev)
    }

    override fun settMottaker(mottakerDto: Dto.Mottaker?, annenMottakerNavn: String?): Mottaker? {
        redigertBrev = redigertBrev.withSakspart(annenMottakerNavn = annenMottakerNavn)

        return if (mottakerDto == null) {
            mottaker?.delete()
            null
        } else if (mottaker == null) {
            Mottaker.opprettMottaker(this, mottakerDto)
                // pga. optional backreference, så må vi oppdatere referansen til mottaker-tabellen
                .also { refresh(flush = true) }
        } else {
            mottaker?.oppdater(mottakerDto)
        }
    }

    override fun toDto(coverage: Set<LetterMarkupWithDataUsage.Property>?): Dto.Brevredigering =
        Dto.Brevredigering(
            info = toBrevInfo(),
            redigertBrev = redigertBrev,
            redigertBrevHash = redigertBrevHash,
            saksbehandlerValg = saksbehandlerValg,
            propertyUsage = coverage,
            valgteVedlegg = valgteVedlegg?.valgteVedlegg
        )

    override fun toBrevInfo(): Dto.BrevInfo =
        Dto.BrevInfo(
            id = id.value,
            saksId = saksId,
            vedtaksId = vedtaksId,
            opprettetAv = opprettetAv,
            opprettet = opprettet,
            sistredigertAv = sistRedigertAv,
            sistredigert = sistredigert,
            redigeresAv = reservasjon?.reservertAv,
            brevkode = brevkode,
            laastForRedigering = laastForRedigering,
            distribusjonstype = distribusjonstype,
            mottaker = mottaker?.toDto(),
            avsenderEnhetId = avsenderEnhetId,
            spraak = spraak,
            sistReservert = sistReservert,
            journalpostId = journalpostId,
            attestertAv = attestertAvNavIdent,
            status = when {
                journalpostId != null -> Dto.BrevStatus.ARKIVERT
                laastForRedigering && isVedtaksbrev ->
                    if (attestertAvNavIdent != null) {
                        Dto.BrevStatus.KLAR
                    } else {
                        Dto.BrevStatus.ATTESTERING
                    }

                laastForRedigering -> Dto.BrevStatus.KLAR

                else -> Dto.BrevStatus.KLADD
            }
        )
}