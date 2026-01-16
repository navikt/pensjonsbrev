package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.db.Document
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.db.P1Data
import no.nav.pensjon.brev.skribenten.db.ValgteVedlegg
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.notYetStubbed
import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.jetbrains.exposed.dao.id.EntityID
import java.time.Instant

open class BrevredigeringStub : Brevredigering {
    override val id: EntityID<Long> get() = notYetStubbed()
    override val saksId: Long get() = notYetStubbed()
    override val vedtaksId: Long get() = notYetStubbed()
    override val brevkode: Brevkode.Redigerbart get() = notYetStubbed()
    override val spraak: LanguageCode get() = notYetStubbed()
    override val avsenderEnhetId: String get() = notYetStubbed()
    override val saksbehandlerValg: SaksbehandlerValg get() = notYetStubbed()
    override val redigertBrev: Edit.Letter get() = notYetStubbed()
    override val redigertBrevHash: Hash<Edit.Letter> get() = notYetStubbed()
    override val laastForRedigering: Boolean get() = notYetStubbed()
    override val distribusjonstype: Distribusjonstype get() = notYetStubbed()
    override val redigeresAv: NavIdent get() = notYetStubbed()
    override val sistRedigertAv: NavIdent get() = notYetStubbed()
    override val opprettetAv: NavIdent get() = notYetStubbed()
    override val opprettet: Instant get() = notYetStubbed()
    override val sistredigert: Instant get() = notYetStubbed()
    override val sistReservert: Instant get() = notYetStubbed()
    override val journalpostId: Long get() = notYetStubbed()
    override val document: Iterable<Document> get() = notYetStubbed()
    override val mottaker: Mottaker get() = notYetStubbed()
    override val p1Data: P1Data get() = notYetStubbed()
    override val valgteVedlegg: ValgteVedlegg get() = notYetStubbed()
    override val attestertAvNavIdent: NavIdent get() = notYetStubbed()
    override val brevtype: LetterMetadata.Brevtype get() = notYetStubbed()
    override val isVedtaksbrev: Boolean get() = notYetStubbed()
    override val reservasjon: Reservasjon get() = notYetStubbed()

    override fun reserver(fra: Instant, saksbehandler: NavIdent, policy: BrevreservasjonPolicy): Outcome<Reservasjon, BrevreservasjonPolicy.ReservertAvAnnen> {
        notYetStubbed()
    }

    override fun oppdaterRedigertBev(nyttRedigertbrev: Edit.Letter, av: NavIdent) {
        notYetStubbed()
    }

    override fun markerSomKlar() {
        notYetStubbed()
    }

    override fun markerSomKladd() {
        notYetStubbed()
    }

    override fun mergeRendretBrev(rendretBrev: LetterMarkup) {
        notYetStubbed()
    }

    override fun settMottaker(mottakerDto: Dto.Mottaker): Mottaker {
        notYetStubbed()
    }

    override fun fjernMottaker() {
        notYetStubbed()
    }

    override fun toDto(coverage: Set<LetterMarkupWithDataUsage.Property>?): Dto.Brevredigering {
        notYetStubbed()
    }

    override fun toBrevInfo(): Dto.BrevInfo {
        notYetStubbed()
    }
}