package no.nav.pensjon.brev.skribenten.model

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.letter.Edit
import java.time.LocalDateTime

object Api {
    data class OpprettBrevRequest(
        val brevkode: Brevkode.Redigerbar,
        val saksbehandlerValg: BrevbakerBrevdata,
    )

    data class OppdaterBrevRequest(
        val brevkode: Brevkode.Redigerbar,
        val saksbehandlerValg: BrevbakerBrevdata,
        val redigertBrev: Edit.Letter,
    )

    data class BrevResponse(
        val info: BrevInfo,
        val redigertBrev: Edit.Letter,
        val saksbehandlerValg: BrevbakerBrevdata,
    )

    data class BrevInfo(
        val id: Long,
        val opprettetAv: String,
        val opprettet: LocalDateTime,
        val sistredigertAv: String,
        val sistredigert: LocalDateTime,
        val brevkode: Brevkode.Redigerbar,
        val redigeresAv: String?,
    )
}