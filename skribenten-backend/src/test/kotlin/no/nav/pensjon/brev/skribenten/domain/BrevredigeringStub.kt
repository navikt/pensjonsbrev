package no.nav.pensjon.brev.skribenten.domain

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.db.P1Data
import no.nav.pensjon.brev.skribenten.db.ValgteVedlegg
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.JournalpostId
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.notYetStubbed
import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import java.time.Instant

open class BrevredigeringStub : Brevredigering {
    override val id: EntityID<BrevId> get() = notYetStubbed()
    override val saksId: SaksId get() = notYetStubbed()
    override val vedtaksId: VedtaksId get() = notYetStubbed()
    override val brevkode: Brevkode.Redigerbart get() = notYetStubbed()
    override val spraak: LanguageCode get() = notYetStubbed()
    override val avsenderEnhetId: EnhetId get() = notYetStubbed()
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
    override val journalpostId: JournalpostId get() = notYetStubbed()
    override var document: Dto.Document? = null
    override val mottaker: Mottaker get() = notYetStubbed()
    override val p1Data: P1Data get() = notYetStubbed()
    override val valgteVedlegg: ValgteVedlegg get() = notYetStubbed()
    override val attestertAvNavIdent: NavIdent get() = notYetStubbed()
    override val brevtype: LetterMetadata.Brevtype get() = notYetStubbed()
    override val isVedtaksbrev: Boolean get() = notYetStubbed()

    override fun gjeldendeReservasjon(policy: BrevreservasjonPolicy): Reservasjon? = notYetStubbed()
    override fun reserver(fra: Instant, saksbehandler: NavIdent, policy: BrevreservasjonPolicy): Outcome<Reservasjon, BrevreservasjonPolicy.ReservertAvAnnen> = notYetStubbed()
    override fun oppdaterRedigertBev(nyttRedigertbrev: Edit.Letter, av: NavIdent): Unit = notYetStubbed()
    override fun markerSomKlar(): Unit = notYetStubbed()
    override fun markerSomKladd(): Unit = notYetStubbed()
    override fun mergeRendretBrev(rendretBrev: LetterMarkup): Unit = notYetStubbed()
    override fun settMottaker(mottakerDto: Dto.Mottaker?, annenMottakerNavn: String?): Mottaker? = notYetStubbed()
    override fun toDto(brevreservasjonPolicy: BrevreservasjonPolicy, coverage: Set<LetterMarkupWithDataUsage.Property>?): Dto.Brevredigering = notYetStubbed()
    override fun toBrevInfo(brevreservasjonPolicy: BrevreservasjonPolicy): Dto.BrevInfo = notYetStubbed()
}