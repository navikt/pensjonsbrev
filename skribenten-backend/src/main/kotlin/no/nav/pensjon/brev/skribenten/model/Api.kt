package no.nav.pensjon.brev.skribenten.model

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.letter.Edit
import java.time.LocalDateTime
import no.nav.pensjon.brev.skribenten.services.LetterMetadata
import no.nav.pensjon.brev.skribenten.services.SpraakKode

object Api {
    class GeneriskBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata

    data class OpprettBrevRequest(
        val brevkode: Brevkode.Redigerbar,
        val spraak: SpraakKode,
        val avsenderEnhet: String?,
        val saksbehandlerValg: GeneriskBrevdata,
    )

    data class OppdaterBrevRequest(
        val saksbehandlerValg: GeneriskBrevdata,
        val redigertBrev: Edit.Letter,
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
    data class BrevResponse(
        val info: BrevInfo,
        val redigertBrev: Edit.Letter,
        val saksbehandlerValg: BrevbakerBrevdata,
    )

    data class SakContext(
        val sak: Pen.SakSelection,
        val brevMetadata: List<LetterMetadata>
    )

    data class BestillDoksysBrevRequest(
        val brevkode: String,
        val spraak: SpraakKode,
        val vedtaksId: Long? = null,
        val enhetsId: String,
    )

    data class BestillExstreamBrevRequest(
        val brevkode: String,
        val spraak: SpraakKode,
        val isSensitive: Boolean,
        val vedtaksId: Long? = null,
        val idTSSEkstern: String? = null,
        val brevtittel: String? = null,
        val enhetsId: String,
    )
    data class BestillEblankettRequest(
        val brevkode: String,
        val landkode: String,
        val mottakerText: String,
        val isSensitive: Boolean,
        val enhetsId: String,
    )

    data class BestillOgRedigerBrevResponse(
        val url: String? = null,
        val journalpostId: String? = null,
        val failureType: FailureType? = null,
    ) {
        enum class FailureType {
            DOKSYS_BESTILLING_ADDRESS_NOT_FOUND,
            DOKSYS_BESTILLING_INTERNAL_SERVICE_CALL_FAILIURE,
            DOKSYS_BESTILLING_PERSON_NOT_FOUND,
            DOKSYS_BESTILLING_TPS_CALL_FAILIURE,
            DOKSYS_BESTILLING_UNAUTHORIZED,
            DOKSYS_BESTILLING_UNEXPECTED_DOKSYS_ERROR,
            EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT,
            EXSTREAM_REDIGERING_GENERELL,
            FERDIGSTILLING_TIMEOUT,
            SAF_ERROR,
            SKRIBENTEN_INTERNAL_ERROR,
            ENHET_UNAUTHORIZED,
            NAVANSATT_MANGLER_NAVN,
        }
    }
}